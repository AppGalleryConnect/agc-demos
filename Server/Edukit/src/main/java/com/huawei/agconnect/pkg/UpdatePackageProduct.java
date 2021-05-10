/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageProductUpdateRequest;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProductBriefInfo;
import com.huawei.agconnect.server.edukit.pkg.model.ProductPrice;
import com.huawei.agconnect.server.edukit.pkg.model.UpdatePkgProductListRequest;
import com.huawei.agconnect.server.edukit.pkg.resp.UpdatePkgProductListResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新会员包商品
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class UpdatePackageProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePackageProduct.class);

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
                        UpdatePackageProduct.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        String pkgId = "pkg_599175484991265792";
        PackageProductUpdateRequest packageProductUpdateRequest =
            AGCEdukit.getInstance(clientName).getPackageProductUpdateRequest(pkgId, buildUpdatePkgProductListRequest());
        UpdatePkgProductListResponse updatePkgProductListResponse = packageProductUpdateRequest.updatePkgProductList();
        LOGGER.info("Update package productList response:{}", updatePkgProductListResponse);
        if (CommonErrorCode.SUCCESS != updatePkgProductListResponse.getResult().getResultCode()) {
            LOGGER.error("update pkg product failed.");
        } else {
            LOGGER.info("update pkg product success.");
        }
    }

    private static UpdatePkgProductListRequest buildUpdatePkgProductListRequest() {
        ProductPrice productPrice = ProductPrice.builder()
            .priceTypeSet(CommonConstants.PriceType.CURRENT)
            .priceSet(333D)
            .countryCodeSet("CN")
            .build();
        List<ProductPrice> productPrices = new ArrayList<>();
        productPrices.add(productPrice);
        PkgProductBriefInfo productBriefInfo =
            PkgProductBriefInfo.builder().devProductIdSet("32573").priceListSet(productPrices).build();
        List<PkgProductBriefInfo> productBriefInfos = new ArrayList<>();
        productBriefInfos.add(productBriefInfo);
        UpdatePkgProductListRequest productListRequest =
            UpdatePkgProductListRequest.builder().productsSet(productBriefInfos).build();
        return productListRequest;
    }
}
