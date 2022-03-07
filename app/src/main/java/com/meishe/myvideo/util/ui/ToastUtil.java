package com.meishe.myvideo.util.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mTextCenterToast;
    private static Toast mTextToast;
    private static Toast mViewToast;

    public static void showToast(Context context, int i) {
        String string = context.getResources().getString(i);
        if (!TextUtils.isEmpty(string)) {
            showToast(context, string);
        }
    }

    public static void showCenterToast(Context context, int i) {
        String string = context.getResources().getString(i);
        if (!TextUtils.isEmpty(string)) {
            showCenterToast(context, string);
        }
    }

    public static void showShortToast(Context context, int i) {
        if (!TextUtils.isEmpty(context.getResources().getString(i))) {
            Toast.makeText(context, i, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastCenter(Context context, String str) {
        Toast makeText = Toast.makeText(context.getApplicationContext(), str, 0);
        makeText.setGravity(17, 0, 0);
        TextView textView = (TextView) makeText.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        if (textView != null) {
            textView.setGravity(17);
        }
        makeText.show();
    }

    public static void showToastCenterWithBg(Context context, String str, String str2, int i) {
        Toast makeText = Toast.makeText(context.getApplicationContext(), "", 0);
        makeText.setText(str);
        makeText.setGravity(17, 0, 0);
        makeText.getView().setBackgroundResource(i);
        TextView textView = (TextView) makeText.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        textView.setTextColor(Color.parseColor(str2));
        textView.setTextSize(2, 16.0f);
        if (textView != null) {
            textView.setGravity(17);
        }
        makeText.show();
    }

    public static void showToast(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            if (mTextToast == null) {
                mTextToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            }
            mTextToast.setText(str);
            mTextToast.show();
        }
    }

    public static void showCenterToast(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            if (mTextCenterToast == null) {
                mTextCenterToast = Toast.makeText(context, "", 0);
            }
            mTextCenterToast.setText(str);
            mTextCenterToast.setGravity(17, 0, 0);
            mTextCenterToast.show();
        }
    }

    public static Toast getToast() {
        Toast toast = mTextToast;
        return toast == null ? mViewToast : toast;
    }

    public static void releaseInstance() {
        mTextToast = null;
        mViewToast = null;
    }

    private static class SafelyHandlerWarpper extends Handler {
        private Handler impl;

        public SafelyHandlerWarpper(Handler handler) {
            this.impl = handler;
        }

        public void handleMessage(Message message) {
            this.impl.handleMessage(message);
        }

        public void dispatchMessage(Message message) {
            try {
                super.dispatchMessage(message);
            } catch (Exception unused) {
            }
        }
    }
}
