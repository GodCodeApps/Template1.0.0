package com.meishe.myvideo.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.view.RatioView;
import com.meishe.myvideo.view.menu.adapter.GridItemDecoration;
import com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter;
import com.meishe.myvideo.view.menu.adapter.MenuFunctionAdapter;
import com.meishe.myvideo.view.menu.adapter.MenuTableAdapter;
import com.meishe.myvideo.view.menu.data.MenusManager;
import com.meishe.myvideoapp.R;
import java.util.List;

public class BottomMenuView extends ConstraintLayout implements IBottomMenuView {
    private static final int TYPE_MENU_MAIN = 20;
    private static final int TYPE_MENU_OTHER = 22;
    private static final int TYPE_MENU_SUB = 21;
    private IBottomClickListener listener;
    private Context mContext;
    private MenuFunctionAdapter mFunctionAdapter;
    private MenuFunctionAdapter mSubFunctionAdapter;
    private BaseInfo mSubInfo;
    private MenuTableAdapter mTableAdapter;
    private View menuBack;
    private View menuBreakView;
    private RecyclerView menuMainRecycler;
    private ConstraintLayout menuSub;
    private RecyclerView menuSubRecycler;
    private TextView menuSubTittle;
    private View menuSubmit;
    private TabLayout menuTable;
    private RecyclerView menuTableRecycler;
    private View menuTableSubmit;
    private RatioView ratioView;
    private int type = 20;

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void clickSubItem(View view, BaseInfo baseInfo, int i) {
    }

    private void showCombCaptionView() {
    }

    public BottomMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initView();
        initData();
        showMain();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_bottom_menu, this);
        this.menuMainRecycler = (RecyclerView) inflate.findViewById(R.id.menu_recyclerView_main);
        this.menuSubRecycler = (RecyclerView) inflate.findViewById(R.id.menu_recyclerView_sub);
        this.menuTableRecycler = (RecyclerView) inflate.findViewById(R.id.menu_recyclerView_table);
        this.menuSub = (ConstraintLayout) inflate.findViewById(R.id.view_menu_sub);
        this.menuSubmit = inflate.findViewById(R.id.menu_sub_submit);
        this.menuBreakView = inflate.findViewById(R.id.menu_break);
        this.menuSubTittle = (TextView) inflate.findViewById(R.id.tv_menu_sub_tittle);
        this.menuBack = inflate.findViewById(R.id.menu_back);
        this.ratioView = (RatioView) inflate.findViewById(R.id.menu_ratio);
        this.menuTable = (TabLayout) inflate.findViewById(R.id.menu_table);
        this.menuTableSubmit = inflate.findViewById(R.id.menu_table_submit);
        initListener();
    }

    private void initListener() {
        this.menuBack.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass1 */

            public void onClick(View view) {
                if (BottomMenuView.this.type == 21) {
                    BottomMenuView.this.showMain();
                    return;
                }
                BottomMenuView bottomMenuView = BottomMenuView.this;
                bottomMenuView.showSubView(bottomMenuView.mSubInfo);
            }
        });
        this.menuSubmit.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass2 */

            public void onClick(View view) {
                if (BottomMenuView.this.type == 21) {
                    BottomMenuView.this.showMain();
                    return;
                }
                BottomMenuView bottomMenuView = BottomMenuView.this;
                bottomMenuView.showSubView(bottomMenuView.mSubInfo);
            }
        });
        this.menuTableSubmit.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass3 */

            public void onClick(View view) {
                if (BottomMenuView.this.type == 21) {
                    BottomMenuView.this.showMain();
                    return;
                }
                BottomMenuView bottomMenuView = BottomMenuView.this;
                bottomMenuView.showSubView(bottomMenuView.mSubInfo);
            }
        });
        this.ratioView.setOnRatioListener(new RatioView.OnRatioListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass4 */

            @Override // com.meishe.myvideo.view.RatioView.OnRatioListener
            public void onRatioClick(int i) {
                if (BottomMenuView.this.listener != null) {
                    BottomMenuView.this.listener.onRatioClick(i);
                }
            }
        });
    }

    private void initData() {
        this.menuTable.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        this.menuTable.setTabIndicatorFullWidth(false);
        initMenus();
    }

    private void initMenus() {
        List<BaseInfo> mainMenuData = MenusManager.getMainMenuData(this.mContext);
        this.mFunctionAdapter = new MenuFunctionAdapter(this.mContext, this.menuMainRecycler);
        this.menuMainRecycler.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.menuMainRecycler.setAdapter(this.mFunctionAdapter);
        this.menuMainRecycler.addItemDecoration(new SpaceItemDecoration(18, 18));
        this.mFunctionAdapter.addAll(mainMenuData);
        this.mFunctionAdapter.setOnItemClickListener(new MenuBaseAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass5 */

            @Override // com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BottomMenuView.this.showSubView(BottomMenuView.this.mFunctionAdapter.getItem(i));
            }
        });
        this.mSubFunctionAdapter = new MenuFunctionAdapter(this.mContext, this.menuSubRecycler);
        this.menuSubRecycler.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.menuSubRecycler.setAdapter(this.mSubFunctionAdapter);
        this.menuSubRecycler.addItemDecoration(new SpaceItemDecoration(18, 18));
        this.mSubFunctionAdapter.setOnItemClickListener(new MenuBaseAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass6 */

            @Override // com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
                BaseInfo item = BottomMenuView.this.mSubFunctionAdapter.getItem(i);
                if (BottomMenuView.this.type != 22) {
                    BottomMenuView.this.clickSubItem(view, item, i);
                } else if (BottomMenuView.this.listener != null) {
                    BottomMenuView.this.listener.onItemClick(item);
                }
            }
        });
    }

    private void initTableRecycler() {
        this.mTableAdapter = new MenuTableAdapter(this.mContext, this.menuTableRecycler);
        this.menuTableRecycler.setLayoutManager(new GridLayoutManager(this.mContext, 5));
        this.menuTableRecycler.setAdapter(this.mTableAdapter);
        this.menuTableRecycler.addItemDecoration(new GridItemDecoration(18, 18, 18, 18));
        this.mTableAdapter.setOnItemClickListener(new MenuBaseAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass7 */

            @Override // com.meishe.myvideo.view.menu.adapter.MenuBaseAdapter.OnItemClickListener
            public void onItemClicked(View view, int i) {
            }
        });
    }

    @Override // com.meishe.myvideo.view.menu.IBottomMenuView
    public void showMain() {
        this.type = 20;
        closeAllViews();
        this.menuMainRecycler.setVisibility(0);
    }

    @Override // com.meishe.myvideo.view.menu.IBottomMenuView
    public void showSubView(BaseInfo baseInfo) {
        if (baseInfo == null) {
            showMain();
            return;
        }
        closeAllViews();
        int i = this.type;
        if (i == 20) {
            this.type = 21;
            this.mSubInfo = baseInfo;
            if (getResources().getString(R.string.main_menu_name_ratio).equals(baseInfo.mName)) {
                showRatioView();
                return;
            }
            List<BaseInfo> handleSubMenu = handleSubMenu(baseInfo);
            if (handleSubMenu != null) {
                if (this.type == 21) {
                    this.menuBack.setVisibility(0);
                }
                this.mSubFunctionAdapter.addAll(handleSubMenu);
                this.mSubFunctionAdapter.notifyDataSetChanged();
                this.menuSubRecycler.setVisibility(0);
            }
            this.menuSub.requestLayout();
            this.menuSub.setVisibility(0);
        } else if (i == 21) {
            List<BaseInfo> localData = MenusManager.getLocalData(baseInfo.mName, this.mContext);
            if (localData != null) {
                this.mSubFunctionAdapter.addAll(localData);
                this.mSubFunctionAdapter.notifyDataSetChanged();
            }
            this.menuBack.setVisibility(0);
            this.menuSub.setVisibility(0);
            this.menuSubRecycler.setVisibility(0);
        } else {
            this.type = 21;
            showSubView(this.mSubInfo);
        }
    }

    private List<BaseInfo> handleSubMenu(BaseInfo baseInfo) {
        List<BaseInfo> localData = MenusManager.getLocalData(baseInfo.mName, this.mContext);
        if (getResources().getString(R.string.main_menu_name_theme).equals(baseInfo.mName)) {
            this.type = 22;
            localData = MenusManager.getShowData(baseInfo.mName, this.mContext);
            showBottomTittle(baseInfo.mName);
        }
        if (getResources().getString(R.string.main_menu_name_sticker).equals(baseInfo.mName)) {
            this.type = 22;
            showStickerView();
            localData = null;
        }
        if (getResources().getString(R.string.main_menu_name_caption).equals(baseInfo.mName)) {
            this.type = 22;
            showCaptionView();
            localData = null;
        }
        if (getResources().getString(R.string.main_menu_name_com_caption).equals(baseInfo.mName)) {
            this.type = 22;
            showCombCaptionView();
            localData = null;
        }
        if (!getResources().getString(R.string.main_menu_name_adjust).equals(baseInfo.mName)) {
            return localData;
        }
        this.type = 22;
        List<BaseInfo> showData = MenusManager.getShowData(baseInfo.mName, this.mContext);
        showBottomTittle(baseInfo.mName);
        return showData;
    }

    private void showWaterMarkView(String str) {
        this.menuTableSubmit.setVisibility(0);
        this.menuTable.setVisibility(0);
        this.menuTableRecycler.setVisibility(0);
        if (this.mTableAdapter == null) {
            initTableRecycler();
        }
        this.menuTable.removeAllTabs();
        List<String> tabData = MenusManager.getTabData(this.mContext, getResources().getString(R.string.main_menu_name_water_mark));
        for (int i = 0; i < tabData.size(); i++) {
            TabLayout tabLayout = this.menuTable;
            tabLayout.addTab(tabLayout.newTab());
            this.menuTable.getTabAt(i).setText(tabData.get(i));
        }
        this.menuTable.clearOnTabSelectedListeners();
        this.menuTable.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass8 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
                if (text != null) {
                    BottomMenuView.this.mTableAdapter.addAll(MenusManager.getWaterMarkData(BottomMenuView.this.mContext, text.toString()));
                    BottomMenuView.this.mTableAdapter.notifyDataSetChanged();
                }
            }
        });
        for (int i2 = 0; i2 < tabData.size(); i2++) {
            if (tabData.get(i2).equals(str)) {
                this.menuTable.getTabAt(i2).select();
            }
        }
    }

    private void showCaptionView() {
        this.menuTableSubmit.setVisibility(0);
        this.menuTable.setVisibility(0);
        this.menuTableRecycler.setVisibility(0);
        if (this.mTableAdapter == null) {
            initTableRecycler();
        }
        this.menuTable.removeAllTabs();
        List<String> tabData = MenusManager.getTabData(this.mContext, getResources().getString(R.string.main_menu_name_caption));
        for (int i = 0; i < tabData.size(); i++) {
            TabLayout tabLayout = this.menuTable;
            tabLayout.addTab(tabLayout.newTab());
            this.menuTable.getTabAt(i).setText(tabData.get(i));
        }
        this.menuTable.clearOnTabSelectedListeners();
        this.menuTable.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass9 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
                if (text != null) {
                    BottomMenuView.this.mTableAdapter.addAll(MenusManager.getCaptionsDataByName(BottomMenuView.this.mContext, text.toString()));
                    BottomMenuView.this.mTableAdapter.notifyDataSetChanged();
                }
            }
        });
        this.mTableAdapter.addAll(MenusManager.getCaptionsDataByName(this.mContext, getResources().getString(R.string.fragment_menu_table_all)));
        this.mTableAdapter.notifyDataSetChanged();
    }

    private void showStickerView() {
        this.menuTableSubmit.setVisibility(0);
        this.menuTable.setVisibility(0);
        this.menuTableRecycler.setVisibility(0);
        if (this.mTableAdapter == null) {
            initTableRecycler();
        }
        this.menuTable.removeAllTabs();
        List<String> tabData = MenusManager.getTabData(this.mContext, getResources().getString(R.string.main_menu_name_sticker));
        for (int i = 0; i < tabData.size(); i++) {
            TabLayout tabLayout = this.menuTable;
            tabLayout.addTab(tabLayout.newTab());
            this.menuTable.getTabAt(i).setText(tabData.get(i));
        }
        this.menuTable.clearOnTabSelectedListeners();
        this.menuTable.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.menu.BottomMenuView.AnonymousClass10 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
                if (text != null) {
                    BottomMenuView.this.mTableAdapter.addAll(MenusManager.getSticksDataByName(BottomMenuView.this.mContext, text.toString()));
                    BottomMenuView.this.mTableAdapter.notifyDataSetChanged();
                }
            }
        });
        this.mTableAdapter.addAll(MenusManager.getSticksDataByName(this.mContext, getResources().getString(R.string.fragment_menu_table_all)));
        this.mTableAdapter.notifyDataSetChanged();
    }

    private void showBottomTittle(String str) {
        this.menuSubmit.setVisibility(0);
        TextView textView = this.menuSubTittle;
        textView.setText(str + "");
        this.menuSubTittle.setVisibility(0);
        this.menuBreakView.setVisibility(0);
    }

    @Override // com.meishe.myvideo.view.menu.IBottomMenuView
    public void showRatioView() {
        this.ratioView.setVisibility(0);
        this.menuBack.setVisibility(0);
        this.menuSub.requestLayout();
        this.menuSub.setVisibility(0);
    }

    @Override // com.meishe.myvideo.view.menu.IBottomMenuView
    public void setOnBottomClickListener(IBottomClickListener iBottomClickListener) {
        this.listener = iBottomClickListener;
    }

    public void closeAllViews() {
        this.menuMainRecycler.setVisibility(8);
        for (int i = 0; i < this.menuSub.getChildCount(); i++) {
            this.menuSub.getChildAt(i).setVisibility(8);
        }
    }
}
