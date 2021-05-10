/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.agconnect.album;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.album.impl.AlbumDeleteRequest;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除专辑
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class DeleteAlbum {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAlbum.class);

    public static void main(String[] args) {
        /**
         * 用户名，自定义
         */
        String clientName = "edukit";
        /**
         * 初始化
         */
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(DeleteAlbum.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        Long albumId = 222L;
        AlbumDeleteRequest albumDeleteRequest = AGCEdukit.getInstance(clientName).getAlbumDeleteRequest(albumId);
        CommonResponse commonResponse = albumDeleteRequest.deleteAlbum();
        LOGGER.info("delete album response:{}", commonResponse);
        if (CommonErrorCode.SUCCESS != commonResponse.getResult().getResultCode()) {
            LOGGER.error("delete album failed.");
        } else {
            LOGGER.info("delete album success.");
        }
    }
}
