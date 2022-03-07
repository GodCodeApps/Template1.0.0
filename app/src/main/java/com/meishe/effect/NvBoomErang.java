package com.meishe.effect;

import android.util.Log;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.myvideo.audio.AudioRecorder;

public class NvBoomErang {
    public static NvsTimeline createTimeline(String str) {
        String str2;
        int i;
        if (!NvsEffectSdkContext.functionalityAuthorised("boomerang")) {
            str2 = "Functionality boomerang is not authorised!";
        } else if (str == null || str.isEmpty()) {
            str2 = "createTimeline: videoSourcePath is null!";
        } else {
            NvsStreamingContext instance = NvsStreamingContext.getInstance();
            if (instance == null) {
                str2 = "createTimeline: nvsStreamingContext is null!";
            } else {
                NvsAVFileInfo aVFileInfo = instance.getAVFileInfo(str);
                if (aVFileInfo == null) {
                    str2 = "createTimeline: fileInfo is null!";
                } else {
                    NvsSize videoStreamDimension = aVFileInfo.getVideoStreamDimension(0);
                    if (videoStreamDimension == null) {
                        str2 = "createTimeline: video size is null!";
                    } else {
                        NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
                        nvsVideoResolution.imageWidth = videoStreamDimension.width;
                        nvsVideoResolution.imageHeight = videoStreamDimension.height;
                        nvsVideoResolution.imagePAR = new NvsRational(1, 1);
                        NvsRational nvsRational = new NvsRational(20, 1);
                        NvsAudioResolution nvsAudioResolution = new NvsAudioResolution();
                        nvsAudioResolution.sampleRate = AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ;
                        nvsAudioResolution.channelCount = 2;
                        NvsTimeline createTimeline = instance.createTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
                        if (createTimeline == null) {
                            str2 = "createTimeline: failed to create timeline";
                        } else {
                            NvsVideoTrack appendVideoTrack = createTimeline.appendVideoTrack();
                            if (appendVideoTrack == null) {
                                str2 = "createTimeline: failed to appendVideoTrack";
                            } else {
                                if (aVFileInfo.getVideoStreamCount() > 0) {
                                    i = aVFileInfo.getVideoStreamRotation(0);
                                    if (i == 1) {
                                        i = 3;
                                    } else if (i == 2) {
                                        i = 2;
                                    } else if (i == 3) {
                                        i = 1;
                                    }
                                } else {
                                    i = 0;
                                }
                                for (int i2 = 0; i2 < 8; i2++) {
                                    NvsVideoClip appendClip = appendVideoTrack.appendClip(str);
                                    if (appendClip != null) {
                                        appendClip.setExtraVideoRotation(i);
                                        if (i2 % 2 != 0) {
                                            appendClip.setPlayInReverse(true);
                                        }
                                    }
                                }
                                for (int i3 = 0; i3 < appendVideoTrack.getClipCount(); i3++) {
                                    appendVideoTrack.setBuiltinTransition(i3, null);
                                    NvsVideoClip clipByIndex = appendVideoTrack.getClipByIndex(i3);
                                    if (clipByIndex != null) {
                                        clipByIndex.changeSpeed(2.2100000381469727d);
                                        if (i3 > 0) {
                                            clipByIndex.changeTrimInPoint(45000, true);
                                        }
                                    }
                                }
                                return createTimeline;
                            }
                        }
                    }
                }
            }
        }
        Log.e("NvBoomErang", str2);
        return null;
    }
}
