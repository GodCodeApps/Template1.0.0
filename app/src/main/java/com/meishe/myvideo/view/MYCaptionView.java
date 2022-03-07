package com.meishe.myvideo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.meishe.engine.bean.MeicamCaptionClip;
import com.meishe.myvideo.bean.CaptionStyleInfo;
import com.meishe.myvideo.fragment.CaptionColorFragment;
import com.meishe.myvideo.fragment.CaptionFontFragment;
import com.meishe.myvideo.fragment.CaptionLetterSpacingFragment;
import com.meishe.myvideo.fragment.CaptionOutlineFragment;
import com.meishe.myvideo.fragment.CaptionPositionFragment;
import com.meishe.myvideo.fragment.CaptionStyleFragment;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class MYCaptionView extends RelativeLayout {
    private ArrayList<CaptionStyleInfo> mCaptionStyleList;
    private TabLayout mCaptionStyleTab;
    private Context mContext;
    private EditText mEtCaptionInput;
    private ArrayList<Fragment> mFragmentArrayList;
    private FragmentManager mFragmentManager;
    private boolean mIsOnlyChangeText = false;
    private View mIvConfirm;
    private OnCaptionChangeListener mOnCaptionChangeListener;
    private CustomViewPager mViewPager;

    public interface OnCaptionChangeListener {
        void onCaptionConfirm();

        void onCaptionTextChange(String str);
    }

    private void initData() {
    }

    public MYCaptionView(Context context) {
        super(context);
        init(context);
    }

    public MYCaptionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYCaptionView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_caption_style, this);
        this.mCaptionStyleTab = (TabLayout) inflate.findViewById(R.id.captionStyleTab);
        this.mViewPager = (CustomViewPager) inflate.findViewById(R.id.viewPager);
        this.mIvConfirm = inflate.findViewById(R.id.iv_confirm);
        this.mEtCaptionInput = (EditText) inflate.findViewById(R.id.et_caption_input);
    }

    private void initListener() {
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYCaptionView.AnonymousClass1 */

            public void onClick(View view) {
                MYCaptionView.this.hide();
                if (MYCaptionView.this.mOnCaptionChangeListener != null) {
                    MYCaptionView.this.mOnCaptionChangeListener.onCaptionConfirm();
                }
            }
        });
        this.mEtCaptionInput.addTextChangedListener(new TextWatcher() {
            /* class com.meishe.myvideo.view.MYCaptionView.AnonymousClass2 */

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!MYCaptionView.this.mIsOnlyChangeText && MYCaptionView.this.mOnCaptionChangeListener != null) {
                    MYCaptionView.this.mOnCaptionChangeListener.onCaptionTextChange(editable.toString());
                }
            }
        });
        this.mCaptionStyleTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.MYCaptionView.AnonymousClass3 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                MYCaptionView.this.mViewPager.setCurrentItem(tab.getPosition());
            }
        });
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class com.meishe.myvideo.view.MYCaptionView.AnonymousClass4 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                MYCaptionView.this.mCaptionStyleTab.getTabAt(i).select();
            }
        });
    }

    private void initTabLayout() {
        String[] stringArray;
        this.mCaptionStyleTab.removeAllTabs();
        this.mFragmentArrayList = new ArrayList<>();
        for (String str : getResources().getStringArray(R.array.menu_tab_caption)) {
            TabLayout tabLayout = this.mCaptionStyleTab;
            tabLayout.addTab(tabLayout.newTab().setText(str));
        }
        initCaptionTabFragment();
    }

    private void initCaptionTabFragment() {
        this.mFragmentArrayList.add(new CaptionStyleFragment());
        this.mFragmentArrayList.add(new CaptionColorFragment());
        this.mFragmentArrayList.add(new CaptionOutlineFragment());
        this.mFragmentArrayList.add(new CaptionFontFragment());
        this.mFragmentArrayList.add(new CaptionLetterSpacingFragment());
        this.mFragmentArrayList.add(new CaptionPositionFragment());
    }

    public void show(MeicamCaptionClip meicamCaptionClip) {
        initData();
        initTabLayout();
        initListener();
        initViewPager();
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_enter));
        setVisibility(0);
        showInput(this.mEtCaptionInput);
        if (meicamCaptionClip != null) {
            this.mEtCaptionInput.setText(meicamCaptionClip.getText());
            return;
        }
        this.mIsOnlyChangeText = true;
        this.mEtCaptionInput.setText((CharSequence) null);
        this.mIsOnlyChangeText = false;
    }

    private void initViewPager() {
        this.mViewPager.setAdapter(new FragmentPagerAdapter(this.mFragmentManager) {
            /* class com.meishe.myvideo.view.MYCaptionView.AnonymousClass5 */

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getItemPosition(@NonNull Object obj) {
                return -2;
            }

            @Override // androidx.fragment.app.FragmentPagerAdapter
            public Fragment getItem(int i) {
                return (Fragment) MYCaptionView.this.mFragmentArrayList.get(i);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return MYCaptionView.this.mFragmentArrayList.size();
            }
        });
    }

    public void hide() {
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_exit));
        setVisibility(8);
        hideInput(this.mEtCaptionInput);
        for (int i = 0; i < this.mFragmentArrayList.size(); i++) {
            removeFragmentInternal(this.mFragmentArrayList.get(i));
        }
        this.mFragmentArrayList.clear();
    }

    public void showInput(EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) this.mContext.getSystemService("input_method")).showSoftInput(editText, 1);
    }

    /* access modifiers changed from: protected */
    public void hideInput(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (editText != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void removeFragmentInternal(Fragment fragment) {
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        beginTransaction.remove(fragment);
        beginTransaction.commitNow();
    }

    public void setOnCaptionChangeListener(OnCaptionChangeListener onCaptionChangeListener) {
        this.mOnCaptionChangeListener = onCaptionChangeListener;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }
}
