package com.phyous.prism;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phyous.prism.util.PositionalTextWatcher;
import com.phyous.prism.widget.TypefacedTextView;

import java.util.ArrayList;

public class GraderCardFragment extends Fragment {
    private String mTitle;
    private ArrayList<String> mTextEntries;
    private int mColor;
    private String mHintText;
    private LinearLayout mLinearLayout;
    private boolean mInitialized = false;

    private static final String TITLE_INDEX = "title_index";
    private static final String INITIAL_TEXT_INDEX = "initial_text_index";
    private static final String COLOR_INDEX = "color_index";

    public GraderCardFragment() {
        mTitle = null;
        mTextEntries = null;
        mColor = 1;
        mHintText = "";
    }

    public GraderCardFragment(String title,
            ArrayList<String> textEntries,
            int color,
            String hintText) {
        mTitle = title;
        mTextEntries = textEntries;
        mColor = color;
        mHintText = hintText;
    }

    public ArrayList<String> getTextEntries() {
        return mTextEntries;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_INDEX);
            mTextEntries = savedInstanceState.getStringArrayList(INITIAL_TEXT_INDEX);
            mColor = savedInstanceState.getInt(COLOR_INDEX);
        }

        final View rootView = inflater.inflate(R.layout.fragment_grader_card, container, false);
        final TypefacedTextView tv = (TypefacedTextView) rootView.findViewById(R.id.title);
        tv.setText(mTitle);

        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.text_entry_list);

        RelativeLayout innerBox = (RelativeLayout) rootView.findViewById(R.id.inner_box);
        GradientDrawable layoutBG = (GradientDrawable) innerBox.getBackground();
        layoutBG.setColor(getResources().getColor(mColor));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        TODO: This piece of code is hacky. It should be replaced. A couple of things going on here:
        1- On screen rotation, the textwatcher of the edittext boxes are being hit improperly.
           mInitialized avoid this.
        2- initializeTextEntryList() populates the views after everything
           is initialized to ensure data is kept between rotations.
         */
        if (!mInitialized) {
            mInitialized = true;
            initializeTextEntryList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_INDEX, mTitle);
        savedInstanceState.putStringArrayList(INITIAL_TEXT_INDEX, mTextEntries);
        savedInstanceState.putInt(COLOR_INDEX, mColor);
    }

    private void initializeTextEntryList() {
        for (int i = 0; i < getAllCells(); i++) {
            createListRow(i);
        }
    }

    private void createTextEntryRow() {
        createListRow(getFilledCells());
    }

    private void updateTextEntryRow(View row, Editable s) {
        int childPos = mLinearLayout.indexOfChild(row);
        mTextEntries.set(childPos, s.toString());
    }

    private void deleteTextEntryRow(View row) {
        int childPos = mLinearLayout.indexOfChild(row);
        mLinearLayout.removeView(row);
        mTextEntries.remove(childPos);

        int previousChild = childPos - 1;
        if (previousChild >= 0) {
            View rowView = mLinearLayout.getChildAt(previousChild);
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
    private void createListRow(final int position) {
        ViewGroup parent = mLinearLayout;
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.grader_row_item, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.text_entry);
        textView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        textView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ImageView image = (ImageView) rowView.findViewById(R.id.list_nub);

        PositionalTextWatcher tw;
        if (position >= getFilledCells()) {
            tw = createTextWatcher(true, rowView);
            image.setImageResource(R.drawable.ic_drawer_light);
            textView.setHint(mHintText);
            int hintTextColor = getResources().getColor(R.color.hint_text_grey);
            textView.setHintTextColor(hintTextColor);
        } else {
            tw = createTextWatcher(false, rowView);
            String text = mTextEntries.get(position);
            textView.setText(text);
            image.setImageResource(R.drawable.ic_drawer_dark);
        }

        textView.addTextChangedListener(tw);

        parent.addView(rowView);
    }

    private PositionalTextWatcher createTextWatcher(final boolean forEmptyCell, View rowView) {
        return new PositionalTextWatcher(rowView) {
            @Override
            public void afterTextChanged(Editable s) {
                if (mInitialized) {
                    if (s.length() > 0 && !getChangedState() && forEmptyCell) {
                        setChangedState(true);
                        createTextEntryRow();
                        mTextEntries.add(s.toString());
                        darkenFilledCell();
                    } else if (s.length() == 0) {
                        deleteTextEntryRow(getRowView());
                    } else {
                        updateTextEntryRow(getRowView(), s);
                    }
                }
            }
        };
    }

    /**
     * Get number of cells filled with text
     * @return number of cells
     */
    private int getFilledCells() {
        return mTextEntries.size();
    }

    /**
     * Get total number of cells filled with text and empty
     * @return number of cells
     */
    private int getAllCells() {
        return mTextEntries.size() + 1;
    }
}
