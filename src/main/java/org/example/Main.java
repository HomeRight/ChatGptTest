package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Main {


    private static final String API_KEY = "sk-OchDOS8JJb4MHqeM8b2694C92d064a879aBe59EbA6Ba83Ce";  // 在這裡替換為你的API金鑰

    private static final String API_URL = "https://api.120509.xyz/v1/chat/completions";

    private static final String FILE_PATH = "test.txt"; // 文件路径

    public static void main(String[] args) {
        // 代理伺服器的主機名和端口號
        String proxyHost = "fetfw.fareastone.com.tw";
        int proxyPort = 8080; // 代理伺服器的端口號


        // 读取文件内容
        String fileContent = readFileContent(FILE_PATH);

        System.out.println("fileContent:" + fileContent);


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
        String json = "{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\", \"content\": \"" + fileContent + "\"}]}";

        System.out.println("json:" + json);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        // 發送API請求
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析响应体
            String responseBody = response.body().string();
            System.out.println("responseBody:" + responseBody);

            // 使用Jackson解析JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // 提取choices数组中的第一个元素的message字段
            JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

// 获取content字段的值
            String content = contentNode.asText();

            // 打印提取的message字段
            System.out.println(content);


            // 要保存的文件路径
            String filePath = "taipei_attractions.txt";

            // 将字符串写入文件
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(content);
                System.out.println("File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 读取文件内容的方法
    // 读取文件内容的方法
    private static String readFileContent(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString().trim();
    }

}