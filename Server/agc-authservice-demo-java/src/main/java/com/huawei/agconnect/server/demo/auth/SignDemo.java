/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
