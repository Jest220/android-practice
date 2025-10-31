package com.lr3_428_09;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

public class LessonEditActivity extends AppCompatActivity {
    private ScheduleItem lesson;
    private EditText etClassroom;
    private Spinner spinnerLessonName, spinnerLessonType, spinnerTeacher, spinnerLessonNumber, spinnerDayOfWeek, spinnerWeekType;
    private Button btnSave, btnDelete, btnCancel;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private ScheduleDao scheduleDao;
    private List<SimpleModel> weektypes;
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

    private int getIndex(Spinner spinner, String val) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (val.equals(spinner.getItemAtPosition(i).toString())) {
                return i;
            }
        }
        return 0;
    }
    private void initializeModels() {
        weektypes = new SimpleDao(TableName.WEEK_TYPES_TABLE.getName(), db).getAll();
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
        // Настройка спиннера преподавателей
        setSpinnerAdapter(spinnerTeacher, teachers);

        // Настройка спиннера типов предметов
        setSpinnerAdapter(spinnerLessonType, lessonTypes);

        // Настройка спиннера названий предметов
        setSpinnerAdapter(spinnerLessonName, lessonNames);

        // Настройка спиннера дней недели
        setSpinnerAdapter(spinnerDayOfWeek, dows);

        // Настройка спиннера типов недели
        setSpinnerAdapter(spinnerWeekType, weektypes);
    }

    private void setupSpinnerLessonNumber() {
        int weekTypeId = ((SimpleModel) spinnerWeekType.getSelectedItem()).getId();
        int dayOfWeekId = ((SimpleModel) spinnerDayOfWeek.getSelectedItem()).getId();

        List<String> numbers;

        if (lesson == null) {
            numbers = scheduleDao.getAvailableNumbers(weekTypeId, dayOfWeekId);
            setSpinnerAdapter(spinnerLessonNumber, numbers);
            return;
        }

        int lessonWeekTypeId = weektypes.get(getIndex(spinnerWeekType, lesson.getWeekType())).getId();
        int lessonDayOfWeekId = dows.get(getIndex(spinnerDayOfWeek, lesson.getDayOfWeek())).getId();

        if (weekTypeId == lessonWeekTypeId && dayOfWeekId == lessonDayOfWeekId) {
            numbers = scheduleDao.getAvailableNumbers(weekTypeId, dayOfWeekId, lesson.getNumber());
            setSpinnerAdapter(spinnerLessonNumber, numbers);
            spinnerLessonNumber.setSelection(getIndex(spinnerLessonNumber, String.valueOf(lesson.getNumber())));
        } else {
            numbers = scheduleDao.getAvailableNumbers(weekTypeId, dayOfWeekId);
            setSpinnerAdapter(spinnerLessonNumber, numbers);
        }
    }

    private <T> void setSpinnerAdapter(Spinner spinner, List<T> items) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void loadLessonData() {
        Intent intent = getIntent();
        lesson = (ScheduleItem) intent.getSerializableExtra("lesson");
        
        if (lesson != null) {
            isEditMode = true;
            spinnerDayOfWeek.setSelection(getIndex(spinnerDayOfWeek, lesson.getDayOfWeek()));
            spinnerWeekType.setSelection(getIndex(spinnerWeekType, lesson.getWeekType()));
            spinnerLessonName.setSelection(getIndex(spinnerLessonName, lesson.getLessonName()));
            spinnerLessonType.setSelection(getIndex(spinnerLessonType, lesson.getLessonType()));
            spinnerTeacher.setSelection(getIndex(spinnerTeacher, lesson.getTeacherName()));
            spinnerLessonNumber.setSelection(getIndex(spinnerLessonNumber, String.valueOf(lesson.getNumber())));
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

        spinnerWeekType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupSpinnerLessonNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupSpinnerLessonNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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

        if (classroom.length() > 50) {
            Toast.makeText(this, "Сделайте аудиторию короче, максимальный размер: 50",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}