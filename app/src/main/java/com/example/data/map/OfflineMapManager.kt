package com.example.data.map

import android.content.Context
import android.os.StatFs
import com.example.data.local.db.MapRegionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.osmdroid.config.Configuration
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.math.PI
import kotlin.math.asinh
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.tan

import java.util.zip.ZipInputStream
import kotlinx.coroutines.delay

object OfflineMapManager {

    private const val USER_AGENT = "JeevanSetu-DisasterResponse/1.0 (Android; Humanitarian-Offline)"
    private const val MIN_REQUIRED_STORAGE_BYTES = 50L * 1024L * 1024L // 50 MB

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Initializes osmdroid configuration, sets private internal storage directories,
     * and extracts any preloaded offline tile archives from assets into the osmdroid base path.
     */
    fun initOsmdroid(context: Context) {
        val basePath = File(context.filesDir, "osmdroid").apply { if (!exists()) mkdirs() }
        val tileCache = File(context.cacheDir, "osmdroid/tiles").apply { if (!exists()) mkdirs() }

        val config = Configuration.getInstance()
        config.osmdroidBasePath = basePath
        config.osmdroidTileCache = tileCache
        config.userAgentValue = USER_AGENT

        // Copy bundled offline map archives from assets if not already copied or if updated
        extractBundledAssetsIfMissing(context, basePath)
    }

    private fun extractBundledAssetsIfMissing(context: Context, targetDir: File) {
        try {
            val assetList = context.assets.list("offline_maps") ?: return
            val tileCacheBase = File(context.cacheDir, "osmdroid/tiles/Mapnik")
            val persistentCacheBase = File(context.filesDir, "osmdroid/tiles/Mapnik")
            tileCacheBase.mkdirs()
            persistentCacheBase.mkdirs()

            for (assetName in assetList) {
                if (assetName.endsWith(".zip") || assetName.endsWith(".sqlite") || assetName.endsWith(".mbtiles")) {
                    val destFile = File(targetDir, assetName)
                    val assetBytes = context.assets.open("offline_maps/$assetName").use { it.readBytes() }

                    // 1. Copy archive file to osmdroid base directory for MapTileFileArchiveProvider
                    if (!destFile.exists() || destFile.length() != assetBytes.size.toLong()) {
                        FileOutputStream(destFile).use { output ->
                            output.write(assetBytes)
                        }
                    }

                    // 2. Also extract individual tiles to filesystem cache for MapTileFilesystemProvider
                    if (assetName.endsWith(".zip")) {
                        try {
                            ZipInputStream(assetBytes.inputStream()).use { zipIn ->
                                var entry = zipIn.nextEntry
                                while (entry != null) {
                                    if (!entry.isDirectory && entry.name.endsWith(".png")) {
                                        val relPath = entry.name.removePrefix("Mapnik/").removePrefix("/")
                                        val cacheTile = File(tileCacheBase, "$relPath.tile")
                                        val persistentTile = File(persistentCacheBase, "$relPath.tile")
                                        val tileBytes = zipIn.readBytes()
                                        if (!cacheTile.exists() || cacheTile.length() != tileBytes.size.toLong()) {
                                            cacheTile.parentFile?.mkdirs()
                                            persistentTile.parentFile?.mkdirs()
                                            FileOutputStream(cacheTile).use { it.write(tileBytes) }
                                            FileOutputStream(persistentTile).use { it.write(tileBytes) }
                                        }
                                    }
                                    zipIn.closeEntry()
                                    entry = zipIn.nextEntry
                                }
                            }
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Assets extraction failed gracefully without blocking startup
        }
    }

    /**
     * Calculates the total size in bytes of offline map tiles and stored packages.
     */
    fun getOfflineStorageBytes(context: Context): Long {
        var total = 0L
        val basePath = File(context.filesDir, "osmdroid")
        val cachePath = File(context.cacheDir, "osmdroid")
        val jsonPath = File(context.filesDir, "offline_maps")

        total += getFolderSize(basePath)
        total += getFolderSize(cachePath)
        total += getFolderSize(jsonPath)
        return total
    }

    private fun getFolderSize(dir: File): Long {
        if (!dir.exists()) return 0L
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getFolderSize(file) else file.length()
        }
        return size
    }

    /**
     * Returns the available storage in bytes on the internal data partition.
     */
    fun getAvailableStorageBytes(context: Context): Long {
        return try {
            val stat = StatFs(context.filesDir.absolutePath)
            stat.availableBytes
        } catch (_: Exception) {
            1024L * 1024L * 1024L // 1 GB fallback
        }
    }

    /**
     * Downloads offline map tiles for a specified geographic region across zoom levels 11 to 14.
     * Reports progression from 1% to 100%.
     */
    suspend fun downloadRegionTiles(
        context: Context,
        region: MapRegionEntity,
        onProgress: suspend (Int) -> Unit
    ): Result<Unit> = withContext(Dispatchers.IO) {
        // 1. Verify available storage
        val available = getAvailableStorageBytes(context)
        if (available < MIN_REQUIRED_STORAGE_BYTES) {
            return@withContext Result.failure(
                IllegalStateException("Insufficient storage space. Need at least 50 MB free space.")
            )
        }

        // 2. Compute bounding coordinates
        val centerLat = region.centerLat
        val centerLng = region.centerLng
        val radiusKm = region.radiusKm.coerceIn(5.0, 50.0)

        val latDelta = radiusKm / 111.0
        val lngDelta = radiusKm / (111.0 * cos(Math.toRadians(centerLat)).coerceAtLeast(0.1))

        val minLat = centerLat - latDelta
        val maxLat = centerLat + latDelta
        val minLng = centerLng - lngDelta
        val maxLng = centerLng + lngDelta

        // 3. Collect tile coordinates for zoom levels 11 to 14 (ideal offline tactical detail)
        data class TileCoord(val z: Int, val x: Int, val y: Int)
        val tilesToDownload = mutableListOf<TileCoord>()

        for (z in 11..14) {
            val (xMin, yMax) = deg2tile(minLat, minLng, z)
            val (xMax, yMin) = deg2tile(maxLat, maxLng, z)

            val xStart = min(xMin, xMax)
            val xEnd = max(xMin, xMax)
            val yStart = min(yMin, yMax)
            val yEnd = max(yMin, yMax)

            for (x in xStart..xEnd) {
                for (y in yStart..yEnd) {
                    tilesToDownload.add(TileCoord(z, x, y))
                }
            }
        }

        val totalTiles = tilesToDownload.size.coerceAtLeast(1)
        var downloadedCount = 0

        val tileCacheBase = File(context.cacheDir, "osmdroid/tiles/Mapnik")
        val persistentCacheBase = File(context.filesDir, "osmdroid/tiles/Mapnik")
        if (!tileCacheBase.exists()) tileCacheBase.mkdirs()
        if (!persistentCacheBase.exists()) persistentCacheBase.mkdirs()

        // 4. Progressive tile download loop
        for (tile in tilesToDownload) {
            val tileFile = File(tileCacheBase, "${tile.z}/${tile.x}/${tile.y}.png.tile")
            val persistentFile = File(persistentCacheBase, "${tile.z}/${tile.x}/${tile.y}.png.tile")

            if (!tileFile.exists() || tileFile.length() == 0L) {
                try {
                    val url = "https://tile.openstreetmap.org/${tile.z}/${tile.x}/${tile.y}.png"
                    val request = Request.Builder()
                        .url(url)
                        .header("User-Agent", USER_AGENT)
                        .build()

                    val response = httpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        response.body?.byteStream()?.use { input ->
                            tileFile.parentFile?.mkdirs()
                            persistentFile.parentFile?.mkdirs()

                            val bytes = input.readBytes()
                            FileOutputStream(tileFile).use { it.write(bytes) }
                            FileOutputStream(persistentFile).use { it.write(bytes) }
                        }
                    }
                } catch (e: Exception) {
                    // Continue with remaining tiles even if an individual tile fails
                }
            }

            downloadedCount++
            val progress = ((downloadedCount.toDouble() / totalTiles.toDouble()) * 100).toInt().coerceIn(1, 100)
            if (downloadedCount % 5 == 0 || downloadedCount == totalTiles) {
                onProgress(progress)
            }
        }

        onProgress(100)
        return@withContext Result.success(Unit)
    }

    /**
     * Deletes stored offline data for a region, including:
     *  - Exported JSON file if available
     *  - Cached tile files in osmdroid filesystem cache for the region's bounding box
     */
    suspend fun deleteRegionData(context: Context, region: MapRegionEntity): Unit = withContext(Dispatchers.IO) {
        try {
            // Delete exported JSON if available
            region.exportedFilePath?.let { path ->
                val f = File(path)
                if (f.exists()) f.delete()
            }

            val sanitizedName = region.regionName
                .replace(Regex("[^a-zA-Z0-9_-]"), "_")
                .trim('_')
            val localJsonFile = File(File(context.filesDir, "offline_maps"), "DisasterGuard_Map_${sanitizedName}_Offline.json")
            if (localJsonFile.exists()) {
                localJsonFile.delete()
            }

            // Delete cached OSM tiles for this region (zoom 11–14)
            val tileCacheBase = File(context.cacheDir, "osmdroid/tiles/Mapnik")
            val persistentCacheBase = File(context.filesDir, "osmdroid/tiles/Mapnik")

            val latDelta = region.radiusKm / 111.0
            val lngDelta = region.radiusKm / (111.0 * cos(Math.toRadians(region.centerLat)).coerceAtLeast(0.1))

            for (z in 11..14) {
                val (xMin, yMax) = deg2tile(region.centerLat - latDelta, region.centerLng - lngDelta, z)
                val (xMax, yMin) = deg2tile(region.centerLat + latDelta, region.centerLng + lngDelta, z)

                for (x in min(xMin, xMax)..max(xMin, xMax)) {
                    for (y in min(yMin, yMax)..max(yMin, yMax)) {
                        File(tileCacheBase, "$z/$x/${y}.png.tile").takeIf { it.exists() }?.delete()
                        File(persistentCacheBase, "$z/$x/${y}.png.tile").takeIf { it.exists() }?.delete()
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    fun deg2tile(lat: Double, lon: Double, zoom: Int): Pair<Int, Int> {
        val latRad = Math.toRadians(lat)
        val n = 1 shl zoom
        val x = floor((lon + 180.0) / 360.0 * n).toInt()
        val y = floor((1.0 - asinh(tan(latRad)) / PI) / 2.0 * n).toInt()
        return Pair(x.coerceIn(0, n - 1), y.coerceIn(0, n - 1))
    }
}
