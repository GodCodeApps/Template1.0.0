package com.meishe.myvideo.util.ui;

import android.content.Context;
import android.view.View;
import com.meishe.myvideo.view.CommonDialog;

public class DialogUtil {
    public static void showSelectDialog(Context context, final String str, final String str2, final String str3, final String str4, final CommonDialog.TipsButtonClickListener tipsButtonClickListener) {
        final CommonDialog commonDialog = new CommonDialog(context, 2);
        commonDialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            /* class com.meishe.myvideo.util.ui.DialogUtil.AnonymousClass1 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnCreateListener
            public void OnCreated() {
                commonDialog.setTitleTxt(str);
                commonDialog.setFirstTipsTxt(str2);
                commonDialog.setBtnText(str3, str4);
            }
        });
        commonDialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            /* class com.meishe.myvideo.util.ui.DialogUtil.AnonymousClass2 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnRightBtnClicked(View view) {
                commonDialog.dismiss();
                CommonDialog.TipsButtonClickListener tipsButtonClickListener = tipsButtonClickListener;
                if (tipsButtonClickListener != null) {
                    tipsButtonClickListener.onTipsRightButtonClick(view);
                }
            }

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnLeftBtnClicked(View view) {
                commonDialog.dismiss();
                CommonDialog.TipsButtonClickListener tipsButtonClickListener = tipsButtonClickListener;
                if (tipsButtonClickListener != null) {
                    tipsButtonClickListener.onTipsLeftButtonClick(view);
                }
            }
        });
        commonDialog.setCancelable(true);
        commonDialog.show();
    }

    public static void showTipDialog(Context context, final String str, final String str2, final String str3, final CommonDialog.TipsButtonClickListener tipsButtonClickListener) {
        final CommonDialog commonDialog = new CommonDialog(context, 1);
        commonDialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            /* class com.meishe.myvideo.util.ui.DialogUtil.AnonymousClass3 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnCreateListener
            public void OnCreated() {
                commonDialog.setTitleTxt(str);
                commonDialog.setFirstTipsTxt(str2);
                commonDialog.setBtnText(null, str3);
            }
        });
        commonDialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            /* class com.meishe.myvideo.util.ui.DialogUtil.AnonymousClass4 */

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnRightBtnClicked(View view) {
                commonDialog.dismiss();
                CommonDialog.TipsButtonClickListener tipsButtonClickListener = tipsButtonClickListener;
                if (tipsButtonClickListener != null) {
                    tipsButtonClickListener.onTipsRightButtonClick(view);
                }
            }

            @Override // com.meishe.myvideo.view.CommonDialog.OnBtnClickListener
            public void OnLeftBtnClicked(View view) {
                commonDialog.dismiss();
                CommonDialog.TipsButtonClickListener tipsButtonClickListener = tipsButtonClickListener;
                if (tipsButtonClickListener != null) {
                    tipsButtonClickListener.onTipsLeftButtonClick(view);
                }
            }
        });
        commonDialog.setCancelable(true);
        commonDialog.show();
    }
}
