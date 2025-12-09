package com.lr3_428_09;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lr3_428_09.adapter.DayGroupAdapter;
import com.lr3_428_09.database.DbHelper;
import com.lr3_428_09.database.ScheduleDao;
import com.lr3_428_09.model.DayGroup;
import com.lr3_428_09.model.ScheduleItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private Spinner spinnerWeekType;
    private FloatingActionButton fabAddLesson;
    private FloatingActionButton fabAdmin;

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
        ArrayAdapter<WeekType> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_bold, WeekType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekType.setAdapter(adapter);

        fabAddLesson = findViewById(R.id.fabAddLesson);
        fabAdmin = findViewById(R.id.fabAdmin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Обработчик для кнопки добавления пары
        fabAddLesson.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LessonEditActivity.class);
            startActivity(intent);
        });

        fabAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdministrationActivity.class);
            startActivity(intent);
        });

        DbHelper dbHelper = new DbHelper(this);

        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            scheduleDao = new ScheduleDao(db);

            spinnerWeekType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setupRecyclerView();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(MainActivity.this, "Ничего не выбрано", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании базы", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<DayGroup> dayGroups;
        dayGroups = getListDaysGroup(scheduleDao, spinnerWeekType.getSelectedItemPosition());
        DayGroupAdapter dayGroupAdapter = new DayGroupAdapter(dayGroups);
        recyclerView.setAdapter(dayGroupAdapter);
    }

    private List<DayGroup> getListDaysGroup(ScheduleDao scheduleDao, int weekType) {
        DayOfWeek[] daysOfWeek = DayOfWeek.values();

        List<DayGroup> groups = new ArrayList<>();
        Cursor cursor = scheduleDao.getAllLessons(weekType);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String dow = daysOfWeek[cursor.getInt(1)].getName();
            int number = cursor.getInt(2);
            String classroom = cursor.getString(3);
            String lesson_name = cursor.getString(4);
            String teacher_name = cursor.getString(5);
            String lesson_type = cursor.getString(6);

            ScheduleItem item = new ScheduleItem(id, number, WeekType.values()[weekType].getName(),
                    dow, lesson_name, lesson_type, teacher_name, classroom);

            DayGroup group = groups.stream()
                    .filter(a -> a.getDayName().equals(dow))
                    .findAny()
                    .orElse(null);
            if (group != null) {
                group.getLessons().add(item);
            } else {
                groups.add(new DayGroup(dow, new ArrayList<>()));
                groups.get(groups.size() - 1).getLessons().add(item);
            }
        }
        return groups;
    }
}