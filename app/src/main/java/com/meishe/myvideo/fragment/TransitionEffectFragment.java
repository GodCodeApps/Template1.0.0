package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meicam.sdk.NvsVideoTransition;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.MeicamTransition;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.fragment.TransitionFragment;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;

public class TransitionEffectFragment extends TransitionFragment {
    private LinearLayout mLlTopContainer;
    private MeicamTransition mMeicamTransition = null;
    private RelativeLayout mRlSeekContainer;
    private SeekBar mSeekBar;
    private int mTargetTransitionIndex;
    private TextView trans_general_toAll;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.transition_general_list_fragment;
    }

    public TransitionEffectFragment(int i, TransitionFragment.TransitionRefreshListener transitionRefreshListener) {
        this.mTargetTransitionIndex = i;
        this.mTransitionRefreshListener = transitionRefreshListener;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mRecyclerView = (RecyclerView) this.mRootView.findViewById(R.id.recyclerView);
        this.mRlSeekContainer = (RelativeLayout) this.mRootView.findViewById(R.id.rl_seek_container);
        this.mLlTopContainer = (LinearLayout) this.mRootView.findViewById(R.id.ll_top_container);
        this.mSeekBar = (SeekBar) this.mRootView.findViewById(R.id.seek_bar);
        initRecyclerView();
        this.trans_general_toAll = (TextView) this.mRootView.findViewById(R.id.trans_general_toAll);
        this.trans_general_toAll.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
        this.mData = MenuDataManager.getTransitionEffectData(this.mContext);
        ((BaseInfo) this.mData.get(0)).mEffectType = 26;
        this.mAdapter.addAll(this.mData);
        setTransitionPosition(this.mContext);
        BaseInfo item = this.mAdapter.getItem(this.mAdapter.getSelectPosition());
        if (item == null) {
            return;
        }
        if (getResources().getString(R.string.top_menu_no).equals(item.mName) || getResources().getString(R.string.more).equals(item.mName)) {
            this.mLlTopContainer.setVisibility(8);
        } else {
            this.mLlTopContainer.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.TransitionEffectFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BaseInfo item = TransitionEffectFragment.this.mAdapter.getItem(i);
                if (item != null) {
                    if (TransitionEffectFragment.this.getResources().getString(R.string.top_menu_no).equals(item.mName) || TransitionEffectFragment.this.getResources().getString(R.string.more).equals(item.mName)) {
                        TransitionEffectFragment.this.mLlTopContainer.setVisibility(8);
                    } else {
                        TransitionEffectFragment.this.mLlTopContainer.setVisibility(0);
                    }
                }
                TransitionEffectFragment.this.postEvent(i, MessageEvent.MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT);
            }
        });
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class com.meishe.myvideo.fragment.TransitionEffectFragment.AnonymousClass2 */

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (TransitionEffectFragment.this.mMeicamTransition != null) {
                    long j = (long) i;
                    ((NvsVideoTransition) TransitionEffectFragment.this.mMeicamTransition.getObject()).setVideoTransitionDuration(j, 0);
                    TransitionEffectFragment.this.mMeicamTransition.setDuration(j);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                TransitionEffectFragment transitionEffectFragment = TransitionEffectFragment.this;
                transitionEffectFragment.mMeicamTransition = TimelineDataUtil.getMainTrackTransitionByIndex(transitionEffectFragment.mTargetTransitionIndex - 1);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                TransitionEffectFragment.this.mMeicamTransition = null;
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.trans_general_toAll) {
            TimelineDataUtil.setMainTrackTransitionAll(TimelineDataUtil.getMainTrackTransitionByIndex(this.mTargetTransitionIndex - 1));
            Logger.e("TAG", "onClick: " + this.mTargetTransitionIndex);
            if (this.mTransitionRefreshListener != null) {
                this.mTransitionRefreshListener.refreshTransition(this.mTargetTransitionIndex - 1);
            }
        }
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dp2px(this.mContext, 3.0f), ScreenUtils.dp2px(this.mContext, 3.0f)));
    }
}
