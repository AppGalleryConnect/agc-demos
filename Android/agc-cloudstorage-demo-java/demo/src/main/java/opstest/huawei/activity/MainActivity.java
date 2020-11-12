package opstest.huawei.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.Tasks;

import java.io.File;

import opstest.huawei.R;

public class MainActivity extends AppCompatActivity {

    private AGCStorageManagement mAGCStorageManagement;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AGConnectInstance.initialize(getApplicationContext());
        login();
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    private void initAGCStorageManagement() {
        mAGCStorageManagement = AGCStorageManagement.getInstance();
    }

    private void login() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            System.out.println("already signin a user");
            return;
        }
        AGConnectAuth.getInstance().signInAnonymously().addOnSuccessListener(new OnSuccessListener<SignInResult>() {
            @Override
            public void onSuccess(SignInResult signInResult) {
                System.out.println("AGConnect OnSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                System.out.println("AGConnect OnFail: " + e.getMessage());
            }
        });

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

    public void deleteFile(View view) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement();
        }
        deleteFile();
    }

    private void deleteFile() {
        final String path = "test.png";
        System.out.println(String.format("path=%s", path));

        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
                Task<Void> deleteTask = storageReference.delete();
                try {
                    Tasks.await(deleteTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void uploadFile() {

        final String path = "test.png";
        String fileName = "test.png";
        String agcSdkDirPath = getAGCSdkDirPath();
        final File file = new File(agcSdkDirPath, fileName);
        if (!file.exists()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
                UploadTask uploadTask = storageReference.putFile(file);
                try {
                    UploadTask.UploadResult uploadResult = Tasks.await(uploadTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void downloadFile() {

        String fileName = "download_" + System.currentTimeMillis() + ".png";
        final String path = "test.png";
        String agcSdkDirPath = getAGCSdkDirPath();
        final File file = new File(agcSdkDirPath, fileName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageReference storageReference = mAGCStorageManagement.getStorageReference(path);
                DownloadTask downloadTask = storageReference.getFile(file);
                try {
                    DownloadTask.DownloadResult downloadResult = Tasks.await(downloadTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

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
