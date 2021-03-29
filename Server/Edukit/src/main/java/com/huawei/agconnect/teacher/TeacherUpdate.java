
package com.huawei.agconnect.teacher;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;
import com.huawei.agconnect.server.edukit.teacher.impl.TeacherUpdateRequest;
import com.huawei.agconnect.server.edukit.teacher.model.Teacher;
import com.huawei.agconnect.server.edukit.teacher.model.TeacherEdit;
import com.huawei.agconnect.server.edukit.teacher.model.TeacherLocalizedData;
import com.huawei.agconnect.server.edukit.teacher.model.TeacherMetaData;
import com.huawei.agconnect.server.edukit.teacher.resp.TeacherUpdateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 教师更新并提交
 * 更新流程开始前须确认该教师为可更新状态，即已创建且不在已提交未审核状态
 * 支持基本数据、元数据、多语言数据三部分分别或任意组合的部分更新，未更新的部分继承原先的数据
 *
 * @since 2020-07-11
 */
public class TeacherUpdate {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherUpdate.class);

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
                        .toCredential(TeacherUpdate.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }
        TeacherEdit teacherEdit = buildTeacherEdit();
        TeacherUpdateRequest teacherUpdateRequest =
            AGCEdukit.getInstance(clientName).getTeacherUpdateRequest(teacherEdit);

        /**
         * 您可以单独把教师提交审核，也可以把教师关联到课程里一起提交审核
         */
        CompletableFuture<TeacherUpdateResponse> future = teacherUpdateRequest.commit();
        // latch仅用于demo中防止main线程提前结束导致进程退出，在您的工程中不需要使用Latch进行同步操作
        CountDownLatch waitTaskLatch = new CountDownLatch(1);
        future.thenAccept(teacherUpdateResponse -> {
            LOGGER.info("Update teacher response : {}", teacherUpdateResponse);
            if (teacherUpdateResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
                // 保存教师版本ID，如果审核没有通过，可以使用该教师版本ID继续更新
            } else {
                // 根据错误码进行异常场景处理

            }
            waitTaskLatch.countDown();
        });
        // 以下操作非demo示例，仅为了保证在异步任务执行完成前main线程不会因为执行完成导致程序提前结束
        try {
            waitTaskLatch.await();
            LOGGER.info("Update teacher finished.");
        } catch (InterruptedException e) {
            LOGGER.error("Update teacher failed.");
        }
    }

    private static TeacherEdit buildTeacherEdit() {
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

        // 英文语言对应的本地化信息
        TeacherLocalizedData teacherLocalizedData2 = TeacherLocalizedData.builder()
            .languageSet("us-CN")
            .nameSet("Mr.Wang")
            .descriptionSet("Mr.Wang is humorous")
            .teacherPortraitSet(imageFileInfo)
            .build();

        // 多种语言的本地化信息list
        List<TeacherLocalizedData> teacherMultiList = new ArrayList<>();
        teacherMultiList.add(teacherLocalizedData);
        teacherMultiList.add(teacherLocalizedData2);

        // 构建待删除语言list，须注意中文CommonConstants.DEFAULT_LANGUAGE不允许删除
        List<String> langList = new ArrayList<>();
        langList.add("us-CN");

        // 构建教师对象
        Teacher teacher = Teacher.builder()
            // 教育中心分配的教师ID,可由TeacherCreateResponse::getTeacherId获取
            .teacherIdSet("457048347694399488")
            // 需要被替换的旧用户侧教师编号,仅当更新基本信息时可能需要传入
            // 基本信息更新条件可以是教育中心分配的教师ID，也可以是您分配的用户侧教师编号；两个字段同时提供时，以教师ID为准
            .devTeacherIdSet("T88")
            .teacherMetaDataSet(teacherMetaData)
            .teacherLocalizedDataSet(teacherMultiList)
            // 建议删除多语言信息时不要同时更新对应语言的多语言信息，否则会产生冲突，导致多语言更新后为空或无效的删除。
            // .languageListForDeleteSet(langList)
            .build();

        // 构建教师编辑版本对象
        return TeacherEdit.builder()
            .teacherSet(teacher)
            // 教师编辑版本ID，传入前需确认该教师不是已提交未审核的状态
            // 只更新基本信息的情况除外，仅当教师通过审核后的初次更新可以不传入编辑版本ID（会自动创建），
            // 否则会报错（review version already exists）
            .teacherEditIdSet("457048347694399489")
            .build();
    }
}
