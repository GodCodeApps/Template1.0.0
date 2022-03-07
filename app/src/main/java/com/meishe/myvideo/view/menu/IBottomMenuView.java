package com.meishe.myvideo.view.menu;

import com.meishe.myvideo.bean.BaseInfo;

public interface IBottomMenuView {
    void setOnBottomClickListener(IBottomClickListener iBottomClickListener);

    void showMain();

    void showRatioView();

    void showSubView(BaseInfo baseInfo);
}
