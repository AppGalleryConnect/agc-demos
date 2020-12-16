/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.jwt.AGCAuthJwtToken;
import com.huawei.agconnect.server.auth.service.AGCAuth;
import com.huawei.agconnect.server.auth.util.RSAKeyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sign demo
 *
 * @since 2020-08-18
 */
public class SignDemo extends AbstractDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignDemo.class);

    /**
     * self account user id
     */
    private static final String UID = "uidDemo";

    /**
     * self account photoUrl
     */
    private static final String PHOTO_URL = "photoUrl";

    /**
     * self account displayName
     */
    private static final String DISPLAY_NAME = "demoName";

    public static void main(String[] args) {
        init();

        /**
         * get privateKey and publicKey
         */
        RSAKeyPair rsaKeyPair = null;
        try {
            rsaKeyPair = AGCAuth.getInstance(AUTH_CLIENT_NAME).generateKey();
        } catch (AGCAuthException e) {
            // record error log or throw exception
            return;
        }

        AGCAuthJwtToken jwtToken = null;

        try {
            jwtToken =
                AGCAuth.getInstance(AUTH_CLIENT_NAME).sign(UID, DISPLAY_NAME, PHOTO_URL, rsaKeyPair.getPrivateKey());
        } catch (AGCAuthException e) {
            // record error log or throw exception
            return;
        }

        LOGGER.info("sign jwt success");
    }
}
