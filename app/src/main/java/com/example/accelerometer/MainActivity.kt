package com.example.accelerometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private lateinit var tvAccelerometerValues: TextView
    private lateinit var slopeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAccelerometerValues = findViewById(R.id.tvAccelerometerValues)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        slopeText = findViewById(R.id.slopeText)

        if (accelerometerSensor == null) {
            tvAccelerometerValues.text = "Акселерометр відсутній на цьому пристрої"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // абстр. клас просить, щоб було, але метод не потрібен
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // Акселерометр рахує 3 значення: нахил телефона по осях X, Y та Z
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                tvAccelerometerValues.text = String.format("X: %.1f\nY: %.1f\nZ: %.1f", x, y, z)
                slopeText.text = slopeText(x, y, z)
            }
        }
    }

    // Вмикаємо відстеження датчиків, коли додаток стає активним
    override fun onResume() {
        super.onResume()
        accelerometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Вимикаємо відстеження, коли додаток згортається, щоб не садити батарею телефона
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // текстова підказка для різноманітності та цікавості)
    fun slopeText(x: Float, y: Float, z: Float): String {
        var text = ""

        if (x <= -4) {
            text += "Нахилено вправо\n"
        } else if (x >= 4) {
            text += "Нахилено вліво\n"
        }

        if (y <= -4) {
            text += "Шапкою вниз\n"
        } else if (y >= 4) {
            text += "Шапкою вгору\n"
        }

        if (z <= -7) {
            text += "Екраном донизу"
        } else if (z >= 7) {
            text += "Екраном догори"
        }

        return text
    }
}