package com.meishe.myvideo.bean.menu;

import android.content.Context;
import android.content.res.Resources;
import com.meishe.engine.bean.CommonData;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;

public class BeautyDataHelper {
    public static ArrayList<BaseInfo> getBeautyDataListByType(Context context, int i) {
        Resources resources = context.getResources();
        ArrayList<BaseInfo> arrayList = new ArrayList<>();
        if (i == 1) {
            BeautyShapeDataItem beautyShapeDataItem = new BeautyShapeDataItem(resources.getString(R.string.strength));
            beautyShapeDataItem.mIconRcsId = R.mipmap.beauty_strength;
            beautyShapeDataItem.beautyShapeId = CommonData.VIDEO_FX_BEAUTY_STRENGTH;
            arrayList.add(beautyShapeDataItem);
            BeautyShapeDataItem beautyShapeDataItem2 = new BeautyShapeDataItem(resources.getString(R.string.whitening));
            beautyShapeDataItem2.mIconRcsId = R.mipmap.beauty_whitening;
            beautyShapeDataItem2.beautyShapeId = CommonData.VIDEO_FX_BEAUTY_WHITENING;
            arrayList.add(beautyShapeDataItem2);
            BeautyShapeDataItem beautyShapeDataItem3 = new BeautyShapeDataItem(resources.getString(R.string.ruddy));
            beautyShapeDataItem3.mIconRcsId = R.mipmap.beauty_reddening;
            beautyShapeDataItem3.beautyShapeId = CommonData.VIDEO_FX_BEAUTY_REDDENING;
            arrayList.add(beautyShapeDataItem3);
            BeautyShapeDataItem beautyShapeDataItem4 = new BeautyShapeDataItem(resources.getString(R.string.correctionColor));
            beautyShapeDataItem4.mIconRcsId = R.mipmap.beauty_adjust_color;
            beautyShapeDataItem4.beautyShapeId = "Default Beauty Enabled";
            arrayList.add(beautyShapeDataItem4);
            BeautyShapeDataItem beautyShapeDataItem5 = new BeautyShapeDataItem(resources.getString(R.string.sharpness));
            beautyShapeDataItem5.mIconRcsId = R.mipmap.beauty_sharpen;
            beautyShapeDataItem5.beautyShapeId = "Default Sharpen Enabled";
            arrayList.add(beautyShapeDataItem5);
        } else {
            BeautyShapeDataItem beautyShapeDataItem6 = new BeautyShapeDataItem(resources.getString(R.string.strength));
            beautyShapeDataItem6.mIconRcsId = R.mipmap.beauty_strength;
            beautyShapeDataItem6.beautyShapeId = "Strength";
            arrayList.add(beautyShapeDataItem6);
            BeautyShapeDataItem beautyShapeDataItem7 = new BeautyShapeDataItem(resources.getString(R.string.whitening));
            beautyShapeDataItem7.mIconRcsId = R.mipmap.beauty_whitening;
            beautyShapeDataItem7.beautyShapeId = "Whitening";
            arrayList.add(beautyShapeDataItem7);
            BeautyShapeDataItem beautyShapeDataItem8 = new BeautyShapeDataItem(resources.getString(R.string.ruddy));
            beautyShapeDataItem8.mIconRcsId = R.mipmap.beauty_reddening;
            beautyShapeDataItem8.beautyShapeId = "Reddening";
            arrayList.add(beautyShapeDataItem8);
        }
        return arrayList;
    }

    public static ArrayList<BaseInfo> getShapeDataList(Context context) {
        ArrayList<BaseInfo> arrayList = new ArrayList<>();
        BeautyShapeDataItem beautyShapeDataItem = new BeautyShapeDataItem(context.getResources().getString(R.string.cheek_thinning));
        beautyShapeDataItem.mIconRcsId = R.mipmap.shape_cheek_thinning;
        beautyShapeDataItem.beautyShapeId = "Face Size Warp Degree";
        arrayList.add(beautyShapeDataItem);
        BeautyShapeDataItem beautyShapeDataItem2 = new BeautyShapeDataItem(context.getResources().getString(R.string.eye_enlarging));
        beautyShapeDataItem2.mIconRcsId = R.mipmap.shape_eye_enlarging;
        beautyShapeDataItem2.type = "Default";
        beautyShapeDataItem2.beautyShapeId = "Eye Size Warp Degree";
        arrayList.add(beautyShapeDataItem2);
        BeautyShapeDataItem beautyShapeDataItem3 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_chin));
        beautyShapeDataItem3.mIconRcsId = R.mipmap.shape_intensity_chin;
        beautyShapeDataItem3.beautyShapeId = "Chin Length Warp Degree";
        beautyShapeDataItem3.type = "Custom";
        arrayList.add(beautyShapeDataItem3);
        BeautyShapeDataItem beautyShapeDataItem4 = new BeautyShapeDataItem(context.getResources().getString(R.string.face_small));
        beautyShapeDataItem4.mIconRcsId = R.mipmap.shape_face_little;
        beautyShapeDataItem4.beautyShapeId = "Face Length Warp Degree";
        beautyShapeDataItem4.type = "Custom";
        arrayList.add(beautyShapeDataItem4);
        BeautyShapeDataItem beautyShapeDataItem5 = new BeautyShapeDataItem(context.getResources().getString(R.string.face_thin));
        beautyShapeDataItem5.mIconRcsId = R.mipmap.shape_face_thin;
        beautyShapeDataItem5.beautyShapeId = "Face Width Warp Degree";
        beautyShapeDataItem5.type = "Custom";
        arrayList.add(beautyShapeDataItem5);
        BeautyShapeDataItem beautyShapeDataItem6 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_forehead));
        beautyShapeDataItem6.mIconRcsId = R.mipmap.shape_intensity_forehead;
        beautyShapeDataItem6.type = "Custom";
        beautyShapeDataItem6.beautyShapeId = "Forehead Height Warp Degree";
        arrayList.add(beautyShapeDataItem6);
        BeautyShapeDataItem beautyShapeDataItem7 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_nose));
        beautyShapeDataItem7.mIconRcsId = R.mipmap.shape_intensity_nose;
        beautyShapeDataItem7.type = "Custom";
        beautyShapeDataItem7.beautyShapeId = "Nose Width Warp Degree";
        arrayList.add(beautyShapeDataItem7);
        BeautyShapeDataItem beautyShapeDataItem8 = new BeautyShapeDataItem(context.getResources().getString(R.string.nose_long));
        beautyShapeDataItem8.mIconRcsId = R.mipmap.shape_nose_long;
        beautyShapeDataItem8.type = "Custom";
        beautyShapeDataItem8.beautyShapeId = "Nose Length Warp Degree";
        arrayList.add(beautyShapeDataItem8);
        BeautyShapeDataItem beautyShapeDataItem9 = new BeautyShapeDataItem(context.getResources().getString(R.string.eye_corner));
        beautyShapeDataItem9.mIconRcsId = R.mipmap.shape_eye_corner;
        beautyShapeDataItem9.type = "Custom";
        beautyShapeDataItem9.beautyShapeId = "Eye Corner Stretch Degree";
        arrayList.add(beautyShapeDataItem9);
        BeautyShapeDataItem beautyShapeDataItem10 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_mouth));
        beautyShapeDataItem10.mIconRcsId = R.mipmap.shape_intensity_mouth;
        beautyShapeDataItem10.type = "Custom";
        beautyShapeDataItem10.beautyShapeId = "Mouth Size Warp Degree";
        arrayList.add(beautyShapeDataItem10);
        BeautyShapeDataItem beautyShapeDataItem11 = new BeautyShapeDataItem(context.getResources().getString(R.string.mouse_corner));
        beautyShapeDataItem11.mIconRcsId = R.mipmap.shape_mouse_corner;
        beautyShapeDataItem11.type = "Custom";
        beautyShapeDataItem11.beautyShapeId = "Mouth Corner Lift Degree";
        arrayList.add(beautyShapeDataItem11);
        return arrayList;
    }

    public static ArrayList<BeautyShapeDataItem> getFaceUShapeDataList(Context context) {
        ArrayList<BeautyShapeDataItem> arrayList = new ArrayList<>();
        BeautyShapeDataItem beautyShapeDataItem = new BeautyShapeDataItem(context.getResources().getString(R.string.cheek_thinning));
        beautyShapeDataItem.mIconRcsId = R.mipmap.shape_cheek_thinning;
        beautyShapeDataItem.type = "Default";
        beautyShapeDataItem.beautyShapeId = "Cheek Thinning";
        arrayList.add(beautyShapeDataItem);
        BeautyShapeDataItem beautyShapeDataItem2 = new BeautyShapeDataItem(context.getResources().getString(R.string.eye_enlarging));
        beautyShapeDataItem2.mIconRcsId = R.mipmap.shape_eye_enlarging;
        beautyShapeDataItem2.type = "Default";
        beautyShapeDataItem2.beautyShapeId = "Eye Enlarging";
        arrayList.add(beautyShapeDataItem2);
        BeautyShapeDataItem beautyShapeDataItem3 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_forehead));
        beautyShapeDataItem3.mIconRcsId = R.mipmap.shape_intensity_forehead;
        beautyShapeDataItem3.type = "Custom";
        beautyShapeDataItem3.beautyShapeId = "Intensity Forhead";
        arrayList.add(beautyShapeDataItem3);
        BeautyShapeDataItem beautyShapeDataItem4 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_chin));
        beautyShapeDataItem4.mIconRcsId = R.mipmap.shape_intensity_chin;
        beautyShapeDataItem4.beautyShapeId = "Intensity Chin";
        beautyShapeDataItem4.type = "Custom";
        arrayList.add(beautyShapeDataItem4);
        BeautyShapeDataItem beautyShapeDataItem5 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_nose));
        beautyShapeDataItem5.mIconRcsId = R.mipmap.shape_intensity_nose;
        beautyShapeDataItem5.type = "Custom";
        beautyShapeDataItem5.beautyShapeId = "Intensity Nose";
        arrayList.add(beautyShapeDataItem5);
        BeautyShapeDataItem beautyShapeDataItem6 = new BeautyShapeDataItem(context.getResources().getString(R.string.intensity_mouth));
        beautyShapeDataItem6.mIconRcsId = R.mipmap.shape_intensity_mouth;
        beautyShapeDataItem6.type = "Custom";
        beautyShapeDataItem6.beautyShapeId = "Intensity Mouth";
        arrayList.add(beautyShapeDataItem6);
        return arrayList;
    }
}
