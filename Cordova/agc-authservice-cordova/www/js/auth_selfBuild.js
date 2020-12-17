document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.getElementById('signIn').addEventListener('click', signIn);
    document.getElementById('link').addEventListener('click', link);
    document.getElementById('unlink').addEventListener('click', unlink);
    document.getElementById('Menu').addEventListener('click', Menu);
}

function Menu() {
    window.location.href='index.html';
}

function signIn() {
    console.log('func signIn.');
    var token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXNwbGF5TmFtZSI6IkBoaHoiLCJleHAiOjE2MDQ4MjAwNDQsInBob3RvVXJsIjoid3d3LmJhaWR1LmNvbSIsInVpZCI6IjQ3Nzk3Nzg5MTE2NzAxMTU4NCJ9.OhapPEIyx4c40djVjhLotv5GiPvcAYy9JbXY8wzoXPjXnSckTwR3jm9mmYMH4riZ9lzAIf5xHHmlk9tEKApIeMU3m6rVJW6sINj1Sh90H6Xqv5SOkpJHc-UK7g1arkRSCaSUA5p_cf5731wNreHn4MXj_4wrthwWcBVxbplX2ckG28o6kN8hM8jP1EhYMKsVjAbAjnc7Hw49dORleDDH8rsoqndVhko2J9uWU0KwRwKqGWWjMrJ9Lk1AnBJrWfOF-4CU13XOZaKYzq8n3TWdq2op9LHgLNf66oh4UBBo2hPGA7DBOeSVNfWK2ek7jpt-2w3JswOb5wGoigfoCSZcrN0CC2rtp3i_u_biZLQBaL4R1oxLtWQlUiWGuExQsyi7t2MiSZhH-mvZQzbCAOwAFWj11m7wzxeXYrVggYJtLsk6EegruZsXjrrWuSbK6C9VgF4oqo3mG5JlV7a5KQSV9nGpYyd8phZP9hmpUJXgnHBlrTzQyb5p1cCapig4SuJJ";

    var selfBuildCredential = {
        provider: 10,
        accessToken: token
    }
    AGCAuthPlugin.signIn(selfBuildCredential).then(success).catch(error);


    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function link() {
    console.log('func link.');

    var selfBuildCredential = {
        provider: 10,
        accessToken: 'gujun15900396786@163.com'
    }
    AGCAuthPlugin.link(selfBuildCredential).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}

function unlink() {
    console.log('func unlink.');

    AGCAuthPlugin.unlink(10).then(success).catch(error);

    function success() {
        console.log('success');
    }

    function error(result) {
        console.log('error:' + result);
    }
}





