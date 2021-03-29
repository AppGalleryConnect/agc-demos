
package com.huawei.agconnect.pkg;

import com.huawei.agconnect.course.GetCourseStatus;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.pkg.model.ManagePkgProductStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会员包商品状态管理能力开放Demo
 *
 * @author lWX832783
 * @since 2020-11-13
 */
public class ManagePkgProductStat {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagePkgProductStat.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 会员包id，由创建会员包操作类获取
         */
        String pkgId = "pkg_504295070036715520";
        /**
         * 会员包系统商品ID
         */
        String sysProductId = "504295070154156032";
        /**
         * 会员包商品操作，只允许将已上架成功的会员包商品停售，0-停售会员包商品，1-激活已停售会员包商品
         */
        ManagePkgProductStatus status =
            ManagePkgProductStatus.builder().actionSet(CommonConstants.PkgProductStatus.DISCONTINUE).build();

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(GetCourseStatus.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        CommonResponse resp = AGCEdukit.getInstance(clientName).managePkgProductStatus(pkgId, sysProductId, status);
        LOGGER.info("ManagePkgProductStat resp:{}", resp);
    }
}
