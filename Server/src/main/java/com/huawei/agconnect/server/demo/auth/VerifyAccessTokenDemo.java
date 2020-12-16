/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.auth.entity.AuthAccessToken;
import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * verify access token demo
 *
 * @since 2020-10-19
 */
public class VerifyAccessTokenDemo extends AbstractDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyAccessTokenDemo.class);

    /**
     * token need verify
     */
    private static final String ACCESS_TOKEN = "access_token";

    public static void main(String[] args) {

        init();

        AuthAccessToken authAccessToken = null;

        try {
            authAccessToken = AGCAuth.getInstance(AUTH_CLIENT_NAME).verifyAccessToken(ACCESS_TOKEN, true);
        } catch (AGCAuthException e) {
            // record error log or throw exception
        }

        if (authAccessToken != null) {
            LOGGER.info("verify token success");
            // get user info from AuthAccessToken object, include name/picture/phone/email and so on
        } else {
            LOGGER.error("verify token failed");
        }
    }
}
