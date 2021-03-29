
package com.huawei.agconnect.report;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.signup.model.BatchReportSignupCourseRequest;
import com.huawei.agconnect.server.edukit.signup.model.CourseSignupWithMultipleOrder;
import com.huawei.agconnect.server.edukit.signup.model.OrderInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量通知用户课程订购状态变更
 */
public class BatchReportSignupCourse {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchReportSignupCourse.class);

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
        Integer userIdType = 1;

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        BatchReportSignupCourse.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        BatchReportSignupCourseRequest request = buildRequest();
        CommonResponse response =
            AGCEdukit.getInstance(clientName).batchReportSignupCourse(userId, userIdType, request);
        LOGGER.info("Batch report course sign up response:{}", response);
        if (response.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Batch report course sign up succeed.");
        } else {
            LOGGER.info("Batch report course sign up failed.");
        }
    }

    private static BatchReportSignupCourseRequest buildRequest() {
        List<OrderInfo> ordersOfCourse1 = new ArrayList<>();
        ordersOfCourse1.add(OrderInfo.builder()
            .orderIdSet("1223sdfs2323")
            .payOrderIdSet("hsdh2343254u231")
            .orderTimeSet("2020-07-13T06:45:00Z")
            .build());
        ordersOfCourse1.add(OrderInfo.builder()
            .orderIdSet("1223564s4s23")
            .payOrderIdSet("uwnsh323878748923")
            .orderTimeSet("2020-07-13T06:45:05Z")
            .build());

        List<CourseSignupWithMultipleOrder> signupList = new ArrayList<>();
        signupList.add(CourseSignupWithMultipleOrder.builder()
            .courseIdSet("219182900480848896")
            .expireSet("2020-12-01T08:00:00Z")
            .ordersSet(ordersOfCourse1)
            .build());

        List<OrderInfo> ordersOfCourse2 = new ArrayList<>();
        ordersOfCourse2.add(OrderInfo.builder()
            .orderIdSet("1029821s2323")
            .payOrderIdSet("hsdh2343254u231")
            .orderTimeSet("2020-07-13T06:45:00Z")
            .build());
        ordersOfCourse2.add(OrderInfo.builder()
            .orderIdSet("9283983294s23")
            .payOrderIdSet("uwnsh323878748923")
            .orderTimeSet("2020-07-13T06:45:05Z")
            .build());

        signupList.add(CourseSignupWithMultipleOrder.builder()
            .courseIdSet("219182900480848896")
            .expireSet("2020-12-01T08:00:00Z")
            .ordersSet(ordersOfCourse2)
            .build());
        return BatchReportSignupCourseRequest.builder().statusSet(2).signupListSet(signupList).build();

    }
}
