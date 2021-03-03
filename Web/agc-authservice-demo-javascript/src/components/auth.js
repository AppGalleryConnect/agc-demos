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

import agconnect from '@agconnect/api';
import '@agconnect/auth';
import '@agconnect/instance';

export class AuthCrypt {
}

AuthCrypt.prototype.encrypt = function (value) {
  return value + '---authEncrypt';
};

AuthCrypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

export class Crypt {
}

Crypt.prototype.encrypt = function (value) {
  return value + '---encrypt';
};

Crypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

/**
 * Set the object to encrypt/decrypt user Info,only for auth.
 * @param cryptImpl object used to encrypt/decrypt
 */
export function setAuthCryptImp(cryptImpl) {
  agconnect.auth().setCryptImp(cryptImpl);
}

/**
 * Set the object to encrypt/decrypt Info, sach as clinet token.
 * @param cryptImpl object used to encrypt/decrypt
 */
export function setCryptImp(cryptImpl) {
  agconnect.instance().setCryptImp(cryptImpl);
}

/**
 * Set where auth-related data is stored locally。0：indexedDB；1：sessionStorage；2：memory
 * @param saveMode storage mode
 */
function setUserInfoPersistence(saveMode) {
  agconnect.auth().setUserInfoPersistence(saveMode);
}

/**
 * Signs in a user to AppGallery Connect through third-party authentication.
 * @param credential AGConnectAuthCredential
 */
function login(credential) {
  return agconnect
    .auth()
    .signIn(credential)
    .then((res) => {
      return Promise.resolve(res);
    })
    .catch((err) => {
      return Promise.reject('sign in failed');
    });
}

/**
 * Obtains a credential using a mobile number and password or verification code.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param password password
 * @param verifyCode verification code
 */
function getPhoneCredential(countryCode, account, password, verifyCode) {
  if (verifyCode) {
    return agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, account, password, verifyCode);
  }
  return agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, account, password);
}

/**
 * Obtains a credential using an email address and a password or a verification code.
 * @param account email address
 * @param password password
 * @param verifyCode verification code
 */
function getEmailCredential(account, password, verifyCode) {
  if (verifyCode) {
    return agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(account, password, verifyCode);
  }
  return agconnect.auth.EmailAuthProvider.credentialWithPassword(account, password);
}

/**
 * Obtains a credential using a mobile number and password or verification code,
 * then use the credential to login.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param password password
 * @param verifyCode verification code
 */
function loginWithPhone(countryCode, account, password, verifyCode) {
  let credential = getPhoneCredential(countryCode, account, password, verifyCode);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

/**
 * Obtains a credential of WeChat, then use the credential to login.
 * @param token Access token obtained after WeChat SDK authorization.
 * @param openId OpenID obtained after WeChat SDK authorization.
 * @param autoCreateUser (Optional) Indicates whether to automatically create an account. The default value is true (yes)
 */
function loginWithWeChat(token, openId, autoCreateUser = true) {
  let credential = agconnect.auth.WeixinAuthProvider.credentialWithToken(token, openId, autoCreateUser);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

/**
 * Obtains a credential of QQ, then use the credential to login.
 * @param token Token obtained after QQ SDK authorization.
 * @param openId OpenID obtained after QQ SDK authorization.
 * @param autoCreateUser (Optional) Indicates whether to automatically create an account. The default value is true (yes).
 */
function loginWithQQ(token, openId, autoCreateUser = true) {
  let credential = agconnect.auth.QQAuthProvider.credentialWithToken(token, openId, autoCreateUser);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

/**
 * Obtains a credential using an email address and a password or a verification code，
 * then use the credential to login.
 * @param account email address
 * @param password password
 * @param verifyCode verification code
 */
function loginWithEmail(account, password, verifyCode) {
  let credential = getEmailCredential(account, password, verifyCode);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

/**
 * Signs in a user anonymously.
 */
function loginAnonymously() {
  return agconnect
    .auth()
    .signInAnonymously()
    .then((res) => {
      return Promise.resolve(res);
    })
    .catch((err) => {
      return Promise.reject('sign in anonymously failed');
    });
}

/**
 * Creates an account using a mobile number.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param password password
 * @param verifyCode verification code
 */
function createPhoneUser(countryCode, account, verifyCode, password) {
  return agconnect
    .auth()
    .createPhoneUser(new agconnect.auth.PhoneUser(countryCode, account, password, verifyCode))
    .then((res) => {
      return Promise.resolve(res);
    })
    .catch((err) => {
      return Promise.reject('create phone user failed');
    });
}

/**
 * Creates an account using a email address.
 * @param account email address
 * @param password password
 * @param verifyCode verification code
 */
function createEmailUser(account, password, verifyCode) {
  return agconnect
    .auth()
    .createEmailUser(new agconnect.auth.EmailUser(account, password, verifyCode))
    .then((res) => {
      return Promise.resolve(res);
    })
    .catch((err) => {
      return Promise.reject('create email user failed');
    });
}

/**
 * Obtains information about the current signed-in user.
 */
function getUserInfo() {
  return agconnect
    .auth()
    .getCurrentUser()
    .then((user) => {
      return Promise.resolve(user);
    })
    .catch((err) => {
      console.error("get user info err:", err)
      return Promise.reject('get user error', err);
    });
}

/**
 * Applies for a verification code using a mobile number.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param lang Language for sending a verification code, for example, zh_CN.
 * @param sendInterval Interval for sending verification codes, in seconds. The value ranges from 30 to 120.
 */
function getPhoneVerifyCode(countryCode, account, lang, sendInterval) {
  return agconnect.auth.PhoneAuthProvider.requestVerifyCode(
    countryCode,
    account,
    agconnect.auth.Action.ACTION_REGISTER_LOGIN,
    lang,
    sendInterval,
  )
    .then((ret) => {
      return Promise.resolve(ret);
    })
    .catch((err) => {
      return Promise.reject('get verify code error', err);
    });
}

/**
 * Applies for a verification code using an email address.
 * @param account email address
 * @param lang Language for sending a verification code, for example, zh_CN.
 * @param sendInterval Interval for sending verification codes, in seconds. The value ranges from 30 to 120.
 */
function getEmailVerifyCode(account, lang, sendInterval) {
  return agconnect.auth.EmailAuthProvider.requestVerifyCode(
    account,
    agconnect.auth.Action.ACTION_REGISTER_LOGIN,
    lang,
    sendInterval,
  )
    .then((ret) => {
      return Promise.resolve(ret);
    })
    .catch((err) => {
      return Promise.reject('get verify code error', err);
    });
}

/**
 * Signs out a user and deletes the user's cached data.
 */
function logout() {
  return agconnect
    .auth()
    .signOut()
    .then(() => {
      return Promise.resolve();
    })
    .catch((err) => {
      return Promise.reject('logout error', err);
    });
}

/**
 * Links a new authentication mode for the current user.
 */
function link(linkObj, param1, param2, param3) {
  return agconnect.auth().getCurrentUser().then(async user => {
    let credential = undefined;
    if (linkObj == "phone") {
      credential = getPhoneCredential('86', param1, param2, param3);
    } else if (linkObj == "email") {
      credential = getEmailCredential(param1, param2, param3);
    } else if (linkObj == "QQ") {
      credential = agconnect.auth.QQAuthProvider.credentialWithToken(param1, param2, true);
    } else if (linkObj == "weChat") {
      credential = agconnect.auth.WeixinAuthProvider.credentialWithToken(param1, param2, true);
    }

    if (!credential) {
      return Promise.reject('credential is undefined');
    }
    await user.link(credential);
  });
}

/**
 * Unlinks the current user from the linked authentication mode.
 * @param credentialProvider AGConnectAuthCredentialProvider
 */
function unlink(credentialProvider) {
  return agconnect.auth().getCurrentUser().then(async user => {
    await user.unlink(credentialProvider);
  });
}

/**
 * Deletes the current user information and cache information from the AppGallery Connect server.
 */
function deleteUser() {
  return agconnect.auth().deleteUser();
}

export {
  setUserInfoPersistence,
  loginWithPhone,
  loginWithWeChat,
  loginWithQQ,
  loginWithEmail,
  loginAnonymously,
  createPhoneUser,
  createEmailUser,
  getUserInfo,
  getPhoneVerifyCode,
  getEmailVerifyCode,
  logout,
  deleteUser,
  getEmailCredential,
  link,
  unlink
};

