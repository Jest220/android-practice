package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SensorsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_list);

        ListView listView = findViewById(R.id.listSensors);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager != null ? sensorManager.getSensorList(Sensor.TYPE_ALL) : new ArrayList<>();

        List<String> items = new ArrayList<>();
        for (Sensor s : sensors) {
            String item = s.getName() + "\nТип: " + s.getStringType() +
                    "\nВендор: " + s.getVendor() +
                    "\nДиапазон: " + s.getMaximumRange() +
                    "\nТочность: " + s.getResolution() +
                    "\nПитание (мА): " + s.getPower();
            items.add(item);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }
}
