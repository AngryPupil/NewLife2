package net.pupil.newlife.common;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PupilOkHttp3Utils {

    private static OkHttpClient sClient = new OkHttpClient();

    public interface CallSucc {
        void onResponse();
    }

    public interface CallFail {
        void onFailure();
    }

    public static void callGet(String url, CallSucc callSucc) {
        callGet(url, callSucc, null);
    }

    public static void callGet(String url, CallSucc callSucc, CallFail callFail) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = sClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callFail != null) {
                    callFail.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callSucc.onResponse();
            }
        });
    }

    public static void callPost(String url, CallSucc callSucc) {
        callPost(url, callSucc, null);
    }

    public static void callPost(String url, CallSucc callSucc, CallFail callFail) {
        Request request = new Request.Builder().url(url).post(null).build();
        Call call = sClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callFail != null) {
                    callFail.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callSucc.onResponse();
            }
        });
    }

    public static List<Call> queuedCalls () {
        return sClient.dispatcher().queuedCalls();
    }

    public static int queuedCallsCount () {
        return sClient.dispatcher().queuedCallsCount();
    }

    public static void cancelAll() {
        sClient.dispatcher().cancelAll();
    }
}
