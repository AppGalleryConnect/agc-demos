/*
        Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
using Java.Text;
using Java.Util;
using System;

namespace XamarinHmsCloudDBDemo.Utils
{
    /// <summary>
    /// Parse date to format as we need
    /// </summary>
    public class DateUtils
    {
        /// <summary>
        /// Format date to string in yyyy-MM-dd.
        /// </summary>
        /// <param name="date">time input by caller</param>
        /// <returns>format string</returns>
        public static string FormatDate(Date date)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.Default);
            return simpleDateFormat.Format(date.Time);
        }

        /// <summary>
        /// Parse date from an input string. Return current time if parsed failed.
        /// </summary>
        /// <param name="dateStr">input date string</param>
        /// <returns>date from date string</returns>
        public static Date ParseDate(String dateStr)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.Default);
            try
            {
                return simpleDateFormat.Parse(dateStr);
            }
            catch (ParseException e)
            {
                e.PrintStackTrace();
            }
            return new Date(Java.Lang.JavaSystem.CurrentTimeMillis());
        }
    }
}