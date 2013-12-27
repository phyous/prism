package com.phyous.prism;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.phyous.prism.widget.TypefacedTextView;

public class GraderCardFragment extends Fragment {
    private final String mTitle;
    private String[] mInitialText;

    public GraderCardFragment(String title, String[] initialText) {
        mTitle = title;
        mInitialText = initialText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        EditText editText = (EditText) rootView.findViewById(R.id.text_entry);
        editText.setText(arrayToString(mInitialText));

        return rootView;
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