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
import {Alert, NativeEventEmitter, NativeModules, Platform} from 'react-native';
import EventTypes from "./constants/EventTypes";
import LocationTypes from "./constants/LocationTypes";
import DismissTypes from "./constants/DismissTypes";

const {AGCAppMessagingModule} = NativeModules;

const isIOS = Platform.OS === 'ios';

class AGCAppMessaging{

    constructor() {
        this.emitter = new NativeEventEmitter(AGCAppMessagingModule);
    }

    static getInstance() {
        if (!this.instance) {
            this.instance = new AGCAppMessaging();
        }
        return this.instance;
    }

    addMessageDisplayListener(listener){
        return subscription = this.emitter.addListener(
            EventTypes.ON_MESSAGE_DISPLAY, listener
        );
    }

    addMessageClickListener(listener){
        return subscription = this.emitter.addListener(
            EventTypes.ON_MESSAGE_CLICK, listener
        );
    }

    addMessageDismissListener(listener){
        return subscription = this.emitter.addListener(
            EventTypes.ON_MESSAGE_DISMISS, listener
        );
    }

    addMessageErrorListener(listener){
        return subscription = this.emitter.addListener(
            EventTypes.ON_MESSAGE_ERROR, listener
        );
    }

    addMessageCustomViewListener(listener){
        return subscription = this.emitter.addListener(
            EventTypes.CUSTOM_VIEW, listener
        );
    }

    setFetchMessageEnable(enable) {
        if (enable != null) {
            return AGCAppMessagingModule.setFetchMessageEnable(enable);
        }
    }

    setDisplayEnable(enable) {
        if (enable != null) {
            return AGCAppMessagingModule.setDisplayEnable(enable);
        }
    }

    isDisplayEnable() {
        return AGCAppMessagingModule.isDisplayEnable();
    }

    isFetchMessageEnable() {
        return AGCAppMessagingModule.isFetchMessageEnable();
    }

    setForceFetch() {
        return AGCAppMessagingModule.setForceFetch();
    }

    setDisplayLocation(location) {
        return AGCAppMessagingModule.setDisplayLocation(location);
    }

    removeCustomView() {
        if (isIOS){
            console.log("This function is not available in iOS platforms.");
            return;
        }
        return AGCAppMessagingModule.removeCustomView();
    }

    trigger(eventId) {
        if (eventId != null) {
            return AGCAppMessagingModule.trigger(eventId);
        }
    }

    handleCustomViewMessageEvent(params) {
        if (params != null) {
            return AGCAppMessagingModule.handleCustomViewMessageEvent(params);
        }
    }
}
AGCAppMessaging.EventTypes = EventTypes;
AGCAppMessaging.LocationTypes = LocationTypes;
AGCAppMessaging.DismissTypes = DismissTypes;
export default AGCAppMessaging;
