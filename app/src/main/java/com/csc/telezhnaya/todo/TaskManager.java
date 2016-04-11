package com.csc.telezhnaya.todo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Date;

import static com.csc.telezhnaya.todo.MyContentProvider.*;
import static com.csc.telezhnaya.todo.TaskTable.*;

public class TaskManager implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String DB_ORDER = TaskTable.COLUMN_STARRED + " DESC, " +
            TaskTable.COLUMN_STATUS + ", " + TaskTable.COLUMN_DATE;
    public static final TaskManager INSTANCE = new TaskManager();
    private CursorAdapter adapter;

    private TaskManager() {
    }

    private Context context;

    public void bind(Context context) {
        this.context = context;
        adapter = new CursorAdapter(context, null, true) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView) view.findViewById(R.id.task);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.task_done);
                RatingBar star = (RatingBar) view.findViewById(R.id.star);

                textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT)));
                boolean done = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == 1;
                checkBox.setChecked(done);
                star.setRating(cursor.getInt(cursor.getColumnIndex(COLUMN_STARRED)));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable._ID));
                view.setTag(id);

                if (done) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        };
    }

    public void unbind() {
        context = null;
    }

    public CursorAdapter getAdapter() {
        return adapter;
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
        // насколько я смогла разобраться, нужно вызывать changeCursor, чтобы старый убивали
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }
}
