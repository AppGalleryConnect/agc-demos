<template>
  <div class="hello">
    <div v-show="!isLogin">
        <el-button type="primary" @click="signInAnonymously">Login</el-button>
    </div>
    <div v-show="isLogin" style="max-width:1600px;margin:auto;">
        <h1>AGCCloudStorageDemo</h1>
        <div style="display:flex;margin-bottom:30px;">
            <el-button type="primary" size="medium" @click="getFileList('')">Get FileList</el-button>
            <el-button type="primary" size="medium" @click="getFileListAll('')">
                Get FileList All
            </el-button>
            <el-button type="primary" size="medium" @click="uploadString">
                Upload String
            </el-button>
            <el-upload action :on-change="uploadFile" :auto-upload="false">
                <el-button type="primary" size="medium" style="width: 100%;margin-left:10px;">Upload File</el-button>
            </el-upload>
        </div>
        <el-table
            ref="multipleTable"
            border
            size="medium"
            :data="list">
            <el-table-column
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column width="80px" label="index">
                <template v-slot="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column width="100px" label="type">
                <template v-slot="scope">
                    {{ scope.row.isFile ? 'file' : 'directory' }}
                </template>
            </el-table-column>
            <el-table-column
                width="150px"
                label="name"
                prop="name"
                show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="operation">
                <template v-slot="scope">
                    <el-button
                        v-if="!scope.row.isFile"
                        type="success"
                        size="medium"
                        @click="getFileList(scope.row)"
                    >
                        Get FileList
                    </el-button>
                    <el-button
                        v-if="!scope.row.isFile"
                        type="success"
                        size="medium"
                        @click="getFileListAll(scope.row)"
                    >
                        Get FileList All
                    </el-button>
                    <el-button
                        v-if="scope.row.isFile"
                        type="success"
                        size="medium"
                        @click="downloadFile(scope.row)"
                    >
                        Download File
                    </el-button>
                    <el-button
                        v-if="scope.row.isFile"
                        type="success"
                        size="medium"
                        @click="getFileMetadata(scope.row)"
                    >
                        Get FileMetadata
                    </el-button>
                    <el-button
                        v-if="scope.row.isFile"
                        type="success"
                        size="medium"
                        @click="updateFileMetadata(scope.row)"
                    >
                        Update FileMetadata
                    </el-button>
                    <el-button v-if="scope.row.isFile" type="danger" size="medium" @click="deleteFile(scope.row)">
                        Delete File
                    </el-button>
                    <el-button type="success" size="medium" @click="toString(scope.row)">
                        To String
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
  </div>
</template>

<script>
import agconnect from "@agconnect/api";
import "@agconnect/instance";
import "@agconnect/auth";
import "@agconnect/cloudstorage";
// setSaveMode

import { agConnectConfig } from "../agConnectConfig";
const config = agConnectConfig
export default {
    name: 'Page',
    data(){
        return {
            list:[],
            isLogin:false,
            ref:{}
        }
    },
    mounted(){
        agconnect.instance().configInstance(config)
    },
    methods:{
        async signInAnonymously () {
            agconnect
                .auth()
                .signInAnonymously()
                .then(() => {
                    alert('login successfully!')
                    this.isLogin = true
                    this.ref = agconnect.cloudStorage().storageReference()
                })
                .catch(() => {
                    return Promise.reject('sign in anonymously failed')
                })
            
        },
        downloadFile(row) {
            const child = this.ref.child(row.path)
            child.getDownloadURL().then(function (downloadURL) {
                alert(downloadURL)
                console.log(downloadURL)
            })
        },
        getFileMetadata(row) {
            const child = this.ref.child(row.path)
            child.getFileMetadata().then((res) => {
                console.log(res)
            })
        },
        updateFileMetadata(row) {
            const child = this.ref.child(row.path)
            child.updateFileMetadata({
                cacheControl: 'helloworld',
                contentDisposition: 'helloworld',
                contentEncoding: 'helloworld',
                contentLanguage: 'helloworld',
                contentType: 'helloworld',
                customMetadata: {
                    hello: 'kitty'
                }
            }).then((res) => {
                console.log(res)
            })
        },
        getFileList(row) {
            var path = row && row.path ? row.path : '';
            const child = this.ref.child(path)
            child.list({ maxResults: 5 }).then((res) => {
                this.list = [...res.dirList.map(item => { item.isFile = false; item.select = false; return item }), ...res.fileList.map(item => { item.isFile = true; item.select = false; return item })]
                this.nextMarker = res.pageMarker
                console.log(this.list)
            }).catch(err => {
                console.log(err)
            })
        },
        getFileListAll(row) {
            const child = this.ref.child(row && row.path ? row.path : '')
            child.listAll()
            .then((res) => {
                console.log('res', res)
                this.list = [...res.dirList.map(item => { item.isFile = false; item.select = false; return item }), ...res.fileList.map(item => { item.isFile = true; item.select = false; return item })]
            })
            .catch((err) => {
                console.log(err)
            })
        },
        deleteFile(row) {
            const child = this.ref.child(row.path)
            child.delete().then(() => {
                alert('delete success')
                console.log('delete success')
            }).catch(err => {
                console.log(err)
            })
        },
        toString (row) {
            const child = this.ref.child(row.path)
            alert(child.toString())
            console.log(child.toString())
        },
        uploadFile(file) {
            const path = 'jssdk/' + file.name
            const metadata = {
                cacheControl: 'helloworld',
                contentDisposition: 'helloworld',
                contentEncoding: 'helloworld',
                contentLanguage: 'helloworld',
                contentType: 'helloworld',
                customMetadata: {
                    hello: 'kitty'
                }
            }
            var uploadTask = this.ref.child(path).put(file.raw, metadata)
            this.printUploadPercent(uploadTask)
        },
        uploadString(){
            const path = 'jssdk/test1.txt'
            const format = 'raw'
            const uploadMessage = 'message'
            const metadata = {
                cacheControl: 'helloworld',
                contentDisposition: 'helloworld',
                contentEncoding: 'helloworld',
                contentLanguage: 'helloworld',
                contentType: 'helloworld',
                customMetadata: {
                    hello: 'kitty'
                }
            }
            var uploadTask = this.ref.child(path).putString(uploadMessage, format, metadata);
            this.printUploadPercent(uploadTask)
        },
        printUploadPercent (uploadTask) {
            uploadTask.on('state_changed', function (snapshot) {
                if(!snapshot){
                    console.log('Upload Result is null')
                    return;
                }
                if(snapshot.totalByteCount == 0){
                    console.log('Upload File is empty')
                    return;
                }
                var progress = (snapshot.bytesTransferred / snapshot.totalByteCount) * 100
                console.log('Upload is ' + progress.toFixed(1) + '% done')
                switch (snapshot.state) {
                    case 'paused':
                        console.log('Upload is paused')
                        break
                    case 'running':
                        console.log('Upload is running')
                        break
                    case 'success':
                        console.log('Upload is success')
                        break
                    case 'canceled':
                        console.log('Upload is canceled')
                        break
                    case 'error':
                        console.log('Upload is error')
                        break
                }
            }, function (snapshot) {
                switch (snapshot.state) {
                    case 'paused':
                        console.log('Upload is paused')
                        break
                    case 'running':
                        console.log('Upload is running')
                        break
                    case 'success':
                        console.log('Upload is success')
                        break
                    case 'canceled':
                        console.log('Upload is canceled')
                        break
                    case 'error':
                        console.log('Upload is error')
                        break
                }
            }, function () {
                console.log('Upload is success')
            })
        }
    }
}
</script>

<style scoped>

</style>
