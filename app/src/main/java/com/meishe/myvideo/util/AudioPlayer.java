package com.meishe.myvideo.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.meishe.myvideo.bean.MusicInfo;
import com.meishe.myvideoapp.R;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayer {
    private static final int UPDATE_TIME = 0;
    private static AudioPlayer mMusicPlayer;
    private final String TAG = "AudioPlayer";
    private Context mContext;
    private MusicInfo mCurrentMusic;
    private OnPlayListener mListener;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private final PlayHandler m_handler = new PlayHandler(this);

    public interface OnPlayListener {
        void onGetCurrentPos(int i);

        void onMusicPlay();

        void onMusicStop();
    }

    public void setPlayListener(OnPlayListener onPlayListener) {
        this.mListener = onPlayListener;
    }

    private AudioPlayer(Context context) {
        this.mContext = context;
        this.mCurrentMusic = null;
    }

    public static AudioPlayer getInstance(Context context) {
        if (mMusicPlayer == null) {
            synchronized (AudioPlayer.class) {
                if (mMusicPlayer == null) {
                    mMusicPlayer = new AudioPlayer(context);
                }
            }
        }
        return mMusicPlayer;
    }

    public void destroyPlayer() {
        if (this.mMediaPlayer != null) {
            stopMusicTimer();
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
            this.m_handler.removeCallbacksAndMessages(null);
        }
    }

    public void startPlay() {
        stopMusicTimer();
        MusicInfo musicInfo = this.mCurrentMusic;
        if (musicInfo != null && this.mMediaPlayer != null) {
            if (musicInfo.isPrepare()) {
                try {
                    this.mMediaPlayer.start();
                    startMusicTimer();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("AudioPlayer", "start Exception");
                }
            }
            OnPlayListener onPlayListener = this.mListener;
            if (onPlayListener != null) {
                onPlayListener.onMusicPlay();
            }
        }
    }

    public void stopPlay() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            stopMusicTimer();
            OnPlayListener onPlayListener = this.mListener;
            if (onPlayListener != null) {
                onPlayListener.onMusicStop();
            }
        }
    }

    public void setCurrentMusic(MusicInfo musicInfo, boolean z) {
        if (musicInfo != null) {
            this.mCurrentMusic = musicInfo;
            this.mCurrentMusic.setPrepare(false);
            resetMediaPlayer(z);
        }
    }

    public void seekPosition(long j) {
        long j2 = j / 1000;
        if (j2 < ((long) this.mMediaPlayer.getDuration()) && j2 >= 0) {
            this.mMediaPlayer.seekTo((int) j2);
        }
    }

    public long getCurMusicPos() {
        return (long) this.mMediaPlayer.getCurrentPosition();
    }

    private void resetMediaPlayer(final boolean z) {
        String str;
        stopMusicTimer();
        if (this.mCurrentMusic == null) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                    this.mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("AudioPlayer", "stop & release: null");
                }
                this.mMediaPlayer = null;
                return;
            }
            return;
        }
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                /* class com.meishe.myvideo.util.AudioPlayer.AnonymousClass1 */

                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (AudioPlayer.this.mCurrentMusic != null) {
                        int trimIn = ((int) AudioPlayer.this.mCurrentMusic.getTrimIn()) / 1000;
                        if (trimIn > 0) {
                            AudioPlayer.this.mMediaPlayer.seekTo(trimIn);
                        }
                        if (AudioPlayer.this.mCurrentMusic.isPrepare() && !Util.isBackground(AudioPlayer.this.mContext)) {
                            AudioPlayer.this.startPlay();
                        }
                    }
                }
            });
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                /* class com.meishe.myvideo.util.AudioPlayer.AnonymousClass2 */

                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (AudioPlayer.this.mCurrentMusic != null) {
                        AudioPlayer.this.mCurrentMusic.setPrepare(true);
                        AudioPlayer.this.mMediaPlayer.seekTo(((int) AudioPlayer.this.mCurrentMusic.getTrimIn()) / 1000);
                    }
                    if (!Util.isBackground(AudioPlayer.this.mContext) && z) {
                        AudioPlayer.this.startPlay();
                    }
                }
            });
            this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                /* class com.meishe.myvideo.util.AudioPlayer.AnonymousClass3 */

                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    Toast.makeText(AudioPlayer.this.mContext, AudioPlayer.this.mContext.getString(R.string.music_play_error), 0).show();
                    return true;
                }
            });
        }
        try {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("AudioPlayer", "stop & release: null");
        }
        try {
            if (this.mCurrentMusic.isHttpMusic()) {
                str = this.mCurrentMusic.getFileUrl();
            } else {
                str = this.mCurrentMusic.getFilePath();
            }
            if (str != null) {
                if (this.mCurrentMusic.isAsset()) {
                    AssetFileDescriptor openFd = this.mContext.getAssets().openFd(this.mCurrentMusic.getAssetPath());
                    this.mMediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                } else {
                    this.mMediaPlayer.setDataSource(str);
                }
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.prepareAsync();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private void startMusicTimer() {
        this.mTimer = new Timer();
        this.mTimerTask = new TimerTask() {
            /* class com.meishe.myvideo.util.AudioPlayer.AnonymousClass4 */

            public void run() {
                if (AudioPlayer.this.mMediaPlayer != null && AudioPlayer.this.mMediaPlayer.isPlaying()) {
                    AudioPlayer.this.m_handler.sendEmptyMessage(0);
                }
            }
        };
        this.mTimer.schedule(this.mTimerTask, 0, 100);
    }

    private void stopMusicTimer() {
        TimerTask timerTask = this.mTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mTimerTask = null;
        }
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendCurrentPos(int i) {
        OnPlayListener onPlayListener = this.mListener;
        if (onPlayListener != null) {
            onPlayListener.onGetCurrentPos(i * 1000);
        }
    }

    /* access modifiers changed from: package-private */
    public static class PlayHandler extends Handler {
        WeakReference<AudioPlayer> mWeakReference;

        public PlayHandler(AudioPlayer audioPlayer) {
            this.mWeakReference = new WeakReference<>(audioPlayer);
        }

        public void handleMessage(Message message) {
            AudioPlayer audioPlayer = this.mWeakReference.get();
            if (audioPlayer != null && message.what == 0 && audioPlayer.mMediaPlayer != null) {
                int currentPosition = audioPlayer.mMediaPlayer.getCurrentPosition();
                if (((long) currentPosition) >= audioPlayer.mCurrentMusic.getTrimOut() / 1000) {
                    audioPlayer.mMediaPlayer.seekTo((int) (audioPlayer.mCurrentMusic.getTrimIn() / 1000));
                    audioPlayer.startPlay();
                }
                audioPlayer.sendCurrentPos(currentPosition);
            }
        }
    }
}
