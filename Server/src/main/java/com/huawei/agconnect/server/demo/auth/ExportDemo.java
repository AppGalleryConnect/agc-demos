/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.auth.entity.UserImportExportResult;
import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * export
 *
 * @since 2020-08-18
 */
public class ExportDemo extends AbstractDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportDemo.class);

    public static void main(String[] args) {
        init();

        UserImportExportResult importExportResult = null;
        try {
            importExportResult = AGCAuth.getInstance(AUTH_CLIENT_NAME)
                .exportUserData(ExportDemo.class.getClassLoader().getResource("export_user.json").getPath());
        } catch (AGCAuthException e) {
            // record error log or throw exception
            return;
        }
        LOGGER.info("export success user:{}, failed user:{}, failed user list:{}", importExportResult.getSuccessCount(),
            importExportResult.getErrorUsers(), importExportResult.getErrorUsersList());
    }
}
