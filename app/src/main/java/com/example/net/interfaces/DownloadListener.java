package com.example.net.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface DownloadListener {
    void onFaile(String str);

    void onSuccess(Response<ResponseBody> response);
}
