
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
import com.huawei.agconnect.server.edukit.common.model.ProductPrice;
import com.huawei.agconnect.server.edukit.common.model.Result;
import com.huawei.agconnect.server.edukit.course.impl.CourseCreateRequest;
import com.huawei.agconnect.server.edukit.course.model.Course;
import com.huawei.agconnect.server.edukit.course.model.CourseEditMetaData;
import com.huawei.agconnect.server.edukit.course.model.CourseLocalizedData;
import com.huawei.agconnect.server.edukit.course.model.CourseMetaData;
import com.huawei.agconnect.server.edukit.course.model.CourseMultiLanguageData;
import com.huawei.agconnect.server.edukit.course.resp.CourseCreateResponse;
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
 * 创建有章节课程，如果课程创建成功，但章节创建失败，
 * 可以保存创建课程时返回的课程ID和课程版本ID，用来创建章节,等全部创建成功后，再提交审核
 * 有章节课程不需要指定课程视频、音频文件，需要指定章节视频、音频文件
 */
public class CreateCourseWithLesson {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCourseWithLesson.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(
                        CreateCourseWithLesson.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        /* 如果您不需要使用CompletableFuture，可以参考本注释块中的代码
        CourseCreateResponse courseCreateResponse = createCourseWithLessonSync(clientName);
        LOGGER.info("Create course response : {}", courseCreateResponse);
        if (courseCreateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            // 获取课程ID，请保存课程ID与您的课程的关联关系
            // 保存课程版本ID，如果审核没有通过，可以使用该课程版本ID继续更新
        }*/

        CompletableFuture<CourseCreateResponse> future = createCourseWithLessonAsync(clientName);
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(courseCreateResponse -> {
            if (courseCreateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 获取课程ID，请保存课程ID与您的课程的关联关系
                // 保存课程版本ID，如果审核没有通过，可以使用该课程版本ID继续更新
            } else {
                LOGGER.error("Create course response : {}", courseCreateResponse);
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Create course with lesson finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Create course with lesson failed.");
        }
    }

    private static CompletableFuture<CourseCreateResponse> createCourseWithLessonAsync(String clientName) {
        Course course = buildCourse();

        // 获取课程创建请求对象
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        CourseCreateRequest courseCreateRequest = edukit.getCourseCreateRequest(course);

        // 章节需要与课程一起提交，这里先保存课程草稿
        return courseCreateRequest.saveDraft().thenCompose(courseCreateResponse -> {
            LOGGER.info("Course create response:{}", courseCreateResponse);
            Result result = new Result();
            LessonCreateResponse lessonCreateResponse = new LessonCreateResponse();
            if (courseCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                result.setResultCode(CommonErrorCode.SYSTEM_ERROR.getRespErrorCode());
                result.setResultDesc(CommonErrorCode.SYSTEM_ERROR.getErrorDesc());
                lessonCreateResponse.setResult(result);
                return CompletableFuture.supplyAsync(() -> lessonCreateResponse);
            }

            // 课程创建成功后，需要在本地保存返回的courseId和courseEditId
            LessonCreateResponse finalLessonCreateResponse = lessonCreateResponse;
            CompletableFuture<LessonCreateResponse> future = CompletableFuture.supplyAsync(() -> {
                result.setResultCode(CommonErrorCode.SUCCESS);
                result.setResultDesc(CommonErrorCode.SUCCESS_DESC);
                finalLessonCreateResponse.setResult(result);
                return finalLessonCreateResponse;
            });
            // 按顺序创建3个章节，只要有一个章节创建失败，后续章节就不会再创建
            for (int i = 1; i <= 3; i++) {
                int finalI = i;
                future = future.thenCompose(response -> {
                    if (response.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                        LOGGER.error("Lesson create failed, response:{}", response);
                        return CompletableFuture.supplyAsync(() -> response);
                    }
                    LOGGER.info("Lesson create succeed, response:{}", response);
                    // 章节创建成功后，需要保存章节ID和章节版本ID
                    return createLessonAsync(clientName, courseCreateResponse.getCourseId(),
                        courseCreateResponse.getEditId(), finalI);
                });
            }
            return future;
        }).thenCompose(lessonCreateResponse -> {
            if (lessonCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Lesson create failed, response:{}", lessonCreateResponse);
                return CompletableFuture.supplyAsync(() -> {
                    CourseCreateResponse courseCreateResponse = new CourseCreateResponse();
                    Result result = new Result();
                    result.setResultCode(CommonErrorCode.SYSTEM_ERROR.getRespErrorCode());
                    result.setResultDesc(CommonErrorCode.SYSTEM_ERROR.getErrorDesc());
                    courseCreateResponse.setResult(result);
                    return courseCreateResponse;
                });
            }
            // 章节创建完成后，需要和课程一起提交审核
            return courseCreateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW);
        });
    }

    private static CourseCreateResponse createCourseWithLessonSync(String clientName) {
        Course course = buildCourse();
        // 获取课程创建请求对象
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        CourseCreateRequest courseCreateRequest = edukit.getCourseCreateRequest(course);
        // 章节需要与课程一起提交，这里先保存课程草稿
        CourseCreateResponse courseCreateResponse = courseCreateRequest.saveDraft().join();
        LOGGER.info("Course create response:{}", courseCreateResponse);
        if (courseCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
            return courseCreateResponse;
        }
        // 课程创建成功后，需要在本地保存返回的courseId和courseEditId

        // 创建3个章节
        boolean createAllLessonsSucceed = true;
        for (int i = 0; i < 3;) {
            LessonCreateResponse lessonCreateResponse =
                createLessonSync(clientName, courseCreateResponse.getCourseId(), courseCreateResponse.getEditId(), ++i);
            LOGGER.info("Lesson create response:{}", lessonCreateResponse);
            if (lessonCreateResponse.getResult().getResultCode() != CommonErrorCode.SUCCESS) {
                LOGGER.error("Lesson create failed:{}", lessonCreateResponse);
                createAllLessonsSucceed = false;
                break;
            }
        }

        if (createAllLessonsSucceed) {
            return courseCreateRequest.commit(CommonConstants.CourseCommitAction.COMMIT_REVIEW).join();
        }

        Result result = new Result();
        result.setResultCode(CommonErrorCode.SYSTEM_ERROR.getRespErrorCode());
        result.setResultDesc(CommonErrorCode.SYSTEM_ERROR.getErrorDesc());
        courseCreateResponse.setResult(result);
        return courseCreateResponse;
    }

    private static CompletableFuture<LessonCreateResponse> createLessonAsync(String clientName, String courseId,
        String courseEditId, int count) {
        LOGGER.info("Create lesson start count:" + count);
        // 构造章节元数据
        LessonMetaData metaData = LessonMetaData.builder()
            .nameSet("demo创建章节" + count)
            .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
            .freeFlagSet(true)
            .availableFromSet("2021-12-20T08:00:00Z")
            .availableBeforeSet("2022-01-01T10:00:00Z")
            .displayOrderSet(count * 10)
            .build();

        // 构造本地化多语言数据
        MediaFileInfo mediaFileInfo = MediaFileInfo.builder()
            .fileTypeSet(CommonConstants.MediaFileType.MP4)
            .pathSet(path + count + ".mp4")
            .build();
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
        LessonLocalizedData lessonLocalizedData = LessonLocalizedData.builder().nameSet("demo创建章节" + count).build();

        LessonMultiLanguageData lessonLocalized = LessonMultiLanguageData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .mediaLocalizedDataSet(mediaLocalizedDataList)
            .lessonLocalizedDataSet(lessonLocalizedData)
            .build();

        List<LessonMultiLanguageData> multiLangLessonLocalizedData = new ArrayList<>();
        multiLangLessonLocalizedData.add(lessonLocalized);

        Lesson lesson = Lesson.builder()
            .metaDataSet(metaData)
            .multiLangLessonLocalizedDataSet(multiLangLessonLocalizedData)
            .build();

        // 创建章节
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        LessonCreateRequest lessonCreateRequest = edukit.getLessonCreateRequest(lesson, courseId, courseEditId);
        return lessonCreateRequest.createLesson();
    }

    private static LessonCreateResponse createLessonSync(String clientName, String courseId, String courseEditId,
        int count) {
        LOGGER.info("Create lesson start count:" + count);
        // 构造章节元数据
        LessonMetaData metaData = LessonMetaData.builder()
            .nameSet("demo创建章节" + count)
            .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
            .freeFlagSet(true)
            .availableFromSet("2021-12-20T08:00:00Z")
            .availableBeforeSet("2022-01-01T10:00:00Z")
            .displayOrderSet(count * 10)
            .build();

        // 构造本地化多语言数据
        MediaFileInfo mediaFileInfo = MediaFileInfo.builder()
            .fileTypeSet(CommonConstants.MediaFileType.MP4)
            .pathSet(path + count + ".mp4")
            .build();
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
        LessonLocalizedData lessonLocalizedData = LessonLocalizedData.builder().nameSet("demo创建章节" + count).build();

        LessonMultiLanguageData lessonLocalized = LessonMultiLanguageData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .mediaLocalizedDataSet(mediaLocalizedDataList)
            .lessonLocalizedDataSet(lessonLocalizedData)
            .build();

        List<LessonMultiLanguageData> multiLangLessonLocalizedData = new ArrayList<>();
        multiLangLessonLocalizedData.add(lessonLocalized);

        Lesson lesson = Lesson.builder()
            .metaDataSet(metaData)
            .multiLangLessonLocalizedDataSet(multiLangLessonLocalizedData)
            .build();

        // 创建章节
        AGCEdukit edukit = AGCEdukit.getInstance(clientName);
        LessonCreateRequest lessonCreateRequest = edukit.getLessonCreateRequest(lesson, courseId, courseEditId);
        return lessonCreateRequest.createLesson().join();
    }

    private static Course buildCourse() {
        // 构造课程元数据
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add("CN");
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add("214338295549722624");
        List<String> tagIds = new ArrayList<>();
        tagIds.add("243338018147009280");
        CourseEditMetaData courseEditMetaData = CourseEditMetaData.builder()
            .eduappUsedSet(true)
            .tagIdsSet(tagIds)
            .includeLessonsSet(true)
            .eduappPurchasedSet(true)
            .distNotifyUrlSet("https://test.com")
            .sourceNameSet("my app")
            .eduappUsedSet(true)
            .validityUnitSet(CommonConstants.ValidityUnit.PERMANENT)
            .sellingModeSet(CommonConstants.CourseSellingMode.FREE)
            .categoryIdsSet(categoryIds)
            .nameSet("demo创建章节自动提交")
            .typeIdSet(CommonConstants.CourseType.RECORDED)
            .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
            .build();
        CourseMetaData courseMetaData =
            CourseMetaData.builder().countryCodesSet(countryCodes).courseEditMetaDataSet(courseEditMetaData).build();

        // 构造课程本地化多语言数据
        List<ImageFileInfo> infos = new ArrayList<>();
        ImageFileInfo cover = ImageFileInfo.builder()
            // 课程封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "cover.png")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_HORIZONTAL_COVER)
            .build();
        ImageFileInfo introduce = ImageFileInfo.builder()
            // 课程介绍图片 jpg、png格式，图片分辨率为宽度1080像素，高度最大4096像素，单张图片最大为2MB
            .pathSet(path + "introduce.png")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_INTRODUCTION)
            .build();
        infos.add(introduce);
        // 视频分辨率(宽*高) >=640*360且<=3840*2160，大小要求10GB以内
        MediaFileInfo media =
            MediaFileInfo.builder().pathSet(path + "test.mp4").fileTypeSet(CommonConstants.MediaFileType.MP4).build();

        MediaLocalizedData mediaLocalizedData = MediaLocalizedData.builder()
            .meidaFileInfoSet(media)
            .ordinalSet(1)
            .mediaTypeSet(CommonConstants.MediaType.COURSE_INTRODUCTION_VIDEO)
            .mediaLenSet(1024)
            .widthSet(1080)
            .heigthSet(720)
            .build();
        List<MediaLocalizedData> mediaLocalizedDataList = new ArrayList<>();
        mediaLocalizedDataList.add(mediaLocalizedData);
        List<CourseMultiLanguageData> courseLocalizedDataList = new ArrayList<>();
        courseLocalizedDataList.add(CourseMultiLanguageData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .courseLocalizedDataSet(CourseLocalizedData.builder()
                .introduceImageFileInfosSet(infos)
                .coverImageFileInfoSet(cover)
                .nameSet("demo创建章节自动提交")
                .deeplinkUrlSet("www.test.com")
                .shortDescriptionSet("小学二年级语文简介")
                .fullDescriptionSet("小学二年级语文详细介绍")
                .build())
            .mediaLocalizedDataListSet(mediaLocalizedDataList) // 有章节时，如果不需要课程介绍视频（COURSE_INTRODUCTION_VIDEO），可以忽略
            .build());

        ProductPrice productPrice = ProductPrice.builder()
            .countryCodeSet("CN")
            .priceSet(2.0)
            .priceTypeSet(CommonConstants.PriceType.CURRENT)
            .build();
        List<ProductPrice> productPrices = new ArrayList<>();
        productPrices.add(productPrice);

        Course course = Course.builder()
            .courseMetaDataSet(courseMetaData)
            .courseMultiLanguageDataListSet(courseLocalizedDataList)
            .productPricesSet(productPrices)
            .progressCallbackSet(new ProgressCallbackImpl()::onProgressChanged)
            .build();
        return course;
    }
}
