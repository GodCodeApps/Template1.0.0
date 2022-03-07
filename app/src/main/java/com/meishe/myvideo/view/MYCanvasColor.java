package com.meishe.myvideo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.CaptionColorInfo;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.util.ImageUtils;
import com.meishe.myvideoapp.R;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class MYCanvasColor extends MYBaseView {
    private MultiFunctionAdapter mAdapter;
    private View mApplyAll;
    private MYMultiColorView mColorPicker;
    private ImageView mIvConfirm;
    private int mSelectPosition = -1;

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initRecyclerView() {
    }

    public void onClick(View view) {
    }

    public MYCanvasColor(Context context) {
        super(context);
    }

    public MYCanvasColor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MYCanvasColor(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.canvas_color_view, this);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mColorPicker = (MYMultiColorView) inflate.findViewById(R.id.multi_color_view);
        this.mApplyAll = inflate.findViewById(R.id.ll_middle_menu);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initListener() {
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasColor.AnonymousClass1 */

            public void onClick(View view) {
                MYCanvasColor.this.hide();
            }
        });
        this.mApplyAll.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasColor.AnonymousClass2 */

            public void onClick(View view) {
                if (MYCanvasColor.this.mSelectPosition >= 0 && MYCanvasColor.this.mSelectPosition < MYCanvasColor.this.mAdapter.getItemCount()) {
                    BaseInfo item = MYCanvasColor.this.mAdapter.getItem(MYCanvasColor.this.mSelectPosition);
                    String parseViewToBitmap = ImageUtils.parseViewToBitmap(MYCanvasColor.this.mContext, view.findViewById(R.id.iv_color), item.getColorValue());
                    if (!TextUtils.isEmpty(parseViewToBitmap)) {
                        item.setFilePath(parseViewToBitmap);
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_APPLY_ALL_BACKGROUND_COLOR);
                        messageEvent.setBaseInfo(item);
                        EventBus.getDefault().post(messageEvent);
                    }
                }
            }
        });
        this.mAdapter = this.mColorPicker.getMultiFunctionAdapter();
        this.mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MYCanvasColor.AnonymousClass3 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                MYCanvasColor.this.mSelectPosition = i;
                BaseInfo item = MYCanvasColor.this.mAdapter.getItem(i);
                String parseViewToBitmap = ImageUtils.parseViewToBitmap(MYCanvasColor.this.mContext, view.findViewById(R.id.iv_color), item.getColorValue());
                if (!TextUtils.isEmpty(parseViewToBitmap)) {
                    item.setFilePath(parseViewToBitmap);
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setEventType(1019);
                    messageEvent.setBaseInfo(item);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        });
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void onMessageEvent(MessageEvent messageEvent) {
        super.onMessageEvent(messageEvent);
        if (messageEvent.getEventType() == 1042) {
            String strValue = messageEvent.getStrValue();
            int i = -1;
            if (!TextUtils.isEmpty(strValue) && !"nobackground.png".equals(strValue)) {
                List<BaseInfo> allItems = this.mColorPicker.getMultiFunctionAdapter().getAllItems();
                int i2 = 0;
                while (true) {
                    if (i2 >= allItems.size()) {
                        break;
                    }
                    String filePath = ((CaptionColorInfo) allItems.get(i2)).getFilePath();
                    if (!TextUtils.isEmpty(filePath) && filePath.contains(strValue)) {
                        i = i2;
                        break;
                    }
                    i2++;
                }
            }
            this.mSelectPosition = i;
            this.mAdapter.setSelectPosition(this.mSelectPosition);
        }
    }
}
