package com.phyous.prism.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/*
    A custom EditText class that allows the keyboard to set "Done" instead of newline when in
    Multi-line mode. See here for explanation:
    http://stackoverflow.com/questions/5014219/multiline-edittext-with-done-softinput-action-label-on-2-3
 */
public class CustomMultiLineEditText extends EditText {
    public CustomMultiLineEditText(Context context) {
        super(context);
    }

    public CustomMultiLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMultiLineEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions & EditorInfo.IME_MASK_ACTION;
        if ((imeActions & EditorInfo.IME_ACTION_DONE) != 0) {
            outAttrs.imeOptions ^= imeActions;
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_NEXT;
        }
        if ((outAttrs.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return connection;
    }
}
