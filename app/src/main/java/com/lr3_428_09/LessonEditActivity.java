package com.lr3_428_09;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lr3_428_09.database.DbHelper;
import com.lr3_428_09.database.ScheduleDao;
import com.lr3_428_09.model.ScheduleItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LessonEditActivity extends AppCompatActivity {
    private ScheduleItem lesson;
    private EditText etLessonName, etClassroom, etLessonType, etTeacher, etLessonNumber;
    private Spinner spinnerDayOfWeek, spinnerWeekType;
    private Button btnSave, btnDelete, btnCancel;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_edit);

        initializeViews();
        initializeDatabase();
        setupSpinners();
        loadLessonData();
        setupButtons();
    }

    private void initializeViews() {
        etLessonName = findViewById(R.id.etLessonName);
        etClassroom = findViewById(R.id.etClassroom);
        etLessonType = findViewById(R.id.etLessonType);
        etTeacher = findViewById(R.id.etTeacher);
        etLessonNumber = findViewById(R.id.etLessonNumber);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        spinnerWeekType = findViewById(R.id.spinnerWeekType);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void initializeDatabase() {
        dbHelper = new DbHelper(this);
        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            scheduleDao = new ScheduleDao(db);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании базы данных", Toast.LENGTH_LONG).show();
        }
    }

    private void setupSpinners() {
        // Настройка спиннера дней недели
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayAdapter.addAll(getDayNames());
        spinnerDayOfWeek.setAdapter(dayAdapter);

        // Настройка спиннера типов недели
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekAdapter.addAll(getWeekTypes());
        spinnerWeekType.setAdapter(weekAdapter);
    }

    private void loadLessonData() {
        Intent intent = getIntent();
        lesson = (ScheduleItem) intent.getSerializableExtra("lesson");
        
        if (lesson != null) {
            isEditMode = true;
            
            etLessonName.setText(lesson.getLessonName());
            etClassroom.setText(lesson.getClassroom());
            etLessonType.setText(lesson.getLessonType());
            etTeacher.setText(lesson.getTeacherName());
            int lessonNumber = lesson.getNumber();
            etLessonNumber.setText(String.valueOf(lessonNumber));

            spinnerDayOfWeek.setSelection(lesson.getDayofweek());
            spinnerWeekType.setSelection(lesson.getWeektype());
            
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void setupButtons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLesson();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLesson();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Просто закрываем активность без сохранения
                finish();
            }
        });
    }

    private void saveLesson() {
        String number = etLessonNumber.getText().toString().trim();
        String lessonName = etLessonName.getText().toString().trim();
        String classroom = etClassroom.getText().toString().trim();
        String lessonType = etLessonType.getText().toString().trim();
        String teacher = etTeacher.getText().toString().trim();
        String weekType = spinnerWeekType.getSelectedItem().toString();
        String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();

        if (number.isEmpty()) {
            Toast.makeText(this, "Введите номер пары", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lessonName.isEmpty()) {
            Toast.makeText(this, "Введите название урока", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classroom.isEmpty()) {
            Toast.makeText(this, "Введите аудиторию", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lessonType.isEmpty()) {
            Toast.makeText(this, "Введите тип пары", Toast.LENGTH_SHORT).show();
            return;
        }

        if (teacher.isEmpty()) {
            Toast.makeText(this, "Введите преподавателя", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            int id = lesson.getId();
            scheduleDao.updateLesson(id, number, weekType, dayOfWeek, lessonName, lessonType, teacher, classroom);
            Toast.makeText(this, "Урок обновлен", Toast.LENGTH_SHORT).show();
        } else {
            scheduleDao.insertLesson(number, weekType, dayOfWeek, lessonName, lessonType, teacher, classroom);
            Toast.makeText(this, "Урок добавлен", Toast.LENGTH_SHORT).show();
        }
        
        finish();
    }

    private void deleteLesson() {
        if (isEditMode) {
            scheduleDao.deleteLesson(lesson.getId());
            Toast.makeText(this, "Урок удален", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private List<String> getDayNames() {
        List<String> days = new ArrayList<>();
        days.add("Понедельник");
        days.add("Вторник");
        days.add("Среда");
        days.add("Четверг");
        days.add("Пятница");
        days.add("Суббота");
        return days;
    }

    private List<String> getWeekTypes() {
        List<String> weeks = new ArrayList<>();
        weeks.add("Нечетная");
        weeks.add("Четная");
        return weeks;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
