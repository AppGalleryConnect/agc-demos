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
    <el-form :model="dataForm_sdk" :rules="rules" status-icon label-position="left" label-width="0px"
             class="demo-ruleForm login-page">
      <h3 class="title">JS-SDK</h3>
      <el-form-item prop="account">
        <el-input type="text" v-model="dataForm_sdk.account" auto-complete="off" placeholder="Account"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="dataForm_sdk.password" auto-complete="off" placeholder="Password"></el-input>
      </el-form-item>
      <el-form-item prop="verifyCode">
        <el-input type="text" v-model="dataForm_sdk.verifyCode" auto-complete="off" placeholder="PIN">
          <el-button slot="suffix" type="info" size="mini" @click="getVerifyCode">get PIN</el-button>
        </el-input>
      </el-form-item>
      <br/>
      <el-form-item style="width: 100%;">
        <el-row>
          <el-button type="primary" size="medium" style="width: 28%;" @click="loginByPwd">login by PWD</el-button>
          <el-button type="primary" size="medium" style="width: 28%;" @click="loginByVerifyCode">login by PIN
          </el-button>
          <el-button type="primary" size="medium" style="width: 36%;" @click="loginAnonymously">login anonymously
          </el-button>
        </el-row>
        <br/>
        <el-row>
          <el-button type="success" size="medium" style="width: 28%;" @click="createUser" round>create user</el-button>
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
  import "@agconnect/storage";

  export default {
    data() {
      return {
        saveMode: '0',
        provider: 'phone',
        customProvider: '0',
        dataForm_sdk: {
          account: '',
          password: '',
          verifyCode: '',
        },
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
        rules: {
          email: [{required: true, message: 'input your account', trigger: 'blur'}],
          password: [{required: false, message: 'input your password', trigger: 'blur'}],
        },
        dialogVisible: false,
        labelPosition: 'left',
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
    // initialize demo
    async created() {
      configInstance();

      // Gets the storage location last set by the user in the demo and inherits it
      this.saveMode = await getSaveMode('saveMode');
      if (!this.saveMode) {
        this.saveMode = '0';
      }
      agc.setUserInfoPersistence(parseInt(this.saveMode));
      agc.setCryptImp(new agc.Crypt());
      agc.setAuthCryptImp(new agc.AuthCrypt());
      let providerParam = this.$router.history.current.query.provider;
      if (providerParam) {
        this.provider = providerParam;
      }

      //Check whether any user is logged in. If so, refresh the page to the user information page
      this.getUserInfo();
    },
    methods: {
      async setStorageMode() {
        // Save the storage location locally
        await setSaveMode('saveMode', this.saveMode);
        this.$router.go(0);
      },
      // Change login mode
      providerChange() {
        this.dataForm_sdk.account = '';
        this.dataForm_sdk.password = '';
        this.dataForm_sdk.verifyCode = '';

        providerChangeUtil(this);
      },
      loginByPwd() {
        if (this.dataForm_sdk.password == '') {
          alert('Please input password!');
          return;
        }
        switch (this.provider) {
          case 'phone':
            agc.loginWithPhone('86', this.dataForm_sdk.account, this.dataForm_sdk.password)
              .then((res) => {
                alert('login successfully!');
                this.getUserInfo();
              }).catch((err) => {
              alert(err);
            });
            break;
          case 'email':
            agc.loginWithEmail(this.dataForm_sdk.account, this.dataForm_sdk.password).then(
              () => {
                alert('login successfully!');
                this.getUserInfo();
              }, (reason) => {
                alert(reason);
              },
            );
            break;
          default:
            break;
        }
      },
      loginByVerifyCode() {
        if (this.dataForm_sdk.verifyCode == '') {
          alert('Please input verifyCode!');
          return;
        }

        switch (this.provider) {
          case 'phone':
            agc.loginWithPhone(
              '86',
              this.dataForm_sdk.account,
              this.dataForm_sdk.password,
              this.dataForm_sdk.verifyCode,
            ).then((res) => {
              alert('login successfully!');
              this.getUserInfo();
            }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          case 'email':
            agc.loginWithEmail(
              this.dataForm_sdk.account,
              this.dataForm_sdk.password,
              this.dataForm_sdk.verifyCode,
            ).then((res) => {
              alert('login successfully!');
              this.getUserInfo();
            }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          default:
            break;
        }
      },
      loginAnonymously() {
        agc.loginAnonymously().then((res) => {
          alert('login successfully!');
          this.getUserInfo();
        }).catch((err) => {
          alert(JSON.stringify(err));
        });
      },
      createUser() {
        switch (this.provider) {
          case 'phone':
            agc.createPhoneUser(
              '86',
              this.dataForm_sdk.account,
              this.dataForm_sdk.verifyCode,
              this.dataForm_sdk.password,
            ).then((res) => {
              alert('create user successfully!');
              this.getUserInfo();
            }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          case 'email':
            agc.createEmailUser(
              this.dataForm_sdk.account,
              this.dataForm_sdk.password,
              this.dataForm_sdk.verifyCode,
            ).then((res) => {
              alert('create user successfully!');
              this.getUserInfo();
            }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          default:
            break;
        }
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
      getVerifyCode() {
        this.dataForm_sdk.verifyCode = '';
        switch (this.provider) {
          case 'phone':
            agc.getPhoneVerifyCode('86', this.dataForm_sdk.account, 'zh_CN', 90)
              .then((ret) => {
                alert('verify code sent by AGC!');
              }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          case 'email':
            agc.getEmailVerifyCode(this.dataForm_sdk.account, 'zh_CN', 120)
              .then((ret) => {
                alert('verify code sent by AGC!');
              }).catch((err) => {
              alert(JSON.stringify(err));
            });
            break;
          default:
            break;
        }
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
              emailVerified:'',
              passwordSetted:'',
            };
          });
        }).catch((err) => {
          alert(JSON.stringify(err));
        });
      },

      doLink() {
        if (this.provider == 'phone') {
          agc.link('phone', this.dataForm_sdk.account, this.dataForm_sdk.password, this.dataForm_sdk.verifyCode).then((ret) => {
            alert('link phone OK');
            this.getUserInfo();
          }).catch((err) => {
            alert(JSON.stringify(err));
          });
        } else if (this.provider == 'email') {
          agc.link('email', this.dataForm_sdk.account, this.dataForm_sdk.password, this.dataForm_sdk.verifyCode).then((ret) => {
            alert('link email OK');
            this.getUserInfo();
          }).catch((err) => {
            alert(JSON.stringify(err));
          });
        }
      },
      doUnLink() {
        if (this.provider == 'phone') {
          agc.unlink(11).then((ret) => {
            alert('UNlink phone OK');
            this.getUserInfo();
          }).catch((err) => {
            alert(JSON.stringify(err));
          });
        } else if (this.provider == 'email') {
          agc.unlink(12).then((ret) => {
            alert('UNlink email OK');
            this.getUserInfo();
          }).catch((err) => {
            alert(JSON.stringify(err));
          });
        }
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
</style>
