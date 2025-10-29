package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_09.model.DayOfWeek;
import com.lr3_428_09.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekDao {
    SQLiteDatabase db;

    public DayOfWeekDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<DayOfWeek> getAllDows() {
        List<DayOfWeek> resp = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from DaysOfWeek", null);
        while (cursor.moveToNext()) {
            resp.add(new DayOfWeek(cursor.getInt(0), cursor.getString(1)));
        }
        return resp;
    }
}
