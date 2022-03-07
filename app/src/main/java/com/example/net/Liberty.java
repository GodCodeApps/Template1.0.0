package com.example.net;

import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.example.net.interfaces.DownloadListener;
import com.example.net.interfaces.NetListener;
import com.example.net.parser.DefaultParser;
import com.example.net.parser.HttpContentType;
import com.example.net.utils.MultiPartBodyUtil;
import com.example.net.utils.RequestBodyUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Liberty {
    private static final String TAG = "Liberty";
    private ApiService apiService;
    private String baseUrl;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private Liberty(OkHttpClient okHttpClient2, Retrofit retrofit3, ApiService apiService2, String str) {
        this.apiService = apiService2;
        this.retrofit = retrofit3;
        this.okHttpClient = okHttpClient2;
        this.baseUrl = str;
    }

    public static final class Builder {
        private String baseUrl;
        private long connectTimeout = 30000;
        private OkHttpClient okHttpClient;
        private long readTimeout = 30000;

        public Builder() {
        }

        Builder(Liberty liberty) {
            this.okHttpClient = liberty.okHttpClient;
            this.connectTimeout = (long) liberty.okHttpClient.connectTimeoutMillis();
            this.readTimeout = (long) liberty.okHttpClient.readTimeoutMillis();
            this.baseUrl = liberty.baseUrl;
        }

        public Builder connectTimeout(long j) {
            this.connectTimeout = j;
            return this;
        }

        public Builder readTimeout(long j) {
            this.readTimeout = j;
            return this;
        }

        public Builder baseUrl(String str) {
            this.baseUrl = str;
            return this;
        }

        public Liberty build() {
            Retrofit buildRetrofit = buildRetrofit(this.baseUrl);
            return new Liberty(this.okHttpClient, buildRetrofit, (ApiService) buildRetrofit.create(ApiService.class), this.baseUrl);
        }

        public Retrofit buildRetrofit(String str) {
            Gson create = new GsonBuilder().setLenient().create();
            if (this.okHttpClient == null) {
                this.okHttpClient = initOkHttpClient();
            }
            return new Retrofit.Builder().client(this.okHttpClient).baseUrl(str).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(create)).build();
        }

        private OkHttpClient initOkHttpClient() {
            OkHttpClient.Builder unsafeOkHttpBuilder = getUnsafeOkHttpBuilder(new OkHttpClient.Builder());
            unsafeOkHttpBuilder.connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS);
            unsafeOkHttpBuilder.readTimeout(this.readTimeout, TimeUnit.MILLISECONDS);
            List<Interceptor> interceptors = unsafeOkHttpBuilder.interceptors();
            if (interceptors != null) {
                interceptors.clear();
            }
            return unsafeOkHttpBuilder.build();
        }

        public OkHttpClient.Builder getUnsafeOkHttpBuilder(OkHttpClient.Builder builder) {
            SSLContext sSLContext;
            NoSuchAlgorithmException e;
            KeyManagementException e2;
            X509TrustManager r1 = new X509TrustManager() {
                /* class com.example.net.Liberty.Builder.AnonymousClass1 */

                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            try {
                sSLContext = SSLContext.getInstance("SSL");
                try {
                    sSLContext.init(null, new TrustManager[]{r1}, new SecureRandom());
                } catch (KeyManagementException e4) {
                    e2 = e4;
                    Log.e(Liberty.TAG, e2.toString());
                    builder.sslSocketFactory(sSLContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                        /* class com.example.net.Liberty.Builder.AnonymousClass2 */

                        public boolean verify(String str, SSLSession sSLSession) {
                            return true;
                        }
                    });
                    return builder;
                }
            } catch (NoSuchAlgorithmException e5) {
                e = e5;
                sSLContext = null;
                Log.e(Liberty.TAG, e.toString());
                builder.sslSocketFactory(sSLContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                    /* class com.example.net.Liberty.Builder.AnonymousClass2 */

                    public boolean verify(String str, SSLSession sSLSession) {
                        return true;
                    }
                });
                return builder;
            }
            builder.sslSocketFactory(sSLContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                /* class com.example.net.Liberty.Builder.AnonymousClass2 */

                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }
            });
            return builder;
        }
    }

    public <T> void get(String str, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.get(str), new DefaultParser(cls), netListener);
    }

    public <T> void get(String str, Map<String, String> map, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.buildBodyGetCall(str, map), new DefaultParser(cls), netListener);
    }

    public <T> void getWithHeader(String str, Map<String, String> map, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.getWithHeaderNoParamCall(str, map), new DefaultParser(cls), netListener);
    }

    public <T> void getWithHeaderAndParm(String str, Map<String, String> map, Map<String, String> map2, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.getWithHeaderHasParamCall(str, map, map2), new DefaultParser(cls), netListener);
    }

    public <T> void post(String str, Object obj, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.buildBodyPostCall(str, RequestBodyUtil.createJsonBody(obj)), new DefaultParser(cls), netListener);
    }

    public <T> void postForm(String str, Map<String, String> map, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.buildFiledsPostCall(str, map), new DefaultParser(cls), netListener);
    }

    public <T> void postWithEncoding(String str, String str2, byte[] bArr, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.postWithHeaderEncoding(str, str2, RequestBodyUtil.createBytesBody(bArr)), new DefaultParser(cls), netListener);
    }

    public <T> void postWithHeader(String str, Map<String, String> map, Object obj, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.postWithHeader(str, map, RequestBodyUtil.createJsonBody(obj)), new DefaultParser(cls), netListener);
    }

    public <T> void uploadFile(String str, String str2, File file, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.uploadFile(str, RequestBodyUtil.createTextBody(str2), MultiPartBodyUtil.createMultiPart(str2, str2, file)), new DefaultParser(cls), netListener);
    }

    public <T> void uploadFile(String str, String str2, byte[] bArr, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        new NetworkTask().send(this.apiService.uploadFile(str, RequestBodyUtil.createTextBody(str2), MultiPartBodyUtil.createBytesMultiPart(str2, str2, bArr)), new DefaultParser(cls), netListener);
    }

    public <T> void uploadImageWithParam(String str, Map<String, String> map, String str2, byte[] bArr, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }
        new NetworkTask().send(this.apiService.uploadFile(str, RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_JSON), jsonObject.toString()), MultiPartBodyUtil.createBytesMultiPart(str2, str2, bArr)), new DefaultParser(cls), netListener);
    }

    public <T> void uploadImagesWithParams(String str, String str2, Object obj, String str3, List<String> list, Class<T> cls, NetListener netListener) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(str2, RequestBody.create((MediaType) null, new Gson().toJson(obj)));
        for (String str4 : list) {
            File file = new File(str4);
            RequestBody create = RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA), file);
            arrayMap.put(str3 + "\";filename=\"" + file.getName(), create);
        }
        new NetworkTask().send(this.apiService.uploadFiles(str, arrayMap), new DefaultParser(cls), netListener);
    }

    public void downloadFileSync(String str, final DownloadListener downloadListener) throws IOException {
        this.apiService.get(str).enqueue(new Callback<ResponseBody>() {
            /* class com.example.net.Liberty.AnonymousClass1 */

            @Override // retrofit2.Callback
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                downloadListener.onSuccess(response);
            }

            @Override // retrofit2.Callback
            public void onFailure(Call<ResponseBody> call, Throwable th) {
                if (downloadListener != null) {
                    downloadListener.onFaile("下载出错!" + th.toString());
                }
            }
        });
    }

    public void downloadFileBreakPoint(String str, String str2, String str3, final DownloadListener downloadListener) throws IOException {
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str3);
        long length = file2.length();
        file2.exists();
        this.apiService.downBigFile("bytes=" + length + "-", str).enqueue(new Callback<ResponseBody>() {
            /* class com.example.net.Liberty.AnonymousClass2 */

            @Override // retrofit2.Callback
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (downloadListener != null) {
                    downloadListener.onSuccess(response);
                }
            }

            @Override // retrofit2.Callback
            public void onFailure(Call<ResponseBody> call, Throwable th) {
                if (downloadListener != null) {
                    downloadListener.onFaile("下载出错!" + th.toString());
                }
            }
        });
    }
}
