package com.lr3_428_09.model;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    private int id;
    private int number;
    private int weektype;
    private int dayofweek;
    private String lessonName;
    private String lessonType;
    private String teacherName;
    private String classroom;

    public ScheduleItem(int id, int number, int weektype, int dayofweek,
                        String lessonName, String lessonType,
                        String teacherName, String classroom) {
        this.id = id;
        this.number = number;
        this.weektype = weektype;
        this.dayofweek = dayofweek;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.teacherName = teacherName;
        this.classroom = classroom;
    }

    // Getters
    public int getId() { return id; }
    public String getLessonName() { return lessonName; }
    public String getLessonType() { return lessonType; }
    public String getTeacherName() { return teacherName; }
    public String getClassroom() { return classroom; }
    public int getNumber() { return number; }
    public int getWeektype() { return weektype; }
    public int getDayofweek() { return dayofweek; }
}