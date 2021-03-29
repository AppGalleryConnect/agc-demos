
package com.huawei.agconnect.course;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.course.resp.CourseStatusResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取课程审核状态
 */
public class GetCourseStatus {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetCourseStatus.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 课程ID
         */
        String courseId = "421438183050379264";

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
        CourseStatusResponse courseStatus = AGCEdukit.getInstance(clientName).getCourseStatus(courseId);
        LOGGER.info("Course status response:{}", courseStatus);
    }
}
