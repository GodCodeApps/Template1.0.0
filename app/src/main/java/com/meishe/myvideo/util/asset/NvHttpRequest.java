package com.meishe.myvideo.util.asset;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;
import android.util.Log;
import com.example.net.interfaces.DownloadListener;
import com.example.net.interfaces.NetListener;
import com.meishe.myvideo.MSApi;
import com.meishe.myvideo.application.MeiSheApplication;
import com.meishe.myvideo.bean.down.AssetListDownResponse;
import com.meishe.myvideo.util.ThreadPoolUtils;
import com.umeng.analytics.pro.b;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Response;

public class NvHttpRequest {
    public static final int NONETWORK = 0;
    public static final int NOWIFI = 2;
    private static final String TAG = "NvHttpRequest ";
    public static final int WIFI = 1;
    private static NvHttpRequest m_instance;
    private ExecutorService executorService = ThreadPoolUtils.newSingleThreadExecutor("DownloadAssetPool");

    public interface NvHttpRequestListener {
        void onDonwloadAssetFailed(String str, int i, String str2);

        void onDonwloadAssetProgress(int i, int i2, String str);

        void onDonwloadAssetSuccess(boolean z, String str, int i, String str2);

        void onGetAssetListFailed(String str, int i);

        void onGetAssetListSuccess(ArrayList arrayList, int i, boolean z);
    }

    public static int checkNetWorkType(Context context) {
        if (!checkNetWork(context)) {
            return 0;
        }
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getType() == 1 ? 1 : 2;
    }

    public static boolean checkNetWork(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static NvHttpRequest sharedInstance() {
        if (m_instance == null) {
            m_instance = new NvHttpRequest();
        }
        return m_instance;
    }

    private NvHttpRequest() {
    }

    public void getAssetList(final int i, int i2, int i3, int i4, int i5, final NvHttpRequestListener nvHttpRequestListener) {
        try {
            new MSApi().getDownLoadAssetList(getRequestParam(i, i2, i3, i4, i5), new NetListener<AssetListDownResponse>() {
                /* class com.meishe.myvideo.util.asset.NvHttpRequest.AnonymousClass1 */

                public void onSuccess(AssetListDownResponse assetListDownResponse) {
                    if (nvHttpRequestListener == null) {
                        return;
                    }
                    if (assetListDownResponse.errNo == 0) {
                        nvHttpRequestListener.onGetAssetListSuccess(assetListDownResponse.list, i, assetListDownResponse.hasNext);
                    } else {
                        nvHttpRequestListener.onGetAssetListFailed(null, i);
                    }
                }

                @Override // com.example.net.interfaces.NetListener
                public void onFaile(String str) {
                    NvHttpRequestListener nvHttpRequestListener = nvHttpRequestListener;
                    if (nvHttpRequestListener != null) {
                        nvHttpRequestListener.onGetAssetListFailed(str, i);
                    }
                }
            });
        } catch (RemoteException | IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | JSONException e) {
            if (nvHttpRequestListener != null) {
                nvHttpRequestListener.onGetAssetListFailed(e.getMessage(), i);
            }
        }
    }

    public static boolean isZh(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    private HashMap getRequestParam(int i, int i2, int i3, int i4, int i5) {
        HashMap hashMap = new HashMap();
        hashMap.put("command", "listMaterial");
        hashMap.put("acceptAspectRatio", String.valueOf(i2));
        if (i == 12) {
            hashMap.put("category", String.valueOf(20000));
        } else if (i == 18) {
            hashMap.put("category", String.valueOf(6));
        } else if (i == 19) {
            hashMap.put("category", String.valueOf(5));
        } else if (i == 20) {
            hashMap.put("category", String.valueOf(4));
        } else if (i == 21) {
            hashMap.put("category", String.valueOf(7));
        } else if (i == 2) {
            hashMap.put("category", String.valueOf(2));
        } else if (i == 5) {
            hashMap.put("category", String.valueOf(1));
        } else if (i == 25) {
            hashMap.put("category", String.valueOf(2));
        } else if (i == 26) {
            hashMap.put("category", String.valueOf(3));
        } else {
            hashMap.put("category", String.valueOf(i3));
        }
        hashMap.put("page", String.valueOf(i4));
        hashMap.put("pageSize", String.valueOf(i5));
        if (isZh(MeiSheApplication.getContext())) {
            hashMap.put("lang", "zh_CN");
        } else {
            hashMap.put("lang", "en");
        }
        if (i == 1) {
            hashMap.put(b.x, String.valueOf(1));
        } else if (i == 2) {
            hashMap.put(b.x, String.valueOf(2));
        } else if (i == 3) {
            hashMap.put(b.x, String.valueOf(3));
        } else if (i == 4) {
            hashMap.put(b.x, String.valueOf(4));
        } else if (i == 5 || i == 25 || i == 26) {
            hashMap.put(b.x, String.valueOf(5));
        } else if (i == 12) {
            hashMap.put(b.x, String.valueOf(4));
        } else if (i == 8) {
            hashMap.put(b.x, String.valueOf(8));
        } else if (i == 9) {
            hashMap.put(b.x, String.valueOf(9));
        } else if (i == 10) {
            hashMap.put(b.x, String.valueOf(10));
        } else if (i == 11) {
            hashMap.put(b.x, String.valueOf(11));
        } else if (i == 13) {
            hashMap.put(b.x, String.valueOf(13));
        } else if (i == 6) {
            hashMap.put(b.x, String.valueOf(6));
        } else if (i == 15) {
            hashMap.put(b.x, String.valueOf(14));
        } else if (i == 16) {
            hashMap.put(b.x, String.valueOf(15));
        } else if (i == 18) {
            hashMap.put(b.x, String.valueOf(2));
        } else if (i == 19) {
            hashMap.put(b.x, String.valueOf(2));
        } else if (i == 20) {
            hashMap.put(b.x, String.valueOf(2));
        } else if (i == 21) {
            hashMap.put(b.x, String.valueOf(2));
        }
        return hashMap;
    }

    public void downloadAsset(String str, final String str2, final String str3, final String str4, final NvHttpRequestListener nvHttpRequestListener, final int i, final String str5) {
        try {
            new MSApi().downLoadAsset(str, new DownloadListener() {
                /* class com.meishe.myvideo.util.asset.NvHttpRequest.AnonymousClass2 */

                @Override // com.example.net.interfaces.DownloadListener
                public void onSuccess(final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        NvHttpRequest.this.executorService.execute(new Runnable() {
                            /* class com.meishe.myvideo.util.asset.NvHttpRequest.AnonymousClass2.AnonymousClass1 */

                            public void run() {
                                NvHttpRequest.this.writeAssetToDisc(response, str2, str3, str4, nvHttpRequestListener, i, str5);
                            }
                        });
                    } else {
                        Log.e(NvHttpRequest.TAG, "服务器端错误");
                    }
                }

                @Override // com.example.net.interfaces.DownloadListener
                public void onFaile(String str) {
                    File file = new File(str3);
                    if (file.exists()) {
                        file.delete();
                    }
                    NvHttpRequestListener nvHttpRequestListener = nvHttpRequestListener;
                    if (nvHttpRequestListener != null) {
                        nvHttpRequestListener.onDonwloadAssetFailed(str, i, str5);
                    }
                }
            });
        } catch (RemoteException | IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | JSONException e) {
            nvHttpRequestListener.onDonwloadAssetFailed(e.getMessage(), i, str5);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a2 A[Catch:{ all -> 0x0112 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00aa A[Catch:{ all -> 0x0112 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bc A[Catch:{ IOException -> 0x00c5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00c1 A[Catch:{ IOException -> 0x00c5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00f7 A[Catch:{ IOException -> 0x0100 }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00fc A[Catch:{ IOException -> 0x0100 }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0121 A[Catch:{ IOException -> 0x012a }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0126 A[Catch:{ IOException -> 0x012a }] */
    /* JADX WARNING: Removed duplicated region for block: B:92:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:94:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeAssetToDisc(retrofit2.Response<okhttp3.ResponseBody> r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, com.meishe.myvideo.util.asset.NvHttpRequest.NvHttpRequestListener r19, int r20, java.lang.String r21) {
        /*
        // Method dump skipped, instructions count: 323
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.util.asset.NvHttpRequest.writeAssetToDisc(retrofit2.Response, java.lang.String, java.lang.String, java.lang.String, com.meishe.myvideo.util.asset.NvHttpRequest$NvHttpRequestListener, int, java.lang.String):void");
    }
}
