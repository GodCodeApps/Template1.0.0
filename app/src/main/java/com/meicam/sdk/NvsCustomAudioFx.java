package com.meicam.sdk;

public class NvsCustomAudioFx {

    public static class RenderContext {
        public long effectEndTime;
        public long effectStartTime;
        public long effectTime;
        public NvsAudioSampleBuffers inputAudioSample;
    }

    public interface Renderer {
        void onCleanup();

        NvsAudioSampleBuffers onFlush();

        void onInit();

        NvsAudioSampleBuffers onRender(RenderContext renderContext);

        int querySupportedInputAudioSampleFormat();
    }
}
