package com.meishe.myvideo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

public class CustomPopWindow implements PopupWindow.OnDismissListener {
    private static final float DEFAULT_ALPHA = 0.7f;
    private static final String TAG = "CustomPopWindow";
    private boolean enableOutsideTouchDisMiss;
    private OnViewClickListener listener;
    private int mAnimationStyle;
    private float mBackgroundDrakValue;
    private boolean mClippEnable;
    private View mContentView;
    private Context mContext;
    private int mHeight;
    private boolean mIgnoreCheekPress;
    private int mInputMode;
    private boolean mIsBackgroundDark;
    private boolean mIsFocusable;
    private boolean mIsOutside;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private View.OnTouchListener mOnTouchListener;
    private PopupWindow mPopupWindow;
    private int mResLayoutId;
    private int mSoftInputMode;
    private boolean mTouchable;
    private int mWidth;
    private Window mWindow;
    private int[] viewIds;

    public interface OnViewClickListener {
        void onViewClick(CustomPopWindow customPopWindow, View view);
    }

    private CustomPopWindow(Context context) {
        this.mIsFocusable = true;
        this.mIsOutside = true;
        this.mResLayoutId = -1;
        this.mAnimationStyle = -1;
        this.mClippEnable = true;
        this.mIgnoreCheekPress = false;
        this.mInputMode = -1;
        this.mSoftInputMode = -1;
        this.mTouchable = true;
        this.mIsBackgroundDark = false;
        this.mBackgroundDrakValue = 0.0f;
        this.enableOutsideTouchDisMiss = true;
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public CustomPopWindow showAsDropDown(View view, int i, int i2) {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.showAsDropDown(view, i, i2);
        }
        return this;
    }

    public CustomPopWindow showAsDropDown(View view) {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.showAsDropDown(view);
        }
        return this;
    }

    @RequiresApi(api = 19)
    public CustomPopWindow showAsDropDown(View view, int i, int i2, int i3) {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.showAsDropDown(view, i, i2, i3);
        }
        return this;
    }

    public CustomPopWindow showAtLocation(View view, int i, int i2, int i3) {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.showAtLocation(view, i, i2, i3);
        }
        return this;
    }

    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(this.mClippEnable);
        if (this.mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        int i = this.mInputMode;
        if (i != -1) {
            popupWindow.setInputMethodMode(i);
        }
        int i2 = this.mSoftInputMode;
        if (i2 != -1) {
            popupWindow.setSoftInputMode(i2);
        }
        PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            popupWindow.setOnDismissListener(onDismissListener);
        }
        View.OnTouchListener onTouchListener = this.mOnTouchListener;
        if (onTouchListener != null) {
            popupWindow.setTouchInterceptor(onTouchListener);
        }
        popupWindow.setTouchable(this.mTouchable);
    }

    private void setChildViewClick(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getId() != -1) {
                viewGroup.setOnClickListener(new View.OnClickListener() {
                    /* class com.meishe.myvideo.view.CustomPopWindow.AnonymousClass1 */

                    public void onClick(View view) {
                        CustomPopWindow.this.onViewClick(view);
                    }
                });
            }
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (checkViewCanClick(childAt)) {
                    childAt.setOnClickListener(new View.OnClickListener() {
                        /* class com.meishe.myvideo.view.CustomPopWindow.AnonymousClass2 */

                        public void onClick(View view) {
                            CustomPopWindow.this.onViewClick(view);
                        }
                    });
                }
                setChildViewClick(childAt);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onViewClick(View view) {
        dissmiss();
        this.listener.onViewClick(this, view);
    }

    private boolean checkViewCanClick(View view) {
        if (view.getId() == -1) {
            return false;
        }
        if (!(view instanceof TextView) && !(view instanceof Button) && !(view instanceof LinearLayout)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private PopupWindow build() {
        int i;
        if (this.mContentView == null) {
            this.mContentView = LayoutInflater.from(this.mContext).inflate(this.mResLayoutId, (ViewGroup) null);
        }
        if (this.listener != null) {
            setChildViewClick(this.mContentView);
        }
        Activity activity = (Activity) this.mContentView.getContext();
        if (activity != null && this.mIsBackgroundDark) {
            float f = this.mBackgroundDrakValue;
            if (f <= 0.0f || f >= 1.0f) {
                f = DEFAULT_ALPHA;
            }
            this.mWindow = activity.getWindow();
            WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
            attributes.alpha = f;
            this.mWindow.addFlags(2);
            this.mWindow.setAttributes(attributes);
        }
        int i2 = this.mWidth;
        if (i2 == 0 || (i = this.mHeight) == 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, -1, -1);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, i2, i);
        }
        int i3 = this.mAnimationStyle;
        if (i3 != -1) {
            this.mPopupWindow.setAnimationStyle(i3);
        }
        apply(this.mPopupWindow);
        try {
            if (this.mWidth == 0 || this.mHeight == 0) {
                this.mPopupWindow.getContentView().measure(0, 0);
                this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
                this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "build: " + e.toString());
        }
        this.mPopupWindow.setOnDismissListener(this);
        if (!this.enableOutsideTouchDisMiss) {
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setOutsideTouchable(false);
            this.mPopupWindow.setBackgroundDrawable(null);
            this.mPopupWindow.getContentView().setFocusable(true);
            this.mPopupWindow.getContentView().setFocusableInTouchMode(true);
            this.mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                /* class com.meishe.myvideo.view.CustomPopWindow.AnonymousClass3 */

                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (i != 4) {
                        return false;
                    }
                    CustomPopWindow.this.mPopupWindow.dismiss();
                    return true;
                }
            });
            this.mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                /* class com.meishe.myvideo.view.CustomPopWindow.AnonymousClass4 */

                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (motionEvent.getAction() == 0 && (x < 0 || x >= CustomPopWindow.this.mWidth || y < 0 || y >= CustomPopWindow.this.mHeight)) {
                        Log.e(CustomPopWindow.TAG, "out side ");
                        Log.e(CustomPopWindow.TAG, "width:" + CustomPopWindow.this.mPopupWindow.getWidth() + "height:" + CustomPopWindow.this.mPopupWindow.getHeight() + " x:" + x + " y  :" + y);
                        return true;
                    } else if (motionEvent.getAction() != 4) {
                        return false;
                    } else {
                        Log.e(CustomPopWindow.TAG, "out side ...");
                        return true;
                    }
                }
            });
        } else {
            this.mPopupWindow.setFocusable(this.mIsFocusable);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        }
        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    public void onDismiss() {
        dissmiss();
    }

    public void dissmiss() {
        PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
        Window window = this.mWindow;
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.alpha = 1.0f;
            this.mWindow.setAttributes(attributes);
        }
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return this.mPopupWindow;
    }

    public static class PopupWindowBuilder {
        private CustomPopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context) {
            this.mCustomPopWindow = new CustomPopWindow(context);
        }

        public PopupWindowBuilder size(int i, int i2) {
            this.mCustomPopWindow.mWidth = i;
            this.mCustomPopWindow.mHeight = i2;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean z) {
            this.mCustomPopWindow.mIsFocusable = z;
            return this;
        }

        public PopupWindowBuilder setView(int i) {
            this.mCustomPopWindow.mResLayoutId = i;
            this.mCustomPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            this.mCustomPopWindow.mContentView = view;
            this.mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean z) {
            this.mCustomPopWindow.mIsOutside = z;
            return this;
        }

        public PopupWindowBuilder setAnimationStyle(int i) {
            this.mCustomPopWindow.mAnimationStyle = i;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean z) {
            this.mCustomPopWindow.mClippEnable = z;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean z) {
            this.mCustomPopWindow.mIgnoreCheekPress = z;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int i) {
            this.mCustomPopWindow.mInputMode = i;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDismissListener) {
            this.mCustomPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        public PopupWindowBuilder setViewClickListener(OnViewClickListener onViewClickListener) {
            this.mCustomPopWindow.listener = onViewClickListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int i) {
            this.mCustomPopWindow.mSoftInputMode = i;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean z) {
            this.mCustomPopWindow.mTouchable = z;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener onTouchListener) {
            this.mCustomPopWindow.mOnTouchListener = onTouchListener;
            return this;
        }

        public PopupWindowBuilder enableBackgroundDark(boolean z) {
            this.mCustomPopWindow.mIsBackgroundDark = z;
            return this;
        }

        public PopupWindowBuilder setBgDarkAlpha(float f) {
            this.mCustomPopWindow.mBackgroundDrakValue = f;
            return this;
        }

        public PopupWindowBuilder enableOutsideTouchableDissmiss(boolean z) {
            this.mCustomPopWindow.enableOutsideTouchDisMiss = z;
            return this;
        }

        public CustomPopWindow create() {
            this.mCustomPopWindow.build();
            return this.mCustomPopWindow;
        }
    }
}
