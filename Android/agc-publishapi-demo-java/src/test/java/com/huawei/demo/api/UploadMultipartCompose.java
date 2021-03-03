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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Combine segment files
 *
 * @author xxxxxxx
 * @since 2021-01-14
 */
public class UploadMultipartCompose {
    private static String OBJECTID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual objectId

    private static String NSPUPLOADID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual nspUploadId

    private static String ADDITIONALPROP1_PARTOBJECTID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual additionalProp1 partObjectId

    private static String ADDITIONALPROP1_ETAG = "41a9ba6bd7130538555fac180e149f94"; //replace by your actual additionalProp1 etag

    private static String ADDITIONALPROP2_PARTOBJECTID = "CN/20200617/1592394575877-bc31da8a-f97d-4569-8ff0-2ec8ad9ea0da.png"; //replace by your actual additionalProp2 partObjectId

    private static String ADDITIONALPROP2_ETAG = "2b8dd6a26f8eaa6d32d3a364f55d2a92"; //replace by your actual additionalProp2 etag


    public static JSONObject uploadMultipartCompose(String domain, String clientId, String token) {
        HttpPost put = new HttpPost(
                domain + "/publish/v2/upload/multipart/compose?objectId=" + OBJECTID + "&nspUploadId=" + NSPUPLOADID);
        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);
        String s = "{\n" +
                "    \"additionalProp1\": {\n" +
                "        \"partObjectId\": " + ADDITIONALPROP1_PARTOBJECTID + ",\n" +
                "        \"etag\": " + ADDITIONALPROP1_ETAG + "\n" +
                "    },\n" +
                "    \"additionalProp2\": {\n" +
                "        \"partObjectId\": " + ADDITIONALPROP2_PARTOBJECTID + ",\n" +
                "        \"etag\": " + ADDITIONALPROP2_ETAG + "\n" +
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
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    // Segment files merged successfully
                } else {
                    // Failed to merge segment files., please refer to the
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
        }finally {
            put.releaseConnection();
        }
        return null;
    }
}