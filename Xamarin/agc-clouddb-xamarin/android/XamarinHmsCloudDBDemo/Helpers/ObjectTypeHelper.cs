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
using System.Collections.Generic;
using XamarinHmsCloudDBDemo.Model;

namespace XamarinHmsCloudDBDemo.Helpers
{
    /// <summary>
    /// CloudDB sdk is strongly depend on Java
    /// so CSharp type must convert to Java type via Java Reflection Feature.
    /// </summary>
    public class ObjectTypeHelper
    {
        public static BookInfo ConvertJavaTypeToCSharpType(Java.Lang.Object javaBookInfoObject)
        {

            BookInfo bookInfo = new BookInfo()
            {
                Id = (int)ObtainFieldValue(javaBookInfoObject, "id"),
                BookName = (string)ObtainFieldValue(javaBookInfoObject, "bookName"),
                Author = (string)ObtainFieldValue(javaBookInfoObject, "author"),
                Price = (double)ObtainFieldValue(javaBookInfoObject, "price"),
                Publisher = (string)ObtainFieldValue(javaBookInfoObject, "publisher"),
                PublishTime = (Java.Util.Date)ObtainFieldValue(javaBookInfoObject, "publishTime"),
                ShadowFlag = (bool)ObtainFieldValue(javaBookInfoObject, "shadowFlag")
            };

            return bookInfo;
        }

        public static Java.Lang.Object ConvertCSharpTypeToJavaType(BookInfo csharpBookInfoObject)
        {
            Java.Lang.Object javaBookInfoObject = Java.Lang.Class.ForName("com.company.project.BookInfo").NewInstance();
            Dictionary<string, Java.Lang.Object> fieldKeyValuePairs = new Dictionary<string, Java.Lang.Object>();

            fieldKeyValuePairs.Add("id", csharpBookInfoObject.Id);
            fieldKeyValuePairs.Add("bookName", csharpBookInfoObject.BookName);
            fieldKeyValuePairs.Add("author", csharpBookInfoObject.Author);
            fieldKeyValuePairs.Add("price", csharpBookInfoObject.Price);
            fieldKeyValuePairs.Add("publisher", csharpBookInfoObject.Publisher);
            fieldKeyValuePairs.Add("publishTime", csharpBookInfoObject.PublishTime);
            fieldKeyValuePairs.Add("shadowFlag", csharpBookInfoObject.ShadowFlag);

            SetFieldValue(ref javaBookInfoObject, fieldKeyValuePairs);
            return javaBookInfoObject;
        }

        public static Java.Lang.Object ObtainFieldValue(Java.Lang.Object javaObject, string fieldName)
        {
            var javaObjectField = javaObject.Class.GetDeclaredField(fieldName);
            javaObjectField.Accessible = true;
            var fieldValue = javaObjectField.Get(javaObject);
            return fieldValue;
        }

        public static void SetFieldValue(ref Java.Lang.Object javaObject, Dictionary<string, Java.Lang.Object> fieldKeyValuePairs)
        {
            foreach (KeyValuePair<string, Java.Lang.Object> kvp in fieldKeyValuePairs)
            {
                var javaObjectField = javaObject.Class.GetDeclaredField(kvp.Key);
                javaObjectField.Accessible = true;
                javaObjectField.Set(javaObject, kvp.Value);
            }
        }

    }
}