package com.meishe.myvideo.manager;

import android.os.Handler;
import android.text.TextUtils;
import com.meicam.sdk.NvsMediaFileConvertor;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoClip;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.myvideo.util.PathUtils;
import java.io.File;

public class ConvertFileManager implements NvsMediaFileConvertor.MeidaFileConvertorCallback {
    private static ConvertFileManager mInstance;
    private long mCurrentTaskId;
    private String mDestFilePath;
    private NvsMediaFileConvertor mMediaFileConvertor;
    private MeicamVideoClip mMeicamVideoClip;
    private OnConvertListener mOnConvertListener;

    public interface OnConvertListener {
        void onConvertCancel();

        void onConvertFinish(boolean z);

        void onConvertProgress(float f);
    }

    @Override // com.meicam.sdk.NvsMediaFileConvertor.MeidaFileConvertorCallback
    public void notifyAudioMuteRage(long j, long j2, long j3) {
    }

    private ConvertFileManager() {
    }

    public static ConvertFileManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConvertFileManager();
        }
        return mInstance;
    }

    @Override // com.meicam.sdk.NvsMediaFileConvertor.MeidaFileConvertorCallback
    public void onProgress(long j, float f) {
        OnConvertListener onConvertListener;
        if (this.mCurrentTaskId == j && (onConvertListener = this.mOnConvertListener) != null) {
            onConvertListener.onConvertProgress(f * 100.0f);
        }
    }

    @Override // com.meicam.sdk.NvsMediaFileConvertor.MeidaFileConvertorCallback
    public void onFinish(long j, String str, String str2, int i) {
        if (this.mCurrentTaskId == j) {
            videoClipConvertComplete(str2, i == 0);
        }
    }

    private void videoClipConvertComplete(String str, boolean z) {
        if (z) {
            this.mMeicamVideoClip.setReverseFilePath(str);
        } else {
            this.mMeicamVideoClip.setReverseFilePath("");
        }
        this.mMeicamVideoClip.setConvertSuccess(z);
        finishConvert();
        OnConvertListener onConvertListener = this.mOnConvertListener;
        if (onConvertListener != null) {
            onConvertListener.onConvertFinish(z);
        }
    }

    public void convertFile(MeicamVideoClip meicamVideoClip) {
        this.mMeicamVideoClip = meicamVideoClip;
        if (this.mMediaFileConvertor == null) {
            this.mMediaFileConvertor = new NvsMediaFileConvertor();
        }
        String filePath = ((NvsVideoClip) meicamVideoClip.getObject()).getFilePath();
        String fileName = PathUtils.getFileName(filePath);
        this.mDestFilePath = PathUtils.getVideoConvertDir() + File.separator + "MY_" + System.currentTimeMillis() + "_" + fileName;
        this.mMediaFileConvertor.setMeidaFileConvertorCallback(this, (Handler) null);
        this.mCurrentTaskId = this.mMediaFileConvertor.convertMeidaFile(filePath, this.mDestFilePath, true, 0, NvsStreamingContext.getInstance().getAVFileInfo(meicamVideoClip.getFilePath()).getDuration(), null);
    }

    public void cancelConvert() {
        NvsMediaFileConvertor nvsMediaFileConvertor = this.mMediaFileConvertor;
        if (nvsMediaFileConvertor != null) {
            nvsMediaFileConvertor.cancelTask(this.mCurrentTaskId);
            this.mMediaFileConvertor = null;
            this.mCurrentTaskId = -1;
            if (!TextUtils.isEmpty(this.mDestFilePath)) {
                File file = new File(this.mDestFilePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            OnConvertListener onConvertListener = this.mOnConvertListener;
            if (onConvertListener != null) {
                onConvertListener.onConvertCancel();
            }
        }
    }

    private void finishConvert() {
        NvsMediaFileConvertor nvsMediaFileConvertor = this.mMediaFileConvertor;
        if (nvsMediaFileConvertor != null) {
            nvsMediaFileConvertor.release();
        }
        this.mMediaFileConvertor = null;
    }

    public void setOnConvertListener(OnConvertListener onConvertListener) {
        this.mOnConvertListener = onConvertListener;
    }
}
