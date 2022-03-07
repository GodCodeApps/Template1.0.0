package com.meishe.myvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.CanvasBlurInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

public class MYCanvasBlur extends MYBaseView {
    private static final int GRADE_COUNT = 4;
    private MultiFunctionAdapter mAdapter;
    private View mApplyAll;
    private ArrayList<CanvasBlurInfo> mData;
    private ImageView mIvConfirm;
    private RecyclerView mRecyclerView;
    private int mSelectPosition;

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float getStrength(int i) {
        return (((float) i) * 1.0f) / 4.0f;
    }

    public void onClick(View view) {
    }

    public MYCanvasBlur(Context context) {
        super(context);
    }

    public MYCanvasBlur(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYCanvasBlur(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.canvas_blur_view, this);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mApplyAll = inflate.findViewById(R.id.ll_middle_menu);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
        this.mData = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            this.mData.add(new CanvasBlurInfo(i + ""));
        }
        this.mData.add(0, new CanvasBlurInfo(this.mContext.getResources().getString(R.string.top_menu_no)));
        this.mAdapter.addAll(this.mData);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dp2px(this.mContext, 3.0f), ScreenUtils.dp2px(getContext(), 12.0f)));
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initListener() {
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasBlur.AnonymousClass1 */

            public void onClick(View view) {
                MYCanvasBlur.this.hide();
            }
        });
        this.mApplyAll.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasBlur.AnonymousClass2 */

            public void onClick(View view) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_BLUR);
                MYCanvasBlur mYCanvasBlur = MYCanvasBlur.this;
                messageEvent.setFloatValue(mYCanvasBlur.getStrength(mYCanvasBlur.mSelectPosition));
                EventBus.getDefault().post(messageEvent);
            }
        });
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasBlur.AnonymousClass3 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                MYCanvasBlur.this.mSelectPosition = i;
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventType(1021);
                messageEvent.setFloatValue(MYCanvasBlur.this.getStrength(i));
                EventBus.getDefault().post(messageEvent);
            }
        });
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void onMessageEvent(MessageEvent messageEvent) {
        super.onMessageEvent(messageEvent);
        if (messageEvent.getEventType() == 1044) {
            float floatValue = messageEvent.getFloatValue();
            if (floatValue >= 0.0f) {
                this.mSelectPosition = (int) (((floatValue * 1.0f) * 4.0f) / 50.0f);
            } else {
                this.mSelectPosition = 0;
            }
            this.mAdapter.setSelectPosition(this.mSelectPosition);
        }
    }
}
