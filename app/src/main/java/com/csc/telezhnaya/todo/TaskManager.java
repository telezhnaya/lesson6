package com.csc.telezhnaya.todo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import java.util.Date;

import static com.csc.telezhnaya.todo.MyContentProvider.*;
import static com.csc.telezhnaya.todo.TaskTable.*;

public class TaskManager {
    public static final String DB_ORDER = TaskTable.COLUMN_STATUS + ", " + TaskTable.COLUMN_DATE;
    public static final TaskManager INSTANCE = new TaskManager();

    private TaskManager() {
    }

    private Context context;
    private CursorAdapter cursorAdapter;


    public void bind(Context context, CursorAdapter cursorAdapter) {
        this.context = context;
        this.cursorAdapter = cursorAdapter;
    }

    public void unbind() {
        context = null;
        cursorAdapter = null;
    }

    public void addTask(String text) {
        long time = new Date().getTime();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_DATE, time);
        values.put(COLUMN_STATUS, 0);
        context.getContentResolver().insert(ENTRIES_URI, values);

        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "entries"),
                null, null, null, DB_ORDER);
        cursorAdapter.changeCursor(cursor);
    }

    public void updateTask(int position, String newText, boolean done) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, newText);
        values.put(COLUMN_STATUS, done ? 1 : 0);
        context.getContentResolver().update(ContentUris.withAppendedId(ENTRIES_URI, position),
                values, TaskTable._ID + "=?", new String[]{String.valueOf(position)});
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "entries"),
                null, null, null, DB_ORDER);
        cursorAdapter.changeCursor(cursor);
    }
}
