package com.lr3_428_09.model;

import java.io.Serializable;

public class Lesson implements Serializable {
    private int id;
    private String name;

    public Lesson(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
