package com.meishe.myvideo;

import android.os.RemoteException;
import com.example.net.Liberty;
import com.example.net.interfaces.DownloadListener;
import com.example.net.interfaces.NetListener;
import com.meishe.myvideo.bean.down.AbstractResponse;
import com.meishe.myvideo.bean.down.AssetListDownResponse;
import com.meishe.myvideo.bean.up.FeedBackUpParams;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import org.json.JSONException;

public class MSApi {
    private static Liberty liberty = new Liberty.Builder().baseUrl(ServerURL.getBaseUrl()).build();

    public <T> void feedback(FeedBackUpParams feedBackUpParams, NetListener<T> netListener) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, JSONException, RemoteException {
        HashMap hashMap = new HashMap(1);
        hashMap.put("content-type", "application/json");
        liberty.postWithHeader(ServerURL.FEED_BACK, hashMap, feedBackUpParams, AbstractResponse.class, netListener);
    }

    public <T> void getDownLoadAssetList(HashMap hashMap, NetListener<T> netListener) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, JSONException, RemoteException {
        liberty.get(ServerURL.MATERIAL_INFO, hashMap, AssetListDownResponse.class, netListener);
    }

    public void downLoadAsset(String str, DownloadListener downloadListener) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, JSONException, RemoteException {
        liberty.downloadFileSync(str, downloadListener);
    }
}
