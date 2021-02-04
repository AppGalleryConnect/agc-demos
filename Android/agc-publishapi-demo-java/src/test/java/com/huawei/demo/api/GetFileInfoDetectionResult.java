/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;

/**
 * Query the adaptation range detection result of multiple APKs.
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class GetFileInfoDetectionResult {
    public static JSONObject getFileInfoDetectionResult(String domain, String clientId, String token, String appId) {
        HttpPost post = new HttpPost(domain + "/publish/v2/fitinfo-detection?appId=" + appId);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("client_id", clientId);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                JSONObject ret = (JSONObject) object.get("ret");
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    // Succeeded in querying the adaptation range detection result of multiple APKs
                } else {
                    // Failed to query the adaptation range detection result of multiple APKs, please refer to the
                    // meaning of the error code in the information for the specific reason
                }
                br.close();
                httpClient.close();
                return object;
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