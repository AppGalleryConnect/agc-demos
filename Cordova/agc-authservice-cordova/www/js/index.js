/*
    Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener("deviceready", onDeviceReady, false);
const $ = (id) => document.getElementById(id);
let currentUser;
async function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);

    $("loginWithEmail").style.display = "none";
    $("loginWithPhone").style.display = "none";
    $("createUserEmailProvider").style.display = "none";
    $("createUserPhoneProvider").style.display = "none";
    $("userProfileScreen").style.display = "none";
    $("userProfileSettingsScreen").style.display = "none";
    $("userProfileSettingsScreenUpdateDisplayName").style.display = "none";
    $("userProfileSettingsScreenUpdateEmail").style.display = "none";
    $("userProfileSettingsScreenUpdatePhone").style.display = "none";
    $("userProfileSettingsScreenUpdatePassword").style.display = "none";
    $("loginWithEmailResetPassword").style.display = "none";
    $("loginWithPhoneResetPassword").style.display = "none";
    $("userProfileScreenProfileLink").style.display = "none";
    $("userProfileScreenProfileLinkEmailProvider").style.display = "none";
    $("userProfileScreenProfileLinkPhoneProvider").style.display = "none";

    AGCAuthPlugin.addTokenListener((tokenSnapshot) => {
        console.log("TokenSnapshot: " + JSON.stringify(tokenSnapshot, null, 1));
    }).then(() => {
        console.log("addTokenListener :: Success! ");
    }).catch((error) => alert("addTokenListener :: Error! " + JSON.stringify(error, null, 1)));

    currentUser = await AGCAuthPlugin.getCurrentUser();
    if (currentUser !== null) {
        $("main").style.display = "none";
        $("userProfileScreen").style.display = "block";
        updateUserProfileLayout();
    }
}
/**
 * create user methods
 */
$("createUserEmailProviderButton").onclick = () => {
    const email = $("createUserEmail").value;
    const password = $("createUserPasswordE").value;
    const verifyCode = $("emailVerifyCode").value;
    AGCAuthPlugin.createUser(AGCAuthPlugin.Provider.Email_Provider, { email, password, verifyCode })
        .then((result) => {
            alert("createUserEmailProvider :: Success");
            $("userProfileScreen").style.display = "block";
            $("createUserEmailProvider").style.display = "none";
            updateUserProfileLayout();
        })
        .catch((error) => alert("createUserEmailProvider :: Error! " + JSON.stringify(error, null, 1)));
}
$("createUserPhoneProviderButton").onclick = () => {
    const countryCode = $("phoneCountryCodeField").value;
    const phoneNumber = $("createUserPhoneNumber").value;
    const password = $("createUserPasswordP").value;
    const verifyCode = $("phoneVerifyCode").value;
    AGCAuthPlugin.createUser(AGCAuthPlugin.Provider.Phone_Provider, { countryCode, phoneNumber, password, verifyCode }).then((result) => {
        alert("createUserPhoneProvider :: Success");
        $("userProfileScreen").style.display = "block";
        $("createUserPhoneProvider").style.display = "none";
        updateUserProfileLayout();
    }).catch((error) => alert("updateProfile :: Error! " + JSON.stringify(error, null, 1)));
}


/**
 * user logIn_signOut methods
 */
$("logInWithEmailLogIn").onclick = () => {

    const code = $("logInEmailVerifyCodeField").value;
    const emailV = $("emailLogInField").value;
    const passwordV = $("passwordFieldEmailLogIn").value;

    let emailAuthCredential = {
        email: emailV,
        password: passwordV,
        verifyCode: code,
    }
    AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Email_Provider, emailAuthCredential).then((result) => {
        console.log("logInWithEmailLogIn :: Success");
        $("loginWithEmail").style.display = "none";
        $("userProfileScreen").style.display = "block";
        updateUserProfileLayout();
    }).catch((error) => alert("logInWithEmailLogIn :: Error! " + JSON.stringify(error, null, 1)));
}
$("logInWithPhoneLogIn").onclick = () => {
    const cCode = $("logInCountryCodeField").value;
    const phoneV = $("logInPhoneNumberField").value;
    const code = $("logInPhoneVerifyCodeField").value;
    const passwordV = $("passwordFieldPhoneLogIn").value;

    let phoneAuthCredential = {
        countryCode: cCode,
        phoneNumber: phoneV,
        password: passwordV,
        verifyCode: code,
    }
    AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Phone_Provider, phoneAuthCredential).then((result) => {
        console.log("logInWithPhoneLogIn :: Success");
        $("loginWithPhone").style.display = "none";
        $("userProfileScreen").style.display = "block";
        updateUserProfileLayout();
    }).catch((error) => alert("logInWithPhoneLogIn :: Error! " + JSON.stringify(error, null, 1)));
}
$("logInAnonymous").onclick = () => {
    AGCAuthPlugin.signInAnonymously().then((result) => {
        console.log("signInAnonymously :: Success");
        $("main").style.display = "none";
        $("userProfileScreen").style.display = "block";
        updateUserProfileLayout();
    }).catch((error) => alert("signInAnonymously :: Error! " + JSON.stringify(error, null, 1)));
}
$("userProfileSignOutUser").onclick = () => {
    AGCAuthPlugin.signOut().then(() => {
        currentUser = null;
        alert("userProfileSignOutUser :: Success! ");
        $("main").style.display = "block";
        $("userProfileScreen").style.display = "none";
    }).catch((error) => {
        alert("userProfileSignOutUser :: Error! " + JSON.stringify(error, null, 1));
    });
}
$("userProfileSettingsDeleteUser").onclick = () => {
    AGCAuthPlugin.deleteUser().then(() => {
        $("userProfileSettingsScreen").style.display = "none";
        $("main").style.display = "block";
        alert("Deleted user.");
    }).catch((error) => alert("deleteUser :: Error! " + JSON.stringify(error, null, 1)));
}

$("logInThirdPartyNextButton").onclick = () => {
    let e = $("providerType");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    if (AGCAuthPlugin.Provider.HMS_Provider === provider) {

        if (cordova.platformId == "android") {
            alert("Please integrate related @hmscore/cordova-plugin-hms-account plugin and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
            /*const signInParameters = {
                authRequestOption: [HMSCommonTypes.AuthRequestOption.SCOPE_ACCESS_TOKEN],
                authParam: HMSCommonTypes.HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM
            }
            HMSAccount.signIn(signInParameters).then((result) => {
                console.log("HMSAccount.signIn :: Success! " + JSON.stringify(result, null, 1));
                alert(JSON.stringify(result, null, 1));
                AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.HMS_Provider, {
                    token: result.accessToken,
                    autoCreateUser: true,
                }).then(() => {
                    console.log("logInWithHMSLogIn :: Success");
                    $("main").style.display = "none";
                    $("userProfileScreen").style.display = "block";
                    updateUserProfileLayout();
                }).catch((error) => alert("logInWithHMSLogIn :: Error! " + JSON.stringify(error, null, 1)));
            }).catch((error) => alert("HMSAccount.signIn :: Error! " + JSON.stringify(error, null, 1)));*/

        } else {
            alert("This authentication mode is available only for Android.");
        }

    } else if (AGCAuthPlugin.Provider.Facebook_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.Twitter_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.WeiXin_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.HWGame_Provider === provider) {

        if (cordova.platformId == "android") {
            alert("Please integrate related @hmscore/hms-js-gameservice plugin and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
                
            /*HMSGameService.signIn().then(() => {
                console.log("HMSGameService.signIn :: Success");
    
                HMSGameService.getCurrentPlayer().then((res) => {
                    console.log("HMSGameService.getCurrentPlayer :: Success " + JSON.stringify(res, null, 1));
                    
                    let hwGameCredential = {
                        playerSign: res.data.playerSign,
                        playerId: res.data.playerId,
                        displayName: res.data.displayName,
                        imageUrl: "example.com/image.jpg",
                        playerLevel: res.data.playerLevel,
                        signTs: res.data.signTs,
                        autoCreateUser: true,
                    }
                    AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.HWGame_Provider, hwGameCredential).then(() => {
                        console.log("logInWithHWGameLogIn :: Success");
                        $("main").style.display = "none";
                        $("userProfileScreen").style.display = "block";
                        updateUserProfileLayout();
                    }).catch((error) => alert("logInWithHWGameLogIn :: Error! " + JSON.stringify(error, null, 1)));
    
                }).catch((error) => alert("HMSGameService.getCurrentPlayer :: Error! " + JSON.stringify(error, null, 1)));
            }).catch((error) => alert("HMSGameService.signIn :: Error! " + JSON.stringify(error, null, 1)));
            */

        } else {
            alert("This authentication mode is available only for Android.");
        }
        
    } else if (AGCAuthPlugin.Provider.QQ_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.WeiBo_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.Google_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.GoogleGame_Provider === provider) {

        if (cordova.platformId == "android") {
            alert("Please integrate related third party plugins and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
    
        } else {
            alert("This authentication mode is available only for Android.");
        }

    } else if (AGCAuthPlugin.Provider.Apple_Provider === provider) {

        if (cordova.platformId == "ios") {
            alert("Please integrate related third party plugins and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
    
        } else {
            alert("This authentication mode is available only for iOS.");
        }

    } else if (AGCAuthPlugin.Provider.SelfBuild_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else {
        alert("Please select provider.");
    }
}

$("unlinkThirdPartyProviderButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    let e = $("linkProviderType");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    currentUser.unlink(provider).then(() => {
        alert('Succesfully unlinked provider type: ' + provider);
        updateUserProfileLayout();
    }).catch((error) => alert("currentUser.unlink :: Error! " + JSON.stringify(error, null, 1)));

}

$("linkThirdPartyProviderButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    let e = $("linkProviderType");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    if (AGCAuthPlugin.Provider.Google_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.GoogleGame_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.HMS_Provider === provider) {
        
        if (cordova.platformId == "android") {
            alert("Please integrate related @hmscore/cordova-plugin-hms-account plugin and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
            /*const signInParameters = {
                authRequestOption: [HMSCommonTypes.AuthRequestOption.SCOPE_ACCESS_TOKEN],
                authParam: HMSCommonTypes.HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM
            }
            HMSAccount.signIn(signInParameters).then((result) => {
                console.log("HMSAccount.signIn :: Success! " + JSON.stringify(result, null, 1));
                let hmsCredential = {
                    token: result.accessToken,
                    autoCreateUser: true
                }
                currentUser.link(provider, hmsCredential).then(() => {
                    alert('Succesfully linked HMS account.');
                    updateUserProfileLayout();
    
                }).catch((error) => alert("currentUser.link :: Error! " + JSON.stringify(error, null, 1)));
            }).catch((error) => alert("HMSAccount.signIn :: Error! " + JSON.stringify(error, null, 1)));*/
    
        } else {
            alert("This authentication mode is available only for Android.");
        }

    } else if (AGCAuthPlugin.Provider.HWGame_Provider === provider) {
        
        if (cordova.platformId == "android") {
            alert("Please integrate related @hmscore/hms-js-gameservice plugin and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
                
            /*HMSGameService.signIn().then(() => {
                console.log("HMSGameService.signIn :: Success");
    
                HMSGameService.getCurrentPlayer().then((res) => {
                    console.log("HMSGameService.getCurrentPlayer :: Success " + JSON.stringify(res, null, 1));
                    let hwGameCredential = {
                        playerSign: res.data.playerSign,
                        playerId: res.data.playerId,
                        displayName: res.data.displayName,
                        imageUrl: "example.com/image.jpg",
                        playerLevel: res.data.playerLevel,
                        signTs: res.data.signTs,
                        autoCreateUser: true,
                    }
                    currentUser.link(AGCAuthPlugin.Provider.HWGame_Provider, hwGameCredential).then(() => {
                        alert('Succesfully linked HWGame account.');
                        updateUserProfileLayout();
                    }).catch((error) => alert("currentUser.link :: Error! " + JSON.stringify(error, null, 1)));
    
                }).catch((error) => alert("HMSGameService.getCurrentPlayer :: Error! " + JSON.stringify(error, null, 1)));
            }).catch((error) => alert("HMSGameService.signIn :: Error! " + JSON.stringify(error, null, 1)));
            */
    
        } else {
            alert("This authentication mode is available only for Android.");
        }

    } else if (AGCAuthPlugin.Provider.Facebook_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.Twitter_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.Apple_Provider === provider) {
        
        if (cordova.platformId == "ios") {
            alert("Please integrate related third party plugins and retrive " +
                "the parameters to auth plugin to enable this function, you can read readme for detail.");
    
        } else {
            alert("This authentication mode is available only for iOS.");
        }

    } else if (AGCAuthPlugin.Provider.WeiBo_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.QQ_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.WeiXin_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.SelfBuild_Provider === provider) {
        alert("Please integrate related third party plugins and retrive " +
            "the parameters to auth plugin to enable this function, you can read readme for detail.");
    } else if (AGCAuthPlugin.Provider.Phone_Provider === provider) {
        $("userProfileScreenProfileLink").style.display = "none";
        $("userProfileScreenProfileLinkPhoneProvider").style.display = "block";
    } else if (AGCAuthPlugin.Provider.Email_Provider === provider) {
        $("userProfileScreenProfileLink").style.display = "none";
        $("userProfileScreenProfileLinkEmailProvider").style.display = "block";
    } else {
        alert("Please select provider.");
    }
}

$("linkEmailRequestEmailVerifyCodeButton").onclick = () => {
    const email = $("linkEmailField").value;
    AGCAuthPlugin.requestEmailVerifyCode(email, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to email.");
    }).catch((error) => alert("linkEmail :: Error! " + JSON.stringify(error, null, 1)));
}
$("linkPhoneRequestPhoneVerifyCodeButton").onclick = () => {
    const phoneNumber = $("linkPhonePhoneNumberField").value;
    const countryCode = $("linkPhoneCountryCodeField").value;
    AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phoneNumber, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to phone.");
    }).catch((error) => alert("linkPhone :: Error! " + JSON.stringify(error, null, 1)));

}
$("linkPhoneProvider").onclick = () => {
    const countryCode = $("linkPhoneCountryCodeField").value;
    const phoneNumber = $("linkPhonePhoneNumberField").value;
    const password = $("linkPhonePasswordField").value;
    const verifyCode = $("linkPhoneVerifyCodeField").value;
    let phoneAuthCredentialLink = {
        countryCode: countryCode,
        phoneNumber: phoneNumber,
        password: password,
        verifyCode: verifyCode
    }
    currentUser.link(AGCAuthPlugin.Provider.Phone_Provider, phoneAuthCredentialLink).then(() => {
        alert('Succesfully linked phone account.');
        updateUserProfileLayout();

    }).catch((error) => alert("currentUser.link :: Error! " + JSON.stringify(error, null, 1)));
}
$("linkEmailProvider").onclick = () => {
    const email = $("linkEmailField").value;
    const password = $("linkEmailPasswordField").value;
    const verifyCode = $("linkEmailVerifyCodeField").value;
    let emailAuthCredentialLink = {
        email: email,
        password: password,
        verifyCode: verifyCode
    }
    currentUser.link(AGCAuthPlugin.Provider.Email_Provider, emailAuthCredentialLink).then(() => {
        alert('Succesfully linked email account.');
        updateUserProfileLayout();
    }).catch((error) => alert("currentUser.link :: Error! " + JSON.stringify(error, null, 1)));
}

/**
 * request verifiy code method
 */
$("updatePasswordVCB").onclick = () => {
    const email = $("updatePasswordEmailF").value;
    const countryCode = $("updatePasswordCountryCodeF").value;
    const phoneNumber = $("updatePasswordPhoneF").value;
    let e = $("providersSelectElem");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    if (AGCAuthPlugin.Provider.Phone_Provider === provider) {
        AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phoneNumber, {
            action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_RESET_PASSWORD,
            sendInterval: 30,
            locale: "en_US",
        }).then((result) => {
            alert("Verification code has been sent to phone number.");
        }).catch((error) => alert("updatePasswordVCB :: Error! " + JSON.stringify(error, null, 1)));

    } else if (AGCAuthPlugin.Provider.Email_Provider === provider) {
        AGCAuthPlugin.requestEmailVerifyCode(email, {
            action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_RESET_PASSWORD,
            sendInterval: 30,
            locale: "en_US",
        }).then((result) => {
            alert("Verification code has been sent to email.");
        }).catch((error) => alert("updatePasswordVCB :: Error! " + JSON.stringify(error, null, 1)));

    } else {
        alert("Please select provider.");
    }
}
$("updatePhoneVCB").onclick = () => {
    const countryCode = $("countryCodeField").value;
    const phone = $("phoneNumberField").value;
    AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phone, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Phone verification code has been sent.");
    }).catch((error) => alert("requestPhoneVerifyCode :: Error! " + JSON.stringify(error, null, 1)));
}
$("updateEmailVCB").onclick = () => {
    const email = $("newEmailField").value;
    AGCAuthPlugin.requestEmailVerifyCode(email, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Email verification code has been sent.");
    }).catch((error) => alert("requestEmailVerifyCode :: Error! " + JSON.stringify(error, null, 1)));
}
$("requestEmailVerifyCodeButton").onclick = () => {
    const email = $("emailLogInField").value;
    AGCAuthPlugin.requestEmailVerifyCode(email, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to email.");
    }).catch((error) => alert("createUserEmailProvider :: Error! " + JSON.stringify(error, null, 1)));
}
$("requestPhoneVerifyCodeButton").onclick = () => {
    const phoneNumber = $("logInPhoneNumberField").value;
    const countryCode = $("logInCountryCodeField").value;
    AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phoneNumber, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to phone.");
    }).catch((error) => alert("createUserEmailProvider :: Error! " + JSON.stringify(error, null, 1)));
}
$("requestEmailVerifyCode").onclick = () => {
    const email = $("createUserEmail").value;
    AGCAuthPlugin.requestEmailVerifyCode(email, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to email.");
    }).catch((error) => alert("createUserEmailProvider :: Error! " + JSON.stringify(error, null, 1)));
}
$("requestPhoneVerifyCode").onclick = () => {
    const phoneNumber = $("createUserPhoneNumber").value;
    const countryCode = $("phoneCountryCodeField").value;
    AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phoneNumber, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_REGISTER_LOGIN,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to phone.");
    }).catch((error) => alert("createUserEmailProvider :: Error! " + JSON.stringify(error, null, 1)));
}
$("resetPasswordPhoneVCB").onclick = () => {
    const countryCode = $("countryCodeFieldRP").value;
    const phoneNumber = $("phoneNumberFieldRP").value;
    AGCAuthPlugin.requestPhoneVerifyCode(countryCode, phoneNumber, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_RESET_PASSWORD,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to phone number.");
    }).catch((error) => alert("resetPasswordPhoneVCB :: Error! " + JSON.stringify(error, null, 1)));
}
$("resetPasswordEmailVCB").onclick = () => {
    const email = $("emailFieldRP").value;
    AGCAuthPlugin.requestEmailVerifyCode(email, {
        action: AGCAuthPlugin.VerifyCodeSettingsActions.ACTION_RESET_PASSWORD,
        sendInterval: 30,
        locale: "en_US",
    }).then((result) => {
        alert("Verification code has been sent to email.");
    }).catch((error) => alert("resetPasswordEmailVCB :: Error! " + JSON.stringify(error, null, 1)));
}

/**
 * update button methods
 */
$("updatePasswordButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    let e = $("providersSelectElem");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    const verifyCode = $("updatePasswordVCF").value;
    const newPassword = $("newPasswordField").value;
    currentUser.updatePassword(newPassword, verifyCode, provider).then(() => {
        alert("Updated password.");
        updateUserProfileLayout();
    }).catch((error) => alert("updatePassword :: Error! " + JSON.stringify(error, null, 1)));
}
$("updatePhoneButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    const countryCode = $("countryCodeField").value;
    const phone = $("phoneNumberField").value;
    const code = $("updatePhoneVCF").value;
    const locale = $("updatePhoneLangField").value;
    if (locale !== "undefined" && locale !== "") {
        currentUser.updatePhone(countryCode, phone, code, locale).then(() => {
            alert("Updated Phone Number");
            updateUserProfileLayout();
            $("userProfileSettingsScreen").style.display = "block";
            $("userProfileSettingsScreenUpdatePhone").style.display = "none";

        }).catch((error) => alert("updatePhone :: Error! " + JSON.stringify(error, null, 1)));
    } else {
        currentUser.updatePhone(countryCode, phone, code).then(() => {
            alert("Updated Phone Number");
            updateUserProfileLayout();
            $("userProfileSettingsScreen").style.display = "block";
            $("userProfileSettingsScreenUpdatePhone").style.display = "none";

        }).catch((error) => alert("updatePhone :: Error! " + JSON.stringify(error, null, 1)));
    }
}
$("updateEmailButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    const email = $("newEmailField").value;
    const verifyCode = $("updateEmailVCF").value;
    const locale = $("updateEmailLangField").value;
    if (locale !== "undefined" && locale !== "") {
        currentUser.updateEmail(email, verifyCode, locale).then(() => {
            alert("Updated Email");
            updateUserProfileLayout();
            $("userProfileSettingsScreen").style.display = "block";
            $("userProfileSettingsScreenUpdateEmail").style.display = "none";

        }).catch((error) => alert("updateEmail :: Error! " + JSON.stringify(error, null, 1)));
    } else {
        currentUser.updateEmail(email, verifyCode).then(() => {
            alert("Updated Email");
            updateUserProfileLayout();
            $("userProfileSettingsScreen").style.display = "block";
            $("userProfileSettingsScreenUpdateEmail").style.display = "none";

        }).catch((error) => alert("updateEmail :: Error! " + JSON.stringify(error, null, 1)));
    }
}
$("updateProfileButton").onclick = async () => {
    currentUser = await AGCAuthPlugin.getCurrentUser();
    const displayNameF = $("displayNameField").value;
    const photoUrlF = $("profilePhotoField").value;
    let request = {
        displayName: displayNameF,
        photoUrl: photoUrlF
    }
    currentUser.updateProfile(request).then(() => {
        alert("Updated profile");
        $("userProfileSettingsScreen").style.display = "block";
        $("userProfileSettingsScreenUpdateDisplayName").style.display = "none";
        updateUserProfileLayout();
    }).catch((error) => alert("updateProfile :: Error! " + JSON.stringify(error, null, 1)));
}


/**
 * reset password methods
 */
$("resetPasswordPhoneButton").onclick = () => {
    const countryCode = $("countryCodeFieldRP").value;
    const phoneNumber = $("phoneNumberFieldRP").value;
    const newPassword = $("newPasswordFieldPRP").value;
    const verifyCode = $("resetPasswordPhoneVCF").value;
    AGCAuthPlugin.resetPasswordWithPhone(countryCode, phoneNumber, newPassword, verifyCode).then(() => {
        alert("resetPasswordWithPhone :: Success");
        $("loginWithPhone").style.display = "block";
        $("loginWithPhoneResetPassword").style.display = "none";
    }).catch((error) => alert("resetPasswordWithPhone :: Error! " + JSON.stringify(error, null, 1)));
}
$("resetPasswordEmailButton").onclick = () => {
    const email = $("emailFieldRP").value;
    const newPassword = $("newPasswordFieldERP").value;
    const verifyCode = $("resetPasswordEmailVCF").value;
    AGCAuthPlugin.resetPasswordWithEmail(email, newPassword, verifyCode).then(() => {
        alert("resetPasswordWithEmail :: Success");
        $("loginWithEmail").style.display = "block";
        $("loginWithEmailResetPassword").style.display = "none";
    }).catch((error) => alert("resetPasswordWithEmail :: Error! " + JSON.stringify(error, null, 1)));
}


/**
 * layout methods
 */
function updateUserProfileLayout() {
    AGCAuthPlugin.getCurrentUser().then((currentUser) => {
        currentUser.getEmail().then((email) => {
            $("userProfileEmail").innerHTML = email ? email : "empty";
        });
        currentUser.getPhone().then((phone) => {
            $("userProfilePhoneNumber").innerHTML = phone ? phone : "empty";
        });
        currentUser.getDisplayName().then((displayName) => {
            $("userProfileDisplayName").innerHTML = displayName ? displayName : "empty";
        });
        currentUser.getPhotoUrl().then((photoUrl) => {
            $("userProfileAvatar").src = photoUrl ? photoUrl : "https://developer.huawei.com/Enexport/sites/default/images/AppGallery-Connect/Auth-Service/auth1.png";
        });
        currentUser.getEmailVerified().then((isEmailVerified) => {
            $("userProfileEmailLabel").innerHTML = isEmailVerified ? "Email - Verified" : "Email";
        });
        currentUser.getProviderId().then((providerId) => {
            $("userProfileProviderId").innerHTML = providerId;
        });
        currentUser.getUid().then((uid) => {
            $("userProfileUid").innerHTML = uid ? uid : "empty";
        });
        currentUser.getToken(false).then((token) => {
            $("userProfileToken").innerHTML = token.token ? token.token : "empty";
        });
        currentUser.isAnonymous().then((isAnonymous) => {
            $("userProfileIsAnonymous").innerHTML = isAnonymous ? isAnonymous : "false";
        });
        currentUser.getPasswordSetted().then((passwordSetted) => {
            $("userProfilePasswordSetted").innerHTML = passwordSetted ? "true" : "false";
        });
        currentUser.getUserExtra().then((userExtra) => {
            $("userProfileUserCreateTime").innerHTML = userExtra.createTime ? userExtra.createTime : "empty";
            $("userProfileLastSignInTime").innerHTML = userExtra.lastSignInTime ? userExtra.lastSignInTime : "empty";
        });
    }).catch((error) => alert("getCurrentUser :: Error! " + JSON.stringify(error, null, 1)));
}
$("createUserNextButton").onclick = () => {
    $("main").style.display = "none";
    let e = $("createUserSelectElem");
    let value = e.options[e.selectedIndex].value;
    const provider = parseInt(value, 10);
    if (AGCAuthPlugin.Provider.Phone_Provider === provider) {
        $("createUserPhoneProvider").style.display = "block";
    } else if (AGCAuthPlugin.Provider.Email_Provider === provider) {
        $("createUserEmailProvider").style.display = "block";
    } else {
        alert("Please select provider.");
    }
}
$("userProfileLink").onclick = () => {
    $("userProfileScreen").style.display = "none";
    $("userProfileScreenProfileLink").style.display = "block";
}
$("logInWithEmailLogInResetPassword").onclick = () => {
    $("loginWithEmail").style.display = "none";
    $("loginWithEmailResetPassword").style.display = "block";
}
$("logInWithPhoneLogInResetPassword").onclick = () => {
    $("loginWithPhone").style.display = "none";
    $("loginWithPhoneResetPassword").style.display = "block";
}
$("userProfileSettingsUpdateEmail").onclick = () => {
    $("userProfileSettingsScreen").style.display = "none";
    $("userProfileSettingsScreenUpdateEmail").style.display = "block";
}
$("userProfileSettingsUpdateDisplayName").onclick = () => {
    $("userProfileSettingsScreen").style.display = "none";
    $("userProfileSettingsScreenUpdateDisplayName").style.display = "block";
}
$("userProfileSettingsUpdatePhone").onclick = () => {
    $("userProfileSettingsScreen").style.display = "none";
    $("userProfileSettingsScreenUpdatePhone").style.display = "block";
}
$("userProfileSettingsUpdatePassword").onclick = () => {
    $("userProfileSettingsScreen").style.display = "none";
    $("userProfileSettingsScreenUpdatePassword").style.display = "block";
}
$("logInWithPhoneButton").onclick = () => {
    $("main").style.display = "none";
    $("loginWithPhone").style.display = "block";
}
$("logInWithEmailButton").onclick = () => {
    $("main").style.display = "none";
    $("loginWithEmail").style.display = "block";
}
$("userProfileSettings").onclick = () => {
    $("userProfileScreen").style.display = "none";
    $("userProfileSettingsScreen").style.display = "block";
}

/**
 * back button methods
 */
$("userProfileSettingsScreenUpdateDisplayNameBack").onclick = () => {
    $("userProfileSettingsScreenUpdateDisplayName").style.display = "none";
    $("userProfileSettingsScreen").style.display = "block";
}
$("userProfileSettingsScreenUpdateEmailBack").onclick = () => {
    $("userProfileSettingsScreenUpdateEmail").style.display = "none";
    $("userProfileSettingsScreen").style.display = "block";
}
$("userProfileSettingsScreenUpdatePhoneBack").onclick = () => {
    $("userProfileSettingsScreenUpdatePhone").style.display = "none";
    $("userProfileSettingsScreen").style.display = "block";
}
$("userProfileSettingsScreenUpdatePasswordBack").onclick = () => {
    $("userProfileSettingsScreenUpdatePassword").style.display = "none";
    $("userProfileSettingsScreen").style.display = "block";
}
$("loginWithPhoneResetPasswordBack").onclick = () => {
    $("loginWithPhoneResetPassword").style.display = "none";
    $("loginWithPhone").style.display = "block";
}
$("loginWithEmailResetPasswordBack").onclick = () => {
    $("loginWithEmailResetPassword").style.display = "none";
    $("loginWithEmail").style.display = "block";
}
$("userProfileScreenProfileLinkBack").onclick = () => {
    $("userProfileScreenProfileLink").style.display = "none";
    $("userProfileScreen").style.display = "block";
}
$("createUserEmailProviderButtonBack").onclick = () => {
    $("main").style.display = "block";
    $("createUserEmailProvider").style.display = "none";
}
$("createUserPhoneProviderButtonBack").onclick = () => {
    $("main").style.display = "block";
    $("createUserPhoneProvider").style.display = "none";
}
$("logInWithPhoneButtonBack").onclick = () => {
    $("main").style.display = "block";
    $("loginWithPhone").style.display = "none";
}
$("userProfileSettingsBack").onclick = () => {
    $("userProfileScreen").style.display = "block";
    $("userProfileSettingsScreen").style.display = "none";
}
$("logInWithEmailButtonBack").onclick = () => {
    $("main").style.display = "block";
    $("loginWithEmail").style.display = "none";
}
$("userProfileScreenProfileLinkEmailProviderButtonBack").onclick = () => {
    $("userProfileScreenProfileLink").style.display = "block";
    $("userProfileScreenProfileLinkEmailProvider").style.display = "none";
}
$("userProfileScreenProfileLinkPhoneProviderButtonBack").onclick = () => {
    $("userProfileScreenProfileLink").style.display = "block";
    $("userProfileScreenProfileLinkPhoneProvider").style.display = "none";
}
