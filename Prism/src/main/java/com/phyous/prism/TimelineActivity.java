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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.phyous.prism.provider.Entry;
import com.phyous.prism.provider.EntryDataSource;
import com.phyous.prism.service.ScheduleClient;

import java.util.ArrayList;
import java.util.Calendar;

public class TimelineActivity extends ActionBarActivity {
    private EntryDataSource mEntryDataSource;
    private EntryListAdapter mEntryListAdapter;
    private ListView mListView;
    private ScheduleClient scheduleClient;
    private static final int NEW_ENTRY_REQUEST_CODE = 1;

    // If user has less than NUX_ENTRY_MIN, show new user experience
    private static final int NUX_ENTRY_MIN = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        // Enable creating new entries
        final ImageView button = (ImageView) findViewById(R.id.plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGrader();
            }
        });

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
    }

    private void addNewUserExperience() {
        LinearLayout header =
                (LinearLayout) getLayoutInflater().inflate(R.layout.timeline_nux_header, null);
        mListView.addHeaderView(header);
    }

    private void startGrader() {
        // TODO: Move setupAlarm call somewhere appropriate. App initialization?
        setupAlarm();
        Intent intent = new Intent(TimelineActivity.this, GraderActivity.class);
        startActivityForResult(intent, NEW_ENTRY_REQUEST_CODE);
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
    protected void onStop() {
        if (scheduleClient != null) {
            scheduleClient.doUnbindService();
        }
        super.onStop();
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

    public void setupAlarm() {
        // TODO: Add user settings to control when alarm goes off (or if it goes off at all)
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 22);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        scheduleClient.setAlarmForNotification(c);
    }
}
