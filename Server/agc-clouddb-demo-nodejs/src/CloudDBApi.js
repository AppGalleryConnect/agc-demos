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

'use strict';

const FieldType = {
    BOOLEAN: 'Boolean',
    BYTE: 'Byte',
    SHORT: 'Short',
    INT: 'Integer',
    LONG: 'Long',
    FLOAT: 'Float',
    DOUBLE: 'Double',
    BYTE_ARRAY: 'ByteArray',
    STRING: 'String',
    DATE: 'Date',
    TEXT: 'Text'
};

const ConditionType = {
    BEGINS_WITH: 'BeginsWith',
    ENDS_WITH: 'EndsWith',
    CONTAINS: 'Contains',
    EQUAL_TO: 'EqualTo',
    NOT_EQUAL_TO: 'NotEqualTo',
    GREATER_THAN: 'GreaterThan',
    GREATER_THAN_OR_EQUAL_TO: 'GreaterThanOrEqualTo',
    LESS_THAN: 'LessThan',
    LESS_THAN_OR_EQUAL_TO: 'LessThanOrEqualTo',
    IN: 'In',
    IS_NULL: 'IsNull',
    AVERAGE: 'Average',
    IS_NOT_NULL: 'IsNotNull',
    ORDER_BY: 'OrderBy',
    LIMIT: 'Limit',
    UID: 'Uid',
    COUNT: 'Count',
    MAX: 'Max',
    MIN: 'Min',
    SUM: 'Sum',
    SELECT: 'Select',
    GROUP_BY: 'GroupBy',
    START_AT: 'StartAt',
    START_AFTER: 'StartAfter',
    END_AT: 'EndAt',
    END_BEFORE: 'EndBefore'
};

const OperationType = {
    UPSERT: 'Upsert',
    DELETE: 'Delete',
};

const OrderType = {
    ASCEND: 'asc',
    DESCEND: 'desc'
};

const ParaInvalidFailure = {code: '1070008', message: 'Invalid parameter.'};
const TransactionFailure = {code: '1370035', message: 'Incorrect transaction execution sequence.'};
const DataLargeFailure = {code: '1370019', message: 'A maximum of 1000 data records within 20 MB can be queried at a time. Adjust the query condition.'};
const MAX_QUERY_DATA_SIZE = 20 * 1024 * 1024;
const RETRY_COUNT = 5;
const RETRY_CODE = '1370022';
const TRANSACTION_SUCCESS = {"info":""};

var mHttpsClient;

/**
 * interfaces for https client
 *
 * @param host the host address of http server
 * @param port the port of the http server
 */
function HttpsClient(context) {
    this.host = 'clouddb.hispace.hicloud.com';
    this.port = '6444';
    this.cert = '';

    const fs = require('fs');
    this.cert = fs.readFileSync(__dirname + '/cert.pem', 'ascii');

    this.send = function(path, method, param, callback) {
        if (isEmpty(context)) {
            callback(false, ParaInvalidFailure);
            return;
        }

        this.accessKey = context.env.clouddb_ak;
        this.secretKey = context.env.clouddb_sk;
        this.productId = context.env.PRODUCT_ID;
        const isInvalid =
            !this.productId || !this.accessKey || !this.secretKey || this.accessKey.length > 64 || this.secretKey.length > 64;
        if (isInvalid) {
            callback(false, ParaInvalidFailure);
            return;
        }

        const client = require('https');

        this.processRequest(client, path, method, param, callback);
    };
}

HttpsClient.prototype.processRequest = function(client, path, method, param, callback) {
    const buf = Buffer.from(JSON.stringify(param));
    const time = new Date().getTime().toString();
    const temp = 'false\nfalse\nfalse\n' + time + '\n' + method + '\n\n\n\n\n';
    const authorization = 'SDK-HMAC-SHA256 containPath=false,containBody=false,containQuery=false,accessId=' +
        this.accessKey + ',timestamp=' + time + ',signedHeaders=,signature=' + this.encrypt(temp);
    const CipherSuites = [
        'ECDHE-ECDSA-AES128-GCM-SHA256',
        'ECDHE-RSA-AES128-GCM-SHA256',
        'ECDHE-ECDSA-AES256-GCM-SHA384',
        'ECDHE-RSA-AES256-GCM-SHA384'
    ];
    const options = {
        hostname: this.host,
        port: this.port,
        path: path,
        method: method,
        requestCert: true,
        ciphers: CipherSuites.join(':'),
        rejectUnauthorized: true,
        ca: [this.cert],
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            Connection: 'keep-alive',
            'Content-Length': buf.length,
            uid: 'FaaS',
            accessKey: this.accessKey,
            productId: this.productId,
            from: 'FaaS',
            authorization: authorization
        }
    };

    this.request(client, options, buf.toString('utf8'), callback);
}

HttpsClient.prototype.request = function(client, options, postParam, callback) {
    let chunks = [];
    let length = 0;
    let failedInfo = {};
    let req = client.request(options, function(res) {
        res.on('data', function(chunk) {
            chunks.push(chunk);
            length += chunk.length;
        });

        res.on('end', function() {
            let result = Buffer.concat(chunks, length).toString('utf8');
            try {
                result = JSON.parse(result);
            } catch (e) { }
            if (res.statusCode.toString().startsWith('2')) {
                callback(true, result);
            } else if (res.statusCode === 413) {
                failedInfo.code = '1370008';
                failedInfo.message = 'Inserted data volumes are too large.';
                callback(false, failedInfo);
            } else {
                if (!!result.info && !!result.errorCode) {
                    failedInfo.code = result.errorCode;
                    failedInfo.message = result.info;
                    callback(false, failedInfo);
                } else {
                    failedInfo.code = '1079999';
                    failedInfo.message = 'System error.';
                    callback(false, failedInfo);
                }
            }
        });
    });

    req.on('error', function() {
        failedInfo.code = '1079999';
        failedInfo.message = 'System error.';
        callback(false, failedInfo);
    });
    req.write(postParam);
    req.end();
}

HttpsClient.prototype.encrypt = function(data) {
    let crypto = require('crypto');
    let key = this.secretKey.toString('ascii');
    let hmac = crypto.createHmac('sha256', key);
    hmac.update(data);
    return hmac.digest('hex');
}

function isEmpty(...params) {
    if (params.length === 0) {
        return true;
    }

    for (let param of params) {
        if (!param) {
            return true;
        }
    }

    return false;
}

function isOverLimit(...params) {
    if (params.length === 0) {
        return false;
    }
    let size = 0;
    for (let param of params) {
        size = Buffer.byteLength(JSON.stringify(param));
        if (size > MAX_QUERY_DATA_SIZE) {
            return true;
        }
    }
    return false
}

/**
 * Initialize a CloudDBZone
 *
 * @param cloudDBZoneName the name of the CloudDBZone
 * @param context the sign context info
 */
function CloudDBZone(cloudDBZoneName, context) {
    this.cloudDBZoneName = cloudDBZoneName;
    if (!mHttpsClient) {
        mHttpsClient = new HttpsClient(context);
    }
}

/**
 * Initialize a AGConnectCloudDB with context
 *
 * @param context the sign context info
 */
function AGConnectCloudDB(context) {
    if (!mHttpsClient) {
        mHttpsClient = new HttpsClient(context);
    }
}

/**
 * Execute upsert to upsert data
 *
 * @param objectTypeName the object type Name
 * @param objects the data to upsert
 */
CloudDBZone.prototype.executeUpsert = function(objectTypeName, objects) {
    const path = '/clouddbservice/v1/objects';
    const method = 'POST';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        objects: objects
    };
    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName, objects)) {
            reject(ParaInvalidFailure);
            return;
        }

        for (let i = 0; i < objects.length; i++) {
            let keys = Object.keys(objects[i]);
            for (let j = 0; j < keys.length; j++) {
                if (typeof objects[i][keys[j]] === 'string' || objects[i][keys[j]] instanceof Array) {
                    continue;
                }

                if (!isFinite(objects[i][keys[j]])) {
                    reject(ParaInvalidFailure);
                    return;
                }
            }
        }
        sendRequest(path, method, param, function(result, info) {
            if (result) {
                resolve(info);
            } else {
                reject(info);
            }
        });
    });
}

/**
 * Execute delete to delete data
 *
 * @param objectTypeName the object type Name
 * @param objects the data to delete
 */
CloudDBZone.prototype.executeDelete = function(objectTypeName, objects) {
    const path = '/clouddbservice/v1/objects';
    const method = 'DELETE';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        objects: objects
    };
    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName, objects)) {
            reject(ParaInvalidFailure);
            return;
        }
        sendRequest(path, method, param, function(result, info) {
            if (result) {
                resolve(info);
            } else {
                reject(info);
            }
        });
    });
}

/**
 * Execute delete to delete user encryption data
 *
 * @param userId the user Id
 */
AGConnectCloudDB.prototype.executeDeleteUserKey = function(userId) {
    const path = '/clouddbservice/v1/userKey';
    const method = 'DELETE';
    const param = {
        userId: userId
    };
    return new Promise(function(resolve, reject) {
        if (isEmpty(userId)) {
            reject(ParaInvalidFailure);
            return;
        }
        mHttpsClient.send(path, method, param, function(flag, result) {
            if (flag) {
                resolve(result);
            } else {
                reject(result);
            }
        });
    });
}

/**
 * Execute deleteAll to clear the object type.
 *
 * @param objectTypeName the object type Name
 */
CloudDBZone.prototype.executeDeleteAll = function(objectTypeName) {
    const path = '/clouddbservice/v1/allObjects';
    const method = 'DELETE';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName
    };
    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName)) {
            reject(ParaInvalidFailure);
            return;
        }
        sendRequest(path, method, param, function(result, info) {
            if (result) {
                resolve(info);
            } else {
                reject(info);
            }
        });
    });
}

function sendRequest(path, method, param, callback) {
    let promise = new Promise(function(resolve, reject) {
        mHttpsClient.send(path, method, param, function(flag, result) {
            if (flag) {
                resolve(result);
            } else {
                reject(result);
            }
        });
    });
    promise.then(function(data) {
            getProcessResult(data, callback);
        }).catch(function(data) {
            callback(false, data);
        });
}

function getProcessResult(data, callback) {
    let timeout;
    let processId = data.processId;
    let timeoutCount = 0;
    let maxCount = 10000;
    let finalCallback = callback;

    function getResultCallback(result, info) {
        let failedInfo = {};
        if (!result) {
            finalCallback(true, failedInfo);
            clearTimeout(timeout);
            return;
        }

        if (info.finished) {
            if (info.processPercentage < 1) {
                failedInfo.code = info.errorCode;
                failedInfo.message = info.failedReason;
                finalCallback(false, failedInfo);
            } else {
                finalCallback(true, {});
            }
            clearTimeout(timeout);
        } else {
            timeoutCount++;
            clearTimeout(timeout);
            if (timeoutCount >= maxCount) {
                failedInfo.code = '1079999';
                failedInfo.message = 'System error.';
                finalCallback(false, failedInfo);
            } else {
                timeout = setTimeout(getResult, 100, processId);
            }
        }
    }

    function getResult() {
        const getResultPath = '/clouddbservice/v1/processPercentage';
        const getResultMethod = 'POST';
        let getResultParam = {
            processId: data.processId
        };
        mHttpsClient.send(getResultPath, getResultMethod, getResultParam, getResultCallback);
    }
    timeout = setTimeout(getResult, 100, processId);
}

/**
 * Execute query to query data.
 *
 * @param objectTypeName the object type Name
 * @param conditions the query conditon
 */
CloudDBZone.prototype.executeQuery = function(objectTypeName, conditions) {
    const path = '/clouddbservice/v1/objects/queries';
    const method = 'POST';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        queryConditions: conditions
    };

    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName)) {
            reject(ParaInvalidFailure);
            return;
        }

        mHttpsClient.send(path, method, param, function(flag, result) {
            if (flag) {
                resolve(result);
            } else {
                reject(result);
            }
        });
    });
}

/**
 * Execute query to query data, if you don't care the value of byteArrary type field,
 * and want to query mutiple data, this function is recommended.
 *
 * @param objectTypeName the object type Name
 * @param conditions the query conditon
 */
CloudDBZone.prototype.executeBasicQuery = function(objectTypeName, conditions) {
    const path = '/clouddbservice/v2/objects/queries';
    const method = 'POST';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        queryConditions: conditions
    };

    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName)) {
            reject(ParaInvalidFailure);
            return;
        }

        mHttpsClient.send(path, method, param, function(flag, result) {
            if (flag) {
                resolve(result);
            } else {
                reject(result);
            }
        });
    });
}

/**
 * Execute query to query data to execute Transaction
 *
 * @param objectTypeName the object type Name
 * @param conditions the query condition
 */
CloudDBZone.prototype.executeTransactionQuery = function(objectTypeName, conditions) {
    const path = '/clouddbservice/v1/syncObjects/queries';
    const method = 'POST';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        queryConditions: conditions
    };

    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName)) {
            reject(ParaInvalidFailure);
            return;
        }

        mHttpsClient.send(path, method, param, function(flag, result) {
            if (flag) {
                resolve(result);
            } else {
                reject(result);
            }
        });
    });
}

/**
 * Execute load to load the specified file.
 *
 * @param objectTypeName the object type Name
 * @param conditions the query conditon
 * @param fieldName the query field name
 */
CloudDBZone.prototype.executeLoad = function(objectTypeName, conditions, fieldName) {
    const path = '/clouddbservice/v1/field/load';
    const method = 'POST';
    const param = {
        cloudDBZoneName: this.cloudDBZoneName,
        objectTypeName: objectTypeName,
        queryConditions: conditions,
        fieldName: fieldName
    };

    return new Promise(function(resolve, reject) {
        if (isEmpty(objectTypeName) || isEmpty(fieldName)) {
            reject(ParaInvalidFailure);
            return;
        }

        mHttpsClient.send(path, method, param, function(flag, result) {
        if (flag) {
            resolve(result);
            } else {
                reject(result);
            }
        });
    });
}

/**
 * Execute transaction. only support upsert and delete
 *
 * @param fx the function
 * @param transaction the transaction
 */
CloudDBZone.prototype.runTransaction = async function(fx, transaction) {
    let isRetry = true;
    let retryCount = RETRY_COUNT;
    let result;
    while (retryCount > 0 && isRetry) {
        result = await retryTransaction(fx, transaction, this.cloudDBZoneName).then(result => {
            isRetry = false;
            return Promise.resolve(result);
        }).catch(err => {
            if (err.code == RETRY_CODE) {
                retryCount -= 1;
                isRetry = true;
                transaction.releaseData();
            } else {
                isRetry = false;
            }
            if (retryCount == 0 || err.code != RETRY_CODE) {
                return Promise.reject(err);
            }
        })
    }
    return result;
}

function retryTransaction(fx, transaction, cloudDBZoneName) {
    const path = '/clouddbservice/v1/transaction';
    const method = 'POST';
    return fx(transaction).then(()=>{
        const param = {
            cloudDBZoneName: cloudDBZoneName,
            transactionList: transaction.getTransactionList(),
            needVerifyObjectsList: transaction.getNeedVerifyObjectsList()
        };
        if (param.transactionList && param.transactionList.length == 0) {
            return Promise.resolve(TRANSACTION_SUCCESS);
        }
        return new Promise(function(resolve, reject) {
            if (transaction.isExistException) {
                reject(TransactionFailure);
                return;
            }
            if (isOverLimit(param.needVerifyObjectsList)) {
                reject(DataLargeFailure);
                return;
            }
            mHttpsClient.send(path, method, param, function(flag, result) {
                if (flag) {
                    resolve(result);
                } else {
                    reject(result);
                }
            });
        });
    })
}


Transaction.prototype.releaseData = function() {
    this.transactionList = [];
    this.needVerifyObjectsList = [];
}

/**
 * Get a transaction instance.
 */
function Transaction(cloudDBZone) {
    this.cloudDBZone = cloudDBZone;
    this.transactionList = [];
    this.needVerifyObjectsList = [];
    this.isExistException = false;
    this.addTransaction = function(operationType, objectTypeName, objects) {
        let transactionObject = {};
        transactionObject.operationType = operationType;
        transactionObject.objectTypeName = objectTypeName;
        transactionObject.objects = objects;
        this.transactionList.push(transactionObject);
        return this;
    };

    this.addNeedVerifyObject = function(objectTypeName, objects) {
        let needVerifyObject = {};
        needVerifyObject.objectTypeName = objectTypeName;
        needVerifyObject.objects = objects;
        this.needVerifyObjectsList.push(needVerifyObject);
        return this;
    };
}

Transaction.prototype.executeQuery = function(objectTypeName, conditions) {
    let transaction = this;
    if (this.getTransactionList().length !== 0) {
        this.isExistException = true;
        return Promise.reject(TransactionFailure);
    }
    return this.cloudDBZone.executeTransactionQuery(objectTypeName, conditions).then(function (result) {
        if (result.data.length == 0) {
            return Promise.resolve(transaction);
        }
        transaction.addNeedVerifyObject(objectTypeName, result.data);
        if (isOverLimit(transaction.getNeedVerifyObjectsList())) {
            return Promise.reject(DataLargeFailure);
        }
        return Promise.resolve(transaction);
    }).catch(function (err) {
        return Promise.reject(err);
    });
}

/**
 * Get a CloudDBQuery instance.
 */
function CloudDBQuery() {
    this.conditions = [];
    this.addCondition = function(conditionType, fieldName, value) {
        let conditionObject = {};
        conditionObject.conditionType = conditionType;
        conditionObject.fieldName = fieldName;
        conditionObject.value = value;
        this.conditions.push(conditionObject);
        return this;
    };
}


/**
 * Get transaction list
 */
Transaction.prototype.getTransactionList = function() {
    return this.transactionList;
}

/**
 * Get needVerifyObjects list
 */
Transaction.prototype.getNeedVerifyObjectsList = function() {
    return this.needVerifyObjectsList;
}

/**
 * Get query conditions
 */
CloudDBQuery.prototype.getQueryConditions = function() {
    return this.conditions;
}
/*
* Get aggregate
*/
CloudDBQuery.prototype.Aggregate = function(){
    this.avg = function(fieldName, aliasName) {
        if(aliasName == "" || aliasName == null || aliasName == undefined) {
            return "average("+fieldName+")";
        } else {
            return "average("+fieldName+") alias " + aliasName;
        }
    }

    this.max = function(fieldName, aliasName) {
        if(aliasName == "" || aliasName == null || aliasName == undefined) {
            return "maximum("+fieldName+")";
        } else {
            return "maximum("+fieldName+") alias " + aliasName;
        }
    }

    this.min = function(fieldName, aliasName) {
        if(aliasName == "" || aliasName == null || aliasName == undefined) {
            return "minimum("+fieldName+")";
        } else {
            return "minimum("+fieldName+") alias " + aliasName;
        }
    }

    this.sum = function(fieldName, aliasName) {
        if(aliasName == "" || aliasName == null || aliasName == undefined) {
            return "sum("+fieldName+")";
        } else {
            return "sum("+fieldName+") alias " + aliasName;
        }
    }

    this.count = function(fieldName, aliasName) {
        if(aliasName == "" || aliasName == null || aliasName == undefined) {
            return "count("+fieldName+")";
        } else {
            return "count("+fieldName+") alias " + aliasName;
        }
    }
    return this;
}

/**
 * Add a beginsWith query condition
 *
 * @param fieldName the field to match with
 * @param value the data to begin with
 */
CloudDBQuery.prototype.beginsWith = function(fieldName, value) {
    return this.addCondition(ConditionType.BEGINS_WITH, fieldName, value);
}

/**
 * Add a endsWith query condition
 *
 * @param fieldName the field to match with
 * @param value the data to end with
 */
CloudDBQuery.prototype.endsWith = function(fieldName, value) {
    return this.addCondition(ConditionType.ENDS_WITH, fieldName, value);
}

/**
 * Add a contains query condition
 *
 * @param fieldName the field to match with
 * @param value the data to contain
 */
CloudDBQuery.prototype.contains = function(fieldName, value) {
    return this.addCondition(ConditionType.CONTAINS, fieldName, value);
}

/**
 * Add a equalTo query condition
 *
 * @param fieldName the field to match with
 * @param value the data to equal to
 */
CloudDBQuery.prototype.equalTo = function(fieldName, value) {
    return this.addCondition(ConditionType.EQUAL_TO, fieldName, value);
}

/**
 * Add a notEqualTo query condition
 *
 * @param fieldName the field to match with
 * @param value the data that does not equal to
 */
CloudDBQuery.prototype.notEqualTo = function(fieldName, value) {
    return this.addCondition(ConditionType.NOT_EQUAL_TO, fieldName, value);
}

/**
 * Add a greaterThan query condition
 *
 * @param fieldName the field to match with
 * @param value the data that is greater than.
 */
CloudDBQuery.prototype.greaterThan = function(fieldName, value) {
    return this.addCondition(ConditionType.GREATER_THAN, fieldName, value);
}

/**
 * Add a greaterThanOrEqualTo query condition
 *
 * @param fieldName the field to match with
 * @param value the data that is greater than or equal to.
 */
CloudDBQuery.prototype.greaterThanOrEqualTo = function(fieldName, value) {
    return this.addCondition(ConditionType.GREATER_THAN_OR_EQUAL_TO, fieldName, value);
}

/**
 * Add a lessThan query condition
 *
 * @param fieldName the field to match with
 * @param value the data that is les than.
 */
CloudDBQuery.prototype.lessThan = function(fieldName, value) {
    return this.addCondition(ConditionType.LESS_THAN, fieldName, value);
}

/**
 * Add a lessThanOrEqualTo query condition
 *
 * @param fieldName the field to match with
 * @param value the data that is less than or equal to.
 */
CloudDBQuery.prototype.lessThanOrEqualTo = function(fieldName, value) {
    return this.addCondition(ConditionType.LESS_THAN_OR_EQUAL_TO, fieldName, value);
}

/**
 * Add a in query condition
 *
 * @param fieldName the field to match with
 * @param values this is an array which is a range to check whether the record is in.
 */
CloudDBQuery.prototype.in = function(fieldName, values) {
    return this.addCondition(ConditionType.IN, fieldName, values);
}

/**
 * Add a average condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.average = function(fieldName) {
    return this.addCondition(ConditionType.AVERAGE, fieldName);
}

/**
 * Add a isNull condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.isNull = function(fieldName) {
    return this.addCondition(ConditionType.IS_NULL, fieldName);
}

/**
 * Add a isNotNull condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.isNotNull = function(fieldName) {
    return this.addCondition(ConditionType.IS_NOT_NULL, fieldName);
}

/**
 * Add a condition queried order by asc
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.orderByAsc = function(fieldName) {
    return this.addCondition(ConditionType.ORDER_BY, fieldName, OrderType.ASCEND);
}

/**
 * Add a condition queried order by desc
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.orderByDesc = function(fieldName) {
    return this.addCondition(ConditionType.ORDER_BY, fieldName, OrderType.DESCEND);
}

/**
 * Add a condition queried by uid.
 *
 * @param uid the user's uid.
 */
CloudDBQuery.prototype.byUid = function(uid) {
    return this.addCondition(ConditionType.UID, null, uid);
}

/**
 * Add a limit condition
 *
 * @param offset the offset number
 * @param number the number of queried record
 */
CloudDBQuery.prototype.limit = function(offset, number) {
    let limitObject = {offset: offset, number: number};
    return this.addCondition(ConditionType.LIMIT, null, limitObject);
}

/**
 * Add a count condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.count = function(fieldName) {
    return this.addCondition(ConditionType.COUNT, fieldName);
}

/**
 * Add a max condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.max = function(fieldName) {
    return this.addCondition(ConditionType.MAX, fieldName);
}

/**
 * Add a min condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.min = function(fieldName) {
    return this.addCondition(ConditionType.MIN, fieldName);
}

/**
 * Add a sum condition
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.sum = function(fieldName) {
    return this.addCondition(ConditionType.SUM, fieldName);
}

/**
 * select fieldNames
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.select = function(fieldNames) {
    return this.addCondition(ConditionType.SELECT, fieldNames);
}

/**
 * Add a condition queried order by asc
 *
 * @param fieldName the field to match with
 */
CloudDBQuery.prototype.groupBy = function(fieldName) {
    return this.addCondition(ConditionType.GROUP_BY, fieldName);
}

/**
  * Add a condition queried start at
  *
  * @param objects the object to match with
  */
 CloudDBQuery.prototype.startAt = function(object) {
     return this.addCondition(ConditionType.START_AT, null, object);
 }

/**
  * Add a condition queried start after
  *
  * @param objects the object to match with
  */
 CloudDBQuery.prototype.startAfter = function(object) {
     return this.addCondition(ConditionType.START_AFTER, null, object);
 }

/**
  * Add a condition queried end at
  *
  * @param objects the object to match with
  */
 CloudDBQuery.prototype.endAt = function(object) {
     return this.addCondition(ConditionType.END_AT, null, object);
 }

/**
  * Add a condition queried end before
  *
  * @param objects the object to match with
  */
 CloudDBQuery.prototype.endBefore = function(object) {
     return this.addCondition(ConditionType.END_BEFORE, null, object);
 }

/**
 * Add a operation type  upsert
 *
 * @param objectTypeName the objectType name to match with
 * @param objects the delete objects
 */
Transaction.prototype.executeUpsert = function(objectTypeName, objects) {
    return this.addTransaction(OperationType.UPSERT, objectTypeName, objects);
}

/**
 * Add a operation type  delete
 *
 * @param objectTypeName the objectType name to match with
 * @param objects the delete objects
 */
Transaction.prototype.executeDelete = function(objectTypeName, objects) {
    return this.addTransaction(OperationType.DELETE, objectTypeName, objects);
}

module.exports = {
    CloudDBZone,
    CloudDBQuery,
    FieldType,
    ConditionType,
    OperationType,
    AGConnectCloudDB,
    Transaction
};
