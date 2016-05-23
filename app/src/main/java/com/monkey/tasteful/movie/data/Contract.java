package com.monkey.tasteful.movie.data;


import android.provider.BaseColumns;


/**
 * Created by emanjaan on 5/21/16.
 */
public class Contract
{
    public static reviewsEntry movieEntry;

    public static final class reviewsEntry implements BaseColumns
    {
        // Table name
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_TITLE= "display_title";

        public static final String COLUMN_SUMMARY = "summary_short";

        public static final String COLUMN_DATE = "publish_date";

    }
}
