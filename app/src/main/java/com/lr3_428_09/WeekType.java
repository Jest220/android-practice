package com.lr3_428_09;

import androidx.annotation.NonNull;

public enum WeekType {
    ODD("Нечетная"),
    EVEN("Четная");

    private final String name;

    WeekType(String name) {
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
