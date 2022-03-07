package com.meishe.myvideo.fragment;

import android.content.Context;
import android.text.TextUtils;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;
import java.util.List;

public abstract class TransitionFragment extends BaseFragment {
    protected List<BaseInfo> mData;
    protected EditorTimelineTransitionAdapter.TransitionData mTransitionData;
    protected int mTransitionPosition;
    protected TransitionRefreshListener mTransitionRefreshListener;

    public interface TransitionRefreshListener {
        void refreshTransition(int i);
    }

    public void setTransitionPosition(Context context) {
        if (this.mTransitionData == null) {
            this.mTransitionPosition = 1;
        } else if (context.getResources().getString(R.string.top_menu_no).equals(this.mTransitionData.getTransitionName())) {
            this.mTransitionPosition = 1;
        } else if (CollectionUtils.isEmpty(this.mData) || TextUtils.isEmpty(this.mTransitionData.getTransitionName())) {
            this.mTransitionPosition = 1;
        } else {
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= this.mData.size()) {
                    break;
                } else if (this.mTransitionData.getTransitionName().equals(this.mData.get(i2).mName)) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
            this.mTransitionPosition = i;
        }
        if (this.mAdapter != null) {
            this.mAdapter.setSelectPosition(this.mTransitionPosition);
        }
    }

    public void setTransitionData(EditorTimelineTransitionAdapter.TransitionData transitionData) {
        this.mTransitionData = transitionData;
    }
}
