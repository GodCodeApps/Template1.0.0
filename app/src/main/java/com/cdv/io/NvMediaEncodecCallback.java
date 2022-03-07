package com.cdv.io;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import java.nio.ByteBuffer;

public class NvMediaEncodecCallback {
    private static final String TAG = "NvMediaEncodecCallback";
    private HandlerThread mCallbackThread = null;
    private long m_contextInterface = -1;

    /* access modifiers changed from: private */
    public static native void nativeOnError(long j, int i);

    /* access modifiers changed from: private */
    public static native void nativeOnOutputBufferAvailable(long j, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);

    /* access modifiers changed from: private */
    public static native void nativeOnOutputFormatChanged(long j, MediaFormat mediaFormat);

    NvMediaEncodecCallback(long j) {
        this.m_contextInterface = j;
    }

    public boolean setCallbackToCodec(MediaCodec mediaCodec) {
        if (mediaCodec == null || Build.VERSION.SDK_INT < 21) {
            return false;
        }
        if (this.mCallbackThread == null) {
            this.mCallbackThread = new HandlerThread("callback handler");
            HandlerThread handlerThread = this.mCallbackThread;
            if (handlerThread == null) {
                Log.e(TAG, "Failed to create background handler thread!");
                return false;
            }
            handlerThread.start();
        }
        Looper looper = this.mCallbackThread.getLooper();
        if (looper == null) {
            closeCallbackThread();
            Log.e(TAG, "Failed to getLooper of the background thread!");
            return false;
        }
        mediaCodec.setCallback(new MediaCodec.Callback() {
            /* class com.cdv.io.NvMediaEncodecCallback.AnonymousClass1 */

            public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
                Log.d(NvMediaEncodecCallback.TAG, "onInputBufferAvailable");
            }

            public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, MediaCodec.BufferInfo bufferInfo) {
                if (NvMediaEncodecCallback.this.m_contextInterface > 0 && bufferInfo != null) {
                    try {
                        NvMediaEncodecCallback.nativeOnOutputBufferAvailable(NvMediaEncodecCallback.this.m_contextInterface, mediaCodec.getOutputBuffer(i), bufferInfo);
                        mediaCodec.releaseOutputBuffer(i, false);
                    } catch (Exception e) {
                        Log.e(NvMediaEncodecCallback.TAG, "MediaCodec.releaseOutputBuffer failed!");
                        e.printStackTrace();
                    }
                }
            }

            public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
                if (NvMediaEncodecCallback.this.m_contextInterface > 0) {
                    int i = -1;
                    if (codecException != null) {
                        i = codecException.getErrorCode();
                    }
                    Log.d(NvMediaEncodecCallback.TAG, "onErrorCode:" + i);
                    NvMediaEncodecCallback.nativeOnError(NvMediaEncodecCallback.this.m_contextInterface, i);
                }
            }

            public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
                if (NvMediaEncodecCallback.this.m_contextInterface > 0) {
                    Log.d(NvMediaEncodecCallback.TAG, "onOutputFormatChanged");
                    NvMediaEncodecCallback.nativeOnOutputFormatChanged(NvMediaEncodecCallback.this.m_contextInterface, mediaFormat);
                }
            }
        }, new Handler(looper));
        return true;
    }

    public void cleanUp() {
        closeCallbackThread();
    }

    private void closeCallbackThread() {
        if (this.mCallbackThread != null && Build.VERSION.SDK_INT >= 21) {
            if (this.mCallbackThread.isAlive()) {
                this.mCallbackThread.quitSafely();
            }
            try {
                this.mCallbackThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mCallbackThread = null;
        }
    }
}
