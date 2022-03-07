package com.cdv.io;

import android.content.Context;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import com.cdv.utils.NvAndroidInterruptionChecker;
import com.cdv.utils.NvAndroidUtils;
import com.meishe.myvideo.util.statusbar.OSUtils;
import java.nio.ByteBuffer;

public class NvAndroidVideoFileReaderSW {
    private static final int ERROR_EOF = 1;
    private static final int ERROR_FAIL = 2;
    private static final int ERROR_INTERRUPTED_DECODING = 3;
    private static final int ERROR_MEDIA_EXTRACTOR_PRELOAD_FAILED = 4;
    private static final int ERROR_OK = 0;
    private static final int SKIP_MODE_ALL_NONREFERENCE = 1;
    private static final int SKIP_MODE_BELOW_TIMESTAMP = 2;
    private static final int SKIP_MODE_NONE = 0;
    private static final String TAG = "NvAndroidVideoFileReaderSW";
    private static final boolean m_verbose = false;
    private long m_actualDuration = 0;
    private MediaCodec.BufferInfo m_bufferInfo = null;
    private Handler m_cleanupHandler = null;
    private long m_contiuousDecodingThreshold = 1000000;
    private MediaCodec m_decoder = null;
    ByteBuffer[] m_decoderInputBuffers = null;
    ByteBuffer[] m_decoderOutputBuffers = null;
    private boolean m_decoderSetupFailed = false;
    private boolean m_decoderStarted = false;
    private boolean m_decoderUseSurface = false;
    private long m_duration = 0;
    private MediaExtractor m_extractor = null;
    private boolean m_extractorInOriginalState = true;
    private MediaFormat m_format = null;
    private Object m_frameSyncObject = new Object();
    private Handler m_handler = null;
    private ImageReader m_imageReader = null;
    private boolean m_imageReady = false;
    private boolean m_inputBufferQueued = false;
    private NvAndroidInterruptionChecker m_interruptionChecker;
    private long m_lastSeekActualTimestamp = Long.MIN_VALUE;
    private long m_lastSeekTimestamp = Long.MIN_VALUE;
    private boolean m_onlyDecodeKeyFrame = false;
    private long m_owner = 0;
    private int m_pendingInputFrameCount = 0;
    private long m_preloadedTimestamp = Long.MIN_VALUE;
    private boolean m_sawInputEOS = false;
    private boolean m_sawOutputEOS = false;
    private boolean m_skipNonReferenceFrameWhenPlayback = false;
    private long m_timestampOfLastCopiedFrame = Long.MIN_VALUE;
    private long m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
    private int m_videoTrackIndex = -1;

    private native void nativeCopyVideoFrame(long j, ByteBuffer byteBuffer, int i, int i2, long j2);

    private native void nativeCopyVideoFrameFromYUV420ImagePlanes(long j, int i, int i2, int i3, int i4, ByteBuffer byteBuffer, int i5, int i6, ByteBuffer byteBuffer2, int i7, int i8, ByteBuffer byteBuffer3, int i9, int i10, long j2);

    private native void nativeSetFormatInfo(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    NvAndroidVideoFileReaderSW(long j, Handler handler, Handler handler2) {
        this.m_owner = j;
        this.m_handler = handler;
        this.m_cleanupHandler = handler2;
        this.m_bufferInfo = new MediaCodec.BufferInfo();
    }

    public boolean OpenFile(String str, Context context, int i, long j) {
        if (IsValid()) {
            Log.e(TAG, "You can't call OpenFile() twice!");
            return false;
        }
        this.m_extractor = NvAndroidUtils.createMediaExtractorFromMediaFilePath(context, str);
        MediaExtractor mediaExtractor = this.m_extractor;
        if (mediaExtractor == null) {
            return false;
        }
        this.m_extractorInOriginalState = true;
        int trackCount = mediaExtractor.getTrackCount();
        int i2 = 0;
        while (true) {
            if (i2 >= trackCount) {
                break;
            } else if (this.m_extractor.getTrackFormat(i2).getString("mime").startsWith("video/")) {
                this.m_videoTrackIndex = i2;
                break;
            } else {
                i2++;
            }
        }
        int i3 = this.m_videoTrackIndex;
        if (i3 < 0) {
            Log.e(TAG, "Failed to find a video track from " + str);
            CloseFile();
            return false;
        }
        this.m_extractor.selectTrack(i3);
        this.m_format = this.m_extractor.getTrackFormat(this.m_videoTrackIndex);
        if (Build.VERSION.SDK_INT == 16) {
            this.m_format.setInteger("max-input-size", 0);
        }
        boolean equals = Build.HARDWARE.equals("qcom");
        if (Build.VERSION.SDK_INT >= 23 && i >= 0 && !equals) {
            MediaFormat mediaFormat = this.m_format;
            if (i <= 0) {
                i = 120;
            }
            mediaFormat.setInteger("operating-rate", i);
        }
        try {
            this.m_duration = this.m_format.getLong("durationUs");
            this.m_actualDuration = this.m_duration;
            String string = this.m_format.getString("mime");
            if (equals && this.m_format.containsKey("frame-rate")) {
                this.m_format.setInteger("frame-rate", 0);
            }
            this.m_decoderSetupFailed = false;
            if (!SetupDecoder(string)) {
                this.m_decoderSetupFailed = true;
                CloseFile();
                return false;
            }
            this.m_contiuousDecodingThreshold = j;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CloseFile();
            return false;
        }
    }

    public void preload(long j) {
        int preloadInternal = preloadInternal(j);
        int i = 0;
        while (preloadInternal == 4) {
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
            preloadInternal = preloadInternal(j);
            i++;
            Log.w(TAG, "Try to preload! times=" + i);
            if (i >= 2) {
                break;
            }
        }
        if (preloadInternal == 4) {
            Log.w(TAG, "Try to recreate MediaExtractor after preload!");
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        }
    }

    public boolean hasDecoderSetupFailed() {
        return this.m_decoderSetupFailed;
    }

    public void enableOnlyDecodeKeyFrame(boolean z) {
        this.m_onlyDecodeKeyFrame = z;
    }

    public void setActualDuration(long j) {
        if (j > this.m_duration) {
            this.m_actualDuration = j;
        }
    }

    public void setInterruptionChecker(NvAndroidInterruptionChecker nvAndroidInterruptionChecker) {
        this.m_interruptionChecker = nvAndroidInterruptionChecker;
    }

    public void skipNonReferenceFrame(boolean z) {
        this.m_skipNonReferenceFrameWhenPlayback = z;
    }

    public void CloseFile() {
        InvalidLastSeekTimestamp();
        CleanupDecoder(true);
        MediaExtractor mediaExtractor = this.m_extractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.m_extractor = null;
            this.m_videoTrackIndex = -1;
            this.m_format = null;
            this.m_duration = 0;
            this.m_actualDuration = 0;
            this.m_extractorInOriginalState = true;
            this.m_onlyDecodeKeyFrame = false;
        }
    }

    public int SeekVideoFrame(long j, long j2) {
        if (!IsValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        long j3 = this.m_duration;
        if (max >= j3) {
            if (max >= this.m_actualDuration + 25000) {
                return 1;
            }
            max = j3 - 1;
        }
        long j4 = this.m_timestampOfLastCopiedFrame;
        if (j4 != Long.MIN_VALUE && Math.abs(max - j4) <= j2) {
            return 0;
        }
        int SeekInternal = SeekInternal(max, j2, this.m_onlyDecodeKeyFrame ? 0 : 2);
        if (SeekInternal == 0) {
            this.m_lastSeekTimestamp = max;
            this.m_lastSeekActualTimestamp = this.m_timestampOfLastCopiedFrame;
        } else {
            InvalidLastSeekTimestamp();
        }
        return SeekInternal;
    }

    public int StartPlayback(long j, long j2) {
        long j3;
        long j4;
        if (!IsValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        if (max >= this.m_actualDuration) {
            return 1;
        }
        long j5 = this.m_duration;
        if (max >= j5) {
            max = j5 - 1;
        }
        long j6 = this.m_preloadedTimestamp;
        if (j6 != Long.MIN_VALUE) {
            if (max >= j6) {
                long j7 = this.m_timestampOfLastCopiedFrame;
                if (max <= j7) {
                    max = j7;
                }
            }
            this.m_preloadedTimestamp = Long.MIN_VALUE;
        } else {
            long j8 = this.m_lastSeekTimestamp;
            if (j8 != Long.MIN_VALUE && max == j8) {
                long j9 = this.m_lastSeekActualTimestamp;
                if (j9 != Long.MIN_VALUE) {
                    j3 = j9;
                    j4 = this.m_timestampOfLastCopiedFrame;
                    if (j3 != j4 && j4 == this.m_timestampOfLastDecodedFrame) {
                        return 0;
                    }
                    int SeekInternal = SeekInternal(j3, j2, 2);
                    InvalidLastSeekTimestamp();
                    return SeekInternal;
                }
            }
        }
        j3 = max;
        j4 = this.m_timestampOfLastCopiedFrame;
        if (j3 != j4) {
        }
        int SeekInternal2 = SeekInternal(j3, j2, 2);
        InvalidLastSeekTimestamp();
        return SeekInternal2;
    }

    public int GetNextVideoFrameForPlayback() {
        if (!IsValid()) {
            return 1;
        }
        int DecodeToFrame = DecodeToFrame(Long.MIN_VALUE, 0, this.m_skipNonReferenceFrameWhenPlayback ? 1 : 0);
        InvalidLastSeekTimestamp();
        return DecodeToFrame;
    }

    private boolean IsValid() {
        return this.m_decoder != null;
    }

    private boolean preferDecodeToImageReader(String str) {
        if (!Build.MANUFACTURER.equals(OSUtils.ROM_OPPO) || !Build.MODEL.equals("R15")) {
            return false;
        }
        return str.equals("video/mpeg2");
    }

    private int preloadInternal(long j) {
        if (!IsValid()) {
            return 2;
        }
        long max = Math.max(Math.min(j, this.m_duration - 1), 0L);
        int SeekInternal = SeekInternal(max, 0, 2);
        if (SeekInternal == 0) {
            if (this.m_timestampOfLastCopiedFrame == Long.MIN_VALUE) {
                return SeekInternal;
            }
            this.m_preloadedTimestamp = max;
            return SeekInternal;
        } else if (SeekInternal != 1 || this.m_timestampOfLastCopiedFrame != Long.MIN_VALUE) {
            return SeekInternal;
        } else {
            long j2 = this.m_timestampOfLastDecodedFrame;
            if (j2 == Long.MIN_VALUE || j2 >= this.m_duration - 100000) {
                return SeekInternal;
            }
            return 4;
        }
    }

    private boolean SetupDecoder(String str) {
        if (!preferDecodeToImageReader(str) || !setupDecoderWithImageReader(str)) {
            return setupDecoderWithBuffers(str);
        }
        return true;
    }

    private boolean setupDecoderWithBuffers(String str) {
        try {
            this.m_decoder = MediaCodec.createDecoderByType(str);
            this.m_decoder.configure(this.m_format, (Surface) null, (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderInputBuffers = this.m_decoder.getInputBuffers();
            this.m_decoderOutputBuffers = this.m_decoder.getOutputBuffers();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return false;
        }
    }

    private boolean setupDecoderWithImageReader(String str) {
        boolean z;
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        try {
            this.m_decoder = MediaCodec.createDecoderByType(str);
            int[] iArr = this.m_decoder.getCodecInfo().getCapabilitiesForType(str).colorFormats;
            int length = iArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else if (iArr[i] == 2135033992) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                Log.w(TAG, "We can't decode to ImageReader if COLOR_FormatYUV420Flexible is not supported!");
                this.m_decoder.release();
                this.m_decoder = null;
                return false;
            }
            this.m_format.setInteger("color-format", 2135033992);
            this.m_imageReader = ImageReader.newInstance(this.m_format.getInteger("width"), this.m_format.getInteger("height"), 35, 1);
            this.m_imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                /* class com.cdv.io.NvAndroidVideoFileReaderSW.AnonymousClass1 */

                public void onImageAvailable(ImageReader imageReader) {
                    synchronized (NvAndroidVideoFileReaderSW.this.m_frameSyncObject) {
                        NvAndroidVideoFileReaderSW.this.m_imageReady = true;
                        NvAndroidVideoFileReaderSW.this.m_frameSyncObject.notifyAll();
                    }
                }
            }, this.m_handler);
            this.m_decoder.configure(this.m_format, this.m_imageReader.getSurface(), (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderUseSurface = true;
            this.m_decoderInputBuffers = this.m_decoder.getInputBuffers();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return false;
        }
    }

    private void CleanupDecoder(boolean z) {
        if (this.m_decoder != null && this.m_decoderStarted) {
            try {
                if (this.m_sawInputEOS && !this.m_sawOutputEOS) {
                    DrainOutputBuffers();
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
        if (!z) {
            CleanupDecoderCore(false);
        } else {
            this.m_cleanupHandler.post(new Runnable() {
                /* class com.cdv.io.NvAndroidVideoFileReaderSW.AnonymousClass2 */

                public void run() {
                    NvAndroidVideoFileReaderSW.this.CleanupDecoderCore(true);
                }
            });
        }
        this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        this.m_timestampOfLastCopiedFrame = Long.MIN_VALUE;
        this.m_pendingInputFrameCount = 0;
        this.m_sawInputEOS = false;
        this.m_sawOutputEOS = false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void CleanupDecoderCore(boolean z) {
        MediaCodec mediaCodec = this.m_decoder;
        if (mediaCodec != null) {
            if (this.m_decoderStarted) {
                try {
                    if (this.m_inputBufferQueued) {
                        try {
                            mediaCodec.flush();
                        } catch (Exception unused) {
                        }
                        this.m_inputBufferQueued = false;
                    }
                    this.m_decoder.stop();
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                    e.printStackTrace();
                }
                this.m_decoderStarted = false;
                this.m_decoderInputBuffers = null;
            }
            this.m_decoder.release();
            this.m_decoder = null;
        }
        ImageReader imageReader = this.m_imageReader;
        if (imageReader != null) {
            imageReader.close();
            this.m_imageReader = null;
        }
        this.m_decoderUseSurface = false;
    }

    private int SeekInternal(long j, long j2, int i) {
        long j3 = this.m_timestampOfLastDecodedFrame;
        boolean z = true;
        if ((j3 == Long.MIN_VALUE || j <= j3 || j >= j3 + this.m_contiuousDecodingThreshold) && (!this.m_extractorInOriginalState || j >= this.m_contiuousDecodingThreshold)) {
            z = false;
        }
        if (this.m_onlyDecodeKeyFrame) {
            z = false;
        }
        if (!z) {
            try {
                this.m_extractor.seekTo(j, 0);
                if (!this.m_sawInputEOS) {
                    if (!this.m_sawOutputEOS) {
                        if (this.m_inputBufferQueued) {
                            try {
                                this.m_decoder.flush();
                            } catch (Exception unused) {
                            }
                            this.m_inputBufferQueued = false;
                            this.m_pendingInputFrameCount = 0;
                        }
                    }
                }
                CleanupDecoder(false);
                if (!SetupDecoder(this.m_format.getString("mime"))) {
                    return 2;
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
                return 2;
            }
        }
        return DecodeToFrame(j, j2, i);
    }

    private int DecodeToFrame(long j, long j2, int i) {
        try {
            return DoDecodeToFrame(j, j2, i);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return 2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:0x026c A[LOOP:0: B:1:0x0010->B:110:0x026c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0264 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int DoDecodeToFrame(long r30, long r32, int r34) {
        /*
        // Method dump skipped, instructions count: 655
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReaderSW.DoDecodeToFrame(long, long, int):int");
        return r34;
    }

    private Image AwaitNewImage() {
        synchronized (this.m_frameSyncObject) {
            do {
                if (!this.m_imageReady) {
                    try {
                        this.m_frameSyncObject.wait(3000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "" + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    this.m_imageReady = false;
                    try {
                        return this.m_imageReader.acquireLatestImage();
                    } catch (Exception e2) {
                        Log.e(TAG, "" + e2.getMessage());
                        e2.printStackTrace();
                        return null;
                    }
                }
            } while (this.m_imageReady);
            Log.e(TAG, "ImageReader wait timed out!");
            return null;
        }
    }

    private void DrainOutputBuffers() {
        if (this.m_sawInputEOS && !this.m_sawOutputEOS) {
            int i = 0;
            while (!this.m_sawOutputEOS) {
                int dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, 5000);
                i++;
                if (!(dequeueOutputBuffer == -1 || dequeueOutputBuffer == -3 || dequeueOutputBuffer == -2)) {
                    if (dequeueOutputBuffer < 0) {
                        Log.e(TAG, "DrainDecoderBuffers(): Unexpected result from decoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                        return;
                    }
                    if ((this.m_bufferInfo.flags & 4) != 0) {
                        this.m_sawOutputEOS = true;
                    }
                    this.m_decoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    i = 0;
                }
                if (i > 100) {
                    Log.e(TAG, "DrainDecoderBuffers(): We have tried too many times and can't decode a frame!");
                    return;
                }
            }
        }
    }

    private void InvalidLastSeekTimestamp() {
        this.m_lastSeekTimestamp = Long.MIN_VALUE;
        this.m_lastSeekActualTimestamp = Long.MIN_VALUE;
    }

    private boolean isNonReferenceFrame(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.limit() < 5) {
            return false;
        }
        byte[] bArr = new byte[5];
        int position = byteBuffer.position();
        byteBuffer.get(bArr);
        byteBuffer.position(position);
        int i = bArr[4] & 31;
        if (bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1 && i == 1 && ((bArr[4] >> 5) & 3) == 0) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00c3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void ParseMediaFormat(android.media.MediaFormat r14) {
        /*
        // Method dump skipped, instructions count: 220
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReaderSW.ParseMediaFormat(android.media.MediaFormat):void");
    }

    private boolean isInterruptedDecoding() {
        NvAndroidInterruptionChecker nvAndroidInterruptionChecker = this.m_interruptionChecker;
        if (nvAndroidInterruptionChecker == null) {
            return false;
        }
        return nvAndroidInterruptionChecker.isInterrupted();
    }
}
