
package test.model;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class result
{
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public UploadFileRsp getUploadFileRsp() {
        return uploadFileRsp;
    }

    public void setUploadFileRsp(UploadFileRsp uploadFileRsp) {
        this.uploadFileRsp = uploadFileRsp;
    }

    private String resultCode;

    private UploadFileRsp uploadFileRsp;
}
