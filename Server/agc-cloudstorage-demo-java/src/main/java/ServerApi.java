/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

import com.huawei.agconnect.server.cloud.storage.ProgressListener;
import com.huawei.agconnect.server.cloud.storage.ProgressStatus;
import com.huawei.agconnect.server.cloud.storage.StorageManagement;
import com.huawei.agconnect.server.cloud.storage.entity.ObjectMetadata;
import com.huawei.agconnect.server.cloud.storage.exception.StorageException;
import com.huawei.agconnect.server.cloud.storage.request.DownloadFileRequest;
import com.huawei.agconnect.server.cloud.storage.request.ListObjectsRequest;
import com.huawei.agconnect.server.cloud.storage.request.PutObjectRequest;
import com.huawei.agconnect.server.cloud.storage.response.DeleteObjectResult;
import com.huawei.agconnect.server.cloud.storage.response.ListObjectsResult;
import com.huawei.agconnect.server.cloud.storage.service.AGCStorageManagement;
import com.huawei.agconnect.server.cloud.storage.service.StorageReference;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.credential.CredentialService;
import com.huawei.agconnect.server.commons.exception.AGCException;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @since 2021-07-03
 */
public class ServerApi {
    /**
     * Bucket name
     */
    private static final String BUCKET_NAME = "XXXXXX";

    private static final String File_NAME = "XXXXXXXXXXXX";

    /**
     * Resource Path
     */
    private static final String RESOURCEURL = Demo.class.getClassLoader().getResource("").getPath();

    private static final String OBJECT_NAME = "2M.txt";

    private static StorageManagement storageManagement;

    /**
     * Upload a file
     *
     * @throws AGCException AGC exception
     */
    public static void uploadFile() throws AGCException {
        File file = new File(RESOURCEURL + "/2M.txt");
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference();
        String[] result = {""};
        ProgressListener listener = new ProgressListener() {
            @Override
            public void inProgress(ProgressStatus status) {
                result[0] = "upload transferred/total:" + status.getTransferredBytes() + "/" + status.getTotalBytes()
                    + "," + status.getTransferPercentage() + "%, " + "instaneos speed: "
                    + status.getInstantaneousSpeed() + ", avg speed: " + status.getAverageSpeed();
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> System.out.println(result[0]), 2, 3, TimeUnit.SECONDS);
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setObjectMetadata(null);
        putObjectRequest.setObjectName(OBJECT_NAME);
        putObjectRequest.setUploadFile(file);
        putObjectRequest.setProgressListener(listener);
        storageReference.putObject(putObjectRequest);
        System.out.println(OBJECT_NAME + " upload successed " + storageReference.doesObjectExist(OBJECT_NAME));
        service.shutdownNow();
    }

    /**
     * Download a file
     *
     * @throws StorageException storage exception
     */
    public static void downloadFile() throws StorageException {
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference(OBJECT_NAME);
        final String[] result = {""};
        DownloadFileRequest request = new DownloadFileRequest();
        request.setOffset(0);
        request.setSaveFile(new File(OBJECT_NAME));
        request.setProgressListener(new ProgressListener() {
            @Override
            public void inProgress(ProgressStatus status) {
                result[0] = "download transferred/total:" + status.getTransferredBytes() + "/" + status.getTotalBytes()
                    + "," + status.getTransferPercentage() + "%, " + "instaneos speed: "
                    + status.getInstantaneousSpeed() + ", avg speed: " + status.getAverageSpeed();
            }
        });
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> System.out.println(result[0]), 2, 3, TimeUnit.SECONDS);

        storageReference.download(request);
        System.out.println("download finished");
        service.shutdownNow();
    }

    public static void listObjects() throws StorageException {
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference(OBJECT_NAME);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setMarker("");
        listObjectsRequest.setMaxKeys(100);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setListTimeout(5);

        ListObjectsResult objectsResult = storageReference.listObjects(listObjectsRequest);
        for (String file : objectsResult.getFileList()) {
            System.out.println("file - " + file);
        }
        for (String dir : objectsResult.getDirList()) {
            System.out.println("dir - " + dir);
        }
    }

    /**
     * Get metadata of a file
     *
     * @throws StorageException storage exception
     */
    public static void getMetadata() throws StorageException {
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference(OBJECT_NAME);

        ObjectMetadata objectMetadata = storageReference.getObjectMetadata();
        System.out.println(objectMetadata.getContentType());
        System.out.println(objectMetadata.getCacheControl());
        System.out.println(objectMetadata.getContentEncoding());
        System.out.println(objectMetadata.getContentDisposition());
        System.out.println(objectMetadata.getContentLanguage());
    }

    /**
     * Update metadata of a file
     *
     * @throws StorageException storage exception
     */
    public static void updateMetadata() throws StorageException {
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference(OBJECT_NAME);

        ObjectMetadata updatedObjectMetadata = storageReference.updateObjectMetadata(initObjectMetadata());
        System.out.println(updatedObjectMetadata.getContentType());
        System.out.println(updatedObjectMetadata.getCacheControl());
        System.out.println(updatedObjectMetadata.getContentEncoding());
        System.out.println(updatedObjectMetadata.getContentDisposition());
        System.out.println(updatedObjectMetadata.getContentLanguage());
    }

    /**
     * Delete a file
     *
     * @throws StorageException storage exception
     */
    public static void deleteFile() throws StorageException {
        storageManagement =
            AGCStorageManagement.getInstance(AGCClient.getInstance("agcClient"), "cn-north-4", BUCKET_NAME);
        StorageReference storageReference = storageManagement.getStorageReference();
        DeleteObjectResult deleteObjectResult = storageReference.deleteObject(OBJECT_NAME);
        System.out.println("Delete object " + deleteObjectResult.getObjectName());
    }

    private static ObjectMetadata initObjectMetadata() {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/*");
        metadata.setCacheControl("no-cache");
        metadata.setContentEncoding("identity");
        metadata.setContentDisposition("inline");
        metadata.setContentLanguage("en");
        return metadata;
    }

    /**
     * Download a file
     *
     * @throws AGCException AGC exception
     */
    public static void initializeClient() throws AGCException {
        CredentialService credential =
            CredentialParser.toCredential(RESOURCEURL + File_NAME);
        AGCParameter agcParameter = AGCParameter.builder().setCredential(credential).build();
		// Initialize a clientName and region is CN, and use clientName to obtain the value.
        AGCClient.initialize("agcClient", agcParameter, "CN");
        System.out.println("init agcParameter Success.");
    }
}
