
package com.huawei.agconnect.report;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.learning.model.UnitaryCourseLearningInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 上报用户单体课学习记录
 */
public class ReportUnitaryCourseLearning {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUnitaryCourseLearning.class);

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
        String courseId = "241634327119528064";
        /**
         * userId字段对应的帐号类型
         */
        Integer userIdType = 1;
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        ReportUnitaryCourseLearning.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        UnitaryCourseLearningInfo unitaryCourseLearningInfo = UnitaryCourseLearningInfo.builder()
            .learningStatusSet(CommonConstants.LearningStatus.FINISHED)
            .startTimeSet(new Date())
            .build();
        CommonResponse response = AGCEdukit.getInstance(clientName)
            .reportUnitaryCourseLearning(userId, courseId, userIdType, unitaryCourseLearningInfo);
        LOGGER.info("Report unitary course learning response:{}", response);
        if (response.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Report unitary course learning succeed.");
        } else {
            LOGGER.info("Report unitary course learning failed.");
        }
    }
}
