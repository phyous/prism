package com.phyous.prism;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phyous.prism.provider.Entry;
import com.phyous.prism.provider.EntryDataSource;

import static com.phyous.prism.util.DateHelper.getDateTitle;

public class EntryListAdapter extends CursorAdapter {
    private final int STRING_SUMMARY_LENGTH = 18;
    private EntryDataSource mEntryDataSource;
    public EntryListAdapter(Context context, Cursor c) {
        super(context, c);
        mEntryDataSource = new EntryDataSource(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.timeline_row_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1 = (TextView) view.findViewById(R.id.entry_date);
        TextView tv2 = (TextView) view.findViewById(R.id.entry_summary);

        long id = cursor.getLong(0);
        long date = cursor.getLong(1);

        final String dateFormatted = getDateTitle(date);
        Entry entry =  mEntryDataSource.getEntryById(id);
        String summary = createSummary(entry);

        tv1.setText(dateFormatted);
        tv2.setText(summary);
    }

    private String createSummary(Entry entry) {
        StringBuilder sb = new StringBuilder();
        for(String s: entry.getNegatives()) {
            sb.append(s);
            sb.append(", ");
        }
        int maxLength = sb.length() > STRING_SUMMARY_LENGTH ? STRING_SUMMARY_LENGTH : sb.length();
        String summary = sb.toString().substring(0, maxLength) + "...";
        return summary;
    }


}