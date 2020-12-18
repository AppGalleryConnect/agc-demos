/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
// handler.js is a demo for handling Quick Start Node.js Demo.
// This demo shows an operation scenario of upserting first and then query the data. 
// Refer to this demo and Cloud DB API document, you can do more, for example excuting delete and excuting deleteAll.
let myHandler = function(event, context, callback, logger) {
    const cloudDBApi = require('./CloudDBApi.js');
    let cloudDBQuery = new cloudDBApi.CloudDBQuery();
    let queryCondition = cloudDBQuery.getQueryConditions();
    let cloudDBZoneName = 'QuickStartDemo';
    let objectTypeName = 'BookInfo';
    
    let cloudDBZone = new cloudDBApi.CloudDBZone(cloudDBZoneName, context);
    // Start to upsert data.
    let objects = [{
            'id': 1,
            'bookName':'BookA',
            'author':'Lily',
            'price':32.0,
            'publisher':'Beijing',
            'publishTime':'2019-12-02 16:00:00 000'
            }]
    cloudDBZone.executeUpsert(objectTypeName, objects).then(function() {
        logger.info('Data upserting succeeded.');
        // Start to query data.
        return cloudDBZone.executeBasicQuery(objectTypeName, queryCondition);
    }).then(function(data) {
        logger.info('Data querying succeeded.');
        callback("Data querying succeeded.");
    }).catch(function(error) {
        logger.error('Data operation failed.');
        let code = error.code;
        let message = error.message;
        // ...
        callback(error);
    });
};

module.exports.myHandler = myHandler;