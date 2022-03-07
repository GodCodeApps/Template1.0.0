package com.meishe.effect;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import com.meicam.effect.sdk.NvsEffect;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.myvideo.audio.AudioRecorder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NvLyrics {
    public static final String POSITION_COORDINATE = "position";
    public static final String TEXTURE_COORDINATE = "inputTextureCoordinate";
    public static final String TEXTURE_UNIFORM = "inputImageTexture";
    private EffectRenderCore a;
    private NvsEffectSdkContext b;
    private Context c;
    private String d;
    private String e;
    private String f;
    private Boolean g = Boolean.TRUE;
    private Boolean h = Boolean.TRUE;
    private Boolean i = Boolean.TRUE;
    private ArrayList j = new ArrayList();
    private String k;
    private MediaPlayer l;
    private long m;
    private int n;
    private int o;
    private int p = -1;
    private NvsEffect q;
    private Boolean r = Boolean.FALSE;
    private long s;
    private long t = 0;

    public NvLyrics(Context context) {
        this.c = context;
        this.p = -1;
        this.b = NvsEffectSdkContext.getInstance();
        if (this.a == null) {
            this.a = new EffectRenderCore(this.b);
        }
        if (this.a == null) {
            Log.e("NvLyrics", "Failed to create effect render core!");
        }
        if (this.l == null) {
            this.l = new MediaPlayer();
        }
        if (this.l == null) {
            Log.e("NvLyrics", "Failed to create media player!");
        }
    }

    private void a(String str, String str2) {
        if (str != null && !str.equals("") && str2 != null && !str2.equals("")) {
            this.k = str;
            List a2 = (!this.i.booleanValue() || !judgeIspostFixFile(str2, "lrc").booleanValue()) ? Constants.a(str2) : Constants.a(this.c, str2.substring(8));
            if (a2 != null) {
                for (int i2 = 0; i2 < a2.size(); i2++) {
                    Set<Map.Entry> entrySet = ((Map) a2.get(i2)).entrySet();
                    for (Map.Entry entry : entrySet) {
                        new HashMap();
                        lrcMusicInfo lrcmusicinfo = new lrcMusicInfo();
                        lrcmusicinfo.setMusicText(((String) entry.getValue()).toString());
                        lrcmusicinfo.setMusicTime(Integer.valueOf(((Long) entry.getKey()).toString()).intValue());
                        this.j.add(lrcmusicinfo);
                    }
                }
            }
            String str3 = this.k;
            if (str3 != null && !str3.equals("")) {
                MediaPlayer mediaPlayer = this.l;
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    this.n = 0;
                    this.o = 0;
                }
                try {
                    if (this.l != null) {
                        if (!this.h.booleanValue()) {
                            this.l.setDataSource(this.k);
                        } else if (this.k.length() > 8) {
                            AssetFileDescriptor openFd = this.c.getAssets().openFd(this.k.substring(8));
                            this.l.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                        }
                        this.l.prepareAsync();
                        this.l.setLooping(true);
                        this.l.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            /* class com.meishe.effect.NvLyrics.AnonymousClass1 */

                            public void onPrepared(MediaPlayer mediaPlayer) {
                                if (!NvLyrics.this.l.isPlaying()) {
                                    NvLyrics.this.l.start();
                                }
                            }
                        });
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static Boolean judgeIspostFixFile(String str, String str2) {
        return str.substring(str.lastIndexOf(".") + 1, str.length()).equals(str2) ? Boolean.TRUE : Boolean.FALSE;
    }

    public NvsTimeline createTimeline(ArrayList arrayList, String str) {
        String str2;
        NvsStreamingContext instance = NvsStreamingContext.getInstance();
        if (instance == null) {
            str2 = "StreamingSdkContext is null!";
        } else if (arrayList == null || arrayList.size() == 0) {
            str2 = "invaild video list!";
        } else {
            int i2 = 0;
            NvsSize videoStreamDimension = instance.getAVFileInfo((String) arrayList.get(0)).getVideoStreamDimension(0);
            NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
            nvsVideoResolution.imageWidth = videoStreamDimension.width;
            nvsVideoResolution.imageHeight = videoStreamDimension.height;
            nvsVideoResolution.imagePAR = new NvsRational(1, 1);
            NvsRational nvsRational = new NvsRational(25, 1);
            NvsAudioResolution nvsAudioResolution = new NvsAudioResolution();
            nvsAudioResolution.sampleRate = AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ;
            nvsAudioResolution.channelCount = 2;
            NvsTimeline createTimeline = instance.createTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
            if (createTimeline == null) {
                str2 = "Failed to create timeline!";
            } else {
                NvsVideoTrack appendVideoTrack = createTimeline.appendVideoTrack();
                if (appendVideoTrack == null) {
                    str2 = "append video track failed!";
                } else {
                    while (true) {
                        if (i2 >= arrayList.size()) {
                            NvsAudioTrack appendAudioTrack = createTimeline.appendAudioTrack();
                            if (appendAudioTrack == null) {
                                str2 = "append audio track failed!";
                            } else if (appendAudioTrack.appendClip(str) != null) {
                                return createTimeline;
                            } else {
                                str2 = "append audio clip failed!";
                            }
                        } else if (appendVideoTrack.appendClip((String) arrayList.get(i2)) == null) {
                            str2 = "append video clip failed!";
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
        }
        Log.e("NvLyrics", str2);
        return null;
    }

    public void pause() {
        this.m = System.currentTimeMillis();
        this.r = Boolean.TRUE;
        MediaPlayer mediaPlayer = this.l;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void releaseResources() {
        MediaPlayer mediaPlayer = this.l;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.l = null;
        }
        EffectRenderCore effectRenderCore = this.a;
        if (effectRenderCore != null) {
            effectRenderCore.destoryGLResource();
        }
        this.b = null;
        this.a = null;
        NvsEffectSdkContext.close();
        this.c = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x0192  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int render(int r15, boolean r16, int r17, int r18, int r19, boolean r20) {
        /*
        // Method dump skipped, instructions count: 486
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.effect.NvLyrics.render(int, boolean, int, int, int, boolean):int");
        return r15;
    }

    public void resume() {
        EffectRenderCore effectRenderCore = this.a;
        if (effectRenderCore != null) {
            this.t += effectRenderCore.getExcutedTime();
            this.a.resetStartTime();
        }
        this.m = System.currentTimeMillis();
        this.r = Boolean.FALSE;
        MediaPlayer mediaPlayer = this.l;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public boolean start(String str, String str2, String str3) {
        String str4;
        this.p = -1;
        this.j.clear();
        this.r = Boolean.FALSE;
        this.t = 0;
        EffectRenderCore effectRenderCore = this.a;
        if (effectRenderCore != null) {
            effectRenderCore.removeRenderEffect("Storyboard");
        }
        if (str.equals("") || str == null) {
            str4 = "invaild caption directory!";
        } else if (str2.equals("") || str2 == null) {
            str4 = "invaild music file!";
        } else if (str3.equals("") || str3 == null) {
            str4 = "invaild lrc file!";
        } else {
            this.g = str.indexOf("assets:") == 0 ? Boolean.TRUE : Boolean.FALSE;
            this.h = str2.indexOf("assets:") == 0 ? Boolean.TRUE : Boolean.FALSE;
            this.i = str3.indexOf("assets:") == 0 ? Boolean.TRUE : Boolean.FALSE;
            if (str != null && !str.equals("")) {
                ArrayList arrayList = new ArrayList();
                if (!this.g.booleanValue()) {
                    File file = new File(str);
                    if (file.exists()) {
                        this.d = file.getAbsolutePath();
                        this.e = Constants.analysisJsonFile(this.c, file.getAbsolutePath() + "/info.json");
                        arrayList = Constants.getAllFilesFromSD(str, ".xml", true);
                    }
                } else if (str.length() > 8) {
                    this.d = str;
                    this.e = Constants.analysisJsonFileFromAsset(this.c, str.substring(8) + "/info.json");
                    arrayList = Constants.getAllFilesFromAssets(this.c, str.substring(8));
                }
                if (!(arrayList == null || arrayList.size() == 0)) {
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        if (((String) arrayList.get(i2)).indexOf("style") != -1) {
                            this.f = (String) arrayList.get(i2);
                        }
                    }
                }
            }
            a(str2, str3);
            this.m = System.currentTimeMillis();
            return true;
        }
        Log.e("NvLyrics", str4);
        return false;
    }

    public void stop() {
        MediaPlayer mediaPlayer = this.l;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        this.p = -1;
        this.r = Boolean.FALSE;
        this.t = 0;
        EffectRenderCore effectRenderCore = this.a;
        if (effectRenderCore != null) {
            effectRenderCore.removeRenderEffect("Storyboard");
        }
        System.gc();
    }
}
