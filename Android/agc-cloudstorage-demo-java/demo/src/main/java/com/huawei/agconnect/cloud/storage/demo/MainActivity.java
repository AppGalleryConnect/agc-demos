package com.huawei.agconnect.cloud.storage.demo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.ListResult;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Task;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private AGCStorageManagement mAGCStorageManagement;
    private TextView mShowResultTv;
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowResultTv = findViewById(R.id.showResult);
        AGConnectInstance.initialize(getApplicationContext());
        login();
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    private void initAGCStorageManagement() {
        mAGCStorageManagement = AGCStorageManagement.getInstance();
    }

    private void login() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            System.out.println("already sign a user");
            return;
        }
        AGConnectAuth.getInstance().signInAnonymously().addOnSuccessListener(signInResult -> System.out.println("AGConnect OnSuccess"))
                .addOnFailureListener(e -> System.out.println("AGConnect OnFail: " + e.getMessage()));
    }

    public void uploadFile(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        uploadFile();
    }

    public void downloadFile(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        downloadFile();
    }

    public void getFileMetadata(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        getFileMetadata();
    }

    public void updateFileMetadata(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        updateFileMetadata();
    }

    public void getFileList(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        getFileList();
    }

    public void deleteFile(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        deleteFile();
    }

    private void deleteFile() {
        final String path = "test.jpg";
        System.out.println(String.format("path=%s", path));
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        Task<Void> deleteTask = storageReference.delete();
        try {
            deleteTask.addOnSuccessListener(aVoid -> mShowResultTv.setText("delete success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("delete failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFile() {
        final String path = "test.jpg";
        String fileName = "test.jpg";
        String agcSdkDirPath = getAGCSdkDirPath();
        final File file = new File(agcSdkDirPath, fileName);
        if (!file.exists()) {
            mShowResultTv.setText("file is not exist!");
            return;
        }
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        UploadTask uploadTask = storageReference.putFile(file);
        try {
            uploadTask.addOnSuccessListener(uploadResult -> mShowResultTv.setText("upload success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("upload failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile() {
        String fileName = "download_" + System.currentTimeMillis() + ".jpg";
        final String path = "test.jpg";
        String agcSdkDirPath = getAGCSdkDirPath();
        final File file = new File(agcSdkDirPath, fileName);
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        DownloadTask downloadTask = storageReference.getFile(file);
        try {
            downloadTask.addOnSuccessListener(downloadResult -> mShowResultTv.setText("download success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("download failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFileMetadata() {
        final String path = "test.jpg";
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        Task<FileMetadata> fileMetadataTask = storageReference.getFileMetadata();
        try {
            fileMetadataTask.addOnSuccessListener(metadata -> mShowResultTv.setText("getfilemetadata success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("getfilemetadata failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFileMetadata() {
        final String path = "test.jpg";
        FileMetadata fileMetadata = initFileMetadata();
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        Task<FileMetadata> fileMetadataTask = storageReference.updateFileMetadata(fileMetadata);
        try {
            fileMetadataTask.addOnSuccessListener(metadata -> mShowResultTv.setText("updatefilemetadata success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("updatefilemetadata failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFileList() {
        final String path = "test.jpg";
        StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
        Task<ListResult> listResultTask = null;
        listResultTask = storageReference.list(100);
        try {
            listResultTask.addOnSuccessListener(listResult -> mShowResultTv.setText("getfilelist success!"))
                    .addOnFailureListener(e -> mShowResultTv.setText("getfilelist failure!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileMetadata initFileMetadata() {
        FileMetadata metadata = new FileMetadata();
        metadata.setContentType("image/*");
        metadata.setCacheControl("no-cache");
        metadata.setContentEncoding("identity");
        metadata.setContentDisposition("inline");
        metadata.setContentLanguage("en");
        return metadata;
    }

    private String getAGCSdkDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AGCSdk/";
        System.out.println("path=" + path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }
}
