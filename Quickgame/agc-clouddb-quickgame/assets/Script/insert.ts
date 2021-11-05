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

import {BookInfo} from './model/BookInfo';
import {operate} from "./operate";

const {ccclass, property} = cc._decorator;

// @ts-ignore
const Global = require('Global');

@ccclass
export default class NewClass extends cc.Component {

    @property(cc.EditBox)
    id: cc.EditBox = null;

    @property(cc.EditBox)
    bookName: cc.EditBox = null;

    @property(cc.EditBox)
    author: cc.EditBox = null;

    @property(cc.EditBox)
    price: cc.EditBox = null;

    @property(cc.EditBox)
    publisher: cc.EditBox = null;

    @property(cc.EditBox)
    publishTime: cc.EditBox = null;

    @property(cc.EditBox)
    shadowFlag: cc.EditBox = null;

    @property(cc.Label)
    errorMessage: cc.Label = null;


    async click() {
        const book = new BookInfo();
        if (!Global.NUMBER_REG.test(this.id.string)) {
            console.error('id is invalid');
            this.errorMessage.string = '[error] id is invalid';
            return;
        }
        book.setId(Number.parseFloat(this.id.string));
        book.setBookName(this.bookName.string);
        book.setAuthor(this.author.string);
        if (this.price.string && !Global.PRICE_REG.test(this.price.string)) {
            if (!Global.PRICE_REG.test(this.price.string)) {
                console.error('price is invalid');
                this.errorMessage.string = '[error] price is invalid';
                return;
            }
            book.setPrice(Number.parseFloat(this.price.string));
        }
        book.setPublisher(this.publisher.string);
        if (this.publishTime.string) {
            if (!Global.DATA_REG.test(this.publishTime.string)) {
                console.error('publishTime is invalid');
                this.errorMessage.string = '[error] publishTime is invalid';
                return;
            }
            book.setPublishTime(operate.handleTime(new Date(this.publishTime.string)));
        }
        if (this.shadowFlag.string) {
            if (!Global.BOOLEAN_REG.test(this.shadowFlag.string)) {
                console.error('shadowFlag is invalid');
                this.errorMessage.string = '[error] shadowFlag is invalid';
                return;
            }
            book.setShadowFlag(this.shadowFlag.string == 'true');
        }
        try {
            await operate.insertBook(book);
            cc.director.loadScene('Main');
        } catch (e) {
            console.error('insert book failed');
            console.error(JSON.stringify(e));
            this.errorMessage.string = '[error] ' + JSON.stringify(e);
        }
    }

    back() {
        cc.director.loadScene('Main');
    }
}
