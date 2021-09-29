/*
    Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
let apmsPlugin;
let mCustomTrace;
let mNetworkMeasure;
async function onDeviceReady() {
    // Cordova is now initialized. Have fun!
    console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);
    apmsPlugin = await AGCAPM.getInstance();
    alert("AGCAPM: initialized");
}
$("enableCollection").onclick = () => {
    let enable = true;
    apmsPlugin.enableCollection(enable).then(() => {
        alert("enableCollection: success");
    }).catch((error) => alert("enableCollection :: Error! " + JSON.stringify(error, null, 1)));
}
$("disableCollection").onclick = () => {
    let enable = false;
    apmsPlugin.enableCollection(enable).then(() => {
        alert("disableCollection: success");
    }).catch((error) => alert("disableCollection :: Error! " + JSON.stringify(error, null, 1)));
}
$("enableAnrMonitor").onclick = () => {
    let enable = true;
    apmsPlugin.enableAnrMonitor(enable).then(() => {
        alert("enableAnrMonitor: success");
    }).catch((error) => alert("enableAnrMonitor :: Error! " + JSON.stringify(error, null, 1)));
}
$("setUserIdentifier").onclick = () => {
    let userIdentifier = "HUAWEIuserIdentifier" + Math.floor(Math.random() * 1001);
    apmsPlugin.setUserIdentifier(userIdentifier).then(() => {
        alert("setUserIdentifier: success");
    }).catch((error) => alert("setUserIdentifier :: Error! " + JSON.stringify(error, null, 1)));
}
function traceTestSumFunc(num1, num2) {
    return new Promise((resolve, reject) => {
        for (let i = 0; i < num2; i++) {
            num1 += i;
        }
        resolve(num1 + num2);
    });
}
$("addCustomTrace").onclick = () => {
    let ctname = "addCustomTraceTestFunc" + Math.floor(Math.random() * 1001);
    AGCAPM.addCustomTrace(traceTestSumFunc(7, 90000000), ctname, true).then((res) => {
        alert("addCustomTrace: success :: " + res);
    }).catch((error) => alert("addCustomTrace :: Error! " + JSON.stringify(error, null, 1)));
}

//customTrace
$("testCustomTrace").onclick = () => {
    let traceName = "TNtraceName" + Math.floor(Math.random() * 1001);
    apmsPlugin.createCustomTrace(traceName).then(async (customTrace) => {
        mCustomTrace = customTrace;
        await mCustomTrace.start();
        console.log("ct_start: success ");

        let propertyName1 = "propertyName1";
        let propertyValue1 = "propertyValue1";
        await mCustomTrace.putProperty(propertyName1, propertyValue1);
        console.log("putProperty: success ");

        let propertyName2 = "propertyName2";
        let propertyValue2 = "propertyValue2";
        await mCustomTrace.putProperty(propertyName2, propertyValue2);
        console.log("putProperty: success ");

        let measureName1 = "measureName1";
        let measureValue1 = "1";
        await mCustomTrace.incrementMeasure(measureName1, measureValue1);
        console.log("incrementMeasure: success ");

        let measureName2 = "measureName2";
        let measureValue2 = "1";
        await mCustomTrace.putMeasure(measureName2, measureValue2);
        console.log("putMeasure: success ");

        await traceTestSumFunc(11, 90000000);
        await mCustomTrace.stop();
        alert("testCustomTrace: success");

    }).catch((error) => alert("testCustomTrace :: Error! " + JSON.stringify(error, null, 1)));
}

$("ct_getTraceProperties").onclick = () => {
    mCustomTrace.getTraceProperties().then((customTraceProperties) => {
        alert("ct_getTraceProperties: success :: customTraceProperties:\n" + JSON.stringify(customTraceProperties, null, 1));
    }).catch((error) => alert("ct_getTraceProperties :: Error! " + JSON.stringify(error, null, 1)));
}

//networkMeasure
$("testNetworkMeasure").onclick = () => {
    let url = "https://jsonplaceholder.typicode.com/todos";
    let httpMethod = "GET";
    apmsPlugin.createNetworkMeasure(url, httpMethod).then(async (networkMeasure) => {
        mNetworkMeasure = networkMeasure;

        await mNetworkMeasure.start();
        console.log("start: success ");

        const xhr = new XMLHttpRequest();
        xhr.open(httpMethod, url);
        xhr.send();
        xhr.onload = async function () {
            console.log(this.responseText);
            statusCode = this.status;
            await mNetworkMeasure.setStatusCode(statusCode);
            console.log("setStatusCode: success ");

            let contentType = "application/json";
            await mNetworkMeasure.setContentType(contentType);
            console.log("setContentType: success ");

            let bytesSent = "0";
            await mNetworkMeasure.setBytesSent(bytesSent);
            console.log("setBytesSent: success ");

            let bytesReceived = xhr.response.length.toString();
            await mNetworkMeasure.setBytesReceived(bytesReceived);
            console.log("setBytesReceived: success ");
            
            let propertyName1 = "propertyName1";
            let propertyValue1 = "propertyValue1";
            await mNetworkMeasure.putProperty(propertyName1, propertyValue1);
    
            let propertyName2 = "propertyName2";
            let propertyValue2 = "propertyValue2";
            await mNetworkMeasure.putProperty(propertyName2, propertyValue2);
            console.log("putProperty: success ");
    
            await mNetworkMeasure.stop();
            console.log("stop: success ");
    
            alert("testNetworkMeasure: success");
        }
    }).catch((error) => alert("testNetworkMeasure :: Error! " + JSON.stringify(error, null, 1)));
}

$("nm_getProperties").onclick = () => {
    mNetworkMeasure.getProperties().then((networkMeasureProperties) => {
        alert("nm_getProperties: success : networkMeasureProperties:\n" + JSON.stringify(networkMeasureProperties, null, 1));
    }).catch((error) => alert("nm_getProperties :: Error! " + JSON.stringify(error, null, 1)));
}