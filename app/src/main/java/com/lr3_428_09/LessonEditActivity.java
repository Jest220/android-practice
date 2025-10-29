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
import com.lr3_428_09.database.SimpleDao;
import com.lr3_428_09.database.TableName;
import com.lr3_428_09.model.ScheduleItem;
import com.lr3_428_09.model.SimpleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LessonEditActivity extends AppCompatActivity {
    private ScheduleItem lesson;
    private EditText etClassroom;
    private Spinner spinnerLessonName, spinnerLessonType, spinnerTeacher, spinnerLessonNumber, spinnerDayOfWeek, spinnerWeekType;
    private Button btnSave, btnDelete, btnCancel;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private List<SimpleModel> dows;
    private List<SimpleModel> lessonNames;
    private List<SimpleModel> lessonTypes;
    private List<SimpleModel> teachers;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_edit);

        initializeViews();
        initializeDatabase();
        initializeModels();
        setupSpinners();
        loadLessonData();
        setupButtons();
    }

    private int getSelectedIndex(Spinner spinner, String val) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (val.equals(spinner.getItemAtPosition(i).toString())) {
                return i;
            }
        }
        return 0;
    }
    private void initializeModels() {
        dows = new SimpleDao(TableName.DOWS_TABLE.getName(), db).getAll();
        lessonNames = new SimpleDao(TableName.LESSONS_TABLE.getName(), db).getAll();
        lessonTypes = new SimpleDao(TableName.LESSON_TYPES_TABLE.getName(), db).getAll();
        teachers = new SimpleDao(TableName.TEACHERS_TABLE.getName(), db).getAll();
    }

    private void initializeViews() {
        etClassroom = findViewById(R.id.etClassroom);
        spinnerLessonName = findViewById(R.id.spinnerLessonName);
        spinnerLessonType = findViewById(R.id.spinnerLessonType);
        spinnerTeacher = findViewById(R.id.spinnerTeacher);
        spinnerLessonNumber = findViewById(R.id.spinnerLessonNumber);
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
        // Настройка спиннера номера предмета
        ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberAdapter.addAll(getNumbers());
        spinnerLessonNumber.setAdapter(numberAdapter);

        // Настройка спиннера преподавателей
        ArrayAdapter<SimpleModel> teacherAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teachers);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeacher.setAdapter(teacherAdapter);

        // Настройка спиннера типов предметов
        ArrayAdapter<SimpleModel> lessonTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonTypes);
        lessonTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessonType.setAdapter(lessonTypeAdapter);

        // Настройка спиннера названий предметов
        ArrayAdapter<SimpleModel> lessonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonNames);
        lessonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessonName.setAdapter(lessonAdapter);

        // Настройка спиннера дней недели
        ArrayAdapter<SimpleModel> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dows);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(dayAdapter);

        // Настройка спиннера типов недели
        ArrayAdapter<SimpleModel> weekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getWeekTypes());
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekType.setAdapter(weekAdapter);
    }

    private void loadLessonData() {
        Intent intent = getIntent();
        lesson = (ScheduleItem) intent.getSerializableExtra("lesson");
        
        if (lesson != null) {
            isEditMode = true;
            spinnerDayOfWeek.setSelection(lesson.getDayofweek());
            spinnerWeekType.setSelection(lesson.getWeektype());
            spinnerLessonName.setSelection(getSelectedIndex(spinnerLessonName, lesson.getLessonName()));
            spinnerLessonType.setSelection(getSelectedIndex(spinnerLessonType, lesson.getLessonType()));
            spinnerTeacher.setSelection(getSelectedIndex(spinnerTeacher, lesson.getTeacherName()));
            spinnerLessonNumber.setSelection(getSelectedIndex(spinnerLessonNumber, String.valueOf(lesson.getNumber())));
            etClassroom.setText(lesson.getClassroom());
            
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> { saveLesson(); });

        btnDelete.setOnClickListener(v -> { deleteLesson(); });

        btnCancel.setOnClickListener(v -> { finish(); });
    }

    private void saveLesson() {
        int number = Integer.parseInt(spinnerLessonNumber.getSelectedItem().toString());
        int weektype_id = ((SimpleModel) spinnerWeekType.getSelectedItem()).getId();
        int dayofweek_id = ((SimpleModel) spinnerDayOfWeek.getSelectedItem()).getId();
        int lesson_id = ((SimpleModel) spinnerLessonName.getSelectedItem()).getId();
        int lessontype_id = ((SimpleModel) spinnerLessonType.getSelectedItem()).getId();
        int teacher_id = ((SimpleModel) spinnerTeacher.getSelectedItem()).getId();
        String classroom = etClassroom.getText().toString().trim();

        if (classroom.isEmpty()) {
            Toast.makeText(this, "Введите аудиторию", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            int id = lesson.getId();
            scheduleDao.updateLesson(id, number, weektype_id, dayofweek_id, lesson_id, lessontype_id, teacher_id, classroom);
            Toast.makeText(this, "Пара обновлена", Toast.LENGTH_SHORT).show();
        } else {
            scheduleDao.insertLesson(number, weektype_id, dayofweek_id, lesson_id, lessontype_id, teacher_id, classroom);
            Toast.makeText(this, "Пара добавлена", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteLesson() {
        if (isEditMode) {
            scheduleDao.deleteLesson(lesson.getId());
            Toast.makeText(this, "Пара удалена", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private List<SimpleModel> getWeekTypes() {
        List<SimpleModel> weeks = new ArrayList<>();
        weeks.add(new SimpleModel(1, "Нечетная"));
        weeks.add(new SimpleModel(2, "Четная"));
        return weeks;
    }

    private List<String> getNumbers() {
        List<String> numbers = new ArrayList<>();
        for (int i = 1; i <= 6; ++i) {
            numbers.add(String.valueOf(i));
        }
        return numbers;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}