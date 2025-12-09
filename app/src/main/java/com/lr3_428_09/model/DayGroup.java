package com.lr3_428_09.model;

import java.util.List;

public class DayGroup {
    private final String dayName;
    private final List<ScheduleItem> lessons;

    public DayGroup(String dayName, List<ScheduleItem> lessons) {
        this.dayName = dayName;
        this.lessons = lessons;
    }

    public String getDayName() { return dayName; }
    public List<ScheduleItem> getLessons() { return lessons; }
}