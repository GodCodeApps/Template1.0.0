package com.meicam.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.Log;
import com.cdv.io.NvAndroidVirtualCameraSurfaceTexture;
import com.meicam.sdk.NvsCustomVideoFx;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class NvsStreamingContext {
    public static final int AV_FILEINFO_EXTRA_NULL = 0;
    public static final int AV_FILEINFO_EXTRA_PIXELFORMAT = 1;
    public static final int AV_FILEINFO_EXTRA_VIDEO_CODEC_ID = 2;
    public static final int BUFFER_IMAGE_ROTATION_0 = 0;
    public static final int BUFFER_IMAGE_ROTATION_180 = 2;
    public static final int BUFFER_IMAGE_ROTATION_270 = 3;
    public static final int BUFFER_IMAGE_ROTATION_90 = 1;
    public static final int CAMERA_FLASH_MODE_AUTO = 4;
    public static final int CAMERA_FLASH_MODE_OFF = 1;
    public static final int CAMERA_FLASH_MODE_ON = 2;
    public static final int CAMERA_FLASH_MODE_TORCH = 8;
    public static final int CAPTURE_DEVICE_ERROR_SERVER_DIED = 2;
    public static final int CAPTURE_DEVICE_ERROR_UNKNOWN = 1;
    public static final int CLEAR_CACHE_FLAG_AVFILE_INFO = 8;
    public static final int CLEAR_CACHE_FLAG_AVFILE_KEYFRAME_INFO = 16;
    public static final int CLEAR_CACHE_FLAG_CAPTION_FONT_INFO = 32;
    public static final int CLEAR_CACHE_FLAG_ICON_ENGINE = 2;
    public static final int CLEAR_CACHE_FLAG_STREAMING_ENGINE = 1;
    public static final int CLEAR_CACHE_FLAG_WAVEFORM_ENGINE = 4;
    public static final String COMPILE_AUDIO_BITRATE = "audio bitrate";
    public static final String COMPILE_AUDIO_ENCODER_NAME = "audio encoder name";
    public static final String COMPILE_BITRATE = "bitrate";
    public static final int COMPILE_BITRATE_GRADE_HIGH = 2;
    public static final int COMPILE_BITRATE_GRADE_LOW = 0;
    public static final int COMPILE_BITRATE_GRADE_MEDIUM = 1;
    public static final int COMPILE_ENCODE_PROFILE_BASELINE = 1;
    public static final int COMPILE_ENCODE_PROFILE_HIGH = 3;
    public static final int COMPILE_ENCODE_PROFILE_MAIN = 2;
    public static final String COMPILE_FPS = "fps";
    public static final String COMPILE_GOP_SIZE = "gopsize";
    public static final String COMPILE_LOSSLESS_AUDIO = "lossless audio";
    public static final String COMPILE_OPTIMIZE_FOR_NETWORK_USE = "optimize-for-network-use";
    public static final String COMPILE_SOFTWARE_ENCODER_CRF = "software encorder crf";
    public static final String COMPILE_SOFTWARE_ENCODER_CRF_BITRATE_MAX = "software encorder crf bitrate max";
    public static final String COMPILE_SOFTWARE_ENCODER_MODE = "software encorder mode";
    public static final String COMPILE_SOFTWARE_ENCODER_PRESET = "software encorder preset";
    public static final String COMPILE_USE_OPERATING_RATE = "use operating rate";
    public static final String COMPILE_VIDEO_ENCODER_NAME = "video encoder name";
    public static final String COMPILE_VIDEO_ENCODER_PROFILE = "encorder profile";
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_1080 = 3;
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_2160 = 4;
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_360 = 0;
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_480 = 1;
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_720 = 2;
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_CUSTOM = 256;
    public static final int DEBUG_LEVEL_DEBUG = 3;
    public static final int DEBUG_LEVEL_ERROR = 1;
    public static final int DEBUG_LEVEL_NONE = 0;
    public static final int DEBUG_LEVEL_WARNING = 2;
    public static final int HUMAN_DETECTION_DATA_TYPE_FAKE_FACE = 0;
    public static final int HUMAN_DETECTION_DATA_TYPE_MAKEUP = 1;
    public static final int HUMAN_DETECTION_FEATURE_AVATAR_EXPRESSION = 4;
    public static final int HUMAN_DETECTION_FEATURE_EXTRA = 128;
    public static final int HUMAN_DETECTION_FEATURE_FACE_ACTION = 2;
    public static final int HUMAN_DETECTION_FEATURE_FACE_LANDMARK = 1;
    public static final int HUMAN_DETECTION_FEATURE_IMAGE_MODE = 16;
    public static final int HUMAN_DETECTION_FEATURE_MULTI_THREAD = 32;
    public static final int HUMAN_DETECTION_FEATURE_SINGLE_THREAD = 64;
    public static final int HUMAN_DETECTION_FEATURE_VIDEO_MODE = 8;
    public static final String RECORD_BITRATE = "bitrate";
    public static final String RECORD_GOP_SIZE = "gopsize";
    public static final int STREAMING_CONTEXT_FLAG_ASYNC_ENGINE_STATE = 16;
    public static final int STREAMING_CONTEXT_FLAG_ASYNC_INITIALIZED = 64;
    public static final int STREAMING_CONTEXT_FLAG_COMPACT_MEMORY_MODE = 2;
    public static final int STREAMING_CONTEXT_FLAG_NO_HARDWARE_VIDEO_READER = 8;
    public static final int STREAMING_CONTEXT_FLAG_SUPPORT_16K_EDIT = 128;
    public static final int STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT = 1;
    public static final int STREAMING_CONTEXT_FLAG_SUPPORT_8K_EDIT = 4;
    public static final int STREAMING_CONTEXT_FLAG_USE_SYSTEM_IMAGE_READER = 512;
    public static final int STREAMING_CONTEXT_VIDEO_DECODER_WITHOUT_SURFACE_TEXTURE = 32;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME = 32;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_DISABLE_FLIP_FOR_FRONT_CAMERA = 2048;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_DONT_CAPTURE_AUDIO = 16;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER = 4;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_ENABLE_TAKE_PICTURE = 8192;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_FACE_ACTION_WITH_PARTICLE = 256;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_GRAB_CAPTURED_VIDEO_FRAME = 1;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_INPUT_ASPECT_RATIO_USED = 4096;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_LOW_PIPELINE_SIZE = 512;
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE = 8;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_BUDDY_HOST_VIDEO_FRAME = 32;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DISABLE_ALIGN_VIDEO_SIZE = 256;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER = 1;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DISABLE_MEDIA_MUXER = 16;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DISABLE_PRELOADER_ON_SOURCE = 64;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DONT_USE_INPUT_SURFACE = 2;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_ONLY_AUDIO = 8;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_ONLY_VIDEO = 4;
    public static final int STREAMING_ENGINE_COMPILE_FLAG_TRUNCATE_AUDIO_STREAM = 128;
    public static final int STREAMING_ENGINE_HARDWARE_ERROR_TYPE_VIDEO_DECODER_SETUP_ERROR = 2;
    public static final int STREAMING_ENGINE_HARDWARE_ERROR_TYPE_VIDEO_DECODING_ERROR = 3;
    public static final int STREAMING_ENGINE_HARDWARE_ERROR_TYPE_VIDEO_ENCODER_SETUP_ERROR = 0;
    public static final int STREAMING_ENGINE_HARDWARE_ERROR_TYPE_VIDEO_ENCODING_ERROR = 1;
    public static final int STREAMING_ENGINE_INTERRUPT_STOP = 4;
    public static final int STREAMING_ENGINE_PLAYBACK_EXCEPTION_TYPE_MEDIA_FILE_ERROR = 0;
    public static final int STREAMING_ENGINE_PLAYBACK_FLAG_AUTO_CACHE_ALL_CAF_FRAMES = 128;
    public static final int STREAMING_ENGINE_PLAYBACK_FLAG_BUDDY_HOST_VIDEO_FRAME = 32;
    public static final int STREAMING_ENGINE_PLAYBACK_FLAG_DISABLE_FIXED_PREROLL_TIME = 16;
    public static final int STREAMING_ENGINE_PLAYBACK_FLAG_DISABLE_PRELOADER_ON_SOURCE = 64;
    public static final int STREAMING_ENGINE_PLAYBACK_FLAG_LOW_PIPELINE_SIZE = 8;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_DISABLE_HARDWARE_ENCODER = 4;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_DISABLE_MEDIA_MUXER = 128;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_DONT_USE_INPUT_SURFACE = 64;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_IGNORE_VIDEO_ROTATION = 32;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_ONLY_RECORD_VIDEO = 16;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_RECORDING_WITHOUT_FX = 256;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_SOFTWARE_VIDEO_INTRA_FRAME_ONLY_FAST_STOP = 8;
    public static final int STREAMING_ENGINE_RECORDING_FLAG_VIDEO_INTRA_FRAME_ONLY = 2;
    public static final int STREAMING_ENGINE_SEEK_FLAG_BUDDY_HOST_VIDEO_FRAME = 16;
    public static final int STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER = 4;
    public static final int STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER = 2;
    public static final int STREAMING_ENGINE_SEND_BUFFER_FLAG_ONLY_FOR_BUDDY = 2;
    public static final int STREAMING_ENGINE_SEND_BUFFER_FLAG_ONLY_FOR_GRAB = 1;
    public static final int STREAMING_ENGINE_STATE_CAPTUREPREVIEW = 1;
    public static final int STREAMING_ENGINE_STATE_CAPTURERECORDING = 2;
    public static final int STREAMING_ENGINE_STATE_COMPILE = 5;
    public static final int STREAMING_ENGINE_STATE_PLAYBACK = 3;
    public static final int STREAMING_ENGINE_STATE_SEEKING = 4;
    public static final int STREAMING_ENGINE_STATE_STOPPED = 0;
    public static final int STREAMING_ENGINE_STOP_FLAG_ASYNC = 2;
    public static final int STREAMING_ENGINE_STOP_FLAG_FORCE_STOP_COMPILATION = 1;
    private static final String TAG = "Meicam";
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_EXTREMELY_HIGH = 4;
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH = 2;
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_LOW = 0;
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_MEDIUM = 1;
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_SUPER_HIGH = 3;
    public static final int VIDEO_PREVIEW_SIZEMODE_FULLSIZE = 0;
    public static final int VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE = 1;
    public static final int VIDEO_STABILIZATION_MODE_AUTO = 1;
    public static final int VIDEO_STABILIZATION_MODE_OFF = 0;
    public static final int VIDEO_STABILIZATION_MODE_STANDARD = 2;
    public static final int VIDEO_STABILIZATION_MODE_SUPER = 3;
    private static AssetManager m_assetManager = null;
    private static ClassLoader m_classLoader = null;
    private static Context m_context = null;
    private static boolean m_customNativeLibraryDirPath = false;
    private static int m_debugLevel = 3;
    private static boolean m_faceDetectionLibLoaded = false;
    private static int m_iconSize = 0;
    private static boolean m_initializedOnce = false;
    private static NvsStreamingContext m_instance = null;
    public static String m_logFilePath = null;
    private static int m_maxAudioReaderCount = 0;
    private static int m_maxIconReader = 0;
    private static int m_maxImageReaderCount = 0;
    private static int m_maxReaderCount = 0;
    private static String m_nativeLibraryDirPath = null;
    private static boolean m_saveDebugMessagesToFile = false;
    private NvsAssetPackageManager m_assetPackageManager = null;
    private CaptureRecordingDurationCallback m_captureRecordingDurationCallback;
    private CaptureRecordingFrameReachedCallback m_captureRecordingFrameReachedCallback;
    private CaptureRecordingStartedCallback m_captureRecordingStartedCallback;
    private CaptureDeviceCallback m_catpureDeviceCallback;
    private CapturedPictureCallback m_catpuredPictureCallback;
    private CapturedVideoFrameGrabberCallback m_catpuredVideoFrameGrabberCallback;
    private CompileCallback m_compileCallback;
    private CompileCallback2 m_compileCallback2;
    private Hashtable<String, Object> m_compileConfigurations = new Hashtable<>();
    private CompileFloatProgressCallback m_compileFloatProgressCallback;
    private float m_compileVideoBitrateMultiplier = 1.0f;
    private HardwareErrorCallback m_hardwareErrorCallback;
    private long m_internalObject = 0;
    private boolean m_isAuxiliaryContext = false;
    private PlaybackCallback m_playbackCallback;
    private PlaybackCallback2 m_playbackCallback2;
    private PlaybackDelayCallback m_playbackDelayCallback;
    private PlaybackExceptionCallback m_playbackExceptionCallback;
    private float m_recordVideoBitrateMultiplier = 1.0f;
    private SeekingCallback m_seekingCallback;
    private StreamingEngineCallback m_streamingEngineCallback;
    private TimelineTimestampCallback m_timelineTimestampCallback;

    public interface CaptureDeviceCallback {
        void onCaptureDeviceAutoFocusComplete(int i, boolean z);

        void onCaptureDeviceCapsReady(int i);

        void onCaptureDeviceError(int i, int i2);

        void onCaptureDevicePreviewResolutionReady(int i);

        void onCaptureDevicePreviewStarted(int i);

        void onCaptureDeviceStopped(int i);

        void onCaptureRecordingError(int i);

        void onCaptureRecordingFinished(int i);
    }

    public static class CaptureDeviceCapability {
        public float exposureCompensationStep;
        public int maxExposureCompensation;
        public int maxZoom;
        public int minExposureCompensation;
        public boolean supportAutoExposure;
        public boolean supportAutoFocus;
        public boolean supportContinuousFocus;
        public boolean supportExposureCompensation;
        public boolean supportFlash;
        public ArrayList<NvsSize> supportVideoSize;
        public boolean supportVideoStabilization;
        public boolean supportZoom;
        public float[] zoomRatios;
    }

    public interface CaptureRecordingDurationCallback {
        void onCaptureRecordingDuration(int i, long j);
    }

    public interface CaptureRecordingFrameReachedCallback {
        void onRecordingFirstVideoFrameReached(int i, long j);
    }

    public interface CaptureRecordingStartedCallback {
        void onCaptureRecordingStarted(int i);
    }

    public interface CapturedPictureCallback {
        void onCapturedPictureArrived(ByteBuffer byteBuffer, NvsVideoFrameInfo nvsVideoFrameInfo);
    }

    public interface CapturedVideoFrameGrabberCallback {
        void onCapturedVideoFrameGrabbedArrived(ByteBuffer byteBuffer, NvsVideoFrameInfo nvsVideoFrameInfo);
    }

    public interface CompileCallback {
        void onCompileFailed(NvsTimeline nvsTimeline);

        void onCompileFinished(NvsTimeline nvsTimeline);

        void onCompileProgress(NvsTimeline nvsTimeline, int i);
    }

    public interface CompileCallback2 {
        void onCompileCompleted(NvsTimeline nvsTimeline, boolean z);
    }

    public interface CompileFloatProgressCallback {
        void onCompileFloatProgress(NvsTimeline nvsTimeline, float f);
    }

    public interface HardwareErrorCallback {
        void onHardwareError(int i, String str);
    }

    public interface ImageGrabberCallback {
        void onImageGrabbedArrived(Bitmap bitmap, long j);
    }

    public interface PlaybackCallback {
        void onPlaybackEOF(NvsTimeline nvsTimeline);

        void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline);

        void onPlaybackStopped(NvsTimeline nvsTimeline);
    }

    public interface PlaybackCallback2 {
        void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long j);
    }

    public interface PlaybackDelayCallback {
        void onPlaybackDelayed(NvsTimeline nvsTimeline, long j, boolean z);
    }

    public interface PlaybackExceptionCallback {
        void onPlaybackException(NvsTimeline nvsTimeline, int i, String str);
    }

    public static class SdkVersion {
        public int majorVersion;
        public int minorVersion;
        public int revisionNumber;
    }

    public interface SeekingCallback {
        void onSeekingTimelinePosition(NvsTimeline nvsTimeline, long j);
    }

    public interface StreamingEngineCallback {
        void onFirstVideoFramePresented(NvsTimeline nvsTimeline);

        void onStreamingEngineStateChanged(int i);
    }

    public interface TimelineTimestampCallback {
        void onTimestampOutOfRange(NvsTimeline nvsTimeline);
    }

    public static class VerifyLicenseResult {
        public boolean needCheckExpiration;
        public boolean success;
    }

    private native boolean nativeApplyCaptureScene(String str);

    private native boolean nativeCanDecodeVideoStreamBySoftware(String str);

    private native void nativeCancelAutoFocus();

    private native boolean nativeCheckDontSetCameraParamOnRecordingWithSystemRecorder();

    private native void nativeClearCachedResources(boolean z);

    private native void nativeClearCachedResourcesFlags(boolean z, int i);

    private native NvsTimeline nativeCloneTimeline(NvsTimeline nvsTimeline);

    private static native void nativeClose();

    private static native void nativeCloseHumanDetection();

    private native boolean nativeCompileTimeline(NvsTimeline nvsTimeline, long j, long j2, String str, int i, int i2, float f, Hashtable hashtable, int i3);

    private native boolean nativeConnectCapturePreviewWithLiveWindow(Object obj);

    private native boolean nativeConnectCapturePreviewWithSurfaceTexture(SurfaceTexture surfaceTexture);

    private native boolean nativeConnectTimelineWithLiveWindow(NvsTimeline nvsTimeline, Object obj);

    private native boolean nativeConnectTimelineWithSurfaceTexture(NvsTimeline nvsTimeline, SurfaceTexture surfaceTexture, NvsRational nvsRational);

    private native boolean nativeCreateAuxiliaryStreamingContext(NvsStreamingContext nvsStreamingContext, int i);

    private native NvsTimeline nativeCreateTimeline(NvsVideoResolution nvsVideoResolution, NvsRational nvsRational, NvsAudioResolution nvsAudioResolution);

    private native void nativeDestoryAuxiliaryStreamingContext(NvsStreamingContext nvsStreamingContext);

    private native float nativeDetectEngineRenderFramePerSecond();

    private native void nativeDetectPackageName(Context context);

    private native int nativeDetectVideoFileKeyframeInterval(String str);

    private static native boolean nativeFunctionalityAuthorised(String str);

    private native NvsAVFileInfo nativeGetAVFileInfo(String str, int i);

    private static native NvsAVFileInfo nativeGetAVInfoFromFile(String str, int i);

    private native List<String> nativeGetAllBuiltinAudioFxNames();

    private native List<String> nativeGetAllBuiltinCaptureVideoFxNames();

    private native List<String> nativeGetAllBuiltinVideoFxNames();

    private native List<String> nativeGetAllBuiltinVideoTransitionNames();

    private native long nativeGetAssetPackageManager();

    private native NvsFxDescription nativeGetAudioFxDescription(String str);

    private native int nativeGetAudioStreamCountInMediaFile(String str);

    private native String nativeGetBeautyVideoFxName();

    private native CaptureDeviceCapability nativeGetCaptureDeviceCapability(int i);

    private native int nativeGetCaptureDeviceCount();

    private native NvsSize nativeGetCapturePreviewVideoSize(int i);

    private native NvsCaptureVideoFx nativeGetCaptureVideoFxByIndex(int i);

    private native int nativeGetCaptureVideoFxCount();

    private native String nativeGetCurrentCaptureSceneId();

    private native int nativeGetCustomCompileVideoHeight();

    private native String nativeGetDefaultThemeEndingLogoImageFilePath();

    private native String nativeGetDefaultVideoTransitionName();

    private native int nativeGetExposureCompensation();

    private native int nativeGetFlashMode();

    private native List<NvsFontInfo> nativeGetFontInfoByFilePath(String str);

    private native SdkVersion nativeGetSdkVersion();

    private static native int nativeGetStatisticsFrequency();

    private native int nativeGetStreamingEngineState();

    private native long nativeGetTimelineCurrentPosition(NvsTimeline nvsTimeline);

    private native NvsFxDescription nativeGetVideoFxDescription(String str);

    private native int nativeGetVideoStabilization();

    private native int nativeGetZoom();

    private native Bitmap nativeGrabImageFromTimeline(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i);

    private native boolean nativeGrabImageFromTimelineAsyncMode(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i);

    private static native int nativeHasARModule();

    private static native boolean nativeInit(String str, int i);

    private static native boolean nativeInitHumanDetection(Context context, String str, String str2, int i);

    private static native boolean nativeInitHumanDetectionExt(Context context, String str, String str2, int i);

    private static native boolean nativeInitJNI(Context context);

    private native NvsCaptureVideoFx nativeInsertBuiltinCaptureVideoFx(String str, int i);

    private native NvsCaptureVideoFx nativeInsertCustomCaptureVideoFx(NvsCustomVideoFx.Renderer renderer, int i);

    private native NvsCaptureVideoFx nativeInsertPackagedCaptureVideoFx(String str, int i);

    private native boolean nativeIsCaptureDeviceBackFacing(int i);

    private native boolean nativeIsCompilingPaused();

    private native boolean nativeIsDefaultCaptionFade();

    private static native boolean nativeIsNeedCheckExpiration();

    private native boolean nativeIsPlaybackPaused();

    private native boolean nativeIsRecordingPaused();

    private native boolean nativeIsSdkAuthorised();

    private static native boolean nativeIsStatisticsPrivateInfo();

    private native boolean nativePauseResumeCompiling(boolean z);

    private native boolean nativePauseResumePlayback(boolean z);

    private native boolean nativePauseResumeRecording(boolean z);

    private native boolean nativePlaybackTimeline(NvsTimeline nvsTimeline, long j, long j2, int i, boolean z, int i2);

    private native boolean nativePlaybackTimelineWithProxyScale(NvsTimeline nvsTimeline, long j, long j2, NvsRational nvsRational, boolean z, int i);

    private native String nativeRegisterFontByFilePath(String str);

    private native void nativeRemoveAllCaptureVideoFx();

    private native boolean nativeRemoveCaptureVideoFx(int i);

    private native void nativeRemoveCurrentCaptureScene();

    private native boolean nativeRemoveTimeline(NvsTimeline nvsTimeline);

    private native NvsColor nativeSampleColorFromCapturedVideoFrame(RectF rectF);

    private native boolean nativeSeekTimeline(NvsTimeline nvsTimeline, long j, int i, int i2);

    private native boolean nativeSeekTimelineWithProxyScale(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i);

    private native boolean nativeSendBufferToCapturePreview(byte[] bArr, long j, int i);

    private static native void nativeSetAssetManager(AssetManager assetManager);

    private native void nativeSetAudioOutputDeviceVolume(float f);

    private native void nativeSetAutoExposureRect(RectF rectF);

    private static native void nativeSetCaptureDeviceCallback(CaptureDeviceCallback captureDeviceCallback);

    private native void nativeSetCaptureFps(int i);

    private static native void nativeSetCaptureRecordingDurationCallback(CaptureRecordingDurationCallback captureRecordingDurationCallback);

    private static native void nativeSetCaptureRecordingFrameReachedCallback(CaptureRecordingFrameReachedCallback captureRecordingFrameReachedCallback);

    private static native void nativeSetCaptureRecordingStartedCallback(CaptureRecordingStartedCallback captureRecordingStartedCallback);

    private static native void nativeSetCapturedPictureCallback(CapturedPictureCallback capturedPictureCallback);

    private static native void nativeSetCapturedVideoFrameGrabberCallback(CapturedVideoFrameGrabberCallback capturedVideoFrameGrabberCallback);

    private native void nativeSetCompileCallback(CompileCallback compileCallback);

    private native void nativeSetCompileCallback2(CompileCallback2 compileCallback2);

    private native void nativeSetCompileFloatProgressCallback(CompileFloatProgressCallback compileFloatProgressCallback);

    private native void nativeSetCustomCompileVideoHeight(int i);

    private static native void nativeSetDebugLevel(int i);

    private native void nativeSetDefaultAudioTransitionName(String str);

    private native void nativeSetDefaultCaptionFade(boolean z);

    private native boolean nativeSetDefaultThemeEndingLogoImageFilePath(String str);

    private native void nativeSetExposureCompensation(int i);

    private native void nativeSetHardwareErrorCallback(HardwareErrorCallback hardwareErrorCallback);

    private static native void nativeSetIconSize(int i);

    private native void nativeSetImageGrabberCallback(ImageGrabberCallback imageGrabberCallback);

    private static native void nativeSetImageReaderCount(int i);

    private static native void nativeSetLogFilePath(String str);

    private static native void nativeSetMaxAudioReaderCount(int i);

    private static native void nativeSetMaxCafCacheMemorySize(int i);

    private static native void nativeSetMaxIconReader(int i);

    private static native void nativeSetMaxReaderCount(int i);

    private native void nativeSetMediaCodecIconReaderEnabled(String str, boolean z);

    private native void nativeSetMediaCodecVideoDecodingOperatingRate(String str, int i);

    private native void nativeSetPlaybackCallback(PlaybackCallback playbackCallback);

    private native void nativeSetPlaybackCallback2(PlaybackCallback2 playbackCallback2);

    private native void nativeSetPlaybackDelayCallback(PlaybackDelayCallback playbackDelayCallback);

    private native void nativeSetPlaybackExceptionCallback(PlaybackExceptionCallback playbackExceptionCallback);

    private static native void nativeSetSaveDebugMessagesToFile(boolean z);

    private native void nativeSetSeekingCallback(SeekingCallback seekingCallback);

    private native void nativeSetStreamingEngineCallback(StreamingEngineCallback streamingEngineCallback);

    private native boolean nativeSetThemeEndingEnabled(boolean z);

    private native void nativeSetUserWatermarkForCapture(String str, int i, int i2, float f, int i3, int i4, int i5);

    private native void nativeSetVideoStabilization(int i);

    private native void nativeSetZoom(int i);

    private static native boolean nativeSetupHumanDetectionData(int i, String str);

    private native void nativeStartAutoFocus(RectF rectF);

    private native boolean nativeStartBufferCapturePreview(int i, int i2, NvsRational nvsRational, int i3, boolean z);

    private native boolean nativeStartCapturePreview(int i, int i2, int i3, NvsRational nvsRational);

    private native boolean nativeStartCapturePreviewWithSpecialSize(int i, int i2, int i3, NvsSize nvsSize);

    private native void nativeStartContinuousFocus();

    private native boolean nativeStartDualBufferCapturePreview(int i, int i2, int i3, int i4, int i5, boolean z, int i6, NvsRational nvsRational, int i7, NvAndroidVirtualCameraSurfaceTexture nvAndroidVirtualCameraSurfaceTexture);

    private native boolean nativeStartRecording(String str, float f, int i, int i2, int i3);

    private native void nativeStop(int i);

    private native void nativeStopRecording(boolean z);

    private native boolean nativeTakePicture(int i);

    private native void nativeToggleFlashMode(int i);

    private static native VerifyLicenseResult nativeVerifySdkLicenseFile(Context context, String str, boolean z);

    public boolean startCapturePreviewForLiveStreaming(int i, int i2, int i3, NvsRational nvsRational, String str) {
        return false;
    }

    public static void setNativeLibraryDirPath(String str) {
        NvsUtils.checkFunctionInMainThread();
        m_nativeLibraryDirPath = str;
        if (str != null) {
            m_customNativeLibraryDirPath = true;
        }
    }

    public static void setLoadPluginFromAssets(boolean z) {
        NvsUtils.checkFunctionInMainThread();
    }

    public static void setPluginDirPath(String str) {
        NvsUtils.checkFunctionInMainThread();
    }

    public static void setDebugLevel(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i != m_debugLevel) {
            m_debugLevel = i;
            if (m_instance != null) {
                nativeSetDebugLevel(m_debugLevel);
            }
        }
    }

    public static void setSaveDebugMessagesToFile(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        if (z != m_saveDebugMessagesToFile) {
            m_saveDebugMessagesToFile = z;
            if (m_instance != null) {
                nativeSetSaveDebugMessagesToFile(m_saveDebugMessagesToFile);
            }
        }
    }

    public static void setLogFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        if (str != null) {
            if (str.endsWith("/")) {
                if (str.length() - 2 > 0) {
                    str = str.substring(0, str.length() - 2);
                } else {
                    return;
                }
            }
            if (!str.isEmpty()) {
                File file = new File(str);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (file.isDirectory()) {
                    m_logFilePath = str;
                    if (m_instance != null) {
                        nativeSetLogFilePath(str);
                    }
                }
            }
        }
    }

    public static Context getContext() {
        NvsUtils.checkFunctionInMainThread();
        return m_context;
    }

    public static ClassLoader getClassLoader() {
        NvsUtils.checkFunctionInMainThread();
        return m_classLoader;
    }

    public boolean isSdkAuthorised() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsSdkAuthorised();
    }

    public static NvsStreamingContext init(Context context, String str, int i) {
        NvsCheckExpirationOnline init;
        boolean z;
        NvsUtils.checkFunctionInMainThread();
        NvsStreamingContext nvsStreamingContext = m_instance;
        if (nvsStreamingContext != null) {
            return nvsStreamingContext;
        }
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (m_nativeLibraryDirPath == null) {
            m_nativeLibraryDirPath = applicationInfo.nativeLibraryDir + "/";
        }
        String str2 = ("HOME=" + context.getFilesDir().getAbsolutePath()) + "\tTMPDIR=" + context.getFilesDir().getAbsolutePath();
        try {
            m_assetManager = context.getAssets();
            m_context = context.getApplicationContext();
            m_classLoader = m_context.getClassLoader();
            boolean z2 = false;
            if (!m_initializedOnce) {
                if (m_customNativeLibraryDirPath) {
                    initNativeLibraryDirPath(context, m_nativeLibraryDirPath);
                }
                try {
                    Class.forName("com.meicam.sdk.NvsLazyLoadingIndicator");
                    z = true;
                } catch (Exception unused) {
                    z = false;
                }
                if (!z) {
                    tryLoadFaceDetectionLibrary();
                }
                tryLoadNativeLibrary("aiEngine");
                tryLoadNativeLibrary("aiEngine_g");
                loadNativeLibrary("NvStreamingSdkCore");
            }
            if (nativeInitJNI(m_context)) {
                nativeSetAssetManager(m_assetManager);
                if (m_maxReaderCount > 0) {
                    nativeSetMaxReaderCount(m_maxReaderCount);
                }
                if (m_iconSize > 0) {
                    nativeSetIconSize(m_iconSize);
                }
                if (m_maxIconReader > 0) {
                    nativeSetMaxIconReader(m_maxIconReader);
                }
                if (m_maxAudioReaderCount > 0) {
                    nativeSetMaxAudioReaderCount(m_maxAudioReaderCount);
                }
                if (m_maxImageReaderCount > 0) {
                    nativeSetImageReaderCount(m_maxImageReaderCount);
                }
                nativeSetDebugLevel(m_debugLevel);
                nativeSetSaveDebugMessagesToFile(m_saveDebugMessagesToFile);
                if (NvsSystemVariableManager.getSystemVariableInt(context, "isExpired") == 1) {
                    z2 = true;
                }
                VerifyLicenseResult verifyLicenseResult = new VerifyLicenseResult();
                if (!m_initializedOnce) {
                    verifyLicenseResult = nativeVerifySdkLicenseFile(context, str, z2);
                }
                if (verifyLicenseResult.needCheckExpiration && (init = NvsCheckExpirationOnline.init(context)) != null) {
                    init.startCheck();
                }
                if (!nativeInit(str2, i)) {
                    return null;
                }
                if (m_logFilePath != null) {
                    nativeSetLogFilePath(m_logFilePath);
                }
                m_instance = new NvsStreamingContext(context, true);
                m_initializedOnce = true;
                if (m_instance.isSdkAuthorised() && nativeIsNeedCheckExpiration()) {
                    NvsStatisticsSender.init(context).sendStatistics(nativeGetStatisticsFrequency(), nativeIsStatisticsPrivateInfo());
                }
                return m_instance;
            }
            throw new Exception("nativeInitJNI() failed!");
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            m_context = null;
            m_classLoader = null;
            m_assetManager = null;
            return null;
        }
    }

    public static NvsStreamingContext init(Activity activity, String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        return init((Context) activity, str, i);
    }

    public static NvsStreamingContext init(Activity activity, String str) {
        NvsUtils.checkFunctionInMainThread();
        return init(activity, str, 0);
    }

    public static void close() {
        NvsUtils.checkFunctionInMainThread();
        if (m_instance != null) {
            NvsCheckExpirationOnline instance = NvsCheckExpirationOnline.instance();
            if (instance != null) {
                instance.release();
            }
            NvsStatisticsSender instance2 = NvsStatisticsSender.getInstance();
            if (instance2 != null) {
                instance2.release();
            }
            NvsAssetPackageManager assetPackageManager = m_instance.getAssetPackageManager();
            if (assetPackageManager != null) {
                assetPackageManager.setCallbackInterface(null);
                assetPackageManager.setInternalObject(0);
            }
            m_instance.setCaptureDeviceCallback(null);
            m_instance.setCapturedVideoFrameGrabberCallback(null);
            m_instance.setCaptureRecordingStartedCallback(null);
            m_instance.setCaptureRecordingDurationCallback(null);
            m_instance.setPlaybackCallback(null);
            m_instance.setCompileCallback(null);
            m_instance.setStreamingEngineCallback(null);
            m_instance.setTimelineTimestampCallback(null);
            m_instance.setCompileCallback2(null);
            m_instance.setCompileFloatProgressCallback(null);
            m_instance.setPlaybackCallback2(null);
            m_instance.setPlaybackDelayCallback(null);
            m_instance.setPlaybackExceptionCallback(null);
            m_instance.setHardwareErrorCallback(null);
            m_instance = null;
            m_context = null;
            m_classLoader = null;
            nativeSetAssetManager(null);
            m_assetManager = null;
            nativeClose();
        }
    }

    public static NvsStreamingContext getInstance() {
        NvsUtils.checkFunctionInMainThread();
        return m_instance;
    }

    public static void openMainThreadChecker(boolean z) {
        NvsUtils.setCheckEnable(z);
    }

    public static boolean initHumanDetection(Context context, String str, String str2, int i) {
        tryLoadFaceDetectionLibrary();
        if (!m_faceDetectionLibLoaded) {
            return false;
        }
        return nativeInitHumanDetection(context, str, str2, i);
    }

    public static boolean initHumanDetectionExt(Context context, String str, String str2, int i) {
        return nativeInitHumanDetectionExt(context, str, str2, i);
    }

    public static boolean setupHumanDetectionData(int i, String str) {
        return nativeSetupHumanDetectionData(i, str);
    }

    public static void closeHumanDetection() {
        nativeCloseHumanDetection();
    }

    public static int hasARModule() {
        NvsUtils.checkFunctionInMainThread();
        return nativeHasARModule();
    }

    public float detectEngineRenderFramePerSecond() {
        NvsUtils.checkFunctionInMainThread();
        return nativeDetectEngineRenderFramePerSecond();
    }

    private NvsStreamingContext(Context context, boolean z) {
        if (z) {
            this.m_assetPackageManager = new NvsAssetPackageManager(false);
            this.m_assetPackageManager.setInternalObject(nativeGetAssetPackageManager());
            nativeDetectPackageName(context);
        }
    }

    public SdkVersion getSdkVersion() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetSdkVersion();
    }

    public NvsAssetPackageManager getAssetPackageManager() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_assetPackageManager;
    }

    public void setCaptureDeviceCallback(CaptureDeviceCallback captureDeviceCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_catpureDeviceCallback = captureDeviceCallback;
        nativeSetCaptureDeviceCallback(captureDeviceCallback);
    }

    public void setCapturedVideoFrameGrabberCallback(CapturedVideoFrameGrabberCallback capturedVideoFrameGrabberCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_catpuredVideoFrameGrabberCallback = capturedVideoFrameGrabberCallback;
        nativeSetCapturedVideoFrameGrabberCallback(capturedVideoFrameGrabberCallback);
    }

    public void setCapturedPictureCallback(CapturedPictureCallback capturedPictureCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_catpuredPictureCallback = capturedPictureCallback;
        nativeSetCapturedPictureCallback(capturedPictureCallback);
    }

    public void setCaptureRecordingStartedCallback(CaptureRecordingStartedCallback captureRecordingStartedCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_captureRecordingStartedCallback = captureRecordingStartedCallback;
        nativeSetCaptureRecordingStartedCallback(captureRecordingStartedCallback);
    }

    public void setCaptureRecordingFrameReachedCallback(CaptureRecordingFrameReachedCallback captureRecordingFrameReachedCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_captureRecordingFrameReachedCallback = captureRecordingFrameReachedCallback;
        nativeSetCaptureRecordingFrameReachedCallback(captureRecordingFrameReachedCallback);
    }

    public void setCaptureRecordingDurationCallback(CaptureRecordingDurationCallback captureRecordingDurationCallback) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        this.m_captureRecordingDurationCallback = captureRecordingDurationCallback;
        nativeSetCaptureRecordingDurationCallback(captureRecordingDurationCallback);
    }

    public void setPlaybackCallback(PlaybackCallback playbackCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_playbackCallback = playbackCallback;
        nativeSetPlaybackCallback(playbackCallback);
    }

    public void setCompileCallback(CompileCallback compileCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_compileCallback = compileCallback;
        nativeSetCompileCallback(compileCallback);
    }

    public void setStreamingEngineCallback(StreamingEngineCallback streamingEngineCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_streamingEngineCallback = streamingEngineCallback;
        nativeSetStreamingEngineCallback(streamingEngineCallback);
    }

    public void setTimelineTimestampCallback(TimelineTimestampCallback timelineTimestampCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_timelineTimestampCallback = timelineTimestampCallback;
    }

    public void setCompileCallback2(CompileCallback2 compileCallback2) {
        NvsUtils.checkFunctionInMainThread();
        this.m_compileCallback2 = compileCallback2;
        nativeSetCompileCallback2(compileCallback2);
    }

    public void setCompileFloatProgressCallback(CompileFloatProgressCallback compileFloatProgressCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_compileFloatProgressCallback = compileFloatProgressCallback;
        nativeSetCompileFloatProgressCallback(compileFloatProgressCallback);
    }

    public void setPlaybackCallback2(PlaybackCallback2 playbackCallback2) {
        NvsUtils.checkFunctionInMainThread();
        this.m_playbackCallback2 = playbackCallback2;
        nativeSetPlaybackCallback2(playbackCallback2);
    }

    public void setPlaybackDelayCallback(PlaybackDelayCallback playbackDelayCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_playbackDelayCallback = playbackDelayCallback;
        nativeSetPlaybackDelayCallback(playbackDelayCallback);
    }

    public void setPlaybackExceptionCallback(PlaybackExceptionCallback playbackExceptionCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_playbackExceptionCallback = playbackExceptionCallback;
        nativeSetPlaybackExceptionCallback(playbackExceptionCallback);
    }

    public void setSeekingCallback(SeekingCallback seekingCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_seekingCallback = seekingCallback;
        nativeSetSeekingCallback(seekingCallback);
    }

    public void setHardwareErrorCallback(HardwareErrorCallback hardwareErrorCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_hardwareErrorCallback = hardwareErrorCallback;
        nativeSetHardwareErrorCallback(hardwareErrorCallback);
    }

    public NvsAVFileInfo getAVFileInfo(String str) {
        return nativeGetAVFileInfo(str, 0);
    }

    public NvsAVFileInfo getAVFileInfo(String str, int i) {
        return nativeGetAVFileInfo(str, i);
    }

    public static NvsAVFileInfo getAVInfoFromFile(String str, int i) {
        return nativeGetAVInfoFromFile(str, i);
    }

    public int detectVideoFileKeyframeInterval(String str) {
        return nativeDetectVideoFileKeyframeInterval(str);
    }

    public boolean canDecodeVideoStreamBySoftware(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeCanDecodeVideoStreamBySoftware(str);
    }

    public static boolean functionalityAuthorised(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeFunctionalityAuthorised(str);
    }

    public boolean setDefaultThemeEndingLogoImageFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSetDefaultThemeEndingLogoImageFilePath(str);
    }

    public String getDefaultThemeEndingLogoImageFilePath() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetDefaultThemeEndingLogoImageFilePath();
    }

    public String registerFontByFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeRegisterFontByFilePath(str);
    }

    public List<NvsFontInfo> getFontInfoByFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFontInfoByFilePath(str);
    }

    public boolean setThemeEndingEnabled(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSetThemeEndingEnabled(z);
    }

    public NvsTimeline createTimeline(NvsVideoResolution nvsVideoResolution, NvsRational nvsRational, NvsAudioResolution nvsAudioResolution) {
        NvsUtils.checkFunctionInMainThread();
        return nativeCreateTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
    }

    public boolean removeTimeline(NvsTimeline nvsTimeline) {
        NvsUtils.checkFunctionInMainThread();
        return nativeRemoveTimeline(nvsTimeline);
    }

    public int getStreamingEngineState() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStreamingEngineState();
    }

    public long getTimelineCurrentPosition(NvsTimeline nvsTimeline) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetTimelineCurrentPosition(nvsTimeline);
    }

    public boolean compileTimeline(NvsTimeline nvsTimeline, long j, long j2, String str, int i, int i2, int i3) {
        NvsUtils.checkFunctionInMainThread();
        return nativeCompileTimeline(nvsTimeline, j, j2, str, i, i2, this.m_compileVideoBitrateMultiplier, this.m_compileConfigurations, i3);
    }

    public boolean pauseCompiling() {
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumeCompiling(true);
    }

    public boolean resumeCompiling() {
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumeCompiling(false);
    }

    public boolean isCompilingPaused() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsCompilingPaused();
    }

    public void setCustomCompileVideoHeight(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetCustomCompileVideoHeight(i);
    }

    public int getCustomCompileVideoHeight() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCustomCompileVideoHeight();
    }

    public boolean connectTimelineWithLiveWindow(NvsTimeline nvsTimeline, NvsLiveWindow nvsLiveWindow) {
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectTimelineWithLiveWindow(nvsTimeline, nvsLiveWindow);
    }

    public boolean connectTimelineWithLiveWindowExt(NvsTimeline nvsTimeline, NvsLiveWindowExt nvsLiveWindowExt) {
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectTimelineWithLiveWindow(nvsTimeline, nvsLiveWindowExt);
    }

    public boolean connectTimelineWithSurfaceTexture(NvsTimeline nvsTimeline, SurfaceTexture surfaceTexture) {
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectTimelineWithSurfaceTexture(nvsTimeline, surfaceTexture, new NvsRational(1, 1));
    }

    public boolean connectTimelineWithSurfaceTexture(NvsTimeline nvsTimeline, SurfaceTexture surfaceTexture, NvsRational nvsRational) {
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectTimelineWithSurfaceTexture(nvsTimeline, surfaceTexture, nvsRational);
    }

    public boolean seekTimeline(NvsTimeline nvsTimeline, long j, int i, int i2) {
        NvsUtils.checkFunctionInMainThread();
        if (nvsTimeline == null) {
            return false;
        }
        if (j >= 0 && j < nvsTimeline.getDuration()) {
            return nativeSeekTimeline(nvsTimeline, j, i, i2);
        }
        TimelineTimestampCallback timelineTimestampCallback = this.m_timelineTimestampCallback;
        if (timelineTimestampCallback != null) {
            timelineTimestampCallback.onTimestampOutOfRange(nvsTimeline);
        }
        return false;
    }

    public boolean seekTimeline(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (nvsTimeline == null) {
            return false;
        }
        if (j >= 0 && j < nvsTimeline.getDuration()) {
            return nativeSeekTimelineWithProxyScale(nvsTimeline, j, nvsRational, i);
        }
        TimelineTimestampCallback timelineTimestampCallback = this.m_timelineTimestampCallback;
        if (timelineTimestampCallback != null) {
            timelineTimestampCallback.onTimestampOutOfRange(nvsTimeline);
        }
        return false;
    }

    public Bitmap grabImageFromTimeline(NvsTimeline nvsTimeline, long j, NvsRational nvsRational) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGrabImageFromTimeline(nvsTimeline, j, nvsRational, 0);
    }

    public Bitmap grabImageFromTimeline(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGrabImageFromTimeline(nvsTimeline, j, nvsRational, i);
    }

    public void setImageGrabberCallback(ImageGrabberCallback imageGrabberCallback) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageGrabberCallback(imageGrabberCallback);
    }

    public boolean grabImageFromTimelineAsync(NvsTimeline nvsTimeline, long j, NvsRational nvsRational, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGrabImageFromTimelineAsyncMode(nvsTimeline, j, nvsRational, i);
    }

    public boolean playbackTimeline(NvsTimeline nvsTimeline, long j, long j2, int i, boolean z, int i2) {
        NvsUtils.checkFunctionInMainThread();
        if (nvsTimeline == null) {
            return false;
        }
        if (j >= 0 && j < nvsTimeline.getDuration() && (j2 < 0 || j < j2)) {
            return nativePlaybackTimeline(nvsTimeline, j, j2, i, z, i2);
        }
        TimelineTimestampCallback timelineTimestampCallback = this.m_timelineTimestampCallback;
        if (timelineTimestampCallback != null) {
            timelineTimestampCallback.onTimestampOutOfRange(nvsTimeline);
        }
        return false;
    }

    public boolean playbackTimeline(NvsTimeline nvsTimeline, long j, long j2, NvsRational nvsRational, boolean z, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (nvsTimeline == null) {
            return false;
        }
        if (j >= 0 && j < nvsTimeline.getDuration() && (j2 < 0 || j < j2)) {
            return nativePlaybackTimelineWithProxyScale(nvsTimeline, j, j2, nvsRational, z, i);
        }
        TimelineTimestampCallback timelineTimestampCallback = this.m_timelineTimestampCallback;
        if (timelineTimestampCallback != null) {
            timelineTimestampCallback.onTimestampOutOfRange(nvsTimeline);
        }
        return false;
    }

    public boolean pausePlayback() {
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumePlayback(true);
    }

    public boolean resumePlayback() {
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumePlayback(false);
    }

    public boolean isPlaybackPaused() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsPlaybackPaused();
    }

    public void stop() {
        NvsUtils.checkFunctionInMainThread();
        nativeStop(0);
    }

    public void stop(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeStop(i);
    }

    public void clearCachedResources(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeClearCachedResources(z);
    }

    public void clearCachedResources(boolean z, int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeClearCachedResourcesFlags(z, i);
    }

    public int getCaptureDeviceCount() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 0;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptureDeviceCount();
    }

    public boolean isCaptureDeviceBackFacing(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeIsCaptureDeviceBackFacing(i);
    }

    public CaptureDeviceCapability getCaptureDeviceCapability(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptureDeviceCapability(i);
    }

    public boolean checkDontSetCameraParamOnRecordingWithSystemRecorder() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeCheckDontSetCameraParamOnRecordingWithSystemRecorder();
    }

    public boolean connectCapturePreviewWithLiveWindow(NvsLiveWindow nvsLiveWindow) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectCapturePreviewWithLiveWindow(nvsLiveWindow);
    }

    public boolean connectCapturePreviewWithLiveWindowExt(NvsLiveWindowExt nvsLiveWindowExt) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectCapturePreviewWithLiveWindow(nvsLiveWindowExt);
    }

    public boolean connectCapturePreviewWithSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeConnectCapturePreviewWithSurfaceTexture(surfaceTexture);
    }

    public void setUserWatermarkForCapture(String str, int i, int i2, float f, int i3, int i4, int i5) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetUserWatermarkForCapture(str, i, i2, f, i3, i4, i5);
    }

    public void removeUserWatermarkForCapture() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetUserWatermarkForCapture(null, 0, 0, 0.0f, 0, 0, 0);
    }

    public boolean startCapturePreview(int i, int i2, int i3, NvsRational nvsRational) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (!checkCameraPermission()) {
            return false;
        }
        return nativeStartCapturePreview(i, i2, i3, nvsRational);
    }

    public boolean startCapturePreviewWithSpecialSize(int i, int i2, int i3, NvsSize nvsSize) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (!checkCameraPermission()) {
            return false;
        }
        return nativeStartCapturePreviewWithSpecialSize(i, i2, i3, nvsSize);
    }

    public boolean startBufferCapturePreview(int i, int i2, NvsRational nvsRational, int i3, boolean z) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeStartBufferCapturePreview(i, i2, nvsRational, i3, z);
    }

    public boolean startDualBufferCapturePreview(SurfaceTexture surfaceTexture, int i, int i2, int i3, int i4, int i5, boolean z, int i6, NvsRational nvsRational, int i7) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (surfaceTexture == null) {
            return false;
        }
        NvAndroidVirtualCameraSurfaceTexture nvAndroidVirtualCameraSurfaceTexture = new NvAndroidVirtualCameraSurfaceTexture(surfaceTexture);
        if (nativeStartDualBufferCapturePreview(i, i2, i3, i4, i5, z, i6, nvsRational, i7, nvAndroidVirtualCameraSurfaceTexture)) {
            return true;
        }
        nvAndroidVirtualCameraSurfaceTexture.release();
        return false;
    }

    public boolean sendBufferToCapturePreview(byte[] bArr, long j) {
        if (!this.m_isAuxiliaryContext) {
            return sendBufferToCapturePreview(bArr, j, 0);
        }
        Log.e(TAG, "The auxiliary streaming context can not support capture!!");
        return false;
    }

    public boolean sendBufferToCapturePreview(byte[] bArr, long j, int i) {
        if (!this.m_isAuxiliaryContext) {
            return nativeSendBufferToCapturePreview(bArr, j, i);
        }
        Log.e(TAG, "The auxiliary streaming context can not support capture!!");
        return false;
    }

    public NvsSize getCapturePreviewVideoSize(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return new NvsSize(0, 0);
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCapturePreviewVideoSize(i);
    }

    public NvsColor sampleColorFromCapturedVideoFrame(RectF rectF) {
        NvsUtils.checkFunctionInMainThread();
        return nativeSampleColorFromCapturedVideoFrame(rectF);
    }

    public void startAutoFocus(RectF rectF) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeStartAutoFocus(rectF);
    }

    public void cancelAutoFocus() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeCancelAutoFocus();
    }

    public void StartContinuousFocus() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeStartContinuousFocus();
    }

    public void setAutoExposureRect(RectF rectF) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetAutoExposureRect(rectF);
    }

    public void setZoom(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetZoom(i);
    }

    public int getZoom() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 1;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetZoom();
    }

    public void toggleFlash(boolean z) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        if (z) {
            nativeToggleFlashMode(8);
        } else {
            nativeToggleFlashMode(1);
        }
    }

    public boolean isFlashOn() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (nativeGetFlashMode() == 8) {
            return true;
        }
        return false;
    }

    public int getVideoStabilization() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 0;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoStabilization();
    }

    public void setVideoStabilization(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetVideoStabilization(i);
    }

    public void setExposureCompensation(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeSetExposureCompensation(i);
    }

    public int getExposureCompensation() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 0;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetExposureCompensation();
    }

    public NvsCaptureVideoFx appendBuiltinCaptureVideoFx(String str) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return insertBuiltinCaptureVideoFx(str, getCaptureVideoFxCount());
    }

    public NvsCaptureVideoFx insertBuiltinCaptureVideoFx(String str, int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertBuiltinCaptureVideoFx(str, i);
    }

    public NvsCaptureVideoFx appendPackagedCaptureVideoFx(String str) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return insertPackagedCaptureVideoFx(str, getCaptureVideoFxCount());
    }

    public NvsCaptureVideoFx insertPackagedCaptureVideoFx(String str, int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertPackagedCaptureVideoFx(str, i);
    }

    public NvsCaptureVideoFx appendCustomCaptureVideoFx(NvsCustomVideoFx.Renderer renderer) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertCustomCaptureVideoFx(renderer, getCaptureVideoFxCount());
    }

    public NvsCaptureVideoFx insertCustomCaptureVideoFx(NvsCustomVideoFx.Renderer renderer, int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertCustomCaptureVideoFx(renderer, i);
    }

    public NvsCaptureVideoFx appendBeautyCaptureVideoFx() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return insertBeautyCaptureVideoFx(getCaptureVideoFxCount());
    }

    public NvsCaptureVideoFx insertBeautyCaptureVideoFx(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return insertBuiltinCaptureVideoFx(getBeautyVideoFxName(), i);
    }

    public boolean removeCaptureVideoFx(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeRemoveCaptureVideoFx(i);
    }

    public void removeAllCaptureVideoFx() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeRemoveAllCaptureVideoFx();
    }

    public int getCaptureVideoFxCount() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 0;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptureVideoFxCount();
    }

    public NvsCaptureVideoFx getCaptureVideoFxByIndex(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCaptureVideoFxByIndex(i);
    }

    public boolean applyCaptureScene(String str) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeApplyCaptureScene(str);
    }

    public String getCurrentCaptureSceneId() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return null;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetCurrentCaptureSceneId();
    }

    public void removeCurrentCaptureScene() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeRemoveCurrentCaptureScene();
    }

    public void setCompileConfigurations(Hashtable<String, Object> hashtable) {
        NvsUtils.checkFunctionInMainThread();
        this.m_compileConfigurations = hashtable;
    }

    public Hashtable<String, Object> getCompileConfigurations() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_compileConfigurations;
    }

    public void setCompileVideoBitrateMultiplier(float f) {
        NvsUtils.checkFunctionInMainThread();
        if (f > 0.0f) {
            this.m_compileVideoBitrateMultiplier = f;
        }
    }

    public float getCompileVideoBitrateMultiplier() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_compileVideoBitrateMultiplier;
    }

    public void setRecordVideoBitrateMultiplier(float f) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        if (f > 0.0f) {
            this.m_recordVideoBitrateMultiplier = f;
        }
    }

    public float getRecordVideoBitrateMultiplier() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 1.0f;
        }
        NvsUtils.checkFunctionInMainThread();
        return this.m_recordVideoBitrateMultiplier;
    }

    public boolean startRecording(String str) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (!checkCameraPermission()) {
            return false;
        }
        return nativeStartRecording(str, this.m_recordVideoBitrateMultiplier, 0, 0, 0);
    }

    public boolean startRecording(String str, int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (!checkCameraPermission()) {
            return false;
        }
        return nativeStartRecording(str, this.m_recordVideoBitrateMultiplier, 0, 0, i);
    }

    public boolean startRecording(String str, int i, Hashtable<String, Object> hashtable) {
        int i2;
        int i3;
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        if (!checkCameraPermission()) {
            return false;
        }
        if (hashtable != null) {
            Number number = (Number) hashtable.get("bitrate");
            int intValue = number != null ? number.intValue() : 0;
            Number number2 = (Number) hashtable.get("gopsize");
            if (number2 != null) {
                i3 = intValue;
                i2 = number2.intValue();
                return nativeStartRecording(str, this.m_recordVideoBitrateMultiplier, i3, i2, i);
            }
            i3 = intValue;
        } else {
            i3 = 0;
        }
        i2 = 0;
        return nativeStartRecording(str, this.m_recordVideoBitrateMultiplier, i3, i2, i);
    }

    public void stopRecording() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeStopRecording(true);
    }

    public void stopRecording(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeStopRecording(z);
    }

    public boolean pauseRecording() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumeRecording(true);
    }

    public boolean resumeRecording() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativePauseResumeRecording(false);
    }

    public boolean isRecordingPaused() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeIsRecordingPaused();
    }

    public boolean takePicture(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return false;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeTakePicture(i);
    }

    public void toggleFlashMode(int i) {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return;
        }
        NvsUtils.checkFunctionInMainThread();
        nativeToggleFlashMode(i);
    }

    public int getFlashMode() {
        if (this.m_isAuxiliaryContext) {
            Log.e(TAG, "The auxiliary streaming context can not support capture!!");
            return 1;
        }
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFlashMode();
    }

    public List<String> getAllBuiltinVideoFxNames() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAllBuiltinVideoFxNames();
    }

    public List<String> getAllBuiltinAudioFxNames() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAllBuiltinAudioFxNames();
    }

    public List<String> getAllBuiltinVideoTransitionNames() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAllBuiltinVideoTransitionNames();
    }

    public List<String> getAllBuiltinCaptureVideoFxNames() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAllBuiltinCaptureVideoFxNames();
    }

    public String getBeautyVideoFxName() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetBeautyVideoFxName();
    }

    public String getDefaultVideoTransitionName() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetDefaultVideoTransitionName();
    }

    public void setDefaultAudioTransitionName(String str) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetDefaultAudioTransitionName(str);
    }

    public NvsFxDescription getVideoFxDescription(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoFxDescription(str);
    }

    public NvsFxDescription getAudioFxDescription(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetAudioFxDescription(str);
    }

    public NvsVideoFrameRetriever createVideoFrameRetriever(String str) {
        return new NvsVideoFrameRetriever(str);
    }

    public NvsVideoKeyFrameRetriever createVideoKeyFrameRetriever(String str, int i, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        return new NvsVideoKeyFrameRetriever(str, i, z);
    }

    public void setDefaultCaptionFade(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetDefaultCaptionFade(z);
    }

    public boolean isDefaultCaptionFade() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsDefaultCaptionFade();
    }

    @Deprecated
    public void setImageReaderCount(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageReaderCount(i);
    }

    public static void setMaxImageReaderCount(int i) {
        NvsUtils.checkFunctionInMainThread();
        m_maxImageReaderCount = i;
    }

    public void setAudioOutputDeviceVolume(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetAudioOutputDeviceVolume(f);
    }

    public NvsStreamingContext createAuxiliaryStreamingContext(int i) {
        NvsUtils.checkFunctionInMainThread();
        Context context = m_context;
        if (context == null) {
            return null;
        }
        NvsStreamingContext nvsStreamingContext = new NvsStreamingContext(context, false);
        if (!nativeCreateAuxiliaryStreamingContext(nvsStreamingContext, i)) {
            return null;
        }
        nvsStreamingContext.m_isAuxiliaryContext = true;
        return nvsStreamingContext;
    }

    public void destoryAuxiliaryStreamingContext(NvsStreamingContext nvsStreamingContext) {
        if (nvsStreamingContext != null) {
            nativeDestoryAuxiliaryStreamingContext(nvsStreamingContext);
        }
    }

    public static void setMaxReaderCount(int i) {
        NvsUtils.checkFunctionInMainThread();
        m_maxReaderCount = i;
    }

    public static void setMaxAudioReaderCount(int i) {
        NvsUtils.checkFunctionInMainThread();
        m_maxAudioReaderCount = i;
    }

    public static void setIconSize(int i) {
        NvsUtils.checkFunctionInMainThread();
        m_iconSize = i;
    }

    public static void setMaxIconReader(int i) {
        NvsUtils.checkFunctionInMainThread();
        m_maxIconReader = i;
    }

    public void setMediaCodecVideoDecodingOperatingRate(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMediaCodecVideoDecodingOperatingRate(str, i);
    }

    public void setMediaCodecIconReaderEnabled(String str, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMediaCodecIconReaderEnabled(str, z);
    }

    public int getAudioStreamCountInMediaFile(String str) {
        return nativeGetAudioStreamCountInMediaFile(str);
    }

    public void setCaptureFps(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetCaptureFps(i);
    }

    public static void setMaxCafCacheMemorySize(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetMaxCafCacheMemorySize(i);
    }

    /* access modifiers changed from: protected */
    public void setInternalObject(long j) {
        this.m_internalObject = j;
    }

    /* access modifiers changed from: protected */
    public long getInternalObject() {
        return this.m_internalObject;
    }

    /* access modifiers changed from: protected */
    public boolean isAuxiliaryContext() {
        return this.m_isAuxiliaryContext;
    }

    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT < 23 || m_context.checkSelfPermission("android.permission.CAMERA") == 0) {
            return true;
        }
        Log.e(TAG, "CAMERA permission has not been granted!");
        return false;
    }

    private boolean checkInternetPermission() {
        if (Build.VERSION.SDK_INT < 23 || m_context.checkSelfPermission("android.permission.INTERNET") == 0) {
            return true;
        }
        Log.e(TAG, "INTERNET permission has not been granted!");
        return false;
    }

    private static void loadNativeLibrary(String str) throws SecurityException, UnsatisfiedLinkError, NullPointerException {
        System.loadLibrary(str);
    }

    private static boolean tryLoadNativeLibrary(String str) throws SecurityException, UnsatisfiedLinkError, NullPointerException {
        try {
            System.loadLibrary(str);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static void tryLoadFaceDetectionLibrary() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (!m_faceDetectionLibLoaded) {
            boolean z5 = false;
            try {
                Class.forName("com.meicam.sdk.NvsBEFFaceEffectDetector");
                z = true;
            } catch (Exception unused) {
                z = false;
            }
            if (!z || tryLoadNativeLibrary("effect")) {
                try {
                    Class.forName("com.meicam.sdk.NvsMGFaceEffectDetector");
                    z2 = true;
                } catch (Exception unused2) {
                    z2 = false;
                }
                if (!z2 || (tryLoadNativeLibrary("megface-new") && tryLoadNativeLibrary("MegviiFacepp"))) {
                    try {
                        Class.forName("com.meicam.sdk.NvsSTFaceEffectDetector");
                        z3 = true;
                    } catch (Exception unused3) {
                        z3 = false;
                    }
                    if (!z3 || tryLoadNativeLibrary("st_mobile")) {
                        try {
                            Class.forName("com.meicam.sdk.NvsFaceEffectV1Detector");
                            z4 = true;
                        } catch (Exception unused4) {
                            z4 = false;
                        }
                        try {
                            Class.forName("com.meicam.sdk.NvsFUFaceEffectDetector");
                            z4 = true;
                        } catch (Exception unused5) {
                        }
                        if (!z4 || tryLoadNativeLibrary("nama") || tryLoadNativeLibrary("fuai")) {
                            try {
                                Class.forName("com.meicam.sdk.NvsFaceEffect2Init");
                                z5 = true;
                            } catch (Exception unused6) {
                            }
                            if (!z5 || tryLoadNativeLibrary("st_mobile")) {
                                m_faceDetectionLibLoaded = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|4|5|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x000a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initNativeLibraryDirPath(android.content.Context r1, java.lang.String r2) {
        /*
            boolean r0 = hasDexClassLoader()
            if (r0 == 0) goto L_0x000e
            createNewNativeDirAboveEqualApiLevel14(r1, r2)     // Catch:{ Exception -> 0x000a }
            goto L_0x0011
        L_0x000a:
            createNewNativeDirAboveEqualApiLevel21(r1, r2)     // Catch:{ Exception -> 0x0011 }
            goto L_0x0011
        L_0x000e:
            createNewNativeDirBelowApiLevel14(r1, r2)     // Catch:{ Exception -> 0x0011 }
        L_0x0011:
            return
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meicam.sdk.NvsStreamingContext.initNativeLibraryDirPath(android.content.Context, java.lang.String):void");
    }

    private static void createNewNativeDirAboveEqualApiLevel14(Context context, String str) throws Exception {
        Object pathList = getPathList((PathClassLoader) context.getClassLoader());
        Field declaredField = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
        declaredField.setAccessible(true);
        File[] fileArr = (File[]) declaredField.get(pathList);
        Object newInstance = Array.newInstance(File.class, fileArr.length + 1);
        Array.set(newInstance, 0, new File(str));
        for (int i = 1; i < fileArr.length + 1; i++) {
            Array.set(newInstance, i, fileArr[i - 1]);
        }
        declaredField.set(pathList, newInstance);
    }

    private static void createNewNativeDirBelowApiLevel14(Context context, String str) throws NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Field declaredField = pathClassLoader.getClass().getDeclaredField("mLibPaths");
        declaredField.setAccessible(true);
        String[] strArr = (String[]) declaredField.get(pathClassLoader);
        Object newInstance = Array.newInstance(String.class, strArr.length + 1);
        Array.set(newInstance, 0, str);
        for (int i = 1; i < strArr.length + 1; i++) {
            Array.set(newInstance, i, strArr[i - 1]);
        }
        declaredField.set(pathClassLoader, newInstance);
    }

    private static void createNewNativeDirAboveEqualApiLevel21(Context context, String str) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        if (Build.VERSION.SDK_INT >= 21) {
            Object pathList = getPathList((PathClassLoader) context.getClassLoader());
            Field declaredField = pathList.getClass().getDeclaredField("systemNativeLibraryDirectories");
            declaredField.setAccessible(true);
            List list = (List) declaredField.get(pathList);
            list.add(new File(str));
            declaredField.set(pathList, list);
            Field declaredField2 = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            declaredField2.setAccessible(true);
            ArrayList arrayList = (ArrayList) declaredField2.get(pathList);
            arrayList.add(new File(str));
            declaredField2.set(pathList, arrayList);
            Class<?> cls = Class.forName("dalvik.system.DexPathList$Element");
            Constructor<?> constructor = cls.getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
            Field declaredField3 = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            Field field = declaredField3;
            field.setAccessible(true);
            Object[] objArr = (Object[]) field.get(pathList);
            Object newInstance = Array.newInstance(cls, objArr.length + 1);
            if (constructor != null) {
                try {
                    Array.set(newInstance, 0, constructor.newInstance(new File(str), true, null, null));
                    for (int i = 1; i < objArr.length + 1; i++) {
                        Array.set(newInstance, i, objArr[i - 1]);
                    }
                    declaredField3.set(pathList, newInstance);
                } catch (IllegalArgumentException unused) {
                    Method declaredMethod = pathList.getClass().getDeclaredMethod("makePathElements", List.class);
                    declaredMethod.setAccessible(true);
                    Object invoke = declaredMethod.invoke(null, arrayList);
                    Field declaredField4 = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
                    declaredField4.setAccessible(true);
                    declaredField4.set(pathList, invoke);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getField(Object obj, Class cls, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    private static boolean hasDexClassLoader() {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
