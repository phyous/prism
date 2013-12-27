package com.phyous.prism;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyous.prism.util.DateHelper;

import static com.phyous.prism.util.DateHelper.getCurrentDateStartLong;

public class GraderActivity extends ActionBarActivity {
    GraderCardFragment mStartFragment;
    GraderCardFragment mStopFragment;
    private long mGraderTimeMillis;
    private String[] mStartTextArray;
    private String[] mStopTextArray;
    public static final String ENTRY_STOP_ARRAY = "entry_stop_array";
    public static final String ENTRY_START_ARRAY = "entry_start_array";
    public static final String ENTRY_DATE = "entry_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grader);

        Bundle extras = getIntent().getExtras();
        long entryDate = extras == null ? 0L : extras.getLong(ENTRY_DATE, 0L);
        if (entryDate == 0) {
            mGraderTimeMillis = getCurrentDateStartLong();
            mStartTextArray = new String[0];
            mStopTextArray = new String[0];
        } else {
            mGraderTimeMillis = entryDate;
            mStartTextArray = extras.getStringArray(ENTRY_START_ARRAY);
            mStopTextArray = extras.getStringArray(ENTRY_STOP_ARRAY);
        }

        mStartFragment = new GraderCardFragment("Start", mStartTextArray);
        mStopFragment = new GraderCardFragment("Stop", mStopTextArray);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.start, mStartFragment)
                    .add(R.id.stop, mStopFragment)
                    .commit();
        }

        final String dateText = DateHelper.getDateTitle(mGraderTimeMillis);
        TextView dateTitle = (TextView) findViewById(R.id.date_title);
        dateTitle.setText(dateText);

        final ImageView button = (ImageView) findViewById(R.id.arrow_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraderActivity.this, TimelineActivity.class);
                String[] startFeedback = getFeedbacksFromFragment(mStartFragment);
                String[] stopFeedback = getFeedbacksFromFragment(mStopFragment);
                intent.putExtra(ENTRY_STOP_ARRAY, stopFeedback);
                intent.putExtra(ENTRY_START_ARRAY, startFeedback);
                intent.putExtra(ENTRY_DATE, mGraderTimeMillis);

                GraderActivity.this.setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    private String[] getFeedbacksFromFragment(GraderCardFragment fragment) {
        EditText editText = (EditText) fragment.getView().findViewById(R.id.text_entry);
        String rawFeedbackText = editText.getText().toString();
        String[] splits = rawFeedbackText.split("\n");
        return splits;
    }
}
