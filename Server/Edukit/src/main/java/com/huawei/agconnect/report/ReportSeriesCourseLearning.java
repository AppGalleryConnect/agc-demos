
package com.huawei.agconnect.report;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.learning.model.SeriesCourseLearningInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 上报用户系列课学习记录
 */
public class ReportSeriesCourseLearning {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportSeriesCourseLearning.class);

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
         * 用户订购/退订的章节ID
         */
        String lessonId = "405584373635678208";
        /**
         * userId字段对应的帐号类型
         */
        Integer userIdType = 1;
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        ReportSeriesCourseLearning.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        SeriesCourseLearningInfo seriesCourseLearningInfo = SeriesCourseLearningInfo.builder()
            .startTimeSet(new Date())
            .courseLearningStatusSet(CommonConstants.LearningStatus.LEARNING)
            .lessonLearningStatusSet(CommonConstants.LearningStatus.FINISHED)
            .build();
        CommonResponse response = AGCEdukit.getInstance(clientName)
            .reportSeriesCourseLearning(userId, courseId, userIdType, lessonId, seriesCourseLearningInfo);
        LOGGER.info("Report series course learning response:{}", response);
        if (response.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Report series course learning succeed.");
        } else {
            LOGGER.info("Report series course learning failed.");
        }
    }
}
