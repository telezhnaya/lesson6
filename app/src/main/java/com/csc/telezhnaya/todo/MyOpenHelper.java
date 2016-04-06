package com.csc.telezhnaya.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "todo.db";

    private static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + TaskTable.TABLE_NAME + "("
                    + TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TaskTable.COLUMN_TEXT + " TEXT, "
                    + TaskTable.COLUMN_DATE + " INTEGER, "
                    + TaskTable.COLUMN_STATUS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME;

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
