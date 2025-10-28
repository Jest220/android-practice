package com.lr3_428_09.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeekTypeDao {
    private SQLiteDatabase db;

    public WeekTypeDao(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor getAllWeekTypes() {
        return db.rawQuery("select type from WeekTypes", null);
    }

    public Cursor getWeekType(int id) {
        return db.rawQuery("select type from WeekTypes where id = ? limit 1", new String[]{String.valueOf(id)});
    }
}
