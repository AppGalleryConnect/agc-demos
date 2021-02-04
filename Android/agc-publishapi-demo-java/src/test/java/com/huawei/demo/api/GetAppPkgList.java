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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;

/**
 * Querying the Software Package List of an Application
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class GetAppPkgList {
    private static Integer FROM_REC_COUNT = 1; // replace by your actual fromRecCount

    private static Integer MAX_REQ_COUNT = 10; // replace by your actual maxReqCount

    public static JSONObject getAppPkgList(String domain, String clientId, String token, String appId) {
        HttpGet put = new HttpGet(domain + "/publish/v2/package-list?appId=" + appId + "&fromRecCount=" + FROM_REC_COUNT
            + "&maxReqCount=" + MAX_REQ_COUNT);
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
                    // Software package list queried successfully.
                } else {
                    // Failed to query the software package list, please refer to the
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
        } finally {
            put.releaseConnection();
        }
        return null;
    }
}