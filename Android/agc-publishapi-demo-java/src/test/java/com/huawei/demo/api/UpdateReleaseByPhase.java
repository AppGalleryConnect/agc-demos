/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;

/**
 * Update Phased Release
 *
 * @author xxxxxxx
 * @since 2021-01-12
 */
public class UpdateReleaseByPhase {
    private static String STATE = "RELEASE"; // replace by your actual state

    public static void updateReleaseByPhase(String domain, String clientId, String token, String appId) {
        HttpPut put = new HttpPut(domain + "/publish/v2/phased-release?appId=" + appId);

        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);

        JSONObject keyString = new JSONObject();
        keyString.put("state", STATE);

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
                    // The phased release is updated successfully.
                } else {
                    // Failed to update the phased release, please refer to the meaning of the error code for the
                    // specific reason
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