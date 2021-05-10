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
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageCreateRequest;
import com.huawei.agconnect.server.edukit.pkg.model.PackageData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditLocalizedData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditMetaData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditProductLocalizedData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProduct;
import com.huawei.agconnect.server.edukit.pkg.model.ProductPrice;
import com.huawei.agconnect.server.edukit.pkg.resp.PkgCommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建会员包
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class CreatePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePackage.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

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
                    .setCredential(CredentialParser
                        .toCredential(CreatePackage.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        PackageCreateRequest packageCreateRequest =
            AGCEdukit.getInstance(clientName).getPackageCreateRequest(buildPackageData());
        PkgCommonResponse pkgCommonResponse = packageCreateRequest.createPkg().join();
        LOGGER.info("create pkg response:{}", pkgCommonResponse);
        if (CommonErrorCode.SUCCESS != pkgCommonResponse.getResult().getResultCode()) {
            LOGGER.error("create pkg failed.");
        } else {
            LOGGER.info("create pkg success.");
        }
    }

    private static PackageData buildPackageData() {
        PkgEditMetaData editMetaData = PkgEditMetaData.builder()
            .defaultLangSet("zh-CN")
            .sourceNameSet("没了")
            .nameSet("SDK会员包")
            .distNotifyUrlSet("www.baidu.com")
            // .residentLeagueAppSet("101478527")
            .eduappPurchasedSet(true)
            .build();

        ImageFileInfo cover = ImageFileInfo.builder()
            // 会员包封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "pkgcover.png")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_ALBUM_HORIZONTAL_COVER)
            .build();
        ImageFileInfo introduce = ImageFileInfo.builder()
            // 会员包封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "pkgintroduce.jpg")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_INTRODUCTION)
            .build();
        List<ImageFileInfo> introduces = new ArrayList<>();
        introduces.add(introduce);
        PkgEditLocalizedData editLocalizedData = PkgEditLocalizedData.builder()
            .coverImgFileInfoSet(cover)
            .introduceImageFileInfosSet(introduces)
            .nameSet("SDK会员包")
            .languageSet("zh-CN")
            .fullDescriptionSet("sdk描述")
            .build();

        List<PkgEditLocalizedData> pkgEditLocalizedDatas = new ArrayList<>();
        pkgEditLocalizedDatas.add(editLocalizedData);

        PkgEditProductLocalizedData productLocalizedData =
            PkgEditProductLocalizedData.builder().languageSet("zh-CN").nameSet("sdk会员包版本多语言数据").build();
        List<PkgEditProductLocalizedData> pkgEditProductLocalizedDataList = new ArrayList<>();
        pkgEditProductLocalizedDataList.add(productLocalizedData);
        ProductPrice productPrice = ProductPrice.builder()
            .countryCodeSet("CN")
            .priceSet(22d)
            .priceTypeSet(CommonConstants.PriceType.CURRENT)
            .build();
        List<ProductPrice> productPrices = new ArrayList<>();
        productPrices.add(productPrice);
        PkgProduct pkgProduct = PkgProduct.builder()
            .deeplinkUrlSet("wwwbaidu.com")
            .defaultLangSet("zh-CN")
            .nameSet("sdk会员包商品")
            .statusSet(CommonConstants.ProductStatus.ON_SHELFING)
            .localizedDataSet(pkgEditProductLocalizedDataList)
            .validityUnitSet(CommonConstants.ValidityUnit.WEEK)
            .validityNumSet(CommonConstants.ValidityUnit.DAY)
            .priceListSet(productPrices)
            .needDeliverySet(false)
            .devProductIdSet("32573")
            .build();
        List<PkgProduct> productList = new ArrayList<>();
        productList.add(pkgProduct);
        PackageData packageData = PackageData.builder()
            .actionSet(CommonConstants.PkgAction.SUBMIT_FOR_REVIEW)
            .associateAlbumIdSet(599175165595016192L)
            .metaDataSet(editMetaData)
            .localizedDataSet(pkgEditLocalizedDatas)
            .pkgProductsSet(productList)
            .build();
        return packageData;
    }
}
