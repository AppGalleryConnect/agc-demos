
package com.huawei.agconnect.report;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.signup.model.SignupInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通知用户课程订购状态变更
 */
public class ReportSignUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportSignUp.class);

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
         * 用户订购/退订的课程ID
         */
        String courseId = "405667146631282688";
        /**
         * userId字段对应的帐号类型
         */
        Integer userIdType = 1;

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(ReportSignUp.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        SignupInfo signupInfo = SignupInfo.builder()
            .orderIdSet("12234221tuiding")
            .payOrderIdSet("32239u2tuiding")
            .statusSet(2)
            .expireSet("2020-12-01T08:00:00Z")
            .orderTimeSet("2020-07-13T06:45:00Z")
            .build();
        CommonResponse response =
            AGCEdukit.getInstance(clientName).reportSignup(userId, courseId, userIdType, signupInfo);
        LOGGER.info("Report sign up response:{}", response);
        if (response.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Report sign up succeed.");
        } else {
            LOGGER.info("Report sign up failed.");
        }
    }
}
