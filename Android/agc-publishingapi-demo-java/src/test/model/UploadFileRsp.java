
package test.model;

import java.util.List;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class UploadFileRsp {
    public String getIfSuccess() {
        return ifSuccess;
    }

    public void setIfSuccess(String ifSuccess) {
        this.ifSuccess = ifSuccess;
    }

    String ifSuccess;

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    List<FileInfo> fileInfoList;
}
