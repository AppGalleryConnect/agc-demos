
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

import prompt from '@system.prompt';
import * as schema from '../config/app-schema.json';
import * as context from '../config/agconnect-services.json';
import { BookInfo } from '../module/BookInfo';
import { AGConnectCloudDB, CloudDBZoneQuery, CloudDBZoneConfig } from '@agconnect/database';
import agconnect from '@agconnect/api';
let handlerList = [];
let isOpenZone = false;
const reg = /^(((((0[48]|[2468][048]|[3579][26])00))|(([0-9]{2})(0[48]|[2468][048]|[13579][26])))[-|.|/| ]0?2[-|.|/| ]29|(((?!0{1,4})[0-9]{1,4})[-|.|/| ](((0[13-9]|1[0-2]|[13-9])[-|.|/| ](29|30))|((0[13578]|(10|12)|[13578])[-|.|/| ]31)|((0(?:[1-9])|1(?:[0-2])|[1-9])[-|.|/| ](0(?:[1-9])|1[0-9]|2[0-8]|[1-9])))))$/;
export default {

    setCondition(conditions) {
        this.queryCondition = conditions;
    },

    getZoneStatus() {
        return isOpenZone;
    },

    getCondition() {
        return this.queryCondition.getQueryConditions();
    },

    async getUserAccount() {
        const user = await agconnect.auth().getCurrentUser();
        if (!user) return undefined;
        return user.getEmail() || resizeBy.getPhone();
    },

    async logout() {
        try {
            await agconnect.auth().signOut();
            prompt.showToast({
                message: 'signOut success'
            })
        } catch (error) {
            console.log('signOut failed', JSON.stringify(error));
        }
    },

    async login(areaCode, phoneNum, password) {
        try {
            const user = await agconnect.auth().getCurrentUser();
            if (user) {
                prompt.showToast({
                    message: 'user already login'
                })
                return;
            }
            const credential = agconnect.auth.PhoneAuthProvider
                .credentialWithPassword(areaCode, phoneNum, password);
            await agconnect.auth().signIn(credential);
            prompt.showToast({
                message: 'signIn success'
            })
        } catch (error) {
            console.log(error);
            console.log('signIn failed', JSON.stringify(error));
            prompt.showToast({
                message: 'signIn failed'
            })
        }
    },

    initializeCloudDB() {
        console.log('initialize clouddb...');
        AGConnectCloudDB.initialize(context);
        this.agConnectCloudDB = AGConnectCloudDB.getInstance();
        this.agConnectCloudDB.createObjectType(schema);
    },

    async openCloudDBZone(zoneName) {
        try {
            const config = new CloudDBZoneConfig(zoneName);
            this.zone = await this.agConnectCloudDB.openCloudDBZone(config);
            isOpenZone = true;
            prompt.showToast({
                message: 'Open CloudDBZone success'
            })
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'Open CloudDBZone fail'
            })
        }
    },

    async setUserKey(key, rekey) {
        try {
            await this.agConnectCloudDB.setUserKey(key, rekey);
            prompt.showToast({
                message: 'set key success'
            })
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'set key failed'
            })
        }
    },

    closeCloudDBZone() {
        try {
            this.agConnectCloudDB.closeCloudDBZone(this.zone);
            this.zone = null;
            isOpenZone = false;
            prompt.showToast({
                message: 'close CloudDBZone success'
            })
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'close CloudDBZone fail'
            })
        }
    },

    async query() {
        try {
            let conditions = this.constructCondition();
            if (this.queryCondition) {
                conditions = this.queryCondition;
            }
            const data = await this.zone.executeQuery(conditions);
            return data;
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'query CloudDB data fail'
            })
        }
    },

    async AVGQuery(condition, fieldName) {
        try {
            const data = await this.zone.executeAverageQuery(condition, fieldName);
            console.log(JSON.stringify(data));
            return data;
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'avg query CloudDB data fail'
            });
        }
    },

    async upsertObject(book) {
        try {
            await this.zone.executeUpsert(book);
            prompt.showToast({
                message: 'upsert data success'
            });
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'upsert data fail'
            });
        }
    },

    async deleteObject(book) {
        try {
            await this.zone.executeDelete(book);
            prompt.showToast({
                message: 'delete data success'
            });
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'delete data fail'
            });
        }
    },

    async subscribeSnap(condition) {
        try {
            const handler = await this.zone.subscribeSnapshot(condition, {
                onSnapshot: (snapshot, e) => {
                    if (!e) {
                        console.log(e);
                    }
                }
            });
            handlerList.push(handler);
            prompt.showToast({
                message: 'subscribe success'
            });
        } catch (error) {
            console.log(error);
            prompt.showToast({
                message: 'subscribe fail'
            });
        }
    },

    async removeSnapShot() {
        try {
            if (handlerList.length == 0) {
                prompt.showToast({
                    message: 'Not subscribed'
                });
                return;
            }
            handlerList.pop().remove();
            prompt.showToast({
                message: 'delete Subscription success'
            });
        } catch (error) {
            prompt.showToast({
                message: 'delete Subscription fail'
            });
            console.log(error);
        }
    },

    async executeTransaction(condition, entity, deleteEntity) {
        try {
            const ue = [];
            ue.push(entity);

            const de = [];
            de.push(deleteEntity);
            const d = await this.zone.runTransaction({
                apply: (transaction) => {
                    return new Promise((resolve, reject) => {
                        transaction.executeQuery(condition).then(data => {
                            console.log("trsanction data:" + JSON.stringify(data));
                            transaction.executeUpsert(ue);
                            transaction.executeDelete(de);
                            resolve(true);
                        }).catch(error => {
                            console.log(error);
                            resolve(false);
                        });
                    });
                }
            });
            console.log(d);
            prompt.showToast({
                message: 'execute transaction success'
            });
        } catch (error) {
            prompt.showToast({
                message: 'execute transaction fail'
            });
            console.log(error);
        }
    },

    constructCondition() {
        try {
            return CloudDBZoneQuery.where(BookInfo);
        } catch (error) {
            prompt.showToast({
                message: 'construct condition fail'
            });
            console.log(error);
        }
    },

    isValidDate(date) {
        return reg.test(date);
    },

    parseDate(date) {
        const year = date.getFullYear();
        let month = (date.getMonth() + 1).toString();
        let day = (date.getDate()).toString();
        if (month.length === 1) {
            month = "0" + month;
        }
        if (day.length === 1) {
            day = "0" + day;
        }
        const dateTime = year + "-" + month + "-" + day;
        return dateTime;
    },

    handleTime(date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        return date;
    },

    clearAllCondition() {
        this.queryCondition = null;
    }
}
