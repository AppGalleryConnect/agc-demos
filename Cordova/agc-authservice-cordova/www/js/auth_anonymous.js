document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('signInAnonymously').addEventListener('click', signInAnonymously);
    document.getElementById('Menu').addEventListener('click', Menu);
}

function Menu() {
    window.location.href='index.html';
}

function signInAnonymously() {
    console.log('func signInAnonymously.');

    AGCAuthPlugin.signInAnonymously().then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}




