package com.phyous.prism;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.phyous.prism.provider.Entry;
import com.phyous.prism.provider.EntryDataSource;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {
    private EntryDataSource mEntryDataSource;
    private EntryListAdapter mEntryListAdapter;
    private ListView mListView;
    private static final int NEW_ENTRY_REQUEST_CODE = 1;

    // If user has less than NUX_ENTRY_MIN, show new user experience
    private static final int NUX_ENTRY_MIN = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set data for ListView
        mListView = (ListView) findViewById(R.id.listview);
        mEntryDataSource = new EntryDataSource(this);
        updateData();

        // Enable editing of existing items
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(TimelineActivity.this, GraderActivity.class);
                final Entry entry = mEntryDataSource.getEntryById(id);
                if (entry == null) {
                    return;
                }
                intent.putExtra(GraderActivity.ENTRY_DATE, entry.getDate());
                intent.putExtra(GraderActivity.ENTRY_POS_ARRAY, entry.getNegatives());
                intent.putExtra(GraderActivity.ENTRY_NEG_ARRAY, entry.getPositives());
                intent.putExtra(GraderActivity.ENTRY_NEXT_ARRAY, entry.getNext());

                startActivityForResult(intent, NEW_ENTRY_REQUEST_CODE);
            }
        });

        // Enable creating new entries
        final ImageView button = (ImageView) findViewById(R.id.plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineActivity.this, GraderActivity.class);
                startActivityForResult(intent, TimelineActivity.NEW_ENTRY_REQUEST_CODE);
            }
        });

        // Enable settings menu access
        final ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimelineActivity.this, SettingsActivity.class));
            }
        });
    }

    private void addNewUserExperience() {
        LinearLayout header =
                (LinearLayout) getLayoutInflater().inflate(R.layout.timeline_nux_header, null);
        mListView.addHeaderView(header);
    }

    private void updateData() {
        new AsyncTask<Object, Object, Cursor>() {
            @Override
            protected Cursor doInBackground(Object[] params) {
                return mEntryDataSource.getAllEntries();
            }

            @Override
            protected void onPostExecute(Cursor result) {
                if(result.getCount() < NUX_ENTRY_MIN && mListView.getHeaderViewsCount() == 0) {
                    addNewUserExperience();
                }

                if (mEntryListAdapter == null) {
                    mEntryListAdapter = new EntryListAdapter(TimelineActivity.this, result);
                    mListView.setAdapter(mEntryListAdapter);
                } else {
                    mEntryListAdapter.changeCursor(result);
                }
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ENTRY_REQUEST_CODE && resultCode == GraderActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final long date = bundle.getLong(GraderActivity.ENTRY_DATE);
            final ArrayList<String> negEntries =
                    bundle.getStringArrayList(GraderActivity.ENTRY_NEG_ARRAY);
            final ArrayList<String> posEntries =
                    bundle.getStringArrayList(GraderActivity.ENTRY_POS_ARRAY);
            final ArrayList<String> nextEntries =
                    bundle.getStringArrayList(GraderActivity.ENTRY_NEXT_ARRAY);
            final Entry entry = new Entry(date, negEntries, posEntries, nextEntries);

            mEntryDataSource.createOrUpdateEntry(entry);
            updateData();
        }
    }
}
