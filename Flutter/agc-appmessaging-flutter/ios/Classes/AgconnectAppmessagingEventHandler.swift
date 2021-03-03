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

import Foundation
import AGConnectAppMessaging

class AppMessagingOnMessageDismissEventHandler:NSObject,  FlutterStreamHandler {
    
    private var _eventSink: FlutterEventSink?
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self._eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self._eventSink = nil
        return nil
    }
    
    @objc func  showData(_ appMessage: AGCAppMessagingDisplayMessage, dismissKey: String) {
        guard let _eventSink = _eventSink else {
            return
        }
        
        var appMessageData = getappMessagedata(appMessage: appMessage)
        appMessageData["dismissType"] = dismissKey
        _eventSink(appMessageData)
    }
}

class AppMessagingOnMessageDisplayEventHandler:NSObject,  FlutterStreamHandler {
    
    private var _eventSink: FlutterEventSink?
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        
        self._eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self._eventSink = nil
        return nil
    }
    
    @objc func  showData(_ appMessage: AGCAppMessagingDisplayMessage) {
        guard let _eventSink = _eventSink else {
            return
        }
        let appMessageData = getappMessagedata(appMessage: appMessage)
        _eventSink(appMessageData)
    }
}

class AppMessagingOnMessageClickEventHandler:NSObject,  FlutterStreamHandler {
    
    private var _eventSink: FlutterEventSink?
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        
        self._eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self._eventSink = nil
        return nil
    }
    
    @objc func  showData(_ appMessage: AGCAppMessagingDisplayMessage) {
        guard let _eventSink = _eventSink else {
            return
        }
        let appMessageData = getappMessagedata(appMessage: appMessage)
        _eventSink(appMessageData)
    }
}

class AppMessagingOnMessageErrorEventHandler:NSObject,  FlutterStreamHandler {
    
    private var _eventSink: FlutterEventSink?
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self._eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self._eventSink = nil
        return nil
    }
    
    @objc func  showData(_ appMessage: AGCAppMessagingDisplayMessage) {
        guard let _eventSink = _eventSink else {
            return
        }
        let appMessageData = getappMessagedata(appMessage: appMessage)
        _eventSink(appMessageData)
    }
}

class AppMessagingCustomEventHandler:NSObject,  FlutterStreamHandler {
    
    private var _eventSink: FlutterEventSink?
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self._eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self._eventSink = nil
        return nil
    }
    
    @objc func  showData(_ appMessage: AGCAppMessagingDisplayMessage) {
        guard let _eventSink = _eventSink else {
            return
        }
        let appMessageData = getappMessagedata(appMessage: appMessage)
        _eventSink(appMessageData)
    }
    
    /// When using custom app message layout, handle custom app message click events like below.
    /// - Parameters:
    ///   - params: gets eventType (ON_MESSAGE_DISPLAY, ON_MESSAGE_CLICK, ON_MESSAGE_DISMISS, ON_MESSAGE_ERROR) and dismissType param(DISMISS_TYPE_CLICK, DISMISS_TYPE_CLICK_OUTSIDE, DISMISS_TYPE_AUTO, DISMISS_TYPE_SWIPE) when using ON_MESSAGE_DISMISS.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    public func handleCustomViewMessageEvent(params: NSDictionary, result: @escaping FlutterResult) -> Void {
        if let params = (params as? [String : Any]){
            guard let eventType = params["eventType"] as? String else {
                fatalError("Failed to get eventType parameter.")
            }
            guard appMessagingDisplay != nil else {
                return
            }
            switch eventType {
            case AGCAppMessagingConstants.onMessageClick:
                AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onClick: appMessagingDisplay!, button: AGCAppMessagingActionButton())
                break
            case AGCAppMessagingConstants.onMessageDismiss:
                guard let dismissType = params["dismissType"] as? String else {
                    return
                }
                var mDismissType: AGCAppMessagingDismissType = .auto
                switch dismissType {
                case AGCAppMessagingConstants.dismissTypeClick:
                    mDismissType = .click
                    break
                case AGCAppMessagingConstants.dismissTypeClickOutside:
                    mDismissType = .clickOutside
                    break
                case AGCAppMessagingConstants.dismissTypeSwipe:
                    mDismissType = .swipe
                    break
                default:
                    break
                }
                AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onDismiss: appMessagingDisplay!, dismissType: mDismissType)
                break
            case AGCAppMessagingConstants.onMessageDisplay:
                AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onDisplay: appMessagingDisplay!)
                break
            case AGCAppMessagingConstants.onMessageError:
                AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onError: appMessagingDisplay!)
                break
            default:
                break
            }
        }
    }
    
    // MARK: - Notification Names
    var appMessagingDisplay:AGCAppMessagingDisplayMessage?
    
    struct AGCAppMessagingConstants {
        static let onMessageDisplay: String="onMessageDisplay"
        static let onMessageClick: String="onMessageClick"
        static let onMessageDismiss: String="onMessageDismiss"
        static let onMessageError: String="onMessageError"
        static let dismissTypeClick: String="dismissTypeClick"
        static let dismissTypeClickOutside: String="dismissTypeClickOutside"
        static let dismissTypeAuto: String="dismissTypeAuto"
        static let dismissTypeSwipe: String="dismissTypeSwipe"
    }
}
public extension Notification.Name {
    static let AgcMsgDisplayNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationOnMessageDisplay")
    static let AgcMsgClickNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationOnMessageClick")
    static let AgcMsgDismissNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationOnMessageDismiss")
    static let AgcMsgErrorNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationOnMessageError")
    static let AgcMsgCustomViewNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationDisplay")
}
func hexStringFromColor(color: UIColor) -> String {
    let components = color.cgColor.components
    let r: CGFloat = components?[0] ?? 0.0
    let g: CGFloat = components?[1] ?? 0.0
    let b: CGFloat = components?[2] ?? 0.0
    let hexString = String.init(format: "#%02lX%02lX%02lX", lroundf(Float(r * 255)), lroundf(Float(g * 255)), lroundf(Float(b * 255)))
    return hexString
}

func getappMessagedata(appMessage: AGCAppMessagingDisplayMessage)->[String:Any]{
    var appMessageData:[String: Any] = [:]
    var base:[String: Any] = [:]
    base["id"] = "\(appMessage.messageId)"
    base["startTime"] = Int(appMessage.startTime.timeIntervalSince1970*1000)
    if let endTime = appMessage.endTime {
        base["endTime"] = Int(endTime.timeIntervalSince1970*1000)
    }else {
        base["endTime"] = -1
    }
    base["frequencyType"] = appMessage.frequencyType.rawValue
    base["frequencyValue"] = appMessage.frequencyValue
    if(!appMessage.isTest){
        base["testFlag"] = 0
    }else{
        base["testFlag"] = 1
    }
    base["triggerEvents"] = appMessage.triggerEvents
    let mType = appMessage.messageType
    switch mType {
    case AGCAppMessagingDisplayMessageType.card:
        base["messageType"] = "CARD"
    case AGCAppMessagingDisplayMessageType.banner:
        base["messageType"] = "BANNER"
    case AGCAppMessagingDisplayMessageType.picture:
        base["messageType"] = "PICTURE"
    case AGCAppMessagingDisplayMessageType.unknown:
        base["messageType"] = "UN_SUPPORT"
    default :
        print("error messageType")
    }
    appMessageData["base"] = base
    if let message = appMessage as? AGCAppMessagingCardDisplay{
        var card:[String: Any] = [:]
        card["title"] = message.title
        card["titleColor"] = hexStringFromColor(color: message.titleColor)
        card["titleColorOpenness"] = Double (message.titleColor.cgColor.alpha)
        card["body"] = message.body
        card["bodyColor"] = hexStringFromColor(color: message.bodyColor)
        card["bodyColorOpenness"] = Double (message.bodyColor.cgColor.alpha)
        card["backgroundColor"] = hexStringFromColor(color: message.backgroundColor)
        card["backgroundColorOpenness"] = Double (message.backgroundColor.cgColor.alpha)
        card["portraitPictureURL"] = message.portraitPictureURL?.absoluteString
        card["landscapePictureURL"] = message.landscapePictureURL?.absoluteString
        
        var majorButton:[String: Any] = [:]
        if let majorButtonText = message.majorButton?.text {
            majorButton["text"] = majorButtonText
        }
        if let majorButtonTextColor = message.majorButton?.textColor {
            majorButton["textColor"] = hexStringFromColor(color: majorButtonTextColor)
        }
        if let majorButtonTextColorOpenness = message.majorButton?.textColor.cgColor.alpha {
            majorButton["textColorOpenness"] = Double (majorButtonTextColorOpenness)
        }
        if let majorButtonActionURL = message.majorButton?.actionURL?.absoluteString {
            majorButton["actionURL"] = majorButtonActionURL
        }
        card["majorButton"] = majorButton
        var minorButton:[String: Any] = [:]
        if let minorButtonText = message.minorButton?.text {
            minorButton["text"] = minorButtonText
        }
        if let minorButtonTextColor = message.minorButton?.textColor {
            minorButton["textColor"] = hexStringFromColor(color: minorButtonTextColor)
        }
        if let minorButtonTextColorOpenness = message.minorButton?.textColor.cgColor.alpha {
            minorButton["textColorOpenness"] = Double (minorButtonTextColorOpenness)
        }
        if let minorButtonActionURL = message.minorButton?.actionURL?.absoluteString {
            minorButton["actionURL"] = minorButtonActionURL
        }
        card["minorButton"] = minorButton
        appMessageData["card"] = card
    }else if let message = appMessage as? AGCAppMessagingPictureDisplay {
        var picture:[String: Any] = [:]
        picture["pictureURL"] = message.pictureURL?.absoluteString
        picture["actionURL"] = message.actionURL?.absoluteString
        appMessageData["picture"] = picture
    }
    else if let message = appMessage as? AGCAppMessagingBannerDisplay {
        var banner:[String: Any] = [:]
        banner["title"] = message.title
        banner["titleColor"] = hexStringFromColor(color: message.titleColor)
        banner["titleColorOpenness"] = Double (message.titleColor.cgColor.alpha)
        banner["body"] = message.body
        banner["bodyColor"] = hexStringFromColor(color: message.bodyColor)
        banner["bodyColorOpenness"] = Double (message.bodyColor.cgColor.alpha)
        banner["backgroundColor"] = hexStringFromColor(color: message.backgroundColor)
        banner["backgroundColorOpenness"] = Double (message.backgroundColor.cgColor.alpha)
        banner["pictureURL"] = message.pictureURL?.absoluteString
        banner["actionURL"] = message.actionURL?.absoluteString
        appMessageData["banner"] = banner
    }
    return appMessageData
}
