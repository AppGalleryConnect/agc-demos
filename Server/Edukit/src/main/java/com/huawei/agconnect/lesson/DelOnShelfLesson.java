
package com.huawei.agconnect.lesson;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.course.impl.CourseUpdateRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;
import com.huawei.agconnect.server.edukit.course.model.CourseEdit;
import com.huawei.agconnect.server.edukit.course.resp.CourseUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除已上架章节
 */
public class DelOnShelfLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelOnShelfLesson.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 课程ID
         * 可由CourseCreateRequest::saveDraft或CourseCreateRequest::commit返回。
         */
        String courseId = "456427322174668800";
        /**
         * 章节ID
         * 可由LessonCreateRequest::createLesson返回。
         */
        String lessonId = "456427346291916800";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(DelOnShelfLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        /**
         * 创建新课程版本
         */
        CourseEdit courseEdit = CourseEdit.builder().courseSet(Course.builder().courseIdSet(courseId).build()).build();
        CourseUpdateRequest courseUpdateRequest = AGCEdukit.getInstance(clientName).getCourseUpdateRequest(courseEdit);
        CourseUpdateResponse courseNewEdition = courseUpdateRequest.createNewEdit();
        if (courseNewEdition.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("Create course new edition failed and response:{}", courseNewEdition);
            return;
        }
        CommonResponse commonResponse = delOnShelfLesson(clientName, courseId, courseNewEdition.getEditId(), lessonId);
        if (commonResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("delete on shelf lesson response:{}", commonResponse);
            return;
        }

        CourseUpdateResponse courseUpdateResponse =
            courseUpdateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW).join();

        LOGGER.info("delete on shelf lesson succeed courseUpdateResponse = {}.", courseUpdateResponse);
    }

    private static CommonResponse delOnShelfLesson(String clientName, String courseId, String courseEditId,
        String lessonId) {
        return AGCEdukit.getInstance(clientName)
            .getLessonDeleteRequest(courseId, courseEditId, lessonId)
            .deleteLesson();
    }
}
