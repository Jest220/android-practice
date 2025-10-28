package com.lr3_428_09;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lr3_428_09.database.DbHelper;
import com.lr3_428_09.database.ScheduleDao;
import com.lr3_428_09.database.WeekTypeDao;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private WeekTypeDao weekTypeDao;
    private Spinner spinnerWeekType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerWeekType = findViewById(R.id.spinnerWeekType);

        dbHelper = new DbHelper(this);

        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            scheduleDao = new ScheduleDao(db);
            weekTypeDao = new WeekTypeDao(db);

            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Cursor cursor = weekTypeDao.getAllWeekTypes();
            while (cursor.moveToNext()) {
                adapter.add(cursor.getString(0));
            }
            cursor.close();
            spinnerWeekType.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании базы", Toast.LENGTH_LONG).show();
        }
    }
}