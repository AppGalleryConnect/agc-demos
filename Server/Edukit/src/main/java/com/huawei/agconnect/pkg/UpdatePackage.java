
package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;
import com.huawei.agconnect.server.edukit.common.model.ProductPrice;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageUpdateRequest;
import com.huawei.agconnect.server.edukit.pkg.model.PackageData;
import com.huawei.agconnect.server.edukit.pkg.model.PackageEdit;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditLocalizedData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditMetaData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgMetaData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgMetaDataNotFree;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProduct;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProductMetaData;
import com.huawei.agconnect.server.edukit.pkg.resp.PackageUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 更新会员包
 * 注意：
 * 会员包已上架时，不需要指定editId，SDK会自动创建新版本，并在响应里返回。
 * 会员包未上架时，需要指定editId为创建会员包时返回的值，否则未上架部分的修改将会丢失
 *
 * @since 2020-07-23
 */
public class UpdatePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePackage.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

    public static void main(String[] args) {
        // 请求客户端名称，自定义
        String clientName = "edukit";
        // 会员包id，可由PackageCreateRequest::commit或PackageCreateRequest::saveDraft获取
        String pkgId = "pkg_477927346708736000";
        String editId = "477927346708736001";
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
        CompletableFuture<PackageUpdateResponse> future = updatePkg(clientName, pkgId, editId);
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(packageUpdateResponse -> {
            LOGGER.info("Update package response : {}", packageUpdateResponse);
            if (packageUpdateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 保存会员包版本ID，如果审核没有通过，可以使用该会员包版本ID继续更新
            } else {
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Update package finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Update package failed.");
        }
    }

    private static CompletableFuture<PackageUpdateResponse> updatePkg(String clientName, String pkgId, String editId) {
        ProductPrice productPrice1 = ProductPrice.builder()
            .countryCodeSet("CN")
            .priceSet(20.0)
            .priceTypeSet(CommonConstants.PriceType.CURRENT)
            .build();
        ProductPrice productPrice2 = ProductPrice.builder()
            .countryCodeSet("CN")
            .priceSet(30.0)
            .priceTypeSet(CommonConstants.PriceType.ORIGINAL)
            .build();
        ProductPrice productPrice3 = ProductPrice.builder()
            .countryCodeSet("US")
            .priceSet(50.0)
            .priceTypeSet(CommonConstants.PriceType.CURRENT)
            .build();
        ProductPrice productPrice4 = ProductPrice.builder()
            .countryCodeSet("US")
            .priceSet(60.0)
            .priceTypeSet(CommonConstants.PriceType.ORIGINAL)
            .build();
        List<ProductPrice> priceList = new ArrayList<>();
        priceList.add(productPrice1);
        priceList.add(productPrice2);
        priceList.add(productPrice3);
        priceList.add(productPrice4);

        List<PkgProduct> pkgProductList = new ArrayList<>();
        PkgProduct p1 = PkgProduct.builder()
            .sysProductIdSet("477927346809399296")
            .pkgProductMetaDataSet(PkgProductMetaData.builder()
                .devProductIdSet("1247")
                .nameSet("更新会员包商品1")
                .deeplinkUrlSet("https//clouddragonhuaweicom1")
                .validityUnitSet(5)
                .validityNumSet(1)
                .build())
            .priceListSet(priceList)
            .build();
        PkgProduct p2 = PkgProduct.builder()
            .pkgProductMetaDataSet(PkgProductMetaData.builder()
                .devProductIdSet("SXv1")
                .nameSet("更新会员包商品2")
                .deeplinkUrlSet("https//clouddragonhuaweicom")
                .validityUnitSet(5)
                .validityNumSet(2)
                .build())
            .priceListSet(priceList)
            .build();
        pkgProductList.add(p1);
        // pkgProductList.add(p2);
        List<ImageFileInfo> imageFileInfo1 = new ArrayList<>();
        ImageFileInfo image = ImageFileInfo.builder()
            .pathSet(path + "introduce.png")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_INTRODUCTION)
            .build();
        imageFileInfo1.add(image);
        PkgEditLocalizedData pkgEditLocalizedData = PkgEditLocalizedData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .nameSet("大学语文会员收费")
            .fullDescriptionSet("针对大学生的语文课程")
            .introduceImageFileInfosSet(imageFileInfo1)
            .build();

        List<PkgEditLocalizedData> list = new ArrayList<>();
        list.add(pkgEditLocalizedData);

        PackageData packageData = PackageData.builder()
            .pkgIdSet(pkgId)
            .pkgMetaDataSet(PkgMetaData.builder()
                .freeFlagSet(false)
                .metaDataNotFreeSet(PkgMetaDataNotFree.builder()
                    .eduappPurchasedSet(true)
                    .residentAGAppSet("101478527")
                    .distNotifyUrlSet("https://callback.example.com/notify")
                    .build())
                .build())
            .pkgEditDataSet(PkgEditData.builder()
                .metaDataSet(PkgEditMetaData.builder()
                    .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
                    .nameSet("开发SDK创建大学语文会员11")
                    .build())
                .localizedDataSet(list)
                .build())
            .productListSet(pkgProductList)
            .build();

        PackageEdit packageEdit = PackageEdit.builder().packageDataSet(packageData).packageEditIdSet(editId).build();

        PackageUpdateRequest request = AGCEdukit.getInstance(clientName).getPackageUpdateRequest(packageEdit);
        return request.saveDraft();

    }
}
