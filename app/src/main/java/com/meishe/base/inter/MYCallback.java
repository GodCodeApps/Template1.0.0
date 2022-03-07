package com.meishe.base.inter;

import android.os.Bundle;
import android.view.View;

public interface MYCallback {
    int bindLayout();

    void getBundleExtras(Bundle bundle);

    void initData();

    void initView(View view);
}
