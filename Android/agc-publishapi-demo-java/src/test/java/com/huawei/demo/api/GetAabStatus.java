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
 * Query the Software Package Compilation Status
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class GetAabStatus {
    private static String PKGVERSION = "63030472658304"; // replace by your actual pkgVersion

    public static JSONObject getAabStatus(String domain, String clientId, String token, String appId) {
        HttpGet get =
            new HttpGet(domain + "/publish/v2/aab/complile/status?appId=" + appId + "&pkgVersion=" + PKGVERSION);

        get.setHeader("Authorization", "Bearer " + token);
        get.setHeader("client_id", clientId);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();

                // Object returned by the app information query API, which can be received using the AppInfo object. For
                // details, please refer to the API reference.
                JSONObject object = JSON.parseObject(result);
                JSONObject ret = (JSONObject) object.get("ret");
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    // The software package compilation status is queried successfully
                } else {
                    // Failed to query the compilation status of the software package, please refer to the
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