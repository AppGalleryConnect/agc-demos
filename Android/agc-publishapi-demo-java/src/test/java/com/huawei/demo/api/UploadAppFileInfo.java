/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.huawei.demo.common.KeyConstants;
import com.huawei.demo.model.FileInfo;
import com.huawei.demo.model.PublishFileInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class UploadAppFileInfo {
    public static void updateAppFileInfo(String domain, String clientId, String token, String appId,
        List<FileInfo> files) throws InvocationTargetException, IllegalAccessException {
        HttpPut put = new HttpPut(domain + "/publish/v2/app-file-info?appId=" + appId);

        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);

        JSONObject keyString = new JSONObject();

        // Set the language.
        keyString.put("lang", "zh-CN");

        List<PublishFileInfo> fileInfos = new ArrayList<>();
        for (FileInfo fileInfo : files) {
            PublishFileInfo publishFileInfo = new PublishFileInfo();
            BeanUtils.copyProperties(publishFileInfo, fileInfo);

            publishFileInfo.setFileDestUrl(fileInfo.getFileDestUlr());

            fileInfos.add(publishFileInfo);
        }

        keyString.put("files", fileInfos);

        // Update the icon.
        keyString.put("fileType", 0);

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
                    // The application file information has been updated successfully
                } else {
                    // The application file information has been updated successfully, please refer to the error code
                    // statement in the materials for specific reasons
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
