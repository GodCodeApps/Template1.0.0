package com.meishe.myvideo.view.editview;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import com.meishe.myvideo.view.editview.EditView;
import com.meishe.myvideoapp.R;

public abstract class EditControlView extends RelativeLayout {
    protected Context mContext;
    private AlertDialog mDialog;

    public abstract void dismiss();

    public abstract void execute(String str, String str2);

    /* access modifiers changed from: protected */
    public abstract <T> View getView(String str, EditView.CallBack<T> callBack);

    /* access modifiers changed from: protected */
    public abstract void initView();

    public abstract <T> void show(String str, EditView.CallBack<T> callBack);

    public EditControlView(Context context) {
        super(context);
    }

    public EditControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
    }

    public void showDialogView(AlertDialog alertDialog, View view) {
        alertDialog.show();
        alertDialog.setContentView(view);
        alertDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.width = -1;
        attributes.height = -2;
        attributes.flags = 2;
        alertDialog.getWindow().setGravity(80);
        attributes.dimAmount = 0.0f;
        alertDialog.getWindow().setAttributes(attributes);
        alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.colorTranslucent));
        alertDialog.getWindow().setWindowAnimations(R.style.edit_dlg_style);
    }
}
