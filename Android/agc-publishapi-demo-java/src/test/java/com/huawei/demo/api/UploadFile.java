/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.huawei.demo.model.FileInfo;
import com.huawei.demo.model.FileServerOriResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class UploadFile {

    // suffix File name extension apk/rpk/pdf/jpg/jpeg/png/bmp/mp4/mov.
    private static String SUFFIX = "png";  // file suffix,replace you real value

    /**
     * Obtain the file upload URL.
     * 
     * @param domain Domain name.
     * @param clientId clientid
     * @param token token
     * @param appId App ID.
     */
    public static JSONObject getUploadUrl(String domain, String clientId, String token, String appId)
        throws IOException {
        HttpGet get = new HttpGet(domain + "/publish/v2/upload-url?appId=" + appId + "&suffix=" + SUFFIX);

        get.setHeader("Authorization", "Bearer " + token);
        get.setHeader("client_id", clientId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();

                // Object returned by the app information query API, which can be received using the AppInfo object. For
                // details, please refer to the API reference.
                JSONObject object = JSON.parseObject(result);

                return object;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * Upload a file.
     * 
     * @param domain Domain name.
     * @param clientId clientid
     * @param token token
     * @param appId App ID.
     * @return Response.
     */
    public static List<FileInfo> uploadFile(String domain, String clientId, String token, String appId)
        throws IOException {
        JSONObject object = getUploadUrl(domain, clientId, token, appId);

        String authCode = String.valueOf(object.get("authCode"));

        String uploadUrl = String.valueOf(object.get("uploadUrl"));

        // HttpPost post = new HttpPost(uploadParams.get("uploadUrl") + uploadFile);
        HttpPost post = new HttpPost(uploadUrl);

        // File to upload.
        FileBody bin = new FileBody(new File("file\\images.png"));

        // Construct a POST request.
        HttpEntity reqEntity = MultipartEntityBuilder.create()
            .addPart("file", bin)
            .addTextBody("authCode", authCode) // Obtain the authentication code.
            .addTextBody("fileCount", "1")
            .addTextBody("parseType", "1")
            .build();

        post.setEntity(reqEntity);
        post.addHeader("accept", "application/json");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {

                FileServerOriResult fileServerResult =
                    JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()), FileServerOriResult.class);

                // Obtain the result code.
                if (!"0".equals(fileServerResult.getResult().getResultCode())) {
                    // failed
                    return null;
                }

                return fileServerResult.getResult().getUploadFileRsp().getFileInfoList();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
