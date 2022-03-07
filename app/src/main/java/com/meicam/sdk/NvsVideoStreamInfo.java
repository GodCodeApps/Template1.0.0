package com.meicam.sdk;

public class NvsVideoStreamInfo {
    public static final int VIDEO_CODEC_TYPE_H264 = 1;
    public static final int VIDEO_CODEC_TYPE_H265 = 2;
    public static final int VIDEO_CODEC_TYPE_MJPEG = 9;
    public static final int VIDEO_CODEC_TYPE_MPEG1 = 7;
    public static final int VIDEO_CODEC_TYPE_MPEG2 = 8;
    public static final int VIDEO_CODEC_TYPE_MPEG4 = 3;
    public static final int VIDEO_CODEC_TYPE_UNKNOWN = 0;
    public static final int VIDEO_CODEC_TYPE_VP8 = 4;
    public static final int VIDEO_CODEC_TYPE_VP9 = 5;
    public static final int VIDEO_CODEC_TYPE_WMV = 6;
    public static final int VIDEO_ROTATION_0 = 0;
    public static final int VIDEO_ROTATION_180 = 2;
    public static final int VIDEO_ROTATION_270 = 3;
    public static final int VIDEO_ROTATION_90 = 1;
    public int componentBitCount;
    public int displayRotation;
    public long duration;
    public NvsRational frameRate;
    public int imageHeight;
    public int imageWidth;
    public NvsRational pixelAspectRatio;
    public int videoCodecType;
}
