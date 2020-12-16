/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * revoke demo
 *
 * @since 2020-10-19
 */
public class RevokeDemo extends AbstractDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevokeDemo.class);

    /**
     * revoke token user id
     */
    private static final String UID = "uidDemo";

    public static void main(String[] args) {

        init();

        try {
            AGCAuth.getInstance(AUTH_CLIENT_NAME).revokeRefreshTokens(UID);
        } catch (AGCAuthException e) {
            // record error log or throw exception
        }
    }
}
