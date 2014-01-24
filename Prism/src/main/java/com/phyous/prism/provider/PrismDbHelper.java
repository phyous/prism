package com.phyous.prism.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrismDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Prism.db";
    private static SQLiteDatabase sInstance = null;

    public static synchronized SQLiteDatabase getDb(Context context) {
        if (sInstance == null) {
            sInstance = new PrismDbHelper(context.getApplicationContext()).getWritableDatabase();
        }
        return sInstance;
    }

    private PrismDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String sql: PrismContract.getPrismCreateSQLs()) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Add proper upgrade code when necessary
//        for(String sql: PrismContract.getPrismDeleteSQLs()) {
//            db.execSQL(sql);
//        }
//        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
