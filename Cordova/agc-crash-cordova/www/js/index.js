/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

var btnTest = document.getElementById("testCrash");
var btnEn = document.getElementById("crashCollectEnable");
var btnDis = document.getElementById("crashCollectDisable");

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    btnEn.addEventListener("click", crashCollectEnable);
    btnDis.addEventListener("click", crashCollectDisable);
    btnTest.addEventListener("click", testCrash);
}

function crashCollectEnable() {
    AGCCrashPlugin.enableCrashCollection(true);
}

function crashCollectDisable() {
    AGCCrashPlugin.enableCrashCollection(false);
}

function testCrash() {
    AGCCrashPlugin.setUserId("cordova user");

    AGCCrashPlugin.setCustomKey("key1", "value1");

    AGCCrashPlugin.setCustomKey("key2", 3);

    AGCCrashPlugin.setCustomKey("key3", true);

    AGCCrashPlugin.log("just test log");

    AGCCrashPlugin.logWithLevel(3, "just test logWithLevel");

    AGCCrashPlugin.testIt();

    function success() {
        console.log('success');
    }

    function error() {
        console.log('error');
    }
}
