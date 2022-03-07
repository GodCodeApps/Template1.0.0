package com.meishe.myvideo.util.permission;

import android.content.Context;
import android.view.View;
import com.meishe.myvideo.interfaces.TipsButtonClickListener;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideoapp.R;

public class PermissionDialog {
    public static void noPermissionDialog(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.permissions_tips);
        Util.showDialog(context, stringArray[0], stringArray[1], new TipsButtonClickListener() {
            /* class com.meishe.myvideo.util.permission.PermissionDialog.AnonymousClass1 */

            @Override // com.meishe.myvideo.interfaces.TipsButtonClickListener
            public void onTipsButtoClick(View view) {
                AppManager.getInstance().finishActivity();
            }
        });
    }
}
