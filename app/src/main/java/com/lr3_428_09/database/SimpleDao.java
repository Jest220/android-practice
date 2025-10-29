package com.lr3_428_09.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_09.model.SimpleModel;

import java.util.ArrayList;
import java.util.List;

public class SimpleDao {
    private String tableName;
    private SQLiteDatabase db;

    public SimpleDao(String tableName, SQLiteDatabase db) {
        this.tableName = tableName;
        this.db = db;
    }

    public List<SimpleModel> getAll() {
        List<SimpleModel> resp = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("select * from " + tableName, null)) {
            while (cursor.moveToNext()) {
                resp.add(new SimpleModel(cursor.getInt(0), cursor.getString(1)));
            }
        }
        return resp;
    }
    public long insert(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.insert(tableName, null, values);
    }
    public int delete(int id) {
        return db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
    }

    public int update(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.update(tableName, values, "id = ?", new String[]{String.valueOf(id)});
    }
}
