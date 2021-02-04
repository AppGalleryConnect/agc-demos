/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.model;

/**
 * @author xxxxxxx
 * @since 2021-01-13
 */
public class PackageFileInfo {
    private String downloadUrl;

    private String downloadFileName;

    private String sensitivePermissionDesc;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getSensitivePermissionDesc() {
        return sensitivePermissionDesc;
    }

    public void setSensitivePermissionDesc(String sensitivePermissionDesc) {
        this.sensitivePermissionDesc = sensitivePermissionDesc;
    }

    public PackageFileInfo(String downloadUrl, String downloadFileName, String sensitivePermissionDesc) {
        this.downloadUrl = downloadUrl;
        this.downloadFileName = downloadFileName;
        this.sensitivePermissionDesc = sensitivePermissionDesc;
    }

    public PackageFileInfo() {
    }
}