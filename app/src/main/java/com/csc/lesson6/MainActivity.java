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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Uri entriesUri = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries");

        Cursor cursor =  getContentResolver().query(entriesUri,
                null, null, null, null);
        while (cursor.moveToNext()) {
            int titleColumnIndex = cursor.getColumnIndex(COLUMN_TITLE);
            String title = cursor.getString(titleColumnIndex);
            int contentColumnIndex = cursor.getColumnIndex(COLUMN_CONTENT);
            String content = cursor.getString(contentColumnIndex);
            Log.d(TAG, "onCreate: title = " + title + ", " + "content" + " = " + content);
        }

        Uri uri = null;
        getContentResolver().registerContentObserver(uri, true, null);

        cursor.registerContentObserver(new ContentObserver(new Handler()) {
            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Cursor cursor1 =  getContentResolver().query(entriesUri,
                        null, null, null, null);
                while (cursor1.moveToNext()) {
                    int titleColumnIndex = cursor1.getColumnIndex(COLUMN_TITLE);
                    String title = cursor1.getString(titleColumnIndex);
                    int contentColumnIndex = cursor1.getColumnIndex(COLUMN_CONTENT);
                    String content = cursor1.getString(contentColumnIndex);
                    Log.d(TAG, "onCreate: title = " + title + ", " + "content" + " = " + content);
                }
                cursor1.close();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
            }
        });

//        cursor.close();

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE,
                        "new title3");
                values.put(FeedsTable.COLUMN_CONTENT,
                        "<![CDATA[Компания Sony выпустит более мощную версию своей игровой консоли PlayStation 4 с обновленной видеокартой в октябре 2016 года. Будущая версия приставки, которая, предположительно, получит название PlayStation 4k, сможет запускать игры с 4k-графикой, чье разрешение в 4 раза превышает текущий стандарт 1080p.]]>");
                values.put(FeedsTable.COLUMN_DATE,
                        "Mon, 28 Mar 2016 19:04:05 +0300");
                getContentResolver().insert(
                        entriesUri,
                        values);
            }
        });
    }
}
