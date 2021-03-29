
package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageDeleteRequest;
import com.huawei.agconnect.server.edukit.pkg.model.PackageData;
import com.huawei.agconnect.server.edukit.pkg.model.PackageEdit;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgEditLocalizedData;
import com.huawei.agconnect.server.edukit.pkg.model.PkgProduct;
import com.huawei.agconnect.server.edukit.pkg.resp.PackageDeleteResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除会员包信息
 * 如果构造了会员包商品，则只删除会员包商品。
 * 如果构造了会员包本地化多语言数据，则只删除会员包本地化多语言数据。
 * 如果构造了会员包商品和会员包本地化多语言数据，则删除会员包商品和会员包本地化多语言数据。
 *
 * @since 2020-07-23
 */
public class DeletePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePackage.class);

    public static void main(String[] args) {
        // 请求客户端名称，自定义
        String clientName = "edukit";
        // 会员包id，由创建会员包操作类获取
        String pkgId = "pkg_456964613699862528";
        String editId = "";
        // 教育中心返回的商品id，创建会员包时返回，可由PackageCreateResponse::getProductListDetail获取
        String sysProductId = "454032006825705472";
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(DeletePackage.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        PackageDeleteResponse response = deletePkg(clientName, pkgId, editId, sysProductId);

        LOGGER.info("Delete package response:{}", response);
    }

    private static PackageDeleteResponse deletePkg(String clientName, String pkgId, String editId,
        String sysProductId) {
        List<PkgProduct> pkgProductList = new ArrayList<>();
        // 需要删除会员包商品时，仅需指定sysProductId。
        PkgProduct p1 = PkgProduct.builder().sysProductIdSet(sysProductId).build();
        pkgProductList.add(p1);
        // 需要删除本地化多语言数据时，仅需携带packageEditId和language。
        PkgEditLocalizedData l1 = PkgEditLocalizedData.builder().languageSet(CommonConstants.DEFAULT_LANGUAGE).build();
        List<PkgEditLocalizedData> list = new ArrayList<>();
        list.add(l1);

        PackageData packageData = PackageData.builder()
            .pkgIdSet(pkgId)
            .pkgEditDataSet(PkgEditData.builder().localizedDataSet(list).build())
            .productListSet(pkgProductList)
            .build();

        PackageEdit packageEdit = PackageEdit.builder().packageEditIdSet(editId).packageDataSet(packageData).build();

        PackageDeleteRequest request = AGCEdukit.getInstance(clientName).getPackageDeleteRequest(packageEdit);
        PackageDeleteResponse packageDeleteResponse = request.delete();

        /**
         * 删除会员包本地化多语言数据后，需要提交审核后才能生效
         * CompletableFuture<PackageUpdateResponse> cf =
         * AGCEdukit.getInstance(clientName).getPackageUpdateRequest(PackageEdit.builder()
         * .packageEditIdSet(packageDeleteResponse.getPkgEditId())
         * .packageDataSet(PackageData.builder().pkgIdSet(pkgId).build())
         * .build()).commit(CommonConstants.CommitAction.COMMIT_REVIEW);
         * LOGGER.info("commit package response:{}", cf.join());
         */
        return packageDeleteResponse;
    }
}
