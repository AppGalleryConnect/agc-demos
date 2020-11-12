import agconnect from '@agconnect/api';
import '@agconnect/instance';
import '@agconnect/auth';
import '@agconnect/network';

function setUserInfoPersistence(saveMode) {
    agconnect.auth().setUserInfoPersistence(saveMode);
}

function initConfig(config) {
    agconnect.instance().configInstance(config);
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

function loginWithPhone(countryCode, account, password, verifyCode) {
    let credential = getPhoneCredential(countryCode, account, password, verifyCode);
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
            if (user == null) {
                return Promise.reject('no user login, please login first');
            }
            return Promise.resolve(user);
        })
        .catch((err) => {
            return Promise.reject('get user error');
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
            return Promise.reject('get verify code error');
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
            return Promise.reject('get verify code error');
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
            return Promise.reject('logout error');
        });
}

function deleteUser() {
    agconnect.auth().deleteUser();
}

export {
    initConfig,
    setUserInfoPersistence,
    loginWithPhone,
    loginWithEmail,
    loginAnonymously,
    createPhoneUser,
    createEmailUser,
    getUserInfo,
    getVerifyCode,
    getEmailVerifyCode,
    logout,
    deleteUser,
};
