package com.meishe.draft;

import android.content.Context;
import android.text.TextUtils;
import com.meicam.sdk.NvsTimeline;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.Logger;
import com.meishe.common.utils.TimeFormatUtil;
import com.meishe.draft.data.DraftData;
import com.meishe.draft.util.DraftFileUtil;
import com.meishe.engine.bean.ClipInfo;
import com.meishe.engine.bean.MeicamVideoClip;
import com.meishe.engine.bean.TimelineData;
import com.meishe.engine.bean.TimelineDataUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class DraftManager {
    private static final String JSON_KEY_COVER_PATH = "coverPath";
    private static final String JSON_KEY_DURING = "projectDuring";
    private static final String JSON_KEY_FILE_SIZE = "fileSize";
    private static final String JSON_KEY_LAST_MODIFY_TIME = "lastModifiedTime";
    private static final String TAG = "DraftManager";
    private static volatile DraftManager sDraftLocal;
    private String mCoverPath;
    private long timeLineDuring = -1;
    private NvsTimeline timeline;

    public static DraftManager getInstance() {
        if (sDraftLocal == null) {
            synchronized (DraftManager.class) {
                if (sDraftLocal == null) {
                    sDraftLocal = new DraftManager();
                }
            }
        }
        return sDraftLocal;
    }

    public void saveDraftData(String str, long j) {
        TimelineData.getInstance().setProjectId(String.valueOf(UUID.randomUUID()));
        TimelineData.getInstance().setProjectDuring(getDraftDuring());
        DraftFileUtil.saveConfigJson(addExtraParamToJson(TimelineData.getInstance().toJson(), String.valueOf(j)), str);
    }

    public DraftData copyDraft(Context context, DraftData draftData) {
        DraftData clone = draftData.clone();
        if (clone == null) {
            return null;
        }
        String jsonData = draftData.getJsonData();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            JSONObject jSONObject = new JSONObject(jsonData);
            jSONObject.put(JSON_KEY_LAST_MODIFY_TIME, currentTimeMillis);
            jsonData = jSONObject.toString();
            clone.setJsonData(jsonData);
        } catch (JSONException e) {
            Logger.e(TAG, "addExtraParamToJson error: " + e.fillInStackTrace());
        }
        clone.setDirPath(DraftFileUtil.saveConfigJson(jsonData, DraftFileUtil.getDirPath(context, currentTimeMillis)));
        return clone;
    }

    private String addExtraParamToJson(String str, String str2) {
        String printSize = DraftFileUtil.getPrintSize(getDraftFileSize());
        String coverPath = getCoverPath();
        String draftDuring = getDraftDuring();
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.put(JSON_KEY_FILE_SIZE, printSize);
            jSONObject.put(JSON_KEY_LAST_MODIFY_TIME, str2);
            jSONObject.put(JSON_KEY_COVER_PATH, coverPath);
            jSONObject.put(JSON_KEY_DURING, draftDuring);
            return jSONObject.toString();
        } catch (JSONException e) {
            Logger.e(TAG, "addExtraParamToJson error: " + e.fillInStackTrace());
            return str;
        }
    }

    private String addExtraParamToJson(String str) {
        String printSize = DraftFileUtil.getPrintSize(getDraftFileSize());
        String coverPath = getCoverPath();
        String draftDuring = getDraftDuring();
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.put(JSON_KEY_FILE_SIZE, printSize);
            jSONObject.put(JSON_KEY_COVER_PATH, coverPath);
            jSONObject.put(JSON_KEY_DURING, draftDuring);
            return jSONObject.toString();
        } catch (JSONException e) {
            Logger.e(TAG, "addExtraParamToJson error: " + e.fillInStackTrace());
            return str;
        }
    }

    private String getCoverPath() {
        return this.mCoverPath;
    }

    public void setCoverPath(String str) {
        this.mCoverPath = str;
    }

    public List<DraftData> getDraftData(Context context) {
        String draftDirPath = DraftFileUtil.getDraftDirPath(context);
        ArrayList arrayList = null;
        if (TextUtils.isEmpty(draftDirPath)) {
            return null;
        }
        File file = new File(draftDirPath);
        if (!file.exists()) {
            return null;
        }
        String[] list = file.list();
        if (!(list == null || list.length == 0)) {
            arrayList = new ArrayList();
            for (String str : list) {
                DraftData draftData = new DraftData();
                String str2 = draftDirPath + File.separator + str;
                draftData.setDirPath(str2);
                String stringFromSDcardFile = DraftFileUtil.getStringFromSDcardFile(str2 + File.separator + "config.json");
                if (!TextUtils.isEmpty(stringFromSDcardFile)) {
                    draftData.setJsonData(stringFromSDcardFile);
                    draftData.setFileName(str);
                    try {
                        JSONObject jSONObject = new JSONObject(stringFromSDcardFile);
                        Object opt = jSONObject.opt(JSON_KEY_FILE_SIZE);
                        if (opt != null && (opt instanceof String)) {
                            draftData.setFileSize((String) opt);
                        }
                        Object opt2 = jSONObject.opt(JSON_KEY_LAST_MODIFY_TIME);
                        if (opt2 != null && (opt2 instanceof String)) {
                            draftData.setLastModifyTime(TimeFormatUtil.getCurrentTime(Long.parseLong((String) opt2), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
                            draftData.setLasetModifyTimeLong(Long.parseLong((String) opt2));
                        }
                        Object opt3 = jSONObject.opt(JSON_KEY_COVER_PATH);
                        if (opt3 != null && (opt3 instanceof String)) {
                            draftData.setCoverPath((String) opt3);
                        }
                        Object opt4 = jSONObject.opt(JSON_KEY_DURING);
                        if (opt4 != null && (opt4 instanceof String)) {
                            draftData.setDuration((String) opt4);
                        }
                    } catch (Exception e) {
                        Logger.e(TAG, "getDraftData error: " + e.fillInStackTrace());
                    }
                    arrayList.add(draftData);
                }
            }
            Collections.sort(arrayList, new Comparator<DraftData>() {
                /* class com.meishe.draft.DraftManager.AnonymousClass1 */

                public int compare(DraftData draftData, DraftData draftData2) {
                    if (draftData.getLasetModifyTimeLong() > draftData2.getLasetModifyTimeLong()) {
                        return -1;
                    }
                    return draftData.getLasetModifyTimeLong() < draftData2.getLasetModifyTimeLong() ? 1 : 0;
                }
            });
        }
        return arrayList;
    }

    private long getDraftFileSize() {
        long groupFileSize = getGroupFileSize(TimelineDataUtil.getMainTrackVideoClip());
        long j = -1;
        if (groupFileSize > 0) {
            j = -1 + groupFileSize;
        }
        long groupFileSize2 = getGroupFileSize(TimelineDataUtil.getPipTrackVideoClip());
        return groupFileSize2 > 0 ? j + groupFileSize2 : j;
    }

    private long getGroupFileSize(List<ClipInfo> list) {
        long j = -1;
        if (CollectionUtils.isEmpty(list)) {
            return -1;
        }
        Iterator<ClipInfo> it = list.iterator();
        while (it.hasNext()) {
            long fileSize = DraftFileUtil.getFileSize(((MeicamVideoClip) it.next()).getFilePath());
            if (fileSize > 0) {
                j += fileSize;
            }
        }
        return j;
    }

    private String getDraftDuring() {
        int i = (int) ((this.timeLineDuring / 1000) / 1000);
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        if (i4 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2));
    }

    public void setTimeLineDuring(long j) {
        this.timeLineDuring = j;
    }

    public void editDraft(String str, long j) {
        DraftFileUtil.saveConfigJson(addExtraParamToJson(TimelineData.getInstance().toJson(), String.valueOf(j)), str);
    }

    public void editDraftNoModifiedTime(String str) {
        String str2;
        String json = TimelineData.getInstance().toJson();
        String lastModifiedTime = TimelineData.getInstance().getLastModifiedTime();
        if (TextUtils.isEmpty(lastModifiedTime)) {
            str2 = addExtraParamToJson(json, lastModifiedTime);
        } else {
            str2 = addExtraParamToJson(json);
        }
        DraftFileUtil.saveConfigJson(str2, str);
    }
}
