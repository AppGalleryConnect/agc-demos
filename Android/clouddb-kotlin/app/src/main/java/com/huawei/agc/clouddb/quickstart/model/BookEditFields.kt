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

/**
 * Constants to mark book info field
 */
interface BookEditFields {
    /**
     * To mark it's in add mode or in modify mode
     */
    enum class EditMode {
        ADD, MODIFY
    }

    companion object {
        const val BOOK_ID = "id"
        const val BOOK_NAME = "bookName"
        const val AUTHOR = "author"
        const val PRICE = "price"
        const val SHADOW_FLAG = "shadowFlag"
        const val LOWEST_PRICE = "lowest_price"
        const val HIGHEST_PRICE = "highest_price"
        const val PUBLISHER = "publisher"
        const val PUBLISH_TIME = "publishTime"
        const val SHOW_COUNT = "showCount"
        const val EDIT_MODE = "EDIT_MODE"
    }
}
