
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

public class ReportApiDemo {
    public static void main(String[] args) {
        String token = getToken();
        List<String> filterCondition=new ArrayList<>(16);
        List<String> filterConditionValue=new ArrayList<>(16);
        filterCondition.add("countryId");
        filterConditionValue.add("CN");
        getReport(token, "101236389", "zh-CN","20190501","20190630",filterCondition,filterConditionValue);
    }
  /* Obtain the token.
  * Note:
  * The domain name is connect-api.cloud.huawei.com for China and connect-api-dre.cloud for Europe.
  * The domain name is connect-api-dra.cloud for Asia Pacific and connect-api-drru.cloud for Russia.
  * */
    public static String getToken() {
        String token = null;
        try {
            HttpPost post = new HttpPost("http://10.31.26.167:18062/api/oauth2/v1/token");
            JSONObject keyString = new JSONObject();
            keyString.put("client_id", "205401629556519936");
            keyString.put("client_secret", "C287F53841C1A29BD049E7E4A35CD35804C1A8A8E93C432671A553DCC2DC9352");
            keyString.put("grant_type", "clieant_credentials");
            StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                token = object.getString("access_token");
            }
            post.releaseConnection();
            httpClient.close();
        } catch (Exception e) {
            System.out.println("Failed to get token");
        }
        return token;
    }

    /*
     * Obtain the report download URL.
     * */

    public static void getReport(String token, String appId, String language,String startTime,String endTime,List<String> filterCondition,List<String> filterConditionValue) {
       /*
       * Note:
       * The domain name is connect-api.cloud.huawei.com for China and connect-api-dre.cloud for Europe.
       * The domain name is connect-api-dra.cloud for Asia Pacific and connect-api-drru.cloud for Russia.
       * */
       StringBuilder fc=new StringBuilder();
        for (int i = 0; i < filterCondition.size(); i++) {
            fc.append("&filterCondition=");
            fc.append(filterCondition.get(i));
            fc.append("&filterConditionValue=");
            fc.append(filterConditionValue.get(i));
        }
        String sc = fc.toString();

        HttpGet get = new HttpGet(
                "http://10.31.26.167:18062/api/report/distribution-operation-quality/v1/orderDetailExport/"+appId+"?"+"language="+language
                        +"&startTime="+startTime+"&endTime="+endTime+sc);
        get.setHeader("Authorization", "Bearer " + token);
        get.setHeader("client_id", "205401629556519936");
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
            }
        } catch (Exception e) {
            System.out.println("Failed to get Report");
        }
    }

}
