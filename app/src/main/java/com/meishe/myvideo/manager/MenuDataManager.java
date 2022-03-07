package com.meishe.myvideo.manager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.Log;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.BeautyShapeInfo;
import com.meishe.myvideo.bean.CaptionStyleInfo;
import com.meishe.myvideo.bean.ComCaptionInfo;
import com.meishe.myvideo.bean.EditAdjustInfo;
import com.meishe.myvideo.bean.EffectInfo;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.bean.TransitionInfo;
import com.meishe.myvideo.bean.menu.BeautyDataHelper;
import com.meishe.myvideo.bean.menu.EditMenuInfo;
import com.meishe.myvideo.util.ParseJsonFile;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.asset.NvAssetManager;
import com.meishe.myvideo.view.menu.bean.MenuInfo;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuDataManager {
    private static final String ASSET_PATH_EFFECT_DREAM = "effect_dream";
    private static final String ASSET_PATH_EFFECT_FRAME = "effect_frame";
    private static final String ASSET_PATH_EFFECT_LIVELY = "effect_lively";
    private static final String ASSET_PATH_EFFECT_SHAKING = "effect_shaking";
    private static final String TAG = "MenuDataManager";
    private NvAssetManager mAssetManager;

    public static List<BaseInfo> getMainMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.main_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.main_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            EditMenuInfo editMenuInfo = new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]);
            editMenuInfo.mMenuIndex = BaseInfo.MENU_INDEX_LEVEL_1;
            arrayList.add(editMenuInfo);
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getMainMenuDatas(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.main_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.main_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new MenuInfo(stringArray[i], obtainTypedArray.getResourceId(i, -1)));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getEditMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_edit_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_edit_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getAudioEditMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_edit_audio_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_edit_audio_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getTransitionData(Context context) {
        ArrayList arrayList = new ArrayList();
        int[] iArr = {R.mipmap.ic_transition_fade, R.mipmap.ic_transition_turning, R.mipmap.ic_transition_swap, R.mipmap.ic_transition_stretch_in, R.mipmap.ic_transition_page_curl, R.mipmap.ic_transition_lens_flare, R.mipmap.ic_transition_star, R.mipmap.ic_transition_dip_to_black, R.mipmap.ic_transition_dip_to_white, R.mipmap.ic_transition_push_to_right, R.mipmap.ic_transition_push_to_left, R.mipmap.ic_transition_upper_left_into};
        TransitionInfo transitionInfo = new TransitionInfo(context.getResources().getString(R.string.more));
        transitionInfo.mIconRcsId = R.mipmap.ic_more;
        transitionInfo.mEffectMode = BaseInfo.EFFECT_MODE_BUILTIN;
        transitionInfo.mEffectType = 5;
        arrayList.add(transitionInfo);
        TransitionInfo transitionInfo2 = new TransitionInfo(context.getResources().getString(R.string.top_menu_no));
        transitionInfo2.mIconRcsId = R.mipmap.ic_no;
        transitionInfo2.mEffectMode = BaseInfo.EFFECT_MODE_BUILTIN;
        transitionInfo2.mEffectType = 5;
        arrayList.add(transitionInfo2);
        String[] strArr = {context.getResources().getString(R.string.trans_fade), context.getResources().getString(R.string.trans_turning), context.getResources().getString(R.string.trans_swap), context.getResources().getString(R.string.trans_stretch_in), context.getResources().getString(R.string.trans_page_curl), context.getResources().getString(R.string.trans_lens_flare), context.getResources().getString(R.string.trans_star), context.getResources().getString(R.string.trans_dip_to_black), context.getResources().getString(R.string.trans_dip_to_white), context.getResources().getString(R.string.trans_push_to_right), context.getResources().getString(R.string.trans_push_to_left), context.getResources().getString(R.string.trans_upper_left_into)};
        String[] strArr2 = {"Fade", "Turning", "Swap", "Stretch In", "Page Curl", "Lens Flare", "Star", "Dip To Black", "Dip To White", "Push To Right", "Push To Top", "Upper Left Into"};
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            TransitionInfo transitionInfo3 = new TransitionInfo(str);
            if (i < iArr.length) {
                transitionInfo3.mIconRcsId = iArr[i];
            }
            transitionInfo3.mEffectMode = BaseInfo.EFFECT_MODE_BUILTIN;
            transitionInfo3.mEffectType = 5;
            transitionInfo3.mName = str;
            transitionInfo3.setEffectName(strArr2[i]);
            arrayList.add(transitionInfo3);
        }
        return arrayList;
    }

    public static List<BaseInfo> getTransition3DData(Context context) {
        ArrayList arrayList = new ArrayList();
        TransitionInfo transitionInfo = new TransitionInfo(context.getResources().getString(R.string.more), "", R.drawable.more_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        transitionInfo.mEffectType = 25;
        arrayList.add(transitionInfo);
        TransitionInfo transitionInfo2 = new TransitionInfo(context.getResources().getString(R.string.no), "", R.drawable.none_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        transitionInfo2.mEffectType = 25;
        arrayList.add(transitionInfo2);
        NvAssetManager sharedInstance = NvAssetManager.sharedInstance();
        sharedInstance.searchLocalAssets(25);
        sharedInstance.searchReservedAssets(25, "transition_3d");
        ArrayList<NvAssetInfo> localData = getLocalData(context, 25, "transition_3d");
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, localData, "transition_3d/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, localData, "transition_3d/info.txt");
        }
        Iterator<NvAssetInfo> it = localData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if (checkAndInstallPackage(next.uuid, next.bundledLocalDirPath, 1)) {
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/transition_3d/" + next.uuid) + ".png";
                }
                TransitionInfo transitionInfo3 = new TransitionInfo(next.mName, next.coverUrl, 0, 1, BaseInfo.EFFECT_MODE_PACKAGE, next.uuid);
                transitionInfo3.mEffectType = 25;
                arrayList.add(transitionInfo3);
            }
        }
        return arrayList;
    }

    private static boolean checkAndInstallPackage(String str, String str2, int i) {
        int installAssetPackage;
        NvsAssetPackageManager assetPackageManager = NvsStreamingContext.getInstance().getAssetPackageManager();
        if (assetPackageManager.getAssetPackageStatus(str, i) == 2 || (installAssetPackage = assetPackageManager.installAssetPackage(str2, null, i, true, new StringBuilder())) == 0) {
            return true;
        }
        Log.e(TAG, "getTransition3DData: 安装失败了。 " + installAssetPackage);
        return false;
    }

    public static List<BaseInfo> getTransitionEffectData(Context context) {
        ArrayList arrayList = new ArrayList();
        TransitionInfo transitionInfo = new TransitionInfo(context.getResources().getString(R.string.more), "", R.drawable.more_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        transitionInfo.mEffectType = 26;
        arrayList.add(transitionInfo);
        TransitionInfo transitionInfo2 = new TransitionInfo(context.getResources().getString(R.string.no), "", R.drawable.none_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        transitionInfo2.mEffectType = 26;
        arrayList.add(transitionInfo2);
        NvAssetManager sharedInstance = NvAssetManager.sharedInstance();
        sharedInstance.searchLocalAssets(26);
        sharedInstance.searchReservedAssets(26, "transition_effect");
        ArrayList<NvAssetInfo> localData = getLocalData(context, 26, "transition_effect");
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, localData, "transition_effect/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, localData, "transition_effect/info.txt");
        }
        TimelineData.getInstance().getMakeRatio();
        Iterator<NvAssetInfo> it = localData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if (checkAndInstallPackage(next.uuid, next.bundledLocalDirPath, 1)) {
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/transition_effect/" + next.uuid) + ".png";
                }
                TransitionInfo transitionInfo3 = new TransitionInfo(next.mName, next.coverUrl, 0, 1, BaseInfo.EFFECT_MODE_PACKAGE, next.uuid);
                transitionInfo3.mEffectType = 26;
                arrayList.add(transitionInfo3);
            }
        }
        return arrayList;
    }

    public static List<BaseInfo> getAddVoiceMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_comb_voice_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_comb_voice_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getEffectMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_effect_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_effect_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getAdjustMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_adjust_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_adjust_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditAdjustInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<EditMenuInfo> getBackgroundMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_background_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_background_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i, -1), stringArray[i]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getMenuData(Context context, int i, int i2) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(i);
        String[] stringArray = context.getResources().getStringArray(i2);
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < stringArray.length; i3++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i3, -1), stringArray[i3]));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getMenuData(Context context, int i, int i2, boolean z) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(i);
        String[] stringArray = context.getResources().getStringArray(i2);
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < stringArray.length; i3++) {
            arrayList.add(new EditMenuInfo(obtainTypedArray.getResourceId(i3, -1), stringArray[i3], z));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    private static List<BaseInfo> getThemeData(Context context) {
        ArrayList arrayList = new ArrayList();
        setBaseData(context, arrayList, 1);
        ArrayList<NvAssetInfo> localData = getLocalData(context, 1, "theme");
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, localData, "theme/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, localData, "theme/info.txt");
        }
        int aspectRatio = TimelineDataUtil.getAspectRatio();
        Iterator<NvAssetInfo> it = localData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if ((next.aspectRatio & aspectRatio) != 0) {
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/theme/" + next.uuid) + ".png";
                }
                EffectInfo effectInfo = new EffectInfo(next.mName, next.coverUrl, 0, 1, BaseInfo.EFFECT_MODE_PACKAGE, next.uuid);
                effectInfo.mEffectType = 1;
                arrayList.add(effectInfo);
            }
        }
        return arrayList;
    }

    private static List<BaseInfo> getFilterData(Context context) {
        ArrayList arrayList = new ArrayList();
        setBaseData(context, arrayList, 2);
        ArrayList<NvAssetInfo> localData = getLocalData(context, 2, "filter");
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, localData, "filter/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, localData, "filter/info.txt");
        }
        Iterator<NvAssetInfo> it = localData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if ((next.aspectRatio & 31) != 0) {
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/filter/" + next.uuid) + ".png";
                }
                arrayList.add(new EffectInfo(next.mName, next.coverUrl, 0, 2, BaseInfo.EFFECT_MODE_BUNDLE, next.uuid));
            }
        }
        return arrayList;
    }

    private static List<BaseInfo> getEffectData(Context context, String str, int i) {
        ArrayList arrayList = new ArrayList();
        addMoreData(context, arrayList, i);
        ArrayList<NvAssetInfo> localData = getLocalData(context, i, str);
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, localData, str + "/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, localData, str + "/info.txt");
        }
        int aspectRatio = TimelineDataUtil.getAspectRatio();
        Iterator<NvAssetInfo> it = localData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if ((next.aspectRatio & aspectRatio) != 0) {
                if (next.isReserved()) {
                    next.coverUrl = (("file:///android_asset/" + str + "/") + next.uuid) + ".png";
                }
                arrayList.add(new EffectInfo(next.mName, next.coverUrl, 0, i, BaseInfo.EFFECT_MODE_BUNDLE, next.uuid));
            }
        }
        return arrayList;
    }

    private static ArrayList<NvAssetInfo> getLocalData(Context context, int i, String str) {
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        init.searchReservedAssets(i, str);
        init.searchLocalAssets(i);
        return init.getUsableAssets(i, 31, 0);
    }

    public static ArrayList<CaptionStyleInfo> getCaptionStyleData(Context context) {
        ArrayList<CaptionStyleInfo> arrayList = new ArrayList<>();
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        init.searchLocalAssets(3);
        init.searchReservedAssets(3, "captionstyle");
        ArrayList<NvAssetInfo> usableAssets = init.getUsableAssets(3, 31, 0);
        ArrayList<ParseJsonFile.FxJsonFileInfo.JsonFileInfo> readBundleFxJsonFile = ParseJsonFile.readBundleFxJsonFile(context, "captionstyle/info.json");
        if (readBundleFxJsonFile != null) {
            Iterator<ParseJsonFile.FxJsonFileInfo.JsonFileInfo> it = readBundleFxJsonFile.iterator();
            while (it.hasNext()) {
                ParseJsonFile.FxJsonFileInfo.JsonFileInfo next = it.next();
                Iterator<NvAssetInfo> it2 = usableAssets.iterator();
                while (it2.hasNext()) {
                    NvAssetInfo next2 = it2.next();
                    if (next2 != null && !TextUtils.isEmpty(next2.uuid) && next2.isReserved && next2.uuid.equals(next.getFxPackageId())) {
                        next2.mName = next.getName();
                        next2.aspectRatio = Integer.parseInt(next.getFitRatio());
                        next2.coverUrl = "file:///android_asset/captionstyle/" + next.getImageName();
                    }
                }
            }
        }
        Iterator<NvAssetInfo> it3 = usableAssets.iterator();
        while (it3.hasNext()) {
            NvAssetInfo next3 = it3.next();
            if (next3 != null && !TextUtils.isEmpty(next3.uuid)) {
                CaptionStyleInfo captionStyleInfo = new CaptionStyleInfo();
                captionStyleInfo.setAsset(next3);
                captionStyleInfo.mAssetMode = 2;
                arrayList.add(captionStyleInfo);
            }
        }
        CaptionStyleInfo captionStyleInfo2 = new CaptionStyleInfo();
        captionStyleInfo2.mName = context.getResources().getString(R.string.more);
        arrayList.add(0, captionStyleInfo2);
        CaptionStyleInfo captionStyleInfo3 = new CaptionStyleInfo();
        captionStyleInfo3.mName = context.getResources().getString(R.string.top_menu_no);
        arrayList.add(1, captionStyleInfo3);
        return arrayList;
    }

    private static void setBaseData(Context context, List<BaseInfo> list, int i) {
        EffectInfo effectInfo = new EffectInfo(context.getResources().getString(R.string.more), "", R.drawable.more_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        effectInfo.mEffectType = i;
        list.add(effectInfo);
        EffectInfo effectInfo2 = new EffectInfo(context.getResources().getString(R.string.no), "", R.drawable.none_icon, BaseInfo.EFFECT_MODE_BUILTIN);
        effectInfo2.mEffectType = i;
        list.add(effectInfo2);
    }

    private static void addMoreData(Context context, List<BaseInfo> list, int i) {
        EffectInfo effectInfo;
        if (isZh(context)) {
            effectInfo = new EffectInfo("更多", "", R.mipmap.ic_more, BaseInfo.EFFECT_MODE_BUILTIN);
        } else {
            effectInfo = new EffectInfo("more", "", R.mipmap.ic_more, BaseInfo.EFFECT_MODE_BUILTIN);
        }
        effectInfo.mEffectType = i;
        list.add(effectInfo);
    }

    public static boolean isZh(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    public static List<BaseInfo> getMainMenuData(String str, Context context) {
        if (TextUtils.isEmpty(str) || context == null) {
            return null;
        }
        Resources resources = context.getResources();
        if (str.equals(resources.getString(R.string.main_menu_name_edit))) {
            return getEditMenuData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_edit_video))) {
            return getMenuData(context, R.array.sub_edit_video_menu_icon, R.array.sub_edit_video_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_theme))) {
            return getThemeData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_edit))) {
            return getEditMenuData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_filter))) {
            return getMenuData(context, R.array.menu_tab_filter_and_adjust_effect_icon, R.array.menu_tab_filter_and_adjust_effect_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_fx))) {
            return getEffectMenuData(context);
        }
        if (str.equals(resources.getString(R.string.effect_frame))) {
            return getEffectData(context, ASSET_PATH_EFFECT_FRAME, 18);
        }
        if (str.equals(resources.getString(R.string.effect_dream))) {
            return getEffectData(context, ASSET_PATH_EFFECT_DREAM, 19);
        }
        if (str.equals(resources.getString(R.string.effect_lively))) {
            return getEffectData(context, ASSET_PATH_EFFECT_LIVELY, 20);
        }
        if (str.equals(resources.getString(R.string.effect_shaking))) {
            return getEffectData(context, ASSET_PATH_EFFECT_SHAKING, 21);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_adjust))) {
            return getMenuData(context, R.array.menu_tab_filter_and_adjust_effect_icon, R.array.menu_tab_filter_and_adjust_effect_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_background))) {
            return getMenuData(context, R.array.sub_background_menu_icon, R.array.sub_background_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_caption)) || str.equals(resources.getString(R.string.main_menu_name_com_caption)) || str.equals(resources.getString(R.string.main_menu_name_sticker))) {
            return getMenuData(context, R.array.sub_caption_menu_icon, R.array.sub_caption_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_music)) || str.equals(resources.getString(R.string.main_menu_name_dubbing))) {
            return getMenuData(context, R.array.sub_comb_voice_menu_icon, R.array.sub_comb_voice_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_water_mark))) {
            return getMenuData(context, R.array.menu_tab_watermark_effect_icon, R.array.menu_tab_watermark_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_picture_in_picture))) {
            return getMenuData(context, R.array.menu_tab_pic_in_pic_icon, R.array.menu_tab_pic_in_pic_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_audio_edit))) {
            return getMenuData(context, R.array.sub_edit_audio_menu_icon, R.array.sub_edit_audio_menu_name);
        }
        return null;
    }

    public static List<BaseInfo> getData(String str, Context context) {
        if (TextUtils.isEmpty(str) || context == null) {
            return null;
        }
        Resources resources = context.getResources();
        if (str.equals(resources.getString(R.string.main_menu_name_edit))) {
            return getEditMenuData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_edit_video))) {
            return getMenuData(context, R.array.sub_edit_video_menu_icon, R.array.sub_edit_video_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_theme))) {
            return getThemeData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_edit))) {
            return getEditMenuData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_filter))) {
            return getFilterData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_fx))) {
            return getEffectMenuData(context);
        }
        if (str.equals(resources.getString(R.string.effect_frame))) {
            return getEffectData(context, ASSET_PATH_EFFECT_FRAME, 18);
        }
        if (str.equals(resources.getString(R.string.effect_dream))) {
            return getEffectData(context, ASSET_PATH_EFFECT_DREAM, 19);
        }
        if (str.equals(resources.getString(R.string.effect_lively))) {
            return getEffectData(context, ASSET_PATH_EFFECT_LIVELY, 20);
        }
        if (str.equals(resources.getString(R.string.effect_shaking))) {
            return getEffectData(context, ASSET_PATH_EFFECT_SHAKING, 21);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_adjust))) {
            return getAdjustMenuData(context);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_background))) {
            return getMenuData(context, R.array.sub_background_menu_icon, R.array.sub_background_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_music)) || str.equals(resources.getString(R.string.main_menu_name_dubbing))) {
            return getMenuData(context, R.array.sub_comb_voice_menu_icon, R.array.sub_comb_voice_menu_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_water_mark))) {
            return getMenuData(context, R.array.menu_tab_watermark_effect_icon, R.array.menu_tab_watermark_name);
        }
        if (str.equals(resources.getString(R.string.main_menu_name_picture_in_picture))) {
            return getMenuData(context, R.array.menu_tab_pic_in_pic_icon, R.array.menu_tab_pic_in_pic_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_audio_edit))) {
            return getMenuData(context, R.array.sub_edit_audio_menu_icon, R.array.sub_edit_audio_menu_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_caption_edit))) {
            return getMenuData(context, R.array.sub_edit_caption_menu_icon, R.array.sub_edit_caption_menu_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_sticker_edit))) {
            return getMenuData(context, R.array.sub_edit_sticker_menu_icon, R.array.sub_edit_sticker_menu_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_compound_caption_edit))) {
            return getMenuData(context, R.array.sub_edit_compound_caption_menu_icon, R.array.sub_edit_compound_caption_menu_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_effect_edit))) {
            return getMenuData(context, R.array.sub_edit_effect_menu_icon, R.array.sub_edit_effect_menu_name);
        }
        if (str.equals(resources.getString(R.string.sub_menu_pip_edit))) {
            return getEditMenuData(context);
        }
        return null;
    }

    public static List<BaseInfo> getBeautyMenuData(Context context) {
        return BeautyDataHelper.getBeautyDataListByType(context, 1);
    }

    public static List<BaseInfo> getShapeMenuData(Context context) {
        return BeautyDataHelper.getShapeDataList(context);
    }

    public static ArrayList<NvAssetInfo> getAnimateStickerDataList(Context context) {
        ArrayList<NvAssetInfo> arrayList = new ArrayList<>();
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        init.searchLocalAssets(4);
        init.searchReservedAssets(4, CommonData.CLIP_STICKER);
        ArrayList<NvAssetInfo> usableAssets = init.getUsableAssets(4, 31, 0);
        if (usableAssets != null && usableAssets.size() > 0) {
            Iterator<NvAssetInfo> it = usableAssets.iterator();
            while (it.hasNext()) {
                NvAssetInfo next = it.next();
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/sticker/" + next.uuid) + ".png";
                }
            }
            arrayList = usableAssets;
        }
        NvAssetInfo nvAssetInfo = new NvAssetInfo();
        nvAssetInfo.mName = context.getResources().getString(R.string.more);
        arrayList.add(0, nvAssetInfo);
        return arrayList;
    }

    public static ArrayList<NvAssetInfo> getCustomAnimateStickerDataList(Context context) {
        ArrayList<NvAssetInfo> arrayList = new ArrayList<>();
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        init.searchLocalAssets(12);
        init.searchReservedAssets(12, "customsticker");
        ArrayList<NvAssetInfo> usableAssets = init.getUsableAssets(12, 31, 0);
        if (usableAssets == null || usableAssets.size() <= 0) {
            return arrayList;
        }
        Iterator<NvAssetInfo> it = usableAssets.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if (next.isReserved()) {
                next.coverUrl = ("file:///android_asset/sticker/" + next.uuid) + ".png";
            }
        }
        return usableAssets;
    }

    public static ArrayList<BaseInfo> getCompoundCaptionDataList(Context context) {
        ArrayList<BaseInfo> arrayList = new ArrayList<>();
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        initAssetData(init, 16, "compoundcaption");
        ArrayList<NvAssetInfo> usableAssets = init.getUsableAssets(16, 31, 0);
        if (usableAssets != null && usableAssets.size() > 0) {
            Iterator<NvAssetInfo> it = usableAssets.iterator();
            while (it.hasNext()) {
                NvAssetInfo next = it.next();
                if (next.isReserved()) {
                    next.coverUrl = ("file:///android_asset/compoundcaption/" + next.uuid) + ".png";
                }
                ComCaptionInfo comCaptionInfo = new ComCaptionInfo();
                comCaptionInfo.setAsset(next);
                comCaptionInfo.mPicPath = next.coverUrl;
                arrayList.add(comCaptionInfo);
            }
        }
        arrayList.add(0, new ComCaptionInfo(context.getString(R.string.more)));
        return arrayList;
    }

    public static ArrayList<BaseInfo> getBeautyShapeDataList(Context context) {
        ArrayList<BaseInfo> arrayList = new ArrayList<>();
        BeautyShapeInfo beautyShapeInfo = new BeautyShapeInfo();
        beautyShapeInfo.mName = context.getResources().getString(R.string.cheek_thinning);
        beautyShapeInfo.mIconRcsId = R.mipmap.ic_cheek_thinning;
        beautyShapeInfo.mBeautyShapeId = "Face Size Warp Degree";
        beautyShapeInfo.mStrength = -0.6d;
        beautyShapeInfo.mDefaultValue = beautyShapeInfo.mStrength;
        arrayList.add(beautyShapeInfo);
        BeautyShapeInfo beautyShapeInfo2 = new BeautyShapeInfo();
        beautyShapeInfo2.mName = context.getResources().getString(R.string.eye_enlarging);
        beautyShapeInfo2.mIconRcsId = R.mipmap.ic_eye_enlarging;
        beautyShapeInfo2.mType = "Default";
        beautyShapeInfo2.mBeautyShapeId = "Eye Size Warp Degree";
        beautyShapeInfo2.mStrength = 0.5d;
        beautyShapeInfo2.mDefaultValue = beautyShapeInfo2.mStrength;
        arrayList.add(beautyShapeInfo2);
        BeautyShapeInfo beautyShapeInfo3 = new BeautyShapeInfo();
        beautyShapeInfo3.mName = context.getResources().getString(R.string.intensity_chin);
        beautyShapeInfo3.mIconRcsId = R.mipmap.ic_intensity_chin;
        beautyShapeInfo3.mBeautyShapeId = "Chin Length Warp Degree";
        beautyShapeInfo3.mType = "Custom";
        arrayList.add(beautyShapeInfo3);
        BeautyShapeInfo beautyShapeInfo4 = new BeautyShapeInfo();
        beautyShapeInfo4.mName = context.getResources().getString(R.string.intensity_forehead);
        beautyShapeInfo4.mIconRcsId = R.mipmap.ic_intensity_forehead;
        beautyShapeInfo4.mType = "Custom";
        beautyShapeInfo4.mBeautyShapeId = "Forehead Height Warp Degree";
        beautyShapeInfo4.mStrength = 0.25d;
        beautyShapeInfo4.mDefaultValue = beautyShapeInfo4.mStrength;
        arrayList.add(beautyShapeInfo4);
        BeautyShapeInfo beautyShapeInfo5 = new BeautyShapeInfo();
        beautyShapeInfo5.mName = context.getResources().getString(R.string.intensity_nose);
        beautyShapeInfo5.mIconRcsId = R.mipmap.ic_intensity_nose;
        beautyShapeInfo5.mType = "Custom";
        beautyShapeInfo5.mBeautyShapeId = "Nose Width Warp Degree";
        beautyShapeInfo5.mStrength = -0.5d;
        beautyShapeInfo5.mDefaultValue = beautyShapeInfo4.mStrength;
        arrayList.add(beautyShapeInfo5);
        BeautyShapeInfo beautyShapeInfo6 = new BeautyShapeInfo();
        beautyShapeInfo6.mName = context.getResources().getString(R.string.intensity_mouth);
        beautyShapeInfo6.mIconRcsId = R.mipmap.intensity_mouth;
        beautyShapeInfo6.mType = "Custom";
        beautyShapeInfo6.mBeautyShapeId = "Mouth Size Warp Degree";
        arrayList.add(beautyShapeInfo6);
        return arrayList;
    }

    private static void initAssetData(NvAssetManager nvAssetManager, int i, String str) {
        nvAssetManager.searchLocalAssets(i);
        nvAssetManager.searchReservedAssets(i, str);
    }

    private ArrayList<NvAssetInfo> getAssetsDataList(NvAssetManager nvAssetManager, int i) {
        return nvAssetManager.getUsableAssets(i, 31, 0);
    }
}
