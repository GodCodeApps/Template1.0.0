package com.meishe.myvideo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.MeicamTheme;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.MeicamVideoFx;
import com.meishe.engine.bean.MeicamVideoTrack;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.data.MeicamAdjustData;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.event.MessageEvent;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideo.util.ui.ToastUtil;
import com.meishe.myvideo.view.MYSeekBarView;
import com.meishe.myvideoapp.R;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class MYWidthConfirmMenuView extends MYBaseView {
    public static final int TYPE_MENU_CLIP_ADJUST = 2;
    public static final int TYPE_MENU_CLIP_FILTER = 1;
    public static final int TYPE_MENU_EFFECT = 3;
    public static final int TYPE_MENU_THEME = 5;
    public static final int TYPE_MENU_TIMELINE_ADJUST = 6;
    public static final int TYPE_MENU_TIMELINE_FILTER = 4;
    private BaseInfo mBaseInfo;
    private MeicamVideoClip mClipInfo;
    private ImageView mIvApplyToAllView;
    private ImageView mIvConfirm;
    private RecyclerView mLLWithConfirmRecyclerView;
    private LinearLayout mLlApplyToAllAndResetView;
    private LinearLayout mLlResetView;
    private LinearLayout mLlTopSeekMenu;
    private View mResetLeftLine;
    private RelativeLayout mRlWithConfirmMenu;
    private MYSeekBarView mSeekBarView;
    private TextView mTvApplyToAllView;
    private TextView mTvContent;
    private TextView mTvReset;
    private TextView mTvTimelineResetView;
    private int mType;
    private MultiFunctionAdapter mWidthConfirmMultiFunctionAdapter;

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initData() {
    }

    public MYWidthConfirmMenuView(Context context) {
        super(context);
    }

    public MYWidthConfirmMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void init() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.width_confirm_menu_view, this);
        this.mRlWithConfirmMenu = (RelativeLayout) inflate.findViewById(R.id.rl_with_confirm_menu);
        this.mLLWithConfirmRecyclerView = (RecyclerView) inflate.findViewById(R.id.width_confirm_menu_recycleView);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mLlTopSeekMenu = (LinearLayout) inflate.findViewById(R.id.ll_top_seek_menu);
        this.mTvReset = (TextView) inflate.findViewById(R.id.tv_reset);
        this.mResetLeftLine = inflate.findViewById(R.id.reset_left_line);
        this.mIvApplyToAllView = (ImageView) inflate.findViewById(R.id.iv_apply_all);
        this.mTvApplyToAllView = (TextView) inflate.findViewById(R.id.tv_apply_all);
        this.mSeekBarView = (MYSeekBarView) inflate.findViewById(R.id.view_seek_bar);
        this.mLlApplyToAllAndResetView = (LinearLayout) inflate.findViewById(R.id.ll_apply_to_all_and_reset);
        this.mLlResetView = (LinearLayout) inflate.findViewById(R.id.ll_reset);
        this.mTvTimelineResetView = (TextView) inflate.findViewById(R.id.tv_timeline_reset);
        this.mSeekBarView.setStartTextVisible(false);
        this.mSeekBarView.setEndTextVisible(false);
    }

    public BaseInfo getBaseInfo() {
        return this.mBaseInfo;
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initRecyclerView() {
        this.mWidthConfirmMultiFunctionAdapter = new MultiFunctionAdapter(this.mContext, this.mLLWithConfirmRecyclerView);
        this.mLLWithConfirmRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mLLWithConfirmRecyclerView.setAdapter(this.mWidthConfirmMultiFunctionAdapter);
        this.mLLWithConfirmRecyclerView.addItemDecoration(new SpaceItemDecoration(18, 18));
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void initListener() {
        this.mIvConfirm.setOnClickListener(this);
        this.mTvReset.setOnClickListener(this);
        this.mIvApplyToAllView.setOnClickListener(this);
        this.mTvApplyToAllView.setOnClickListener(this);
        this.mTvTimelineResetView.setOnClickListener(this);
        this.mWidthConfirmMultiFunctionAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MYWidthConfirmMenuView.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BaseInfo item = MYWidthConfirmMenuView.this.mWidthConfirmMultiFunctionAdapter.getItem(i);
                MYWidthConfirmMenuView.this.mBaseInfo = item;
                MYWidthConfirmMenuView.this.mSeekBarView.setName(item.mName);
                if (MYWidthConfirmMenuView.this.mType == 1 || MYWidthConfirmMenuView.this.mType == 4) {
                    if (MYWidthConfirmMenuView.this.getResources().getString(R.string.top_menu_no).equals(item.mName)) {
                        MYWidthConfirmMenuView.this.hideSeekbar();
                        if (MYWidthConfirmMenuView.this.mType == 1) {
                            MessageEvent.sendEvent(item, (int) MessageEvent.MESSAGE_REMOVE_CLIP_FILTER);
                        } else if (MYWidthConfirmMenuView.this.mType == 4) {
                            MessageEvent.sendEvent(item, (int) MessageEvent.MESSAGE_REMOVE_TIMELINE_FILTER);
                        }
                    } else if (MYWidthConfirmMenuView.this.getResources().getString(R.string.more).equals(item.mName)) {
                        if (MYWidthConfirmMenuView.this.mWidthConfirmMultiFunctionAdapter.getSelectPosition() <= 1) {
                            MYWidthConfirmMenuView.this.hideSeekbar();
                        }
                        MessageEvent.sendEvent(item, (int) MessageEvent.MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT);
                    } else {
                        MYWidthConfirmMenuView.this.showFilterSeekbar();
                        MYWidthConfirmMenuView.this.setSeekPress(item);
                        MessageEvent.sendEvent(item, (int) MessageEvent.MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT);
                    }
                } else if (MYWidthConfirmMenuView.this.mType != 2 && MYWidthConfirmMenuView.this.mType != 6) {
                    MYWidthConfirmMenuView.this.hideSeekbar();
                    MessageEvent.sendEvent(item, (int) MessageEvent.MESSAGE_TYPE_WIDTH_CONFIRM_EFFECT);
                } else if (MYWidthConfirmMenuView.this.getResources().getString(R.string.top_menu_no).equals(item.mName)) {
                    MYWidthConfirmMenuView.this.hideSeekbar();
                } else {
                    MYWidthConfirmMenuView.this.showAdjustSeekBar();
                    MYWidthConfirmMenuView.this.setSeekPress(item);
                }
            }
        });
        this.mSeekBarView.setListener(new MYSeekBarView.OnSeekBarListener() {
            /* class com.meishe.myvideo.view.MYWidthConfirmMenuView.AnonymousClass2 */

            @Override // com.meishe.myvideo.view.MYSeekBarView.OnSeekBarListener
            public void onStopTrackingTouch(int i, String str) {
                if (MYWidthConfirmMenuView.this.mType == 2 || MYWidthConfirmMenuView.this.mType == 6) {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_CHANGE_ADJUST_FINISH);
                    EventBus.getDefault().post(messageEvent);
                } else if (MYWidthConfirmMenuView.this.mType == 1) {
                    MessageEvent messageEvent2 = new MessageEvent();
                    messageEvent2.setEventType(MessageEvent.MESSAGE_TYPE_CHANGE_CLIP_FILTER_FINISH);
                    EventBus.getDefault().post(messageEvent2);
                } else if (MYWidthConfirmMenuView.this.mType == 4) {
                    MessageEvent messageEvent3 = new MessageEvent();
                    messageEvent3.setEventType(MessageEvent.MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_FINISH);
                    EventBus.getDefault().post(messageEvent3);
                }
            }

            @Override // com.meishe.myvideo.view.MYSeekBarView.OnSeekBarListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (MYWidthConfirmMenuView.this.mType == 2 || MYWidthConfirmMenuView.this.mType == 6) {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setProgress(i);
                    messageEvent.setStrValue(MYWidthConfirmMenuView.this.mBaseInfo.mName);
                    messageEvent.setEventType(MessageEvent.MESSAGE_TYPE_ADJUST);
                    EventBus.getDefault().post(messageEvent);
                } else if (z) {
                    MessageEvent messageEvent2 = new MessageEvent();
                    int i2 = -1;
                    if (MYWidthConfirmMenuView.this.mType == 1) {
                        i2 = MessageEvent.MESSAGE_TYPE_CHANGE_CLIP_FILTER_PROGRESS;
                        messageEvent2.setFloatValue((((float) i) * 1.0f) / MYWidthConfirmMenuView.this.mSeekBarView.getMaxProgress());
                    } else if (MYWidthConfirmMenuView.this.mType == 4) {
                        i2 = MessageEvent.MESSAGE_TYPE_CHANGE_TIMELINE_FILTER_PROGRESS;
                        messageEvent2.setFloatValue((((float) i) * 1.0f) / MYWidthConfirmMenuView.this.mSeekBarView.getMaxProgress());
                    }
                    messageEvent2.setEventType(i2);
                    EventBus.getDefault().post(messageEvent2);
                }
            }
        });
        this.mRlWithConfirmMenu.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.MYWidthConfirmMenuView.AnonymousClass3 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @TargetApi(26)
    public void setProgress(float f) {
        MYSeekBarView mYSeekBarView = this.mSeekBarView;
        mYSeekBarView.setSeekProgress((int) (f * (mYSeekBarView.getMaxProgress() - this.mSeekBarView.getMinProgress())));
    }

    public void setClipInfo(MeicamVideoClip meicamVideoClip) {
        this.mClipInfo = meicamVideoClip;
    }

    @TargetApi(26)
    public void setSeekPress(BaseInfo baseInfo) {
        int i = this.mType;
        float f = 0.0f;
        if (i == 4) {
            MeicamVideoFx filterFx = TimelineData.getInstance().getFilterFx();
            if (filterFx != null) {
                f = filterFx.getIntensity();
            }
            MYSeekBarView mYSeekBarView = this.mSeekBarView;
            mYSeekBarView.setSeekProgress((int) (f * (mYSeekBarView.getMaxProgress() - this.mSeekBarView.getMinProgress())));
        } else if (i == 1) {
            float filterIntensity = this.mClipInfo.getFilterIntensity(MeicamVideoFx.SUB_TYPE_CLIP_FILTER);
            MYSeekBarView mYSeekBarView2 = this.mSeekBarView;
            mYSeekBarView2.setSeekProgress((int) (filterIntensity * (mYSeekBarView2.getMaxProgress() - this.mSeekBarView.getMinProgress())));
        } else if (i == 2 || i == 6) {
            MeicamAdjustData meicamAdjustData = null;
            int i2 = this.mType;
            if (i2 == 2) {
                meicamAdjustData = this.mClipInfo.getMeicamAdjustData();
            } else if (i2 == 6) {
                meicamAdjustData = TimelineData.getInstance().getMeicamAdjustData();
            }
            if (meicamAdjustData == null) {
                this.mSeekBarView.setSeekProgress(50);
                return;
            }
            if (getResources().getString(R.string.adjust_amount).equals(baseInfo.mName)) {
                f = meicamAdjustData.getAmount();
            } else if (getResources().getString(R.string.adjust_degree).equals(baseInfo.mName)) {
                f = meicamAdjustData.getDegree();
            } else if (getResources().getString(R.string.adjust_black_point).equals(baseInfo.mName)) {
                f = meicamAdjustData.getBlackPoint();
            } else if (getResources().getString(R.string.adjust_brightness).equals(baseInfo.mName)) {
                f = meicamAdjustData.getBrightness();
            } else if (getResources().getString(R.string.adjust_contrast).equals(baseInfo.mName)) {
                f = meicamAdjustData.getContrast();
            } else if (getResources().getString(R.string.adjust_saturation).equals(baseInfo.mName)) {
                f = meicamAdjustData.getSaturation();
            } else if (getResources().getString(R.string.adjust_highlight).equals(baseInfo.mName)) {
                f = meicamAdjustData.getHighlight();
            } else if (getResources().getString(R.string.adjust_shadow).equals(baseInfo.mName)) {
                f = meicamAdjustData.getShadow();
            } else if (getResources().getString(R.string.adjust_temperature).equals(baseInfo.mName)) {
                f = meicamAdjustData.getTemperature();
            } else if (getResources().getString(R.string.adjust_tint).equals(baseInfo.mName)) {
                f = meicamAdjustData.getTint();
            }
            this.mSeekBarView.setSeekProgress((int) ((f + 1.0f) * 50.0f));
        }
    }

    public void onClick(View view) {
        int id = view.getId();/*{ENCODED_INT: 2131296511}*/
        if (id == R.id.iv_apply_all || id == R.id.tv_apply_all) { /*{ENCODED_INT: 2131296823}*/
            if (this.mType == 1) {
                MessageEvent.sendEvent(0, (int) MessageEvent.MESSAGE_APPLY_ALL_FILTER);
                return;
            }
            MeicamVideoClip meicamVideoClip = this.mClipInfo;
            if (meicamVideoClip != null) {
                MeicamAdjustData meicamAdjustData = meicamVideoClip.getMeicamAdjustData();
                List<MeicamVideoTrack> meicamVideoTrackList = TimelineData.getInstance().getMeicamVideoTrackList();
                if (!CollectionUtils.isEmpty(meicamVideoTrackList)) {
                    for (int i = 0; i < meicamVideoTrackList.size(); i++) {
                        List<ClipInfo> clipInfoList = meicamVideoTrackList.get(i).getClipInfoList();
                        if (!CollectionUtils.isEmpty(clipInfoList)) {
                            for (int i2 = 0; i2 < clipInfoList.size(); i2++) {
                                ClipInfo clipInfo = clipInfoList.get(i2);
                                if ((clipInfo instanceof MeicamVideoClip) && clipInfo != this.mClipInfo) {
                                    MeicamVideoClip meicamVideoClip2 = (MeicamVideoClip) clipInfo;
                                    meicamVideoClip2.getMeicamAdjustData().setAdjustData(meicamAdjustData);
                                    meicamVideoClip2.setAdjustEffects();
                                }
                            }
                        }
                    }
                }
                ToastUtil.showCenterToast(getContext(), (int) R.string.has_been_apply_to_all);
                return;
            }
            return;
        } else if (id == R.id.iv_confirm) { /*{ENCODED_INT: 2131296520}*/
            hide();
            MessageEvent.sendEvent(this.mType, 1024);
            return;
        } else if (id == R.id.tv_reset) { /*{ENCODED_INT: 2131296859}*/
            MeicamVideoClip meicamVideoClip3 = this.mClipInfo;
            if (meicamVideoClip3 != null) {
                meicamVideoClip3.getMeicamAdjustData().reset();
                this.mClipInfo.setAdjustEffects();
                this.mSeekBarView.setSeekProgress(50);
                EditorEngine.getInstance().seekTimeline(0);
                return;
            }
            return;
        } else if (id == R.id.tv_timeline_reset) { /*{ENCODED_INT: 2131296868}*/
            MeicamAdjustData meicamAdjustData2 = TimelineData.getInstance().getMeicamAdjustData();
            meicamAdjustData2.reset();
            TimelineData.getInstance().setMeicamAdjustData(meicamAdjustData2);
            this.mSeekBarView.setSeekProgress(50);
            EditorEngine.getInstance().seekTimeline(0);
            return;
        }
        return;
    }

    public void show(String str, List<BaseInfo> list, MeicamVideoClip meicamVideoClip) {
        this.mClipInfo = meicamVideoClip;
        show();
        this.mTvContent.setText(str);
        this.mWidthConfirmMultiFunctionAdapter.addAll(list);
        String currentState = StateManager.getInstance().getCurrentState();
        if (getResources().getString(R.string.sub_menu_name_edit_filter).equals(str)) {
            if (this.mContext.getResources().getString(R.string.main_menu_name_filter).equals(currentState) || this.mContext.getResources().getString(R.string.main_menu_name_adjust).equals(currentState)) {
                this.mType = 4;
                Log.e("TAG", "show: 从滤镜过来的，是timeline编辑");
            } else {
                this.mType = 1;
                Log.e("TAG", "show: 从编辑过来的，是单段编辑");
            }
            int filterSelectPosition = getFilterSelectPosition();
            if (filterSelectPosition == 1) {
                hideSeekbar();
            } else {
                showFilterSeekbar();
                setSeekPress(list.get(filterSelectPosition));
            }
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(filterSelectPosition);
        } else if (getResources().getString(R.string.sub_menu_name_edit_adjust).equals(str)) {
            if (this.mContext.getResources().getString(R.string.main_menu_name_filter).equals(currentState) || this.mContext.getResources().getString(R.string.main_menu_name_adjust).equals(currentState)) {
                this.mType = 6;
            } else {
                this.mType = 2;
            }
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(-1);
        } else if (getResources().getString(R.string.effect_lively).equals(str) || getResources().getString(R.string.effect_shaking).equals(str) || getResources().getString(R.string.effect_dream).equals(str) || getResources().getString(R.string.effect_frame).equals(str)) {
            Log.e("TAG", "show: TYPE_MENU_EFFECT");
            this.mType = 3;
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(-1);
        } else if (getResources().getString(R.string.main_menu_name_theme).equals(str)) {
            this.mType = 5;
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(getThemeSelectPosition());
        } else {
            Log.e("TAG", "show: 00000");
            this.mType = 0;
        }
    }

    private int getFilterSelectPosition() {
        MeicamVideoFx meicamVideoFx;
        String str;
        MeicamVideoClip meicamVideoClip;
        int i = this.mType;
        if (i == 4) {
            meicamVideoFx = TimelineData.getInstance().getFilterFx();
        } else {
            meicamVideoFx = (i != 1 || (meicamVideoClip = this.mClipInfo) == null) ? null : meicamVideoClip.getVideoFx(MeicamVideoFx.SUB_TYPE_CLIP_FILTER);
        }
        if (meicamVideoFx == null) {
            return 1;
        }
        String desc = meicamVideoFx.getDesc();
        boolean equals = "builtin".equals(meicamVideoFx.getType());
        List<BaseInfo> allItems = this.mWidthConfirmMultiFunctionAdapter.getAllItems();
        for (int i2 = 0; i2 < allItems.size(); i2++) {
            BaseInfo baseInfo = allItems.get(i2);
            if (equals) {
                str = baseInfo.getName();
            } else {
                str = baseInfo.getPackageId();
            }
            if (desc.equals(str)) {
                return i2;
            }
        }
        return 1;
    }

    private int getThemeSelectPosition() {
        MeicamTheme meicamTheme = TimelineData.getInstance().getMeicamTheme();
        if (meicamTheme == null) {
            return 1;
        }
        String themePackageId = meicamTheme.getThemePackageId();
        if (TextUtils.isEmpty(themePackageId)) {
            return 1;
        }
        List<BaseInfo> allItems = this.mWidthConfirmMultiFunctionAdapter.getAllItems();
        for (int i = 0; i < allItems.size(); i++) {
            if (themePackageId.equals(allItems.get(i).getPackageId())) {
                return i;
            }
        }
        return 1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showFilterSeekbar() {
        this.mLlTopSeekMenu.setVisibility(0);
        int i = this.mType;
        if (i == 1) {
            this.mLlApplyToAllAndResetView.setVisibility(0);
        } else if (i == 4) {
            this.mLlApplyToAllAndResetView.setVisibility(8);
        }
        this.mLlResetView.setVisibility(8);
        this.mTvReset.setVisibility(8);
        this.mResetLeftLine.setVisibility(8);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showAdjustSeekBar() {
        this.mLlTopSeekMenu.setVisibility(0);
        int i = this.mType;
        if (i == 2) {
            this.mLlApplyToAllAndResetView.setVisibility(0);
            this.mTvReset.setVisibility(0);
            this.mResetLeftLine.setVisibility(0);
            this.mLlResetView.setVisibility(8);
        } else if (i == 6) {
            this.mLlApplyToAllAndResetView.setVisibility(8);
            this.mTvReset.setVisibility(8);
            this.mResetLeftLine.setVisibility(8);
            this.mLlResetView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void hideSeekbar() {
        this.mLlTopSeekMenu.setVisibility(8);
        this.mLlApplyToAllAndResetView.setVisibility(8);
        this.mLlResetView.setVisibility(8);
    }

    public void updateMenuView(int i) {
        List<BaseInfo> list;
        if (i == 1) {
            list = MenuDataManager.getData(getResources().getString(R.string.main_menu_name_theme), this.mContext);
        } else if (i != 2) {
            switch (i) {
                case 18:
                    list = MenuDataManager.getData(getResources().getString(R.string.effect_frame), this.mContext);
                    break;
                case 19:
                    list = MenuDataManager.getData(getResources().getString(R.string.effect_dream), this.mContext);
                    break;
                case 20:
                    list = MenuDataManager.getData(getResources().getString(R.string.effect_lively), this.mContext);
                    break;
                case 21:
                    list = MenuDataManager.getData(getResources().getString(R.string.effect_shaking), this.mContext);
                    break;
                default:
                    list = null;
                    break;
            }
        } else {
            list = MenuDataManager.getData(getResources().getString(R.string.main_menu_name_filter), this.mContext);
        }
        if (list != null) {
            this.mWidthConfirmMultiFunctionAdapter.addAll(list);
        }
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void hide() {
        super.hide();
        this.mLlTopSeekMenu.setVisibility(8);
    }

    @Override // com.meishe.myvideo.view.MYBaseView
    public void onMessageEvent(MessageEvent messageEvent) {
        super.onMessageEvent(messageEvent);
        int eventType = messageEvent.getEventType();
        if (eventType == 1036) {
            setProgress(messageEvent.getFloatValue());
        } else if (eventType == 1037) {
            updateSelectPosition();
        }
    }

    public void setSelection(int i) {
        this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(i);
    }

    private void updateSelectPosition() {
        int i = this.mType;
        if (i == 4 || i == 1) {
            int filterSelectPosition = getFilterSelectPosition();
            if (filterSelectPosition == 1) {
                hideSeekbar();
            } else {
                showFilterSeekbar();
                setSeekPress(this.mWidthConfirmMultiFunctionAdapter.getAllItems().get(filterSelectPosition));
            }
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(filterSelectPosition);
        } else if (i == 2 || i == 6) {
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(-1);
        } else if (i == 3) {
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(-1);
        } else if (i == 5) {
            this.mWidthConfirmMultiFunctionAdapter.setSelectPosition(getThemeSelectPosition());
        }
    }
}
