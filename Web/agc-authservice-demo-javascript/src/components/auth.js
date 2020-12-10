import agconnect from '@agconnect/api';
import '@agconnect/auth';
import '@agconnect/network';
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

//set a cryptImpl only for auth, this cryptImpl can encrypt your auth info
//when they are stored in browser indexDB and sessionStorage
export function setAuthCryptImp(cryptImpl) {
  agconnect.auth().setCryptImp(cryptImpl);
}

//set a default cryptImpl, this cryptImpl can encrypt your client token as well
export function setCryptImp(cryptImpl) {
  agconnect.instance().setCryptImp(cryptImpl);
}

// Set where auth-related data is stored locally。0：indexDB；1：sessionStorage；2：memory
function setUserInfoPersistence(saveMode) {
  agconnect.auth().setUserInfoPersistence(saveMode);
}

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

function getPhoneCredential(countryCode, account, password, verifyCode) {
  if (verifyCode) {
    return agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, account, password, verifyCode);
  }
  return agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, account, password);
}

function getEmailCredential(account, password, verifyCode) {
  if (verifyCode) {
    return agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(account, password, verifyCode);
  }
  return agconnect.auth.EmailAuthProvider.credentialWithPassword(account, password);
}

/* eslint-disable */
function loginWithPhone(countryCode, account, password, verifyCode) {
  let credential = getPhoneCredential(countryCode, account, password, verifyCode);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

/* eslint-disable */
function loginWithWeChat(token, openId, autoCreateUser = true) {
  let credential = agconnect.auth.WeixinAuthProvider.credentialWithToken(token, openId, autoCreateUser);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

function loginWithQQ(token, openId, autoCreateUser = true) {
  let credential = agconnect.auth.QQAuthProvider.credentialWithToken(token, openId, autoCreateUser);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

function loginWithEmail(account, password, verifyCode) {
  let credential = getEmailCredential(account, password, verifyCode);
  if (!credential) {
    return Promise.reject('credential is undefined');
  }
  return login(credential);
}

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

async function createPhoneUser(countryCode, account, verifyCode, password) {
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

function createEmailUser(account, password, verifyCode) {
  return agconnect
    .auth()
    .createEmailUser(new agconnect.auth.EmailUser(account, password, verifyCode))
    .then((res) => {
      return Promise.resolve(res);
    })
    .catch((err) => {
      return Promise.reject('create phone user failed');
    });
}

function getUserInfo() {
  return agconnect
    .auth()
    .getCurrentUser()
    .then((user) => {
      return Promise.resolve(user);
    })
    .catch((err) => {
      console.log("----getuserinfo err:", err)
      return Promise.reject('get user error', err);
    });
}

function getVerifyCode(countryCode, account, lang, sendInterval) {
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

function unlink(credentialProvider) {
  return agconnect.auth().getCurrentUser().then(async user => {
    await user.unlink(credentialProvider);
  });
}

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
  getVerifyCode,
  getEmailVerifyCode,
  logout,
  deleteUser,
  getEmailCredential,
  link,
  unlink
};

