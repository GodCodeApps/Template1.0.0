package com.meishe.myvideo.util.asset;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.myvideo.bean.NvAssetInfo;
import com.meishe.myvideo.bean.down.AssetListDownResponse;
import com.meishe.myvideo.util.PathUtils;
import com.meishe.myvideo.util.asset.NvHttpRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class NvAssetManager implements NvHttpRequest.NvHttpRequestListener, NvsAssetPackageManager.AssetPackageManagerCallback {
    private static final int ASSET_DOWNLOAD_FAILED = 2005;
    private static final int ASSET_DOWNLOAD_PROGRESS = 2003;
    private static final int ASSET_DOWNLOAD_SUCCESS = 2004;
    private static final int ASSET_LIST_REQUEST_FAILED = 2002;
    private static final int ASSET_LIST_REQUEST_SUCCESS = 2001;
    private static final String NV_CDN_URL = "https://assets.meishesdk.com";
    private static final String NV_DOMAIN_URL = "https://assets.meishesdk.com";
    private static final String TAG = "NvAssetManager ";
    private static final String assetdata = "assetdata";
    private static final String customStickerInfo = "/customStickerInfo.json";
    private static NvAssetManager m_instance;
    private static SharedPreferences preferences;
    private HashMap<String, ArrayList<NvAssetInfo>> assetDict;
    private ArrayList<NvCustomStickerInfo> customStickerArray;
    private int downloadingAssetsCounter;
    private boolean isLocalAssetSearchedARScene;
    private boolean isLocalAssetSearchedAnimatedSticker;
    private boolean isLocalAssetSearchedCaption;
    private boolean isLocalAssetSearchedCaptureScene;
    private boolean isLocalAssetSearchedCompoundCaption;
    private boolean isLocalAssetSearchedCustomAnimatedSticker;
    private boolean isLocalAssetSearchedEffetDream;
    private boolean isLocalAssetSearchedEffetFrame;
    private boolean isLocalAssetSearchedEffetLively;
    private boolean isLocalAssetSearchedEffetShaking;
    private boolean isLocalAssetSearchedFace1Sticker;
    private boolean isLocalAssetSearchedFaceSticker;
    private boolean isLocalAssetSearchedFilter;
    private boolean isLocalAssetSearchedParticle;
    private boolean isLocalAssetSearchedSuperZoom;
    private boolean isLocalAssetSearchedTheme;
    private boolean isLocalAssetSearchedTransition;
    private boolean isSearchLocalCustomSticker;
    public boolean isSyncInstallAsset;
    private Context mContext;
    private Handler mHandler = new Handler() {
        /* class com.meishe.myvideo.util.asset.NvAssetManager.AnonymousClass1 */

        public void handleMessage(Message message) {
            switch (message.what) {
                case 2001:
                    RequestAssetData requestAssetData = (RequestAssetData) message.obj;
                    if (requestAssetData != null) {
                        NvAssetManager.this.updateAssetDataListSuccess(requestAssetData.curAssetType, requestAssetData.resultsArray, requestAssetData.hasNext);
                        return;
                    }
                    return;
                case 2002:
                    NvAssetManager.this.updateAssetDataListFailed();
                    return;
                case 2003:
                    DownloadAssetData downloadAssetData = (DownloadAssetData) message.obj;
                    if (downloadAssetData != null) {
                        NvAssetManager.this.updateAssetDownloadProgress(downloadAssetData.curAssetType, downloadAssetData.downloadId, downloadAssetData.downloadProgress);
                        return;
                    }
                    return;
                case 2004:
                    DownloadAssetData downloadAssetData2 = (DownloadAssetData) message.obj;
                    if (downloadAssetData2 != null) {
                        NvAssetManager.this.updateAssetDownloadSuccess(downloadAssetData2.curAssetType, downloadAssetData2.downloadId, downloadAssetData2.downloadPath);
                        return;
                    }
                    return;
                case 2005:
                    DownloadAssetData downloadAssetData3 = (DownloadAssetData) message.obj;
                    if (downloadAssetData3 != null) {
                        NvAssetManager.this.updateAssetDownloadFailed(downloadAssetData3.curAssetType, downloadAssetData3.downloadId);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private NvAssetManagerListener mManagerlistener;
    private NvHttpRequest m_httpRequest;
    private int maxConcurrentAssetDownloadNum = 10;
    private NvsAssetPackageManager packageManager;
    private ArrayList<String> pendingAssetsToDownload = new ArrayList<>();
    private HashMap<String, ArrayList<String>> remoteAssetsOrderedList;
    private NvsStreamingContext streamingContext;

    public interface NvAssetManagerListener {
        void onDonwloadAssetFailed(String str);

        void onDonwloadAssetSuccess(String str, int i);

        void onDownloadAssetProgress(String str, int i);

        void onFinishAssetPackageInstallation(String str);

        void onFinishAssetPackageUpgrading(String str);

        void onGetRemoteAssetsFailed();

        void onRemoteAssetsChanged(boolean z);
    }

    public static class NvCustomStickerInfo {
        public String imagePath;
        public int order;
        public String targetImagePath;
        public String templateUuid;
        public String uuid;
    }

    private String getAssetSuffix(int i) {
        switch (i) {
            case 1:
                return ".theme";
            case 2:
            case 7:
            case 9:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            default:
                return ".videofx";
            case 3:
                return ".captionstyle";
            case 4:
            case 12:
                return ".animatedsticker";
            case 5:
            case 25:
            case 26:
                return ".videotransition";
            case 6:
                return ".ttf";
            case 8:
            case 10:
                return ".capturescene";
            case 11:
            case 13:
                return ".zip";
            case 14:
                return ".bundle";
            case 15:
                return ".arscene";
            case 16:
                return ".compoundcaption";
        }
    }

    public ArrayList<String> getPendingAssetsToDownload() {
        return this.pendingAssetsToDownload;
    }

    private class RequestAssetData {
        public int curAssetType;
        public boolean hasNext;
        public ArrayList<AssetListDownResponse.NvAssetInfo> resultsArray;

        private RequestAssetData() {
        }
    }

    private class DownloadAssetData {
        public int curAssetType;
        public String downloadId;
        public String downloadPath;
        public int downloadProgress;

        private DownloadAssetData() {
        }
    }

    public static NvAssetManager init(Context context) {
        if (m_instance == null) {
            m_instance = new NvAssetManager(context);
        }
        return m_instance;
    }

    public static NvAssetManager sharedInstance() {
        return m_instance;
    }

    public void setManagerlistener(NvAssetManagerListener nvAssetManagerListener) {
        this.mManagerlistener = nvAssetManagerListener;
    }

    public void onDestroy() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private NvAssetManager(Context context) {
        this.mContext = context;
        this.m_httpRequest = NvHttpRequest.sharedInstance();
        this.streamingContext = NvsStreamingContext.getInstance();
        preferences = context.getSharedPreferences(assetdata, 0);
        this.assetDict = new HashMap<>();
        this.remoteAssetsOrderedList = new HashMap<>();
        this.customStickerArray = new ArrayList<>();
        this.packageManager = this.streamingContext.getAssetPackageManager();
        this.packageManager.setCallbackInterface(this);
        this.isSyncInstallAsset = true;
    }

    public void downloadRemoteAssetsInfo(int i, int i2, int i3, int i4, int i5) {
        this.m_httpRequest.getAssetList(i, i2, i3, i4, i5, this);
    }

    public boolean downloadAsset(int i, String str) {
        NvAssetInfo findAsset = findAsset(i, str);
        if (findAsset == null) {
            Log.e(TAG, "Invalid asset uuid " + str);
            return false;
        } else if (!findAsset.hasRemoteAsset()) {
            Log.e(TAG, "Asset doesn't have a remote url!" + str);
            return false;
        } else {
            int i2 = findAsset.downloadStatus;
            if (i2 != 0) {
                if (i2 == 1) {
                    Log.e(TAG, "Asset has already in pending download state!" + str);
                    return false;
                } else if (i2 == 2) {
                    Log.e(TAG, "Asset is being downloaded right now!" + str);
                    return false;
                } else if (i2 == 3) {
                    Log.e(TAG, "Asset is being uncompressed right now!" + str);
                    return false;
                } else if (!(i2 == 4 || i2 == 5)) {
                    Log.e(TAG, "Invalid status for Asset !" + str);
                    return false;
                }
            }
            this.pendingAssetsToDownload.add(findAsset.uuid);
            findAsset.downloadStatus = 1;
            downloadPendingAsset(i);
            return true;
        }
    }

    private void downloadPendingAsset(int i) {
        while (this.downloadingAssetsCounter < this.maxConcurrentAssetDownloadNum && this.pendingAssetsToDownload.size() > 0) {
            ArrayList<String> arrayList = this.pendingAssetsToDownload;
            String str = arrayList.get(arrayList.size() - 1);
            ArrayList<String> arrayList2 = this.pendingAssetsToDownload;
            arrayList2.remove(arrayList2.size() - 1);
            if (!startDownloadAsset(i, str)) {
                findAsset(i, str).downloadStatus = 5;
                NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
                if (nvAssetManagerListener != null) {
                    nvAssetManagerListener.onDonwloadAssetFailed(str);
                }
            }
        }
    }

    private boolean startDownloadAsset(int i, String str) {
        NvAssetInfo findAsset = findAsset(i, str);
        if (findAsset == null) {
            Log.e(TAG, "Invalid asset uuid " + str);
            return false;
        } else if (!findAsset.hasRemoteAsset()) {
            Log.e(TAG, "Asset doesn't have a remote url!" + str);
            return false;
        } else {
            String assetDownloadDir = getAssetDownloadDir(i);
            if (TextUtils.isEmpty(assetDownloadDir)) {
                return false;
            }
            String substring = findAsset.remotePackageUrl.substring(findAsset.remotePackageUrl.lastIndexOf("/"));
            String substring2 = findAsset.remotePackageUrl.substring(findAsset.remotePackageUrl.lastIndexOf("."));
            String substring3 = substring.substring(0, substring.lastIndexOf("."));
            this.m_httpRequest.downloadAsset(findAsset.remotePackageUrl, assetDownloadDir, assetDownloadDir + substring3 + ".tmp", substring2, this, i, findAsset.uuid);
            this.downloadingAssetsCounter = this.downloadingAssetsCounter + 1;
            findAsset.downloadProgress = 0;
            findAsset.downloadStatus = 2;
            return true;
        }
    }

    public boolean cancelAssetDownload(String str) {
        NvAssetInfo findAsset = findAsset(str);
        if (findAsset == null) {
            Log.e(TAG, "Invalid asset uuid " + str);
            return false;
        }
        int i = findAsset.downloadStatus;
        if (i == 1) {
            this.pendingAssetsToDownload.remove(str);
            findAsset.downloadStatus = 0;
        } else if (i != 2) {
            Log.e(TAG, "You can't cancel downloading asset while it is not in any of the download states!" + str);
            return false;
        } else {
            findAsset.downloadStatus = 0;
        }
        return true;
    }

    public ArrayList<NvAssetInfo> getRemoteAssets(int i, int i2, int i3) {
        ArrayList<NvAssetInfo> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = this.remoteAssetsOrderedList.get(String.valueOf(i));
        if (arrayList2 != null) {
            Iterator<String> it = arrayList2.iterator();
            while (it.hasNext()) {
                NvAssetInfo findAsset = findAsset(i, it.next());
                if (i2 == 31 && i3 == 0) {
                    arrayList.add(findAsset);
                } else if (i2 != 31 || i3 == 0) {
                    if (i2 == 31 || i3 != 0) {
                        if ((findAsset.aspectRatio & i2) == i2 && findAsset.categoryId == i3) {
                            arrayList.add(findAsset);
                        }
                    } else if ((findAsset.aspectRatio & i2) == i2) {
                        arrayList.add(findAsset);
                    }
                } else if (findAsset.categoryId == i3) {
                    arrayList.add(findAsset);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<NvAssetInfo> getRemoteAssetsWithPage(int i, int i2, int i3, int i4, int i5) {
        ArrayList<NvAssetInfo> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = this.remoteAssetsOrderedList.get(String.valueOf(i));
        if (arrayList2 != null) {
            int i6 = i4 * i5;
            while (i6 < (i4 + 1) * i5 && i6 < arrayList2.size()) {
                NvAssetInfo findAsset = findAsset(i, arrayList2.get(i6));
                if (i2 == 31 && i3 == 0) {
                    arrayList.add(findAsset);
                } else if (i2 != 31 || i3 == 0) {
                    if (i2 == 31 || i3 != 0) {
                        if ((findAsset.aspectRatio & i2) == i2 && findAsset.categoryId == i3) {
                            arrayList.add(findAsset);
                        }
                    } else if ((findAsset.aspectRatio & i2) == i2) {
                        arrayList.add(findAsset);
                    }
                } else if (findAsset.categoryId == i3) {
                    arrayList.add(findAsset);
                }
                i6++;
            }
        }
        return arrayList;
    }

    public ArrayList<NvAssetInfo> getUsableAssets(int i, int i2, int i3) {
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(String.valueOf(i));
        AnonymousClass2 r0 = new Comparator<NvAssetInfo>() {
            /* class com.meishe.myvideo.util.asset.NvAssetManager.AnonymousClass2 */

            public int compare(NvAssetInfo nvAssetInfo, NvAssetInfo nvAssetInfo2) {
                String str = nvAssetInfo.isReserved() ? nvAssetInfo.bundledLocalDirPath : nvAssetInfo.localDirPath;
                String str2 = nvAssetInfo2.isReserved() ? nvAssetInfo2.bundledLocalDirPath : nvAssetInfo2.localDirPath;
                long fileModifiedTime = PathUtils.getFileModifiedTime(str);
                long fileModifiedTime2 = PathUtils.getFileModifiedTime(str2);
                if (fileModifiedTime2 > fileModifiedTime) {
                    return 1;
                }
                return fileModifiedTime2 == fileModifiedTime ? 0 : -1;
            }
        };
        ArrayList<NvAssetInfo> arrayList2 = new ArrayList<>();
        if (arrayList != null) {
            if (arrayList.size() > 1) {
                Collections.sort(arrayList, r0);
            }
            Iterator<NvAssetInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                NvAssetInfo next = it.next();
                if (i2 == 31 && i3 == 0) {
                    if (next.isUsable()) {
                        arrayList2.add(next);
                    }
                } else if (i2 != 31 || i3 == 0) {
                    if (i2 == 31 || i3 != 0) {
                        if ((next.aspectRatio & i2) == i2 && next.categoryId == i3 && next.isUsable()) {
                            arrayList2.add(next);
                        }
                    } else if ((next.aspectRatio & i2) == i2 && next.isUsable()) {
                        arrayList2.add(next);
                    }
                } else if (next.categoryId == i3 && next.isUsable()) {
                    arrayList2.add(next);
                }
            }
        }
        return arrayList2;
    }

    public ArrayList<NvAssetInfo> getReservedAssets(int i, int i2, int i3) {
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(String.valueOf(i));
        ArrayList<NvAssetInfo> arrayList2 = new ArrayList<>();
        if (arrayList != null) {
            Iterator<NvAssetInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                NvAssetInfo next = it.next();
                if (i2 == 31 && i3 == 0) {
                    if (next.isReserved()) {
                        arrayList2.add(next);
                    }
                } else if (i2 != 31 || i3 == 0) {
                    if (i2 == 31 || i3 != 0) {
                        if ((next.aspectRatio & i2) == i2 && next.categoryId == i3 && next.isUsable() && next.isReserved()) {
                            arrayList2.add(next);
                        }
                    } else if ((next.aspectRatio & i2) == i2 && next.isUsable() && next.isReserved()) {
                        arrayList2.add(next);
                    }
                } else if (next.categoryId == i3 && next.isUsable() && next.isReserved()) {
                    arrayList2.add(next);
                }
            }
        }
        return arrayList2;
    }

    public NvAssetInfo getAsset(String str) {
        return findAsset(str);
    }

    public void searchLocalAssets(int i) {
        searchAssetInLocalPath(i, getAssetDownloadDir(i));
        setIsLocalAssetSearched(i, true);
    }

    public void searchReservedAssets(int i, String str) {
        searchAssetInBundlePath(i, str);
    }

    private void searchAssetInBundlePath(int i, String str) {
        String assetSuffix = getAssetSuffix(i);
        if (i == 14) {
            i = 11;
        }
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(String.valueOf(i));
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.assetDict.put(String.valueOf(i), arrayList);
        }
        try {
            String[] list = this.mContext.getAssets().list(str);
            if (list != null) {
                for (String str2 : list) {
                    if (!TextUtils.isEmpty(str2)) {
                        if (str2.endsWith(assetSuffix)) {
                            String str3 = "assets:/" + str + File.separator + str2;
                            NvAssetInfo installAssetPackage = installAssetPackage(str3, i, true);
                            if (installAssetPackage != null) {
                                installAssetPackage.isReserved = true;
                                installAssetPackage.assetType = i;
                                installAssetPackage.bundledLocalDirPath = str3;
                                NvAssetInfo findAsset = findAsset(i, installAssetPackage.uuid);
                                if (findAsset == null) {
                                    arrayList.add(installAssetPackage);
                                } else if (findAsset.version <= installAssetPackage.version) {
                                    findAsset.copyAsset(installAssetPackage);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchAssetInLocalPath(int i, String str) {
        File[] listFiles;
        String assetSuffix = getAssetSuffix(i);
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(String.valueOf(i));
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.assetDict.put(String.valueOf(i), arrayList);
        }
        File file = new File(str);
        if (file.exists() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                String absolutePath = file2.getAbsolutePath();
                if (absolutePath.endsWith(assetSuffix)) {
                    String substring = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                    if (!TextUtils.isEmpty(substring)) {
                        String str2 = substring.split("\\.")[0];
                        if (!TextUtils.isEmpty(str2)) {
                            NvUserAssetInfo assetInfoFromSharedPreferences = getAssetInfoFromSharedPreferences(str2, i);
                            NvAssetInfo installAssetPackage = installAssetPackage(absolutePath, i, false);
                            if (installAssetPackage != null) {
                                installAssetPackage.isReserved = false;
                                installAssetPackage.assetType = i;
                                if (!(i == 11 || i == 13)) {
                                    installAssetPackage.localDirPath = absolutePath;
                                }
                                if (assetInfoFromSharedPreferences != null) {
                                    if (i != 11) {
                                        installAssetPackage.coverUrl = assetInfoFromSharedPreferences.coverUrl;
                                    }
                                    installAssetPackage.mName = assetInfoFromSharedPreferences.name;
                                    installAssetPackage.categoryId = assetInfoFromSharedPreferences.categoryId;
                                    installAssetPackage.aspectRatio = assetInfoFromSharedPreferences.aspectRatio;
                                    installAssetPackage.remotePackageSize = assetInfoFromSharedPreferences.remotePackageSize;
                                }
                                NvAssetInfo findAsset = findAsset(i, installAssetPackage.uuid);
                                if (findAsset == null) {
                                    arrayList.add(installAssetPackage);
                                } else if (findAsset.version < installAssetPackage.version) {
                                    findAsset.copyAsset(installAssetPackage);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public NvAssetInfo installAssetPackage(String str, int i, boolean z) {
        NvAssetInfo nvAssetInfo = new NvAssetInfo();
        boolean z2 = true;
        String substring = str.substring(str.lastIndexOf("/") + 1);
        if (TextUtils.isEmpty(substring)) {
            return null;
        }
        nvAssetInfo.assetType = i;
        nvAssetInfo.uuid = substring.split("\\.")[0];
        if (TextUtils.isEmpty(nvAssetInfo.uuid)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        NvsAssetPackageManager assetPackageManager = this.streamingContext.getAssetPackageManager();
        if (assetPackageManager == null) {
            return null;
        }
        nvAssetInfo.downloadStatus = 3;
        if (i == 11) {
            String assetDownloadPath = PathUtils.getAssetDownloadPath(11);
            if (!z) {
                z2 = PathUtils.unZipFile(str, assetDownloadPath + File.separator);
            }
            if (z2) {
                nvAssetInfo.downloadStatus = 4;
                nvAssetInfo.version = PathUtils.getAssetVersionWithPath(str);
                if (!z) {
                    nvAssetInfo.localDirPath = assetDownloadPath + File.separator + nvAssetInfo.uuid + File.separator + nvAssetInfo.uuid + ".bundle";
                    nvAssetInfo.coverUrl = assetDownloadPath + File.separator + nvAssetInfo.uuid + File.separator + nvAssetInfo.uuid + ".png";
                }
            } else {
                nvAssetInfo.downloadStatus = 6;
            }
        } else if (i == 13) {
            String assetDownloadPath2 = PathUtils.getAssetDownloadPath(13);
            if (z) {
                nvAssetInfo.bundledLocalDirPath = assetDownloadPath2 + File.separator + nvAssetInfo.uuid;
            } else {
                if (PathUtils.unZipFile(str, assetDownloadPath2 + File.separator)) {
                    nvAssetInfo.downloadStatus = 4;
                    nvAssetInfo.version = PathUtils.getAssetVersionWithPath(str);
                    nvAssetInfo.localDirPath = assetDownloadPath2 + File.separator + nvAssetInfo.uuid;
                } else {
                    nvAssetInfo.downloadStatus = 6;
                }
            }
        } else if (i == 6) {
            nvAssetInfo.downloadStatus = 4;
        } else if (this.isSyncInstallAsset) {
            int installAssetPackage = assetPackageManager.installAssetPackage(str, null, nvAssetInfo.getPackageType(), true, sb);
            if (installAssetPackage == 0) {
                nvAssetInfo.downloadStatus = 4;
                nvAssetInfo.version = assetPackageManager.getAssetPackageVersion(nvAssetInfo.uuid, nvAssetInfo.getPackageType());
                nvAssetInfo.aspectRatio = assetPackageManager.getAssetPackageSupportedAspectRatio(nvAssetInfo.uuid, nvAssetInfo.getPackageType());
            } else if (installAssetPackage == 2) {
                nvAssetInfo.downloadStatus = 4;
                nvAssetInfo.version = assetPackageManager.getAssetPackageVersion(nvAssetInfo.uuid, nvAssetInfo.getPackageType());
                nvAssetInfo.aspectRatio = assetPackageManager.getAssetPackageSupportedAspectRatio(nvAssetInfo.uuid, nvAssetInfo.getPackageType());
                int assetPackageVersionFromAssetPackageFilePath = assetPackageManager.getAssetPackageVersionFromAssetPackageFilePath(str);
                if (assetPackageVersionFromAssetPackageFilePath > nvAssetInfo.version && assetPackageManager.upgradeAssetPackage(str, null, nvAssetInfo.getPackageType(), false, sb) == 0) {
                    nvAssetInfo.version = assetPackageVersionFromAssetPackageFilePath;
                }
            } else {
                nvAssetInfo.downloadStatus = 6;
            }
        } else if (assetPackageManager.getAssetPackageStatus(nvAssetInfo.uuid, nvAssetInfo.getPackageType()) == 2) {
            int assetPackageVersionFromAssetPackageFilePath2 = assetPackageManager.getAssetPackageVersionFromAssetPackageFilePath(str);
            NvAssetInfo findAsset = findAsset(nvAssetInfo.uuid);
            if (findAsset != null && assetPackageVersionFromAssetPackageFilePath2 > findAsset.version) {
                assetPackageManager.upgradeAssetPackage(str, null, nvAssetInfo.getPackageType(), false, sb);
            }
        } else {
            assetPackageManager.installAssetPackage(str, null, nvAssetInfo.getPackageType(), false, sb);
        }
        nvAssetInfo.mName = "";
        nvAssetInfo.categoryId = 0;
        nvAssetInfo.aspectRatio = 31;
        if (nvAssetInfo.assetType == 9) {
            nvAssetInfo.assetDescription = assetPackageManager.getVideoFxAssetPackageDescription(sb.toString());
        }
        return nvAssetInfo;
    }

    public void setAssetInfoToSharedPreferences(int i) {
        String valueOf = String.valueOf(i);
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(valueOf);
        if (arrayList != null && arrayList.size() != 0) {
            HashMap<String, String> hashMap = new HashMap<>();
            Iterator<NvAssetInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                NvAssetInfo next = it.next();
                if (next.isUsable()) {
                    String str = next.uuid;
                    hashMap.put(str, "name:" + next.mName + ";" + "coverUrl:" + next.coverUrl + ";" + "categoryId:" + String.valueOf(next.categoryId) + ";" + "aspectRatio:" + String.valueOf(next.aspectRatio) + ";" + "remotePackageSize:" + String.valueOf(next.remotePackageSize) + ";" + "assetType:" + String.valueOf(next.assetType));
                }
            }
            writeAssetDataToLocal(hashMap, PathUtils.getAssetDownloadPath(-1) + File.separator + "info_" + valueOf + ".json");
        }
    }

    public ArrayList<NvCustomStickerInfo> getUsableCustomStickerAssets() {
        ArrayList<NvCustomStickerInfo> arrayList = new ArrayList<>();
        Iterator<NvCustomStickerInfo> it = this.customStickerArray.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    public void appendCustomStickerInfoData(NvCustomStickerInfo nvCustomStickerInfo) {
        this.customStickerArray.add(0, nvCustomStickerInfo);
    }

    public void setCustomStickerInfoToSharedPreferences() {
        HashMap<String, String> hashMap = new HashMap<>();
        Iterator<NvCustomStickerInfo> it = this.customStickerArray.iterator();
        while (it.hasNext()) {
            NvCustomStickerInfo next = it.next();
            String str = next.uuid;
            hashMap.put(str, "templateUuid:" + next.templateUuid + ";" + "imagePath:" + next.imagePath + ";" + "targetImagePath:" + next.targetImagePath + ";" + "order:" + String.valueOf(next.order));
        }
        writeAssetDataToLocal(hashMap, PathUtils.getAssetDownloadPath(-1) + customStickerInfo);
    }

    private void writeAssetDataToLocal(HashMap<String, String> hashMap, String str) {
        if (hashMap != null && hashMap.size() != 0 && !TextUtils.isEmpty(str)) {
            JSONObject jSONObject = new JSONObject(hashMap);
            File file = new File(str);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                bufferedWriter.write(jSONObject.toString());
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initCustomStickerInfoFromSharedPreferences() {
        if (!this.isSearchLocalCustomSticker) {
            this.isSearchLocalCustomSticker = true;
            if (this.customStickerArray == null) {
                this.customStickerArray = new ArrayList<>();
            }
            if (this.customStickerArray.size() > 0) {
                this.customStickerArray.clear();
            }
            File file = new File(PathUtils.getAssetDownloadPath(-1) + File.separator + customStickerInfo);
            try {
                if (file.exists()) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String str = "";
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        str = str + readLine;
                    }
                    bufferedReader.close();
                    JSONObject jSONObject = new JSONObject(str);
                    Iterator<String> keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        String string = jSONObject.getString(next);
                        NvCustomStickerInfo nvCustomStickerInfo = new NvCustomStickerInfo();
                        String[] split = string.split(";");
                        for (String str2 : split) {
                            nvCustomStickerInfo.uuid = next;
                            if (str2.indexOf("templateUuid:") >= 0) {
                                nvCustomStickerInfo.templateUuid = str2.replaceAll("templateUuid:", "");
                            } else if (str2.indexOf("imagePath:") >= 0) {
                                nvCustomStickerInfo.imagePath = str2.replaceAll("imagePath:", "");
                            } else if (str2.indexOf("targetImagePath:") >= 0) {
                                nvCustomStickerInfo.targetImagePath = str2.replaceAll("targetImagePath:", "");
                            } else if (str2.indexOf("order:") >= 0) {
                                nvCustomStickerInfo.order = Integer.parseInt(str2.replaceAll("order:", ""));
                            }
                        }
                        this.customStickerArray.add(nvCustomStickerInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NvUserAssetInfo getAssetInfoFromSharedPreferences(String str, int i) {
        File file = new File(PathUtils.getAssetDownloadPath(-1) + File.separator + "info_" + String.valueOf(i) + ".json");
        try {
            if (!file.exists()) {
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str2 = "";
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                str2 = str2 + readLine;
            }
            JSONObject jSONObject = new JSONObject(str2);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                if (keys.next().equals(str)) {
                    String string = jSONObject.getString(str);
                    NvUserAssetInfo nvUserAssetInfo = new NvUserAssetInfo();
                    String[] split = string.split(";");
                    for (String str3 : split) {
                        if (str3.indexOf("uuid:") >= 0) {
                            nvUserAssetInfo.uuid = str;
                        } else if (str3.indexOf("name:") >= 0) {
                            nvUserAssetInfo.name = str3.replaceAll("name:", "");
                        } else if (str3.indexOf("coverUrl:") >= 0) {
                            nvUserAssetInfo.coverUrl = str3.replaceAll("coverUrl:", "");
                        } else if (str3.indexOf("categoryId:") >= 0) {
                            nvUserAssetInfo.categoryId = Integer.parseInt(str3.replaceAll("categoryId:", ""));
                        } else if (str3.indexOf("aspectRatio:") >= 0) {
                            nvUserAssetInfo.aspectRatio = Integer.parseInt(str3.replaceAll("aspectRatio:", ""));
                        } else if (str3.indexOf("remotePackageSize:") >= 0) {
                            nvUserAssetInfo.remotePackageSize = Integer.parseInt(str3.replaceAll("remotePackageSize:", ""));
                        } else if (str3.indexOf("assetType:") >= 0) {
                            nvUserAssetInfo.assetType = Integer.parseInt(str3.replaceAll("assetType:", ""));
                        }
                    }
                    return nvUserAssetInfo;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAssetDownloadDir(int i) {
        return PathUtils.getAssetDownloadPath(i);
    }

    private boolean getIsLocalAssetSearched(int i) {
        switch (i) {
            case 1:
                return this.isLocalAssetSearchedTheme;
            case 2:
                return this.isLocalAssetSearchedFilter;
            case 3:
                return this.isLocalAssetSearchedCaption;
            case 4:
                return this.isLocalAssetSearchedAnimatedSticker;
            case 5:
                return this.isLocalAssetSearchedTransition;
            case 6:
            case 7:
            case 14:
            case 17:
            default:
                return false;
            case 8:
                return this.isLocalAssetSearchedCaptureScene;
            case 9:
                return this.isLocalAssetSearchedParticle;
            case 10:
                return this.isLocalAssetSearchedFaceSticker;
            case 11:
                return this.isLocalAssetSearchedFace1Sticker;
            case 12:
                return this.isLocalAssetSearchedCustomAnimatedSticker;
            case 13:
                return this.isLocalAssetSearchedSuperZoom;
            case 15:
                return this.isLocalAssetSearchedARScene;
            case 16:
                return this.isLocalAssetSearchedCompoundCaption;
            case 18:
                return this.isLocalAssetSearchedEffetFrame;
            case 19:
                return this.isLocalAssetSearchedEffetDream;
            case 20:
                return this.isLocalAssetSearchedEffetLively;
            case 21:
                return this.isLocalAssetSearchedEffetShaking;
        }
    }

    private void setIsLocalAssetSearched(int i, boolean z) {
        switch (i) {
            case 1:
                this.isLocalAssetSearchedTheme = z;
                return;
            case 2:
                this.isLocalAssetSearchedFilter = z;
                return;
            case 3:
                this.isLocalAssetSearchedCaption = z;
                return;
            case 4:
                this.isLocalAssetSearchedAnimatedSticker = z;
                return;
            case 5:
                this.isLocalAssetSearchedTransition = z;
                return;
            case 6:
            case 7:
            case 14:
            case 17:
            default:
                return;
            case 8:
                this.isLocalAssetSearchedCaptureScene = z;
                return;
            case 9:
                this.isLocalAssetSearchedParticle = z;
                return;
            case 10:
                this.isLocalAssetSearchedFaceSticker = z;
                return;
            case 11:
                this.isLocalAssetSearchedFace1Sticker = z;
                return;
            case 12:
                this.isLocalAssetSearchedCustomAnimatedSticker = z;
                return;
            case 13:
                this.isLocalAssetSearchedSuperZoom = z;
                return;
            case 15:
                this.isLocalAssetSearchedARScene = z;
                return;
            case 16:
                this.isLocalAssetSearchedCompoundCaption = z;
                return;
            case 18:
                this.isLocalAssetSearchedEffetFrame = z;
                return;
            case 19:
                this.isLocalAssetSearchedEffetDream = z;
                return;
            case 20:
                this.isLocalAssetSearchedEffetLively = z;
                return;
            case 21:
                this.isLocalAssetSearchedEffetShaking = z;
                return;
        }
    }

    private NvAssetInfo findAsset(String str) {
        for (String str2 : this.assetDict.keySet()) {
            ArrayList<NvAssetInfo> arrayList = this.assetDict.get(str2);
            int i = 0;
            while (true) {
                if (i < arrayList.size()) {
                    NvAssetInfo nvAssetInfo = arrayList.get(i);
                    if (nvAssetInfo.uuid.equals(str)) {
                        return nvAssetInfo;
                    }
                    i++;
                }
            }
        }
        return null;
    }

    private NvAssetInfo findAsset(int i, String str) {
        ArrayList<NvAssetInfo> arrayList = this.assetDict.get(String.valueOf(i));
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            NvAssetInfo nvAssetInfo = arrayList.get(i2);
            if (nvAssetInfo.uuid.equals(str)) {
                return nvAssetInfo;
            }
        }
        return null;
    }

    private void addRemoteAssetData(ArrayList<AssetListDownResponse.NvAssetInfo> arrayList, int i) {
        ArrayList<NvAssetInfo> arrayList2 = this.assetDict.get(String.valueOf(i));
        if (arrayList2 == null) {
            arrayList2 = new ArrayList<>();
        }
        Iterator<AssetListDownResponse.NvAssetInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            AssetListDownResponse.NvAssetInfo next = it.next();
            NvAssetInfo nvAssetInfo = new NvAssetInfo();
            nvAssetInfo.assetType = i;
            nvAssetInfo.categoryId = next.category;
            nvAssetInfo.tags = next.tags;
            nvAssetInfo.remotePackageSize = next.packageSize;
            nvAssetInfo.uuid = next.id;
            nvAssetInfo.minAppVersion = next.minAppVersion;
            nvAssetInfo.remotePackageUrl = next.packageUrl.replaceAll("https://assets.meishesdk.com", "https://assets.meishesdk.com");
            nvAssetInfo.remoteVersion = next.version;
            nvAssetInfo.coverUrl = next.coverUrl.replaceAll("https://assets.meishesdk.com", "https://assets.meishesdk.com");
            nvAssetInfo.aspectRatio = next.supportedAspectRatio;
            nvAssetInfo.mName = next.name;
            nvAssetInfo.desc = next.desc;
            NvAssetInfo findAsset = findAsset(i, nvAssetInfo.uuid);
            if (findAsset == null) {
                arrayList2.add(nvAssetInfo);
            } else {
                findAsset.categoryId = nvAssetInfo.categoryId;
                findAsset.mName = nvAssetInfo.mName;
                findAsset.coverUrl = nvAssetInfo.coverUrl;
                findAsset.aspectRatio = nvAssetInfo.aspectRatio;
                findAsset.remotePackageSize = nvAssetInfo.remotePackageSize;
                findAsset.remoteVersion = nvAssetInfo.remoteVersion;
                findAsset.remotePackageUrl = nvAssetInfo.remotePackageUrl;
            }
        }
        this.assetDict.put(String.valueOf(i), arrayList2);
    }

    private void addRemoteAssetOrderedList(ArrayList<AssetListDownResponse.NvAssetInfo> arrayList, int i) {
        ArrayList<String> arrayList2 = this.remoteAssetsOrderedList.get(String.valueOf(i));
        if (arrayList2 == null) {
            arrayList2 = new ArrayList<>();
        }
        Iterator<AssetListDownResponse.NvAssetInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            AssetListDownResponse.NvAssetInfo next = it.next();
            if (!arrayList2.contains(next.id)) {
                arrayList2.add(next.id);
            }
        }
        this.remoteAssetsOrderedList.put(String.valueOf(i), arrayList2);
    }

    private void sendHandleMsg(Object obj, int i) {
        Message obtainMessage = this.mHandler.obtainMessage();
        if (obtainMessage == null) {
            obtainMessage = new Message();
        }
        obtainMessage.what = i;
        obtainMessage.obj = obj;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendMessage(obtainMessage);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAssetDataListSuccess(int i, ArrayList<AssetListDownResponse.NvAssetInfo> arrayList, boolean z) {
        addRemoteAssetData(arrayList, i);
        addRemoteAssetOrderedList(arrayList, i);
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onRemoteAssetsChanged(z);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAssetDataListFailed() {
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onGetRemoteAssetsFailed();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAssetDownloadProgress(int i, String str, int i2) {
        NvAssetInfo findAsset = findAsset(i, str);
        findAsset.downloadProgress = i2;
        findAsset.downloadStatus = 2;
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onDownloadAssetProgress(str, i2);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAssetDownloadSuccess(int i, String str, String str2) {
        this.downloadingAssetsCounter--;
        NvAssetInfo findAsset = findAsset(i, str);
        findAsset.downloadProgress = 100;
        findAsset.downloadStatus = 3;
        findAsset.localDirPath = str2;
        NvAssetInfo installAssetPackage = installAssetPackage(str2, findAsset.assetType, false);
        if (this.isSyncInstallAsset) {
            findAsset.downloadStatus = installAssetPackage.downloadStatus;
            findAsset.version = installAssetPackage.version;
            findAsset.assetDescription = installAssetPackage.assetDescription;
        }
        if (findAsset.assetType == 11 || findAsset.assetType == 13) {
            findAsset.downloadStatus = installAssetPackage.downloadStatus;
            findAsset.version = installAssetPackage.version;
            findAsset.localDirPath = installAssetPackage.localDirPath;
        }
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onDonwloadAssetSuccess(str, i);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAssetDownloadFailed(int i, String str) {
        NvAssetInfo findAsset = findAsset(i, str);
        findAsset.downloadProgress = 0;
        findAsset.downloadStatus = 5;
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onDonwloadAssetFailed(str);
        }
    }

    @Override // com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener
    public void onGetAssetListSuccess(ArrayList arrayList, int i, boolean z) {
        RequestAssetData requestAssetData = new RequestAssetData();
        requestAssetData.curAssetType = i;
        requestAssetData.resultsArray = arrayList;
        requestAssetData.hasNext = z;
        sendHandleMsg(requestAssetData, 2001);
    }

    @Override // com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener
    public void onGetAssetListFailed(String str, int i) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(2002);
        }
    }

    @Override // com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener
    public void onDonwloadAssetProgress(int i, int i2, String str) {
        DownloadAssetData downloadAssetData = new DownloadAssetData();
        downloadAssetData.curAssetType = i2;
        downloadAssetData.downloadId = str;
        downloadAssetData.downloadProgress = i;
        sendHandleMsg(downloadAssetData, 2003);
    }

    @Override // com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener
    public void onDonwloadAssetSuccess(boolean z, String str, int i, String str2) {
        setAssetInfoToSharedPreferences(i);
        DownloadAssetData downloadAssetData = new DownloadAssetData();
        downloadAssetData.curAssetType = i;
        downloadAssetData.downloadId = str2;
        downloadAssetData.downloadPath = str;
        sendHandleMsg(downloadAssetData, 2004);
    }

    @Override // com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener
    public void onDonwloadAssetFailed(String str, int i, String str2) {
        DownloadAssetData downloadAssetData = new DownloadAssetData();
        downloadAssetData.curAssetType = i;
        downloadAssetData.downloadId = str2;
        sendHandleMsg(downloadAssetData, 2005);
    }

    @Override // com.meicam.sdk.NvsAssetPackageManager.AssetPackageManagerCallback
    public void onFinishAssetPackageInstallation(String str, String str2, int i, int i2) {
        if (i2 == 0 || i2 == 2) {
            NvAssetInfo findAsset = findAsset(str);
            findAsset.downloadStatus = 4;
            findAsset.version = this.packageManager.getAssetPackageVersion(str, i);
            findAsset.aspectRatio = this.packageManager.getAssetPackageSupportedAspectRatio(findAsset.uuid, findAsset.getPackageType());
        } else {
            findAsset(str).downloadStatus = 6;
        }
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onFinishAssetPackageInstallation(str);
        }
    }

    @Override // com.meicam.sdk.NvsAssetPackageManager.AssetPackageManagerCallback
    public void onFinishAssetPackageUpgrading(String str, String str2, int i, int i2) {
        if (i2 == 0 || i2 == 2) {
            NvAssetInfo findAsset = findAsset(str);
            findAsset.downloadStatus = 4;
            findAsset.version = this.packageManager.getAssetPackageVersion(str, i);
            findAsset.aspectRatio = this.packageManager.getAssetPackageSupportedAspectRatio(findAsset.uuid, findAsset.getPackageType());
        } else {
            findAsset(str).downloadStatus = 6;
        }
        NvAssetManagerListener nvAssetManagerListener = this.mManagerlistener;
        if (nvAssetManagerListener != null) {
            nvAssetManagerListener.onFinishAssetPackageUpgrading(str);
        }
    }

    public class NvUserAssetInfo {
        public int aspectRatio;
        public int assetType;
        public int categoryId;
        public String coverUrl;
        public String name;
        public int remotePackageSize;
        public String uuid;

        public NvUserAssetInfo() {
        }
    }
}
