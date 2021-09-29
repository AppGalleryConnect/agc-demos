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
using Huawei.Agconnect.Cloud.Database;

namespace XamarinHmsCloudDBDemo.Model
{
    /// <summary>
    /// BookInfo CSharp model.
    /// </summary>
    public class BookInfo : CloudDBZoneObject
    {
        public int Id { get; set; }
        public string BookName { get; set; }
        public string Author { get; set; }
        public double Price { get; set; }
        public string Publisher { get; set; }
        public Java.Util.Date PublishTime { get; set; }
        public bool ShadowFlag { get; set; }

        public BookInfo() : base(Java.Lang.Class.FromType(typeof(BookInfo)))
        {
            this.ShadowFlag = true;
        }
    }
}