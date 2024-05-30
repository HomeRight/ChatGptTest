package org.example;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public class Main {


    private static final String API_KEY = "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";  // 在這裡替換為你的API金鑰

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        // 代理伺服器的主機名和端口號
        String proxyHost = "fetfw.fareastone.com.tw";
        int proxyPort = 8080; // 代理伺服器的端口號

        // 使用代理的用戶名和密碼（如果需要身份驗證）
//        String proxyUsername = "alensu";
//        String proxyPassword = "mju7VFR$";
//
//        // 建立代理授權
//        Authenticator proxyAuthenticator = new Authenticator() {
//            @Override
//            public Request authenticate(Route route, Response response) throws IOException {
//                String credential = Credentials.basic(proxyUsername, proxyPassword);
//                return response.request().newBuilder()
//                        .header("Proxy-Authorization", credential)
//                        .build();
//            }
//        };

        // 建立OkHttpClient實例，並設置代理
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                .build();

        // 構建API請求
        String json = "{\"model\": \"gpt-3.5-turbo-16k\",\"messages\": [{\"role\": \"user\", \"content\": \"Hello, how are you?\"}]}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        // 發送API請求
        try (Response response = client.newCall(request).execute()) {

            System.out.println(response.body().string());
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}