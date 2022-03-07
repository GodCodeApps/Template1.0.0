package com.meishe.myvideo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.CanvasStyleInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideoapp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class MYCanvasStyle extends MYBaseView {
    private static final String IMAGE_ASSETS_PATH = "background/image";
    private MultiFunctionAdapter mAdapter;
    private View mApplyAll;
    private List<BaseInfo> mData;
    private ImageView mIvConfirm;
    private RecyclerView mRecyclerView;
    private int mSelectPosition = -1;

    public void onClick(View view) {
    }

    public MYCanvasStyle(Context context) {
        super(context);
    }

    public MYCanvasStyle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYCanvasStyle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.canvas_style_view, this);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mApplyAll = inflate.findViewById(R.id.ll_middle_menu);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
        this.mData = getBackgroundImageList();
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
            /* class com.meishe.myvideo.view.MYCanvasStyle.AnonymousClass1 */

            public void onClick(View view) {
                MYCanvasStyle.this.hide();
            }
        });
        this.mApplyAll.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasStyle.AnonymousClass2 */

            public void onClick(View view) {
                if (MYCanvasStyle.this.mSelectPosition > 1 && MYCanvasStyle.this.mSelectPosition < MYCanvasStyle.this.mAdapter.getItemCount()) {
                    MessageEvent messageEvent = new MessageEvent();
                    BaseInfo item = MYCanvasStyle.this.mAdapter.getItem(MYCanvasStyle.this.mSelectPosition);
                    messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_IMAGE);
                    messageEvent.setBaseInfo(item);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        });
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasStyle.AnonymousClass3 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                MYCanvasStyle.this.mSelectPosition = i;
                MessageEvent messageEvent = new MessageEvent();
                if (i == 0) {
                    messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_CHANGE_SELECT_BACKGROUND_IMAGE);
                } else if (i == 1) {
                    messageEvent.setBaseInfo(null);
                    messageEvent.setEventType(1020);
                } else {
                    BaseInfo item = MYCanvasStyle.this.mAdapter.getItem(i);
                    messageEvent.setEventType(1020);
                    messageEvent.setBaseInfo(item);
                }
                EventBus.getDefault().post(messageEvent);
            }
        });
    }

    public List<BaseInfo> getBackgroundImageList() {
        try {
            String[] list = this.mContext.getAssets().list(IMAGE_ASSETS_PATH);
            if (list != null) {
                if (list.length > 0) {
                    ArrayList arrayList = new ArrayList();
                    CanvasStyleInfo canvasStyleInfo = new CanvasStyleInfo();
                    canvasStyleInfo.mIconRcsId = R.mipmap.ic_canvas_add_resource;
                    arrayList.add(canvasStyleInfo);
                    CanvasStyleInfo canvasStyleInfo2 = new CanvasStyleInfo();
                    canvasStyleInfo2.mIconRcsId = R.mipmap.ic_canvas_style_no;
                    arrayList.add(canvasStyleInfo2);
                    for (String str : list) {
                        CanvasStyleInfo canvasStyleInfo3 = new CanvasStyleInfo();
                        canvasStyleInfo3.setFilePath(str);
                        arrayList.add(canvasStyleInfo3);
                    }
                    return arrayList;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void onMessageEvent(MessageEvent messageEvent) {
        int i;
        super.onMessageEvent(messageEvent);
        if (messageEvent.getEventType() == 1043) {
            String strValue = messageEvent.getStrValue();
            if (!TextUtils.isEmpty(strValue)) {
                i = 0;
                while (true) {
                    if (i >= this.mData.size()) {
                        break;
                    }
                    String filePath = ((CanvasStyleInfo) this.mData.get(i)).getFilePath();
                    if (!TextUtils.isEmpty(filePath) && filePath.contains(strValue)) {
                        break;
                    }
                    i++;
                }
            }
            i = 1;
            this.mSelectPosition = i;
            this.mAdapter.setSelectPosition(this.mSelectPosition);
        }
    }
}
