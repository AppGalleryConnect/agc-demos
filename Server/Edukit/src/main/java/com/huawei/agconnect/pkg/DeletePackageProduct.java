/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageProductDeleteRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除会员包商品
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class DeletePackageProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePackageProduct.class);

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
                    .setCredential(CredentialParser.toCredential(
                        DeletePackageProduct.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        String pkgId = "pkg_599168332478209024";
        Long pkgEditId = 599168332478209025L;
        String productId = "32553";
        PackageProductDeleteRequest packageProductDeleteRequest =
            AGCEdukit.getInstance(clientName).getPackageProductDeleteRequest(pkgId, pkgEditId, productId);
        CommonResponse commonResponse = packageProductDeleteRequest.deletePkgProduct();
        LOGGER.info("Delete package product response:{}", commonResponse);
        if (CommonErrorCode.SUCCESS != commonResponse.getResult().getResultCode()) {
            LOGGER.error("delete pkg product failed.");
        } else {
            LOGGER.info("delete pkg  product success.");
        }
    }
}
