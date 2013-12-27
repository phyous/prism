package com.phyous.prism;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.phyous.prism.provider.Entry;
import com.phyous.prism.provider.EntryDataSource;

public class TimelineActivity extends ActionBarActivity {
    private EntryDataSource mEntryDataSource;
    private EntryListAdapter mEntryListAdapter;
    private ListView mListView;
    private static final int NEW_ENTRY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Enable creating new entries
        final ImageView button = (ImageView) findViewById(R.id.plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineActivity.this, GraderActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST_CODE);
            }
        });

        // Set data for listview
        mListView = (ListView) findViewById(R.id.listview);
        mEntryDataSource = new EntryDataSource(this);
        new AsyncTask<Object, Object, Cursor>() {
            @Override
            protected Cursor doInBackground(Object[] params) {
                return mEntryDataSource.getAllEntries();
            }

            @Override
            protected void onPostExecute(Cursor result) {
                mEntryListAdapter = new EntryListAdapter(TimelineActivity.this, result);
                mListView.setAdapter(mEntryListAdapter);
            }
        }.execute();

        // Enable editing of existing items
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(TimelineActivity.this, GraderActivity.class);
                final Entry entry = mEntryDataSource.getEntryById(id);
                intent.putExtra(GraderActivity.ENTRY_DATE, entry.getDate());
                intent.putExtra(GraderActivity.ENTRY_STOP_ARRAY, entry.getNegatives());
                intent.putExtra(GraderActivity.ENTRY_START_ARRAY, entry.getPositives());

                startActivityForResult(intent, NEW_ENTRY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ENTRY_REQUEST_CODE && resultCode == GraderActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final long date = bundle.getLong(GraderActivity.ENTRY_DATE);
            final String[] stopEntries = bundle.getStringArray(GraderActivity.ENTRY_STOP_ARRAY);
            final String[] startEntries = bundle.getStringArray(GraderActivity.ENTRY_START_ARRAY);
            final Entry entry = new Entry(date, stopEntries, startEntries);

            mEntryDataSource.createOrUpdateEntry(entry);
            mEntryListAdapter.changeCursor(mEntryDataSource.getAllEntries());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
