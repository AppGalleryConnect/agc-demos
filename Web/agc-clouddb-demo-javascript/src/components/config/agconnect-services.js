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

const context = {
    "agcgw":{
        "websocketbackurl":"connect-ws-drcn.hispace.dbankcloud.cn",
        "backurl":"connect-drcn.dbankcloud.cn",
        "websocketurl":"connect-ws-drcn.hispace.dbankcloud.com",
        "url":"connect-drcn.hispace.hicloud.com"
    },
    "client":{
        "appType":"9999",
        "cp_id":"XXXXXXXXXXXXXXXXXXXXXXX",
        "product_id":"XXXXXXXXXXXXXXXXXXXXXXX",
        "client_id":"XXXXXXXXXXXXXXXXXXXXXXX",
        "client_secret":"XXXXXXXXXXXXXXXXXXXXXXX",
        "project_id":"XXXXXXXXXXXXXXXXXXXXXXX",
        "app_id":"XXXXXXXXXXXXXXXXXXXXXXX",
        "api_key":"XXXXXXXXXXXXXXXXXXXXXXX"
    },
    "service":{
        "analytics":{
            "collector_url":"datacollector-drcn.dt.hicloud.com,datacollector-drcn.dt.dbankcloud.cn",
            "resource_id":"p1",
            "channel_id":""
        },
        "search":{
            "url":"https://search-drcn.cloud.huawei.com"
        },
        "cloudstorage":{
            "storage_url":"https://agc-storage-drcn.platform.dbankcloud.cn"
        },
        "ml":{
            "mlservice_url":"ml-api-drcn.ai.dbankcloud.com,ml-api-drcn.ai.dbankcloud.cn"
        }
    },
    "region":"CN",
    "configuration_version":"2.0"
};


export {
    context
}
