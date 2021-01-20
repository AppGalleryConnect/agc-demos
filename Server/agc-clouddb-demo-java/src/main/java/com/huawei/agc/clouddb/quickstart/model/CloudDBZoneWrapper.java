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

import com.huawei.agconnect.server.clouddb.exception.AGConnectCloudDBException;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneConfig;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneQuery;
import com.huawei.agconnect.server.clouddb.service.AGConnectCloudDB;
import com.huawei.agconnect.server.clouddb.service.CloudDBZone;
import com.huawei.agconnect.server.clouddb.service.impl.CloudDBZoneSnapshot;
import com.huawei.agconnect.server.clouddb.service.impl.Transaction;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Proxying implementation of AGCCloudDB.
 */
public class CloudDBZoneWrapper {
    private CloudDBZone mCloudDBZone;

    private AGConnectCloudDB agConnectCloudDB;

    public CloudDBZoneWrapper() {
        try {
            /*
             * To Integrate the server sdk, a credential file should be used.
             * Change the value of 'credentialPath' to the path of the credential file.
             * */
            String credentialPath = "/path/agc-apiclient-xxx-xxx.json";
            AGCClient.initialize(AGCParameter.builder()
                    .setCredential(CredentialParser.toCredential(credentialPath))
                    .build());
            CloudDBZoneConfig cloudDBZoneConfig = new CloudDBZoneConfig("QuickStartDemo");
            agConnectCloudDB = AGConnectCloudDB.getInstance();
            mCloudDBZone = agConnectCloudDB.openCloudDBZone(cloudDBZoneConfig);
        } catch (AGCException e) {
            System.out.println("Constructor: " + e.getMessage());
        }
    }

    /**
     * Upsert a BookInfo record
     *
     * @param bookInfo BookInfo record that needs to be added or modified in cloud side.
     * @param callback the callback method.
     */
    public void upsertBookInfo(BookInfo bookInfo, Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = mCloudDBZone.executeUpsert(bookInfo);
            callback.processResult("The number of upserted books is: " + result.get());
        } catch (AGConnectCloudDBException | ExecutionException | InterruptedException e) {
            System.out.println("upsertBookInfo: " + e.getMessage());
        }
    }

    /**
     * Upsert BookInfo records
     *
     * @param bookInfos The list of BookInfo that needs to be added or modified in cloud side.
     * @param callback  the callback method.
     */
    public void upsertBookInfos(List<BookInfo> bookInfos, Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = mCloudDBZone.executeUpsert(bookInfos);
            callback.processResult("The number of upserted books is: " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("upsertBookInfos: " + e.getMessage());
        }
    }

    /**
     * Delete a BookInfo record.
     *
     * @param bookInfo BookInfo record that needs to be deleted in cloud side.
     * @param callback the callback method.
     */
    public void deleteBookInfo(BookInfo bookInfo, Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = mCloudDBZone.executeDelete(bookInfo);
            callback.processResult("The number of deleted books is: " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("deleteBookInfo: " + e.getMessage());
        }
    }

    /**
     * Delete BookInfo records.
     *
     * @param bookInfos The list of BookInfo that needs to be deleted in cloud side.
     * @param callback  the callback method.
     */
    public void deleteBookInfos(List<BookInfo> bookInfos, Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = mCloudDBZone.executeDelete(bookInfos);
            callback.processResult("The number of deleted books is: " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("deleteBookInfos: " + e.getMessage());
        }
    }

    /**
     * Delete all the BookInfo records.
     *
     * @param callback the callback method.
     */
    public void deleteAllBooks(Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = mCloudDBZone.executeDeleteAll(BookInfo.class);
            callback.processResult("The number of deleted books is: " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("deleteAllBooks: " + e.getMessage());
        }
    }

    /**
     * Query all books in storage from cloud side.
     *
     * @param callback the callback method.
     */
    public void queryAllBooks(Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<CloudDBZoneSnapshot<BookInfo>> result =
                    mCloudDBZone.executeQuery(CloudDBZoneQuery.where(BookInfo.class));
            CloudDBZoneSnapshot<BookInfo> snapshot = result.get();
            processQueryResult(snapshot, callback);
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("queryAllBooks: " + e.getMessage());
        }
    }

    /**
     * Query books with conditions.
     *
     * @param query    query conditions.
     * @param callback the callback method.
     */
    public void queryBooks(CloudDBZoneQuery<BookInfo> query, Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CompletableFuture<CloudDBZoneSnapshot<BookInfo>> result = mCloudDBZone.executeQuery(query);
            CloudDBZoneSnapshot<BookInfo> snapshot = result.get();
            processQueryResult(snapshot, callback);
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("queryBooks: " + e.getMessage());
        }
    }

    private void processQueryResult(CloudDBZoneSnapshot<BookInfo> snapshot, Callback callback) {
        List<BookInfo> BookInfos = snapshot.getSnapshotObjects();
        StringBuilder stringBuilder = new StringBuilder();
        for (BookInfo bookInfo : BookInfos) {
            stringBuilder.append("ID: ").append(bookInfo.getId()).append(", Book Name: ").append(bookInfo.getBookName())
                    .append(", Author: ").append(bookInfo.getAuthor()).append(", Price: ").append(bookInfo.getPrice())
                    .append(", Publisher: ").append(bookInfo.getPublisher()).append(", PublishTime: ")
                    .append(bookInfo.getPublishTime()).append(System.lineSeparator());
        }
        callback.processResult(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
    }

    /**
     * Query all books and sort them in descending order by price.
     * And get the top 2 results.
     *
     * @param callback the callback method.
     */
    public void queryWithOrder(Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        try {
            CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class);
            query.orderByDesc(BookEditFields.PRICE);
            query.limit(2);
            CompletableFuture<CloudDBZoneSnapshot<BookInfo>> result = mCloudDBZone.executeQuery(query);
            System.out.println("Querying for the second most expensive books: ");
            processQueryResult(result.get(), callback);
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("queryWithOrder: " + e.getMessage());
        }
    }

    /**
     * Get the average price of all books.
     *
     * @param callback the callback method.
     */
    public void averageQuery(Callback callback) {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class);
        try {
            CompletableFuture<Double> result = mCloudDBZone.executeAverageQuery(query, BookEditFields.PRICE);
            callback.processResult("The average price of all books is : " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("averageQuery: " + e.getMessage());
        }
    }

    /**
     * Query all books and sort them in ascending order by price.
     * And end at the book "The Red And Black".
     *
     * @param callback the callback method.
     */
    public void endAtQuery(Callback callback) {
        BookInfo book = new BookInfo();
        book.setId(5);
        book.setBookName("The Red And Black");
        book.setPrice(10.0);
        try {
            CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class)
                    .orderByAsc(BookEditFields.BOOK_NAME)
                    .orderByAsc(BookEditFields.PRICE).endAt(book);
            System.out.println("Querying for books records order by book name and price, end at book 'The Red And Black': ");
            queryBooks(query, callback);
        } catch (AGConnectCloudDBException e) {
            System.out.println("endAtQuery: " + e.getMessage());
        }
    }

    /**
     * Use transaction to delete overdue books.
     *
     * @param query    query conditions.
     * @param callback the callback method.
     */
    public void deleteOverdueBooks(CloudDBZoneQuery<BookInfo> query, Callback callback) throws AGConnectCloudDBException {
        Objects.requireNonNull(mCloudDBZone, "mCloudDBZone is null, try re-initialize it");
        Transaction.Function function = transaction -> {
            try {
                List<BookInfo> bookInfos = transaction.executeQuery(query);
                StringBuilder stringBuilder = new StringBuilder();
                for (BookInfo bookInfo : bookInfos) {
                    stringBuilder.append("Book Name: ").append(bookInfo.getBookName()).append(", PublishTime: ")
                            .append(bookInfo.getPublishTime()).append(System.lineSeparator());
                }
                callback.processResult(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
                transaction.executeDelete(bookInfos);
            } catch (AGConnectCloudDBException e) {
                System.out.println("deleteOverdueBooks: " + e.getMessage());
                return false;
            }
            return true;
        };
        mCloudDBZone.runTransaction(function);
    }

    /**
     * Use this method to delete userKey according to userId.
     *
     * @param userId   the userId of the user.
     * @param callback the callback method.
     */
    public void deleteUserKey(String userId, Callback callback) {
        Objects.requireNonNull(agConnectCloudDB, "agConnectCloudDB is null, try re-initialize it");
        try {
            CompletableFuture<Integer> result = agConnectCloudDB.executeDeleteUserKey(userId);
            callback.processResult("The number of deleted userKey is: " + result.get());
        } catch (AGConnectCloudDBException | InterruptedException | ExecutionException e) {
            System.out.println("deleteUserKey: " + e.getMessage());
        }
    }
}
