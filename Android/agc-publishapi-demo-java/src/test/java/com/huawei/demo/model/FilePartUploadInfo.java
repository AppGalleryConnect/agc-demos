/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.model;

import java.util.Map;

/**
 *
 * @author xxxxxxx
 * @since 2021-01-14
 */
public class FilePartUploadInfo {
    private String url;
    private String method;
    private String partObjectId;
    private Map<String,String> headers;

    public FilePartUploadInfo() {
    }

    public FilePartUploadInfo(String url, String method, String partObjectId, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.partObjectId = partObjectId;
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPartObjectId() {
        return partObjectId;
    }

    public void setPartObjectId(String partObjectId) {
        this.partObjectId = partObjectId;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}