/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取安装失败数据的报表:AppDownloadFailReportDemo
 */
public class AppDownloadFailReportDemo {
    // replace with your client id
    private static final String YOUR_CLIENT_ID = "5223**********6768";

    // replace with your client secret
    private static final String YOUR_CLIENT_SECRET = "879FFF****************************************************B4CC86";

    // replace with your APP ID
    private static final String YOUR_APP_ID = "10*****49";

    private static final String GATEWAY_URL = "https://connect-api.cloud.huawei.com";

    public static void main(String[] args) {
        String token = getToken();
        getReport(token);
    }

    private static String getToken() {
        String token = null;
        HttpPost httpPost = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            String url = String.format(Locale.ROOT, "%s/api/oauth2/v1/token", GATEWAY_URL);
            httpPost = new HttpPost(url);

            JSONObject keyString = new JSONObject();
            keyString.put("client_id", YOUR_CLIENT_ID);
            keyString.put("client_secret", YOUR_CLIENT_SECRET);
            keyString.put("grant_type", "clieant_credentials");
            StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                token = object.getString("access_token");
            }
        } catch (Exception e) {
            // record error log or throw exception
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return token;
    }

    private static void getReport(String token) {
        String appId = YOUR_APP_ID;
        String language = "zh-CN";
        String startTime = "20200101";
        String endTime = "20200130";

        List<String> filterCondition = new ArrayList<>(16);
        List<String> filterConditionValue = new ArrayList<>(16);
        filterCondition.add("countryId");
        filterConditionValue.add("CN");

        StringBuilder conditions = new StringBuilder();
        for (int i = 0; i < filterCondition.size(); i++) {
            conditions.append("&filterCondition=");
            conditions.append(filterCondition.get(i));
            conditions.append("&filterConditionValue=");
            conditions.append(filterConditionValue.get(i));
        }

        String groupBy="date";

        String url = String.format(
            Locale.ROOT, "%s/api/report/distribution-operation-quality/v1/appDownloadFailExport/" + appId + "?"
                + "language=" + language + "&startTime=" + startTime + "&endTime=" + endTime + conditions.toString()+ "&groupBy=" + groupBy,
            GATEWAY_URL);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("client_id", YOUR_CLIENT_ID);

        BufferedReader reader = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = reader.readLine();
                JSONObject object = JSON.parseObject(result);
                if (object.containsKey("fileURL")) {
                    // get report success, download it from object.getString("fileURL")
                    String fileURL = object.getString("fileURL");
                } else {
                    // get report failed, get return code and see the detail info below :
                    // English version : https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-References/agcapi-returncode_v2
                    // Chinese version : https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-References/agcapi-returncode_v2
                    String returnCode = object.getJSONObject("ret").getString("code");
                }
            } else {
                // record error log or throw exception
            }
        } catch (Exception e) {
            // record error log or throw exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    // record error log or throw exception
                }
            }
        }
    }
}