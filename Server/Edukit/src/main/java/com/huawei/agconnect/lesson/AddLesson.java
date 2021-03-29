
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
import com.huawei.agconnect.server.edukit.lesson.impl.LessonCreateRequest;
import com.huawei.agconnect.server.edukit.lesson.model.Lesson;
import com.huawei.agconnect.server.edukit.lesson.model.LessonLocalizedData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMetaData;
import com.huawei.agconnect.server.edukit.lesson.model.LessonMultiLanguageData;
import com.huawei.agconnect.server.edukit.lesson.resp.LessonCreateResponse;

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
public class AddLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddLesson.class);

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
        String courseId = "533212439144359936";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(AddLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        /*如果您不需要使用CompletableFuture，可以参考本注释块中的代码
        CourseUpdateResponse courseUpdateResponse = addLessonSync(clientName, courseId);
        LOGGER.info("Update lesson response : {}", courseUpdateResponse);*/

        CompletableFuture<CourseUpdateResponse> future = addLessonAsync(clientName, courseId);
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

    private static CompletableFuture<CourseUpdateResponse> addLessonAsync(String clientName, String courseId) {
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

            LessonCreateRequest lessonCreateRequest =
                edukit.getLessonCreateRequest(buildLesson(), courseId, courseUpdateResponse.getEditId());
            return lessonCreateRequest.createLesson();
        }).thenCompose(lessonCreateResponse -> {
            if (lessonCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Create lesson failed.");
                return null;
            }
            return courseUpdateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW);
        });
    }

    private static CourseUpdateResponse addLessonSync(String clientName, String courseId) {
        CourseEdit courseEdit = CourseEdit.builder().courseSet(Course.builder().courseIdSet(courseId).build()).build();
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        CourseUpdateRequest courseUpdateRequest = edukit.getCourseUpdateRequest(courseEdit);
        CourseUpdateResponse courseUpdateResponse = courseUpdateRequest.createNewEdit();
        if (courseUpdateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("Create course new edition failed.");
            return null;
        }

        LessonCreateRequest lessonCreateRequest =
            edukit.getLessonCreateRequest(buildLesson(), courseId, courseUpdateResponse.getEditId());
        LessonCreateResponse lessonCreateResponse = lessonCreateRequest.createLesson().join();
        if (lessonCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            LOGGER.error("Create lesson failed.");
            return null;
        }
        return courseUpdateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW).join();
    }

    private static Lesson buildLesson() {
        LOGGER.info("Create lesson start");
        // 构造章节元数据
        LessonMetaData metaData = LessonMetaData.builder()
            .nameSet("demo创建章节")
            .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
            .freeFlagSet(true)
            .availableFromSet("2021-12-20T08:00:00Z")
            .availableBeforeSet("2022-01-01T10:00:00Z")
            .build();

        // 构造本地化多语言数据
        MediaFileInfo mediaFileInfo =
            MediaFileInfo.builder().fileTypeSet(CommonConstants.MediaFileType.MP4).pathSet(path + "test.mp4").build();
        MediaLocalizedData mediaLocalizedData = MediaLocalizedData.builder()
            .meidaFileInfoSet(mediaFileInfo)
            .ordinalSet(1)
            .mediaTypeSet(CommonConstants.MediaType.LESSON_VIDEO_FILE)
            .mediaLenSet(1024)
            .widthSet(1080)
            .heigthSet(720)
            .build();
        List<MediaLocalizedData> mediaLocalizedDataList = new ArrayList<>();
        mediaLocalizedDataList.add(mediaLocalizedData);
        LessonLocalizedData lessonLocalizedData = LessonLocalizedData.builder().nameSet("demo创建章节").build();

        LessonMultiLanguageData lessonLocalized = LessonMultiLanguageData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .mediaLocalizedDataSet(mediaLocalizedDataList)
            .lessonLocalizedDataSet(lessonLocalizedData)
            .build();

        List<LessonMultiLanguageData> multiLangLessonLocalizedData = new ArrayList<>();
        multiLangLessonLocalizedData.add(lessonLocalized);

        return Lesson.builder()
            .metaDataSet(metaData)
            .multiLangLessonLocalizedDataSet(multiLangLessonLocalizedData)
            .build();
    }

}
