package com.meishe.myvideo.view;

public interface ItemTouchListener {
    void onItemDismiss(int i);

    void onItemMoved(int i, int i2);

    void removeAll();
}
