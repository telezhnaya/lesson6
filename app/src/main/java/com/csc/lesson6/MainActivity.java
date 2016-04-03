package com.csc.lesson6;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import static com.csc.lesson6.FeedsTable.COLUMN_CONTENT;
import static com.csc.lesson6.FeedsTable.COLUMN_TITLE;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries");

    private final ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "ContentObserver: onChange");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Cursor cursor = managedQuery(ENTRIES_URI,
                null, null, null, null);

        cursor.registerContentObserver(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }

    public void addData(View view) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,
                "new title3");
        values.put(FeedsTable.COLUMN_CONTENT,
                "<![CDATA[Компания Sony выпустит более мощную версию своей игровой консоли PlayStation 4 с обновленной видеокартой в октябре 2016 года. Будущая версия приставки, которая, предположительно, получит название PlayStation 4k, сможет запускать игры с 4k-графикой, чье разрешение в 4 раза превышает текущий стандарт 1080p.]]>");
        values.put(FeedsTable.COLUMN_DATE,
                "Mon, 28 Mar 2016 19:04:05 +0300");
        getContentResolver().insert(
                ENTRIES_URI,
                values);
    }
}
