package com.meishe.base.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;

public class InitAppService extends IntentService {
    private static final String ACTION_INIT = "com.zcyChat.action.INIT";

    public InitAppService() {
        super("InitAppService");
    }

    public static void startInit(Context context) {
        Intent intent = new Intent(context, InitAppService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            intent.getAction().equals(ACTION_INIT);
        }
    }
}
