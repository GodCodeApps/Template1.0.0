package com.meishe.myvideo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.adapter.EditorTimelineTransitionAdapter;
import com.meishe.myvideo.fragment.TransitionFragment;
import com.meishe.myvideo.manager.StateManager;
import com.meishe.myvideo.util.Constants;
import com.meishe.myvideo.util.ScreenUtils;
import com.meishe.myvideo.util.SoftKeyboardUtil;
import com.meishe.myvideo.util.engine.EditorEngine;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class MYMultiBottomView extends RelativeLayout {
    public static final int STATE_TYPE_EDIT = 100;
    public static final int STATE_TYPE_PREVIEW = 200;
    public static final int TYPE_MENU_BEAUTY = 4;
    public static final int TYPE_MENU_CAPTION = 3;
    public static final int TYPE_MENU_STICKER = 2;
    public static final int TYPE_MENU_TRANSITION = 5;
    public static final int TYPE_MENU_WATER_EFFECT = 6;
    public static final int TYPE_MENU_WATER_MARK = 1;
    private FragmentPagerAdapter mAdapter;
    private Context mContext;
    private EditorTimelineTransitionAdapter.TransitionData mCurrTransitionData;
    private EditText mEtCaptionInput;
    private ArrayList<? extends Fragment> mFragmentArrayList;
    private FragmentManager mFragmentManager;
    private boolean mIsShow;
    private ImageView mIvBottomConfirm;
    private ImageView mIvConfirm;
    private OnChangeListener mOnChangeListener;
    private RelativeLayout mRlBottomConfirm;
    private RelativeLayout mRlTopMenu;
    private int mState;
    private TabLayout mTabLayout;
    private TextView mTvContent;
    private int mType = 0;
    private CustomViewPager mViewPager;
    private OnCaptionTabChangeListener onCaptionTabChangeListener;
    private OnWaterMarkChangeListener onWaterMarkChangeListener;

    public interface OnCaptionTabChangeListener {
        void onTabSelected(int i);
    }

    public interface OnChangeListener {
        void onCaptionTextChange(String str);

        void onClickConfirm(int i);
    }

    public interface OnWaterMarkChangeListener {
        void onTabSelected(int i);
    }

    public MYMultiBottomView(Context context) {
        super(context);
        init(context);
    }

    public MYMultiBottomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYMultiBottomView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_multi_bottom_view, this);
        this.mIvConfirm = (ImageView) inflate.findViewById(R.id.iv_confirm);
        this.mTabLayout = (TabLayout) inflate.findViewById(R.id.tab_layout);
        this.mViewPager = (CustomViewPager) inflate.findViewById(R.id.viewPager);
        this.mRlBottomConfirm = (RelativeLayout) inflate.findViewById(R.id.rl_bottom_confirm);
        this.mIvBottomConfirm = (ImageView) inflate.findViewById(R.id.iv_bottom_confirm);
        this.mEtCaptionInput = (EditText) inflate.findViewById(R.id.et_caption_input);
        this.mRlTopMenu = (RelativeLayout) inflate.findViewById(R.id.rl_top_menu);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        this.mViewPager.setScanScroll(false);
        initListener();
    }

    private void initTabLayout(String[] strArr) {
        this.mTabLayout.removeAllTabs();
        for (String str : strArr) {
            TabLayout tabLayout = this.mTabLayout;
            tabLayout.addTab(tabLayout.newTab().setText(str));
        }
    }

    private void initListener() {
        this.mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass1 */

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                MYMultiBottomView.this.mViewPager.setCurrentItem(position);
                MYMultiBottomView.this.mTvContent.setText(tab.getText());
                if (MYMultiBottomView.this.mType == 3) {
                    SoftKeyboardUtil.hideInput(MYMultiBottomView.this.mContext, MYMultiBottomView.this.mEtCaptionInput);
                }
                if (MYMultiBottomView.this.mType == 5 && CollectionUtils.isIndexAvailable(position, MYMultiBottomView.this.mFragmentArrayList) && MYMultiBottomView.this.mCurrTransitionData != null) {
                    Fragment fragment = (Fragment) MYMultiBottomView.this.mFragmentArrayList.get(position);
                    if (fragment instanceof TransitionFragment) {
                        TransitionFragment transitionFragment = (TransitionFragment) fragment;
                        transitionFragment.setTransitionData(MYMultiBottomView.this.mCurrTransitionData);
                        transitionFragment.setTransitionPosition(MYMultiBottomView.this.mContext);
                    }
                }
                if (tab.getText().equals(MYMultiBottomView.this.mContext.getResources().getString(R.string.fragment_menu_water)) || tab.getText().equals(MYMultiBottomView.this.mContext.getResources().getString(R.string.fragment_menu_water_effect))) {
                    if (tab.getText().equals(MYMultiBottomView.this.mContext.getResources().getString(R.string.fragment_menu_water))) {
                        MYMultiBottomView.this.mType = 1;
                    } else {
                        MYMultiBottomView.this.mType = 6;
                    }
                    if (MYMultiBottomView.this.onWaterMarkChangeListener != null) {
                        MYMultiBottomView.this.onWaterMarkChangeListener.onTabSelected(tab.getPosition());
                    }
                }
                if (MYMultiBottomView.this.mType == 3 && MYMultiBottomView.this.onCaptionTabChangeListener != null) {
                    MYMultiBottomView.this.onCaptionTabChangeListener.onTabSelected(position);
                }
            }
        });
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass2 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                MYMultiBottomView.this.mTabLayout.getTabAt(i).select();
            }
        });
        this.mIvConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass3 */

            public void onClick(View view) {
                MYMultiBottomView.this.hide();
                if (MYMultiBottomView.this.mOnChangeListener != null) {
                    MYMultiBottomView.this.mOnChangeListener.onClickConfirm(MYMultiBottomView.this.mType);
                }
            }
        });
        this.mIvBottomConfirm.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass4 */

            public void onClick(View view) {
                MYMultiBottomView.this.hide();
                if (MYMultiBottomView.this.mOnChangeListener != null) {
                    MYMultiBottomView.this.mOnChangeListener.onClickConfirm(MYMultiBottomView.this.mType);
                }
            }
        });
        this.mEtCaptionInput.addTextChangedListener(new TextWatcher() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass5 */

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (MYMultiBottomView.this.mOnChangeListener != null && MYMultiBottomView.this.mState == 100) {
                    MYMultiBottomView.this.mOnChangeListener.onCaptionTextChange(editable.toString());
                }
            }
        });
        this.mRlTopMenu.setOnTouchListener(new View.OnTouchListener() {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass6 */

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    private void show(String[] strArr, ArrayList<? extends Fragment> arrayList, int i, boolean z, int i2) {
        Logger.d("tell", "show: type =" + i2);
        this.mIsShow = true;
        this.mState = 100;
        this.mType = i2;
        this.mFragmentArrayList = arrayList;
        initTabLayout(strArr);
        initViewPager(i);
        this.mAdapter.notifyDataSetChanged();
        this.mEtCaptionInput.setVisibility(z ? 0 : 8);
        if (z) {
            SoftKeyboardUtil.showInput(this.mContext, this.mEtCaptionInput);
        }
        setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_enter));
        setVisibility(0);
    }

    public void show(String[] strArr, ArrayList<Fragment> arrayList, int i, boolean z, boolean z2, int i2, String str, int i3) {
        if (i3 > 0) {
            setKeyboardHeight(i3);
        }
        show(strArr, arrayList, i, z, z2, i2, str);
    }

    public void show(String[] strArr, ArrayList<Fragment> arrayList, int i, boolean z, boolean z2, int i2, String str) {
        this.mEtCaptionInput.setText(str);
        EditText editText = this.mEtCaptionInput;
        editText.setSelection(editText.getText().length());
        initConfirmView(z, z2);
        show(strArr, arrayList, i, z, i2);
    }

    private void initView(int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRlTopMenu.getLayoutParams();
        layoutParams.height = ScreenUtils.dp2px(this.mContext, (float) i);
        this.mRlTopMenu.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mViewPager.getLayoutParams();
        layoutParams2.height = ScreenUtils.dp2px(this.mContext, (float) i2);
        this.mViewPager.setLayoutParams(layoutParams2);
    }

    public void show(String[] strArr, ArrayList<Fragment> arrayList, int i, boolean z, boolean z2, int i2, int i3) {
        if (i3 > 0) {
            setKeyboardHeight(i3);
        }
        show(strArr, arrayList, i, z, z2, i2);
    }

    public void show(String[] strArr, ArrayList<? extends Fragment> arrayList, int i, boolean z, boolean z2, int i2) {
        initConfirmView(z, z2);
        show(strArr, arrayList, i, z, i2);
    }

    private void initConfirmView(boolean z, boolean z2) {
        if (z) {
            this.mRlBottomConfirm.setVisibility(8);
            this.mIvConfirm.setVisibility(0);
        } else if (z2) {
            this.mRlBottomConfirm.setVisibility(0);
            this.mIvConfirm.setVisibility(8);
            initView(200, 126);
        } else {
            this.mRlBottomConfirm.setVisibility(8);
            this.mIvConfirm.setVisibility(0);
            initView(261, 217);
        }
    }

    public void setKeyboardHeight(int i) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRlTopMenu.getLayoutParams();
        layoutParams.height = ScreenUtils.dp2px(this.mContext, 94.0f) + i;
        this.mRlTopMenu.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mViewPager.getLayoutParams();
        layoutParams2.height = i;
        this.mViewPager.setLayoutParams(layoutParams2);
    }

    private void initViewPager(int i) {
        this.mAdapter = new FragmentPagerAdapter(this.mFragmentManager) {
            /* class com.meishe.myvideo.view.MYMultiBottomView.AnonymousClass7 */

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getItemPosition(@NonNull Object obj) {
                return -2;
            }

            @Override // androidx.fragment.app.FragmentPagerAdapter
            public Fragment getItem(int i) {
                return (Fragment) MYMultiBottomView.this.mFragmentArrayList.get(i);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return MYMultiBottomView.this.mFragmentArrayList.size();
            }
        };
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setCurrentItem(i);
    }

    public void hide() {
        if (this.mIsShow) {
            this.mIsShow = false;
            this.mState = 200;
            if (this.mType == 3) {
                this.mEtCaptionInput.setText((CharSequence) null);
                SoftKeyboardUtil.hideInput(this.mContext, this.mEtCaptionInput);
            }
            setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.view_exit));
            setVisibility(8);
            for (int i = 0; i < this.mFragmentArrayList.size(); i++) {
                removeFragmentInternal((Fragment) this.mFragmentArrayList.get(i));
            }
            this.mFragmentArrayList.clear();
            this.mViewPager.setAdapter(null);
            EditorEngine.getInstance().getVideoFragment().setDrawRectVisible(false);
            EditorEngine.getInstance().getVideoFragment().setCurCaption(null);
            if (!Constants.STATE_PIC_IN_PIC.equals(StateManager.getInstance().getCurrentState())) {
                StateManager.getInstance().changeState(StateManager.getInstance().getCurrentState(), 3);
            }
        }
    }

    public boolean isShow() {
        return this.mIsShow;
    }

    public int getType() {
        return this.mType;
    }

    private void removeFragmentInternal(Fragment fragment) {
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        beginTransaction.remove(fragment);
        beginTransaction.commitNow();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public void setTransitionData(EditorTimelineTransitionAdapter.TransitionData transitionData) {
        this.mCurrTransitionData = transitionData;
    }

    public void setOnWaterMarkChangeListener(OnWaterMarkChangeListener onWaterMarkChangeListener2) {
        this.onWaterMarkChangeListener = onWaterMarkChangeListener2;
    }

    public void setCaptionTabChangeListener(OnCaptionTabChangeListener onCaptionTabChangeListener2) {
        this.onCaptionTabChangeListener = onCaptionTabChangeListener2;
    }
}
