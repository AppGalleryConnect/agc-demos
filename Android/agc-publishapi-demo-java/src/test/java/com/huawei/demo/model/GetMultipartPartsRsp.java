/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo.model;

import java.util.Map;

/**
 * @author xxxxxxx
 * @since 2021-01-14
 */
public class GetMultipartPartsRsp {
    private String ret;

    private Map<String, FilePartUploadInfo> uploadInfoMap;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Map<String, FilePartUploadInfo> getUploadInfoMap() {
        return uploadInfoMap;
    }

    public void setUploadInfoMap(Map<String, FilePartUploadInfo> uploadInfoMap) {
        this.uploadInfoMap = uploadInfoMap;
    }

    public GetMultipartPartsRsp() {
    }

    public GetMultipartPartsRsp(String ret, Map<String, FilePartUploadInfo> uploadInfoMap) {
        this.ret = ret;
        this.uploadInfoMap = uploadInfoMap;
    }
}