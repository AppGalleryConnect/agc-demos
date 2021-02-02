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
document.addEventListener('deviceready', onDeviceReady, false);
const log = document.getElementById("log");

async function onDeviceReady() {
    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
    document.getElementById("isDisplayEnable").onclick = isDisplayEnable;
    document.getElementById("setDisplayEnable").onclick = setDisplayEnableToFalse;
    document.getElementById("isFetchMessageEnable").onclick = isFetchMessageEnable;
    document.getElementById("setFetchMessageEnable").onclick = setFetchMessageEnableToFalse;
    document.getElementById("setDisplayLocation").onclick = setDisplayLocation;

    AGCAppMessaging.addListener(AGCAppMessaging.Events.ON_MESSAGE_DISPLAY, async (appMessage) => {
        const id = await appMessage.getId();
        console.log("Display Event-Message ID:" + id);
    });

    AGCAppMessaging.addListener(AGCAppMessaging.Events.ON_MESSAGE_DISMISS, async (appMessage, dismissType) => {
        const id = await appMessage.getId();
        console.log("Dismiss Event-Message ID:" + id);
        console.log("Dismiss Type: " + dismissType);
    });

    AGCAppMessaging.addListener(AGCAppMessaging.Events.ON_MESSAGE_CLICK, async (appMessage) => {
        const id = await appMessage.getId();
        console.log("Click Event-Message ID:" + id);
    });

    AGCAppMessaging.addListener(AGCAppMessaging.Events.ON_MESSAGE_ERROR, async (appMessage) => {
        const id = await appMessage.getId();
        console.log("Error Event-Message ID:" + id);
    });
}

async function isDisplayEnable() {
    const isDisplayEnable = await AGCAppMessaging.isDisplayEnable();
    log.innerHTML = "<br>" + "isDisplayEnable = " + isDisplayEnable + log.innerHTML;
}

async function setDisplayEnableToFalse() {
    await AGCAppMessaging.setDisplayEnable(false);
    log.innerHTML = "<br>" + "Display is set to false." + log.innerHTML;
}

async function isFetchMessageEnable() {
    const isFetchMessageEnable = await AGCAppMessaging.isFetchMessageEnable();
    log.innerHTML = "<br>" + "isFetchMessageEnable = " + isFetchMessageEnable + log.innerHTML;
}

async function setFetchMessageEnableToFalse() {
    await AGCAppMessaging.setFetchMessageEnable(false);
    log.innerHTML = "<br>" + "Fetch message enable is set to false." + log.innerHTML;
}

async function setDisplayLocation() {
    await AGCAppMessaging.setDisplayLocation(AGCAppMessaging.Location.BOTTOM);
    log.innerHTML = "<br>" + "Display location is set as BOTTOM." + log.innerHTML;
}
