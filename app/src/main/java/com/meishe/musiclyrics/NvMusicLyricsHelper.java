package com.meishe.musiclyrics;

import android.content.Context;
import android.util.Log;
import com.meicam.effect.sdk.NvsEffectSdkContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NvMusicLyricsHelper {
    private static final String a = "NvMusicLyricsHelper";

    private static long a(String str, String str2, String str3) {
        int parseInt = Integer.parseInt(str);
        int parseInt2 = Integer.parseInt(str2);
        int parseInt3 = Integer.parseInt(str3);
        if (parseInt2 >= 60) {
            Log.e("警告: 出现了一个时间不正确的项 -->", "[" + str + ":" + str2 + "." + str3.substring(0, 2) + "]");
        }
        return (long) ((parseInt * 60 * 1000) + (parseInt2 * 1000) + parseInt3);
    }

    private static List a(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        if (str.length() > 8) {
            String substring = str.substring(8);
            try {
                if (!str.equals("")) {
                    InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(substring), "utf-8");
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
                                long a2 = a(group, group2, group3 + "0");
                                String trim = readLine.substring(matcher.end()).trim();
                                if (!trim.equals("")) {
                                    hashMap.put(Long.valueOf(a2), trim);
                                    arrayList.add(hashMap);
                                }
                            }
                        } else {
                            inputStreamReader.close();
                            return arrayList;
                        }
                    }
                } else {
                    String str2 = a;
                    Log.e(str2, "找不到指定的文件:" + str);
                }
            } catch (Exception e) {
                Log.e(a, "读取文件出错!");
                e.printStackTrace();
            }
        }
        return null;
    }

    private static List a(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(str);
            if (!file.isFile() || !file.exists()) {
                String str2 = a;
                Log.e(str2, "找不到指定的文件:" + str);
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
                        long a2 = a(group, group2, group3 + "0");
                        String substring = readLine.substring(matcher.end());
                        if (!substring.equals("")) {
                            hashMap.put(Long.valueOf(a2), substring);
                            arrayList.add(hashMap);
                        }
                    }
                } else {
                    inputStreamReader.close();
                    return arrayList;
                }
            }
        } catch (Exception e) {
            Log.e(a, "读取文件出错!");
            e.printStackTrace();
            return null;
        }
    }

    public static List analysisLrcFile(Context context, String str) {
        String str2;
        String str3;
        if (!NvsEffectSdkContext.functionalityAuthorised("lyrics")) {
            str2 = a;
            str3 = "Functionality lyrics is not authorised!";
        } else {
            Boolean bool = Boolean.TRUE;
            new ArrayList();
            if (str.equals("") || str == null) {
                str2 = a;
                str3 = "invaild lrc file!";
            } else {
                return (str.indexOf("assets:") == 0 ? Boolean.TRUE : Boolean.FALSE).booleanValue() ? a(context, str) : a(str);
            }
        }
        Log.e(str2, str3);
        return null;
    }

    public static ArrayList getCaptionInfoList(List list, long j, long j2, long j3) {
        String str;
        String str2;
        if (!NvsEffectSdkContext.functionalityAuthorised("lyrics")) {
            str = a;
            str2 = "Functionality lyrics is not authorised!";
        } else {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (j < 0 || j2 < 0 || j3 < 0 || j > j2) {
                str = a;
                str2 = "invaild Time!";
            } else {
                if (list != null && list.size() > 0) {
                    int i = 0;
                    int i2 = -1;
                    int i3 = -1;
                    while (i < list.size()) {
                        Set<Map.Entry > entrySet = ((Map) list.get(i)).entrySet();
                        for (Map.Entry entry :entrySet ) {
                            MusicCaptionInfo musicCaptionInfo = new MusicCaptionInfo();
                            musicCaptionInfo.setCaptionText(((String) entry.getValue()).toString());
                            musicCaptionInfo.setCaptionStartTime(Long.valueOf(((Long) entry.getKey()).toString()).longValue() * 1000);
                            arrayList2.add(musicCaptionInfo);
                            long longValue = Long.valueOf(((Long) entry.getKey()).toString()).longValue();
                            if (longValue < j / 1000 || i == 0) {
                                i2 = i;
                            }
                            if (i3 < 0 && longValue >= j2 / 1000) {
                                i3 = i != 0 ? i - 1 : i;
                            }
                            if (i3 < 0 && j2 / 1000 >= longValue && i == list.size() - 1) {
                                i3 = i;
                            }
                        }
                        i++;
                    }
                    if (arrayList2.size() <= 0 || i2 == -1 || i3 == -1 || i2 >= list.size() || i3 >= list.size() || i2 > i3) {
                        return null;
                    }
                    int i4 = i2;
                    while (i4 <= i3) {
                        long captionStartTime = (i4 != i2 || (i2 == 0 && j < ((MusicCaptionInfo) arrayList2.get(i4)).getCaptionStartTime())) ? ((MusicCaptionInfo) arrayList2.get(i4)).getCaptionStartTime() - (j - j3) : j - (j - j3);
                        long captionStartTime2 = (i2 == i3 || i4 == i3 || i4 == list.size() + -1) ? j2 - (j - j3) : ((MusicCaptionInfo) arrayList2.get(i4 + 1)).getCaptionStartTime() - (j - j3);
                        MusicCaptionInfo musicCaptionInfo2 = new MusicCaptionInfo();
                        musicCaptionInfo2.setCaptionText(((MusicCaptionInfo) arrayList2.get(i4)).getCaptionText());
                        musicCaptionInfo2.setCaptionStartTime(captionStartTime);
                        musicCaptionInfo2.setCaptionDurtion(captionStartTime2 - captionStartTime);
                        arrayList.add(musicCaptionInfo2);
                        i4++;
                    }
                }
                return arrayList;
            }
        }
        Log.e(str, str2);
        return null;
    }
}
