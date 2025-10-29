package com.lr3_428_09.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr3_428_09.R;
import com.lr3_428_09.model.ScheduleItem;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<ScheduleItem> lessons;

    public LessonAdapter(List<ScheduleItem> lessons) {
        this.lessons = lessons;
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

        holder.tvLessonName.setText(lesson.getLessonName());
        holder.tvLessonType.setText(lesson.getLessonType());
        holder.tvTeacher.setText(lesson.getTeacherName());
        holder.tvClassroom.setText("Аудитория: " + lesson.getClassroom());
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