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

<template>
    <div>
        <div id="AddDiv" class="black_overlay_bg" style="display: none;">
            <div id="masks-div" class="black_overlay"></div>
            <form id="formAdd" class="form-horizontal add-book" style="background-color: white;" role="form">
                <div class="form-group" style="alignment: center;">
                    <p class="bookActionTitle">Add Book</p>
                </div>
                <div class="form-group">
                    <label for="BookName" class="col-sm-2 control-label text-label">Name</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="BookName"
                               placeholder="Please enter the title of the book">
                    </div>
                </div>
                <div class="form-group">
                    <label for="Author" class="col-sm-2 control-label text-label">Author</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="Author" placeholder="Please enter author">
                    </div>
                </div>
                <div class="form-group">
                    <label for="Price" class="col-sm-2 control-label text-label">Price</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="Price" placeholder="Please enter the price">
                    </div>
                </div>
                <div class="form-group">
                    <label for="Publisher" class="col-sm-2 control-label text-label">Publisher</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="Publisher" placeholder="Please enter publisher">
                    </div>
                </div>
                <div class="form-group">
                    <label for="Time" class="col-sm-2 control-label text-label">Time</label>
                    <div class="col-sm-12">
                        <input type="date" class="form-control" id="Time"
                               placeholder="Please enter the time of publication">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <button type="button" class="btn btn-default btn-lg col-sm-6" @click="addNewData">Add</button>
                        <button type="button" class="btn btn-default btn-lg col-sm-6" @click="cancel">Cancel</button>
                    </div>
                </div>
            </form>
            <form id="formQuery" class="form-horizontal add-book" style="background-color: white;" role="form">
                <div class="form-group">
                    <p class="bookActionTitle">Query</p>
                </div>
                <div class="form-group">
                    <label for="queryName" class="col-sm-2 control-label text-label">Name</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="queryName"
                               placeholder="Please enter the title of the book">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-label">Price</label>
                    <div class="col-sm-12 form-inline">
                        <input type="text" id="minPrice" class="form-control col-sm-5"
                               placeholder="Please enter min price">
                        <label class="col-sm-2">一</label>
                        <input type="text" id="maxPrice" class="form-control col-sm-5"
                               placeholder="Please enter max price">
                    </div>
                </div>
                <div class="form-group">
                    <label for="bookCount" class="col-sm-2 control-label text-label">Count</label>
                    <div class="col-sm-12">
                        <input type="text" class="form-control" id="bookCount" placeholder="Please enter the count">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12">
                        <button type="button" class="btn btn-default btn-lg col-sm-6" @click="queryBooks">Query</button>
                        <button type="button" class="btn btn-default btn-lg col-sm-6" style="float: right;"
                                @click="cancel">Cancel
                        </button>
                    </div>
                </div>
            </form>
        </div>
        <div class="header">
            <img src="../assets/addMore.svg" class="btn-image" @click="showAdd">
            <img src="../assets/query.svg" class="btn-image" @click="showQuery">
            <img src="../assets/mine.svg" class="btn-image loginImage" @click="login">
        </div>
        <table id="list-table" class="table" style="margin-left: 40px;">
            <!--            <caption>Book list</caption>-->
            <thead>
            <tr>
                <th>
                    <button class="btn" @click="orderBy('bookName')">Name</button>
                </th>
                <th>
                    <button class="btn" @click="orderBy('author')">Author</button>
                </th>
                <th>
                    <button class="btn" @click="orderBy('price')">Price</button>
                </th>
                <th>
                    <button class="btn" @click="orderBy('publisher')">Publisher</button>
                </th>
                <th>
                    <button class="btn" @click="orderBy('publishTime')">Time</button>
                </th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item, index) in list" v-bind:key="index">
                <td>{{ item.bookName }}</td>
                <td>{{ item.author }}</td>
                <td>{{ item.price }}</td>
                <td>{{ item.publisher }}</td>
                <td>{{ item.publishTime }}</td>
                <td>
                    <button class="btn" @click="editBookInformation(item)">edit</button>
                </td>
                <td>
                    <button class="btn" @click="deleteBook(item, index)">delete</button>
                </td>
            </tr>
            <tr></tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import {
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
    } from './index.js';

    export default {
        name: "quickstart",
        data() {
            return {
                msg: '123',
                list: bookList
            }
        },
        methods: {
            showAdd: function () {
                showAddView();
            },
            showQuery: function () {
                showQueryView();
            },
            cancel: function () {
                cancelAction();
            },
            addNewData: function () {
                addNewData();
            },
            queryBooks: function () {
                queryBookList();
            },
            editBookInformation: function (object) {
                editBookInformation(object);
            },
            deleteBook: function (object, index) {
                deleteBooks(object, index);
            },
            login: function () {
                login();
            },
            orderBy: function (fieldName) {
                orderByBookList(fieldName);
            }
        }
    }
</script>

<style scoped>
    .header {
        height: 100px;
        background-color: darkseagreen;
        /*position: fixed;*/
        padding-top: 25px;
        padding-left: 30px;
    }

    .btn-image {
        height: 50px;
        width: 50px;
        float: left;
        margin-left: 25px;
    }

    .loginImage {
        float: right;
        margin-right: 30px;
    }

    .black_overlay_bg {
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: none;
        position: absolute;
    }

    .black_overlay {
        width: 100%;
        height: 100%;
        background-color: black;
        /*z-index:1001;*/
        -moz-opacity: 0.5;
        opacity: .50;
        position: absolute;
        /*filter: alpha(opacity=80);*/
    }

    .add-book {
        margin-top: 30px;
        margin-bottom: 30px;
        width: 50%;
        right: 25%;
        top: 25%;
        position: fixed;
        border: 2px solid darkgray;
        border-radius: 10px;
    }

    .add-btn {
        /*background: url('../icon/addMore.png') no-repeat;*/
    }

    .bookActionTitle {
        color: dodgerblue;
        text-align: center;
        font-size: 30px;
        height: 40px;
        line-height: 40px;
        padding-top: 10px;
    }
</style>