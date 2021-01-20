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

import {
    AGConnectCloudDB,
    CloudDBZoneQuery,
    CloudDBZoneConfig,
} from '@agconnect/database';
import agconnect from '@agconnect/api';
import '@agconnect/auth';
import '@agconnect/instance';
import {context} from './config/agconnect-services.js';
import {BookInfo} from './model/BookInfo';

let agcCloudDB;
let cloudDBZone;

/**
 * try agc auth and login
 *
 * @returns {Promise<boolean>}
 */
async function agcLogin() {
    agconnect.instance().configInstance(context)
    AGConnectCloudDB.initialize(context);
    const agcAuth = agconnect.auth();
    console.log('start');
    const getUser = await agcAuth.getCurrentUser();
    if (getUser === null || getUser === undefined) {
        // try anonymously login
        console.log('try anonymously login');
        try {
            const result = await agcAuth.signInAnonymously();
            if (result.getUser() !== null && result.getUser() !== undefined) {
                console.log('login success');

                // try to create schema
                console.log('start create schema');
                try {
                    const schema = require('./config/BookInfo.json');
                    agcCloudDB = AGConnectCloudDB.getInstance();
                    await agcCloudDB.createObjectType(schema);
                    console.log('create success');
                    return true;
                } catch (e) {
                    console.log('create failed:' + e.message);
                    return false;
                }
            } else {
                console.log('login failed');
            }
        } catch (e) {
            console.log('try to login but failed, error reason is:' + e.message);
            return false;
        }
    }
}

/**
 * open a cloud db zone. then you can crud data
 *
 * @returns {Promise<boolean>}
 */
async function openCloudDBZone() {
    console.log('try to open zone')
    try {
        const config = new CloudDBZoneConfig('BaseZoneA');
        cloudDBZone = await agcCloudDB.openCloudDBZone(config);
        console.log('open zone success:');
        console.log(cloudDBZone);
        return true;
    } catch (e) {
        console.log('open zone failed:' + e.message);
        return false;
    }
}

/**
 * subscribeSnapshot, when data change, onSnapshotListener going to receive the change
 *
 * @returns {Promise<void>}
 */
async function subscribeSnapshot(onSnapshotListener) {
    try {
        const query = CloudDBZoneQuery.where(BookInfo);
        query.equalTo('shadowFlag', true);
        const listenerHandler = await cloudDBZone.subscribeSnapshot(query, onSnapshotListener);
        return listenerHandler;
    } catch (e) {
        console.log('subscribeSnapshot error:' + e.message);
        return null;
    }
}

/**
 * The insert
 *
 * @param book The object to be inserted
 * @returns {Promise<number>}
 */
async function executeUpsert(book) {
    try {
        const cloudDBZoneResult = await cloudDBZone.executeUpsert(book);
        console.log('upsert ' + cloudDBZoneResult + ' record');
        return cloudDBZoneResult;
    } catch (e) {
        console.log('upsert failed with reason:' + e.message);
        return 0;
    }
}

/**
 * The delete
 *
 * @param book The object to be deleted
 * @returns {Promise<number>}
 */
async function executeDelete(book) {
    try {
        const num = await cloudDBZone.executeDelete(book);
        return num;
    } catch (e) {
        console.log('delete failed with reason:' + e.message);
        return 0;
    }
}

/**
 * Query all book
 *
 * @returns {Promise<T[]>}
 */
async function executeQueryAllBooks() {
    try {
        return await new Promise(resolve => {
            const query = CloudDBZoneQuery.where(BookInfo);
            query.orderByDesc('price');
            cloudDBZone.executeQuery(query).then(snapshot => {
                const resultArray = snapshot.getSnapshotObjects();
                resolve(resultArray);
            });
        });
    } catch (e) {
        console.log('query failed with reason:' + e.message);
        return null;
    }
}

/**
 * Executing a conditional query
 *
 * @param object Conditional query data
 * @returns {Promise<null|T[]>}
 */
async function executeQueryComposite(object) {
    try {
        return await new Promise(resolve => {
            console.log(object);
            const query = CloudDBZoneQuery.where(BookInfo);
            if (object.name.length > 0) {
                query.equalTo('bookName', object.name);
            }
            if (parseFloat(object.minPrice) > 0) {
                query.greaterThanOrEqualTo('price', parseFloat(object.minPrice));
            }
            if (parseFloat(object.maxPrice) > 0 && parseFloat(object.maxPrice) > parseFloat(object.minPrice)) {
                query.lessThanOrEqualTo('price', parseFloat(object.maxPrice));
            }
            if (parseInt(object.bookCount) > 0) {
                query.limit(parseInt(object.bookCount));
            }
            query.orderByAsc('id');
            cloudDBZone.executeQuery(query).then(snapshot => {
                const resultArray = snapshot.getSnapshotObjects();
                resolve(resultArray);
            });
        });
    } catch (e) {
        console.log('query failed with reason:' + e.message);
        return null;
    }
}

/**
 * Ascending and descending sequence search
 *
 * @param filedName Sort name
 * @param sortType Ascending or descending
 * @returns {Promise<T[]>}
 */
async function queryDataByOrderWay(filedName, sortType) {
    try {
        return await new Promise(resolve => {
            const query = CloudDBZoneQuery.where(BookInfo);
            if (sortType === 1) {
                query.orderByAsc(filedName);
            } else {
                query.orderByDesc(filedName);
            }
            cloudDBZone.executeQuery(query).then(snapshot => {
                const resultArray = snapshot.getSnapshotObjects();
                resolve(resultArray);
            });
        });
    } catch (e) {
        console.log('query failed with reason:' + e.message);
        return null;
    }
}

export {
    agcLogin,
    openCloudDBZone,
    executeUpsert,
    executeQueryAllBooks,
    executeQueryComposite,
    queryDataByOrderWay,
    executeDelete,
    subscribeSnapshot
}