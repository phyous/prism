package com.phyous.prism.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pyoussef on 12/22/13.
 */
public class EntryDataSource {
    private Context mContext;
    private final String[] entryAllColumns = {
            PrismContract.Entry._ID,
            PrismContract.Entry.COLUMN_NAME_DATE
    };
    private final String[] feedbackAllColumns = {
            PrismContract.Feedback._ID,
            PrismContract.Feedback.COLUMN_NAME_FEEDBACK_ENTRY_ID,
            PrismContract.Feedback.COLUMN_NAME_FEEDBACK_TEXT,
            PrismContract.Feedback.COLUMN_NAME_FEEDBACK_TYPE
    };

    public EntryDataSource(Context context) {
        mContext = context;
    }

    private SQLiteDatabase db() {
        return PrismDbHelper.getDb(mContext);
    }

    public void createEntry(Entry model) {
        ContentValues entryValues = new ContentValues();
        entryValues.put(PrismContract.Entry.COLUMN_NAME_DATE, model.getDate());

        long newEntryRowId = db().insert(
                PrismContract.Entry.TABLE_NAME,
                null,
                entryValues);

        createFeedbackValues(0, newEntryRowId, model.getNegatives());
        createFeedbackValues(1, newEntryRowId, model.getPositives());
    }

    public Cursor getAllEntries() {
        String buildSQL = "SELECT * FROM " + PrismContract.Entry.TABLE_NAME;

        return db().rawQuery(buildSQL, null);
    }

    /**
     * Gets full representation of an entry (along with associated Feedbacks).
     * @param date the date for which we will fetch an entry.
     * @return
     */
    public Entry getFullEntry(long date) {
        Entry entry = null;

        final String selection = "date=?";
        final String[] selectionArgs = new String[] {String.valueOf(date)};
        Cursor cursor = db().query(
                PrismContract.Entry.TABLE_NAME,
                entryAllColumns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor, true);
        }
        cursor.close();

        return entry;
    }

    private Entry cursorToEntry(Cursor cursor, boolean deepRetrieval) {
        long id = cursor.getLong(0);
        long date = cursor.getLong(1);
        String[] negatives = null;
        String[] positives = null;

        if(deepRetrieval) {
            negatives = getFeedback(0, id);
            positives = getFeedback(1, id);
        }
        return new Entry(date, negatives, positives, id);
    }

    private String[] getFeedback(long type, long entryId) {
        ArrayList<String> feedbacks = new ArrayList<String>();

        final String selection = "entry_id=? AND feedback_type=?";
        final String[] selectionArgs = new String[] {String.valueOf(entryId), String.valueOf(type)};
        Cursor cursor = db().query(
                PrismContract.Feedback.TABLE_NAME,
                feedbackAllColumns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            feedbacks.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();

        String[] feedbacksArr = new String[feedbacks.size()];
        feedbacksArr = feedbacks.toArray(feedbacksArr);
        return feedbacksArr;
    }

    /**
     * Create feedback entries for a set of feedback strings of a given type
     *
     * @param type    the type of feedback. 0 = negative, 1 = positive.
     * @param entryId the entry to associate this feedback with
     * @param values  entry values to be added (in plain text)
     */
    private void createFeedbackValues(long type, long entryId, String[] values) {
        for (String feedback : values) {
            ContentValues feedbackValues = new ContentValues();
            feedbackValues.put(PrismContract.Feedback.COLUMN_NAME_FEEDBACK_ENTRY_ID, entryId);
            feedbackValues.put(PrismContract.Feedback.COLUMN_NAME_FEEDBACK_TEXT, feedback);
            feedbackValues.put(PrismContract.Feedback.COLUMN_NAME_FEEDBACK_TYPE, type);
            db().insert(
                    PrismContract.Feedback.TABLE_NAME,
                    null,
                    feedbackValues);
        }
    }
}
