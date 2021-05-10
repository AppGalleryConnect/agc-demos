/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.agconnect.album;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.album.impl.AlbumManageRequest;
import com.huawei.agconnect.server.edukit.album.model.ManageAlbumStatus;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理专辑
 *
 * @author lWX832783
 * @since 2021-04-02
 */
public class ManageAlubm {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManageAlubm.class);

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
                        .toCredential(ManageAlubm.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理

            return;
        }

        Long albumId = 599016251687623680L;
        ManageAlbumStatus status = ManageAlbumStatus.builder()
            .actionSet(CommonConstants.AlbumManageAction.SUBMIT_FOR_DISABLING)
            .removalReasonSet("上架专辑")
            .build();

        AlbumManageRequest albumManageRequest =
            AGCEdukit.getInstance(clientName).getAlbumManageRequest(albumId, status);
        CommonResponse commonResponse = albumManageRequest.manageAlbum();
        LOGGER.info("Manage album result:{}", commonResponse.getResult());
        if (CommonErrorCode.SUCCESS != commonResponse.getResult().getResultCode()) {
            LOGGER.error("manage album failed.");
        } else {
            LOGGER.info("manage album success.");
        }
    }
}
