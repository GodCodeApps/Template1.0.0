package com.meishe.photoalbum;

import android.content.Context;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class NvUtil {
    public static String loadFromAssetsFile(Context context, String str) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(str), Key.STRING_CHARSET_NAME);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    bufferedReader.close();
                    inputStreamReader.close();
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadFromSDFile(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return null;
            }
            byte[] bArr = new byte[((int) file.length())];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bArr);
            fileInputStream.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("NvUtil", "Failed to read file: " + str);
            return null;
        }
    }
}
