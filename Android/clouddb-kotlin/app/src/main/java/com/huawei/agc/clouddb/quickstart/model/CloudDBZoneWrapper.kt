/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.agc.clouddb.quickstart.model

import android.content.Context
import android.util.Log
import com.huawei.agconnect.cloud.database.*
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import java.util.*
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Proxying implementation of CloudDBZone.
 */
class CloudDBZoneWrapper {
    private val mCloudDB: AGConnectCloudDB = AGConnectCloudDB.getInstance()
    private var mCloudDBZone: CloudDBZone? = null
    private var mRegister: ListenerHandler? = null
    private var mConfig: CloudDBZoneConfig? = null
    private var mUiCallBack = UiCallBack.DEFAULT

    /**
     * Mark max id of book info. id is the primary key of [BookInfo], so we must provide an value for it
     * when upserting to database.
     */
    private var mBookIndex = 0
    private val mReadWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    /**
     * Monitor data change from database. Update book info list if data have changed
     */
    private val mSnapshotListener = OnSnapshotListener<BookInfo> { cloudDBZoneSnapshot, e ->
        if (e != null) {
            Log.w(TAG, "onSnapshot: " + e.message)
            return@OnSnapshotListener
        }
        val snapshotObjects = cloudDBZoneSnapshot.snapshotObjects
        val bookInfoList: MutableList<BookInfo> = ArrayList()
        try {
            if (snapshotObjects != null) {
                while (snapshotObjects.hasNext()) {
                    val bookInfo = snapshotObjects.next()
                    bookInfoList.add(bookInfo)
                    updateBookIndex(bookInfo)
                }
            }
            mUiCallBack.onSubscribe(bookInfoList)
        } catch (snapshotException: AGConnectCloudDBException) {
            Log.w(TAG, "onSnapshot:(getObject) " + snapshotException.message)
        } finally {
            cloudDBZoneSnapshot.release()
        }
    }

    /**
     * Call AGConnectCloudDB.createObjectType to init schema
     */
    fun createObjectType() {
        try {
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "createObjectType: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage
     */
    fun openCloudDBZone() {
        mConfig = CloudDBZoneConfig("QuickStartDemo",
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
        mConfig!!.persistenceEnabled = true
        try {
            mCloudDBZone = mCloudDB.openCloudDBZone(mConfig!!, true)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "openCloudDBZone: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone2 to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage.
     * AGConnectCloudDB.openCloudDBZone2 is an asynchronous method, we can add
     * OnSuccessListener/OnFailureListener to receive the result for opening cloudDBZone
     */
    fun openCloudDBZoneV2() {
        mConfig = CloudDBZoneConfig("QuickStartDemo",
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
        mConfig!!.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig!!, true)
        task.addOnSuccessListener {
            Log.i(TAG, "Open cloudDBZone success")
            mCloudDBZone = it
            addSubscription()
        }.addOnFailureListener {
            Log.w(TAG, "Open cloudDBZone failed for " + it.message)
        }
    }

    /**
     * Call AGConnectCloudDB.closeCloudDBZone
     */
    fun closeCloudDBZone() {
        try {
            mRegister!!.remove()
            mCloudDB.closeCloudDBZone(mCloudDBZone)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "closeCloudDBZone: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.deleteCloudDBZone
     */
    fun deleteCloudDBZone() {
        try {
            mCloudDB.deleteCloudDBZone(mConfig!!.cloudDBZoneName)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "deleteCloudDBZone: " + e.message)
        }
    }

    /**
     * Add a callback to update book info list
     *
     * @param uiCallBack callback to update book list
     */
    fun addCallBacks(uiCallBack: UiCallBack) {
        mUiCallBack = uiCallBack
    }

    /**
     * Add mSnapshotListener to monitor data changes from storage
     */
    private fun addSubscription() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        try {
            val snapshotQuery = CloudDBZoneQuery.where(BookInfo::class.java)
                    .equalTo(BookEditFields.SHADOW_FLAG, true)
            mRegister = mCloudDBZone!!.subscribeSnapshot(snapshotQuery,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY, mSnapshotListener)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "subscribeSnapshot: " + e.message)
        }
    }

    /**
     * Query all books in storage from cloud side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     */
    fun queryAllBooks() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val queryTask = mCloudDBZone!!.executeQuery(
                CloudDBZoneQuery.where(BookInfo::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        queryTask.addOnSuccessListener { snapshot -> processQueryResult(snapshot) }
                .addOnFailureListener {
                    mUiCallBack.updateUiOnError("Query book list from cloud failed")
                }
    }

    /**
     * Query books with condition
     *
     * @param query query condition
     */
    fun queryBooks(query: CloudDBZoneQuery<BookInfo>) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }

        val queryTask = mCloudDBZone!!.executeQuery(query,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        queryTask.addOnSuccessListener { snapshot -> processQueryResult(snapshot) }
                .addOnFailureListener { mUiCallBack.updateUiOnError("Query failed") }
    }

    private fun processQueryResult(snapshot: CloudDBZoneSnapshot<BookInfo>) {
        val bookInfoCursor = snapshot.snapshotObjects
        val bookInfoList: MutableList<BookInfo> = ArrayList()
        try {
            while (bookInfoCursor.hasNext()) {
                val bookInfo = bookInfoCursor.next()
                bookInfoList.add(bookInfo)
            }
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "processQueryResult: " + e.message)
        } finally {
            snapshot.release()
        }
        mUiCallBack.onAddOrQuery(bookInfoList)
    }

    /**
     * Upsert bookinfo
     *
     * @param bookInfo bookinfo added or modified from local
     */
    fun upsertBookInfos(bookInfo: BookInfo?) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(bookInfo!!)
        upsertTask.addOnSuccessListener { cloudDBZoneResult ->
            Log.i(TAG, "Upsert $cloudDBZoneResult records")
        }.addOnFailureListener {
            mUiCallBack.updateUiOnError("Insert book info failed")
        }
    }

    /**
     * Delete bookinfo
     *
     * @param bookInfoList books selected by user
     */
    fun deleteBookInfos(bookInfoList: List<BookInfo?>?) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val deleteTask = mCloudDBZone!!.executeDelete(bookInfoList!!)
        if (deleteTask.exception != null) {
            mUiCallBack.updateUiOnError("Delete book info failed")
            return
        }
        mUiCallBack.onDelete(bookInfoList)
    }

    private fun updateBookIndex(bookInfo: BookInfo) {
        try {
            mReadWriteLock.writeLock().lock()
            if (mBookIndex < bookInfo.id) {
                mBookIndex = bookInfo.id
            }
        } finally {
            mReadWriteLock.writeLock().unlock()
        }
    }

    /**
     * Get max id of bookinfos
     *
     * @return max book info id
     */
    val bookIndex: Int
        get() = try {
            mReadWriteLock.readLock().lock()
            mBookIndex
        } finally {
            mReadWriteLock.readLock().unlock()
        }

    /**
     * Call back to update ui in HomePageFragment
     */
    interface UiCallBack {
        fun onAddOrQuery(bookInfoList: List<BookInfo>)
        fun onSubscribe(bookInfoList: List<BookInfo>?)
        fun onDelete(bookInfoList: List<BookInfo?>?)
        fun updateUiOnError(errorMessage: String?)

        companion object {
            val DEFAULT: UiCallBack = object : UiCallBack {
                override fun onAddOrQuery(bookInfoList: List<BookInfo>) {
                    Log.i(TAG, "Using default onAddOrQuery")
                }

                override fun onSubscribe(bookInfoList: List<BookInfo>?) {
                    Log.i(TAG, "Using default onSubscribe")
                }

                override fun onDelete(bookInfoList: List<BookInfo?>?) {
                    Log.i(TAG, "Using default onDelete")
                }

                override fun updateUiOnError(errorMessage: String?) {
                    Log.i(TAG, "Using default updateUiOnError")
                }
            }
        }
    }

    companion object {
        private const val TAG = "CloudDBZoneWrapper"

        /**
         * Init AGConnectCloudDB in Application
         *
         * @param context application context
         */
        fun initAGConnectCloudDB(context: Context?) {
            AGConnectCloudDB.initialize(context!!)
        }
    }

}