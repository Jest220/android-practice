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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DayGroupAdapter dayGroupAdapter;
    private DbHelper dbHelper;
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

        dbHelper = new DbHelper(this);

        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            scheduleDao = new ScheduleDao(db);

            ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item_bold,
                    new String[]{"Нечетная", "Четная"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerWeekType.setAdapter(adapter);

            spinnerWeekType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<DayGroup> dayGroups;
                    switch (position) {
                        case 0:
                            dayGroups = getListDaysGroup(scheduleDao, "Нечетная");
                            break;
                        case 1:
                            dayGroups = getListDaysGroup(scheduleDao, "Четная");
                            break;
                        default:
                            dayGroups = null;
                    }
                    dayGroupAdapter = new DayGroupAdapter(dayGroups);
                    recyclerView.setAdapter(dayGroupAdapter);
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
        List<DayGroup> dayGroups;
        switch (spinnerWeekType.getSelectedItemPosition()) {
            case 0:
                dayGroups = getListDaysGroup(scheduleDao, "Нечетная");
                break;
            case 1:
                dayGroups = getListDaysGroup(scheduleDao, "Четная");
                break;
            default:
                dayGroups = null;
        }
        dayGroupAdapter = new DayGroupAdapter(dayGroups);
        recyclerView.setAdapter(dayGroupAdapter);
    }

    private List<DayGroup> getListDaysGroup(ScheduleDao scheduleDao, String weekType) {
        Map<String, Integer> daysofweek = new HashMap<>();
        daysofweek.put("Понедельник", 0);
        daysofweek.put("Вторник", 1);
        daysofweek.put("Среда", 2);
        daysofweek.put("Четверг", 3);
        daysofweek.put("Пятница", 4);
        daysofweek.put("Суббота", 5);

        List<DayGroup> groups = new ArrayList<>();
        Cursor cursor = scheduleDao.getAllLessons(weekType);

        String myDayOfWeek_id = "Понедельник";
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int number = cursor.getInt(2);
            String lesson_name = cursor.getString(4);
            String lesson_type = cursor.getString(7);
            String teacher_name = cursor.getString(5);
            String classroom = cursor.getString(3);
            String dayOfWeek = cursor.getString(1);
            int weektype = spinnerWeekType.getSelectedItemPosition();
            int dayofweek = daysofweek.get(dayOfWeek);

            ScheduleItem item = new ScheduleItem(id, number, weektype, dayofweek, lesson_name,
                    lesson_type, teacher_name, classroom);

            if (dayOfWeek.equals(myDayOfWeek_id)) {
                if (groups.isEmpty()) groups.add(new DayGroup(dayOfWeek, new ArrayList<>()));
                groups.get(groups.size() - 1).getLessons().add(item);
            } else {
                groups.add(new DayGroup(dayOfWeek, new ArrayList<>()));
                groups.get(groups.size() - 1).getLessons().add(item);
                myDayOfWeek_id = dayOfWeek;
            }
        }
        groups.sort(Comparator.comparingInt(a -> daysofweek.get(a.getDayName())));
        return groups;
    }
}