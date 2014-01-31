package com.phyous.prism;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryArrayAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] mValues;


    public EntryArrayAdapter(Context context, String[] values) {
        super(context, R.layout.grader_row_item, values);
        this.mContext = context;
        this.mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.grader_row_item, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.text_entry);
        ImageView image = (ImageView) rowView.findViewById(R.id.list_nub);

        String text = mValues[position];
        textView.setText(text);

        if (text.isEmpty()) {
            image.setImageResource(R.drawable.ic_drawer_light);
        } else {
            image.setImageResource(R.drawable.ic_drawer_dark);
        }

        return rowView;
    }
}
