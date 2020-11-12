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

/**
 * Constants to mark book info field
 */
public interface BookEditFields {
    String BOOK_ID = "id";
    String BOOK_NAME = "bookName";
    String AUTHOR = "author";
    String PRICE = "price";
    String LOWEST_PRICE = "lowest_price";
    String HIGHEST_PRICE = "highest_price";
    String PUBLISHER = "publisher";
    String PUBLISH_TIME = "publishTime";
    String SHOW_COUNT = "showCount";

    String EDIT_MODE = "EDIT_MODE";

    /**
     * To mark it's in add mode or in modify mode
     */
    enum EditMode {
        ADD,
        MODIFY
    }
}
