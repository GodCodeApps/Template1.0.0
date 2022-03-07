package com.meicam.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;

public class NvsStatisticsSender {
    private static String HTTPS_API_SET_STATISTICS = "https://api.meishesdk.com/statistics/index.php";
    private static final boolean NV_DEBUG = false;
    private static String NV_KEY_STATISTICS_APP_START_TIME = "NV_KEY_STATISTICS_APP_START_TIME";
    private static String NV_KEY_STATISTICS_INFO_CURRENT_DATE = "NV_KEY_STATISTICS_INFO_CURRENT_DATE";
    private static final int NV_STATISTICS_DAILY = 1;
    private static final int NV_STATISTICS_EVERY_START = 2;
    private static final int NV_STATISTICS_NEVER = 0;
    private static final String TAG = "Meicam";
    private static NvsStatisticsInfo m_statisticsInfo;
    private static NvsStatisticsSender m_statisticsSender;
    private Context m_context = null;
    private Handler m_handler = null;
    private boolean m_isStatisticsPrivateInfo = false;
    private int m_statisticsFrequency = 0;
    private HandlerThread m_thread = null;

    public static NvsStatisticsSender init(Context context) {
        NvsStatisticsSender nvsStatisticsSender = m_statisticsSender;
        if (nvsStatisticsSender != null) {
            return nvsStatisticsSender;
        }
        m_statisticsSender = new NvsStatisticsSender(context);
        m_statisticsInfo = new NvsStatisticsInfo(context);
        return m_statisticsSender;
    }

    public static NvsStatisticsSender getInstance() {
        return m_statisticsSender;
    }

    private NvsStatisticsSender(Context context) {
        this.m_context = context;
        this.m_thread = new HandlerThread("StatisticsSendThread");
        this.m_thread.start();
        this.m_handler = new Handler(this.m_thread.getLooper()) {
            /* class com.meicam.sdk.NvsStatisticsSender.AnonymousClass1 */

            public void handleMessage(Message message) {
                NvsStatisticsSender.this.startSendStatistics();
            }
        };
    }

    public void sendStatistics(int i, boolean z) {
        this.m_statisticsFrequency = i;
        this.m_isStatisticsPrivateInfo = z;
        this.m_handler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startSendStatistics() {
        if (this.m_statisticsFrequency != 0) {
            Set<String> systemVariableStringSet = NvsSystemVariableManager.getSystemVariableStringSet(this.m_context, NV_KEY_STATISTICS_APP_START_TIME);
            HashSet hashSet = new HashSet();
            for (String str : systemVariableStringSet) {
                hashSet.add(str);
            }
            hashSet.add(m_statisticsInfo.getStartTime());
            String systemVariableString = NvsSystemVariableManager.getSystemVariableString(this.m_context, NV_KEY_STATISTICS_INFO_CURRENT_DATE);
            if (systemVariableString.trim().equals(getCurrentDateString()) && this.m_statisticsFrequency == 1) {
                return;
            }
            if (!systemVariableString.trim().equals(getCurrentDateString()) || this.m_statisticsFrequency != 2) {
                HashSet hashSet2 = new HashSet();
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    hashSet2.add((String) it.next());
                }
                Iterator it2 = hashSet.iterator();
                while (it2.hasNext()) {
                    String str2 = (String) it2.next();
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("appId", m_statisticsInfo.getAppId());
                        jSONObject.put("startTime", str2);
                        jSONObject.put("deviceId", m_statisticsInfo.getDeviceId());
                        jSONObject.put("model", m_statisticsInfo.getModel());
                        jSONObject.put("osType", m_statisticsInfo.getOsType());
                        jSONObject.put("osVersion", m_statisticsInfo.getOsVersion());
                        if (this.m_isStatisticsPrivateInfo) {
                            jSONObject.put("phoneNumber", m_statisticsInfo.getPhoneNumber());
                            ArrayList lngAndLat = m_statisticsInfo.getLngAndLat();
                            jSONObject.put("longitude", lngAndLat.get(0));
                            jSONObject.put("latitude", lngAndLat.get(1));
                        }
                        String jSONObject2 = jSONObject.toString();
                        NvsHttpsRequest nvsHttpsRequest = new NvsHttpsRequest();
                        HashMap hashMap = new HashMap();
                        hashMap.put("command", "setAppStatistics");
                        if (new JSONObject(nvsHttpsRequest.postHttpsRequest(HTTPS_API_SET_STATISTICS, hashMap, jSONObject2)).getInt("errNo") == 0) {
                            hashSet2.remove(str2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                NvsSystemVariableManager.setSystemVariableStringSet(this.m_context, NV_KEY_STATISTICS_APP_START_TIME, hashSet2);
                NvsSystemVariableManager.setSystemVariableString(this.m_context, NV_KEY_STATISTICS_INFO_CURRENT_DATE, getCurrentDateString());
                return;
            }
            NvsSystemVariableManager.setSystemVariableStringSet(this.m_context, NV_KEY_STATISTICS_APP_START_TIME, hashSet);
        }
    }

    private String getCurrentDateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    }

    public void release() {
        if (m_statisticsSender != null) {
            this.m_context = null;
            m_statisticsInfo.release();
            m_statisticsInfo = null;
            m_statisticsSender = null;
            this.m_handler = null;
            try {
                this.m_thread.quit();
                this.m_thread = null;
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
