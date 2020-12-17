document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('signIn').addEventListener('click', signIn);
    document.getElementById('createEmailUser').addEventListener('click', createEmailUser);
    document.getElementById('resetPasswordWithEmail').addEventListener('click', resetPasswordWithEmail);
    document.getElementById('requestEmailVerifyCode').addEventListener('click', requestEmailVerifyCode);
    document.getElementById('link').addEventListener('click', link);
    document.getElementById('unlink').addEventListener('click', unlink);
    document.getElementById('updateEmail').addEventListener('click', updateEmail);
    document.getElementById('updatePassword').addEventListener('click', updatePassword);
    document.getElementById('Menu').addEventListener('click', Menu);
}

function Menu() {
    window.location.href='index.html';
}

function signIn() {
    console.log('func signIn.');

    var emailStr = prompt("Email");
    if (emailStr == null) {
        alert("Email can not be null");
        return;
    }
    var passwordStr = prompt("password");
    if (passwordStr == null) {
        alert("password can not be null");
        return;
    }

    var emailCredential = {
        provider: 12,
        email: emailStr,
        password: passwordStr
    }
    AGCAuthPlugin.signIn(emailCredential).then(success).catch(error);


    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function createEmailUser() {
    console.log('func createEmailUser.');

    var emailStr = prompt("Email");
    if (emailStr == null) {
        alert("Email can not be null");
        return;
    }

    var passwordStr = prompt("password");

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    var emailUser = {
        email: emailStr,
        password: passwordStr,
        verifyCode: verifyCodeStr
    }

    AGCAuthPlugin.createEmailUser(emailUser).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function resetPasswordWithEmail() {
    console.log('func resetPasswordWithEmail.');

    var emailStr = prompt("Email");
    if (emailStr == null) {
        alert("Email can not be null");
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

    AGCAuthPlugin.resetPasswordWithEmail(emailStr, passwordStr, verifyCodeStr).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function requestEmailVerifyCode() {
    console.log('func requestEmailVerifyCode.');

    var emailStr = prompt("Email");
    if (emailStr == null) {
        alert("Email can not be null");
        return;
    }

    var con = confirm("Is Login/Register");
    var actionNum;
    if (con == true) {
        actionNum = 1001;
    } else {
        actionNum = 1002;
    }

    var settings = {
        action: actionNum,
        sendInterval: 100
    }

    AGCAuthPlugin.requestEmailVerifyCode(emailStr, settings).then(success).catch(error);

    function success(result) {
        console.log('success');
        console.log('shortestInterval:' + result.shortestInterval);
        console.log('validityPeriod:' + result.validityPeriod);
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function link() {
    console.log('func link.');

    var emailStr = prompt("Email");
    if (emailStr == null) {
        alert("Email can not be null");
        return;
    }

    var verifyCodeStr = prompt("verifyCode");
    if (verifyCodeStr == null) {
        alert("verifyCode can not be null");
        return;
    }

    var emailCredential = {
        provider: 12,
        email: emailStr,
        verifyCode: verifyCodeStr
    }
    AGCAuthPlugin.link(emailCredential).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function unlink() {
    console.log('func unlink.');

    AGCAuthPlugin.unlink(12).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function updateEmail() {
    console.log('func updateEmail.');

    var email = prompt("Email");
    if (email == null) {
        alert("Email can not be null");
        return;
    }

    var verifyCode = prompt("verifyCode");
    if (verifyCode == null) {
        alert("verifyCode can not be null");
        return;
    }

    AGCAuthPlugin.updateEmail(email, verifyCode).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function updatePassword() {
    console.log('func updatePassword.');

    var provider = 12;

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





