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
  <div class="login-container">
    <el-form status-icon label-position="left" label-width="0px"
             class="demo-ruleForm login-page">
      <h3 class="title">JS-SDK-SelfBuild</h3>
      <el-select v-model="provider" placeholder="login mode select" @change="providerChange">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <br/>
      <br/>
      <el-form-item style="width: 100%;">
        <el-row>
          <div class="row-flex flex-middle">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="please input token"
              v-model="selfBuildToken"></el-input>
              <br/>
              <br/>
            <el-button type="primary" size="medium" style="width: 40%;" @click="loginWithSelfBuild">loginWithSelfBuild</el-button>
          </div>
        </el-row>
        <el-button @click="getUserInfo();drawer = true" style="width: 80%;" type="primary">
          Login User details
        </el-button>
        <br/>
        <br/>
        <el-drawer
          title="User Info"
          :visible.sync="drawer"
          :direction="direction">
          <el-form :label-position="labelPosition" style="width: 20%;" label-width="120px" :model="accountInfo"
                   class="accountInfo">
            <el-form-item label="UID:">{{ accountInfo.uid }}</el-form-item>
            <el-form-item label="Anonymous:">{{ accountInfo.anonymous }}</el-form-item>
            <el-form-item label="displayName:">{{ accountInfo.displayName }}</el-form-item>
            <el-form-item label="email:">{{ accountInfo.email }}</el-form-item>
            <el-form-item label="phone:">{{ accountInfo.phone }}</el-form-item>
            <el-form-item label="photoUrl:">{{ accountInfo.photoUrl }}</el-form-item>
            <el-form-item label="providerId:">{{ accountInfo.providerId }}</el-form-item>
            <el-form-item label="emailVerified:">{{ accountInfo.emailVerified }}</el-form-item>
            <el-form-item label="passwordSetted:">{{ accountInfo.passwordSetted }}</el-form-item>
          </el-form>
          <br/>
          <el-button type="primary" size="medium" style="width: 50%;" @click="logOut">log out</el-button>
          <br/><br/>
          <el-button type="danger" size="medium" style="width: 50%;" @click="deleteUser">delete user</el-button>
        </el-drawer>

      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import * as agc from './auth';
  import {getSaveMode, setSaveMode} from './storage';
  import {configInstance} from "./config";
  import "@agconnect/storage";
  import {providerChangeUtil} from "./utils";

  export default {
    data() {
      return {
        drawer: false,
        direction: 'rtl',
        saveMode: '0',
        accountInfo: {
          uid: '',
          anonymous: '',
          displayName: '',
          email: '',
          phone: '',
          photoUrl: '',
          providerId: '',
          emailVerified:'',
          passwordSetted:'',
        },
        selfBuildToken:'',
        labelPosition: 'left',
        provider: 'selfBuild',
        options: [{
          value: 'phone',
          label: 'phone'
        }, {
          value: 'email',
          label: 'email'
        }, {
          value: 'QQ',
          label: 'QQ'
        }, {
          value: 'weChat',
          label: 'weChat'
        },
          {
            value: 'selfBuild',
            label: 'selfBuild'
          }],
      };
    },
    // initialize demo
    async created() {
      configInstance();
      agc.setCryptImp(new agc.Crypt());
      agc.setAuthCryptImp(new agc.AuthCrypt());
      // Gets the storage location last set by the user in the demo and inherits it
      this.saveMode = await getSaveMode('saveMode');
      if (!this.saveMode) {
        this.saveMode = '0';
      }
      agc.setUserInfoPersistence(parseInt(this.saveMode));

      let providerParam = this.$router.history.current.query.provider;
      if (providerParam) {
        this.provider = providerParam;
      }
    },
    methods: {
      loginWithSelfBuild(){
        let token = this.selfBuildToken;
        agc.loginWithSelfBuild(token).then((ret) => {
          alert('login successfully!');
          this.getUserInfo();
        }).catch((err) => {
          alert(err.message);
        });
      },
      async setStorageMode() {
        // Save the storage location locally
        await setSaveMode('saveMode', this.saveMode);
        this.$router.go(0);
      },
      getUserInfo() {
        agc.getUserInfo().then((user) => {
          if (user) {
            this.accountInfo.anonymous = user.isAnonymous();
            this.accountInfo.uid = user.getUid();
            this.accountInfo.displayName = user.getDisplayName();
            this.accountInfo.email = user.getEmail();
            this.accountInfo.phone = user.getPhone();
            this.accountInfo.photoUrl = user.getPhotoUrl();
            this.accountInfo.providerId = user.getProviderId();
            this.accountInfo.emailVerified = user.getEmailVerified();
            this.accountInfo.passwordSetted = user.getPasswordSetted();
          } else {
            this.accountInfo.anonymous = "";
            this.accountInfo.uid = "";
            this.accountInfo.displayName = "";
            this.accountInfo.email = "";
            this.accountInfo.phone = "";
            this.accountInfo.photoUrl = "";
            this.accountInfo.providerId = "";
            this.accountInfo.emailVerified = "";
            this.accountInfo.passwordSetted = "";
          }
        }).catch((err) => {
          console.error("getuserinfo err:", err);
        });
      },
      logOut() {
        agc.logout().then(() => {
          alert('log out!');
          this.accountInfo = {
            uid: '',
            anonymous: '',
            displayName: '',
            email: '',
            phone: '',
            photoUrl: '',
            providerId: '',
            emailVerified:'',
            passwordSetted:'',
          };
        }).catch((err) => {
          alert(err.message);
        });
      },
      deleteUser() {
        this.$confirm('Permanently delete current user, continue?', 'Tips', {
          confirmButtonText: 'YES',
          cancelButtonText: 'NO',
          type: 'warning',
        }).then(async () => {
          await agc.deleteUser().then(() => {
            alert('delete User OK')
            this.accountInfo = {
              uid: '',
              anonymous: '',
              displayName: '',
              email: '',
              phone: '',
              photoUrl: '',
              providerId: '',
              emailVerified:'',
              passwordSetted:'',
            };
          });
        }).catch((err) => {
          alert(err.message);
        });
      },
      providerChange() {
        providerChangeUtil(this);
      },
    },
  };
</script>
<style scoped>
  .login-container {
    width: 100%;
    height: 100%;
  }

  .row-flex{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    margin: 20px 0;
  }

  .login-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: 60px auto;
    width: 420px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  label.el-checkbox.remember {
    margin: 0px 0px 15px;
    text-align: left;
  }
  .accountInfo {
    margin: 20px 60px auto;
  }
  .flex-middle{
    justify-content: center;
  }
</style>
