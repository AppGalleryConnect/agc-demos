/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;

/**
 * abstract class include init
 *
 * @since 2020-08-18
 */
public abstract class AbstractDemo {

    /**
     * auth client name
     */
    public static final String AUTH_CLIENT_NAME = "auth";

    /**
     * agc client init
     */
    public static void init() {

        /**
         * init
         */
        try {
            AGCClient.initialize(AUTH_CLIENT_NAME,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(ImportDemo.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (

        AGCException e) {
            // record error log or throw exception
            return;
        }
    }

}
