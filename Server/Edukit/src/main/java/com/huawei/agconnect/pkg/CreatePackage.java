
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
import com.huawei.agconnect.server.edukit.pkg.impl.PackageCreateRequest;
import com.huawei.agconnect.server.edukit.pkg.model.PackageData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditLocalizedData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditMetaData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgMetaData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProduct;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProductMetaData;
import com.huawei.agconnect.server.edukit.pkg.resp.PackageCreateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 创建会员包
 *
 * @since 2020-07-23
 */
public class CreatePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePackage.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

    public static void main(String[] args) {
        // 请求客户端名称，自定义
        String clientName = "edukit";
        /**
         * 课程ID
         * 可由CourseCreateRequest::saveDraft或CourseCreateRequest::commit返回。
         */
        String courseId = "446167336685207552";
        String appId = "101478527";
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

        CompletableFuture<PackageCreateResponse> future = createPkg(clientName, courseId, appId);
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(packageCreateResponse -> {
            LOGGER.info("Create package response : {}", packageCreateResponse);
            if (packageCreateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 获取会员包ID，保存会员包Id与您的会员包的关联关系
                // 保存会员包版本ID，如果审核没有通过，可以使用该会员包版本ID继续更新
            } else {
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Create package finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Create package failed.");
        }
    }

    private static CompletableFuture<PackageCreateResponse> createPkg(String clientName, String courseId,
        String appId) {
        List<ImageFileInfo> imageFileInfo = new ArrayList<>();
        ImageFileInfo image = ImageFileInfo.builder().pathSet(path + "introduce.PNG").resourceTypeSet(3).build();
        imageFileInfo.add(image);
        PkgEditLocalizedData pkgEditLocalizedData = PkgEditLocalizedData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .nameSet("大学语文会员收费")
            .fullDescriptionSet("针对大学生的语文课程")
            .introduceImageFileInfosSet(imageFileInfo)
            .build();
        List<PkgEditLocalizedData> list = new ArrayList<>();
        list.add(pkgEditLocalizedData);

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
        List<ProductPrice> priceList = new ArrayList<>();
        priceList.add(productPrice1);
        priceList.add(productPrice2);

        List<PkgProduct> pkgProductList = new ArrayList<>();
        PkgProduct p1 = PkgProduct.builder()
            .pkgProductMetaDataSet(PkgProductMetaData.builder()
                .devProductIdSet("111")
                .nameSet("大学语文课程一个月会员")
                .deeplinkUrlSet("https://clouddragon.huawei.com")
                .validityUnitSet(CommonConstants.ValidityUnit.MONTH)
                .validityNumSet(1)
                .build())
            .priceListSet(priceList)
            .build();
        PkgProduct p2 = PkgProduct.builder()
            .pkgProductMetaDataSet(PkgProductMetaData.builder()
                .devProductIdSet("222")
                .nameSet("大学语文课程一年会员")
                .deeplinkUrlSet("https://clouddragon.huawei.com")
                .validityUnitSet(CommonConstants.ValidityUnit.YEAR)
                .validityNumSet(1)
                .build())
            .priceListSet(priceList)
            .build();
        pkgProductList.add(p1);
        pkgProductList.add(p2);

        List<String> courseList = new ArrayList<>();
        courseList.add(courseId);

        PackageData packageData = PackageData.builder()
            .pkgMetaDataSet(PkgMetaData.builder().freeFlagSet(true).build())
            .pkgEditDataSet(PkgEditData.builder()
                .metaDataSet(PkgEditMetaData.builder()
                    .nameSet("大学语文会员")
                    .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
                    .remarksSet("大学语文会员")
                    .build())
                .localizedDataSet(list)
                .build())
            .productListSet(pkgProductList)
            .addCourseIdsSet(courseList)
            .build();

        PackageCreateRequest request = AGCEdukit.getInstance(clientName).getPackageCreateRequest(packageData);
        return request.saveDraft();
    }
}
