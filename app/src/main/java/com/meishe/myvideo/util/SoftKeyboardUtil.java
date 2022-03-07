package com.meishe.myvideo.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyboardUtil {
    public static void showInput(Context context, EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(editText, 1);
    }

    public static void hideInput(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        if (editText != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
