package com.example.net;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ResponseBody> buildBodyGetCall(@Url String str, @QueryMap Map<String, String> map);

    @POST
    Call<ResponseBody> buildBodyPostCall(@Url String str, @Body RequestBody requestBody);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> buildFiledsPostCall(@Url String str, @FieldMap Map<String, String> map);

    @POST
    Call<ResponseBody> buildPostMapCall(@Url String str, @Body Map<String, String> map);

    @Streaming
    @GET
    Call<ResponseBody> downBigFile(@Url String str);

    @Streaming
    @GET
    Call<ResponseBody> downBigFile(@Header("Range") String str, @Url String str2);

    @GET
    Call<ResponseBody> get(@Url String str);

    @GET
    Call<ResponseBody> getWithHeaderHasParamCall(@Url String str, @HeaderMap Map<String, String> map, @QueryMap Map<String, String> map2);

    @GET
    Call<ResponseBody> getWithHeaderNoParamCall(@Url String str, @HeaderMap Map<String, String> map);

    @POST
    Call<ResponseBody> postWithHeader(@Url String str, @HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @POST
    Call<ResponseBody> postWithHeaderEncoding(@Url String str, @Header("Encoding-Type") String str2, @Body RequestBody requestBody);

    @POST
    @Multipart
    Call<ResponseBody> uploadFile(@Url String str, @Part("description") RequestBody requestBody, @Part MultipartBody.Part part);

    @POST
    @Multipart
    Call<ResponseBody> uploadFiles(@Url String str, @PartMap Map<String, RequestBody> map);
}
