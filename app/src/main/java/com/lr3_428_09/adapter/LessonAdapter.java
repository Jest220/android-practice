package com.lr3_428_09.adapter;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr3_428_09.LessonEditActivity;
import com.lr3_428_09.R;
import com.lr3_428_09.database.DayOfWeekDao;
import com.lr3_428_09.database.DbHelper;
import com.lr3_428_09.database.LessonDao;
import com.lr3_428_09.database.LessonTypeDao;
import com.lr3_428_09.database.TeacherDao;
import com.lr3_428_09.model.ScheduleItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<ScheduleItem> lessons;
    private SQLiteDatabase db;
    private DayOfWeekDao dayOfWeekDao;
    private LessonTypeDao lessonTypeDao;
    private LessonDao lessonDao;
    private TeacherDao teacherDao;

    public LessonAdapter(List<ScheduleItem> lessons, SQLiteDatabase db) {
        this.lessons = lessons;
        this.db = db;
        initDaos();
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        ScheduleItem lesson = lessons.get(position);

        holder.tvLessonName.setText(String.format("%d. %s", lesson.getNumber(), lesson.getLessonName()));
        holder.tvLessonType.setText(lesson.getLessonType());
        holder.tvTeacher.setText(lesson.getTeacherName());
        holder.tvClassroom.setText("Аудитория: " + lesson.getClassroom());

        // Обработчик клика на урок
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LessonEditActivity.class);
                intent.putExtra("lesson", lesson);
                intent.putExtra("lessonName", (Serializable) lessonDao.getAllLessons());
                intent.putExtra("lessonType", (Serializable) lessonTypeDao.getAllLessonTypes());
                intent.putExtra("teacher", (Serializable) teacherDao.getAllTeachers());
                intent.putExtra("dow", (Serializable) dayOfWeekDao.getAllDows());

                v.getContext().startActivity(intent);
            }
        });
    }

    private void initDaos() {
        dayOfWeekDao = new DayOfWeekDao(db);
        lessonDao = new LessonDao(db);
        lessonTypeDao = new LessonTypeDao(db);
        teacherDao = new TeacherDao(db);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonName, tvLessonType, tvTeacher, tvClassroom;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvLessonType = itemView.findViewById(R.id.tvLessonType);
            tvTeacher = itemView.findViewById(R.id.tvTeacher);
            tvClassroom = itemView.findViewById(R.id.tvClassroom);
        }
    }
}