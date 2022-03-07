package com.meishe.effect;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class Constants {
    private static long a(String str, String str2, String str3) {
        int parseInt = Integer.parseInt(str);
        int parseInt2 = Integer.parseInt(str2);
        int parseInt3 = Integer.parseInt(str3);
        if (parseInt2 >= 60) {
            Log.e("警告: 出现了一个时间不正确的项 -->", "[" + str + ":" + str2 + "." + str3.substring(0, 2) + "]");
        }
        return (long) ((parseInt * 60 * 1000) + (parseInt2 * 1000) + parseInt3);
    }

    static List a(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            if (!str.equals("")) {
                InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(str), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Pattern compile = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]");
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        Matcher matcher = compile.matcher(readLine);
                        while (matcher.find()) {
                            HashMap hashMap = new HashMap();
                            String group = matcher.group(1);
                            String group2 = matcher.group(2);
                            String group3 = matcher.group(3);
                            long a = a(group, group2, group3 + "0");
                            String substring = readLine.substring(matcher.end());
                            if (!substring.equals("")) {
                                hashMap.put(Long.valueOf(a), substring);
                                arrayList.add(hashMap);
                            }
                        }
                    } else {
                        inputStreamReader.close();
                        return arrayList;
                    }
                }
            } else {
                Log.e("ContentValues", "找不到指定的文件:" + str);
                return null;
            }
        } catch (Exception e) {
            Log.e("ContentValues", "读取文件出错!");
            e.printStackTrace();
            return null;
        }
    }

    static List a(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(str);
            if (!file.isFile() || !file.exists()) {
                Log.e("ContentValues", "找不到指定的文件:" + str);
                return null;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Pattern compile = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]");
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    Matcher matcher = compile.matcher(readLine);
                    while (matcher.find()) {
                        HashMap hashMap = new HashMap();
                        String group = matcher.group(1);
                        String group2 = matcher.group(2);
                        String group3 = matcher.group(3);
                        long a = a(group, group2, group3 + "0");
                        String substring = readLine.substring(matcher.end());
                        if (!substring.equals("")) {
                            hashMap.put(Long.valueOf(a), substring);
                            arrayList.add(hashMap);
                        }
                    }
                } else {
                    inputStreamReader.close();
                    return arrayList;
                }
            }
        } catch (Exception e) {
            Log.e("ContentValues", "读取文件出错!");
            e.printStackTrace();
            return null;
        }
    }

    public static String analysisJsonFile(Context context, String str) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(str)), Key.STRING_CHARSET_NAME);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    bufferedReader.close();
                    inputStreamReader.close();
                    try {
                        return new JSONObject(sb.toString()).getString("captionstyle");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String analysisJsonFileFromAsset(Context context, String str) {
        if (context == null || str == null) {
            return "";
        }
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
                    try {
                        return new JSONObject(sb.toString()).getString("captionstyle");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static ArrayList getAllFilesFromAssets(Context context, String str) {
        String[] strArr;
        AssetManager assets = context.getAssets();
        ArrayList arrayList = new ArrayList();
        try {
            strArr = assets.list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        for (String str2 : strArr) {
            arrayList.add(str + File.separator + str2);
        }
        return arrayList;
    }

    public static ArrayList getAllFilesFromSD(String str, String str2, boolean z) {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = new File(str).listFiles();
        for (File file : listFiles) {
            if (file.isFile()) {
                if (file.getPath().substring(file.getPath().length() - str2.length()).equals(str2)) {
                    arrayList.add(file.getPath());
                }
                if (!z) {
                    break;
                }
            } else if (file.isDirectory() && file.getPath().indexOf("/.") == -1) {
                arrayList.addAll(getAllFilesFromSD(file.getPath(), str2, z));
            }
        }
        return arrayList;
    }

    public static String readAssetsTxt(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "读取错误，请检查文件名";
        }
    }

    public static String readFileTxt(String str) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(str)), Key.STRING_CHARSET_NAME);
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
