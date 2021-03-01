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
  <div class="qq_login-container">
    <el-form status-icon label-position="left" label-width="0px" class="demo-ruleForm login-page">
      <h3 class="title">JS-SDK-QQ</h3>

      <el-form-item style="width: 100%;">
        <el-row>
          <el-button type="success" size="medium" style="width: 28%;" @click="QQLogin" round>QQLogin</el-button>
          <el-button type="success" size="medium" style="width: 28%;" @click="doLink" round>link</el-button>
          <el-button type="success" size="medium" style="width: 28%;" @click="doUnLink" round>unlink</el-button>
        </el-row>
        <br/>
        <el-collapse accordion>
          <el-collapse-item>
            <template slot="title">login mode</template>
            <el-select v-model="provider" placeholder="login mode select" @change="providerChange">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <br/>
          </el-collapse-item>
          <el-collapse-item>
            <template slot="title">storage mode</template>
            <el-radio-group v-model="saveMode" @change="setStorageMode">
              <el-radio label="2">MEMORY</el-radio>
              <el-radio label="1">SESSION</el-radio>
              <el-radio label="0">INDEXDB</el-radio>
            </el-radio-group>
            <br/>
          </el-collapse-item>
          <el-collapse-item>
            <template slot="title">User info</template>
            <el-form :label-position="labelPosition" style="width: 20%;" label-width="120px" :model="accountInfo">
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
            <br/><br/>
            <el-button type="primary" size="medium" style="width: 50%;" @click="logOut">log out</el-button>
            <br/><br/>
            <el-button type="danger" size="medium" style="width: 50%;" @click="deleteUser">delete user</el-button>
          </el-collapse-item>
        </el-collapse>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import * as agc from './auth';
  import {getSaveMode, setSaveMode} from './storage';
  import {providerChangeUtil} from "./utils";
  import {configInstance} from "./config";
  import {loginWithQQ} from "./auth";

  export default {
    data() {
      return {
        saveMode: '6',
        provider: 'QQ',
        dialogVisible: false,
        labelPosition: 'left',
        accountInfo: {
          uid: '',
          anonymous: '',
          displayName: '',
          email: '',
          phone: '',
          photoUrl: '',
          providerId: '',
          emailVerified: '',
          passwordSetted: '',
        },
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
        }],
        value: ''
      };
    },
    async created() {
      configInstance();
      this.saveMode = await getSaveMode('saveMode');
      if (!this.saveMode) {
        this.saveMode = '2';
      }
      agc.setUserInfoPersistence(parseInt(this.saveMode));
      agc.setCryptImp(new agc.Crypt());
      agc.setAuthCryptImp(new agc.AuthCrypt());
      await this.callbackFunction();
      setTimeout(async () => {
        this.getUserInfo();
      }, 3000);
    },
    methods: {
      async setStorageMode() {
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
          console.error("----getuserinfo err:", err);
        });
      },
      QQLogin() {
        QC.Login.showPopup({
          appId: "101890031",
          redirectURI: "http://127.0.0.1:8080/#/QQLoginEmptyPage"
        });
      },

      /**
       * get the access_token taken after QQ authentication
       * @param self
       */
      async callbackFunction() {
        let hash = window.location.hash;
        if (hash.indexOf("access_token") == -1) {
          return;
        }

        await QC.Login.getMe(async function (openID, accessToken) {
          await agc.getUserInfo().then(async (res) => {
            if (res) {
              await agc.link('QQ', accessToken, openID, '').then((ret) => {
                alert('link QQ OK');
              }).catch((err) => {
                alert(JSON.stringify(err));
              });
            } else {
              await loginWithQQ(accessToken, openID, true).then(result => {
                alert('login With QQ OK');
              }).catch(error => {
                alert(JSON.stringify(err));
              });
            }
          });
        });
        this.getUserInfo();
      },

      providerChange() {
        providerChangeUtil(this)
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
            emailVerified: '',
            passwordSetted: '',
          };
        }).catch((err) => {
          alert(JSON.stringify(err));
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
              emailVerified: '',
              passwordSetted: '',
            };
          });
        }).catch((err) => {
          alert(JSON.stringify(err));
        });
      },
      doLink() {
        this.QQLogin();
      },
      doUnLink() {
        agc.unlink(6).then((ret) => {
          alert('UNlink QQ OK');
          this.getUserInfo();
        }).catch((err) => {
          alert(JSON.stringify(err));
        });
      },
    },
  };
</script>
<style scoped>
  .login-container {
    width: 100%;
    height: 100%;
  }

  .login-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: 180px auto;
    width: 400px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  label.el-checkbox.remember {
    margin: 0px 0px 15px;
    text-align: left;
  }
</style>
