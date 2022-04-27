package com.example.mixitup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Utils {

    public interface ResponseCallback {

        void onGetResponse(String result);
    }

    private static final OkHttpClient client = new OkHttpClient();

    public static void getHttpResponseAsync(String url, String token, ResponseCallback callback) {

        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    callback.onGetResponse(responseBody.string());
                }
            }
        });
    }
}
