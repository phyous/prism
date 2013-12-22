package com.phyous.prism;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phyous.prism.widget.TypefacedTextView;

public class GraderCardFragment extends Fragment {
    private final String mTitle;

    public GraderCardFragment(String title) {
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        return rootView;
    }
}