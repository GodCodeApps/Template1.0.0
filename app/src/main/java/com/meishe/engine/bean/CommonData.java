package com.meishe.engine.bean;

public class CommonData {
    public static final String ATTACHMENT_KEY_STICKER_COVER_PATH = "attachment_key_sticker_cover_path";
    public static final String ATTACHMENT_KEY_VIDEO_CLIP_AR_SCENE = "attachment_key_video_clip_ar_scene";
    public static final String ATTACHMENT_KEY_VIDEO_CLIP_MIRROR = "attachment_key_video_clip_mirror";
    public static final String BUILTIN = "builtin";
    public static final int CLICK_LONG = 500;
    public static final int CLICK_MOVE = 10;
    public static final int CLICK_TIME = 200;
    public static final String CLIP_AUDIO = "audio";
    public static final String CLIP_CAPTION = "caption";
    public static final String CLIP_COMPOUND_CAPTION = "compound_caption";
    public static final String CLIP_HOLDER = "holder";
    public static final String CLIP_IMAGE = "image";
    public static final String CLIP_STICKER = "sticker";
    public static final String CLIP_TIMELINE_FX = "timelineVideoFx";
    public static final String CLIP_VIDEO = "video";
    public static final long DEFAULT_LENGTH = 4000000;
    public static final long DEFAULT_TRIM_IN = 3600000000L;
    public static final int EFFECT_BUILTIN = 0;
    public static final int EFFECT_PACKAGE = 1;
    public static final int MAIN_TRACK_INDEX = 0;
    public static final int MAX_AUDIO_COUNT = 16;
    public static final long MIN_SHOW_LENGTH_DURATION = 800000;
    public static final long ONE_FRAME = 4000;
    public static final String PACKAGE = "package";
    public static final int STORYBOARD_BACKGROUND_TYPE_BLUR = 2;
    public static final int STORYBOARD_BACKGROUND_TYPE_COLOR = 0;
    public static final int STORYBOARD_BACKGROUND_TYPE_IMAGE = 1;
    public static final long TIMEBASE = 1000000;
    public static final int TIMELINE_RESOLUTION_VALUE = 720;
    public static final String TRACK_AUDIO = "audioTrack";
    public static final String TRACK_STICKER_CAPTION = "stickerCaptionTrack";
    public static final String TRACK_TIMELINE_FX = "timelineVideoFxTrack";
    public static final String TRACK_VIDEO = "videoTrack";
    public static final String TRANSITION = "transition";
    public static final String TYPE_BUILD_IN = "builtin";
    public static final int TYPE_CAPTION = 1;
    public static final int TYPE_COMPOUND_CAPTION = 2;
    public static final String TYPE_PACKAGE = "package";
    public static final int TYPE_STICKER = 3;
    public static final String VIDEO_FX_AR_SCENE = "AR Scene";
    public static final String VIDEO_FX_BEAUTY_EFFECT = "Beauty Effect";
    public static final String VIDEO_FX_BEAUTY_REDDENING = "Beauty Reddening";
    public static final String VIDEO_FX_BEAUTY_SHAPE = "Beauty Shape";
    public static final String VIDEO_FX_BEAUTY_STRENGTH = "Beauty Strength";
    public static final String VIDEO_FX_BEAUTY_WHITENING = "Beauty Whitening";
    public static final String VIDEO_FX_SINGLE_BUFFER_MODE = "Single Buffer Mode";

    public enum AspectRatio {
        ASPECT_16V9(1, 1.7777778f),
        ASPECT_1V1(2, 1.0f),
        ASPECT_9V16(4, 0.5625f),
        ASPECT_4V3(8, 1.3333334f),
        ASPECT_3V4(16, 0.75f);
        
        private int aspect;
        private float ratio;

        private AspectRatio(int i, float f) {
            this.aspect = i;
            this.ratio = f;
        }

        public float getRatio() {
            return this.ratio;
        }

        public static int getAspect(float f) {
            AspectRatio[] values = values();
            for (AspectRatio aspectRatio : values) {
                if (Math.abs(aspectRatio.ratio - f) < 0.1f) {
                    return aspectRatio.aspect;
                }
            }
            return 0;
        }
    }
}
