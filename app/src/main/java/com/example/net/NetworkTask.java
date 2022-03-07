package com.example.net;

import android.os.RemoteException;
import android.util.Log;
import com.example.net.interfaces.NetListener;
import com.example.net.parser.IApiResultParseable;
import java.io.IOException;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkTask<T> {
    private static final String TAG = "NetworkTask";

    public void send(Call<ResponseBody> call, final IApiResultParseable iApiResultParseable, final NetListener netListener) throws JSONException, RemoteException, IOException {
        if (call == null) {
            Log.d(TAG, "call == null");
        } else {
            call.enqueue(new Callback<ResponseBody>() {
                /* class com.example.net.NetworkTask.AnonymousClass1 */

                /* JADX DEBUG: Multi-variable search result rejected for r5v6, resolved type: com.example.net.interfaces.NetListener */
                /* JADX WARN: Multi-variable type inference failed */
                @Override // retrofit2.Callback
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response != null) {
                        try {
                            int code = response.code();
                            if (code != HttpConstants.HTTP_OK) {
                                Log.e(NetworkTask.TAG, "server statusCode=" + code);
                            }
                            if (iApiResultParseable != null && response != null) {
                                netListener.onSuccess(iApiResultParseable.parse(response.body().string()));
                            }
                        } catch (RemoteException | IOException | JSONException e) {
                            netListener.onFaile(e.toString());
                        } catch (Exception e2) {
                            netListener.onFaile(e2.toString());
                        }
                    }
                }

                @Override // retrofit2.Callback
                public void onFailure(Call<ResponseBody> call, Throwable th) {
                    netListener.onFaile(th.toString());
                }
            });
        }
    }
}
