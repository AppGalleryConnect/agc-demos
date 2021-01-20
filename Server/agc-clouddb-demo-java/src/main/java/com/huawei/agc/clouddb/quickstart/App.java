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

package com.huawei.agc.clouddb.quickstart;

import com.huawei.agc.clouddb.quickstart.model.BookEditFields;
import com.huawei.agc.clouddb.quickstart.model.BookInfo;
import com.huawei.agc.clouddb.quickstart.model.CloudDBZoneWrapper;
import com.huawei.agc.clouddb.quickstart.utils.BookUtils;
import com.huawei.agc.clouddb.quickstart.utils.DateUtils;
import com.huawei.agconnect.server.clouddb.exception.AGConnectCloudDBException;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneQuery;

public class App {
    public static void main(String[] args) {
        CloudDBZoneWrapper cloudDBZoneWrapper = new CloudDBZoneWrapper();
        BookUtils bookUtils = new BookUtils();

        // delete all books
        cloudDBZoneWrapper.deleteAllBooks(System.out::println);

        // upsert one book
        cloudDBZoneWrapper.upsertBookInfo(bookUtils.bookInfo1, System.out::println);

        // upsert a list of books
        cloudDBZoneWrapper.upsertBookInfos(bookUtils.getUpsertList(), System.out::println);

        // query all books
        System.out.println("Querying for all books in the CloudDB: ");
        cloudDBZoneWrapper.queryAllBooks(System.out::println);

        // query books which prices are higher than 55
        try {
            CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class)
                    .greaterThan(BookEditFields.PRICE, 55.0);
            System.out.println("Querying books which price is higher than 55: ");
            cloudDBZoneWrapper.queryBooks(query, System.out::println);
        } catch (AGConnectCloudDBException e) {
            e.printStackTrace();
        }

        // query the second most expensive books
        cloudDBZoneWrapper.queryWithOrder(System.out::println);

        // pagination query: get books before record "The Red And Black", order by book name and price.
        cloudDBZoneWrapper.endAtQuery(System.out::println);

        // Get the average price of all books
        cloudDBZoneWrapper.averageQuery(System.out::println);

        // delete one book
        cloudDBZoneWrapper.deleteBookInfo(bookUtils.bookInfo2, System.out::println);

        // Use transaction to delete books which are published earlier than 1900
        try {
            CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class)
                    .lessThan(BookEditFields.PUBLISH_TIME, DateUtils.parseDate("1900-01-01 00:00:00 000"));
            System.out.println("Delete books which are published earlier than 1900: ");
            cloudDBZoneWrapper.deleteOverdueBooks(query, System.out::println);
        } catch (AGConnectCloudDBException e) {
            e.printStackTrace();
        }

        // delete a list of books
        cloudDBZoneWrapper.deleteBookInfos(bookUtils.getDeleteList(), System.out::println);

        // delete user key
        cloudDBZoneWrapper.deleteUserKey("123456789", System.out::println);
    }
}
