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
import '@agconnect/instance';
import '@agconnect/auth';

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
  return agconnect.auth()
    .signIn(credential);
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
    return Promise.reject(new Error('credential is undefined'));
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
    return Promise.reject(new Error('credential is undefined'));
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
    return Promise.reject(new Error('credential is undefined'));
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
    return Promise.reject(new Error('credential is undefined'));
  }
  return login(credential);
}

/**
 * Signs in a user anonymously.
 */
function loginAnonymously() {
  return agconnect.auth()
    .signInAnonymously();
}

/**
 * Creates an account using a mobile number.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param password password
 * @param verifyCode verification code
 */
function createPhoneUser(countryCode, account, verifyCode, password) {
  return agconnect.auth()
    .createPhoneUser(new agconnect.auth.PhoneUser(countryCode, account, password, verifyCode));
}

/**
 * Creates an account using a email address.
 * @param account email address
 * @param password password
 * @param verifyCode verification code
 */
function createEmailUser(account, password, verifyCode) {
  return agconnect.auth()
    .createEmailUser(new agconnect.auth.EmailUser(account, password, verifyCode));
}

/**
 * Obtains information about the current signed-in user.
 */
function getUserInfo() {
  return agconnect.auth()
    .getCurrentUser();
}

/**
 * Applies for a verification code using a mobile number.
 * @param countryCode Country/Region code
 * @param account phoneNumber
 * @param lang Language for sending a verification code, for example, zh_CN.
 * @param sendInterval Interval for sending verification codes, in seconds. The value ranges from 30 to 120.
 */
function getPhoneVerifyCode(countryCode, account, lang, sendInterval, isReset) {
  if (isReset) {
    return agconnect.auth().requestPhoneVerifyCode(
      countryCode,
      account,
      agconnect.auth.Action.ACTION_RESET_PASSWORD,
      lang,
      sendInterval,
    );
  } else {
    return agconnect.auth().requestPhoneVerifyCode(
      countryCode,
      account,
      agconnect.auth.Action.ACTION_REGISTER_LOGIN,
      lang,
      sendInterval,
    );
  }
}

/**
 * Applies for a verification code using an email address.
 * @param account email address
 * @param lang Language for sending a verification code, for example, zh_CN.
 * @param sendInterval Interval for sending verification codes, in seconds. The value ranges from 30 to 120.
 */
function getEmailVerifyCode(account, lang, sendInterval, isReset) {
  if (isReset) {
    return agconnect.auth().requestEmailVerifyCode(
      account,
      agconnect.auth.Action.ACTION_RESET_PASSWORD,
      lang,
      sendInterval,
    );
  } else {
    return agconnect.auth().requestEmailVerifyCode(
      account,
      agconnect.auth.Action.ACTION_REGISTER_LOGIN,
      lang,
      sendInterval,
    );
  }
}

/**
 * Signs out a user and deletes the user's cached data.
 */
function logout() {
  return agconnect.auth()
    .signOut();
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
      return Promise.reject(new Error('credential is undefined'));
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

/**
 * Reset Password by phone.
 * @param countryCode Country/Region code
 * @param phoneNumber your phone Number
 * @param newPassword your new Password.
 * @param verifyCode verification code received by your phone Number.
 */
function resetPasswordByPhone(countryCode, phoneNumber, newPassword, verifyCode) {
  return agconnect.auth().resetPasswordByPhone(countryCode, phoneNumber, newPassword, verifyCode);
}

/**
 * Reset Password by Email.
 * @param email your email.
 * @param newPassword your new Password.
 * @param verifyCode verification code received by your phone Email.
 */
function resetPasswordByEmail(email, newPassword, verifyCode) {
  return agconnect.auth().resetPasswordByEmail(email, newPassword, verifyCode);
}

/**
 * add a token listener.
 * @param listener listener object.
 */
function addTokenListener(listener) {
  return agconnect.auth().addTokenListener(listener);
}

/**
 * remove a token listener.
 * @param listener listener object.
 */
function removeTokenListener(listener) {
  return agconnect.auth().removeTokenListener(listener);
}

/**
 * Re authenticate user by phone.
 * @param countryCode Country/Region code
 * @param phoneNumber your phone Number
 * @param password your Password.
 * @param verifyCode verification code received by your phone Number.
 */
function userReauthenticateByPhone(countryCode, phoneNumber, password, verifyCode) {
  let credential;
  if (verifyCode) {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode);
  } else {
    credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, phoneNumber, password);
  }
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.userReauthenticate(credential);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  })
}

/**
 * Re authenticate user by Email.
 * @param email your email.
 * @param password your Password.
 * @param verifyCode verification code received by your email.
 */
function userReauthenticateByEmail(email, password, verifyCode) {
  let credential = '';
  if (verifyCode) {
    credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(email, password, verifyCode);
  } else {
    credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(email, password);
  }
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.userReauthenticate(credential);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  })
}

/**
 * Update phone account password.
 * @param email your email.
 * @param newPassword your new password.
 * @param verifyCode verification code received by your email.
 */
function updatePhonePwd(newPassword, verifyCode) {
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.updatePassword(newPassword, verifyCode, 11);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  });
}

/**
 * Update email account password.
 * @param newPassword your new password.
 * @param verifyCode verification code received by your email.
 */
function updateEmailPwd(newPassword, verifyCode) {
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.updatePassword(newPassword, verifyCode, 12);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  });
}

/**
 * Update account phone number.
 * @param newPhone your new phone number.
 * @param verifyCode verification code received by your new phone number.
 * @param lang language in which the verification code message is sent.
 */
function updatePhone(newPhone, verifyCode, lang) {
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.updatePhone("86", newPhone, verifyCode, lang);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  });
}

/**
 * Update account email.
 * @param newEmail your new email.
 * @param verifyCode verification code received by your new email.
 * @param lang language in which the verification code message is sent.
 */
function updateEmail(newEmail, verifyCode, lang) {
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.updateEmail(newEmail, verifyCode, lang);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  });
}

/**
 * Update account profile.
 * @param profile profile consisted of displayName and photoUrl.
 */
function updateProfile(profile) {
  return agconnect.auth().getCurrentUser().then(async user => {
    if (user) {
      await user.updateProfile(profile);
    } else {
      return Promise.reject(new Error("no user login"));
    }
  });
}

/**
 * self build account login.
 * @param token JWT.
 */
function loginWithSelfBuild(token) {
  let credential = agconnect.auth.SelfBuildAuthProvider.credentialWithToken(token);
  console.log('SelfBuildCredential', credential);
  if (!credential) {
    return Promise.reject(new Error('credential is undefined'));
  }
  return login(credential);
}

export {
  loginWithSelfBuild,
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
  unlink,
  resetPasswordByPhone,
  userReauthenticateByPhone,
  userReauthenticateByEmail,
  updatePhonePwd,
  updateEmailPwd,
  updatePhone,
  updateEmail,
  updateProfile,
  removeTokenListener,
  addTokenListener,
  resetPasswordByEmail
};

