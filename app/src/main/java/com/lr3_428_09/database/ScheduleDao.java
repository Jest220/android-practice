package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScheduleDao {
    private SQLiteDatabase db;

    public ScheduleDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertLesson(String number, String weektype, String dayofweek,
                             String lesson, String lessontype, String teacher, String classroom) {
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("weektype", weektype);
        values.put("dayofweek", dayofweek);
        values.put("lesson", lesson);
        values.put("lessontype", lessontype);
        values.put("teacher", teacher);
        values.put("classroom", classroom);
        return db.insert("Schedule", null, values);
    }

    public Cursor getAllLessons(String weektype) {
        return db.rawQuery("select * from Schedule where weektype = ? order by dayofweek, number;", new String[]{weektype});
    }

    public int deleteLesson(int id) {
        return db.delete("Schedule", "id = ?", new String[]{String.valueOf(id)});
    }

    public int updateLesson(int id, String number, String weektype, String dayofweek,
                                String lesson, String lessontype, String teacher, String classroom) {
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("weektype", weektype);
        values.put("dayofweek", dayofweek);
        values.put("lesson", lesson);
        values.put("lessontype", lessontype);
        values.put("teacher", teacher);
        values.put("classroom", classroom);
        return db.update("Schedule", values, "id = ?", new String[]{String.valueOf(id)});
    }
}
