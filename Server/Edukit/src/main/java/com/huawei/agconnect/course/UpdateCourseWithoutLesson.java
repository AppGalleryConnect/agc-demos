
package com.huawei.agconnect.course;

import com.huawei.agconnect.callback.ProgressCallbackImpl;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;
import com.huawei.agconnect.server.edukit.common.model.MediaFileInfo;
import com.huawei.agconnect.server.edukit.common.model.MediaLocalizedData;
import com.huawei.agconnect.server.edukit.course.impl.CourseUpdateRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;
import com.huawei.agconnect.server.edukit.course.model.CourseEdit;
import com.huawei.agconnect.server.edukit.course.model.CourseEditMetaData;
import com.huawei.agconnect.server.edukit.course.model.CourseMetaData;
import com.huawei.agconnect.server.edukit.course.model.CourseMultiLanguageData;
import com.huawei.agconnect.server.edukit.course.resp.CourseUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 更新无章节课程
 * 注意：
 * 课程处于待审核状态时不可以更新，否则会报"content status conflict"。
 * 如果上次提交审核不通过，需要指定courseEditId为提交审核时返回的课程版本ID进行更新，如果不指定courseEditId，会在当前在架版本的基础上进行更新，
 * 之前未提交的数据将会丢失，如果当前没有在架版本，则会报错"The content/teacher/package is not permitted"。
 * 如果上次提交审核通过，不需要指定courseEditId。
 */
public class UpdateCourseWithoutLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCourseWithoutLesson.class);

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
        String courseId = "470154280746156032";
        String courseEditId = "470154280746156033";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        UpdateCourseWithoutLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        CourseEdit courseEdit = buildCourseEdit(courseId, courseEditId);
        CourseUpdateRequest courseUpdateRequest = AGCEdukit.getInstance(clientName).getCourseUpdateRequest(courseEdit);

        /*如果您不需要使用CompletableFuture，可以参考本注释块中的代码
        CourseUpdateResponse courseUpdateResponse = courseUpdateRequest.saveDraft().join();
        LOGGER.info("Update course response : {}", courseUpdateResponse);*/

        // 如果需要提交审核，请使用courseUpdateRequest.commit(CommonConstants.CommitAction.COMMIT_REVIEW);
        CompletableFuture<CourseUpdateResponse> future = courseUpdateRequest.saveDraft();

        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(courseUpdateResponse -> {
            if (courseUpdateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 保存课程版本ID，如果审核没有通过，可以使用该课程版本ID继续更新
            } else {
                LOGGER.error("Update course response : {}", courseUpdateResponse);
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Update course without lesson finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Update course without lesson failed. message:{}", e.getMessage());
        }
    }

    private static CourseEdit buildCourseEdit(String courseId, String editId) {
        List<String> tagIds = new ArrayList<>();
        tagIds.add("243337962438263680");
        tagIds.add("243300256475775744");
        CourseEditMetaData courseEditMetaData = CourseEditMetaData.builder().tagIdsSet(tagIds).build();
        CourseMetaData courseMetaData = CourseMetaData.builder().courseEditMetaDataSet(courseEditMetaData).build();

        List<MediaLocalizedData> mediaLocalizedDataList = new ArrayList<>();
        // 视频分辨率(宽*高) >=640*360且<=3840*2160，大小要求10GB以内
        MediaFileInfo media =
            MediaFileInfo.builder().fileTypeSet(CommonConstants.MediaFileType.MP4).pathSet(path + "test.mp4").build();
        // jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
        ImageFileInfo frame = ImageFileInfo.builder()
            .resourceTypeSet(CommonConstants.ResourceType.PROMOTIONAL_VIDEO_POSTER)
            .pathSet(path + "frame1.png")
            .build();

        MediaLocalizedData mediaLocalizedData = MediaLocalizedData.builder()
            .meidaFileInfoSet(media)
            .frameImageFileInfoSet(frame)
            .ordinalSet(1)
            .mediaTypeSet(CommonConstants.MediaType.COURSE_VIDEO_FILE)
            .mediaLenSet(1024)
            .widthSet(1080)
            .heigthSet(720)
            .build();
        mediaLocalizedDataList.add(mediaLocalizedData);
        CourseMultiLanguageData courseLocalizedData = CourseMultiLanguageData.builder()
            .mediaLocalizedDataListSet(mediaLocalizedDataList)
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .build();

        List<CourseMultiLanguageData> courseLocalizedDataList = new ArrayList<>();
        courseLocalizedDataList.add(courseLocalizedData);

        Course course = Course.builder()
            .courseIdSet(courseId)
            .courseMetaDataSet(courseMetaData)
            .courseMultiLanguageDataListSet(courseLocalizedDataList)
            .progressCallbackSet(new ProgressCallbackImpl()::onProgressChanged)
            .build();
        return CourseEdit.builder().courseEditIdSet(editId).courseSet(course).build();
    }
}
