package com.phyous.prism;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.phyous.prism.widget.TypefacedTextView;

public class GraderCardFragment extends Fragment {
    private String mTitle;
    private String[] mInitialText;
    private int mColor;

    private static final String TITLE_INDEX = "title_index";
    private static final String INITIAL_TEXT_INDEX = "initial_text_index";
    private static final String COLOR_INDEX = "color_index";


    public GraderCardFragment() {
        mTitle = null;
        mInitialText = null;
        mColor = 1;
    }


    public GraderCardFragment(String title, String[] initialText, int color) {
        mTitle = title;
        mInitialText = initialText;
        mColor = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_INDEX);
            mInitialText = savedInstanceState.getStringArray(INITIAL_TEXT_INDEX);
            mColor = savedInstanceState.getInt(COLOR_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        EditText editText = (EditText) rootView.findViewById(R.id.text_entry);
        editText.setText(arrayToString(mInitialText));

        RelativeLayout innerBox = (RelativeLayout) rootView.findViewById(R.id.inner_box);
        GradientDrawable layoutBG = (GradientDrawable) innerBox.getBackground();
        layoutBG.setColor(getResources().getColor(mColor));

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_INDEX, mTitle);
        savedInstanceState.putStringArray(INITIAL_TEXT_INDEX, mInitialText);
        savedInstanceState.putInt(COLOR_INDEX, mColor);
    }

    private String arrayToString(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for(String s: arr) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}