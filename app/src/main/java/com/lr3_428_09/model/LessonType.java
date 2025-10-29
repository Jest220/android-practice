package com.lr3_428_09.model;

import java.io.Serializable;

public class LessonType implements Serializable {
    private int id;
    private String type;

    public LessonType(int id, String name) {
        this.id = id;
        this.type = name;
    }

    public int getId() { return id; }
    public String getType() { return type; }
}
