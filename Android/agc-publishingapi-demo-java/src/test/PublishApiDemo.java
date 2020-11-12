
package test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import test.model.FileInfo;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class PublishApiDemo {

    /**
     * Token domain name.
     */
    private static String domain = "https://connect-api.cloud.huawei.com/api/";

    /**
     * clientId
     */
    private static String clientId = "188934035483957248";

    /**
     * clientSecret
     */
    private static String clientSecret = "B15B497B44E080EBE2C4DE4E7493022352409516B2A1A5C8F0FCD2C579A8EB14";

    /**
     * App ID.
     */
    private static String appId = "300089319";

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        // Obtain the token.
        String token = GetToken.getToken(domain, clientId, clientSecret);

        // Query app information.
        GetAppInfo.getAppInfo(domain, clientId, token, appId, "zh-CN");

        // Update app information.
        UpdateAppInfo.updateAppInfo(domain, clientId, token, appId);

        // Upload the file.
        List<FileInfo> files = UploadFile.uploadFile(domain, clientId, token, appId, "png");

        // After file upload, call the API for updating app file information.
        UploadAppFileInfo.updateAppFileInfo(domain, clientId, token, appId, files);

        // Submit for review.
        SubmitAudit.submit(domain, clientId, token, appId);
    }

}
