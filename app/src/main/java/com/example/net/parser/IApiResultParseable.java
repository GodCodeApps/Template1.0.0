package com.example.net.parser;

import android.os.RemoteException;
import java.io.IOException;
import org.json.JSONException;

public interface IApiResultParseable {
    <T> T parse(String str) throws JSONException, IOException, RemoteException;
}
