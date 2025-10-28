package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LessonTypeDao {
    private SQLiteDatabase db;

    public LessonTypeDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertLessonType(String type) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        return db.insert("LessonTypes", null, values);
    }

    public Cursor getAllLessonTypes() {
        return db.rawQuery("select * from LessonTypes", null);
    }

    public int deleteLessonType(int id) {
        return db.delete("LessonTypes", "id = ?", new String[]{String.valueOf(id)});
    }
}
