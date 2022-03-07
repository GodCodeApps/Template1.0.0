package com.meicam.sdk;

import com.meicam.sdk.NvsCustomVideoFx;

public class NvsCustomVideoTransition {

    public static class RenderContext {
        public VideoFrame comingInVideoFrame;
        public long effectEndTime;
        public long effectStartTime;
        public long effectTime;
        public NvsCustomVideoFx.RenderHelper helper;
        public VideoFrame outGoingVideoFrame;
        public VideoFrame outputVideoFrame;
        public float progress;
    }

    public interface Renderer {
        void onCleanup();

        void onInit();

        void onPreloadResources();

        void onRender(RenderContext renderContext);
    }

    public static class VideoFrame {
        public int height;
        public boolean isUpsideDownTexture;
        public int texId;
        public int width;
    }
}
