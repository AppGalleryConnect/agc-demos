/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Software Package Segment upload Initialize
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class UploadMultipartInit {
    private static String FILENAME = "123.apk"; //replace by your actual fileName

    private static String FILETYPE = "3"; //replace by your actual fileType

    public static JSONObject uploadMultipartInit(String domain, String clientId, String token, String appId) {
        HttpPost put =
                new HttpPost(domain + "/publish/v2/upload/multipart/init?appId=" + appId + "&fileName=" + FILENAME + "&fileType=" + FILETYPE);
        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(put);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                JSONObject ret = (JSONObject) object.get("ret");
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    //File segment upload initialization succeeded
                } else {
                    // Failed to initialize the file segment upload, please refer to the
                    // meaning of the error code in the information for the specific reason
                }
                return object;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
