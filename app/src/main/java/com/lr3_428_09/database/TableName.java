package com.lr3_428_09.database;

public enum TableName {
    TEACHERS_TABLE("Teachers"),
    DOWS_TABLE("DaysOfWeek"),
    LESSONS_TABLE("Lessons"),
    LESSON_TYPES_TABLE("LessonTypes");

    private final String name;

    TableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
