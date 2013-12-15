package com.phyous.prism;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
                    .add(R.id.plus, startFragment)
                    .add(R.id.minus, stopFragment)
                    .commit();
        }
    }
}
