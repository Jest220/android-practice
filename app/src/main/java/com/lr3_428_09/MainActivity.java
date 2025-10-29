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
import com.lr3_428_09.database.WeekTypeDao;
import com.lr3_428_09.model.DayGroup;
import com.lr3_428_09.model.ScheduleItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DayGroupAdapter dayGroupAdapter;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private WeekTypeDao weekTypeDao;
    private Spinner spinnerWeekType;
    private FloatingActionButton fabAddLesson;

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Обработчик для кнопки добавления урока
        fabAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonEditActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DbHelper(this);

        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            scheduleDao = new ScheduleDao(db);
            weekTypeDao = new WeekTypeDao(db);

            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            Cursor cursor = weekTypeDao.getAllWeekTypes();
            while (cursor.moveToNext()) {
                adapter.add(cursor.getString(0));
            }
            cursor.close();
            spinnerWeekType.setAdapter(adapter);

            spinnerWeekType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<DayGroup> dayGroups;
                    switch (position) {
                        case 0:
                            dayGroups = getListDaysGroup(scheduleDao, 1);
                            break;
                        case 1:
                            dayGroups = getListDaysGroup(scheduleDao, 2);
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

    private List<DayGroup> getListDaysGroup(ScheduleDao scheduleDao, int weekType_id) {
        List<DayGroup> groups = new ArrayList<>();
        groups.add(new DayGroup("Понедельник", new ArrayList<>()));
        Cursor cursor = scheduleDao.getAllLessonsWithJoin(weekType_id);
        int dayOfWeek_id = 1;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int number = cursor.getInt(cursor.getColumnIndex("number"));
            String lesson_name = cursor.getString(cursor.getColumnIndex("lesson_name"));
            String lesson_type = cursor.getString(cursor.getColumnIndex("lesson_type"));
            String teacher_name = cursor.getString(cursor.getColumnIndex("teacher_name"));
            String classroom = cursor.getString(cursor.getColumnIndex("classroom"));

            ScheduleItem item = new ScheduleItem(id, number, String.format("%d. %s", number, lesson_name),
                    lesson_type, teacher_name, classroom);
            int dayOfWeek = cursor.getInt(cursor.getColumnIndex("dayofweek_id"));
            if (dayOfWeek == dayOfWeek_id) {
                groups.get(groups.size() - 1).getLessons().add(item);
            } else {
                groups.add(new DayGroup(cursor.getString(cursor.getColumnIndex("day_name")), new ArrayList<>()));
                groups.get(groups.size() - 1).getLessons().add(item);
                dayOfWeek_id = dayOfWeek;
            }
        }
        return groups;
    }
}