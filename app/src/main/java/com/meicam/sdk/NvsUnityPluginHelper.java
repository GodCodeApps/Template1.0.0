package com.meicam.sdk;

import java.util.concurrent.atomic.AtomicReference;

public class NvsUnityPluginHelper {
    private static AtomicReference<EventCallback> m_eventCallback = new AtomicReference<>(null);

    public interface EventCallback {
        void onPluginEvent(int i);
    }

    public static void setEventCallback(EventCallback eventCallback) {
        m_eventCallback.set(eventCallback);
    }

    protected static void onPluginEvent(int i) {
        EventCallback eventCallback = m_eventCallback.get();
        if (eventCallback != null) {
            try {
                eventCallback.onPluginEvent(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
