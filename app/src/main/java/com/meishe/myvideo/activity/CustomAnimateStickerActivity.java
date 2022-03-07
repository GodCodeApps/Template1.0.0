package com.meishe.myvideo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideo.view.CustomStickerDrawRect;
import com.meishe.myvideo.view.CustomTitleBar;
import com.meishe.myvideoapp.R;
import com.meishe.player.common.utils.ScreenUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import org.greenrobot.eventbus.EventBus;

public class CustomAnimateStickerActivity extends BaseActivity {
    public static final String KEY_IMAGE_FILE_PATH = "imageSrcFilePath";
    private static final int SAVE_BITMAP_FINISHED = 1002;
    private static final String TAG = "CustomStickerActivity";
    private RelativeLayout mBottomLayout;
    private RelativeLayout mCircleModeButton;
    private TextView mCircleText;
    private ImageView mCustomAnimateImage;
    private CustomStickerDrawRect mCustomDrawRect;
    private ImageView mCustomStickerFinish;
    private RelativeLayout mFreeModeButton;
    private TextView mFreeText;
    private SaveBitmapHandler mHandler = new SaveBitmapHandler(this);
    private ImageView mImageCircle;
    private String mImageDstFilePath;
    private ImageView mImageFree;
    private ImageView mImageSquare;
    private String mImageSrcFilePath;
    private int mImageViewHeight;
    private int mImageViewWidth;
    Runnable mSaveBitmapRunnable = new Runnable() {
        /* class com.meishe.myvideo.activity.CustomAnimateStickerActivity.AnonymousClass2 */

        public void run() {
            if (CustomAnimateStickerActivity.this.mShapeMode == 2003 || CustomAnimateStickerActivity.this.mShapeMode == 2005) {
                CustomAnimateStickerActivity customAnimateStickerActivity = CustomAnimateStickerActivity.this;
                Bitmap rectBitmap = customAnimateStickerActivity.getRectBitmap(customAnimateStickerActivity.mImageSrcFilePath);
                CustomAnimateStickerActivity.this.saveBitmapToLocal(rectBitmap);
                if (!rectBitmap.isRecycled()) {
                    rectBitmap.recycle();
                }
            } else if (CustomAnimateStickerActivity.this.mShapeMode == 2004) {
                CustomAnimateStickerActivity customAnimateStickerActivity2 = CustomAnimateStickerActivity.this;
                Bitmap circleBitmap = customAnimateStickerActivity2.getCircleBitmap(customAnimateStickerActivity2.mImageSrcFilePath);
                CustomAnimateStickerActivity.this.saveBitmapToLocal(circleBitmap);
                if (!circleBitmap.isRecycled()) {
                    circleBitmap.recycle();
                }
            }
            CustomAnimateStickerActivity.this.mHandler.sendEmptyMessage(1002);
        }
    };
    private RectF mShapeDrawRectF;
    private int mShapeMode = 2003;
    private RelativeLayout mSquareModeButton;
    private TextView mSquareText;
    private NvsStreamingContext mStreamingContext;
    private CustomTitleBar mTitleBar;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_custom_animate_sticker;
    }

    /* access modifiers changed from: package-private */
    public static class SaveBitmapHandler extends Handler {
        WeakReference<CustomAnimateStickerActivity> mWeakReference;

        public SaveBitmapHandler(CustomAnimateStickerActivity customAnimateStickerActivity) {
            this.mWeakReference = new WeakReference<>(customAnimateStickerActivity);
        }

        public void handleMessage(Message message) {
            CustomAnimateStickerActivity customAnimateStickerActivity = this.mWeakReference.get();
            if (customAnimateStickerActivity != null && message.what == 1002) {
                Log.d(CustomAnimateStickerActivity.TAG, "mImageDstFilePath = " + customAnimateStickerActivity.mImageDstFilePath);
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventType(22);
                EventBus.getDefault().post(messageEvent);
                this.mWeakReference.get().finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        this.mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        this.mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        this.mCustomAnimateImage = (ImageView) findViewById(R.id.customAnimateImage);
        this.mFreeModeButton = (RelativeLayout) findViewById(R.id.freeMode);
        this.mCircleModeButton = (RelativeLayout) findViewById(R.id.circleMode);
        this.mSquareModeButton = (RelativeLayout) findViewById(R.id.squareMode);
        this.mImageFree = (ImageView) findViewById(R.id.imageFree);
        this.mImageCircle = (ImageView) findViewById(R.id.imageCircle);
        this.mImageSquare = (ImageView) findViewById(R.id.imageSquare);
        this.mFreeText = (TextView) findViewById(R.id.freeText);
        this.mCircleText = (TextView) findViewById(R.id.circleText);
        this.mSquareText = (TextView) findViewById(R.id.squareText);
        this.mCustomDrawRect = (CustomStickerDrawRect) findViewById(R.id.customDrawRect);
        this.mCustomStickerFinish = (ImageView) findViewById(R.id.customStickerFinish);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        Bundle extras;
        this.mStreamingContext = EditorEngine.getInstance().getStreamingContext();
        Intent intent = getIntent();
        if (!(intent == null || (extras = intent.getExtras()) == null)) {
            this.mImageSrcFilePath = extras.getString(KEY_IMAGE_FILE_PATH);
        }
        if (setDisplayImage(this.mImageSrcFilePath)) {
            initDrawRect();
        }
    }

    private boolean setDisplayImage(String str) {
        if (str.isEmpty()) {
            return false;
        }
        Glide.with((FragmentActivity) this).load(str).into(this.mCustomAnimateImage);
        setDisplayImageSize(str);
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mFreeModeButton.setOnClickListener(this);
        this.mCircleModeButton.setOnClickListener(this);
        this.mSquareModeButton.setOnClickListener(this);
        this.mCustomStickerFinish.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
        this.mTitleBar.setTextCenter(R.string.select_media);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (!Util.isFastClick()) {
            switch (id) {
                case R.id.circleMode /*{ENCODED_INT: 2131296373}*/:
                    if (this.mShapeMode != 2004) {
                        this.mImageFree.setImageResource(R.mipmap.custom_free);
                        this.mFreeText.setTextColor(Color.parseColor("#ff909293"));
                        this.mImageCircle.setImageResource(R.mipmap.custom_circle_select);
                        this.mCircleText.setTextColor(Color.parseColor("#ff4a90e2"));
                        this.mImageSquare.setImageResource(R.mipmap.custom_square);
                        this.mSquareText.setTextColor(Color.parseColor("#ff909293"));
                        this.mShapeMode = 2004;
                        resetDrawRectF();
                        this.mCustomDrawRect.setDrawRect(this.mShapeDrawRectF, this.mShapeMode);
                        return;
                    }
                    return;
                case R.id.customStickerFinish /*{ENCODED_INT: 2131296389}*/:
                    this.mCustomStickerFinish.setClickable(false);
                    new Thread(this.mSaveBitmapRunnable).start();
                    return;
                case R.id.freeMode /*{ENCODED_INT: 2131296473}*/:
                    if (this.mShapeMode != 2003) {
                        this.mImageFree.setImageResource(R.mipmap.custom_free_select);
                        this.mFreeText.setTextColor(Color.parseColor("#ff4a90e2"));
                        this.mImageCircle.setImageResource(R.mipmap.custom_circle);
                        this.mCircleText.setTextColor(Color.parseColor("#ff909293"));
                        this.mImageSquare.setImageResource(R.mipmap.custom_square);
                        this.mSquareText.setTextColor(Color.parseColor("#ff909293"));
                        this.mShapeMode = 2003;
                        resetDrawRectF();
                        this.mCustomDrawRect.setDrawRect(this.mShapeDrawRectF, this.mShapeMode);
                        return;
                    }
                    return;
                case R.id.squareMode /*{ENCODED_INT: 2131296752}*/:
                    if (this.mShapeMode != 2005) {
                        this.mImageFree.setImageResource(R.mipmap.custom_free);
                        this.mFreeText.setTextColor(Color.parseColor("#ff909293"));
                        this.mImageCircle.setImageResource(R.mipmap.custom_circle);
                        this.mCircleText.setTextColor(Color.parseColor("#ff909293"));
                        this.mImageSquare.setImageResource(R.mipmap.custom_square_select);
                        this.mSquareText.setTextColor(Color.parseColor("#ff4a90e2"));
                        this.mShapeMode = 2005;
                        resetDrawRectF();
                        this.mCustomDrawRect.setDrawRect(this.mShapeDrawRectF, this.mShapeMode);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void setDisplayImageSize(String str) {
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        int i = this.mTitleBar.getLayoutParams().height;
        int i2 = this.mBottomLayout.getLayoutParams().height;
        NvsAVFileInfo aVFileInfo = this.mStreamingContext.getAVFileInfo(str);
        if (aVFileInfo != null) {
            ViewGroup.LayoutParams layoutParams = this.mCustomAnimateImage.getLayoutParams();
            NvsSize videoStreamDimension = aVFileInfo.getVideoStreamDimension(0);
            float f = ((float) videoStreamDimension.width) / (((float) videoStreamDimension.height) * 1.0f);
            if (f >= 1.0f) {
                int i3 = screenWidth - 200;
                layoutParams.width = i3;
                double d = (double) (((float) i3) / f);
                Double.isNaN(d);
                layoutParams.height = (int) Math.floor(d + 0.5d);
            } else {
                int i4 = (((screenHeight - statusBarHeight) - i) - i2) - 200;
                double d2 = (double) (((float) i4) * f);
                Double.isNaN(d2);
                layoutParams.width = (int) Math.floor(d2 + 0.5d);
                layoutParams.height = i4;
            }
            this.mImageViewWidth = layoutParams.width;
            this.mImageViewHeight = layoutParams.height;
            this.mCustomAnimateImage.setLayoutParams(layoutParams);
        }
    }

    private void initDrawRect() {
        this.mShapeDrawRectF = new RectF();
        resetDrawRectF();
        this.mCustomDrawRect.setImgSize(this.mImageViewWidth, this.mImageViewHeight);
        this.mCustomDrawRect.setDrawRect(this.mShapeDrawRectF, 2003);
        setDrawRectLayoutParams(this.mCustomDrawRect);
        this.mCustomDrawRect.setOnDrawRectListener(new CustomStickerDrawRect.OnDrawRectListener() {
            /* class com.meishe.myvideo.activity.CustomAnimateStickerActivity.AnonymousClass1 */

            @Override // com.meishe.myvideo.view.CustomStickerDrawRect.OnDrawRectListener
            public void onDrawRect(RectF rectF) {
                CustomAnimateStickerActivity.this.mShapeDrawRectF = rectF;
            }
        });
    }

    private void resetDrawRectF() {
        int i = this.mImageViewWidth;
        int i2 = this.mImageViewHeight;
        if (i > i2) {
            i = i2;
        }
        if (i >= 400) {
            RectF rectF = this.mShapeDrawRectF;
            int i3 = this.mImageViewWidth;
            int i4 = this.mImageViewHeight;
            rectF.set((float) ((i3 / 2) - 200), (float) ((i4 / 2) - 200), (float) ((i3 / 2) + 200), (float) ((i4 / 2) + 200));
            return;
        }
        int i5 = this.mImageViewWidth;
        float f = 0.0f;
        float f2 = i == i5 ? 0.0f : (((float) i5) * 0.5f) - (((float) this.mImageViewHeight) * 0.5f);
        int i6 = this.mImageViewWidth;
        float f3 = i == i6 ? (float) i6 : (((float) i6) * 0.5f) + (((float) this.mImageViewHeight) * 0.5f);
        int i7 = this.mImageViewWidth;
        if (i == i7) {
            f = (((float) this.mImageViewHeight) * 0.5f) - (((float) i7) * 0.5f);
        }
        int i8 = this.mImageViewWidth;
        this.mShapeDrawRectF.set(f2, f, f3, i == i8 ? (((float) this.mImageViewHeight) * 0.5f) + (((float) i8) * 0.5f) : (float) this.mImageViewHeight);
    }

    private void setDrawRectLayoutParams(CustomStickerDrawRect customStickerDrawRect) {
        RelativeLayout.LayoutParams layoutParams;
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mCustomAnimateImage.getLayoutParams();
        if (layoutParams2 != null && (layoutParams = (RelativeLayout.LayoutParams) customStickerDrawRect.getLayoutParams()) != null) {
            layoutParams.leftMargin = layoutParams2.leftMargin;
            layoutParams.topMargin = layoutParams2.topMargin;
            layoutParams.width = layoutParams2.width + customStickerDrawRect.getScaleImgBtnWidth();
            layoutParams.height = layoutParams2.height + customStickerDrawRect.getScaleImgBtnHeight();
            customStickerDrawRect.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Bitmap getRectBitmap(String str) {
        Bitmap createScaledBitmap;
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        if (decodeFile == null || (createScaledBitmap = Bitmap.createScaledBitmap(decodeFile, this.mImageViewWidth, this.mImageViewHeight, true)) == null) {
            return null;
        }
        double d = (double) this.mShapeDrawRectF.left;
        Double.isNaN(d);
        double d2 = (double) this.mShapeDrawRectF.top;
        Double.isNaN(d2);
        return Bitmap.createBitmap(createScaledBitmap, (int) Math.floor(d + 0.5d), (int) Math.floor(d2 + 0.5d), (int) (this.mShapeDrawRectF.right - this.mShapeDrawRectF.left), (int) (this.mShapeDrawRectF.bottom - this.mShapeDrawRectF.top));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveBitmapToLocal(Bitmap bitmap) {
        String bitmapFilePath;
        if (bitmap != null && (bitmapFilePath = getBitmapFilePath()) != null) {
            this.mImageDstFilePath = bitmapFilePath + "/" + String.valueOf(System.currentTimeMillis()) + ".png";
            File file = new File(this.mImageDstFilePath);
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Bitmap getCircleBitmap(String str) {
        Bitmap rectBitmap = getRectBitmap(str);
        float f = (this.mShapeDrawRectF.right - this.mShapeDrawRectF.left) / 2.0f;
        float f2 = (this.mShapeDrawRectF.bottom - this.mShapeDrawRectF.top) / 2.0f;
        if (f >= f2) {
            f = f2;
        }
        double d = (double) (2.0f * f);
        Double.isNaN(d);
        int floor = (int) Math.floor(d + 0.5d);
        Bitmap createBitmap = Bitmap.createBitmap(floor, floor, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setAntiAlias(true);
        canvas.drawCircle(f, f, f, paint);
        paint.reset();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(rectBitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    private String getBitmapFilePath() {
        File file = new File(PathUtils.getAssetDownloadPath(12));
        if (file.exists() || file.mkdirs()) {
            return file.toString();
        }
        return null;
    }
}
