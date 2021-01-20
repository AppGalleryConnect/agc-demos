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

import {BookInfo} from './model/BookInfo';
import {
    agcLogin,
    executeDelete,
    executeQueryAllBooks,
    executeQueryComposite,
    executeUpsert,
    openCloudDBZone,
    queryDataByOrderWay,
    subscribeSnapshot
} from './AGCManager';
import {AGConnectCloudDBExceptionCode} from '@agconnect/database';

let bookList = [];

let editBook = null;

// orderBy map
const orderByMap = {
    name: 1, // 1 asc 0 desc
    price: 1,
    author: 1,
    publisher: 1,
    publishTime: 1
}

// dom operation
function showAddView() {
    displayView(1);
}

function showQueryView() {
    displayView(2);
}

function cancelAction() {
    document.getElementById('AddDiv').style.display = 'none';
}

function addNewData() {
    const bookName = document.getElementById('BookName').value;
    const author = document.getElementById('Author').value;
    const price = document.getElementById('Price').value;
    const publisher = document.getElementById('Publisher').value;
    const time = document.getElementById('Time').value;

    const book = new BookInfo();
    book.bookName = bookName;
    book.author = author;
    book.price = parseFloat(price);
    book.publisher = publisher;
    book.publishTime = new Date(time.replace(/-/, "/"))
    book.id = parseInt(RndNum(9));

    // add book
    addBook(book);

    document.getElementById('AddDiv').style.display = 'none';
}

function RndNum(n) {
    let rnd = "";
    for (let i = 0; i < n; i++) {
        rnd += Math.floor(Math.random() * 10);
    }
    return rnd;
}

function displayView(type) {
    document.getElementById('AddDiv').style.display = 'block';
    if (type === 1) {
        document.getElementById('formAdd').style.display = 'block';
        document.getElementById('formQuery').style.display = 'none';
    } else {
        document.getElementById('formAdd').style.display = 'none';
        document.getElementById('formQuery').style.display = 'block';
    }
}

function editBookInformation(object) {
    displayView(1);
    document.getElementById('BookName').value = object.bookName;
    document.getElementById('Author').value = object.author;
    document.getElementById('Price').value = object.price;
    document.getElementById('Publisher').value = object.publisher;
    document.getElementById('Time').value = object.publishTime;

    editBook = object;
}

async function login() {
    const state = await agcLogin();
    if (state === true) {
        console.log('login success');
        // update login status
        setTimeout(function () {
            openCloudDBZone().then(res => {
                if (res === true) {
                    console.log('open zone success');
                    // subscribe
                    subscribeBookList();
                } else {
                    console.log('open zone failed');
                }
            })
        }, 2000);
    }
}

function queryBookList() {
    const queryName = document.getElementById('queryName').value;
    const minPrice = document.getElementById('minPrice').value;
    const maxPrice = document.getElementById('maxPrice').value;
    const bookCount = document.getElementById('bookCount').value;

    const object = {
        name: queryName,
        minPrice: minPrice,
        maxPrice: maxPrice,
        bookCount: bookCount,
    }
    console.log(object);
    executeQueryComposite(object).then(res => {
        console.log(res);
        bookList.length = 0;
        for (let i = 0; i < res.length; i++) {
            const bookInfo = res[i];
            const time = bookInfo.publishTime;
            bookInfo.publishTime = dateToString(time);
            bookList.push(bookInfo);
        }
        document.getElementById('AddDiv').style.display = 'none';
    });
}

async function queryBooks() {
    await executeQueryAllBooks().then(array => {
        bookList.length = 0;
        for (let i = 0; i < array.length; i++) {
            const bookInfo = array[i];
            const time = bookInfo.publishTime;
            bookInfo.publishTime = dateToString(time);
            bookList.push(bookInfo);
        }
    });
}

async function subscribeBookList() {
    const onSnapshotListener = {
        onSnapshot: (snapshot, e) => {
            if (e !== null && e !== undefined && e.code !== AGConnectCloudDBExceptionCode.Ok) {
                console.log('subscribeSnapshot error:');
                console.log(e);
            } else {
                console.log('subscribeSnapshot success');
                console.log(snapshot);
                if (snapshot.getSnapshotObjects().length > 0) {
                    const array = snapshot.getSnapshotObjects();
                    bookList.length = 0;
                    for (let i = 0; i < array.length; i++) {
                        const bookInfo = array[i];
                        const time = bookInfo.publishTime;
                        bookInfo.publishTime = dateToString(time);
                        bookList.push(bookInfo);
                    }
                }
            }
        }
    };
    await subscribeSnapshot(onSnapshotListener).then(listenerHandler => {
        console.log(listenerHandler);
    })
}

async function orderByBookList(fieldName) {
    const type = orderByMap[fieldName];
    const array = await queryDataByOrderWay(fieldName, type);
    // change sort type 1: ASC 0: DESC
    if (type === 1) {
        orderByMap[fieldName] = 0;
    } else {
        orderByMap[fieldName] = 1;
    }

    bookList.splice(0, bookList.length);
    for (let i = 0; i < array.length; i++) {
        const bookInfo = array[i];
        const time = bookInfo.publishTime;
        bookInfo.publishTime = dateToString(time);
        bookList.push(bookInfo);
    }
}

function deleteBooks(object, index) {
    const book = bookList[index];
    const deleteBook = new BookInfo();
    deleteBook.id = book.id;
    executeDelete(deleteBook).then(res => {
        if (res > 0) {
            bookList.splice(index, 1);
        } else {
            alert('delete failed');
        }
    });
}

function addBook(bookInfo) {
    if (editBook !== null && editBook !== undefined) {
        bookInfo.id = editBook.id;
        bookInfo.shadowFlag = true;
    }

    executeUpsert(bookInfo).then(res => {
        if (res > 0) {
            queryBooks();
        } else {
            alert('upsert book failed');
        }
        editBook = null;
    })
}

function dateToString(date) {
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
}

export {
    bookList,
    showAddView,
    showQueryView,
    cancelAction,
    addNewData,
    queryBookList,
    editBookInformation,
    deleteBooks,
    login,
    orderByBookList
}


