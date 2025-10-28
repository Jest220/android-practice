package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LessonDao {
    private SQLiteDatabase db;

    public LessonDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertLesson(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.insert("Lessons", null, values);
    }

    public Cursor getAllLessons() {
        return db.rawQuery("select * from Lessons", null);
    }

    public int deleteLesson(int id) {
        return db.delete("Lesson", "id = ?", new String[]{String.valueOf(id)});
    }
}
