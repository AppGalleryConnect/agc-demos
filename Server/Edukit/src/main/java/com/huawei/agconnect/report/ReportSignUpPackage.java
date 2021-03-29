
package com.huawei.agconnect.report;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.signup.model.PackageSignupInfo;
import com.huawei.agconnect.server.edukit.signup.model.SignupInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通知会员包订购状态变更
 */
public class ReportSignUpPackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportSignUpPackage.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 用户华为帐号ID
         */
        String userId = "";
        /**
         * userId字段对应的帐号类型
         */
        Integer userIdType = 2;

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        ReportSignUpPackage.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        PackageSignupInfo packageSignupInfo = buildRequest();
        CommonResponse response =
            AGCEdukit.getInstance(clientName).reportSignupPackage(userId, userIdType, packageSignupInfo);
        LOGGER.info("Report package sign up response:{}", response);
        if (response.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Report package sign up succeed.");
        } else {
            LOGGER.info("Report package sign up failed.");
        }
    }

    private static PackageSignupInfo buildRequest() {
        SignupInfo signupInfo = SignupInfo.builder()
            .statusSet(2)
            .orderTimeSet("2020-07-13T06:45:00Z")
            .orderIdSet("112233333")
            .payOrderIdSet("hw12u23i47279387")
            .expireSet("2020-12-01T08:00:00Z")
            .build();
        return PackageSignupInfo.builder()
            .packageIdSet("pkg_330438929520066560")
            .productNo("935664579453157152")
            .signupInfoSet(signupInfo)
            .build();
    }
}
