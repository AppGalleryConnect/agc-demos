/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.demo;

import com.huawei.demo.api.CreateProject;
import com.huawei.demo.api.DelLanguageInfo;
import com.huawei.demo.api.GetAabStatus;
import com.huawei.demo.api.GetAppIdList;
import com.huawei.demo.api.GetAppInfo;
import com.huawei.demo.api.GetAppPkgList;
import com.huawei.demo.api.GetFileInfoDetectionResult;
import com.huawei.demo.api.GetToken;
import com.huawei.demo.api.ReleaseAppByDownload;
import com.huawei.demo.api.ShelfOffApp;
import com.huawei.demo.api.SubmitAppWithFile;
import com.huawei.demo.api.SubmitAudit;
import com.huawei.demo.api.UpdateAppInfo;
import com.huawei.demo.api.UpdateAppLang;
import com.huawei.demo.api.UpdateAppVersionWithFiles;
import com.huawei.demo.api.UpdateGMSDependencyAttributes;
import com.huawei.demo.api.UpdatePkg;
import com.huawei.demo.api.UpdateReleaseByPhase;
import com.huawei.demo.api.UpdateVersionReleaseTime;
import com.huawei.demo.api.UploadAppFileInfo;
import com.huawei.demo.api.UploadFile;
import com.huawei.demo.api.UploadMultipartCompose;
import com.huawei.demo.api.UploadMultipartInit;
import com.huawei.demo.api.UploadMultipartParts;
import com.huawei.demo.api.UploadSegment;
import com.huawei.demo.common.KeyConstants;
import com.huawei.demo.model.FileInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class PublishApiDemo {

    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {

        // Obtain the token.
        String token = GetToken.getToken(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, KeyConstants.CLIENT_SECRET);

        // Query app information.
        GetAppInfo.getAppInfo(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // Update app information.
        UpdateAppInfo.updateAppInfo(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // Upload the file.
        List<FileInfo> files =
            UploadFile.uploadFile(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // After file upload, call the API for updating app file information.
        UploadAppFileInfo.updateAppFileInfo(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID,
            files);

        // Create a project and generate the first application
        CreateProject.createProject(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token);

        // App removal
        ShelfOffApp.shelfOffApp(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // Get application id based on package name
        GetAppIdList.getAppIdList(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token);

        // Update information related to the application language
        UpdateAppLang.updateAppLang(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // Delete application information
        DelLanguageInfo.deleteLanguageInfo(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        SubmitAppWithFile.submitAppWithFile(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        // Submit for review.
        SubmitAudit.submit(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Update Phased Release
        UpdateReleaseByPhase.updateReleaseByPhase(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Update version release time
        UpdateVersionReleaseTime.updateVersionReleaseTime(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Set Application GMS Dependency Attributes
        UpdateGMSDependencyAttributes.updateGMSDependencyAttributes(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Query the Software Package Compilation Status
        GetAabStatus.getAabStatus(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Submit the package by downloadUrl
        ReleaseAppByDownload.releaseAppByDownload(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Querying the Software Package List of an Application
        GetAppPkgList.getAppPkgList(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Associate software packages with applications by downloading
        UpdateAppVersionWithFiles.updateAppVersionWithFiles(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Query the adaptation range detection result of multiple APKs.
        GetFileInfoDetectionResult.getFileInfoDetectionResult(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Associate the obb file with the APK software package
        UpdatePkg.updatePkg(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Software Package Segment upload Initialize
        UploadMultipartInit.uploadMultipartInit(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token, KeyConstants.APP_ID);

        //Obtaining the Segment Upload Address
        UploadMultipartParts.uploadMultipartParts(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token);

        //Upload a File by Segment
        UploadSegment.uploadSegment(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token);

        //Combine segment files
        UploadMultipartCompose.uploadMultipartCompose(KeyConstants.DOMAIN, KeyConstants.CLIENT_ID, token);
    }

}
