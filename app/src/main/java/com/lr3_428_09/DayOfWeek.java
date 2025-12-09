package com.lr3_428_09;

import androidx.annotation.NonNull;

public enum DayOfWeek {
    Monday("Понедельник"),
    Tuesday("Вторник"),
    Wednesday("Среда"),
    Thursday("Четверг"),
    Friday("Пятница"),
    Saturday("Суббота"),
    Sunday("Воскресенье");

    private final String name;

    DayOfWeek(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
