package com.cdv.io;

import android.content.Context;
import android.graphics.SurfaceTexture;
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
import com.meishe.engine.bean.CommonData;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

public class NvAndroidVideoFileReader implements SurfaceTexture.OnFrameAvailableListener {
    private static final int ERROR_EOF = 1;
    private static final int ERROR_FAIL = 2;
    private static final int ERROR_INTERRUPTED_DECODING = 3;
    private static final int ERROR_MEDIA_EXTRACTOR_PRELOAD_FAILED = 4;
    private static final int ERROR_OK = 0;
    private static final int SKIP_MODE_ALL_NONREFERENCE = 1;
    private static final int SKIP_MODE_BELOW_TIMESTAMP = 2;
    private static final int SKIP_MODE_NONE = 0;
    private static final String TAG = "NvAndroidVideoFileReader";
    private static Method m_setOnFrameAvailableListener2 = null;
    private static final boolean m_verbose = false;
    private long m_actualDuration = 0;
    private MediaCodec.BufferInfo m_bufferInfo = null;
    private Handler m_cleanupHandler = null;
    private Context m_context;
    private long m_contiuousDecodingThreshold = 1500000;
    private boolean m_curTexImageUpdated = false;
    private MediaCodec m_decoder = null;
    ByteBuffer[] m_decoderInputBuffers = null;
    private boolean m_decoderSetupFailed = false;
    private boolean m_decoderStarted = false;
    private long m_duration = 0;
    private MediaExtractor m_extractor = null;
    private boolean m_extractorInOriginalState = true;
    private boolean m_firstPlaybackTexFrameUnconsumed = false;
    private MediaFormat m_format = null;
    private boolean m_frameAvailable = false;
    private Object m_frameSyncObject = new Object();
    private Handler m_handler = null;
    private boolean m_inputBufferQueued = false;
    private NvAndroidInterruptionChecker m_interruptionChecker;
    private long m_lastSeekActualTimestamp = Long.MIN_VALUE;
    private long m_lastSeekTimestamp = Long.MIN_VALUE;
    private int m_pendingInputFrameCount = 0;
    private long m_preloadedTimestamp = Long.MIN_VALUE;
    private boolean m_sawInputEOS = false;
    private boolean m_sawOutputEOS = false;
    private boolean m_skipNonReferenceFrameWhenPlayback = false;
    private Surface m_surface = null;
    private SurfaceTexture m_surfaceTexture = null;
    private Semaphore m_surfaceTextureCreationSemaphore;
    private long m_temporalLayerEndTime = -1;
    private int m_texId;
    private long m_timestampOfCurTexFrame = Long.MIN_VALUE;
    private long m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
    private long m_timestampOfLastInputFrame = Long.MIN_VALUE;
    private int m_usedTemporalLayer = -1;
    private String m_videoFilePath;
    private int m_videoTrackIndex = -1;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                m_setOnFrameAvailableListener2 = SurfaceTexture.class.getDeclaredMethod("setOnFrameAvailableListener", SurfaceTexture.OnFrameAvailableListener.class, Handler.class);
                Log.d(TAG, "New SurfaceTexture.setOnFrameAvailableListener() method is available!");
            } catch (Exception unused) {
                m_setOnFrameAvailableListener2 = null;
            }
        }
    }

    NvAndroidVideoFileReader(Handler handler, Handler handler2) {
        this.m_handler = handler;
        this.m_cleanupHandler = handler2;
        this.m_bufferInfo = new MediaCodec.BufferInfo();
    }

    public boolean OpenFile(String str, int i, Context context, int i2, long j) {
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
        this.m_videoFilePath = str;
        this.m_context = context;
        try {
            int trackCount = mediaExtractor.getTrackCount();
            int i3 = 0;
            while (true) {
                if (i3 >= trackCount) {
                    break;
                } else if (this.m_extractor.getTrackFormat(i3).getString("mime").startsWith("video/")) {
                    this.m_videoTrackIndex = i3;
                    break;
                } else {
                    i3++;
                }
            }
            int i4 = this.m_videoTrackIndex;
            if (i4 < 0) {
                Log.e(TAG, "Failed to find a video track from " + str);
                CloseFile();
                return false;
            }
            this.m_extractor.selectTrack(i4);
            this.m_format = this.m_extractor.getTrackFormat(this.m_videoTrackIndex);
            if (Build.VERSION.SDK_INT == 16) {
                this.m_format.setInteger("max-input-size", 0);
            }
            boolean equals = Build.HARDWARE.equals("qcom");
            if (Build.VERSION.SDK_INT >= 23 && i2 >= 0) {
                MediaFormat mediaFormat = this.m_format;
                if (i2 <= 0) {
                    i2 = 120;
                }
                mediaFormat.setInteger("operating-rate", i2);
            }
            try {
                this.m_duration = this.m_format.getLong("durationUs");
                String string = this.m_format.getString("mime");
                this.m_actualDuration = this.m_duration;
                if (equals && this.m_format.containsKey("frame-rate")) {
                    this.m_format.setInteger("frame-rate", 0);
                }
                try {
                    if (m_setOnFrameAvailableListener2 != null) {
                        this.m_surfaceTexture = new SurfaceTexture(i);
                        m_setOnFrameAvailableListener2.invoke(this.m_surfaceTexture, this, this.m_handler);
                    } else {
                        this.m_surfaceTextureCreationSemaphore = new Semaphore(0);
                        this.m_texId = i;
                        this.m_handler.post(new Runnable() {
                            /* class com.cdv.io.NvAndroidVideoFileReader.AnonymousClass1 */

                            public void run() {
                                try {
                                    NvAndroidVideoFileReader.this.m_surfaceTexture = new SurfaceTexture(NvAndroidVideoFileReader.this.m_texId);
                                    NvAndroidVideoFileReader.this.m_surfaceTextureCreationSemaphore.release();
                                } catch (Exception e) {
                                    Log.e(NvAndroidVideoFileReader.TAG, "" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
                        this.m_surfaceTextureCreationSemaphore.acquire();
                        this.m_surfaceTextureCreationSemaphore = null;
                        this.m_texId = 0;
                        if (this.m_surfaceTexture == null) {
                            CloseFile();
                            return false;
                        }
                        this.m_surfaceTexture.setOnFrameAvailableListener(this);
                    }
                    this.m_surface = new Surface(this.m_surfaceTexture);
                    this.m_decoderSetupFailed = false;
                    if (!SetupDecoder(string)) {
                        this.m_decoderSetupFailed = true;
                        CloseFile();
                        return false;
                    }
                    this.m_contiuousDecodingThreshold = j;
                    this.m_usedTemporalLayer = -1;
                    this.m_temporalLayerEndTime = -1;
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                    e.printStackTrace();
                    CloseFile();
                    return false;
                }
            } catch (Exception e2) {
                Log.e(TAG, "" + e2.getMessage());
                e2.printStackTrace();
                CloseFile();
                return false;
            }
        } catch (Exception e3) {
            Log.e(TAG, "" + e3.getMessage());
            e3.printStackTrace();
            CloseFile();
            return false;
        }
    }

    public void preload(long j) {
        int preloadInternal = preloadInternal(j);
        int i = 0;
        while (preloadInternal == 4) {
            this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
            Log.w(TAG, "Try to recreate MediaExtractor!");
            if (!recreateMediaExtractor()) {
                Log.e(TAG, "Failed to recreate MediaExtractor!");
            }
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
            if (!recreateMediaExtractor()) {
                Log.e(TAG, "Failed to recreate MediaExtractor!");
            }
        }
    }

    public boolean hasDecoderSetupFailed() {
        return this.m_decoderSetupFailed;
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
        }
        this.m_usedTemporalLayer = -1;
        this.m_temporalLayerEndTime = -1;
        this.m_videoFilePath = null;
        this.m_context = null;
    }

    public void SetDecodeTemporalLayer(int i, long j) {
        if (i != this.m_usedTemporalLayer) {
            this.m_temporalLayerEndTime = j;
            this.m_usedTemporalLayer = i;
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
        long j4 = this.m_timestampOfCurTexFrame;
        if (j4 != Long.MIN_VALUE && Math.abs(max - j4) <= j2) {
            return 0;
        }
        int SeekInternal = SeekInternal(max, j2, false, 2);
        if (SeekInternal == 0) {
            this.m_lastSeekTimestamp = max;
            this.m_lastSeekActualTimestamp = this.m_timestampOfCurTexFrame;
        } else {
            InvalidLastSeekTimestamp();
        }
        return SeekInternal;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0065 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0066  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int StartPlayback(long r12, long r14) {
        /*
        // Method dump skipped, instructions count: 105
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReader.StartPlayback(long, long):int");
        return 0;
    }

    public int GetNextVideoFrameForPlayback() {
        if (!IsValid()) {
            return 1;
        }
        if (!this.m_firstPlaybackTexFrameUnconsumed) {
            int DecodeToFrame = DecodeToFrame(Long.MIN_VALUE, 0, false, this.m_skipNonReferenceFrameWhenPlayback ? 1 : 0);
            InvalidLastSeekTimestamp();
            if (DecodeToFrame != 0) {
                return DecodeToFrame;
            }
        } else {
            this.m_firstPlaybackTexFrameUnconsumed = false;
        }
        return 0;
    }

    public long GetTimestampOfCurrentTextureFrame() {
        return this.m_timestampOfCurTexFrame;
    }

    public void GetTransformMatrixOfSurfaceTexture(float[] fArr) {
        SurfaceTexture surfaceTexture = this.m_surfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.getTransformMatrix(fArr);
        }
    }

    public void updateCurTexImage() {
        try {
            if (this.m_timestampOfCurTexFrame != Long.MIN_VALUE && !this.m_curTexImageUpdated) {
                if (this.m_surfaceTexture != null) {
                    this.m_surfaceTexture.updateTexImage();
                }
                this.m_curTexImageUpdated = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.m_frameSyncObject) {
            if (this.m_frameAvailable) {
                Log.e(TAG, "m_frameAvailable already set, frame could be dropped!");
            }
            this.m_frameAvailable = true;
            this.m_frameSyncObject.notifyAll();
        }
    }

    private boolean IsValid() {
        return this.m_decoder != null;
    }

    private int preloadInternal(long j) {
        if (!IsValid()) {
            return 2;
        }
        long max = Math.max(Math.min(j, this.m_duration - 1), 0L);
        int SeekInternal = SeekInternal(max, 0, true, 2);
        if (SeekInternal == 0) {
            if (this.m_timestampOfCurTexFrame == Long.MIN_VALUE) {
                return SeekInternal;
            }
            this.m_preloadedTimestamp = max;
            return SeekInternal;
        } else if (SeekInternal != 1 || this.m_timestampOfCurTexFrame != Long.MIN_VALUE) {
            return SeekInternal;
        } else {
            long j2 = this.m_timestampOfLastDecodedFrame;
            if (j2 == Long.MIN_VALUE || j2 >= this.m_duration - 100000) {
                return SeekInternal;
            }
            return 4;
        }
    }

    private boolean recreateMediaExtractor() {
        try {
            this.m_extractor.release();
            this.m_extractor = NvAndroidUtils.createMediaExtractorFromMediaFilePath(this.m_context, this.m_videoFilePath);
            if (this.m_extractor != null) {
                this.m_extractor.selectTrack(this.m_videoTrackIndex);
                this.m_extractorInOriginalState = true;
                return true;
            }
            throw new Exception("Failed to re-create media extractor!");
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            this.m_extractor = null;
            this.m_videoTrackIndex = -1;
            this.m_format = null;
            this.m_duration = 0;
            this.m_actualDuration = 0;
            this.m_extractorInOriginalState = true;
            CloseFile();
            return false;
        }
    }

    private boolean SetupDecoder(String str) {
        try {
            this.m_decoder = MediaCodec.createDecoderByType(str);
            this.m_decoder.configure(this.m_format, this.m_surface, (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
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
        updateCurTexImage();
        if (this.m_decoder != null && this.m_decoderStarted) {
            try {
                if (this.m_sawInputEOS && !this.m_sawOutputEOS) {
                    DrainOutputBuffers(false);
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
                /* class com.cdv.io.NvAndroidVideoFileReader.AnonymousClass2 */

                public void run() {
                    NvAndroidVideoFileReader.this.CleanupDecoderCore(true);
                }
            });
        }
        this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        this.m_timestampOfCurTexFrame = Long.MIN_VALUE;
        this.m_firstPlaybackTexFrameUnconsumed = false;
        this.m_pendingInputFrameCount = 0;
        this.m_timestampOfLastInputFrame = Long.MIN_VALUE;
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
        if (z) {
            Surface surface = this.m_surface;
            if (surface != null) {
                surface.release();
                this.m_surface = null;
            }
            SurfaceTexture surfaceTexture = this.m_surfaceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                this.m_surfaceTexture = null;
            }
        }
    }

    private int SeekInternal(long j, long j2, boolean z, int i) {
        long j3 = this.m_timestampOfLastDecodedFrame;
        boolean z2 = true;
        if ((j3 == Long.MIN_VALUE || j <= j3 || j >= j3 + this.m_contiuousDecodingThreshold) && (!this.m_extractorInOriginalState || j >= this.m_contiuousDecodingThreshold)) {
            z2 = false;
        }
        if (!z2) {
            try {
                this.m_extractor.seekTo(j, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                if (this.m_extractor.getSampleTime() < 0 && j < this.m_duration - 100000) {
                    Log.w(TAG, "Try to recreate MediaExtractor!");
                    if (!recreateMediaExtractor()) {
                        Log.e(TAG, "Failed to recreate MediaExtractor!");
                        CloseFile();
                        return 2;
                    }
                    this.m_extractor.seekTo(j, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                }
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
        return DecodeToFrame(j, j2, z, i);
    }

    private int DecodeToFrame(long j, long j2, boolean z, int i) {
        try {
            return DoDecodeToFrame(j, j2, z, i);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            CleanupDecoder(false);
            return 2;
        }
    }

    private int DoDecodeToFrame(long j, long j2, boolean z, int i) {
        boolean z2;
        boolean z3;
        boolean z4;
        int dequeueInputBuffer;
        int max = Math.max(this.m_decoderInputBuffers.length / 3, 2);
        boolean z5 = false;
        int i2 = 0;
        do {
            boolean z6 = true;
            if (this.m_sawOutputEOS) {
                if (j != Long.MIN_VALUE) {
                    long j3 = this.m_timestampOfCurTexFrame;
                    if (j3 != Long.MIN_VALUE) {
                        if (z5) {
                            return 0;
                        }
                        if (this.m_sawInputEOS) {
                            long j4 = this.m_timestampOfLastInputFrame;
                            if (j4 == Long.MIN_VALUE || j3 < j4) {
                                return 1;
                            }
                            return 0;
                        }
                    }
                }
                return 1;
            } else if (isInterruptedDecoding()) {
                Log.d(TAG, "Interrupted video Decoding ");
                return 3;
            } else {
                if (!this.m_sawInputEOS && (dequeueInputBuffer = this.m_decoder.dequeueInputBuffer(CommonData.ONE_FRAME)) >= 0) {
                    ByteBuffer byteBuffer = this.m_decoderInputBuffers[dequeueInputBuffer];
                    while (true) {
                        int readSampleData = this.m_extractor.readSampleData(byteBuffer, 0);
                        if (readSampleData < 0) {
                            this.m_decoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            this.m_sawInputEOS = true;
                            break;
                        }
                        if (this.m_extractor.getSampleTrackIndex() != this.m_videoTrackIndex) {
                            Log.w(TAG, "WEIRD: got sample from track " + this.m_extractor.getSampleTrackIndex() + ", expected " + this.m_videoTrackIndex);
                        }
                        long sampleTime = this.m_extractor.getSampleTime();
                        if (((this.m_extractor.getSampleFlags() & 1) != 0) || !canSkipFrame(byteBuffer, sampleTime)) {
                            if (!(i == 1 || (i == 2 && sampleTime < j - j2)) || !isNonReferenceFrame(byteBuffer)) {
                                this.m_timestampOfLastInputFrame = sampleTime;
                                this.m_decoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, 0);
                                this.m_inputBufferQueued = true;
                                this.m_pendingInputFrameCount++;
                                this.m_extractor.advance();
                                this.m_extractorInOriginalState = false;
                            } else {
                                this.m_extractor.advance();
                                this.m_extractorInOriginalState = false;
                            }
                        } else {
                            this.m_extractor.advance();
                            this.m_extractorInOriginalState = false;
                        }
                    }
                }
                int dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, (long) ((this.m_pendingInputFrameCount > max || this.m_sawInputEOS) ? 4000 : 0));
                i2++;
                if (!(dequeueOutputBuffer == -1 || dequeueOutputBuffer == -3)) {
                    if (dequeueOutputBuffer == -2) {
                        this.m_decoder.getOutputFormat();
                    } else if (dequeueOutputBuffer < 0) {
                        Log.e(TAG, "Unexpected result from decoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                        return 2;
                    } else {
                        if ((this.m_bufferInfo.flags & 4) != 0) {
                            this.m_sawOutputEOS = true;
                        }
                        if (!this.m_sawOutputEOS) {
                            this.m_timestampOfLastDecodedFrame = this.m_bufferInfo.presentationTimeUs;
                            this.m_pendingInputFrameCount--;
                            if (j != Long.MIN_VALUE) {
                                boolean z7 = this.m_timestampOfLastDecodedFrame >= j - j2;
                                if (z7 || !this.m_sawInputEOS || z) {
                                    z2 = z5;
                                    z6 = z7;
                                } else {
                                    long j5 = this.m_timestampOfLastInputFrame;
                                    if (j5 == Long.MIN_VALUE || this.m_timestampOfLastDecodedFrame < j5) {
                                        z6 = z7;
                                        z4 = z5;
                                        z3 = false;
                                    } else {
                                        z3 = true;
                                        z4 = true;
                                    }
                                    z2 = z4;
                                    i2 = 0;
                                }
                            } else {
                                z2 = z5;
                            }
                            z3 = false;
                            i2 = 0;
                        } else {
                            z2 = z5;
                            z3 = false;
                            z6 = false;
                        }
                        if (z6) {
                            if (!z) {
                                updateCurTexImage();
                            }
                            synchronized (this.m_frameSyncObject) {
                                this.m_frameAvailable = false;
                            }
                        }
                        this.m_decoder.releaseOutputBuffer(dequeueOutputBuffer, z6);
                        if (z6) {
                            boolean z8 = !z;
                            if (AwaitNewImage(z8)) {
                                this.m_timestampOfCurTexFrame = this.m_bufferInfo.presentationTimeUs;
                                this.m_curTexImageUpdated = z8;
                                if (!z3) {
                                    return 0;
                                }
                            } else {
                                Log.e(TAG, "Render decoded frame to surface texture failed!");
                                return 2;
                            }
                        }
                        z5 = z2;
                    }
                }
            }
        } while (i2 <= 100);
        Log.e(TAG, "We have tried too many times and can't decode a frame!");
        return 2;
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

    private boolean canSkipFrame(ByteBuffer byteBuffer, long j) {
        if (byteBuffer == null || byteBuffer.limit() < 16 || this.m_usedTemporalLayer < 0 || j >= this.m_temporalLayerEndTime) {
            return false;
        }
        byte[] bArr = new byte[16];
        int position = byteBuffer.position();
        byteBuffer.get(bArr);
        byteBuffer.position(position);
        int i = bArr[4] & 31;
        if (bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1 && (i == 14 || i == 20)) {
            if (!(((bArr[5] & 255) >> 7) > 0) || (((bArr[7] & 255) >> 5) & 7) <= this.m_usedTemporalLayer) {
                return false;
            }
        } else if (this.m_usedTemporalLayer > 0) {
            return true;
        } else {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003f, code lost:
        if (r6 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r5.m_surfaceTexture.updateTexImage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0047, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
        android.util.Log.e(com.cdv.io.NvAndroidVideoFileReader.TAG, "" + r6.getMessage());
        r6.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean AwaitNewImage(boolean r6) {
        /*
        // Method dump skipped, instructions count: 109
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.cdv.io.NvAndroidVideoFileReader.AwaitNewImage(boolean):boolean");
        return r6;
    }

    private void DrainOutputBuffers(boolean z) {
        if (z || (this.m_sawInputEOS && !this.m_sawOutputEOS)) {
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

    private boolean isInterruptedDecoding() {
        NvAndroidInterruptionChecker nvAndroidInterruptionChecker = this.m_interruptionChecker;
        if (nvAndroidInterruptionChecker == null) {
            return false;
        }
        return nvAndroidInterruptionChecker.isInterrupted();
    }
}
