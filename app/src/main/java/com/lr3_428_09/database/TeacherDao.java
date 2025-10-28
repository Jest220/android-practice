package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TeacherDao {
    private SQLiteDatabase db;

    public TeacherDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertTeacher(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.insert("Teachers", null, values);
    }

    public Cursor getAllTeachers() {
        return db.rawQuery("select * from Teachers", null);
    }

    public int deleteTeacher(int id) {
        return db.delete("Teachers", "id = ?", new String[]{String.valueOf(id)});
    }
}
