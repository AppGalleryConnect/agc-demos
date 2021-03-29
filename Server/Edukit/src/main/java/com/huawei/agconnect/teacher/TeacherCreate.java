
package com.huawei.agconnect.teacher;

import com.huawei.agconnect.callback.ProgressCallbackImpl;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;
import com.huawei.agconnect.server.edukit.teacher.impl.TeacherCreateRequest;
import com.huawei.agconnect.server.edukit.teacher.model.Teacher;
import com.huawei.agconnect.server.edukit.teacher.model.TeacherLocalizedData;
import com.huawei.agconnect.server.edukit.teacher.model.TeacherMetaData;
import com.huawei.agconnect.server.edukit.teacher.resp.TeacherCreateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述
 *
 * @since 2020-07-11
 */
public class TeacherCreate {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherCreate.class);

    /**
     * 教师头像上传本地路径,jpg、png格式，图片分辨率为312*312像素，单张图片最大为500kb
     */
    private static String path = "D:\\education\\teacher.png";

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(TeacherCreate.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        Teacher teacher = buildTeacher();
        TeacherCreateRequest teacherCreateRequest = AGCEdukit.getInstance(clientName).getTeacherCreateRequest(teacher);

        /**
         * 您可以单独把教师提交审核，也可以把教师关联到课程里一起提交审核
         */
        CompletableFuture<TeacherCreateResponse> future = teacherCreateRequest.saveDraft();
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(teacherCreateResponse -> {
            LOGGER.info("Create teacher response : {}", teacherCreateResponse);
            if (teacherCreateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 教师创建成功后，需要在本地保存返回的teacherId和teacherEditId
                // 保存教师Id与教师的关联关系
                // 保存教师版本ID，如果审核没有通过，可以使用该教师版本ID继续更新
            } else {
                // 根据错误码进行异常场景处理
            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Create teacher finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Create teacher failed.");
        }
    }

    private static Teacher buildTeacher() {
        TeacherMetaData teacherMetaData = TeacherMetaData.builder()
            // 用户指定是否为名师
            .famousFlagSet(false)
            // 用户指定默认语言
            .defaultLangSet(CommonConstants.DEFAULT_LANGUAGE)
            .build();

        // 本地化信息的图片文件信息
        // 不同语言可使用不同头像，jpg、png格式，图片分辨率为312*312像素，单张图片最大为500kb
        ImageFileInfo imageFileInfo =
            ImageFileInfo.builder().pathSet(path).resourceTypeSet(CommonConstants.ResourceType.TUTOR_PORTRAIT).build();

        // 中文语言对应的本地化信息
        TeacherLocalizedData teacherLocalizedData = TeacherLocalizedData.builder()
            .languageSet(CommonConstants.DEFAULT_LANGUAGE)
            .nameSet("王老师")
            .descriptionSet("王老师很幽默")
            .teacherPortraitSet(imageFileInfo)
            .build();

        // 多种语言的本地化信息list
        List<TeacherLocalizedData> teacherMultiList = new ArrayList<>();
        teacherMultiList.add(teacherLocalizedData);

        // 构建教师对象
        return Teacher.builder()
            // 用户指定String类型的教师用户侧编号，允许长度最大为64
            .devTeacherIdSet("T12")
            .teacherMetaDataSet(teacherMetaData)
            .teacherLocalizedDataSet(teacherMultiList)
            .progressCallbackSet(new ProgressCallbackImpl()::onProgressChanged)
            .build();
    }
}
