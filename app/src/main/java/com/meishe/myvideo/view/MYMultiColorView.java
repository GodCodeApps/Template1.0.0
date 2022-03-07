package com.meishe.myvideo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.meicam.sdk.NvsColor;
import com.meishe.engine.util.ColorUtil;
import com.meishe.myvideo.adapter.MultiFunctionAdapter;
import com.meishe.myvideo.bean.CaptionColorInfo;
import com.meishe.myvideo.util.ParseJsonFile;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class MYMultiColorView extends LinearLayout {
    private static final String COLOR_ASSETS_PATH = "background/color/colorAxis.json";
    private ArrayList<CaptionColorInfo> mCaptionColorList;
    private Context mContext;
    private MultiFunctionAdapter mMultiFunctionAdapter;
    private RecyclerView mRecyclerView;

    public MYMultiColorView(Context context) {
        super(context);
        init(context);
    }

    public MYMultiColorView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MYMultiColorView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.layout_multi_color_view, this).findViewById(R.id.recyclerView);
        initData();
        initRecyclerView();
    }

    private void initData() {
        this.mCaptionColorList = new ArrayList<>();
        initCaptionColorList();
    }

    private void initRecyclerView() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mMultiFunctionAdapter = new MultiFunctionAdapter(this.mContext, this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mMultiFunctionAdapter);
        this.mMultiFunctionAdapter.addAll(this.mCaptionColorList);
    }

    private void initCaptionColorList() {
        for (CaptionColorInfo captionColorInfo : getBackgroundColorList()) {
            captionColorInfo.setColorValue(ColorUtil.nvsColorToHexString(new NvsColor(captionColorInfo.r, captionColorInfo.g, captionColorInfo.b, 1.0f)));
            this.mCaptionColorList.add(captionColorInfo);
        }
    }

    public MultiFunctionAdapter getMultiFunctionAdapter() {
        return this.mMultiFunctionAdapter;
    }

    public List<CaptionColorInfo> getBackgroundColorList() {
        String readAssetJsonFile = ParseJsonFile.readAssetJsonFile(this.mContext, COLOR_ASSETS_PATH);
        if (TextUtils.isEmpty(readAssetJsonFile)) {
            return null;
        }
        return (List) new GsonBuilder().create().fromJson(readAssetJsonFile, new TypeToken<ArrayList<CaptionColorInfo>>() {
            /* class com.meishe.myvideo.view.MYMultiColorView.AnonymousClass1 */
        }.getType());
    }
}
