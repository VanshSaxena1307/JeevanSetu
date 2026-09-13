package com.example.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object MapMarkerHelper {

    fun createUserMarker(context: Context): Drawable {
        val size = 64
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x402DBE9B.toInt() // Translucent Mint Primary pulse ring
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 2f, outerPaint)

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF2DBE9B.toInt() // Mint Primary
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2.6f, strokePaint)

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt() // White core
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 3.5f, fillPaint)

        val centerDot = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF168F78.toInt() // Mint Deep
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 6f, centerDot)

        return BitmapDrawable(context.resources, bitmap)
    }

    fun createLocationPin(
        context: Context,
        bgColor: Int,
        label: String
    ): Drawable {
        val width = 72
        val height = 88
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Drop shadow
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x30000000
            style = Paint.Style.FILL
        }
        canvas.drawOval(RectF(18f, height - 12f, width - 18f, height - 2f), shadowPaint)

        // Pin body path (teardrop)
        val pinPath = Path().apply {
            val r = width / 2f - 4f
            val cx = width / 2f
            val cy = r + 4f
            arcTo(RectF(cx - r, cy - r, cx + r, cy + r), 140f, 260f, false)
            lineTo(cx, height - 12f)
            close()
        }

        val pinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
            style = Paint.Style.FILL
        }
        canvas.drawPath(pinPath, pinPaint)

        // Pin border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
        }
        canvas.drawPath(pinPath, borderPaint)

        // Inner white circle
        val innerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt()
            style = Paint.Style.FILL
        }
        val innerRadius = 16f
        val innerCy = width / 2f
        canvas.drawCircle(width / 2f, innerCy, innerRadius, innerCirclePaint)

        // Text icon/symbol
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
            textSize = 20f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
        val yOffset = (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(label, width / 2f, innerCy - yOffset, textPaint)

        return BitmapDrawable(context.resources, bitmap)
    }
}
