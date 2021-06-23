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

package com.huawei.agc.clouddb.quickstart.model;

import android.content.Context;
import android.util.Log;

import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.ListenerHandler;
import com.huawei.agconnect.cloud.database.OnSnapshotListener;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Proxying implementation of CloudDBZone.
 */
public class CloudDBZoneWrapper {
    private static final String TAG = "CloudDBZoneWrapper";

    private AGConnectCloudDB mCloudDB;

    private CloudDBZone mCloudDBZone;

    private ListenerHandler mRegister;

    private CloudDBZoneConfig mConfig;

    private UiCallBack mUiCallBack = UiCallBack.DEFAULT;

    /**
     * Mark max id of book info. id is the primary key of {@link BookInfo}, so we must provide an value for it
     * when upserting to database.
     */
    private int mBookIndex = 0;

    private ReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();

    /**
     * Monitor data change from database. Update book info list if data have changed
     */
    private OnSnapshotListener<BookInfo> mSnapshotListener = new OnSnapshotListener<BookInfo>() {
        @Override
        public void onSnapshot(CloudDBZoneSnapshot<BookInfo> cloudDBZoneSnapshot, AGConnectCloudDBException e) {
            if (e != null) {
                Log.w(TAG, "onSnapshot: " + e.getMessage());
                return;
            }
            CloudDBZoneObjectList<BookInfo> snapshotObjects = cloudDBZoneSnapshot.getSnapshotObjects();
            List<BookInfo> bookInfoList = new ArrayList<>();
            try {
                if (snapshotObjects != null) {
                    while (snapshotObjects.hasNext()) {
                        BookInfo bookInfo = snapshotObjects.next();
                        bookInfoList.add(bookInfo);
                        updateBookIndex(bookInfo);
                    }
                }
                mUiCallBack.onSubscribe(bookInfoList);
            } catch (AGConnectCloudDBException snapshotException) {
                Log.w(TAG, "onSnapshot:(getObject) " + snapshotException.getMessage());
            } finally {
                cloudDBZoneSnapshot.release();
            }
        }
    };

    public CloudDBZoneWrapper() {
        mCloudDB = AGConnectCloudDB.getInstance();
    }

    /**
     * Init AGConnectCloudDB in Application
     *
     * @param context application context
     */
    public static void initAGConnectCloudDB(Context context) {
        AGConnectCloudDB.initialize(context);
    }

    /**
     * Call AGConnectCloudDB.createObjectType to init schema
     */
    public void createObjectType() {
        try {
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo());
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "createObjectType: " + e.getMessage());
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage
     */
    public void openCloudDBZone() {
        mConfig = new CloudDBZoneConfig("QuickStartDemo",
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC);
        mConfig.setPersistenceEnabled(true);
        try {
            mCloudDBZone = mCloudDB.openCloudDBZone(mConfig, true);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "openCloudDBZone: " + e.getMessage());
        }
    }

    public void openCloudDBZoneV2() {
        mConfig = new CloudDBZoneConfig("QuickStartDemo",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC);
        mConfig.setPersistenceEnabled(true);
        Task<CloudDBZone> openDBZoneTask = mCloudDB.openCloudDBZone2(mConfig, true);
        openDBZoneTask.addOnSuccessListener(new OnSuccessListener<CloudDBZone>() {
            @Override
            public void onSuccess(CloudDBZone cloudDBZone) {
                Log.i(TAG, "Open cloudDBZone success");
                mCloudDBZone = cloudDBZone;
                // Add subscription after opening cloudDBZone success
                addSubscription();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w(TAG, "Open cloudDBZone failed for " + e.getMessage());
            }
        });
    }

    /**
     * Call AGConnectCloudDB.closeCloudDBZone
     */
    public void closeCloudDBZone() {
        try {
            mRegister.remove();
            mCloudDB.closeCloudDBZone(mCloudDBZone);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "closeCloudDBZone: " + e.getMessage());
        }
    }

    /**
     * Call AGConnectCloudDB.deleteCloudDBZone
     */
    public void deleteCloudDBZone() {
        try {
            mCloudDB.deleteCloudDBZone(mConfig.getCloudDBZoneName());
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "deleteCloudDBZone: " + e.getMessage());
        }
    }

    /**
     * Add a callback to update book info list
     *
     * @param uiCallBack callback to update book list
     */
    public void addCallBacks(UiCallBack uiCallBack) {
        mUiCallBack = uiCallBack;
    }

    /**
     * Add mSnapshotListener to monitor data changes from storage
     */
    public void addSubscription() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }

        try {
            CloudDBZoneQuery<BookInfo> snapshotQuery = CloudDBZoneQuery.where(BookInfo.class)
                    .equalTo(BookEditFields.SHADOW_FLAG, true);
            mRegister = mCloudDBZone.subscribeSnapshot(snapshotQuery,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY, mSnapshotListener);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "subscribeSnapshot: " + e.getMessage());
        }
    }

    /**
     * Query all books in storage from cloud side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     */
    public void queryAllBooks() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }
        Task<CloudDBZoneSnapshot<BookInfo>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(BookInfo.class),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<BookInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<BookInfo> snapshot) {
                processQueryResult(snapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                mUiCallBack.updateUiOnError("Query book list from cloud failed");
            }
        });
    }

    /**
     * Query books with condition
     *
     * @param query query condition
     */
    public void queryBooks(CloudDBZoneQuery<BookInfo> query) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }

        Task<CloudDBZoneSnapshot<BookInfo>> queryTask = mCloudDBZone.executeQuery(query,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<BookInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<BookInfo> snapshot) {
                processQueryResult(snapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                mUiCallBack.updateUiOnError("Query failed");
            }
        });
    }

    private void processQueryResult(CloudDBZoneSnapshot<BookInfo> snapshot) {
        CloudDBZoneObjectList<BookInfo> bookInfoCursor = snapshot.getSnapshotObjects();
        List<BookInfo> bookInfoList = new ArrayList<>();
        try {
            while (bookInfoCursor.hasNext()) {
                BookInfo bookInfo = bookInfoCursor.next();
                bookInfoList.add(bookInfo);
            }
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "processQueryResult: " + e.getMessage());
        } finally {
            snapshot.release();
        }
        mUiCallBack.onAddOrQuery(bookInfoList);
    }

    /**
     * Upsert bookinfo
     *
     * @param bookInfo bookinfo added or modified from local
     */
    public void upsertBookInfos(BookInfo bookInfo) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }

        Task<Integer> upsertTask = mCloudDBZone.executeUpsert(bookInfo);
        upsertTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer cloudDBZoneResult) {
                Log.i(TAG, "Upsert " + cloudDBZoneResult + " records");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                mUiCallBack.updateUiOnError("Insert book info failed");
            }
        });
    }

    /**
     * Delete bookinfo
     *
     * @param bookInfoList books selected by user
     */
    public void deleteBookInfos(List<BookInfo> bookInfoList) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }

        Task<Integer> deleteTask = mCloudDBZone.executeDelete(bookInfoList);
        if (deleteTask.getException() != null) {
            mUiCallBack.updateUiOnError("Delete book info failed");
            return;
        }
        mUiCallBack.onDelete(bookInfoList);
    }

    private void updateBookIndex(BookInfo bookInfo) {
        try {
            mReadWriteLock.writeLock().lock();
            if (mBookIndex < bookInfo.getId()) {
                mBookIndex = bookInfo.getId();
            }
        } finally {
            mReadWriteLock.writeLock().unlock();
        }
    }

    /**
     * Get max id of bookinfos
     *
     * @return max book info id
     */
    public int getBookIndex() {
        try {
            mReadWriteLock.readLock().lock();
            return mBookIndex;
        } finally {
            mReadWriteLock.readLock().unlock();
        }
    }

    /**
     * Call back to update ui in HomePageFragment
     */
    public interface UiCallBack {
        UiCallBack DEFAULT = new UiCallBack() {
            @Override
            public void onAddOrQuery(List<BookInfo> bookInfoList) {
                Log.i(TAG, "Using default onAddOrQuery");
            }

            @Override
            public void onSubscribe(List<BookInfo> bookInfoList) {
                Log.i(TAG, "Using default onSubscribe");
            }

            @Override
            public void onDelete(List<BookInfo> bookInfoList) {
                Log.i(TAG, "Using default onDelete");
            }

            @Override
            public void updateUiOnError(String errorMessage) {
                Log.i(TAG, "Using default updateUiOnError");
            }
        };

        void onAddOrQuery(List<BookInfo> bookInfoList);

        void onSubscribe(List<BookInfo> bookInfoList);

        void onDelete(List<BookInfo> bookInfoList);

        void updateUiOnError(String errorMessage);
    }
}
