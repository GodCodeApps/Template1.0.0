package com.example.net.parser;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import org.json.JSONException;

public class DefaultParser<T> implements IApiResultParseable {
    public static final String TAG = "DefaultParser";
    Class<T> tClass;

    public DefaultParser(Class<T> cls) {
        this.tClass = cls;
    }

    @Override // com.example.net.parser.IApiResultParseable
    public <T> T parse(String str) throws JSONException, IOException, RemoteException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            T t = (T) new Gson().fromJson(str, (Class) this.tClass);
            if (t == null) {
                return null;
            }
            Log.d(TAG, "DefaultParser:" + t.toString());
            return t;
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            throw new JSONException(e.getMessage());
        } catch (JsonIOException e2) {
            throw new IOException(e2.getMessage());
        }
    }
}
