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

package com.huawei.agc.clouddb.quickstart.utils;

import com.huawei.agc.clouddb.quickstart.model.BookInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * BookUtils class is used to set up book information
 */
public class BookUtils {
    public final BookInfo bookInfo1;
    public final BookInfo bookInfo2;
    private final BookInfo bookInfo3;
    private final BookInfo bookInfo4;
    private final BookInfo bookInfo5;

    public BookUtils() {
        bookInfo1 = new BookInfo();
        bookInfo1.setId(1);
        bookInfo1.setBookName("Harry Potter");
        bookInfo1.setAuthor("J. K. Rowling");
        bookInfo1.setPublisher("Bloomsbury Publishing (UK)");
        bookInfo1.setPublishTime(DateUtils.parseDate("1997-07-26 00:00:00 000"));
        bookInfo1.setPrice(80.99);

        bookInfo2 = new BookInfo();
        bookInfo2.setId(2);
        bookInfo2.setBookName("Murder on the Orient Express");
        bookInfo2.setAuthor("Agatha Christie");
        bookInfo2.setPublisher("Collins Crime Club");
        bookInfo2.setPublishTime(DateUtils.parseDate("1934-01-01 00:00:00 000"));
        bookInfo2.setPrice(50.99);

        bookInfo3 = new BookInfo();
        bookInfo3.setId(3);
        bookInfo3.setBookName("Les Fleurs du mal");
        bookInfo3.setAuthor("Charles Pierre Baudelaire");
        bookInfo3.setPublisher("Auguste Poulet-Malassis");
        bookInfo3.setPublishTime(DateUtils.parseDate("1857-01-01 00:00:00 000"));
        bookInfo3.setPrice(30.99);

        bookInfo4 = new BookInfo();
        bookInfo4.setId(4);
        bookInfo4.setBookName("The Moon and Sixpence");
        bookInfo4.setAuthor("William Somerset Maugham");
        bookInfo4.setPublisher("Heinemann UK");
        bookInfo4.setPublishTime(DateUtils.parseDate("1919-04-15 00:00:00 000"));
        bookInfo4.setPrice(40.99);

        bookInfo5 = new BookInfo();
        bookInfo5.setId(5);
        bookInfo5.setBookName("The Red And Black");
        bookInfo5.setAuthor("Stendhal");
        bookInfo5.setPublisher("A. Levasseur");
        bookInfo5.setPublishTime(DateUtils.parseDate("1830-11-01 00:00:00 000"));
        bookInfo5.setPrice(10.0);
    }

    /**
     * This method is used to set up a list of bookInfo objects.
     * These objects are records which will be upserted.
     *
     * @return List<BookInfo> a list of bookInfo objects.
     */
    public List<BookInfo> getUpsertList() {
        List<BookInfo> upsertList = new ArrayList<>();
        upsertList.add(bookInfo1);
        upsertList.add(bookInfo2);
        upsertList.add(bookInfo3);
        upsertList.add(bookInfo4);
        upsertList.add(bookInfo5);
        return upsertList;
    }

    /**
     * This method is used to set up a list of bookInfo objects.
     * These objects are records which will be deleted.
     *
     * @return List<BookInfo> a list of bookInfo objects.
     */
    public List<BookInfo> getDeleteList() {
        List<BookInfo> deleteList = new ArrayList<>();
        deleteList.add(bookInfo1);
        deleteList.add(bookInfo4);
        return deleteList;
    }
}
