package com.meishe.myvideo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.MeicamTheme;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.adapter.BaseRecyclerAdapter;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.menu.EditMenuInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.manager.MenuDataManager;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.util.Constants;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.ui.DialogUtil;
import com.meishe.myvideo.view.CommonDialog;
import com.meishe.myvideo.view.RatioView;
import com.meishe.myvideoapp.R;
import java.util.List;

public class MenuView extends RelativeLayout implements RatioView.OnRatioListener {
    private static final int AUDIO_EDIT_MENU_TYPE = 4;
    public static final int CONTAIN_EXPAND_TRANS_TYPE = 1;
    private static final int LAST_MENU_TYPE_ADJUST = 6;
    private static final int LAST_MENU_TYPE_EFFECT = 2;
    private static final int LAST_MENU_TYPE_FILTER = 5;
    private static final int LAST_MENU_TYPE_RATIO = 3;
    private static final int TYPE_CAPTION_EDIT_MENU = 7;
    private static final int TYPE_COMPOUND_CAPTION_EDIT_MENU = 9;
    private static final int TYPE_EFFECT_EDIT_MENU = 10;
    private static final int TYPE_PIP_IMAGE_EDIT_MENU = 12;
    private static final int TYPE_PIP_VIDEO_EDIT_MENU = 11;
    private static final int TYPE_STICKER_EDIT_MENU = 8;
    private View mBackBtn;
    private Context mContext;
    private MYSeekBarView mEditorChangeSeekBar;
    private LinearLayout mLinearSubRecy;
    private LinearLayout mMMenuMiddle;
    private List<BaseInfo> mMenuData;
    private LinearLayout mMenuTop;
    private MultiFunctionAdapter mMultiFunctionAdapter;
    private MultiFunctionAdapter mMultiSubFunctionAdapter;
    private OnMenuClickListener mOnMenuClickListener;
    private RatioView mRatioView;
    private RecyclerView mRecyclerView;
    private RecyclerView mSubRecyclerView;
    private TabLayout mTablayout;
    private int mType;

    public interface OnMenuClickListener {
        void changeBottomMenu(String str, int i);

        void onAudioMenuClicked(View view, String str, BaseInfo baseInfo);

        void onBackClick();

        void onCaptionEditMenuClicked(View view, String str, BaseInfo baseInfo);

        void onComCaptionEditMenuClicked(View view, String str, BaseInfo baseInfo);

        void onEffectEditMenuClicked(View view, String str, BaseInfo baseInfo);

        void onMenuClicked(View view, String str, BaseInfo baseInfo, List<BaseInfo> list);

        void onPipEditMenuClicked(View view, String str, BaseInfo baseInfo);

        void onRatioClick(int i);

        void onStickerEditMenuClicked(View view, String str, BaseInfo baseInfo);

        void onSwitchMainMenu();
    }

    public MenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
        initData();
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.mOnMenuClickListener = onMenuClickListener;
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_menu, this);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.menu_recycleView);
        this.mSubRecyclerView = (RecyclerView) inflate.findViewById(R.id.sub_menu_recycleView);
        this.mLinearSubRecy = (LinearLayout) inflate.findViewById(R.id.linear_sub_recy);
        this.mBackBtn = inflate.findViewById(R.id.back_btn);
        this.mMenuTop = (LinearLayout) inflate.findViewById(R.id.ll_menu_top);
        this.mTablayout = (TabLayout) inflate.findViewById(R.id.tl_menu_top);
        this.mMMenuMiddle = (LinearLayout) inflate.findViewById(R.id.ll_middle_menu);
        this.mEditorChangeSeekBar = (MYSeekBarView) inflate.findViewById(R.id.editor_change_seek_bar);
        this.mRatioView = (RatioView) inflate.findViewById(R.id.ratio_view);
        initListener();
    }

    private void initListener() {
        StateManager.getInstance().setOnStateChangeListener(new StateManager.OnStateChangeListener() {
            /* class com.meishe.myvideo.view.MenuView.AnonymousClass1 */

            @Override // com.meishe.myvideo.manager.StateManager.OnStateChangeListener
            public void onChangeState(String str, int i) {
                if (!TextUtils.isEmpty(str)) {
                    List<BaseInfo> list = null;
                    char c = 65535;
                    MenuView.this.mType = -1;
                    switch (str.hashCode()) {
                        case 659901:
                            if (str.equals(Constants.STATE_THEME)) {
                                c = 1;
                                break;
                            }
                            break;
                        case 749022:
                            if (str.equals(Constants.STATE_CAPTION)) {
                                c = 5;
                                break;
                            }
                            break;
                        case 777601:
                            if (str.equals(Constants.STATE_DUBBING)) {
                                c = '\t';
                                break;
                            }
                            break;
                        case 918264:
                            if (str.equals(Constants.STATE_FILTER)) {
                                c = 2;
                                break;
                            }
                            break;
                        case 934383:
                            if (str.equals(Constants.STATE_FX)) {
                                c = 7;
                                break;
                            }
                            break;
                        case 1045307:
                            if (str.equals(Constants.STATE_EDIT)) {
                                c = 3;
                                break;
                            }
                            break;
                        case 1153028:
                            if (str.equals(Constants.STATE_STICKER)) {
                                c = 4;
                                break;
                            }
                            break;
                        case 1244926:
                            if (str.equals(Constants.STATE_MUSIC)) {
                                c = '\b';
                                break;
                            }
                            break;
                        case 20313716:
                            if (str.equals(Constants.STATE_MAIN_MENU)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 29490985:
                            if (str.equals(Constants.STATE_PIC_IN_PIC)) {
                                c = '\n';
                                break;
                            }
                            break;
                        case 988199586:
                            if (str.equals(Constants.STATE_COMPOUND_CAPTION)) {
                                c = 6;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            MenuView.this.switchToMainMenu();
                            return;
                        case 1:
                            List<BaseInfo> data = MenuDataManager.getData(MenuView.this.getResources().getString(R.string.main_menu_name_theme), MenuView.this.mContext);
                            if (data != null) {
                                MenuView menuView = MenuView.this;
                                menuView.showBottomMenu(menuView.getResources().getString(R.string.main_menu_name_theme), data, 0, i);
                                return;
                            }
                            return;
                        case 2:
                        default:
                            return;
                        case 3:
                            MeicamVideoClip selectedMeicamClipInfo = TimelineData.getInstance().getSelectedMeicamClipInfo();
                            if (selectedMeicamClipInfo != null) {
                                String videoType = selectedMeicamClipInfo.getVideoType();
                                if (CommonData.CLIP_VIDEO.equals(videoType)) {
                                    list = MenuDataManager.getMainMenuData(MenuView.this.mContext.getString(R.string.main_menu_name_edit_video), MenuView.this.mContext);
                                } else if (CommonData.CLIP_IMAGE.equals(videoType)) {
                                    list = MenuDataManager.getData(MenuView.this.getResources().getString(R.string.main_menu_name_edit), MenuView.this.mContext);
                                }
                            }
                            if (list != null) {
                                MenuView menuView2 = MenuView.this;
                                menuView2.showBottomMenu(menuView2.getResources().getString(R.string.main_menu_name_edit), list, 0, i);
                                return;
                            }
                            return;
                        case 4:
                        case 5:
                        case 6:
                            List<BaseInfo> mainMenuData = MenuDataManager.getMainMenuData(MenuView.this.getResources().getString(R.string.main_menu_name_sticker), MenuView.this.mContext);
                            if (mainMenuData != null) {
                                MenuView menuView3 = MenuView.this;
                                menuView3.showBottomMenu(menuView3.getResources().getString(R.string.main_menu_name_sticker), mainMenuData, 0, i);
                                return;
                            }
                            return;
                        case 7:
                            List<BaseInfo> mainMenuData2 = MenuDataManager.getMainMenuData(MenuView.this.getResources().getString(R.string.main_menu_name_fx), MenuView.this.mContext);
                            if (mainMenuData2 != null) {
                                MenuView menuView4 = MenuView.this;
                                menuView4.showBottomMenu(menuView4.getResources().getString(R.string.main_menu_name_fx), mainMenuData2, 0, i);
                                return;
                            }
                            return;
                        case '\b':
                        case '\t':
                            List<BaseInfo> mainMenuData3 = MenuDataManager.getMainMenuData(MenuView.this.getResources().getString(R.string.main_menu_name_music), MenuView.this.mContext);
                            if (mainMenuData3 != null) {
                                MenuView menuView5 = MenuView.this;
                                menuView5.showBottomMenu(menuView5.getResources().getString(R.string.main_menu_name_music), mainMenuData3, 0, i);
                                return;
                            }
                            return;
                        case '\n':
                            List<BaseInfo> mainMenuData4 = MenuDataManager.getMainMenuData(MenuView.this.getResources().getString(R.string.main_menu_name_picture_in_picture), MenuView.this.mContext);
                            if (mainMenuData4 != null) {
                                MenuView menuView6 = MenuView.this;
                                menuView6.showBottomMenu(menuView6.getResources().getString(R.string.main_menu_name_picture_in_picture), mainMenuData4, 0, i);
                                return;
                            }
                            return;
                    }
                }
            }
        });
        this.mRatioView.setOnRatioListener(this);
        this.mBackBtn.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MenuView.AnonymousClass2 */

            /* JADX WARNING: Removed duplicated region for block: B:28:0x01d2  */
            /* JADX WARNING: Removed duplicated region for block: B:32:0x0217  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onClick(android.view.View r7) {
                /*
                // Method dump skipped, instructions count: 688
                */
                
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.view.MenuView.AnonymousClass2.onClick(android.view.View):void");
            }
        });
    }

    private void initData() {
        initRecyclerView();
        initSubRecyclerView();
    }

    private void initSubRecyclerView() {
        this.mMultiSubFunctionAdapter = new MultiFunctionAdapter(this.mContext, this.mSubRecyclerView);
        this.mSubRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mSubRecyclerView.setAdapter(this.mMultiSubFunctionAdapter);
        this.mMultiSubFunctionAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MenuView.AnonymousClass3 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BaseInfo item = MenuView.this.mMultiSubFunctionAdapter.getItem(i);
                List<BaseInfo> data = MenuDataManager.getData(item.mName, MenuView.this.mContext);
                if (item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_frame)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_shaking)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_lively)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_dream)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.sub_menu_name_edit_filter)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.sub_menu_name_edit_adjust))) {
                    if (MenuView.this.mOnMenuClickListener != null) {
                        MenuView.this.mOnMenuClickListener.onMenuClicked(view, item.mName, item, data);
                    }
                } else if (MenuView.this.mOnMenuClickListener != null && MenuView.this.mType == 4) {
                    MenuView.this.mOnMenuClickListener.onAudioMenuClicked(view, item.mName, item);
                } else if (MenuView.this.mOnMenuClickListener != null && MenuView.this.mType == 7) {
                    MenuView.this.mOnMenuClickListener.onCaptionEditMenuClicked(view, item.mName, item);
                } else if (MenuView.this.mOnMenuClickListener != null && MenuView.this.mType == 9) {
                    MenuView.this.mOnMenuClickListener.onComCaptionEditMenuClicked(view, item.mName, item);
                } else if (MenuView.this.mOnMenuClickListener != null && MenuView.this.mType == 8) {
                    MenuView.this.mOnMenuClickListener.onStickerEditMenuClicked(view, item.mName, item);
                } else if (MenuView.this.mOnMenuClickListener != null && MenuView.this.mType == 10) {
                    MenuView.this.mOnMenuClickListener.onEffectEditMenuClicked(view, item.mName, item);
                } else if (MenuView.this.mOnMenuClickListener != null && (MenuView.this.mType == 11 || MenuView.this.mType == 12)) {
                    MenuView.this.mOnMenuClickListener.onPipEditMenuClicked(view, item.mName, item);
                } else if (!item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_caption)) && !item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_sticker)) && !item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_water_mark)) && !item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_com_caption)) && !item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_dubbing)) && data != null) {
                    MenuView.this.showBottomMenu(item.mName, data, 0, 1);
                } else if (MenuView.this.mOnMenuClickListener != null) {
                    MenuView.this.mOnMenuClickListener.onMenuClicked(view, item.mName, item, null);
                }
            }
        });
    }

    private void initRecyclerView() {
        this.mMenuData = MenuDataManager.getMainMenuData(this.mContext);
        this.mMultiFunctionAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mRecyclerView.setAdapter(this.mMultiFunctionAdapter);
        this.mRecyclerView.addItemDecoration(new SpaceItemDecoration(18, 18));
        this.mMultiFunctionAdapter.addAll(this.mMenuData);
        this.mMultiFunctionAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.MenuView.AnonymousClass4 */

            @Override // com.meishe.myvideo.adapter.BaseRecyclerAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BaseInfo item = MenuView.this.mMultiFunctionAdapter.getItem(i);
                if (item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_frame)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_shaking)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_lively)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.effect_dream)) || item.mName.equals(MenuView.this.mContext.getResources().getString(R.string.main_menu_name_fx))) {
                    MenuView.this.mType = 2;
                }
                MenuView.this.handleMainMenuClick(view, item);
            }
        });
    }

    public void performMenuClick(int i) {
        this.mMultiFunctionAdapter.setPositionClick(i);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleMainMenuClick(View view, BaseInfo baseInfo) {
        OnMenuClickListener onMenuClickListener;
        OnMenuClickListener onMenuClickListener2;
        MeicamVideoClip selectedMeicamClipInfo;
        StateManager.getInstance().setCurrentState(baseInfo.mName);
        OnMenuClickListener onMenuClickListener3 = this.mOnMenuClickListener;
        if (onMenuClickListener3 != null) {
            onMenuClickListener3.changeBottomMenu(baseInfo.mName, 1);
        }
        List<BaseInfo> mainMenuData = MenuDataManager.getMainMenuData(baseInfo.mName, this.mContext);
        if (this.mContext.getString(R.string.main_menu_name_edit).equals(baseInfo.mName) && (selectedMeicamClipInfo = TimelineData.getInstance().getSelectedMeicamClipInfo()) != null && CommonData.CLIP_VIDEO.equals(selectedMeicamClipInfo.getVideoType())) {
            mainMenuData = MenuDataManager.getMainMenuData(this.mContext.getString(R.string.main_menu_name_edit_video), this.mContext);
        }
        if (baseInfo.mName.equals(this.mContext.getResources().getString(R.string.main_menu_name_ratio))) {
            this.mType = 3;
            showRatioView();
            this.mOnMenuClickListener.onMenuClicked(view, baseInfo.mName, baseInfo, mainMenuData);
        } else if (baseInfo.mName.equals(this.mContext.getString(R.string.sub_menu_name_edit_cut))) {
            OnMenuClickListener onMenuClickListener4 = this.mOnMenuClickListener;
            if (onMenuClickListener4 != null) {
                onMenuClickListener4.onMenuClicked(view, baseInfo.mName, baseInfo, mainMenuData);
            }
        } else if (baseInfo.mName.equals(this.mContext.getString(R.string.main_menu_name_theme))) {
            List<ClipInfo> allPipVideoClips = TimelineDataUtil.getAllPipVideoClips();
            if (allPipVideoClips == null || allPipVideoClips.size() <= 0) {
                OnMenuClickListener onMenuClickListener5 = this.mOnMenuClickListener;
                if (onMenuClickListener5 != null) {
                    onMenuClickListener5.onMenuClicked(view, baseInfo.mName, baseInfo, mainMenuData);
                    return;
                }
                return;
            }
            DialogUtil.showTipDialog(getContext(), null, "删除【画中画】轨道才能添加【主题】效果哦", "知道了", new CommonDialog.TipsButtonClickListener() {
                /* class com.meishe.myvideo.view.MenuView.AnonymousClass5 */

                @Override // com.meishe.myvideo.view.CommonDialog.TipsButtonClickListener
                public void onTipsLeftButtonClick(View view) {
                }

                @Override // com.meishe.myvideo.view.CommonDialog.TipsButtonClickListener
                public void onTipsRightButtonClick(View view) {
                }
            });
        } else {
            if (this.mType == 2 && (onMenuClickListener2 = this.mOnMenuClickListener) != null) {
                onMenuClickListener2.onMenuClicked(view, baseInfo.mName, baseInfo, mainMenuData);
            }
            if (mainMenuData != null) {
                showBottomMenu(baseInfo.mName, mainMenuData, 0, 1);
                if ((baseInfo.mName.equals(this.mContext.getString(R.string.main_menu_name_background)) || baseInfo.mName.equals(this.mContext.getString(R.string.main_menu_name_picture_in_picture))) && (onMenuClickListener = this.mOnMenuClickListener) != null) {
                    onMenuClickListener.onMenuClicked(view, baseInfo.mName, baseInfo, mainMenuData);
                }
            }
        }
    }

    public void switchToMainMenu() {
        this.mType = -1;
        OnMenuClickListener onMenuClickListener = this.mOnMenuClickListener;
        if (onMenuClickListener != null) {
            onMenuClickListener.onSwitchMainMenu();
        }
        hideTransExpand();
        this.mBackBtn.setVisibility(8);
        this.mRecyclerView.setVisibility(0);
        this.mLinearSubRecy.setVisibility(8);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void switchToEditorMenu(String str) {
        char c;
        switch (str.hashCode()) {
            case -1890252483:
                if (str.equals(CommonData.CLIP_STICKER)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1465700526:
                if (str.equals(CommonData.CLIP_COMPOUND_CAPTION)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 93166550:
                if (str.equals(CommonData.CLIP_AUDIO)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 100313435:
                if (str.equals(CommonData.CLIP_IMAGE)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 112202875:
                if (str.equals(CommonData.CLIP_VIDEO)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 552573414:
                if (str.equals(CommonData.CLIP_CAPTION)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 609391404:
                if (str.equals(CommonData.CLIP_TIMELINE_FX)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                List<BaseInfo> data = MenuDataManager.getData(getResources().getString(R.string.sub_menu_audio_edit), this.mContext);
                if (data != null) {
                    this.mType = 4;
                    showBottomMenu(getResources().getString(R.string.sub_menu_audio_edit), data, 0, 3);
                    return;
                }
                return;
            case 1:
                List<BaseInfo> data2 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_effect_edit), this.mContext);
                if (data2 != null) {
                    this.mType = 10;
                    showBottomMenu(getResources().getString(R.string.sub_menu_effect_edit), data2, 0, 3);
                    return;
                }
                return;
            case 2:
                List<BaseInfo> data3 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_caption_edit), this.mContext);
                if (data3 != null) {
                    this.mType = 7;
                    showBottomMenu(getResources().getString(R.string.sub_menu_caption_edit), data3, 0, 3);
                    return;
                }
                return;
            case 3:
                List<BaseInfo> data4 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_compound_caption_edit), this.mContext);
                if (data4 != null) {
                    this.mType = 9;
                    showBottomMenu(getResources().getString(R.string.sub_menu_compound_caption_edit), data4, 0, 3);
                    return;
                }
                return;
            case 4:
                List<BaseInfo> data5 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_sticker_edit), this.mContext);
                if (data5 != null) {
                    this.mType = 8;
                    showBottomMenu(getResources().getString(R.string.sub_menu_sticker_edit), data5, 0, 3);
                    return;
                }
                return;
            case 5:
                List<BaseInfo> data6 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_pip_edit_video), this.mContext);
                if (data6 != null) {
                    this.mType = 11;
                    showBottomMenu(getResources().getString(R.string.sub_menu_pip_edit_video), data6, 0, 3);
                    return;
                }
                return;
            case 6:
                List<BaseInfo> data7 = MenuDataManager.getData(getResources().getString(R.string.sub_menu_pip_edit), this.mContext);
                if (data7 != null) {
                    this.mType = 12;
                    showBottomMenu(getResources().getString(R.string.sub_menu_pip_edit), data7, 0, 3);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void showBottomMenu(String str, List<BaseInfo> list, int i, int i2) {
        if (list != null && list.size() > 0) {
            RatioView ratioView = this.mRatioView;
            if (ratioView != null && ratioView.isShown()) {
                hideRatioView(false);
            }
            this.mBackBtn.setVisibility(0);
            this.mRecyclerView.setVisibility(8);
            this.mLinearSubRecy.setVisibility(0);
            int spaceSize = getSpaceSize(list.size());
            int itemDecorationCount = this.mSubRecyclerView.getItemDecorationCount();
            for (int i3 = 0; i3 < itemDecorationCount; i3++) {
                this.mSubRecyclerView.removeItemDecorationAt(i3);
            }
            this.mSubRecyclerView.addItemDecoration(new SpaceItemDecoration(spaceSize, spaceSize));
            this.mSubRecyclerView.setPadding(getLeftMargin(list.size()), 0, 0, 0);
            this.mMultiSubFunctionAdapter.addAll(list);
            if (getResources().getString(R.string.main_menu_name_theme).equals(str)) {
                MeicamTheme meicamTheme = TimelineData.getInstance().getMeicamTheme();
                if (meicamTheme != null) {
                    int initPosition = initPosition(list, meicamTheme.getThemePackageId());
                    this.mSubRecyclerView.scrollToPosition(initPosition);
                    this.mMultiSubFunctionAdapter.setSelectPosition(initPosition);
                }
            } else {
                this.mSubRecyclerView.scrollToPosition(0);
                this.mMultiSubFunctionAdapter.setSelectPosition(0);
            }
            BaseInfo baseInfo = list.get(0);
            if ((baseInfo instanceof EditMenuInfo) && ((EditMenuInfo) baseInfo).mIsShowSelectState) {
                this.mMultiSubFunctionAdapter.setSelectPosition(-1);
                this.mSubRecyclerView.scrollToPosition(0);
            }
        }
    }

    private int getLeftMargin(int i) {
        int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R.dimen.menu_back_btn_width) + this.mContext.getResources().getDimensionPixelOffset(R.dimen.menu_back_btn_margin_start);
        if (i == 1) {
            return 0;
        }
        return dimensionPixelOffset;
    }

    private int getSpaceSize(int i) {
        int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R.dimen.menu_back_btn_width) + this.mContext.getResources().getDimensionPixelOffset(R.dimen.menu_back_btn_margin_start);
        int screenWidth = ScreenUtils.getScreenWidth(this.mContext);
        if (i == 1) {
            return 0;
        }
        if (i > 4) {
            return ScreenUtils.dip2px(this.mContext, 4.0f);
        }
        return (int) (((((float) ((screenWidth - dimensionPixelOffset) - ((this.mContext.getResources().getDimensionPixelOffset(R.dimen.menu_item_width) + 0) * i))) / 2.0f) / ((float) i)) - 5.0f);
    }

    private int initPosition(List<BaseInfo> list, String str) {
        if (TextUtils.isEmpty(str)) {
            return 1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i).mPackageId)) {
                return i;
            }
        }
        return 0;
    }

    public void showAddVoiceMenu() {
        this.mMultiFunctionAdapter.addAll(MenuDataManager.getAddVoiceMenuData(this.mContext));
        this.mBackBtn.setVisibility(0);
    }

    private void updateTabLayout(int[] iArr, TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this.mTablayout.removeAllTabs();
        for (int i : iArr) {
            TabLayout.Tab newTab = this.mTablayout.newTab();
            newTab.setText(i);
            this.mTablayout.addTab(newTab);
            this.mTablayout.addOnTabSelectedListener(onTabSelectedListener);
        }
        this.mMenuTop.setVisibility(0);
    }

    public void showAdjustExpand(String str) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ScreenUtils.dp2px(this.mContext, 130.0f);
        setLayoutParams(layoutParams);
        this.mMMenuMiddle.setVisibility(0);
        this.mEditorChangeSeekBar.setName(str);
        this.mEditorChangeSeekBar.setSeekProgress(50);
        this.mEditorChangeSeekBar.setStartValueAndCurrentValue(-10.0f, 0.0f);
    }

    private void hideTransExpand() {
        this.mMenuTop.setVisibility(8);
        this.mMMenuMiddle.setVisibility(8);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ScreenUtils.dp2px(this.mContext, 70.0f);
        setLayoutParams(layoutParams);
    }

    public void showRatioView() {
        if (this.mRatioView != null) {
            this.mRecyclerView.setVisibility(4);
            this.mRatioView.setVisibility(0);
        }
        this.mBackBtn.setVisibility(0);
    }

    public void hideRatioView(boolean z) {
        if (this.mRatioView != null) {
            this.mRecyclerView.setVisibility(0);
            this.mRatioView.setVisibility(8);
        }
        if (z) {
            this.mBackBtn.setVisibility(8);
        }
    }

    public RatioView getRatioView() {
        return this.mRatioView;
    }

    @Override // com.meishe.myvideo.view.RatioView.OnRatioListener
    public void onRatioClick(int i) {
        OnMenuClickListener onMenuClickListener = this.mOnMenuClickListener;
        if (onMenuClickListener != null) {
            onMenuClickListener.onRatioClick(i);
        }
    }
}
