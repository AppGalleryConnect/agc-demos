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
namespace XamarinHmsCloudDBDemo.Model
{
    /// <summary>
    /// Constants to mark book info field.
    /// </summary>
    public static class BookEditFields
    {
        public const string BookID = "id";
        public const string BookName = "bookName";
        public const string Author = "author";
        public const string Price = "price";
        public const string ShadowFlag = "shadowFlag";
        public const string LowestPrice = "lowest_price";
        public const string HighestPrice = "highest_price";
        public const string Publisher = "publisher";
        public const string PublishTime = "publishTime";
        public const string ShowCount = "showCount";

        public const string EditMode = "EDIT_MODE";

        /// <summary>
        /// To mark it's in add mode or in modify mode.
        /// </summary>
        public enum EditModes
        {
            Add,
            Modify
        }
    }
}