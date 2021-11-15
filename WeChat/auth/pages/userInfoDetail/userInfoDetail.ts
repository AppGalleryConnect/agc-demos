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
import { AGConnectUser } from '@agconnect/auth-types';

Page({
  data: {
    userInfo: '',
  },
  onLoad: function () {
    this.getUserInfo();
  },

  formSubmit(e: any) {
    switch (e.detail.target.dataset.type) {
      case 'updateProfile':
        this.updateProfile();
        break;
      case 'logout':
        this.logOut();
        break;
      case 'deleteUser':
        this.deleteUser();
        break;
      default:
        break;
    }
  },

  getUserInfo() {
    agc.getUserInfo().then((user: AGConnectUser | null) => {
      if (user) {
        this.setData({
          userInfo: JSON.stringify({
            uid: user.getUid(),
            anonymous: user.isAnonymous(),
            displayName: user.getDisplayName(),
            email: user.getEmail(),
            phone: user.getPhone(),
            photoUrl: user.getPhotoUrl(),
            providerId: user.getProviderId(),
          })
        });
      } else {
        this.resetAccountInfo();
      }
    }).catch((err: any) => {
      this.resetAccountInfo();
      console.error("getuserinfo err:", err);
      wx.showToast({
        title: 'error',
        icon: 'error',
        duration: 2000
      });
    });
  },

  updateProfile() {
    let profile = {
      displayName: 'HW AGC',
      photoUrl: 'a url',
    };
    agc.updateProfile(profile).then(() => {
      this.getUserInfo();
      wx.showToast({
        title: 'update OK',
        icon: 'success',
        duration: 2000
      });
    }).catch((err: any) => {
      console.error("updateProfile err:", err);
      wx.showToast({
        title: 'update fail',
        icon: 'error',
        duration: 2000
      });
    });
  },

  logOut() {
    agc.logout().then(() => {
      this.resetAccountInfo();
      console.log('logout');
      wx.showToast({
        title: 'logout OK',
        icon: 'success',
        duration: 2000
      });
    }).catch((err: any) => {
      console.error("logout err:", err);
      wx.showToast({
        title: 'logout fail',
        icon: 'error',
        duration: 2000
      });
    });
  },
  deleteUser() {
    agc.deleteUser().then(() => {
      console.log('deleteUser');
      this.resetAccountInfo();
      wx.showToast({
        title: 'delete OK',
        icon: 'success',
        duration: 2000
      });
    }).catch((err: any) => {
      console.error("deleteUser err:", err);
      wx.showToast({
        title: 'delete fail',
        icon: 'error',
        duration: 2000
      });
    });
  },
  resetAccountInfo() {
    this.setData({
      userInfo: ''
    });
  }
})