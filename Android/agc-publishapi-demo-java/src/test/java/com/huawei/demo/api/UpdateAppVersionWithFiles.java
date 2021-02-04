/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.demo.common.KeyConstants;
import com.huawei.demo.model.PackageFileInfo;

/**
 * Associate software packages with applications by downloading
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class UpdateAppVersionWithFiles {
    private static String REQUEST_ID = "1"; // replace by your actual requestId

    private static String CALLBACK_ATTR = "www.callbackurl.com"; // replace by your actual url

    private static String DOWNLOAD_FILENAME = "fileName"; // replace by your actual file name

    private static String DOWNLOAD_URL = "www.testurl.com"; // replace by your actual downloadUrl

    private static String SENSITIVE_PERMISSION_DESC = "hello"; // replace by your actual SensitivePermissionDesc

    public static void updateAppVersionWithFiles(String domain, String clientId, String token, String appId) {
        HttpPost post = new HttpPost(domain + "/publish/v2/app-version-with-file/batch?appId=" + appId);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("client_id", clientId);
        JSONObject keyString = new JSONObject();
        keyString.put("requestId", REQUEST_ID);
        keyString.put("callbackAddr", CALLBACK_ATTR);
        List<PackageFileInfo> packageFileInfos = new ArrayList<>();
        PackageFileInfo packageFileInfo = new PackageFileInfo();
        packageFileInfo.setDownloadFileName(DOWNLOAD_FILENAME);
        packageFileInfo.setDownloadUrl(DOWNLOAD_URL);
        packageFileInfo.setSensitivePermissionDesc(SENSITIVE_PERMISSION_DESC);
        packageFileInfos.add(packageFileInfo);
        keyString.put("downloadFiles", packageFileInfos);
        StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
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
                    // The application is associated with the software package successfully
                } else {
                    // Failed to Associate Software Packages with Applications by Downloading Software Packages, please
                    // refer to the meaning of the error code in the information for the specific reason
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