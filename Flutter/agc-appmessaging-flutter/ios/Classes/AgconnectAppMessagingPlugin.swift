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
import Flutter
import UIKit
import AGConnectCore
import AGConnectAppMessaging

public class AgconnectAppMessagingPlugin: NSObject, FlutterPlugin, AGCAppMessagingDelegate, AGCAppMessagingDisplayDelegate {
    
    let appmessagingDismissHandler = AppMessagingOnMessageDismissEventHandler.init()
    let appmessagingCustomHandler = AppMessagingCustomEventHandler.init()
    let appmessagingClickHandler = AppMessagingOnMessageClickEventHandler.init()
    let appmessagingDisplayHandler = AppMessagingOnMessageDisplayEventHandler.init()
    let appmessagingErrorHandler = AppMessagingOnMessageErrorEventHandler.init()
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "com.huawei.agc.flutter.appmessaging_methodchannel", binaryMessenger: registrar.messenger())
        let instance = AgconnectAppMessagingPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        instance.initEventChannel(messenger: registrar.messenger())
        registrar.addApplicationDelegate(instance)
    }
    
    func initEventChannel(messenger: FlutterBinaryMessenger) {
        let eventChannelOnMessageDismiss = FlutterEventChannel(name: "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDismiss", binaryMessenger:
                                                                messenger)
        let eventChannelOnMessageClick = FlutterEventChannel(name: "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageClick", binaryMessenger:
                                                                messenger)
        let eventChannelOnMessageDisplay = FlutterEventChannel(name: "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDisplay", binaryMessenger:
                                                                messenger)
        let eventChannelOnMessageError = FlutterEventChannel(name: "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageError", binaryMessenger:
                                                                messenger)
        let eventChannelCustomEvent = FlutterEventChannel(name: "com.huawei.agc.flutter.appmessaging_eventchannel_customEvent", binaryMessenger:
                                                            messenger)
        
        eventChannelCustomEvent.setStreamHandler(appmessagingCustomHandler)
        eventChannelOnMessageError.setStreamHandler(appmessagingErrorHandler)
        eventChannelOnMessageDismiss.setStreamHandler(appmessagingDismissHandler)
        eventChannelOnMessageClick.setStreamHandler(appmessagingClickHandler)
        eventChannelOnMessageDisplay.setStreamHandler(appmessagingDisplayHandler)
    }
    
    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable : Any] = [:]) -> Bool {
        AGCInstance.startUp()
        AGCAppMessaging.sharedInstance().delegate = self
        let n = Bundle.main.object(forInfoDictionaryKey: "AGCCustomView") as? NSNumber
        let value = n?.boolValue ?? false
        if(value) {
            AGCAppMessaging.sharedInstance().displayComponent = self
        }
        return true
    }
    
    public func appMessaging(onDisplay message: AGCAppMessagingDisplayMessage) {
        appmessagingDisplayHandler.showData(message)
    }
    
    public func appMessaging( onClick message: AGCAppMessagingDisplayMessage, button: AGCAppMessagingActionButton ) {
        appmessagingClickHandler.showData(message)
    }
    
    public func appMessaging( onDismiss message: AGCAppMessagingDisplayMessage, dismissType: AGCAppMessagingDismissType ) {
        var mDismissType = ""
        switch dismissType {
        case AGCAppMessagingDismissType.click:
            mDismissType = "CLICK"
        case AGCAppMessagingDismissType.clickOutside:
            mDismissType = "CLICK_OUTSIDE"
        case AGCAppMessagingDismissType.auto:
            mDismissType = "AUTO"
        case AGCAppMessagingDismissType.swipe:
            mDismissType = "SWIPE"
        default:
            break
        }
        appmessagingDismissHandler.showData(message, dismissKey: mDismissType)
    }
    
    public func appMessaging(onError message: AGCAppMessagingDisplayMessage) {
        NotificationCenter.default.post(name: NSNotification.Name("AGCAppMessagingNotificationOnMessageError"), object: message)
    }
    
    public func appMessageDisplay(_ message: AGCAppMessagingDisplayMessage, delegate: AGCAppMessagingDelegate) {
        appmessagingCustomHandler.showData(message)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        
        let method = Methods.init()
        let agcAppMessaging = FlutterAgconnectAppMessaging.init()
        switch call.method {
        
        case method.IS_DISPLAY_ENABLE:
            agcAppMessaging.isDisplayEnable( resolve: { (response) in
                result(response)
            })
        case method.TRIGGER:
            guard let args = call.arguments as? NSDictionary else {
                return
            }
            agcAppMessaging.trigger(args, resolve: { (response) in
                result(response)
            })
        case method.SET_DISPLAY_ENABLE:
            guard let args = call.arguments as? Bool else {
                return
            }
            agcAppMessaging.setDisplayEnable(args, resolve: { (response) in
                result(response)
            })
        case method.SET_FETCH_MESSAGE_ENABLE:
            guard let args = call.arguments as? Bool else {
                return
            }
            agcAppMessaging.setFetchMessageEnable(args, resolve: { (response) in
                result(response)
            })
        case method.IS_FETCH_MESSAGE_ENABLE:
            agcAppMessaging.isFetchMessageEnable(resolve: { (response) in
                result(response)
            })
        case method.SET_FORCE_FETCH:
            result(FlutterError(code: "", message: "setForceFetch", details: " Enable the debugging mode for your app.Add the 'AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode."))
        case method.REMOVE_CUSTOM_VIEW:
            result(FlutterError(code: "", message: "removeCustomView", details: " Enable the debugging mode for your app.Add the 'AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode."))
        case method.SET_DISPLAY_LOCATION:
            guard let args = call.arguments as? Int else {
                return
            }
            agcAppMessaging.setDisplayLocation(args, resolve: { (response) in
                result(response)
            })
        case method.HANDLE_CUSTOM_VIEW_MESSAGE_EVENT:
            guard let args = call.arguments as? NSDictionary else {
                return
            }
            agcAppMessaging.handleCustomViewMessageEvent(args, resolve: { (response) in
                result(response)
            })
        default:
            result(FlutterError(code: "platformError", message: "Not supported on iOS platform", details: ""));
        }
    }
    struct Methods {
        let TRIGGER = "trigger"
        let SET_DISPLAY_ENABLE = "setDisplayEnable"
        let IS_DISPLAY_ENABLE = "isDisplayEnable"
        let SET_FETCH_MESSAGE_ENABLE = "setFetchMessageEnable"
        let IS_FETCH_MESSAGE_ENABLE = "isFetchMessageEnable"
        let SET_FORCE_FETCH = "setForceFetch"
        let SET_DISPLAY_LOCATION = "setDisplayLocation"
        let REMOVE_CUSTOM_VIEW = "removeCustomView"
        let HANDLE_CUSTOM_VIEW_MESSAGE_EVENT = "handleCustomViewMessageEvent"
    }
}
