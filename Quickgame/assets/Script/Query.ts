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

import { operate } from "./operate";
import { BookInfo } from './model/BookInfo';

// @ts-ignore
const Global = require('Global');
const {ccclass, property} = cc._decorator;


@ccclass
export default class NewClass extends cc.Component {

    @property(cc.EditBox)
    conditionType: cc.EditBox = null;

    @property(cc.EditBox)
    fieldName: cc.EditBox = null;

    @property(cc.EditBox)
    value: cc.EditBox = null;

    @property(cc.EditBox)
    offset: cc.EditBox = null;

    @property(cc.EditBox)
    number: cc.EditBox = null;

    @property(cc.Label)
    errorMessage: cc.Label = null;

    protected onLoad() {
        const condition = operate.getCondition();
        if (condition) {
            this.conditionType.string = condition.getQueryConditions()[0].conditionType;
            this.fieldName.string = condition.getQueryConditions()[0].fieldName;
            if (condition.getQueryConditions()[0].conditionType == 'Limit') {
                this.offset.string = condition.getQueryConditions()[0].value.offset;
                this.number.string = condition.getQueryConditions()[0].value.number;
                return;
            }
            this.value.string = condition.getQueryConditions()[0].value;
        }
    }

    async submit_query() {
        try {
            let queryCondition = new agconnect.cloudDB.CloudDBZoneQuery.where(BookInfo);
            const type = this.conditionType.string;
            const fieldName = this.fieldName.string;
            let localValue = this.value.string;
            if (type !== 'Limit') {
                operate.checkFieldName(fieldName);
            }
            if (type !== 'In' && type !== 'Limit') {
                switch (fieldName) {
                    case 'price':
                    case 'id':
                        localValue = Number.parseFloat(localValue);
                        break;
                    case 'publishTime':
                        if (!Global.DATA_REG.test(localValue)) {
                            console.error('pushlishTime is invalid');
                            this.errorMessage.string = 'pushlishTime is invalid';
                            return;
                        }
                        localValue = operate.handleTime(new Date(localValue));
                        break;
                    default:
                        break;
                }
            }
            switch (type) {
                case 'EqualTo':
                    queryCondition = queryCondition.equalTo(fieldName, localValue);
                    break;
                case 'BeginWith':
                    queryCondition = queryCondition.beginsWith(fieldName, localValue);
                    break;
                case 'EndWith':
                    queryCondition = queryCondition.endsWith(fieldName, localValue);
                    break;
                case 'Contain':
                    queryCondition = queryCondition.contains(fieldName, localValue);
                    break;
                case 'NotEqualTo':
                    queryCondition = queryCondition.notEqualTo(fieldName, localValue);
                    break;
                case 'GreaterThan':
                    queryCondition = queryCondition.greaterThan(fieldName, localValue);
                    break;
                case 'GreaterThanOrEqualTo':
                    queryCondition = queryCondition.greaterThanOrEqualTo(fieldName, localValue);
                    break;
                case 'LessThan':
                    queryCondition = queryCondition.lessThan(fieldName, localValue);
                    break;
                case 'LessThanOrEqualTo':
                    queryCondition = queryCondition.lessThanOrEqualTo(fieldName, localValue);
                    break;
                case 'In':
                    const arr = localValue.split(',');
                    switch (fieldName) {
                        case 'price':
                        case 'id':
                            for (let i = 0; i < arr.length; i++) {
                                arr[i] = Number.parseFloat(arr[i]);
                            }
                            break;
                        case 'publishTime':
                            for (let i = 0; i < arr.length; i++) {
                                if (!Global.DATA_REG.test(localValue)) {
                                    console.error('pushlishTime is invalid');
                                    this.errorMessage.string = 'pushlishTime is invalid';
                                    return;
                                }
                                arr[i] = operate.handleTime(new Date(arr[i]));
                            }
                            break;
                        default:
                            break;
                    }
                    queryCondition = queryCondition.in(fieldName, arr);
                    break;
                case 'IsNull':
                    queryCondition = queryCondition.isNull(fieldName, localValue);
                    break;
                case 'IsNotNull':
                    queryCondition = queryCondition.isNotNull(fieldName, localValue);
                    break;
                case 'Limit':
                    queryCondition = queryCondition.limit(Number.parseFloat(this.number.string),
                        Number.parseFloat(this.offset.string));
                    break;
                default:
                    this.errorMessage.string = 'conditionType is invalid';
                    return;
            }
            operate.setQueryCondiiton(queryCondition);
            cc.director.loadScene('Main');
        } catch (e) {
            this.errorMessage.string = '[error] ' +  JSON.stringify(e);
        }
    }

    back() {
        cc.director.loadScene('Main');
    }

    clearAll() {
        operate.clearAllCondiiton();
        cc.director.loadScene('Main');
    }
}
