
package com.huawei.agconnect.course;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.MediaFileInfo;
import com.huawei.agconnect.server.edukit.common.model.MediaLocalizedData;
import com.huawei.agconnect.server.edukit.course.impl.CourseUpdateRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;
import com.huawei.agconnect.server.edukit.course.model.CourseEdit;
import com.huawei.agconnect.server.edukit.course.model.CourseEditMetaData;
import com.huawei.agconnect.server.edukit.course.model.CourseMetaData;
import com.huawei.agconnect.server.edukit.course.resp.CourseUpdateResponse;
import com.huawei.agconnect.server.edukit.lesson.impl.LessonUpdateRequest;
import com.huawei.agconnect.server.edukit.lesson.model.Lesson;
import com.huawei.agconnect.server.edukit.lesson.model.LessonEdit;
import com.huawei.agconnect.server.edukit.lesson.model.LessonLocalizedData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMetaData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMultiLanguageData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 更新已上架带章节课程
 * 注意：
 * 课程已上架时，不需要指定courseEditId和lessonEditId，SDK会自动创建新版本，并在响应里返回。
 * 课程未上架时，需要指定courseEditId和lessonEditId为创建课程和章节时返回的值，否则未上架部分的修改将会丢失
 */
public class UpdateCourseWithLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCourseWithLesson.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        /**
         * 课程ID
         * 可由CourseCreateRequest::saveDraft或CourseCreateRequest::commit返回。
         */
        String courseId = "441746188056723456";
        String courseEditId = "441746188056723457";
        /**
         * 章节ID
         * 可由LessonCreateRequest::createLesson返回。
         */
        String lessonId = "441746212400463872";
        String lessonEditId = "441746212400463873";
        /**
         * 当创建章节新版本时是否强制创建新版本，即当前课程版本存在对应的章节版本时，是否强制创建新章节版本
         */
        Boolean forceCreateNewEdit = false;

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        UpdateCourseWithLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        /*如果您不需要使用CompletableFuture，可以参考本注释块中的代码
        CourseUpdateResponse courseUpdateResponse =
            updateCourseWithLesson(clientName, courseId, courseEditId, lessonId, lessonEditId, forceCreateNewEdit)
                .join();
        LOGGER.info("Update course response : {}", courseUpdateResponse);*/

        CompletableFuture<CourseUpdateResponse> future =
            updateCourseWithLesson(clientName, courseId, courseEditId, lessonId, lessonEditId, forceCreateNewEdit);
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(courseUpdateResponse -> {
            LOGGER.info("Update course response : {}", courseUpdateResponse);
            if (courseUpdateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 保存课程版本ID，如果审核没有通过，可以使用该课程版本ID继续更新
            } else {
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Update course with lesson finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Update course with lesson failed.");
        }
    }

    private static CompletableFuture<CourseUpdateResponse> updateCourseWithLesson(String clientName, String courseId,
        String courseEditId, String lessonId, String lessonEditId, Boolean forceCreateNewEdit) {
        CourseEdit courseEdit = buildCourseEdit(courseId, courseEditId);

        CourseUpdateRequest courseUpdateRequest = AGCEdukit.getInstance(clientName).getCourseUpdateRequest(courseEdit);
        return courseUpdateRequest.saveDraft().thenCompose((courseUpdateResponse) -> {
            LOGGER.info("Update course response:{}", courseUpdateResponse);
            if (courseUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Update course failed.");
                return null;
            }

            LessonEdit lessonEdit = buildLessonEdit(courseId, courseEditId, lessonId, lessonEditId);

            LessonUpdateRequest lessonUpdateRequest =
                AGCEdukit.getInstance(clientName).getLessonUpdateRequest(lessonEdit, forceCreateNewEdit);
            return lessonUpdateRequest.updateLesson();
        }).thenCompose(lessonUpdateResponse -> {
            LOGGER.info("Create lesson response:{}", lessonUpdateResponse);
            if (lessonUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Create lesson failed and response:{}", lessonUpdateResponse);
                return null;
            }
            // 如果需要提交审核，请使用courseUpdateRequest.commit(CommonConstants.CommitAction.COMMIT_REVIEW);
            return courseUpdateRequest.saveDraft();
        });
    }

    private static LessonEdit buildLessonEdit(String courseId, String courseEditId, String lessonId,
        String lessonEditId) {
        LessonMetaData metaData = LessonMetaData.builder().nameSet("章节上架后更新").build();
        MediaFileInfo mediaFileInfo =
            MediaFileInfo.builder().fileTypeSet(CommonConstants.MediaFileType.MP4).pathSet(path + "test.mp4").build();

        MediaLocalizedData mediaLocalizedData = MediaLocalizedData.builder()
            .meidaFileInfoSet(mediaFileInfo)
            .ordinalSet(1)
            .mediaTypeSet(CommonConstants.MediaType.LESSON_VIDEO_FILE)
            .build();
        List<MediaLocalizedData> mediaLocalizedDataList = new ArrayList<>();
        mediaLocalizedDataList.add(mediaLocalizedData);
        LessonLocalizedData lessonLocalizedData = LessonLocalizedData.builder().nameSet("章节上架后更新").build();

        LessonMultiLanguageData lessonLocalized = LessonMultiLanguageData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .lessonLocalizedDataSet(lessonLocalizedData)
            .mediaLocalizedDataSet(mediaLocalizedDataList)
            .build();
        List<LessonMultiLanguageData> multiLangLessonLocalizedData = new ArrayList<>();
        multiLangLessonLocalizedData.add(lessonLocalized);

        Lesson lesson = Lesson.builder()
            .metaDataSet(metaData)
            .multiLangLessonLocalizedDataSet(multiLangLessonLocalizedData)
            .lessonIdSet(lessonId)
            .build();
        return LessonEdit.builder()
            .courseEditIdForLessonSet(courseEditId)
            .courseIdForLessonSet(courseId)
            .lessonSet(lesson)
            .lessonEditIdSet(lessonEditId)
            .build();
    }

    private static CourseEdit buildCourseEdit(String courseId, String editId) {
        List<String> tagIds = new ArrayList<>();
        List<String> categoryIds = new ArrayList<>();
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add("CN");
        categoryIds.add("214341454741438464");
        tagIds.add("243299369934127872");

        CourseEditMetaData courseEditMetaData =
            CourseEditMetaData.builder().tagIdsSet(tagIds).categoryIdsSet(categoryIds).build();
        CourseMetaData courseMetaData =
            CourseMetaData.builder().courseEditMetaDataSet(courseEditMetaData).countryCodesSet(countryCodes).build();

        Course course = Course.builder().courseIdSet(courseId).courseMetaDataSet(courseMetaData).build();
        return CourseEdit.builder().courseEditIdSet(editId).courseSet(course).build();
    }

}
