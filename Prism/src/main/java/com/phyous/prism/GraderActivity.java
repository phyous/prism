package com.phyous.prism;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyous.prism.util.DateHelper;

import static com.phyous.prism.util.DateHelper.getCurrentDateStartLong;

public class GraderActivity extends ActionBarActivity {
    GraderCardFragment mPosFragment;
    GraderCardFragment mNegFragment;
    GraderCardFragment mNextFragment;
    private long mGraderTimeMillis;
    private String[] mPosTextArray;
    private String[] mNegTextArray;
    private String[] mNextTextArray;
    public static final String ENTRY_POS_ARRAY = "entry_pos_array";
    public static final String ENTRY_NEG_ARRAY = "entry_neg_array";
    public static final String ENTRY_NEXT_ARRAY = "entry_next_array";
    public static final String ENTRY_DATE = "entry_date";

    public GraderActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grader);

        Bundle extras = getIntent().getExtras();
        long entryDate = extras == null ? 0L : extras.getLong(ENTRY_DATE, 0L);
        if (entryDate == 0) {
            mGraderTimeMillis = getCurrentDateStartLong();
            mPosTextArray = new String[0];
            mNegTextArray = new String[0];
            mNextTextArray = new String[0];
        } else {
            mGraderTimeMillis = entryDate;
            mPosTextArray = extras.getStringArray(ENTRY_NEG_ARRAY);
            mNegTextArray = extras.getStringArray(ENTRY_POS_ARRAY);
            mNextTextArray = extras.getStringArray(ENTRY_NEXT_ARRAY);
        }

        mPosFragment = new GraderCardFragment("Praise", mPosTextArray, R.color.green_plus);
        mNegFragment = new GraderCardFragment("Improve", mNegTextArray, R.color.red_minus);
        mNextFragment = new GraderCardFragment("Focus", mNextTextArray, R.color.yellow_next);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.praise, mPosFragment)
                    .add(R.id.improve, mNegFragment)
                    .add(R.id.focus, mNextFragment)
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
                String[] posFeedback = getFeedbackFromFragment(mPosFragment);
                String[] negFeedback = getFeedbackFromFragment(mNegFragment);
                String[] nextFeedback = getFeedbackFromFragment(mNextFragment);
                intent.putExtra(ENTRY_POS_ARRAY, posFeedback);
                intent.putExtra(ENTRY_NEG_ARRAY, negFeedback);
                intent.putExtra(ENTRY_NEXT_ARRAY, nextFeedback);
                intent.putExtra(ENTRY_DATE, mGraderTimeMillis);

                GraderActivity.this.setResult(RESULT_OK, intent);

                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private String[] getFeedbackFromFragment(GraderCardFragment fragment) {
        EditText editText = (EditText) fragment.getView().findViewById(R.id.text_entry);
        String rawFeedbackText = editText.getText().toString();
        String[] splits = rawFeedbackText.split("\n");
        return splits;
    }
}
