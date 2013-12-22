package com.phyous.prism;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyous.prism.util.DateHelper;

import java.util.Date;

public class GraderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grader);

        Fragment startFragment = new GraderCardFragment("Stop");
        Fragment stopFragment = new GraderCardFragment("Start");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.start, startFragment)
                    .add(R.id.stop, stopFragment)
                    .commit();
        }

        final String dateText = DateHelper.getDateTitle(new Date(2013,11,14));
        TextView dateTitle = (TextView) findViewById(R.id.date_title);
        dateTitle.setText(dateText);

        final ImageView button = (ImageView) findViewById(R.id.arrow_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraderActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });
    }
}
