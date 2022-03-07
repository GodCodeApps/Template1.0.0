package com.meishe.myvideo.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.meishe.myvideo.fragment.StickerAllFragment;
import com.meishe.myvideo.fragment.StickerCustomFragment;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class CustomBottomDialog {
    public static final int PADDING = 0;
    private Context mContext;
    private Dialog mDialog;
    private ArrayList<Fragment> mFragmentArrayList;
    private ImageView mIvConfirm;
    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;

    private void initData() {
    }

    public CustomBottomDialog(Context context) {
        this.mContext = context;
        initDialog();
        initView();
        initWindow();
        initData();
        initTabLayout();
        initStickerTabFragment();
        initListener();
    }

    private void initDialog() {
        this.mDialog = new Dialog(this.mContext, 2131755334);
        this.mDialog.setCanceledOnTouchOutside(false);
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.layout_multi_bottom_view, (ViewGroup) null);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mTabLayout = (TabLayout) inflate.findViewById(R.id.tab_layout);
        this.mViewPager = (CustomViewPager) inflate.findViewById(R.id.viewPager);
        this.mDialog.setContentView(inflate);
    }

    private void initWindow() {
        Window window = this.mDialog.getWindow();
        if (window != null) {
            window.setFlags(32, 32);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.height = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp261);
            window.setAttributes(attributes);
            window.getDecorView().setBackgroundResource(R.color.colorTranslucent);
            window.setGravity(80);
            window.setWindowAnimations(R.style.CustomBottomDialogAnimator);
        }
    }

    private void initTabLayout() {
        TabLayout tabLayout = this.mTabLayout;
        tabLayout.addTab(tabLayout.newTab().setText(this.mContext.getResources().getString(R.string.fragment_menu_table_all)));
        TabLayout tabLayout2 = this.mTabLayout;
        tabLayout2.addTab(tabLayout2.newTab().setText(this.mContext.getResources().getString(R.string.fragment_menu_table_custom)));
    }

    private void initStickerTabFragment() {
        this.mFragmentArrayList = new ArrayList<>();
        this.mFragmentArrayList.add(new StickerAllFragment());
        this.mFragmentArrayList.add(new StickerCustomFragment());
    }

    private void initListener() {
        this.mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.CustomBottomDialog.AnonymousClass1 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                CustomBottomDialog.this.mViewPager.setCurrentItem(tab.getPosition());
            }
        });
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class com.meishe.myvideo.view.CustomBottomDialog.AnonymousClass2 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CustomBottomDialog.this.mTabLayout.getTabAt(i).select();
            }
        });
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.CustomBottomDialog.AnonymousClass3 */

            public void onClick(View view) {
                CustomBottomDialog.this.hide();
            }
        });
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            /* class com.meishe.myvideo.view.CustomBottomDialog.AnonymousClass4 */

            @Override // androidx.fragment.app.FragmentPagerAdapter
            public Fragment getItem(int i) {
                return (Fragment) CustomBottomDialog.this.mFragmentArrayList.get(i);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return CustomBottomDialog.this.mFragmentArrayList.size();
            }
        });
    }

    public void show() {
        this.mDialog.show();
    }

    public void hide() {
        this.mDialog.hide();
    }
}
