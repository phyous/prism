package com.phyous.prism;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyous.prism.util.DateHelper;

import java.util.ArrayList;

import static com.phyous.prism.util.DateHelper.getCurrentDateStartLong;

public class GraderActivity extends ActionBarActivity {
    GraderCardFragment mPosFragment;
    GraderCardFragment mNegFragment;
    GraderCardFragment mNextFragment;
    private long mGraderTimeMillis;
    public static final String ENTRY_POS_ARRAY = "entry_pos_array";
    public static final String ENTRY_NEG_ARRAY = "entry_neg_array";
    public static final String ENTRY_NEXT_ARRAY = "entry_next_array";

    public static final String FRAGMENTID_POS_ARRAY = "PosFragment";
    public static final String FRAGMENTID_NEG_ARRAY = "NegFragment";
    public static final String FRAGMENTID_NEXT_ARRAY = "NextFragment";

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
        ArrayList<String> posTextArray, negTextArray, nextTextArray;
        if (entryDate == 0) {
            mGraderTimeMillis = getCurrentDateStartLong();
            posTextArray = new ArrayList<String>();
            negTextArray = new ArrayList<String>();
            nextTextArray = new ArrayList<String>();
        } else {
            mGraderTimeMillis = entryDate;
            posTextArray = extras.getStringArrayList(ENTRY_NEG_ARRAY);
            negTextArray = extras.getStringArrayList(ENTRY_POS_ARRAY);
            nextTextArray = extras.getStringArrayList(ENTRY_NEXT_ARRAY);
        }

        if (savedInstanceState != null) {
            mPosFragment = (GraderCardFragment)
                    getSupportFragmentManager().findFragmentByTag(FRAGMENTID_POS_ARRAY);
            mNegFragment = (GraderCardFragment)
                    getSupportFragmentManager().findFragmentByTag(FRAGMENTID_NEG_ARRAY);
            mNextFragment = (GraderCardFragment)
                    getSupportFragmentManager().findFragmentByTag(FRAGMENTID_NEXT_ARRAY);
        } else {
            mPosFragment = new GraderCardFragment(
                    getString(R.string.praise_title),
                    posTextArray,
                    R.color.green_plus);
            mNegFragment = new GraderCardFragment(
                    getString(R.string.improve_title),
                    negTextArray,
                    R.color.red_minus);
            mNextFragment = new GraderCardFragment(
                    getString(R.string.focus_title),
                    nextTextArray,
                    R.color.yellow_next);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.praise, mPosFragment, FRAGMENTID_POS_ARRAY)
                    .add(R.id.improve, mNegFragment, FRAGMENTID_NEG_ARRAY)
                    .add(R.id.focus, mNextFragment, FRAGMENTID_NEXT_ARRAY)
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
                ArrayList<String> posFeedback = getFeedbackFromFragment(mPosFragment);
                ArrayList<String> negFeedback = getFeedbackFromFragment(mNegFragment);
                ArrayList<String> nextFeedback = getFeedbackFromFragment(mNextFragment);
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

    private ArrayList<String> getFeedbackFromFragment(GraderCardFragment fragment) {
        return fragment.getTextEntries();
    }
}
