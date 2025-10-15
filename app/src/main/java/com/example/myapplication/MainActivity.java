package com.example.myapplication;

import android.content.Context; // Импортируем Context
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView; // Импортируем TextView

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Добавляем "implements SensorEventListener"
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Объявляем переменные для SensorManager, Sensor и TextView
    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;
    private TextView accelerationTextView; // Для отображения данных
    private Button btnListSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Находим TextView в вашей разметке (activity_main.xml)
        accelerationTextView = findViewById(R.id.acceleration_text_view);
        btnListSensors = findViewById(R.id.btnListSensors);
        // Вызов нового активити со списком сенсоров
        btnListSensors.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, SensorsListActivity.class));
        });

        // Получаем экземпляр SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Получаем датчик линейного ускорения
        if (sensorManager != null) {
            linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }

        // Проверяем, доступен ли датчик
        if (linearAccelerationSensor == null) {
            accelerationTextView.setText(R.string.unavailableSensor);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Этот метод вызывается, когда активность становится видимой пользователю
    @Override
    protected void onResume() {
        super.onResume();
        // Регистрируем слушателя для датчика, если он доступен
        if (linearAccelerationSensor != null && sensorManager != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Этот метод вызывается, когда активность приостанавливается (например, сворачивается)
    @Override
    protected void onPause() {
        super.onPause();
        // Отменяем регистрацию слушателя
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    // Этот метод вызывается, когда датчик передает новые данные
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Проверяем, что событие пришло от нашего датчика линейного ускорения
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Получаем значения ускорения по осям X, Y, Z
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Формируем строку для отображения
            String accelerationText = "Линейное ускорение:\n" +
                    "X: " + String.format("%.2f", x) + " м/с²\n" +
                    "Y: " + String.format("%.2f", y) + " м/с²\n" +
                    "Z: " + String.format("%.2f", z) + " м/с²";

            // Отображаем данные в TextView
            accelerationTextView.setText(accelerationText);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
