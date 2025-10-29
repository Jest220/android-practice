package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_09.model.LessonType;
import com.lr3_428_09.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    SQLiteDatabase db;

    public TeacherDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> resp = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Teachers", null);
        while (cursor.moveToNext()) {
            resp.add(new Teacher(cursor.getInt(0), cursor.getString(1)));
        }
        return resp;
    }
}
