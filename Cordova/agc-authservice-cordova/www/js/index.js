document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('Auth_Com').addEventListener('click', Auth_Com);
    document.getElementById('Auth_Anonymously').addEventListener('click', Auth_Anonymously);
    document.getElementById('Auth_Phone').addEventListener('click', Auth_Phone);
    document.getElementById('Auth_Email').addEventListener('click', Auth_Email);
    document.getElementById('Auth_SelfBuild').addEventListener('click', Auth_SelfBuild);
}

function Auth_Com() {
    window.location.href='auth_com.html';
}

function Auth_Anonymously() {
    window.location.href='auth_anonymous.html';
}

function Auth_Phone() {
    window.location.href='auth_phone.html';
}

function Auth_Email() {
    window.location.href='auth_email.html';
}

function Auth_SelfBuild() {
    window.location.href='auth_selfBuild.html';
}