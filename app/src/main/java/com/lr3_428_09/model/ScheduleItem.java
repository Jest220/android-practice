package com.lr3_428_09.model;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    private int id;
    private int number;
    private String weektype;
    private String dayofweek;
    private String lessonName;
    private String lessonType;
    private String teacherName;
    private String classroom;

    public ScheduleItem(int id, int number, String weektype, String dayofweek,
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
    public String getWeekType() { return weektype; }
    public String getDayOfWeek() { return dayofweek; }
}