package com.lr3_428_09.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "schedule.db";
    private final Context context;
    private final String dbPath;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.dbPath = context.getDatabasePath(DB_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void createDatabase() throws IOException {
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            copyDatabaseFromAssets();
        }
    }

    private void copyDatabaseFromAssets() throws IOException {
        InputStream input = context.getAssets().open("databases/" + DB_NAME);
        OutputStream output = new FileOutputStream(dbPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
