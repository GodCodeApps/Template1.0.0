package com.meicam.sdk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class NvsHttpsRequest {
    public String httpsRequest(String str, Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        String str2 = str + "?";
        int size = map.size();
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            str2 = str2 + entry.getKey() + "=" + entry.getValue();
            i++;
            if (i < size) {
                str2 = str2 + "&";
            }
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public String postHttpsRequest(String str, Map<String, String> map, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        String str3 = str + "?";
        int size = map.size();
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            str3 = str3 + entry.getKey() + "=" + entry.getValue();
            i++;
            if (i < size) {
                str3 = str3 + "&";
            }
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str3).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(str2);
            dataOutputStream.flush();
            dataOutputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
