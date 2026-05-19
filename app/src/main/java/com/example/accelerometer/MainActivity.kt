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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAccelerometerValues = findViewById(R.id.tvAccelerometerValues)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

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
}