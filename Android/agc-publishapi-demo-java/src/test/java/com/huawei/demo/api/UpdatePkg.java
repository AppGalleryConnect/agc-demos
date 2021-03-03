/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
import java.nio.charset.Charset;

/**
 * Associate the obb file with the APK software package
 *
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class UpdatePkg {
    private static String PKGVERSION = "63030472658304"; //replace by your actual pkgVersion

    private static String MAINOBBFILE_OBSOBJECTID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual mainObbFile-obsObjectId

    private static String MAINOBBFILE_FILENAME = "123.png"; //replace by your actual mainObbFile-fileName

    private static String MAINOBBFILE_FILESIZE = "123456"; //replace by your actual mainObbFile-fileSize

    private static String MAINOBBFILE_SHA256 = "63030472658304"; //replace by your actual mainObbFile-sha256

    private static String PATCHOBBFILE_OBSOBJECTID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual patchObbFile-obsObjectId

    private static String PATCHOBBFILE_FILENAME = "456.png"; //replace by your actual patchObbFile-fileName

    private static String PATCHOBBFILE_FILESIZE = "123456"; //replace by your actual patchObbFile-fileSize

    private static String PATCHOBBFILE_SHA256 = "63030472658304"; //replace by your actual patchObbFile-sha256


    public static JSONObject updatePkg(String domain, String clientId, String token, String appId) {
        HttpPut put = new HttpPut(domain + "/publish/v2/package?appId=" + appId);
        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);
        String s = "{\n" +
                "    \"pkgVersion\": "+ PKGVERSION + ",\n" +
                "    \"mainObbFile\": {\n" +
                "        \"obsObjectId\": " + MAINOBBFILE_OBSOBJECTID + ",\n" +
                "        \"fileName\": " + MAINOBBFILE_FILENAME + ",\n" +
                "        \"fileSize\": " + MAINOBBFILE_FILESIZE + ",\n" +
                "        \"sha256\": " + MAINOBBFILE_SHA256 + "\n" +
                "    },\n" +
                "    \"patchObbFile\": {\n" +
                "        \"obsObjectId\": " + PATCHOBBFILE_OBSOBJECTID + ",\n" +
                "        \"fileName\": " + PATCHOBBFILE_FILENAME + ",\n" +
                "        \"fileSize\": " + PATCHOBBFILE_FILESIZE + ",\n" +
                "        \"sha256\": " + PATCHOBBFILE_SHA256 + "\n" +
                "    }\n" +
                "}";
        StringEntity entity = new StringEntity(s, Charset.forName("UTF-8"));
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
                if (ret.get("code").equals(0)) {
                    //Associating the obb file with the APK software package succeeded
                } else {
                    // Failed to associate the obb file with the APK software package, please refer to the
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
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            put.releaseConnection();
        }
        return null;
    }
}