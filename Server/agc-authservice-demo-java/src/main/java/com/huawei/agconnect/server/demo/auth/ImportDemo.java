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

import com.huawei.agconnect.server.auth.entity.UserImportExportResult;
import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * import
 *
 * @since 2020-08-18
 */
public class ImportDemo extends AbstractDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportDemo.class);

    public static void main(String[] args) {
        init();

        UserImportExportResult importExportResult = null;
        try {
            importExportResult = AGCAuth.getInstance(AUTH_CLIENT_NAME)
                .importUserData(ImportDemo.class.getClassLoader().getResource("import_user.json").getPath());
        } catch (AGCAuthException e) {
            // record error log or throw exception
            return;
        }

        LOGGER.info("import success user:{}, failed user:{}, failed user list:{}", importExportResult.getSuccessCount(),
            importExportResult.getErrorUsers(), importExportResult.getErrorUsersList());
    }
}
