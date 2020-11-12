package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
public class SubmitAudit
{
    public static void  submit(String domain,String clientId, String token, String appId){
        HttpPost put = new HttpPost(domain + "/publish/v2/app-submit?appid=" + appId);

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
                System.out.println(object.get("ret"));
            }
        } catch (Exception e) {

        }
    }
}
