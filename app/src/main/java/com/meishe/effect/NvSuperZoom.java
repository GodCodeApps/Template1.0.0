package com.meishe.effect;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.meicam.effect.sdk.NvsEffectRenderCore;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.effect.sdk.NvsVideoEffect;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.myvideo.audio.AudioRecorder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NvSuperZoom {
    private Context a;
    private long b;
    private MediaPlayer c;
    private NvsEffectSdkContext d;
    private NvZoomEffectRenderCore e;
    private boolean f = false;
    private boolean g = false;
    private List h = new ArrayList();
    private boolean i = false;
    private int j = 0;
    private int k = 0;
    private Timer l;
    private TimerTask m;
    private int n;
    private int o;
    private String p = null;

    class StillImageData {
        public long m_stillImageBeginTime = 0;
        public long m_stillImageDuration = 0;

        public StillImageData() {
        }
    }

    public NvSuperZoom(Context context) {
        if (context != null) {
            this.a = context;
            this.d = NvsEffectSdkContext.getInstance();
            if (this.d == null) {
                Log.e("EffectSDK", "EffectSdkContext is null!");
            }
            this.e = new NvZoomEffectRenderCore(this.d);
            if (this.e == null) {
                Log.e("EffectSDK", "Failed to create effect render core!");
            }
            this.c = new MediaPlayer();
            if (this.c == null) {
                Log.e("EffectSDK", "Failed to create media player!");
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void a() {
        Timer timer = this.l;
        if (timer != null) {
            timer.cancel();
            this.l = null;
        }
        TimerTask timerTask = this.m;
        if (timerTask != null) {
            timerTask.cancel();
            this.m = null;
        }
    }

    public NvsTimeline createTimeline(String str, String str2) {
        String str3;
        NvsStreamingContext instance = NvsStreamingContext.getInstance();
        if (instance == null) {
            str3 = "StreamingSdkContext is null!";
        } else {
            NvsAVFileInfo aVFileInfo = instance.getAVFileInfo(str);
            NvsSize videoStreamDimension = aVFileInfo.getVideoStreamDimension(0);
            NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
            nvsVideoResolution.imageWidth = videoStreamDimension.width;
            nvsVideoResolution.imageHeight = videoStreamDimension.height;
            int videoStreamRotation = aVFileInfo.getVideoStreamRotation(0);
            if (videoStreamRotation == 1 || videoStreamRotation == 3) {
                nvsVideoResolution.imageWidth = videoStreamDimension.height;
                nvsVideoResolution.imageHeight = videoStreamDimension.width;
            }
            nvsVideoResolution.imagePAR = new NvsRational(1, 1);
            NvsRational nvsRational = new NvsRational(25, 1);
            NvsAudioResolution nvsAudioResolution = new NvsAudioResolution();
            nvsAudioResolution.sampleRate = AudioRecorder.RecordConfig.SAMPLE_RATE_44K_HZ;
            nvsAudioResolution.channelCount = 2;
            NvsTimeline createTimeline = instance.createTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
            if (createTimeline == null) {
                str3 = "Failed to create timeline!";
            } else {
                NvsVideoTrack appendVideoTrack = createTimeline.appendVideoTrack();
                if (appendVideoTrack == null) {
                    str3 = "append video track failed!";
                } else if (appendVideoTrack.appendClip(str) == null) {
                    str3 = "append video clip failed!";
                } else {
                    NvsAudioTrack appendAudioTrack = createTimeline.appendAudioTrack();
                    if (appendAudioTrack == null) {
                        str3 = "append audio track failed!";
                    } else {
                        String str4 = "assets:/meicam/" + str2 + "/" + str2 + ".m4a";
                        if (this.p != null) {
                            str4 = this.p + "/" + str2 + "/" + str2 + ".m4a";
                        }
                        if (appendAudioTrack.appendClip(str4) != null) {
                            return createTimeline;
                        }
                        str3 = "append audio clip failed!";
                    }
                }
            }
        }
        Log.e("EffectSDK", str3);
        return null;
    }

    public long getEffectDuration(String str) {
        InputStreamReader inputStreamReader;
        Context context = this.a;
        if (context == null) {
            return 0;
        }
        try {
            if (this.p == null) {
                AssetManager assets = context.getAssets();
                inputStreamReader = new InputStreamReader(assets.open("meicam/" + str + "/info.json"), Key.STRING_CHARSET_NAME);
            } else {
                inputStreamReader = new InputStreamReader(new FileInputStream(this.p + "/" + str + "/info.json"), Key.STRING_CHARSET_NAME);
            }
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            bufferedReader.close();
            inputStreamReader.close();
            try {
                JSONObject jSONObject = new JSONObject(sb.toString());
                if (jSONObject.has("fxDuration")) {
                    return (long) jSONObject.getInt("fxDuration");
                }
                return 0;
            } catch (JSONException e2) {
                e2.printStackTrace();
                Log.e("EffectSDK", "Failed to parse json file!");
                return 0;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            Log.e("EffectSDK", "Failed to read json file!");
            return 0;
        }
    }

    public NvsEffectRenderCore getEffectRenderCore() {
        NvZoomEffectRenderCore nvZoomEffectRenderCore;
        if (this.d == null || (nvZoomEffectRenderCore = this.e) == null) {
            return null;
        }
        return nvZoomEffectRenderCore.getEffectRenderCore();
    }

    public void releaseResources() {
        MediaPlayer mediaPlayer = this.c;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        NvZoomEffectRenderCore nvZoomEffectRenderCore = this.e;
        if (nvZoomEffectRenderCore != null) {
            nvZoomEffectRenderCore.destoryGLResource();
        }
        this.e = null;
        this.d = null;
        this.a = null;
    }

    public int render(int i2, boolean z, int i3, boolean z2) {
        if (!this.f || this.d == null || this.e == null) {
            return i2;
        }
        long j2 = 0;
        if (!this.g) {
            this.b = System.currentTimeMillis();
            this.g = true;
        } else {
            j2 = System.currentTimeMillis() - this.b;
        }
        int i4 = this.k;
        if (i4 <= 0 || j2 < ((long) i4)) {
            int renderVideoEffect = this.e.renderVideoEffect(i2, z, this.n, this.o, j2, i3, z2, this.i);
            int i5 = 0;
            this.i = false;
            while (true) {
                if (i5 >= this.h.size()) {
                    break;
                }
                StillImageData stillImageData = (StillImageData) this.h.get(i5);
                if (j2 < stillImageData.m_stillImageBeginTime || j2 >= stillImageData.m_stillImageBeginTime + stillImageData.m_stillImageDuration) {
                    i5++;
                } else if (this.j != i5) {
                    this.j = i5;
                } else {
                    this.i = true;
                }
            }
            return renderVideoEffect;
        }
        stop();
        return i2;
    }

    public boolean renderEnded() {
        return !this.f;
    }

    public void setAssetsExternalPath(String str) {
        this.p = str;
    }

    public boolean start(String str, int i2, int i3, float f2, float f3) {
        float f4;
        InputStreamReader inputStreamReader;
        StringBuilder sb;
        String str2;
        TimerTask timerTask;
        String str3;
        if (!NvsEffectSdkContext.functionalityAuthorised("superZoom")) {
            str3 = "Functionality superZoom is not authorised!";
        } else if (this.d == null || this.e == null || this.c == null) {
            Log.e("EffectSDK", "Effect session is not initialised, please init it first!");
            return false;
        } else if (this.f) {
            str3 = "Effect has started, please stop it first!";
        } else {
            float f5 = 0.09f;
            if (str == "rotate") {
                float f6 = -0.18f;
                if (f2 >= -0.18f) {
                    f6 = f2;
                }
                f4 = 0.18f;
                if (f6 <= 0.18f) {
                    f4 = f6;
                }
                float f7 = -0.09f;
                if (f3 >= -0.09f) {
                    f7 = f3;
                }
                if (f7 <= 0.09f) {
                    f5 = f7;
                }
            } else {
                f4 = f2;
                f5 = f3;
            }
            this.n = i2;
            this.o = i3;
            this.h.clear();
            try {
                if (this.p == null) {
                    AssetManager assets = this.a.getAssets();
                    inputStreamReader = new InputStreamReader(assets.open("meicam/" + str + "/info.json"), Key.STRING_CHARSET_NAME);
                } else {
                    inputStreamReader = new InputStreamReader(new FileInputStream(this.p + "/" + str + "/info.json"), Key.STRING_CHARSET_NAME);
                }
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb2 = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb2.append(readLine);
                }
                bufferedReader.close();
                inputStreamReader.close();
                try {
                    JSONObject jSONObject = new JSONObject(sb2.toString());
                    if (jSONObject.has("fxDuration")) {
                        this.k = jSONObject.getInt("fxDuration");
                    }
                    if (jSONObject.has("stillImages")) {
                        JSONArray jSONArray = jSONObject.getJSONArray("stillImages");
                        for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i4);
                            if (jSONObject2 != null) {
                                StillImageData stillImageData = new StillImageData();
                                stillImageData.m_stillImageBeginTime = jSONObject2.getLong("stillImageBeginTime");
                                stillImageData.m_stillImageDuration = jSONObject2.getLong("stillImageDuration");
                                this.h.add(stillImageData);
                            }
                        }
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    Log.e("EffectSDK", "Failed to parse json file!");
                }
                String str4 = i3 / i2 == 2 ? "fx9v18.xml" : "fx9v16.xml";
                if (this.p == null) {
                    str2 = FileUtils.readFile("meicam/" + str + "/" + str4, this.a.getAssets());
                    sb = new StringBuilder("assets:/meicam/");
                } else {
                    str2 = FileUtils.readFile(this.p + "/" + str + "/" + str4, null);
                    sb = new StringBuilder();
                    sb.append(this.p);
                    sb.append("/");
                }
                sb.append(str);
                String sb3 = sb.toString();
                if (str2 == null) {
                    Log.e("EffectSDK", "Failed to read description file!");
                    return false;
                }
                String replace = str2.replace("anchorXValue", String.valueOf(f4 * ((float) i2))).replace("anchorYValue", String.valueOf(f5 * ((float) i3)));
                NvsVideoEffect createVideoEffect = this.d.createVideoEffect("Storyboard", new NvsRational(i2, i3));
                if (createVideoEffect == null) {
                    Log.e("EffectSDK", "Failed to create video effect!");
                    return false;
                }
                createVideoEffect.setStringVal("Description String", replace);
                createVideoEffect.setStringVal("Resource Dir", sb3);
                this.e.addNewRenderEffect(createVideoEffect);
                try {
                    this.c.reset();
                    if (this.p == null) {
                        AssetManager assets2 = this.a.getAssets();
                        AssetFileDescriptor openFd = assets2.openFd("meicam/" + str + "/" + str + ".m4a");
                        this.c.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    } else {
                        MediaPlayer mediaPlayer = this.c;
                        mediaPlayer.setDataSource(this.p + "/" + str + "/" + str + ".m4a");
                    }
                    this.c.prepare();
                    this.c.seekTo(0);
                } catch (IOException e3) {
                    e3.printStackTrace();
                    Log.e("EffectSDK", "Failed to prepare media player!");
                }
                this.c.start();
                a();
                if (this.l == null) {
                    this.l = new Timer();
                }
                if (this.m == null) {
                    this.m = new TimerTask() {
                        /* class com.meishe.effect.NvSuperZoom.AnonymousClass1 */

                        public void run() {
                            if (NvSuperZoom.this.c.isPlaying() && NvSuperZoom.this.c.getCurrentPosition() >= NvSuperZoom.this.k) {
                                NvSuperZoom.this.a();
                                NvSuperZoom.this.c.pause();
                                NvSuperZoom.this.c.seekTo(0);
                            }
                        }
                    };
                }
                Timer timer = this.l;
                if (!(timer == null || (timerTask = this.m) == null)) {
                    timer.schedule(timerTask, 0, 100);
                }
                this.g = false;
                this.f = true;
                return true;
            } catch (IOException e4) {
                e4.printStackTrace();
                Log.e("EffectSDK", "Failed to read json file!");
                return false;
            }
        }
        Log.e("EffectSDK", str3);
        return false;
    }

    public void stop() {
        if (this.f) {
            if (this.c != null) {
                a();
                try {
                    this.c.reset();
                } catch (Exception unused) {
                }
            }
            NvZoomEffectRenderCore nvZoomEffectRenderCore = this.e;
            if (nvZoomEffectRenderCore != null) {
                nvZoomEffectRenderCore.removeRenderEffect("Storyboard");
            }
            this.f = false;
            this.g = false;
            this.i = false;
        }
    }
}
