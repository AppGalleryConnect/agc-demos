/*
* Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
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

module.exports = {
    NUMBER_REG: /^(0|[1-9][0-9]*)$/,
    PRICE_REG: /(^[1-9][0-9]{0,7}$)|(^((0\.0[1-9]$)|(^0\.[1-9]\d?)$)|(^[1-9][0-9]{0,7}\.\d{1,2})$)/,
    BOOLEAN_REG: /^(true|false)$/,
    DATA_REG: /^(((((0[48]|[2468][048]|[3579][26])00))|(([0-9]{2})(0[48]|[2468][048]|[13579][26])))[-|.|/| ]0?2[-|.|/| ]29|(((?!0{1,4})[0-9]{1,4})[-|.|/| ](((0[13-9]|1[0-2]|[13-9])[-|.|/| ](29|30))|((0[13578]|(10|12)|[13578])[-|.|/| ]31)|((0(?:[1-9])|1(?:[0-2])|[1-9])[-|.|/| ](0(?:[1-9])|1[0-9]|2[0-8]|[1-9])))))$/
}
