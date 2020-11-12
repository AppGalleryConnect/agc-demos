package com.huawei.agcsdktest2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.ListResult;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.Tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private AGCStorageManagement storageManagement;

    private final String temporaryPathPrefix = UUID.randomUUID().toString();

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        if (null == storageManagement) {
            AGConnectInstance.initialize(InstrumentationRegistry.getInstrumentation().getTargetContext());
            storageManagement = AGCStorageManagement.getInstance("agc-object-test2-bj4");
        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.huawei.agcsdktest2", appContext.getPackageName());
    }

    @Test
    public void getObjejctList() throws ExecutionException, InterruptedException {
        Task<ListResult> task = storageManagement.getStorageReference("download/download.txt").list(2);
        ListResult result = Tasks.await(task);
        System.out.println("getObjejctList: " + result.getFileList().get(0).getPath());
        task = storageManagement.getStorageReference("download/").list(10);
        result = Tasks.await(task);
        for (int i = 0; i < result.getFileList().size(); i++) {
            System.out.println("getObjejctList2: " + result.getFileList().get(i).getPath());
        }

        for (int i = 0; i < result.getDirList().size(); i++) {
            System.out.println("getObjejctList3: " + result.getDirList().get(i).getPath());
        }
    }

}
