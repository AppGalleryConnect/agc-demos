var btn1= document.getElementById("test1");
var btn2= document.getElementById("test2");
var btnClear = document.getElementById("clear");
var btnGet = document.getElementById("getValue");

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    btn1.addEventListener("click", test1);
    btn2.addEventListener("click", test2);
    btnClear.addEventListener("click", clear);
    btnGet.addEventListener("click", getValue);
}

function test1() {
    var defaults = {
        mLong: 1000,
        mString: 'hello world',
        mDouble: 3.14,
        mBoolean: true
    }
    AGCConfigPlugin.applyDefault(defaults, applyDefaultSuccess, applyDefaultError);

    function applyDefaultSuccess() {
        AGCConfigPlugin.fetch(0, fetchSuccess, fetchError);
        console.log('applyDefault success');
    }

    function applyDefaultError(result) {
        console.log('applyDefault fail:' + result);
    }

    function fetchSuccess() {
        AGCConfigPlugin.applyLastFetched(applyLastFetchedSuccess, applyLastFetchedError);
        console.log('fetch success');
    }

    function fetchError(result) {
        console.log('fetch fail: throttleEndTimeMillis=' + result.throttleEndTimeMillis + ' ,message=' + result.message);
    }

    function applyLastFetchedSuccess() {
        showAllValue();
        console.log('applyLastFetched success');
    }

    function applyLastFetchedError(result) {
        console.log('applyLastFetched fail:' + result);
    }
}

function test2() {
    var defaults = {
        mLong: 1000,
        mString: 'hello world',
        mDouble: 3.14,
        mBoolean: true
    }
    AGCConfigPlugin.applyDefault(defaults, applyDefaultSuccess, applyDefaultError);

    function applyDefaultSuccess() {
        AGCConfigPlugin.applyLastFetched(applyLastFetchedSuccess, applyLastFetchedError);
        console.log('applyDefault success');
    }

    function applyDefaultError(result) {
        console.log('applyDefault fail:' + result);
    }

    function fetchSuccess() {
        showAllValue();
        console.log('fetch success');
    }

    function fetchError(result) {
        console.log('fetch fail: throttleEndTimeMillis=' + result.throttleEndTimeMillis + ' ,message=' + result.message);
    }

    function applyLastFetchedSuccess() {
        AGCConfigPlugin.fetch(0, fetchSuccess, fetchError);
        console.log('applyLastFetched success');
    }

    function applyLastFetchedError(result) {
        console.log('applyLastFetched fail:' + result);
    }
}

function clear() {
    AGCConfigPlugin.clearAll();
}

function getValue() {
    AGCConfigPlugin.getValue('mLong', getValueSuccess, getValueError);
    function getValueSuccess(success_result) {
      console.log('getValue success, value = '+ success_result);
    }

    function getValueError(error_result) {
      console.log('getValue error, errMessage = '+ error_result);
    }

    AGCConfigPlugin.getSource('mLong', getSourceSuccess, getSourceError);
    function getSourceSuccess(success_result) {
     console.log('getSource success, source = '+ success_result);
    }

    function getSourceError(error_result) {
     console.log('getSource error, errMessage = '+ error_result);
    }
}

function showAllValue() {
    AGCConfigPlugin.getMergedAll(success, error);
    function success(result) {
        for (var key in result) {
            console.log(key + " : " + result[key]);
        }
    }

    function error(result) {
        console.log('showAllValue fail:' + result);
    }
}

function log(msg) {
    var p = document.createElement("p");
    p.style.fontSize = "14px";
    p.innerHTML = msg;
    document.body.appendChild(p);
}
