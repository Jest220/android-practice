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
        String query = "SELECT s.id, d.name as dow, s.number, s.classroom, l.name as lesson, t.name as teacher, w.type as week, lt.type as lessontype \n" +
                "FROM Schedule s\n" +
                "JOIN WeekTypes w ON s.weektype_id = w.id\n" +
                "JOIN Teachers t ON s.teacher_id = t.id\n" +
                "JOIN Lessons l ON s.lesson_id = l.id\n" +
                "JOIN LessonTypes lt ON s.lessontype_id = lt.id\n" +
                "JOIN DaysOfWeek d ON s.dayofweek_id = d.id\n" +
                "WHERE w.type = ?\n" +
                "ORDER BY week";
        return db.rawQuery(query, new String[]{weektype});
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
