package com.lr3_428_09.model;

import androidx.annotation.NonNull;

public class SimpleModel {
    private int id;
    private String name;
    public SimpleModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @NonNull
    @Override
    public String toString() { return name; }
}
