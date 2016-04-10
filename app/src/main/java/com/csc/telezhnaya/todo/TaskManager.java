package com.csc.telezhnaya.todo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import java.util.Date;

import static com.csc.telezhnaya.todo.MyContentProvider.*;
import static com.csc.telezhnaya.todo.TaskTable.*;

public class TaskManager implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String DB_ORDER = TaskTable.COLUMN_STARRED + " DESC, " +
            TaskTable.COLUMN_STATUS + ", " + TaskTable.COLUMN_DATE;
    public static final TaskManager INSTANCE = new TaskManager();

    private TaskManager() {
    }

    private Context context;


    public void bind(Context context) {
        this.context = context;
    }

    public void unbind() {
        context = null;
    }

    public void addTask(String text) {
        long time = new Date().getTime();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_DATE, time);
        values.put(COLUMN_STATUS, 0);
        values.put(COLUMN_STARRED, 0);
        context.getContentResolver().insert(ENTRIES_URI, values);
    }

    public void updateTask(int position, String newText, boolean done, boolean starred) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, newText);
        values.put(COLUMN_STATUS, done ? 1 : 0);
        values.put(COLUMN_STARRED, starred ? 1 : 0);
        context.getContentResolver().update(ContentUris.withAppendedId(ENTRIES_URI, position),
                values, TaskTable._ID + "=?", new String[]{String.valueOf(position)});
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, ENTRIES_URI, null, null, null, TaskManager.DB_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
