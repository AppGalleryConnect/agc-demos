
package com.huawei.agconnect.lesson;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.course.impl.CourseUpdateRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;
import com.huawei.agconnect.server.edukit.course.model.CourseEdit;
import com.huawei.agconnect.server.edukit.course.resp.CourseUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 回滚章节(章节未上架）
 * 如果该章节为通过createLesson接口新创建的章节，回滚后将被删除
 * 如果该章节通过updateLesson接口创建了新版本，回滚后新版本将被清除
 * 如果该章节通过deleteLesson接口进行了删除，回滚后该章节将被恢复
 * 回滚操作仅在课程新版本未提交审核之前有效；如已审核通过，在该版本中进行的章节相关操作不能再回滚
 */
public class ResetLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetLesson.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 课程ID
         * 可由CourseCreateRequest::saveDraft或CourseCreateRequest::commit返回。
         */
        String courseId = "470154280746156032";
        String courseEditId = "470154280746156033";
        /**
         * 章节ID
         * 可由LessonCreateRequest::createLesson返回。
         */
        String lessonId = "470154346277961728";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(ResetLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        CommonResponse commonResponse = reSetLesson(clientName, courseId, courseEditId, lessonId);
        LOGGER.info("Reset lesson response:{}", commonResponse);

        // 提交审核时要在请求对象中携带课程编辑版本ID，否则会再次创建课程版本。
        CourseEdit courseEdit = CourseEdit.builder()
            .courseEditIdSet(courseEditId)
            .courseSet(Course.builder().courseIdSet(courseId).build())
            .build();
        CourseUpdateRequest courseUpdateRequest = AGCEdukit.getInstance(clientName).getCourseUpdateRequest(courseEdit);
        CourseUpdateResponse courseUpdateResponse = courseUpdateRequest.saveDraft().join();

        LOGGER.info("Reset lesson finished, courseUpdateResponse = {}.", courseUpdateResponse);
    }

    private static CommonResponse reSetLesson(String clientName, String courseId, String courseEditId,
        String lessonId) {
        return AGCEdukit.getInstance(clientName).getLessonDeleteRequest(courseId, courseEditId, lessonId).resetLesson();
    }
}
