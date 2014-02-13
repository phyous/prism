package com.phyous.prism;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phyous.prism.util.PositionalTextWatcher;
import com.phyous.prism.widget.TypefacedTextView;

import java.util.ArrayList;

public class GraderCardFragment extends Fragment {
    private String mTitle;
    private ArrayList<String> mInitialText;
    private int mColor;
    private LinearLayout mLinearLayout;

    private static final String TITLE_INDEX = "title_index";
    private static final String INITIAL_TEXT_INDEX = "initial_text_index";
    private static final String COLOR_INDEX = "color_index";


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

        final View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        final TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.text_entry_list);
        initializeTextEntryList();

        RelativeLayout innerBox = (RelativeLayout) rootView.findViewById(R.id.inner_box);
        GradientDrawable layoutBG = (GradientDrawable) innerBox.getBackground();
        layoutBG.setColor(getResources().getColor(mColor));

        return rootView;
    }

    private void initializeTextEntryList() {
        for (int i = 0; i < getAllCells(); i++) {
            createListRow(i);
        }
    }

    private void addTextEntryRow() {
        createListRow(getFilledCells());
    }

    private void deleteTextEntryRow(View row) {
        int cursorPos = mLinearLayout.indexOfChild(row) - 1;
        mLinearLayout.removeView(row);

        if(cursorPos >= 0) {
            View rowView = mLinearLayout.getChildAt(cursorPos);
            TextView textView = (TextView) rowView.findViewById(R.id.text_entry);
            textView.requestFocus();
        }
    }

    private void darkenFilledCell() {
        int secondToLast = mLinearLayout.getChildCount() - 2;
        View rowView = mLinearLayout.getChildAt(secondToLast);
        ImageView image = (ImageView) rowView.findViewById(R.id.list_nub);
        image.setImageResource(R.drawable.ic_drawer_dark);
    }

    /**
     * Create a listrow for the linear layout of the fragment at a given position
     * @param position where to create the row in the LinearLayout. If position >= getFilledCells()
     *                 we will lighten the drawer icon to the left of the EditText field.
     */
    private void createListRow(int position) {
        ViewGroup parent = mLinearLayout;
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.grader_row_item, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.text_entry);
        ImageView image = (ImageView) rowView.findViewById(R.id.list_nub);

        if (position >= getFilledCells()) {
            image.setImageResource(R.drawable.ic_drawer_light);
            textView.addTextChangedListener(new PositionalTextWatcher(rowView) {
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0 && !getChangedState()) {
                        setChangedState(true);
                        addTextEntryRow();
                        mInitialText.add(s.toString());
                        darkenFilledCell();
                    } else if (s.length() == 0 && getChangedState()) {
                        deleteTextEntryRow(getParentRow());
                    } else {
                        mInitialText.set(mInitialText.size()-1, s.toString());
                    }
                }
            });
        } else {
            String text = mInitialText.get(position);
            textView.setText(text);
            image.setImageResource(R.drawable.ic_drawer_dark);
        }
        parent.addView(rowView);
    }

    /**
     * Get number of cells filled with text
     * @return number of cells
     */
    private int getFilledCells() {
        return mInitialText.size();
    }

    /**
     * Get total number of cells filled with text and empty
     * @return number of cells
     */
    private int getAllCells() {
        return mInitialText.size() + 1;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_INDEX, mTitle);
        savedInstanceState.putStringArrayList(INITIAL_TEXT_INDEX, mInitialText);
        savedInstanceState.putInt(COLOR_INDEX, mColor);
    }
}
