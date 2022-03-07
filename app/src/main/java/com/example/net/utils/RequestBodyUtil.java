package com.example.net.utils;

import com.example.net.parser.HttpContentType;
import com.google.gson.Gson;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodyUtil {
    public static RequestBody createJsonBody(String str) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_JSON), str);
    }

    public static RequestBody createMultiBody(String str) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA), str);
    }

    public static RequestBody createJsonBody(Object obj) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_JSON), new Gson().toJson(obj));
    }

    public static RequestBody createMultiPartBody(File file) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA), file);
    }

    public static RequestBody createMultiPartBytesBody(byte[] bArr) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA), bArr);
    }

    public static RequestBody createBytesBody(byte[] bArr) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_BYTES), bArr);
    }

    public static RequestBody createTextBody(String str) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_TEXT), str);
    }
}
