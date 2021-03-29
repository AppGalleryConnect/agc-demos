
package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageUpdateRequest;
import com.huawei.agconnect.server.edukit.pkg.model.CreatePkgEditResponse;
import com.huawei.agconnect.server.edukit.pkg.model.PackageData;
import com.huawei.agconnect.server.edukit.pkg.model.PackageEdit;
import com.huawei.agconnect.server.edukit.pkg.resp.PackageUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建会员包新版本
 *
 * @since 2020-07-23
 */
public class CreatePackageNewEdit {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePackageNewEdit.class);

    public static void main(String[] args) {
        // 请求客户端名称，自定义
        String clientName = "edukit";
        // 会员包id，由创建会员包操作类获取
        String pkgId = "pkg_405654749510172672";
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
        CreatePkgEditResponse response = createNewEdit(clientName, pkgId);
        LOGGER.info("createNewEdit response:{}", response);
    }

    private static CreatePkgEditResponse createNewEdit(String clientName, String pkgId) {
        PackageData packageData = PackageData.builder().pkgIdSet(pkgId).build();
        PackageEdit packageEdit = PackageEdit.builder().packageDataSet(packageData).build();

        PackageUpdateRequest request = AGCEdukit.getInstance(clientName).getPackageUpdateRequest(packageEdit);
        return request.createNewEdit();
    }
}
