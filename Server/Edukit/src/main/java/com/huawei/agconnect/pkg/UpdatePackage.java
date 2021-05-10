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
import com.huawei.agconnect.server.edukit.pkg.impl.PackageUpdateRequest;
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
 * 更新会员包
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class UpdatePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePackage.class);

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
                        .toCredential(UpdatePackage.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        PackageUpdateRequest packageUpdateRequest =
            AGCEdukit.getInstance(clientName).getPackageUpdateRequest(buildPackageData());
        PkgCommonResponse commonResponse = packageUpdateRequest.updatePkg().join();
        LOGGER.info("Update package response:{}", commonResponse);
        if (CommonErrorCode.SUCCESS != commonResponse.getResult().getResultCode()) {
            LOGGER.error("update pkg failed.");
        } else {
            LOGGER.info("update pkg success.");
        }
    }

    private static PackageData buildPackageData() {
        PkgEditMetaData editMetaData = PkgEditMetaData.builder()
            .defaultLangSet("zh-CN")
            .sourceNameSet("没了")
            .nameSet("SDK会员包")
            .distNotifyUrlSet("https://www.baidu.com")
            // .residentLeagueAppSet("101478527")
            .eduappPurchasedSet(true)
            .build();

        ImageFileInfo cover = ImageFileInfo.builder()
            // 专辑封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "pkgcover.png")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_ALBUM_HORIZONTAL_COVER)
            .build();
        ImageFileInfo introduce = ImageFileInfo.builder()
            // 课程封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
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
        ProductPrice productPrice = ProductPrice.builder().countryCodeSet("CN").priceSet(22d).priceTypeSet(2).build();
        List<ProductPrice> productPrices = new ArrayList<>();
        productPrices.add(productPrice);
        PkgProduct pkgProduct = PkgProduct.builder()
            .deeplinkUrlSet("wwwbaidu.com")
            .defaultLangSet("zh-CN")
            .nameSet("sdk会员包商品")
            .statusSet(1)
            .localizedDataSet(pkgEditProductLocalizedDataList)
            .validityUnitSet(3)
            .validityNumSet(2)
            .priceListSet(productPrices)
            .needDeliverySet(false)
            .devProductIdSet("32538")
            .build();
        List<PkgProduct> productList = new ArrayList<>();
        productList.add(pkgProduct);
        PackageData packageData = PackageData.builder()
            .pkgIdSet("pkg_599104955454249984")
            .actionSet(1)
            .associateAlbumIdSet(599016251687623680L)
            .metaDataSet(editMetaData)
            .localizedDataSet(pkgEditLocalizedDatas)
            .pkgProductsSet(productList)
            .build();
        return packageData;
    }
}
