package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideoapp.R;

public class CaptionPositionFragment extends BaseFragment {
    public static final int CAPTION_ALIGN_BOTTOM = 5;
    public static final int CAPTION_ALIGN_HORIZ_CENTER = 1;
    public static final int CAPTION_ALIGN_LEFT = 0;
    public static final int CAPTION_ALIGN_RIGHT = 2;
    public static final int CAPTION_ALIGN_TOP = 3;
    public static final int CAPTION_ALIGN_VERT_CENTER = 4;
    private ImageView mAlignBottom;
    private ImageView mAlignCenterHorizontal;
    private ImageView mAlignCenterVertical;
    private ImageView mAlignLeft;
    private ImageView mAlignRight;
    private ImageView mAlignTop;
    private LinearLayout mLlApplyToAll;

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initArguments(Bundle bundle) {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public int initRootView() {
        return R.layout.caption_position_fragment;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void onLazyLoad() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initView() {
        this.mAlignLeft = (ImageView) this.mRootView.findViewById(R.id.alignLeft);
        this.mAlignCenterHorizontal = (ImageView) this.mRootView.findViewById(R.id.alignCenterHorizontal);
        this.mAlignRight = (ImageView) this.mRootView.findViewById(R.id.alignRight);
        this.mAlignTop = (ImageView) this.mRootView.findViewById(R.id.alignTop);
        this.mAlignCenterVertical = (ImageView) this.mRootView.findViewById(R.id.alignCenterVertical);
        this.mAlignBottom = (ImageView) this.mRootView.findViewById(R.id.alignBottom);
        this.mLlApplyToAll = (LinearLayout) this.mRootView.findViewById(R.id.ll_apply_to_all);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.fragment.BaseFragment
    public void initListener() {
        this.mAlignLeft.setOnClickListener(this);
        this.mAlignCenterHorizontal.setOnClickListener(this);
        this.mAlignRight.setOnClickListener(this);
        this.mAlignTop.setOnClickListener(this);
        this.mAlignCenterVertical.setOnClickListener(this);
        this.mAlignBottom.setOnClickListener(this);
        this.mLlApplyToAll.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.ll_apply_to_all) {
            if (id == R.id.alignBottom) { /*{ENCODED_INT: 2131296319}*/
                MessageEvent.sendEvent(5, 1016);
                return;
            } else if (id == R.id.alignCenterHorizontal) { /*{ENCODED_INT: 2131296320}*/
                MessageEvent.sendEvent(1, 1016);
                return;
            } else if (id == R.id.alignCenterVertical) { /*{ENCODED_INT: 2131296321}*/
                MessageEvent.sendEvent(4, 1016);
                return;
            } else if (id == R.id.alignLeft) { /*{ENCODED_INT: 2131296322}*/
                MessageEvent.sendEvent(0, 1016);
                return;
            } else if (id == R.id.alignRight) { /*{ENCODED_INT: 2131296323}*/
                MessageEvent.sendEvent(2, 1016);
                return;
            } else if (id == R.id.alignTop) { /*{ENCODED_INT: 2131296324}*/
                MessageEvent.sendEvent(3, 1016);
                return;
            }
            return;
        } else {
            MessageEvent.sendEvent(MessageEvent.MESSAGE_APPLY_ALL_CAPTION_POSITION);
        }
    }
}
