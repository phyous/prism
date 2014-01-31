package com.phyous.prism;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.View.MeasureSpec;

import com.phyous.prism.widget.TypefacedTextView;

import java.util.ArrayList;

public class GraderCardFragment extends Fragment {
    private String mTitle;
    private ArrayList<String> mInitialText;
    private int mColor;

    private static final String TITLE_INDEX = "title_index";
    private static final String INITIAL_TEXT_INDEX = "initial_text_index";
    private static final String COLOR_INDEX = "color_index";

    public GraderCardFragment() {
        mTitle = null;
        mInitialText = null;
        mColor = 1;
    }


    public GraderCardFragment(String title, ArrayList<String> initialText, int color) {
        mTitle = title;
        mInitialText = initialText;
        mColor = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_INDEX);
            mInitialText = savedInstanceState.getStringArrayList(INITIAL_TEXT_INDEX);
            mColor = savedInstanceState.getInt(COLOR_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        ListView listView = (ListView) rootView.findViewById(R.id.text_entry_list);
        mInitialText.add("");
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.grader_row_active, R.id.text_entry, mInitialText);
        listView.setAdapter(adapter);
        updateListviewHeight(listView);

        RelativeLayout innerBox = (RelativeLayout) rootView.findViewById(R.id.inner_box);
        GradientDrawable layoutBG = (GradientDrawable) innerBox.getBackground();
        layoutBG.setColor(getResources().getColor(mColor));

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_INDEX, mTitle);
        savedInstanceState.putStringArrayList(INITIAL_TEXT_INDEX, mInitialText);
        savedInstanceState.putInt(COLOR_INDEX, mColor);
    }

    private void updateListviewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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