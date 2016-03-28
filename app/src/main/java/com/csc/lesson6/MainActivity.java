package com.csc.lesson6;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentValues values = new ContentValues();
        values.put(FeedsTable.COLUMN_TITLE, "СМИ узнали дату выхода более мощной PlayStation 4");
        values.put(FeedsTable.COLUMN_CONTENT, "<![CDATA[Компания Sony выпустит более мощную версию своей игровой консоли PlayStation 4 с обновленной видеокартой в октябре 2016 года. Будущая версия приставки, которая, предположительно, получит название PlayStation 4k, сможет запускать игры с 4k-графикой, чье разрешение в 4 раза превышает текущий стандарт 1080p.]]>");
        values.put(FeedsTable.COLUMN_DATE, "Mon, 28 Mar 2016 19:04:05 +0300");
        getContentResolver().insert(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), values);
    }
}
