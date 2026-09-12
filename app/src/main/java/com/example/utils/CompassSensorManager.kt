package com.example.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CompassSensorManager(context: Context) {
    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    val heading: Flow<Float> = callbackFlow {
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        var lastAccel: FloatArray? = null
        var lastMag: FloatArray? = null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        var azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                        azimuthDeg = (azimuthDeg + 360f) % 360f
                        trySend(azimuthDeg)
                    }
                    Sensor.TYPE_ACCELEROMETER -> {
                        lastAccel = event.values.clone()
                        computeAzimuth()
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        lastMag = event.values.clone()
                        computeAzimuth()
                    }
                }
            }

            private fun computeAzimuth() {
                val accel = lastAccel ?: return
                val mag = lastMag ?: return
                if (SensorManager.getRotationMatrix(rotationMatrix, null, accel, mag)) {
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    var azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                    azimuthDeg = (azimuthDeg + 360f) % 360f
                    trySend(azimuthDeg)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelSensor?.let { sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
            magSensor?.let { sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        }

        // Default initial heading north
        trySend(0f)

        awaitClose {
            sensorManager?.unregisterListener(listener)
        }
    }
}
