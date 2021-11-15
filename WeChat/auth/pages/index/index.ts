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
import * as agc from '../auth-weixin';

enum AuthType {
  phone = 'phone', email = 'email'
}

Page({
  data: {
    code: '',
    accountInfo: '',
    type: AuthType.phone,
    items: [
      { value: AuthType.phone, name: '手机', checked: true },
      { value: AuthType.email, name: '邮箱' }
    ]
  },
  onLoad() {
    agc.configInstance();
  },

  formSubmit(e: any) {
    const { authType, account, password, verifyCode } = e.detail.value;
    switch (e.detail.target.dataset.type) {
      case 'login':
        this.login(account, password, authType, verifyCode);
        break;
      case 'verifyCode':
        this.getVerifyCode(account, authType);
        break;
      case 'create':
        this.createUser(account, password, verifyCode, authType);
        break;
      case 'anonymously':
        this.loginAnonymously();
        break;
      case 'reauthenticate':
        this.reauthenticate(account, password, verifyCode, authType);
        break;
      case 'updateAccount':
        this.updateAccount(account, verifyCode, authType);
        break;
      case 'verifyCodeForPWD':
        this.getVerifyCodeForPWD(account, authType);
        break;
      case 'updatePwd':
        this.updatePwd(password, verifyCode, authType);
        break;
      default:
        break;
    }
  },
  formReset(e: any) {
    console.log('form发生了reset事件，携带数据为：', e.detail.value)
  },
  reauthenticate(account: string, password: string, verifyCode: string, authType: string) {
    switch (authType) {
      case AuthType.phone:
        agc.userReauthenticateByPhone("86", account, password, verifyCode).then(() => {
          wx.showToast({
            title: 'Reauth OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('reauthenticate error', error);
          wx.showToast({
            title: 'Reauth fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      case AuthType.email:
        agc.userReauthenticateByEmail(account, password, verifyCode).then(() => {
          wx.showToast({
            title: 'Reauth OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('reauthenticate error', error);
          wx.showToast({
            title: 'Reauth fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      default:
        console.error('unsupported auth type');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        break;
    }
  },
  updateAccount(newAccount: string, verifyCode: string, authType: string) {
    switch (authType) {
      case AuthType.phone:
        agc.updatePhone(newAccount, verifyCode, "zh_CN").then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error('updatePhone error', error);
          wx.showToast({
            title: 'update fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      case AuthType.email:
        agc.updateEmail(newAccount, verifyCode, "zh_CN").then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error('updateEmail error', error);
          wx.showToast({
            title: 'update fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      default:
        console.error('unsupported auth type');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        break;
    }
  },

  getVerifyCodeForPWD(account: string, authType: string) {
    switch (authType) {
      case AuthType.phone:
        agc.getPhoneVerifyCode(account, false).then(() => {
          wx.showToast({
            title: 'send OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('getVerifyCodeForPWD error', error);
          wx.showToast({
            title: 'send fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      case AuthType.email:
        agc.getEmailVerifyCode(account, false).then(() => {
          wx.showToast({
            title: 'send OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('getVerifyCodeForPWD error', error);
          wx.showToast({
            title: 'send fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      default:
        console.error('unsupported auth type');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        break;
    }
  },

  updatePwd(newPassword: string, verifyCode: string, authType: string) {
    switch (authType) {
      case AuthType.phone:
        agc.updatePhonePwd(newPassword, verifyCode).then(() => {
          wx.showToast({
            title: 'update OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('updatePwd error', error);
          wx.showToast({
            title: 'update fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      case AuthType.email:
        agc.updateEmailPwd(newPassword, verifyCode).then(() => {
          wx.showToast({
            title: 'update OK',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('updatePwd error', error);
          wx.showToast({
            title: 'update fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      default:
        console.error('unsupported auth type');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        break;
    }
  },

  getVerifyCode(account: string, authType: AuthType) {
    switch (authType) {
      case AuthType.phone:
        agc.getPhoneVerifyCode(account).then(() => {
          wx.showToast({
            title: 'send ok',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('getVerifyCode error', error);
          wx.showToast({
            title: 'send fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      case AuthType.email:
        agc.getEmailVerifyCode(account).then(() => {
          wx.showToast({
            title: 'send ok',
            icon: 'success',
            duration: 2000
          });
        }).catch((error: any) => {
          console.error('getVerifyCode error', error);
          wx.showToast({
            title: 'send fail',
            icon: 'error',
            duration: 2000
          });
        });
        break;
      default:
        console.error('unsupported auth type for getVerifyCode');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        break;
    }
  },
  loginAnonymously() {
    agc.loginAnonymously().then(() => {
      this.gotoUserInfoPage();
    }).catch((error: any) => {
      console.error("loginAnonymously error", error);
      wx.showToast({
        title: 'login fail',
        icon: 'error',
        duration: 2000
      });
    });
  },

  createUser(account: string, password: string, verifyCode: string, authType: AuthType) {
    if (!account) {
      console.error('account must input.');
      wx.showToast({
        title: 'account empty.',
        icon: 'error',
        duration: 2000
      });
      return;
    }
    switch (authType) {
      case AuthType.phone:
        return agc.createPhoneUser(account, password, verifyCode).then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error("createUser error", error);
          wx.showToast({
            title: 'create fail',
            icon: 'error',
            duration: 2000
          });
        });
      case AuthType.email:
        return agc.createEmailUser(account, password, verifyCode).then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error("createUser error", error);
          wx.showToast({
            title: 'create fail',
            icon: 'error',
            duration: 2000
          });
        });
      default:
        console.error('unsupported auth type for createUser');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        return Promise.resolve();
    }
  },
  login(account: string, password: string, authType: AuthType, verifyCode: string) {
    if (!account) {
      console.error('account must input.');
      wx.showToast({
        title: 'account empty',
        icon: 'error',
        duration: 2000
      });
      return;
    }
    switch (authType) {
      case AuthType.phone:
        return agc.loginWithPhone(account, password, verifyCode).then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error("login error", error);
          wx.showToast({
            title: 'login fail',
            icon: 'error',
            duration: 2000
          });
        });
      case AuthType.email:
        return agc.loginWithEmail(account, password, verifyCode).then(() => {
          this.gotoUserInfoPage();
        }).catch((error: any) => {
          console.error("login error", error);
          wx.showToast({
            title: 'login fail',
            icon: 'error',
            duration: 2000
          });
        });
      default:
        console.error('unsupported auth type for login');
        wx.showToast({
          title: 'wrong type',
          icon: 'error',
          duration: 2000
        });
        return Promise.resolve();
    }
  },
  gotoUserInfoPage() {
    wx.navigateTo({
      url: "/pages/userInfoDetail/userInfoDetail"
    })
  }
})
