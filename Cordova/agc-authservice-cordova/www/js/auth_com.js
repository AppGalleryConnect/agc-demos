document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('deleteUser').addEventListener('click', deleteUser);
    document.getElementById('signOut').addEventListener('click', signOut);
    document.getElementById('getCurrentUser').addEventListener('click', getCurrentUser);
    document.getElementById('updateProfile').addEventListener('click', updateProfile);
    document.getElementById('getUserExtra').addEventListener('click', getUserExtra);
    document.getElementById('addListener').addEventListener('click', addListener);
    document.getElementById('removeListener').addEventListener('click', removeListener);
    document.getElementById('sign').addEventListener('click', sign);
    document.getElementById('getToken').addEventListener('click', getToken);
    document.getElementById('Menu').addEventListener('click', Menu);
}
function getToken() {
    console.log('func getToken.');
    AGCAuthPlugin.getToken(false).then(success).catch(error);

    function success(result) {
        console.log('token:' + result.token);
        console.log('expirePeriod:' + result.expirePeriod);
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function addListener() {
    console.log('func addListener.');

    AGCAuthPlugin.addTokenListener(success);
    function success(result) {
        console.log('state:' + result.state);
        console.log('token:' + result.token);
    }
}

function removeListener() {
    console.log('func removeListener.');

    AGCAuthPlugin.removeTokenListener();
}

function sign() {
    console.log('func sign common.');
    AGCAuthPlugin.signInAnonymously().then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}


function Menu() {
    window.location.href='index.html';
}

function deleteUser() {
    console.log('func deleteUser.');

    AGCAuthPlugin.deleteUser().then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function signOut() {
    console.log('func signOut.');

    AGCAuthPlugin.signOut().then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function getCurrentUser() {
    console.log('func getCurrentUser.');

    AGCAuthPlugin.getCurrentUser().then(success).catch(error);

    function success(result) {
        console.log('isAnonymous:' + result.isAnonymous);
        console.log('uid:' + result.uid);
        console.log('displayName:' + result.displayName);
        console.log('photoUrl:' + result.photoUrl);
        console.log('email:' + result.email);
        console.log('phone:' + result.phone);
        console.log('providerId:' + result.providerId);
        console.log('emailVerified:' + result.emailVerified);
        console.log('passwordSet:' + result.passwordSet);
        var providerInfo = result.providerInfo;
        if (providerInfo) {
            for (var i = 0; i < providerInfo.length; i++) {
                console.log(providerInfo[i]);
            }
        }
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function updateProfile() {
    console.log('func updateProfile.');

    var userProfile = {
        displayName: "name",
        photoUrl: "url"
    }

    AGCAuthPlugin.updateProfile(userProfile).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}


function getUserExtra() {
    console.log('func getUserExtra.');

    AGCAuthPlugin.getUserExtra().then(success).catch(error);

    function success(result) {
        console.log('createTime:' + result.createTime);
        console.log('lastSignInTime:' + result.lastSignInTime);
    }

    function error(result) {
        console.log('error:' + result);
    }
}




