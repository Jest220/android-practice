package com.lr3_428_09.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lr3_428_09.R;
import com.lr3_428_09.model.DayGroup;

import java.util.List;

public class DayGroupAdapter extends RecyclerView.Adapter<DayGroupAdapter.DayViewHolder> {

    private List<DayGroup> dayGroups;
    private SQLiteDatabase db;

    public DayGroupAdapter(List<DayGroup> dayGroups, SQLiteDatabase db) {
        this.dayGroups = dayGroups;
        this.db = db;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_group, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayGroup dayGroup = dayGroups.get(position);

        holder.tvDayName.setText(dayGroup.getDayName());

        // Настраиваем внутренний RecyclerView для уроков
        LessonAdapter lessonAdapter = new LessonAdapter(dayGroup.getLessons(), db);
        holder.rvLessons.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvLessons.setAdapter(lessonAdapter);

        // Отключаем вложенную прокрутку для лучшей производительности
        holder.rvLessons.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return dayGroups.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName;
        RecyclerView rvLessons;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            rvLessons = itemView.findViewById(R.id.rvLessons);
        }
    }
}