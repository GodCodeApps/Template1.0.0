package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;

public class MYEditorParentLayout extends RelativeLayout {
    private static final float MAX_SCALE = 15.0f;
    private static final float MIN_SCALE = 0.1f;
    private static final String TAG = "MYEditorParentLayout";
    boolean isTWO = false;
    private float mScale = 1.0f;
    private ScaleGestureDetector mScaleDetector;

    public MYEditorParentLayout(Context context) {
        super(context);
        init(context);
    }

    public MYEditorParentLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYEditorParentLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        motionEvent.getPointerCount();
        if (motionEvent.getActionMasked() == 5) {
            this.isTWO = true;
        } else if (motionEvent.getActionMasked() == 6) {
            this.isTWO = false;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.isTWO = false;
        }
        return this.isTWO;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.isTWO = false;
        }
        this.mScaleDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: private */
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            if (MYEditorParentLayout.this.mScale > MYEditorParentLayout.MAX_SCALE) {
                if (scaleFactor < 1.0f) {
                    MYEditorParentLayout.this.mScale *= scaleFactor;
                }
            } else if (MYEditorParentLayout.this.mScale >= MYEditorParentLayout.MIN_SCALE) {
                MYEditorParentLayout.this.mScale *= scaleFactor;
            } else if (scaleFactor > 1.0f) {
                MYEditorParentLayout.this.mScale *= scaleFactor;
            }
            if (MYEditorParentLayout.this.mScale > MYEditorParentLayout.MAX_SCALE || MYEditorParentLayout.this.mScale < MYEditorParentLayout.MIN_SCALE) {
                return false;
            }
            PixelPerMicrosecondUtil.setScale(MYEditorParentLayout.this.mScale);
            return true;
        }
    }
}
