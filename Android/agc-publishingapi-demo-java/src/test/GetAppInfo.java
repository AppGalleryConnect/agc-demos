
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class GetAppInfo {
    public static void getAppInfo(String domain, String clientId, String token, String appId, String lang) {
        HttpGet get = new HttpGet(domain + "/publish/v2/app-info?appid=" + appId + "&lang=" + lang);

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

                // Object returned by the app information query API, which can be received using the AppInfo object. For details, please refer to the API reference.
                JSONObject object = JSON.parseObject(result);

                System.out.println(object.get("ret"));
            }
        } catch (Exception e) {

        }

    }
}
