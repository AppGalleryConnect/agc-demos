/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.huawei.demo.common.KeyConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 功能描述
 *
 * @since 2021-01-13
 */
public class SubmitAppWithFile {
    private static String DOWNLOAD_NAME = "www.testurl.com"; // Your file address

    private static String DOWNLOAD_FILE_NAME = "testFile"; // The file name to be updated, the file name needs to add the
    // suffix of the corresponding file

    private static String REQUEST_ID = "1";// replace by your actual file name

    public static void submitAppWithFile(String domain, String clientId, String token, String appId) {
        HttpPost put = new HttpPost(domain + "/publish/v2/app-submit-with-file?appid=" + appId);

        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);

        JSONObject keyString = new JSONObject();
        keyString.put("downloadUrl", DOWNLOAD_NAME);
        keyString.put("downloadFileName", DOWNLOAD_FILE_NAME);
        keyString.put("requestId", REQUEST_ID);

        StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        put.setEntity(entity);

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
                    // The file has been successfully updated to the app, and the app has been successfully submitted
                    // for review
                } else {
                    // This interface is used to remove the applications that have been put on the shelve.
                    // The application failed to be removed, please refer to the
                    // meaning of the error code in the information for the specific reason
                }
                br.close();
                httpClient.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
