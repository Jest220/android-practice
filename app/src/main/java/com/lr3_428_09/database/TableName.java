package com.lr3_428_09.database;

public enum TableName {
    TEACHERS_TABLE("Teachers", "Преподаватели"),
    DOWS_TABLE("DaysOfWeek", "Дни недели"),
    LESSONS_TABLE("Lessons", "Названия предметов"),
    LESSON_TYPES_TABLE("LessonTypes", "Типы пар"),
    WEEK_TYPES_TABLE("WeekTypes", "Типы недель");

    private final String name;
    private final String readableName;

    TableName(String name, String readableName) {
        this.name = name;
        this.readableName = readableName;
    }

    public String getName() {
        return name;
    }

    public String getReadableName() {
        return readableName;
    }
}
