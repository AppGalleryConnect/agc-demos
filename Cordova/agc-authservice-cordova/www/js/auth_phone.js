document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('signIn').addEventListener('click', signIn);
    document.getElementById('createPhoneUser').addEventListener('click', createPhoneUser);
    document.getElementById('resetPasswordWithPhone').addEventListener('click', resetPasswordWithPhone);
    document.getElementById('requestPhoneVerifyCode').addEventListener('click', requestPhoneVerifyCode);
    document.getElementById('link').addEventListener('click', link);
    document.getElementById('unlink').addEventListener('click', unlink);
    document.getElementById('updatePhone').addEventListener('click', updatePhone);
    document.getElementById('updatePassword').addEventListener('click', updatePassword);
    document.getElementById('Menu').addEventListener('click', Menu);
}

function Menu() {
    window.location.href='index.html';
}

function signIn() {
    console.log('func signIn.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }
    var passwordStr = prompt("password");
    if (passwordStr == null) {
        alert("password can not be null");
        return;
    }

    var phoneCredential = {
        provider: 11,
        countryCode: countryCodeStr,
        phoneNumber: phoneNumberStr,
        password: passwordStr
    }
    AGCAuthPlugin.signIn(phoneCredential).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function createPhoneUser() {
    console.log('func createPhoneUser.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }
    var passwordStr = prompt("password");

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    var phoneUser = {
        countryCode: countryCodeStr,
        phoneNumber: phoneNumberStr,
        password: passwordStr,
        verifyCode: verifyCodeStr
    }

    AGCAuthPlugin.createPhoneUser(phoneUser).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function resetPasswordWithPhone() {
    console.log('func resetPasswordWithPhone.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }
    var passwordStr = prompt("password");
    if (passwordStr == null) {
        alert("password can not be null");
        return;
    }

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    AGCAuthPlugin.resetPasswordWithPhone(countryCodeStr, phoneNumberStr, passwordStr, verifyCodeStr).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function requestPhoneVerifyCode() {
    console.log('func requestPhoneVerifyCode.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }

//    var con = confirm("Is Login/Register");
//    var actionNum;
//    if (con == true) {
//        actionNum = 1001;
//    } else {
//        actionNum = 1002;
//    }

    var settings = {
        action: 1002,
        sendInterval: 100
    }

    AGCAuthPlugin.requestPhoneVerifyCode(countryCodeStr, phoneNumberStr, settings).then(success).catch(error);

    function success(result) {
        console.log('shortestInterval:' + result.shortestInterval);
        console.log('validityPeriod:' + result.validityPeriod);
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function link() {
    console.log('func link.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    var phoneCredential = {
        provider: 11,
        countryCode: countryCodeStr,
        phoneNumber: phoneNumberStr,
        verifyCode: verifyCodeStr
    }
    AGCAuthPlugin.link(phoneCredential).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function unlink() {
    console.log('func unlink.');

    AGCAuthPlugin.unlink(11).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function updatePhone() {
    console.log('func updatePhone.');

    var countryCodeStr = prompt("countryCode");
    if (countryCodeStr == null) {
        alert("countryCode can not be null");
        return;
    }
    var phoneNumberStr = prompt("phoneNumber");
    if (phoneNumberStr == null) {
        alert("phoneNumber can not be null");
        return;
    }

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    AGCAuthPlugin.updatePhone(countryCodeStr, phoneNumberStr, verifyCodeStr).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function updatePassword() {
    console.log('func updatePassword.');

    var provider = 11;
    var passwordStr = prompt("password");
    if (passwordStr == null) {
        alert("password can not be null");
        return;
    }

    var verifyCode = prompt("verifyCode");
    if (verifyCode == null) {
        alert("verifyCode can not be null");
        return;
    }

    AGCAuthPlugin.updatePassword(passwordStr, verifyCode, provider).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}





