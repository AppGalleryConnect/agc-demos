/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Upload a File by Segment
 *
 * @author xxxxxxx
 * @since 2021-01-14
 */
public class UploadSegment {
    private static String PATHURL = "E:\\123.apk"; // replace by your actual pathUrl

    public static String uploadSegment(String domain, String clientId, String token) {
        JSONObject object = UploadMultipartParts.uploadMultipartParts(domain, clientId, token);
        String a = String.valueOf(object.get("uploadInfoMap"));
        Map<String, JSONObject> uploadInfoMap = UploadSegment.toMap(a);

        String method = String.valueOf(uploadInfoMap.get("additionalProp1").get("method"));
        String url = String.valueOf(uploadInfoMap.get("additionalProp1").get("url"));
        Map<String, String> headers =
            UploadSegment.toMap(String.valueOf(uploadInfoMap.get("additionalProp1").get("headers")));
        if (StringUtils.isEmpty(method)) {
            method = "PUT";
        }
        switch (method) {
            case "PUT":
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // File to upload.
                File tempFile = new File(PATHURL);
                HttpEntity httpEntity = new FileEntity(tempFile);

                HttpPut httpPut = new HttpPut(url);
                httpPut.setConfig(RequestConfig.custom()
                    .setConnectTimeout(KeyConstants.TIME_OUT)
                    .setConnectionRequestTimeout(KeyConstants.TIME_OUT)
                    .setSocketTimeout(KeyConstants.TIME_OUT)
                    .build());
                if (headers != null) {
                    headers.forEach(httpPut::setHeader);
                }
                if (httpEntity != null) {
                    httpPut.setEntity(httpEntity);
                }
                try {
                    httpClient.execute(httpPut);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "POST":
                CloseableHttpClient httpClient2 = HttpClients.createDefault();

                // File to upload.
                File tempFile2 = new File(PATHURL);
                HttpEntity httpEntity2 = new FileEntity(tempFile2);

                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(RequestConfig.custom()
                    .setConnectTimeout(KeyConstants.TIME_OUT)
                    .setConnectionRequestTimeout(KeyConstants.TIME_OUT)
                    .setSocketTimeout(KeyConstants.TIME_OUT)
                    .build());
                if (headers != null) {
                    headers.forEach(httpPost::setHeader);
                }
                if (httpEntity2 != null) {
                    httpPost.setEntity(httpEntity2);
                }
                try {
                    httpClient2.execute(httpPost);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        return null;
    }

    private static <T> Map<String, T> toMap(String json) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isEmpty(json) || "null".equals(json)) {
            return (Map<String, T>) resultMap;
        }

        JSONObject object = JSON.parseObject(json);
        Set<Map.Entry<String, Object>> entrySet = object.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        Map.Entry<String, Object> entry = null;
        boolean hanNext = iterator.hasNext();
        while (hanNext) {
            entry = iterator.next();
            resultMap.put(entry.getKey(), entry.getValue());
            if (null != entry.getValue()) {
                resultMap.put(entry.getKey(), entry.getValue());
            } else {
                resultMap.put(entry.getKey(), "");
            }
            hanNext = iterator.hasNext();
        }
        return (Map<String, T>) resultMap;
    }
}