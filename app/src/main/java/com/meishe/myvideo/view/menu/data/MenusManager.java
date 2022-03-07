package com.meishe.myvideo.view.menu.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.bean.BaseInfo;
import com.meishe.myvideo.bean.EffectInfo;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.asset.NvAssetManager;
import com.meishe.myvideo.view.menu.bean.MenuInfo;
import com.meishe.myvideo.view.menu.bean.TableFunInfo;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenusManager {
    public static List<BaseInfo> getMainMenuData(Context context) {
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.main_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.main_menu_name);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new MenuInfo(stringArray[i], obtainTypedArray.getResourceId(i, -1)));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getLocalData(String str, Context context) {
        String[] strArr;
        TypedArray typedArray;
        ArrayList arrayList = new ArrayList();
        if (context.getResources().getString(R.string.main_menu_name_theme).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_menu_theme_icon);
            strArr = context.getResources().getStringArray(R.array.sub_main_menu_name);
        } else {
            typedArray = null;
            strArr = null;
        }
        if (context.getResources().getString(R.string.main_menu_name_edit).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_edit_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_edit_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_filter).equals(str) || context.getResources().getString(R.string.main_menu_name_adjust).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_filter_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_filter_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_fx).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_effect_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_effect_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_sticker).equals(str) || context.getResources().getString(R.string.main_menu_name_caption).equals(str) || context.getResources().getString(R.string.main_menu_name_com_caption).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_comb_effect_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_comb_effect_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_water_mark).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_watermark_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_watermark_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_music).equals(str) || context.getResources().getString(R.string.main_menu_name_dubbing).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_comb_voice_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_comb_voice_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_picture_in_picture).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_pic_in_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_pic_in_menu_name);
        }
        if (context.getResources().getString(R.string.main_menu_name_background).equals(str)) {
            typedArray = context.getResources().obtainTypedArray(R.array.sub_background_menu_icon);
            strArr = context.getResources().getStringArray(R.array.sub_background_menu_name);
        }
        if (strArr == null || typedArray == null) {
            return null;
        }
        for (int i = 0; i < strArr.length; i++) {
            arrayList.add(new MenuInfo(strArr[i], typedArray.getResourceId(i, -1)));
        }
        typedArray.recycle();
        return arrayList;
    }

    public static List<BaseInfo> getShowData(String str, Context context) {
        if (!TextUtils.isEmpty(str) && context != null) {
            if (str.equals(context.getResources().getString(R.string.main_menu_name_theme))) {
                return getThemeData(context);
            }
            if (str.equals(context.getResources().getString(R.string.main_menu_name_adjust))) {
                return getAdjustData(context);
            }
        }
        return null;
    }

    private static List<BaseInfo> getAdjustData(Context context) {
        ArrayList arrayList = new ArrayList();
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.sub_adjust_menu_icon);
        String[] stringArray = context.getResources().getStringArray(R.array.sub_adjust_menu_name);
        if (stringArray == null || obtainTypedArray == null) {
            return null;
        }
        for (int i = 0; i < stringArray.length; i++) {
            arrayList.add(new MenuInfo(stringArray[i], obtainTypedArray.getResourceId(i, -1)));
        }
        obtainTypedArray.recycle();
        return arrayList;
    }

    private static List<BaseInfo> getThemeData(Context context) {
        ArrayList arrayList = new ArrayList();
        setBaseData(context, arrayList, 1);
        ArrayList<NvAssetInfo> assestData = getAssestData(context, 1, "theme");
        if (isZh(context)) {
            Util.getBundleFilterInfo(context, assestData, "theme/info_Zh.txt");
        } else {
            Util.getBundleFilterInfo(context, assestData, "theme/info.txt");
        }
        int makeRatio = TimelineData.getInstance().getMakeRatio();
        Iterator<NvAssetInfo> it = assestData.iterator();
        while (it.hasNext()) {
            NvAssetInfo next = it.next();
            if ((next.aspectRatio & makeRatio) != 0) {
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

    private static ArrayList<NvAssetInfo> getAssestData(Context context, int i, String str) {
        NvAssetManager init = NvAssetManager.init(context);
        if (init == null) {
            return null;
        }
        init.searchReservedAssets(i, str);
        init.searchLocalAssets(i);
        return init.getUsableAssets(i, 31, 0);
    }

    private static void setBaseData(Context context, List<BaseInfo> list, int i) {
        MenuInfo menuInfo;
        MenuInfo menuInfo2;
        if (isZh(context)) {
            menuInfo = new MenuInfo(context.getResources().getString(R.string.more), "", R.mipmap.ic_more, BaseInfo.EFFECT_MODE_BUILTIN);
        } else {
            menuInfo = new MenuInfo("more", "", R.mipmap.ic_more, BaseInfo.EFFECT_MODE_BUILTIN);
        }
        menuInfo.mEffectType = i;
        list.add(menuInfo);
        if (isZh(context)) {
            menuInfo2 = new MenuInfo("无", "", R.mipmap.ic_no, BaseInfo.EFFECT_MODE_BUILTIN);
        } else {
            menuInfo2 = new MenuInfo("no", "", R.mipmap.ic_no, BaseInfo.EFFECT_MODE_BUILTIN);
        }
        menuInfo2.mEffectType = i;
        list.add(menuInfo2);
    }

    public static boolean isZh(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    public static List<BaseInfo> getSticksDataByName(Context context, String str) {
        TableFunInfo tableFunInfo;
        TableFunInfo tableFunInfo2;
        ArrayList arrayList = new ArrayList();
        if (context.getResources().getString(R.string.fragment_menu_table_all).equals(str)) {
            if (isZh(context)) {
                tableFunInfo2 = new TableFunInfo(context.getResources().getString(R.string.more), R.mipmap.ic_more);
            } else {
                tableFunInfo2 = new TableFunInfo("more", R.mipmap.ic_more);
            }
            arrayList.add(tableFunInfo2);
        } else if (context.getResources().getString(R.string.fragment_menu_table_custom).equals(str)) {
            if (isZh(context)) {
                tableFunInfo = new TableFunInfo("添加", R.mipmap.ic_add_material);
            } else {
                tableFunInfo = new TableFunInfo("more", R.mipmap.ic_add_material);
            }
            arrayList.add(tableFunInfo);
        }
        return arrayList;
    }

    public static List<String> getTabData(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        String[] stringArray = context.getResources().getString(R.string.main_menu_name_sticker).equals(str) ? context.getResources().getStringArray(R.array.menu_tab_stickers) : null;
        if (context.getResources().getString(R.string.main_menu_name_caption).equals(str)) {
            stringArray = context.getResources().getStringArray(R.array.menu_tab_caption);
        }
        if (context.getResources().getString(R.string.main_menu_name_water_mark).equals(str)) {
            stringArray = context.getResources().getStringArray(R.array.menu_tab_watermark_name);
        }
        if (stringArray == null) {
            return arrayList;
        }
        for (String str2 : stringArray) {
            arrayList.add(str2);
        }
        return arrayList;
    }

    public static List<BaseInfo> getCaptionsDataByName(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        context.getResources().getString(R.string.menu_tab_caption_input).equals(str);
        if (context.getResources().getString(R.string.menu_tab_caption_decorate).equals(str)) {
            addTabMore(context, arrayList);
            addTabNo(context, arrayList);
        }
        context.getResources().getString(R.string.menu_tab_caption_colour).equals(str);
        context.getResources().getString(R.string.menu_tab_caption_contour).equals(str);
        context.getResources().getString(R.string.menu_tab_caption_font).equals(str);
        context.getResources().getString(R.string.menu_tab_caption_space).equals(str);
        context.getResources().getString(R.string.menu_tab_caption_location).equals(str);
        return arrayList;
    }

    public static List<BaseInfo> getWaterMarkData(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        if (context.getResources().getString(R.string.sub_menu_tab_watermark).equals(str)) {
            addTabMore(context, arrayList);
        } else if (context.getResources().getString(R.string.sub_menu_tab_watermark_effect).equals(str)) {
            addTabNo(context, arrayList);
            TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.menu_tab_watermark_effect_icon);
            String[] stringArray = context.getResources().getStringArray(R.array.menu_tab_watermark_effect_name);
            for (int i = 0; i < stringArray.length; i++) {
                arrayList.add(new TableFunInfo(stringArray[i], obtainTypedArray.getResourceId(i, -1)));
            }
            obtainTypedArray.recycle();
        }
        return arrayList;
    }

    private static void addTabMore(Context context, List<BaseInfo> list) {
        TableFunInfo tableFunInfo;
        if (isZh(context)) {
            tableFunInfo = new TableFunInfo(context.getResources().getString(R.string.more), R.mipmap.ic_more);
        } else {
            tableFunInfo = new TableFunInfo("more", R.mipmap.ic_more);
        }
        list.add(tableFunInfo);
    }

    private static void addTabNo(Context context, List<BaseInfo> list) {
        TableFunInfo tableFunInfo;
        if (isZh(context)) {
            tableFunInfo = new TableFunInfo("无", R.mipmap.ic_no);
        } else {
            tableFunInfo = new TableFunInfo("no", R.mipmap.ic_no);
        }
        list.add(tableFunInfo);
    }
}
