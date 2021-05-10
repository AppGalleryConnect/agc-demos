
package com.huawei.agconnect.lesson;

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
import com.huawei.agconnect.server.edukit.course.resp.CourseUpdateResponse;
import com.huawei.agconnect.server.edukit.lesson.impl.LessonUpdateRequest;
import com.huawei.agconnect.server.edukit.lesson.model.Lesson;
import com.huawei.agconnect.server.edukit.lesson.model.LessonEdit;
import com.huawei.agconnect.server.edukit.lesson.model.LessonLocalizedData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMetaData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMultiLanguageData;
import com.huawei.agconnect.server.edukit.lesson.resp.LessonUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 单独更新章节（所属课程已上架）
 * 注意：课程已上架时，由于不需要更新课程信息，可通过createNewEdit接口获取courseEditId
 */
public class UpdateLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateLesson.class);

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
        String courseId = "533321566168472576";
        /**
         * 章节ID
         * 可由LessonCreateRequest::createLesson返回。
         */
        String lessonId = "533321648502660096";

        /**
         * 当创建章节新版本时是否强制创建新版本，即当前课程版本存在对应的章节版本时，是否强制创建新章节版本
         */
        Boolean forceCreateNewEdit = false;

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(UpdateLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        /*如果您不需要使用CompletableFuture，可以参考本注释块中的代码
        CourseUpdateResponse courseUpdateResponse =
            updateLessonOnShelfSync(clientName, courseId, lessonId, forceCreateNewEdit);
        LOGGER.info("Update lesson response : {}", courseUpdateResponse);*/

        CompletableFuture<CourseUpdateResponse> future =
            updateLessonOnShelfAsync(clientName, courseId, lessonId, forceCreateNewEdit);

        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(courseUpdateResponse -> {
            if (courseUpdateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 保存章节版本ID，如果审核没有通过，可以使用该版本ID继续更新
            } else {
                LOGGER.error("Update lesson response : {}", courseUpdateResponse);
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Update lesson finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Update lesson failed.");
        }
    }

    private static CompletableFuture<CourseUpdateResponse> updateLessonOnShelfAsync(String clientName, String courseId,
        String lessonId, Boolean forceCreateNewEdit) {
        CourseEdit courseEdit = CourseEdit.builder().courseSet(Course.builder().courseIdSet(courseId).build()).build();
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        CourseUpdateRequest courseUpdateRequest = edukit.getCourseUpdateRequest(courseEdit);
        return CompletableFuture.supplyAsync(() -> {
            return courseUpdateRequest.createNewEdit();
        }).thenCompose(courseUpdateResponse -> {
            if (courseUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Create course new edition failed.");
                return null;
            }
            LessonEdit lessonEdit =
                buildLessonEdit(courseUpdateResponse.getCourseId(), courseUpdateResponse.getEditId(), lessonId);
            LessonUpdateRequest lessonUpdateRequest =
                AGCEdukit.getInstance(clientName).getLessonUpdateRequest(lessonEdit, forceCreateNewEdit);
            return lessonUpdateRequest.updateLesson();
        }).thenCompose(lessonUpdateResponse -> {
            if (lessonUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Update lesson failed.");
                return null;
            }
            return courseUpdateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW);
        });
    }

    private static CourseUpdateResponse updateLessonOnShelfSync(String clientName, String courseId, String lessonId,
        Boolean forceCreateNewEdit) {
        CourseEdit courseEdit = CourseEdit.builder().courseSet(Course.builder().courseIdSet(courseId).build()).build();
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        CourseUpdateRequest courseUpdateRequest = edukit.getCourseUpdateRequest(courseEdit);
        CourseUpdateResponse courseUpdateResponse = courseUpdateRequest.createNewEdit();
        if (courseUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("Create course new edition failed.");
            return null;
        }
        LessonEdit lessonEdit =
            buildLessonEdit(courseUpdateResponse.getCourseId(), courseUpdateResponse.getEditId(), lessonId);
        LessonUpdateRequest lessonUpdateRequest =
            AGCEdukit.getInstance(clientName).getLessonUpdateRequest(lessonEdit, forceCreateNewEdit);
        LessonUpdateResponse lessonUpdateResponse = lessonUpdateRequest.updateLesson().join();
        if (lessonUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("Update lesson failed, lessonUpdateResponse = {}.", lessonUpdateResponse);
            return null;
        }
        return courseUpdateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW).join();
    }

    private static LessonEdit buildLessonEdit(String courseId, String courseEditId, String lessonId) {
        LessonMetaData metaData = LessonMetaData.builder().nameSet("单独更新已上架章节").orderSet(2).build();
        MediaFileInfo mediaFileInfo =
            MediaFileInfo.builder().fileTypeSet(CommonConstants.MediaFileType.MP4).pathSet(path + "1.mp4").build();

        MediaLocalizedData mediaLocalizedData = MediaLocalizedData.builder()
            .meidaFileInfoSet(mediaFileInfo)
            .mediaTypeSet(CommonConstants.MediaType.LESSON_VIDEO_FILE)
            .ordinalSet(1)
            .build();
        List<MediaLocalizedData> mediaLocalizedDataList = new ArrayList<>();
        mediaLocalizedDataList.add(mediaLocalizedData);
        LessonLocalizedData lessonLocalizedData = LessonLocalizedData.builder().nameSet("单独更新已上架章节").build();

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
            .build();
    }

}
