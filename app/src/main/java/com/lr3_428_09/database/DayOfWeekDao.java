package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DayOfWeekDao {
    private SQLiteDatabase db;

    public DayOfWeekDao(SQLiteDatabase db) {
        this.db = db;
    }
    public Cursor getAllDaysOfWeek() {
        return db.rawQuery("select * from DaysOfWeek", null);
    }
}
