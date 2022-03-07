package com.meishe.myvideo.ui.trackview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsWaveformView;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.audio.AudioWaveView;
import com.meishe.myvideo.ui.Utils.BorderRadiusDrawableUtil;
import com.meishe.myvideo.ui.Utils.DisplayUtil;
import com.meishe.myvideo.ui.Utils.FormatUtils;
import com.meishe.myvideo.ui.bean.BaseUIClip;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideoapp.R;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BaseItemView extends RelativeLayout {
    private static final String TAG = "BaseItemView";
    private HandView handView = null;
    private AudioWaveView mAudioWaveView = null;
    private BaseUIClip mBaseUIClip;
    private Context mContext;
    private TextView mDuringTextView;
    private ImageView mIconImageView;
    private ImageView mImageSpeed;
    private View mLeftBlackView;
    private NvsMultiThumbnailSequenceView mNvsMultiThumbnailSequenceView;
    private NvsWaveformView mNvsWaveformView = null;
    private View mRightBlackView;
    private View mSpeedRelative;
    private TextView mSpeedTextView;
    private String mText;
    private Paint mTextPaint;
    private TextView mTextView;
    private int mViewHeight = 0;
    private int textSize = 12;

    public BaseItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BaseItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public BaseItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mContext = context;
        this.textSize = DisplayUtil.dip2px(context, 12.0f);
        this.mViewHeight = context.getResources().getDimensionPixelOffset(R.dimen.track_view_real_height);
        this.mTextPaint = new Paint();
        this.mTextPaint.setTextSize((float) this.textSize);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setAntiAlias(false);
        this.mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(BaseUIClip baseUIClip) {
        this.mBaseUIClip = baseUIClip;
        if (this.mBaseUIClip.getIconFilePath() != null) {
            Logger.d(TAG, "setData: 图片路径 " + this.mBaseUIClip.getIconFilePath());
            addImageView(this.mBaseUIClip.getIconFilePath());
        }
        this.mText = baseUIClip.getText();
        String type = baseUIClip.getType();
        setBackground(BorderRadiusDrawableUtil.getRadiusDrawable(-1, -1, getResources().getDimensionPixelOffset(R.dimen.track_view_background_radius), getBackgroundColorByTrackType(type)));
        if (type.equals(CommonData.CLIP_VIDEO) || type.equals(CommonData.CLIP_IMAGE)) {
            addTimeLineView();
        } else if (type.equals(CommonData.CLIP_AUDIO) && baseUIClip.getAudioType() == 2) {
            addRecordingView();
        } else if (type.equals(CommonData.CLIP_AUDIO) && baseUIClip.getAudioType() != 2) {
            addAudioView();
            addAudioFadeView();
        }
        addTextView();
        if (type.equals(CommonData.CLIP_VIDEO) || type.equals(CommonData.CLIP_IMAGE)) {
            addSpeedText();
        }
        invalidate();
    }

    private void addSpeedText() {
        if (this.mSpeedRelative == null) {
            this.mSpeedRelative = LayoutInflater.from(this.mContext).inflate(R.layout.base_item_view_speed_layout, this);
            this.mSpeedTextView = (TextView) this.mSpeedRelative.findViewById(R.id.base_item_view_speed_text);
            this.mDuringTextView = (TextView) this.mSpeedRelative.findViewById(R.id.base_item_view_time_text);
            this.mImageSpeed = (ImageView) this.mSpeedRelative.findViewById(R.id.image_speed);
            if (this.mBaseUIClip.getSpeed() != 1.0d) {
                this.mSpeedTextView.setVisibility(0);
                this.mImageSpeed.setVisibility(0);
            } else {
                this.mSpeedTextView.setVisibility(8);
                this.mImageSpeed.setVisibility(8);
            }
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");
            TextView textView = this.mSpeedTextView;
            textView.setText(decimalFormat.format(this.mBaseUIClip.getSpeed()) + "x");
            this.mDuringTextView.setText(FormatUtils.durationToText(this.mBaseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn()));
        }
    }

    private void addAudioFadeView() {
        int audioFadeIn = (int) (this.mBaseUIClip.getAudioFadeIn() / 1000000);
        int audioFadeOut = (int) (this.mBaseUIClip.getAudioFadeOut() / 1000000);
        if (audioFadeIn != 0) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.bg_audio_fade_top);
            view.setLayoutParams(new RelativeLayout.LayoutParams(-1, (int) getResources().getDimension(R.dimen.dp6)));
            addView(view);
        }
        if (audioFadeOut != 0) {
            View view2 = new View(getContext());
            view2.setBackgroundResource(R.drawable.bg_audio_fade_bottom);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, (int) getResources().getDimension(R.dimen.dp6));
            layoutParams.addRule(12);
            view2.setLayoutParams(layoutParams);
            addView(view2);
        }
    }

    private void addTextView() {
        if (this.mTextView == null) {
            this.mTextView = new TextView(getContext());
            this.mTextView.setTextColor(getResources().getColor(R.color.white_8));
            this.mTextView.setTextSize(0, getResources().getDimension(R.dimen.sp8));
            this.mTextView.setText(this.mText);
            if (this.mBaseUIClip.getType() == CommonData.CLIP_AUDIO) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                this.mTextView.setBackgroundResource(R.drawable.bg_audio_draw_text);
                int dip2px = DisplayUtil.dip2px(this.mContext, 3.0f);
                this.mTextView.setPadding(dip2px, dip2px, dip2px, dip2px);
                this.mTextView.setSingleLine(true);
                layoutParams.addRule(12);
                this.mTextView.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.leftMargin = (int) getResources().getDimension(R.dimen.dp5);
                layoutParams2.topMargin = (int) getResources().getDimension(R.dimen.dp5);
                this.mTextView.setLayoutParams(layoutParams2);
            }
            addView(this.mTextView);
        }
    }

    private void setText(String str) {
        TextView textView = this.mTextView;
        if (textView != null) {
            this.mText = str;
            textView.setText(this.mText);
        }
    }

    private void addImageView(String str) {
        if (this.mIconImageView == null) {
            this.mIconImageView = new ImageView(getContext());
            int i = this.mViewHeight;
            this.mIconImageView.setLayoutParams(new RelativeLayout.LayoutParams(i, i));
            addView(this.mIconImageView);
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(getContext().getApplicationContext()).asBitmap().load(str).apply(requestOptions).into(this.mIconImageView);
    }

    private void addLeftBlackView() {
        if (this.mLeftBlackView == null) {
            this.mLeftBlackView = new View(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getContext().getResources().getDimensionPixelOffset(R.dimen.dp0p5), this.mViewHeight);
            layoutParams.addRule(18);
            this.mLeftBlackView.setLayoutParams(layoutParams);
            this.mLeftBlackView.setBackgroundColor(getContext().getResources().getColor(R.color.black));
            addView(this.mLeftBlackView);
        }
    }

    private void addRightBlackView() {
        if (this.mRightBlackView == null) {
            this.mRightBlackView = new View(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getContext().getResources().getDimensionPixelOffset(R.dimen.dp0p5), this.mViewHeight);
            layoutParams.addRule(19);
            this.mRightBlackView.setLayoutParams(layoutParams);
            this.mRightBlackView.setBackgroundColor(getContext().getResources().getColor(R.color.black));
            addView(this.mRightBlackView);
        }
    }

    private void addRecordingView() {
        this.mAudioWaveView = (AudioWaveView) LayoutInflater.from(this.mContext).inflate(R.layout.item_add_recording_view, this).findViewById(R.id.audio_view);
        this.mAudioWaveView.setWaveColor(getResources().getColor(R.color.audio_record));
        this.mAudioWaveView.setMaxGroupData(0.9f);
    }

    public void refresh(BaseUIClip baseUIClip, boolean z) {
        this.mBaseUIClip = baseUIClip;
        this.mText = baseUIClip.getText();
        String type = baseUIClip.getType();
        if (type.equals(CommonData.CLIP_VIDEO) || type.equals(CommonData.CLIP_IMAGE)) {
            refreshVideoView();
            setPipDuringVisiableStatus(true);
        } else if (baseUIClip.getAudioType() == 2) {
            AudioWaveView audioWaveView = this.mAudioWaveView;
            if (audioWaveView != null) {
                audioWaveView.setWidth((long) baseUIClip.getRecordLength());
                this.mAudioWaveView.addWaveData(baseUIClip.getRecordArray());
            }
        } else if (type.equals(CommonData.CLIP_AUDIO)) {
            refreshAudioView(z);
        }
        invalidate();
    }

    public void setPipDuringVisiableStatus(boolean z) {
        TextView textView;
        if ((this.mBaseUIClip.getType().equals(CommonData.CLIP_VIDEO) || this.mBaseUIClip.getType().equals(CommonData.CLIP_IMAGE)) && (textView = this.mDuringTextView) != null) {
            if (z) {
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
            this.mDuringTextView.setText(FormatUtils.durationToText(this.mBaseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn()));
            invalidate();
        }
    }

    private void refreshVideoView() {
        if (this.mNvsMultiThumbnailSequenceView != null) {
            ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList = new ArrayList<>();
            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            thumbnailSequenceDesc.mediaFilePath = this.mBaseUIClip.getFilePath();
            thumbnailSequenceDesc.trimIn = this.mBaseUIClip.getTrimIn();
            thumbnailSequenceDesc.trimOut = this.mBaseUIClip.getTrimOut();
            thumbnailSequenceDesc.inPoint = 0;
            thumbnailSequenceDesc.stillImageHint = false;
            thumbnailSequenceDesc.onlyDecodeKeyFrame = true;
            double trimOut = (double) (this.mBaseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn());
            double speed = this.mBaseUIClip.getSpeed();
            Double.isNaN(trimOut);
            thumbnailSequenceDesc.outPoint = (long) (trimOut / speed);
            arrayList.add(thumbnailSequenceDesc);
            this.mNvsMultiThumbnailSequenceView.setThumbnailSequenceDescArray(arrayList);
        }
    }

    private void refreshAudioView(boolean z) {
        NvsWaveformView nvsWaveformView = this.mNvsWaveformView;
        if (nvsWaveformView == null) {
            return;
        }
        if (z) {
            nvsWaveformView.setTrimIn(this.mBaseUIClip.getTrimIn());
        } else {
            nvsWaveformView.setTrimOut(this.mBaseUIClip.getTrimOut());
        }
    }

    private void addAudioView() {
        this.mNvsWaveformView = (NvsWaveformView) LayoutInflater.from(this.mContext).inflate(R.layout.item_add_audio_view, this).findViewById(R.id.view_sdk_wave_form);
        this.mNvsWaveformView.setSingleChannelMode(true);
        if (this.mBaseUIClip.getAudioType() == 1) {
            this.mNvsWaveformView.setWaveformColor(getResources().getColor(R.color.audio_record));
        } else {
            this.mNvsWaveformView.setWaveformColor(getResources().getColor(R.color.audio_music));
        }
        this.mNvsWaveformView.setBackgroundColor(getResources().getColor(R.color.track_background_color_audio));
        this.mNvsWaveformView.setAudioFilePath(this.mBaseUIClip.getFilePath());
        this.mNvsWaveformView.setTrimIn(this.mBaseUIClip.getTrimIn());
        this.mNvsWaveformView.setTrimOut(this.mBaseUIClip.getTrimOut());
    }

    private void addTimeLineView() {
        if (this.mNvsMultiThumbnailSequenceView != null) {
            refreshVideoView();
            return;
        }
        this.mNvsMultiThumbnailSequenceView = (NvsMultiThumbnailSequenceView) LayoutInflater.from(this.mContext).inflate(R.layout.timeline_editor_pip_view, this).findViewById(R.id.pip_multi_thumbnail_sequence_view);
        this.mNvsMultiThumbnailSequenceView.setStartPadding(0);
        refreshVideoView();
        this.mNvsMultiThumbnailSequenceView.setPixelPerMicrosecond(PixelPerMicrosecondUtil.getPixelPerMicrosecond(this.mContext));
        this.mNvsMultiThumbnailSequenceView.setClickable(true);
        this.mNvsMultiThumbnailSequenceView.setScrollEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public BaseUIClip getBaseUIClip() {
        return this.mBaseUIClip;
    }

    public HandView getHandView() {
        return this.handView;
    }

    public void setHandView(HandView handView2) {
        this.handView = handView2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        BaseUIClip baseUIClip = this.mBaseUIClip;
        if (baseUIClip != null) {
            double trimOut = (double) (baseUIClip.getTrimOut() - this.mBaseUIClip.getTrimIn());
            double speed = this.mBaseUIClip.getSpeed();
            Double.isNaN(trimOut);
            i3 = PixelPerMicrosecondUtil.durationToLength((long) (trimOut / speed));
        } else {
            i3 = 100;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(i3, MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(this.mViewHeight, 1073741824));
    }

    public int getBackgroundColorByTrackType(String str) {
        if (CommonData.CLIP_TIMELINE_FX.equals(str)) {
            return getResources().getColor(R.color.track_background_color_filter);
        }
        if (CommonData.CLIP_CAPTION.equals(str)) {
            return getResources().getColor(R.color.track_background_color_caption);
        }
        if (CommonData.CLIP_COMPOUND_CAPTION.equals(str)) {
            return getResources().getColor(R.color.track_background_color_compound_caption);
        }
        if (CommonData.CLIP_STICKER.equals(str)) {
            return getResources().getColor(R.color.track_background_color_sticker);
        }
        if (CommonData.CLIP_AUDIO.equals(str)) {
            return getResources().getColor(R.color.track_background_color_audio);
        }
        return getResources().getColor(R.color.black);
    }
}
