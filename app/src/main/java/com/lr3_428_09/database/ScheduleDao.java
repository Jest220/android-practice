package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScheduleDao {
    private SQLiteDatabase db;

    public ScheduleDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertLesson(int number, int weektypeId, int dayofweekId,
                             int lessonId, int lessontypeId, int teacherId) {
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("weektype_id", weektypeId);
        values.put("dayofweek_id", dayofweekId);
        values.put("lesson_id", lessonId);
        values.put("lessontype_id", lessontypeId);
        values.put("teacher_id", teacherId);
        return db.insert("Schedule", null, values);
    }

    public Cursor getAllLessons() {
        return db.rawQuery("select * from Schedule", null);
    }

    public Cursor getAllLessonsWithJoin(int weektypeId) {
        String query = "SELECT \n" +
                "    s.id,\n" +
                "    s.number,\n" +
                "    s.dayofweek_id,\n" +
                "    dw.name as day_name,\n" +
                "    l.name as lesson_name,\n" +
                "    lt.type as lesson_type,\n" +
                "    t.name as teacher_name,\n" +
                "    s.classroom\n" +
                "FROM Schedule s\n" +
                "JOIN DaysOfWeek dw ON s.dayofweek_id = dw.id\n" +
                "JOIN Lessons l ON s.lesson_id = l.id\n" +
                "JOIN LessonTypes lt ON s.lessontype_id = lt.id\n" +
                "JOIN Teachers t ON s.teacher_id = t.id\n" +
                "WHERE s.weektype_id = " + weektypeId + "\n" +
                "ORDER BY s.dayofweek_id, s.number;";
        return db.rawQuery(query, null);
    }

    public int deleteLesson(int id) {
        return db.delete("Schedule", "id = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getLesson(int id) {
        return db.rawQuery("select * from Schedule where id = ? limit 1", new String[]{String.valueOf(id)});
    }
}
