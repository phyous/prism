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

import java.util.Date;

public class GraderActivity extends ActionBarActivity {
    GraderCardFragment mStartFragment;
    GraderCardFragment mStopFragment;
    public static final String ENTRY_STOP_ARRAY = "entry_stop_array";
    public static final String ENTRY_START_ARRAY = "entry_start_array";
    public static final String ENTRY_DATE = "entry_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grader);

        mStartFragment = new GraderCardFragment("Start");
        mStopFragment = new GraderCardFragment("Stop");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.start, mStartFragment)
                    .add(R.id.stop, mStopFragment)
                    .commit();
        }

        final String dateText = DateHelper.getDateTitle(new Date(2013, 11, 14));
        TextView dateTitle = (TextView) findViewById(R.id.date_title);
        dateTitle.setText(dateText);

        final ImageView button = (ImageView) findViewById(R.id.arrow_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraderActivity.this, TimelineActivity.class);
                String[] startFeedback = getFeedbacksFromFragment(mStartFragment);
                String[] stopFeedback = getFeedbacksFromFragment(mStopFragment);
                long date = DateHelper.getDateLong(new Date(2013, 11, 14));
                intent.putExtra(ENTRY_STOP_ARRAY, stopFeedback);
                intent.putExtra(ENTRY_START_ARRAY, startFeedback);
                intent.putExtra(ENTRY_DATE, date);

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
