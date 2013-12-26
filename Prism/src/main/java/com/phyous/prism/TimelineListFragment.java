package com.phyous.prism;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.phyous.prism.provider.Entry;
import com.phyous.prism.provider.EntryDataSource;

public class TimelineListFragment extends Fragment {
    private EntryDataSource mEntryDataSource;
    private EntryListAdapter mEntryListAdapter;
    private ListView mListView;
    private static final int NEW_ENTRY_REQUEST_CODE = 1;

    public TimelineListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        final ImageView button = (ImageView) rootView.findViewById(R.id.plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GraderActivity.class);
                startActivity(intent);
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.listview);
        mEntryDataSource = new EntryDataSource(getActivity());
        new AsyncTask<Object, Object, Cursor>() {
            @Override
            protected Cursor doInBackground(Object[] params) {
                return mEntryDataSource.getAllEntries();
            }

            @Override
            protected void onPostExecute(Cursor result) {
                mEntryListAdapter = new EntryListAdapter(getActivity(), result);
                mListView.setAdapter(mEntryListAdapter);
            }
        }.execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Toast.makeText(getActivity(),
                        "Click ListItem Number " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return rootView;
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

            mEntryDataSource.createEntry(entry);
            mEntryListAdapter.changeCursor(mEntryDataSource.getAllEntries());
        }
    }
}
