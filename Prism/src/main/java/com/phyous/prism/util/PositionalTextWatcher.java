package com.phyous.prism.util;

import android.text.TextWatcher;
import android.view.View;

public abstract class PositionalTextWatcher implements TextWatcher{
    private View mRow;
    private boolean mChanged = false;

    public PositionalTextWatcher(View row) {
        mRow = row;
    }

    public View getRowView() {
        return mRow;
    }

    public boolean getChangedState() {
        return mChanged;
    }

    public void setChangedState(boolean changed) {
        mChanged = changed;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
}
