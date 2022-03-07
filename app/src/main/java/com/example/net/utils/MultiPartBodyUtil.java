package com.example.net.utils;

import java.io.File;
import okhttp3.MultipartBody;

public class MultiPartBodyUtil {
    public static MultipartBody.Part createMultiPart(String str, String str2, File file) {
        return MultipartBody.Part.createFormData(str, str2, RequestBodyUtil.createMultiPartBody(file));
    }

    public static MultipartBody.Part createBytesMultiPart(String str, String str2, byte[] bArr) {
        return MultipartBody.Part.createFormData(str, str2, RequestBodyUtil.createMultiPartBytesBody(bArr));
    }
}
