
package com.huawei.agconnect.course;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.course.impl.CourseOffShelfRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下架课程，如果当前课程未上架，则报错"content status conflict"
 */
public class OffShelfCourse {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffShelfCourse.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 课程ID
         */
        String courseId = "441844768310362112";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(OffShelfCourse.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        Course course = Course.builder().courseIdSet(courseId).build();
        CourseOffShelfRequest courseOffShelfRequest =
            AGCEdukit.getInstance(clientName).getCourseOffShelfRequest(course);
        CommonResponse commonResponse = courseOffShelfRequest.offShelf();
        LOGGER.info("Off shelf course response:{}", commonResponse);
    }
}
