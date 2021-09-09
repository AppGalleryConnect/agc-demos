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
