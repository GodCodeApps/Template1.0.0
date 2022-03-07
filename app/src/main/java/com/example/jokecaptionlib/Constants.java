package com.example.jokecaptionlib;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Constants {
    static String a = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n    <storyboard sceneWidth=\"720\" sceneHeight=\"1280\">";
    static String b = "\n    \t\t<textTrack source=\"placeholder\" fontFamily=\"\" alignX=\"left\" posX=\"0\" charAnimation=\"1\"  height=\"60\" color=\"1,1,1,1\" clipStart=\"0\" clipDuration=\"3600000\" posterTimeHint=\"2000\">";
    static String c = "\t</textTrack>";
    static String d = "\n</storyboard>";
    static String e = "\n\t</effect>";
    static String f = "\n\t\t<effect name=\"transform\">\n\t\t\t\t";
    static String g = "<animation paramName=\"rotationZ\">\n\t\t\t\t\t";
    static String h = "<animation paramName=\"transX\">\n\t\t\t\t\t\t";
    static String i = "<animation paramName=\"transY\">\n\t\t\t\t\t";
    static String j = "\n\t\t\t\t<animation paramName=\"opacity\">\n\t\t\t\t\t";
    static String k = "\n\t\t\t\t<animation paramName=\"scaleX\">\n\t\t\t\t\t";
    static String l = "\n\t\t\t\t<animation paramName=\"scaleY\">\n\t\t\t\t\t";
    static String m = "\n\t\t\t\t</animation>";
    static String n = "Totation";
    static String o = "TransAndScale";
    static String p = "<effect name=\"transform\">\n\t\t\t\n\t\t\t\t<animation paramName=\"scaleX\">\n\t\t\t\t\t<key time=\"0\" value=\"0\" curveMode=\"outBounce\" curvePeriod=\"0.5\" curveAmplitude=\"1\" curveOvershoot=\"1.7\"/>\n\t\t\t\t\t<key time=\"500\" value=\"1\"/>\n\t\t\t\t</animation>\n\t\t\t\t<animation paramName=\"scaleY\">\n\t\t\t\t\t<key time=\"0\" value=\"0\" curveMode=\"outBounce\" curvePeriod=\"0.5\" curveAmplitude=\"1\" curveOvershoot=\"1.7\"/>\n\t\t\t\t\t<key time=\"500\" value=\"1\"/>\n\t\t\t\t</animation>\n\t\t\t</effect>";
    static String q = "\t<effect name=\"transform\">\n\t\t\t\t<animation paramName=\"scaleX\">\n\t\t\t\t\t<key time=\"0\" value=\"0\" curveMode=\"outQuad\" curvePeriod=\"0.5\" curveAmplitude=\"1\" curveOvershoot=\"1.7\"/>\n\t\t\t\t\t<key time=\"333\" value=\"1\"/>\n\t\t\t\t</animation>\n\t\t\t\t<animation paramName=\"scaleY\">\n\t\t\t\t\t<key time=\"0\" value=\"0\" curveMode=\"outQuad\" curvePeriod=\"0.5\" curveAmplitude=\"1\" curveOvershoot=\"1.7\"/>\n\t\t\t\t\t<key time=\"333\" value=\"1\"/>\n\t\t\t\t</animation>\n\t\t\t\t<animation paramName=\"transX\">\n\t\t\t\t\t<key time=\"0\" value=\"-200\" curveMode=\"outBounce\" curvePeriod=\"0.5\" curveAmplitude=\"1\" curveOvershoot=\"1.7\"/>\n\t\t\t\t\t<key time=\"500\" value=\"0\"/>\n\t\t\t\t</animation>\n\t\t\t</effect>";
    static String r = "\t<charAnimationDesc phaseMode=\"leftToRight\" phaseTime=\"450\" maxPhaseDiff=\"220\">\n\t\t\t<charAnimation charMatchRule=\"periodicIndex\">\n\t\t\t\t<effect name=\"transform\" anchorMode=\"charCenter\">\n\t\t\t\t\t<animation paramName=\"opacity\">\n\t\t\t\t\t\t<key time=\"0\" value=\"0\"/>\n\t\t\t\t\t\t<key time=\"40\" value=\"0\"/>\n\t\t\t\t\t\t<key time=\"41\" value=\"1\"/>\n\t\t\t\t\t</animation>\n\t\t\t\t</effect>\n\t\t\t</charAnimation>\n\t\t</charAnimationDesc>";

    Constants() {
    }

    private static long a(String str, String str2, String str3) {
        int parseInt = Integer.parseInt(str);
        int parseInt2 = Integer.parseInt(str2);
        int parseInt3 = Integer.parseInt(str3);
        if (parseInt2 >= 60) {
            Log.e("警告: 出现了一个时间不正确的项 -->", "[" + str + ":" + str2 + "." + str3.substring(0, 2) + "]");
        }
        return (long) ((parseInt * 60 * 1000) + (parseInt2 * 1000) + parseInt3);
    }

    static List a(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(str);
            if (!file.isFile() || !file.exists()) {
                Log.e("jokelibConstants", "找不到指定的文件:" + str);
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
                        hashMap.put(Long.valueOf(a2), readLine.substring(matcher.end()));
                        arrayList.add(hashMap);
                    }
                } else {
                    inputStreamReader.close();
                    return arrayList;
                }
            }
        } catch (Exception e2) {
            Log.e("jokelibConstants", "读取文件出错!");
            e2.printStackTrace();
            return null;
        }
    }

    static List a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Pattern compile = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]");
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            String str = (String) arrayList.get(i2);
            Matcher matcher = compile.matcher(str);
            while (matcher.find()) {
                HashMap hashMap = new HashMap();
                String group = matcher.group(1);
                String group2 = matcher.group(2);
                String group3 = matcher.group(3);
                long a2 = a(group, group2, group3 + "0");
                hashMap.put(Long.valueOf(a2), str.substring(matcher.end()));
                arrayList2.add(hashMap);
            }
        }
        return arrayList2;
    }
}
