package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class UpdateAppInfo
{
    public static void updateAppInfo(String domain, String clientId, String token, String appId) {
        HttpPut put = new HttpPut(domain + "/publish/v2/app-info?appId=" + appId);

        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);

        JSONObject keyString = new JSONObject();

        // Configure the app to be free of charge.
        keyString.put("isFree", 0);

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
                System.out.println(object.get("ret"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
