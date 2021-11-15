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

const agconnect = require('@agconnect/api');
require('@agconnect/auth');
require('@agconnect/instance');
import { AGConnectAuthCredential, AGConnectUser} from '@agconnect/auth-types';
// The following import methods are not supported
 //import agconnect from '@agconnect/api';
 //import '@agconnect/auth';
 //import '@agconnect/instance';
// import { AGCStorageService } from '@agconnect/storage-types';

function configInstance() {
  var agConnectConfig =
  {
    // App configuration information.
  };
  // Initialize AppGallery Connect.
  agconnect.instance().configInstance(agConnectConfig);
}

function createPhoneUser(account: string, password: string, verifyCode: string) {
  return agconnect.auth().createPhoneUser(new agconnect.auth.PhoneUser('86', account, password, verifyCode));
}

function createEmailUser(account: string, password: string, verifyCode: string) {
  return agconnect.auth().createEmailUser(new agconnect.auth.EmailUser(account, password, verifyCode));
}

function getPhoneVerifyCode(account: string, isRegist = true, sendInterval = 30) {
  return agconnect.auth.PhoneAuthProvider.requestVerifyCode('86', account, 
  isRegist?agconnect.auth.Action.ACTION_REGISTER_LOGIN:agconnect.auth.Action.ACTION_RESET_PASSWORD, 
  'zh_CN', sendInterval);
}
function getEmailVerifyCode(account: string, isRegist = true,sendInterval = 30) {
  return agconnect.auth.EmailAuthProvider.requestVerifyCode(account, 
    isRegist?agconnect.auth.Action.ACTION_REGISTER_LOGIN:agconnect.auth.Action.ACTION_RESET_PASSWORD, 
    'zh_CN', sendInterval);
}

function login(credential: any) {
  return agconnect.auth().signIn(credential);
}

function loginWithPhone(account: string, password: string, verifyCode?: string) {
  let credential;
  if (verifyCode) {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode('86', account, password, verifyCode);
  } else {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword('86', account, password);
  }
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

function loginWithEmail(account: string, password: string, verifyCode?: string) {
  let credential;
  if (verifyCode) {
    credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(account, password, verifyCode);
  } else {
    credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(account, password);
  }
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

function loginAnonymously() {
  return agconnect.auth().signInAnonymously();
}

function getUserInfo() {
  return agconnect.auth().getCurrentUser();
}

function logout() {
  return agconnect.auth().signOut();
}
function userReauthenticateByEmail(email: string, password: string, verifyCode: string) {
  let credential: AGConnectAuthCredential;
  if (verifyCode) {
    credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(email, password, verifyCode);
  } else {
    credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(email, password);
  }
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.userReauthenticate(credential);
      return;
    } else {
      return Promise.reject("no user login");
    }
  })
}
function userReauthenticateByPhone(countryCode: string, phoneNumber: string, password: string, verifyCode: string) {
  let credential: any;
  if (verifyCode) {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode);
  } else {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, phoneNumber, password);
  }
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.userReauthenticate(credential);
      return;
    } else {
      return Promise.reject("no user login");
    }
  })
}
function updatePhonePwd(newPassword: string, verifyCode: string) {
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.updatePassword(newPassword, verifyCode, 11);
      return;
    } else {
      return Promise.reject("no user login");
    }
  });
}

function updateEmailPwd(newPassword: string, verifyCode: string) {
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.updatePassword(newPassword, verifyCode, 12);
      return;
    } else {
      return Promise.reject("no user login");
    }
  });
}
function updatePhone(newPhone: string, verifyCode: string, lang: string) {
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.updatePhone("86", newPhone, verifyCode, lang);
      return;
    } else {
      return Promise.reject("no user login");
    }
  });
}
function updateEmail(newEmail: string, verifyCode: string, lang: string) {
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.updateEmail(newEmail, verifyCode, lang);
      return;
    } else {
      return Promise.reject("no user login");
    }
  });
}
function updateProfile(profile: any) {
  return agconnect.auth().getCurrentUser().then(async (user: AGConnectUser|null) => {
    if (user) {
      await user.updateProfile(profile);
      return;
    } else {
      return Promise.reject("no user login");
    }
  });
}

function deleteUser() {
  return agconnect.auth().deleteUser();
}

export {
  configInstance,
  createPhoneUser,
  createEmailUser,
  getPhoneVerifyCode,
  getEmailVerifyCode,
  loginWithPhone,
  loginWithEmail,
  loginAnonymously,
  getUserInfo,
  logout,
  deleteUser,
  userReauthenticateByEmail,
  userReauthenticateByPhone,
  updatePhonePwd,
  updateEmailPwd,
  updatePhone,
  updateEmail,
  updateProfile
}