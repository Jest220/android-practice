package com.lr3_428_09.model;

public class ScheduleItem {
    private int id;
    private int number;
    private String lessonName;
    private String lessonType;
    private String teacherName;
    private String classroom;

    public ScheduleItem(int id, int number, String lessonName, String lessonType,
                        String teacherName, String classroom) {
        this.id = id;
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
}