package com.meicam.sdk;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import org.json.JSONObject;

public class NvsCheckExpirationOnline {
    private static final String TAG = "Meicam";
    private static NvsCheckExpirationOnline m_checker;
    private Context mContext = null;
    private Handler mHandler = null;
    private Thread mThread = null;
    private Runnable runnable = new Runnable() {
        /* class com.meicam.sdk.NvsCheckExpirationOnline.AnonymousClass1 */

        public void run() {
            if (NvsCheckExpirationOnline.this.mThread != null) {
                NvsCheckExpirationOnline.this.mThread.start();
            }
        }
    };

    public static NvsCheckExpirationOnline init(Context context) {
        NvsCheckExpirationOnline nvsCheckExpirationOnline = m_checker;
        if (nvsCheckExpirationOnline != null) {
            return nvsCheckExpirationOnline;
        }
        m_checker = new NvsCheckExpirationOnline(context);
        return m_checker;
    }

    public static NvsCheckExpirationOnline instance() {
        return m_checker;
    }

    private NvsCheckExpirationOnline(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
        this.mThread = new Thread(new Runnable() {
            /* class com.meicam.sdk.NvsCheckExpirationOnline.AnonymousClass2 */

            public void run() {
                NvsCheckExpirationOnline.this.checkExpiration();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void checkExpiration() {
        NvsHttpsRequest nvsHttpsRequest = new NvsHttpsRequest();
        HashMap hashMap = new HashMap();
        hashMap.put("command", "isExpired");
        hashMap.put("appId", this.mContext.getPackageName());
        try {
            JSONObject jSONObject = new JSONObject(nvsHttpsRequest.httpsRequest("https://api.meishesdk.com/license/index.php", hashMap));
            if (jSONObject.getInt("errNo") != 0) {
                String string = jSONObject.getString("errString");
                Log.e(TAG, "" + string);
            } else if (jSONObject.getBoolean("isExpired")) {
                NvsSystemVariableManager.setSystemVariableInt(this.mContext, "isExpired", 1);
                NvsSystemVariableManager.setSystemVariableString(this.mContext, "lastTime", NvsTimeUtil.getCurrentTime());
            } else {
                NvsSystemVariableManager.setSystemVariableInt(this.mContext, "isExpired", 0);
                NvsSystemVariableManager.setSystemVariableString(this.mContext, "lastTime", NvsTimeUtil.getCurrentTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCheck() {
        int hourRange;
        Context context = this.mContext;
        if (context != null) {
            String systemVariableString = NvsSystemVariableManager.getSystemVariableString(context, "lastTime");
            if (TextUtils.isEmpty(systemVariableString) || (hourRange = NvsTimeUtil.getHourRange(systemVariableString, NvsTimeUtil.getCurrentTime())) < 0 || hourRange > 24) {
                int randomTime = NvsTimeUtil.getRandomTime(30000, 60000);
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.postDelayed(this.runnable, (long) randomTime);
                }
            }
        }
    }

    public void release() {
        if (m_checker != null) {
            this.mContext = null;
            m_checker = null;
            this.mHandler.removeCallbacksAndMessages(null);
            try {
                this.mThread.join();
                this.mThread = null;
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
