package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_09.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonDao {
    SQLiteDatabase db;

    public LessonDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Lesson> getAllLessons() {
        List<Lesson> resp = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Lessons", null);
        while (cursor.moveToNext()) {
            resp.add(new Lesson(cursor.getInt(0), cursor.getString(1)));
        }
        return resp;
    }
}
