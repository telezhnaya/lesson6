package com.csc.lesson6;

import android.provider.BaseColumns;

interface FeedsTable extends BaseColumns {
    String TABLE_NAME = "feeds";

    String COLUMN_TITLE = "title";
    String COLUMN_DATE = "date";
    String COLUMN_CONTENT = "content";
}
