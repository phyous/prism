package com.phyous.prism.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void createOrUpdateEntry(Entry model) {
        Cursor cursor = getCursorByField(PrismContract.Entry.COLUMN_NAME_DATE,
                String.valueOf(model.getDate()));
        int numExistingEntries = cursor.getCount();
        if (numExistingEntries > 0) {
            long id = cursor.getLong(0);
            model.setId(id);
            updateEntry(model);
        } else {
            createEntry(model);
        }
    }

    private void updateEntry(Entry model) {
        updateFeedbackValues(0, model.getId(), model.getNegatives());
        updateFeedbackValues(1, model.getId(), model.getPositives());
        updateFeedbackValues(2, model.getId(), model.getNext());
    }

    private void createEntry(Entry model) {
        ContentValues entryValues = new ContentValues();
        entryValues.put(PrismContract.Entry.COLUMN_NAME_DATE, model.getDate());

        long newEntryRowId = db().insert(
                PrismContract.Entry.TABLE_NAME,
                null,
                entryValues);

        createFeedbackValues(0, newEntryRowId, model.getNegatives());
        createFeedbackValues(1, newEntryRowId, model.getPositives());
        createFeedbackValues(2, newEntryRowId, model.getNext());
    }

    public Cursor getAllEntries() {
        String buildSQL =
                "SELECT * FROM " +
                PrismContract.Entry.TABLE_NAME +
                " ORDER BY " +
                PrismContract.Entry.COLUMN_NAME_DATE +
                " DESC";
        return db().rawQuery(buildSQL, null);
    }

    /**
     * Gets full representation of an entry (along with associated Feedbacks) by id.
     *
     * @param id the entry_id for which we will fetch an entry.
     * @return
     */
    public Entry getEntryById(long id) {
        Entry entry = null;
        Cursor cursor = getCursorByField(PrismContract.Entry._ID, String.valueOf(id));

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor, true);
        }
        cursor.close();

        return entry;
    }

    private Cursor getCursorByField(String field, String value) {
        final String selection = field+"=?";
        final String[] selectionArgs = new String[]{value};
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
        return cursor;
    }

    private Entry cursorToEntry(Cursor cursor, boolean deepRetrieval) {
        long id = cursor.getLong(0);
        long date = cursor.getLong(1);
        ArrayList<String> negatives = null;
        ArrayList<String> positives = null;
        ArrayList<String> next = null;

        if (deepRetrieval) {
            negatives = getFeedback(0, id);
            positives = getFeedback(1, id);
            next = getFeedback(2, id);
        }
        return new Entry(date, negatives, positives, next, id);
    }

    private ArrayList<String> getFeedback(long type, long entryId) {
        ArrayList<String> feedbacks = new ArrayList<String>();

        final String selection = "entry_id=? AND feedback_type=?";
        final String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(type)};
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
        return new ArrayList<String>(Arrays.asList(feedbacksArr));
    }

    /**
     * Create feedback entries for a set of feedback strings of a given type
     *
     * @param type    the type of feedback. 0 = negative, 1 = positive.
     * @param entryId the entry to associate this feedback with
     * @param values  entry values to be added (in plain text)
     */
    private void createFeedbackValues(long type, long entryId, List<String> values) {
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

    private void deleteFeedbackValues(long type, long entryId) {
        final String selection = "entry_id=? AND feedback_type=?";
        final String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(type)};
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
            String id = String.valueOf(cursor.getLong(0));
            db().delete(PrismContract.Feedback.TABLE_NAME, "_id=?", new String[]{id});
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void updateFeedbackValues(long type, long entryId, List<String> values) {
        deleteFeedbackValues(type, entryId);
        createFeedbackValues(type, entryId, values);
    }
}
