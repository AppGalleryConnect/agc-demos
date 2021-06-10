package com.huawei.agconnect.kotlindemo

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement
import com.huawei.agconnect.cloud.storage.core.FileMetadata
import com.huawei.agconnect.cloud.storage.core.ListResult
import com.huawei.hmf.tasks.Task
import java.io.File
import java.sql.DriverManager.println

class MainActivityKotlin : AppCompatActivity() {

    private var mAGCStorageManagement: AGCStorageManagement? = null
    private var mShowResultTv: TextView? = null
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mShowResultTv = findViewById(R.id.showResult)
        AGConnectInstance.initialize(applicationContext)
        login()
        ActivityCompat.requestPermissions(this, permissions, 1)
    }


    private fun initAGCStorageManagement() {
        mAGCStorageManagement = AGCStorageManagement.getInstance("Bucket Name")
        mShowResultTv!!.text = "initAGCStorageManagement success! "
    }

    private fun initChinaAGCStorageManagement() {
        val cnOptions = AGConnectOptionsBuilder().setRoutePolicy(AGCRoutePolicy.CHINA).build(this)
        val cnInstance = AGConnectInstance.buildInstance(cnOptions)
        mAGCStorageManagement = AGCStorageManagement.getInstance(cnInstance, "Bucket Name")
        mShowResultTv!!.text = "initAGCStorageManagement success! "
    }

    private fun login() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            println("already sign a user")
            return
        }
        AGConnectAuth.getInstance().signInAnonymously()
                .addOnSuccessListener { println("AGConnect OnSuccess") }
                .addOnFailureListener { e -> println("AGConnect OnFail: " + e.message) }
    }

    fun initAGCStorageManagement(view: View) {
        initAGCStorageManagement()
    }

    fun uploadFile(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        uploadFile()
    }

    fun downloadFile(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        downloadFile()
    }

    fun getFileMetadata(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        getFileMetadata()
    }

    fun updateFileMetadata(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        updateFileMetadata()
    }

    fun getFileList(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        getFileList()
    }

    fun deleteFile(view: View) {
        if (mAGCStorageManagement == null) {
            initAGCStorageManagement()
        }
        deleteFile()
    }

    private fun deleteFile() {
        val path = "test.jpg"
        println("path=%s$path")
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        val deleteTask = storageReference.delete()
        deleteTask.addOnSuccessListener { mShowResultTv!!.text = "delete success!" }
                .addOnFailureListener { e: Exception -> mShowResultTv!!.text = "delete failure! "+ e.message.toString() }
    }

    private fun uploadFile() {
        val path = "test.jpg"
        val fileName = "test.jpg"
        val agcSdkDirPath = agcSdkDirPath
        val file = File(agcSdkDirPath, fileName)
        if (!file.exists()) {
            mShowResultTv!!.text = "file is not exist!"
            return
        }
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        val uploadTask = storageReference.putFile(file)

        uploadTask.addOnSuccessListener { mShowResultTv!!.text = "upload success!" }
                .addOnFailureListener{ e: Exception -> mShowResultTv!!.text = "upload failure! "+ e.message.toString() }

    }

    private fun downloadFile() {
        val fileName = "download_" + System.currentTimeMillis() + ".jpg"
        val path = "test.jpg"
        val agcSdkDirPath = agcSdkDirPath
        val file = File(agcSdkDirPath, fileName)
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        val downloadTask = storageReference.getFile(file)

        downloadTask.addOnSuccessListener { mShowResultTv!!.text = "download success!" }
                .addOnFailureListener{ e: Exception -> mShowResultTv!!.text = "download failure! "+ e.message.toString() }
    }

    private fun getFileMetadata() {
        val path = "test.jpg"
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        val fileMetadataTask = storageReference.fileMetadata

        fileMetadataTask .addOnSuccessListener { mShowResultTv!!.text = "getfilemetadata success!" }
                .addOnFailureListener{ e: Exception -> mShowResultTv!!.text = "getfilemetadata failure! "+ e.message.toString() }
    }

    private fun updateFileMetadata() {
        val path = "test.jpg"
        val fileMetadata = initFileMetadata()
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        val fileMetadataTask = storageReference.updateFileMetadata(fileMetadata)

        fileMetadataTask.addOnSuccessListener { mShowResultTv!!.text = "updatefilemetadata success!" }
                .addOnFailureListener{ e: Exception -> mShowResultTv!!.text = "updatefilemetadata failure! "+ e.message.toString() }
    }

    private fun getFileList() {
        val path = "test.jpg"
        val storageReference = mAGCStorageManagement!!.getStorageReference(path)
        var listResultTask: Task<ListResult>? = null
        listResultTask = storageReference.list(100)

        listResultTask!!.addOnSuccessListener { mShowResultTv!!.text = "getfilelist success!" }
                .addOnFailureListener{ e: Exception -> mShowResultTv!!.text = "getfilelist failure! "+ e.message.toString() }
    }

    private fun initFileMetadata(): FileMetadata {
        val metadata = FileMetadata()
        metadata.contentType = "image/*"
        metadata.cacheControl = "no-cache"
        metadata.contentEncoding = "identity"
        metadata.contentDisposition = "inline"
        metadata.contentLanguage = "en"
        return metadata
    }

    private val agcSdkDirPath: String
        get() {
            val path = Environment.getExternalStorageDirectory().absolutePath + "/AGCSdk/"
            println("path=$path")
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return path
        }
}
