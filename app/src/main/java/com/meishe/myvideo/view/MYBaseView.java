package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideoapp.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class MYBaseView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "MYBaseView";
    protected Context mContext;
    protected boolean mIsShow;

    public abstract void init();

    public abstract void initData();

    public abstract void initListener();

    public abstract void initRecyclerView();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
    }

    public MYBaseView(Context context) {
        super(context);
        this.mContext = context;
        init();
        initRecyclerView();
        initData();
        initListener();
    }

    public MYBaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init();
        initRecyclerView();
        initData();
        initListener();
        setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYBaseView.AnonymousClass1 */

            public void onClick(View view) {
                Logger.d(MYBaseView.TAG, "onClick: Click other point，do nothing.");
            }
        });
    }

    public MYBaseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        init();
        initRecyclerView();
        initData();
        initListener();
    }

    public void show() {
        this.mIsShow = true;
        if (getVisibility() != 0) {
            setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_enter));
            setVisibility(0);
            EventBus.getDefault().register(this);
        }
    }

    public boolean isShow() {
        return this.mIsShow;
    }

    public void hide() {
        if (this.mIsShow) {
            this.mIsShow = false;
            setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_exit));
            setVisibility(8);
            EventBus.getDefault().unregister(this);
        }
    }

    /* access modifiers changed from: protected */
    public void postEvent(int i, int i2, BaseRecyclerAdapter baseRecyclerAdapter) {
        BaseInfo item = baseRecyclerAdapter.getItem(i);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i2);
        messageEvent.setBaseInfo(item);
        EventBus.getDefault().post(messageEvent);
    }
}
