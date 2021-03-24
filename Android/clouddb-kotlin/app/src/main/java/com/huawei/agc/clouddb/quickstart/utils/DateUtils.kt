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
package com.huawei.agc.clouddb.quickstart.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Parse date to format as we need
 */
object DateUtils {
    /**
     * Format date to string in yyyy-MM-dd
     * @param date time input by caller
     * @return format string
     */
    fun formatDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(date.time)
    }

    /**
     * Parse date from an input string. Return current time if parsed failed
     * @param dateStr input date string
     * @return date from date string
     */
    fun parseDate(dateStr: String?): Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            return simpleDateFormat.parse(dateStr!!)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date(System.currentTimeMillis())
    }
}
