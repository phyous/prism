package com.phyous.prism;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.phyous.prism.util.DateHelper.getDateTitle;

public class EntryListAdapter extends CursorAdapter {
    public EntryListAdapter(Context context, Cursor c) {
        super(context, c);
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

        final String dateFormatted = getDateTitle(cursor.getLong(1));

        tv1.setText(dateFormatted);
        tv2.setText("Some Summary....");
    }


}