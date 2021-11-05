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

import * as context from './config/agconnect-services.json';
import * as schema from "./config/app-schema.json";
import { BookInfo } from './model/BookInfo';

let agConnectCloudDB;
let zone;
let queryCondition;
export class operate {

    static initCloudDB() {
        agconnect.cloudDB.AGConnectCloudDB.initialize(context);
        agConnectCloudDB = agconnect.cloudDB.AGConnectCloudDB.getInstance();
        agConnectCloudDB.createObjectType(schema);
        console.log('init CloudDB success');
    }


    static async openZone(zoneName: string) {
        let cloudDBZoneConfig = new agconnect.cloudDB.CloudDBZoneConfig(zoneName);
        zone = await agConnectCloudDB.openCloudDBZone(cloudDBZoneConfig);
    }

    static handleTime(date: Date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        return date;
    }

    static async insertBook(book: BookInfo) {
        const size = await zone.executeUpsert(book);
        console.log(`insert book success, size: ${size}`);
    }

    static async closeZone() {
        await agConnectCloudDB.closeCloudDBZone(zone);
        zone = null;
    }

    static isOpenZone() {
        return zone;
    }

    static async queryDate() {
        let condition = new agconnect.cloudDB.CloudDBZoneQuery.where(BookInfo);
        if (queryCondition) {
            condition = queryCondition;
        }
        return await zone.executeQuery(condition);
    }

    static async deleteBook(bookInfo) {
        return await zone.executeDelete(bookInfo);
    }

    static setQueryCondiiton(condition) {
        queryCondition = condition;
    }

    static clearAllCondiiton() {
        queryCondition = null;
    }

    static checkFieldName(name) {
        switch (name) {
            case 'id':
            case 'bookName':
            case 'author':
            case 'price':
            case 'publisher':
            case 'publishTime':
            case 'shadowFlag':
                return;
            default:
                throw 'fieldName is invalid';
        }
    }

    static getCondition() {
        return queryCondition;
    }
}
