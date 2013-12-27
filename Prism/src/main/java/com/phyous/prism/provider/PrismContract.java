package com.phyous.prism.provider;

import android.provider.BaseColumns;

public class PrismContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRY_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                    Entry._ID + " INTEGER PRIMARY KEY," +
                    Entry.COLUMN_NAME_DATE + INTEGER_TYPE +" NOT NULL UNIQUE" +
                    " )";
    private static final String SQL_DELETE_ENTRY_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    private static final String SQL_CREATE_FEEDBACK_TABLE =
            "CREATE TABLE " + Feedback.TABLE_NAME + " (" +
                    Feedback._ID + " INTEGER PRIMARY KEY," +
                    Feedback.COLUMN_NAME_FEEDBACK_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
                    Feedback.COLUMN_NAME_FEEDBACK_TYPE + INTEGER_TYPE + COMMA_SEP +
                    Feedback.COLUMN_NAME_FEEDBACK_TEXT + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_FEEDBACK_TABLE =
            "DROP TABLE IF EXISTS " + Feedback.TABLE_NAME;

    public PrismContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public static abstract class Feedback implements BaseColumns {
        public static final String TABLE_NAME = "feedback";
        public static final String COLUMN_NAME_FEEDBACK_ENTRY_ID = "entry_id";
        public static final String COLUMN_NAME_FEEDBACK_TYPE = "feedback_type";
        public static final String COLUMN_NAME_FEEDBACK_TEXT = "feedback_text";
    }

    public static String[] getPrismCreateSQLs() {
        return new String[]{
            SQL_CREATE_ENTRY_TABLE,
            SQL_CREATE_FEEDBACK_TABLE
        };
    }

    public static String[] getPrismDeleteSQLs() {
        return new String[]{
                SQL_DELETE_ENTRY_TABLE,
                SQL_DELETE_FEEDBACK_TABLE
        };
    }
}
