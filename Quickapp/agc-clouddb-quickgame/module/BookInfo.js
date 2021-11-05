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

class BookInfo {
    constructor() {
        this.id = undefined;
        this.bookName = undefined;
        this.author = undefined;
        this.price = undefined;
        this.publisher = undefined;
        this.publishTime = undefined;
        this.shadowFlag = true;
    }

    setId(id) {
        this.id = id;
    }

    getId() {
        return this.id;
    }

    setBookName(bookName) {
        this.bookName = bookName;
    }

    getBookName() {
        return this.bookName;
    }

    setAuthor(author) {
        this.author = author;
    }

    getAuthor() {
        return this.author;
    }

    setPrice(price) {
        this.price = price;
    }

    getPrice() {
        return this.price;
    }

    setPublisher(publisher) {
        this.publisher = publisher;
    }

    getPublisher() {
        return this.publisher;
    }

    setPublishTime(publishTime) {
        this.publishTime = publishTime;
    }

    getPublishTime() {
        return this.publishTime;
    }

    setShadowFlag(shadowFlag) {
        this.shadowFlag = shadowFlag;
    }

    getShadowFlag() {
        return this.shadowFlag;
    }
}

BookInfo.className = 'BookInfo';

export {BookInfo}