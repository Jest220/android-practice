package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_09.model.DayOfWeek;
import com.lr3_428_09.model.LessonType;

import java.util.ArrayList;
import java.util.List;

public class LessonTypeDao {
    SQLiteDatabase db;

    public LessonTypeDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<LessonType> getAllLessonTypes() {
        List<LessonType> resp = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from LessonTypes", null);
        while (cursor.moveToNext()) {
            resp.add(new LessonType(cursor.getInt(0), cursor.getString(1)));
        }
        return resp;
    }
}
