package com.meishe.myvideo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.event.MessageEvent;
import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment<T> extends Fragment implements View.OnClickListener {
    protected static final int SPAN_COUNT = 5;
    public T listener;
    protected FragmentActivity mActivity;
    protected MultiFunctionAdapter mAdapter;
    protected Context mContext;
    protected boolean mIsPrepare;
    protected boolean mIsVisible;
    protected RecyclerView mRecyclerView;
    protected View mRootView;

    /* access modifiers changed from: protected */
    public abstract void initArguments(Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void initListener();

    /* access modifiers changed from: protected */
    public abstract int initRootView();

    /* access modifiers changed from: protected */
    public abstract void initView();

    /* access modifiers changed from: protected */
    public abstract void onLazyLoad();

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
        this.listener = (T) this.mActivity;
        this.mContext = context;
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mRootView = layoutInflater.inflate(initRootView(), viewGroup, false);
        initArguments(getArguments());
        initView();
        this.mIsPrepare = true;
        onLazyLoad();
        initListener();
        return this.mRootView;
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        this.mIsVisible = z;
        if (z) {
            onVisibleToUser();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibleToUser() {
        if (this.mIsPrepare && this.mIsVisible) {
            onLazyLoad();
        }
    }

    /* access modifiers changed from: protected */
    public <T extends View> T findViewById(int i) {
        View view = this.mRootView;
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(i);
    }

    /* access modifiers changed from: protected */
    public void postEvent(int i, int i2) {
        BaseInfo item = this.mAdapter.getItem(i);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i2);
        messageEvent.setBaseInfo(item);
        EventBus.getDefault().post(messageEvent);
    }

    /* access modifiers changed from: protected */
    public void sendEvent(int i, int i2) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setEventType(i2);
        messageEvent.setProgress(i);
        EventBus.getDefault().post(messageEvent);
    }
}
