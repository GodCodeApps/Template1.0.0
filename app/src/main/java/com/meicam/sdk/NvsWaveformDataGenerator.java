package com.meicam.sdk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NvsWaveformDataGenerator {
    private final String TAG = "Meicam";
    private boolean m_fetchingWaveformData = false;
    private long m_nextTaskId = 1;
    private HashMap<Long, Task> m_taskMap = new HashMap<>();
    private float[] m_tmpLeftWaveformData;
    private float[] m_tmpRightWaveformData;
    private long m_tmpSamplesPerGroup = 0;
    private WaveformDataCallback m_waveformDataCallback;
    private long m_waveformDataGenerator = 0;

    public interface WaveformDataCallback {
        void onWaveformDataGenerationFailed(long j, String str, long j2);

        void onWaveformDataReady(long j, String str, long j2, long j3, float[] fArr, float[] fArr2);
    }

    private native void nativeCancelTask(long j);

    private native void nativeClose(long j);

    private native long nativeFetchWaveformData(long j, String str, long j2, long j3, long j4);

    private native long nativeGetAudioFileDuration(String str);

    private native long nativeGetAudioFileSampleCount(String str);

    private native long nativeInit();

    /* access modifiers changed from: private */
    public static class Task {
        public String m_audioFilePath;
        public long m_audioFileSampleCount;
        private float[] m_leftWaveformData;
        private float[] m_rightWaveformData;
        private long m_samplesPerGroup;
        long taskId;
        long waveformTaskId;

        private Task() {
        }
    }

    public NvsWaveformDataGenerator() {
        NvsUtils.checkFunctionInMainThread();
        this.m_waveformDataGenerator = nativeInit();
    }

    public void release() {
        NvsUtils.checkFunctionInMainThread();
        if (!isReleased()) {
            for (Map.Entry<Long, Task> entry : this.m_taskMap.entrySet()) {
                nativeCancelTask(entry.getValue().waveformTaskId);
            }
            this.m_taskMap.clear();
            this.m_waveformDataCallback = null;
            nativeClose(this.m_waveformDataGenerator);
            this.m_waveformDataGenerator = 0;
        }
    }

    public boolean isReleased() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_waveformDataGenerator == 0;
    }

    public void setWaveformDataCallback(WaveformDataCallback waveformDataCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_waveformDataCallback = waveformDataCallback;
    }

    public long getAudioFileDuration(String str) {
        NvsUtils.checkFunctionInMainThread();
        if (str == null) {
            return 0;
        }
        return nativeGetAudioFileDuration(str);
    }

    public long getAudioFileSampleCount(String str) {
        NvsUtils.checkFunctionInMainThread();
        if (str == null) {
            return 0;
        }
        return nativeGetAudioFileSampleCount(str);
    }

    public long generateWaveformData(String str, long j, long j2, long j3, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (isReleased() || str == null || j <= 0) {
            return 0;
        }
        long nativeGetAudioFileSampleCount = nativeGetAudioFileSampleCount(str);
        if (nativeGetAudioFileSampleCount <= 0) {
            return 0;
        }
        this.m_fetchingWaveformData = true;
        long nativeFetchWaveformData = nativeFetchWaveformData(this.m_waveformDataGenerator, str, j, j2, j3);
        this.m_fetchingWaveformData = false;
        if (nativeFetchWaveformData == 0) {
            return 0;
        }
        Task task = new Task();
        long j4 = this.m_nextTaskId;
        this.m_nextTaskId = 1 + j4;
        task.taskId = j4;
        task.waveformTaskId = nativeFetchWaveformData;
        task.m_audioFilePath = str;
        task.m_audioFileSampleCount = nativeGetAudioFileSampleCount;
        task.m_samplesPerGroup = j;
        if (this.m_tmpSamplesPerGroup > 0) {
            task.m_leftWaveformData = this.m_tmpLeftWaveformData;
            task.m_rightWaveformData = this.m_tmpRightWaveformData;
            task.m_samplesPerGroup = this.m_tmpSamplesPerGroup;
            this.m_tmpLeftWaveformData = null;
            this.m_tmpRightWaveformData = null;
            this.m_tmpSamplesPerGroup = 0;
        }
        this.m_taskMap.put(Long.valueOf(task.taskId), task);
        if (task.m_leftWaveformData != null) {
            finishWaveformDataFetchingTask(task.taskId, true);
        }
        return task.taskId;
    }

    public void cancelTask(long j) {
        NvsUtils.checkFunctionInMainThread();
        Task task = this.m_taskMap.get(Long.valueOf(j));
        if (task != null) {
            if (!isReleased()) {
                nativeCancelTask(task.waveformTaskId);
            }
            this.m_taskMap.remove(Long.valueOf(j));
        }
    }

    /* access modifiers changed from: protected */
    public void notifyWaveformDataReady(long j, long j2, long j3, float[] fArr, float[] fArr2) {
        long j4;
        if (!this.m_fetchingWaveformData) {
            Iterator<Map.Entry<Long, Task>> it = this.m_taskMap.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    j4 = 0;
                    break;
                }
                Task value = it.next().getValue();
                if (value.waveformTaskId == j) {
                    value.m_samplesPerGroup = j2;
                    value.m_leftWaveformData = fArr;
                    value.m_rightWaveformData = fArr2;
                    j4 = value.taskId;
                    break;
                }
            }
            if (j4 != 0) {
                finishWaveformDataFetchingTask(j4, false);
                return;
            }
            return;
        }
        this.m_tmpSamplesPerGroup = j2;
        this.m_tmpLeftWaveformData = fArr;
        this.m_tmpRightWaveformData = fArr2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x001a, code lost:
        r13 = r23.m_waveformDataCallback;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void finishWaveformDataFetchingTask(final long r24, boolean r26) {
        /*
            r23 = this;
            r12 = r23
            java.util.HashMap<java.lang.Long, com.meicam.sdk.NvsWaveformDataGenerator$Task> r0 = r12.m_taskMap
            java.lang.Long r1 = java.lang.Long.valueOf(r24)
            java.lang.Object r0 = r0.get(r1)
            com.meicam.sdk.NvsWaveformDataGenerator$Task r0 = (com.meicam.sdk.NvsWaveformDataGenerator.Task) r0
            java.util.HashMap<java.lang.Long, com.meicam.sdk.NvsWaveformDataGenerator$Task> r1 = r12.m_taskMap
            java.lang.Long r2 = java.lang.Long.valueOf(r24)
            r1.remove(r2)
            if (r0 != 0) goto L_0x001a
            return
        L_0x001a:
            com.meicam.sdk.NvsWaveformDataGenerator$WaveformDataCallback r13 = r12.m_waveformDataCallback
            if (r13 != 0) goto L_0x001f
            return
        L_0x001f:
            java.lang.String r5 = r0.m_audioFilePath
            long r6 = r0.m_audioFileSampleCount
            long r19 = com.meicam.sdk.NvsWaveformDataGenerator.Task.access$100(r0)
            float[] r21 = com.meicam.sdk.NvsWaveformDataGenerator.Task.access$200(r0)
            float[] r22 = com.meicam.sdk.NvsWaveformDataGenerator.Task.access$300(r0)
            if (r26 != 0) goto L_0x003b
            r14 = r24
            r16 = r5
            r17 = r6
            r13.onWaveformDataReady(r14, r16, r17, r19, r21, r22)
            goto L_0x0058
        L_0x003b:
            android.os.Handler r14 = new android.os.Handler
            android.os.Looper r0 = android.os.Looper.getMainLooper()
            r14.<init>(r0)
            com.meicam.sdk.NvsWaveformDataGenerator$1 r15 = new com.meicam.sdk.NvsWaveformDataGenerator$1
            r0 = r15
            r1 = r23
            r2 = r13
            r3 = r24
            r8 = r19
            r10 = r21
            r11 = r22
            r0.<init>(r2, r3, r5, r6, r8, r10, r11)
            r14.post(r15)
        L_0x0058:
            return
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meicam.sdk.NvsWaveformDataGenerator.finishWaveformDataFetchingTask(long, boolean):void");
    }
}
