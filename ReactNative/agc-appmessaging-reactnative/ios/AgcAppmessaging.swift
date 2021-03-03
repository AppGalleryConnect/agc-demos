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

import Foundation
import AGConnectAppMessaging
import AGConnectCore

@objc(AGCAppmessaging)

/// Provides methods to initialize AGCAppmessaging Kit and implement appmessaging functions.
class AGCAppmessaging: RCTEventEmitter, AgcAppmessagingHandling{

    /// All the AGCAppmessaging API's can be reached via AgcAppmessagingViewModel class instance.
    private lazy var viewModel: AgcAppmessagingViewModel = AgcAppmessagingViewModel()

    /// Initialization of AGCAppMessagingModule in RN Side.
    override init() {
        super.init()
        addNotifications()
    }

    /// Data synchronization from the AppGallery Connect server.
    /// - Parameters:
    ///   - enable: Boolean value that refers to whether enable or disable fetch message feature.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func setFetchMessageEnable(_ enable: Bool, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.setFetchMessageEnable(enabled: true)
            strongSelf.handle(resolve: resolve, true)
        }
    }

    /// Sets displayEnable in AGCAppMessaging.
    /// - Parameters:
    ///   - enable: Boolean value that refers to whether enable or disable display feature.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func setDisplayEnable(_ enable: Bool, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.setDisplayEnable(enabled: true)
            strongSelf.handle(resolve: resolve, true)
        }
    }

    /// Returns the result of isDisplayEnable.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func isDisplayEnable(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.isDisplayEnable(){ [weak self] (result, error) in
                guard let strongSelf = self else {return}
                strongSelf.handle(resolve: resolve, result)
            }
        }
    }


    ///  Sets whether to allow data synchronization from the AppGallery Connect server.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func isFetchMessageEnable(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.isFetchMessageEnable(){ [weak self] (result, error) in
                guard let strongSelf = self else {return}
                strongSelf.handle(resolve: resolve, result)
            }
        }
    }

    /// Sets force fetch to data synchronization from the AppGallery Connect server.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func setForceFetch(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.setForceFetch(){ [weak self] (result, error) in
                guard let strongSelf = self else {return}
                strongSelf.handle(resolve: resolve, result)
            }
        }
    }

    /// Removes Custom View.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func removeCustomView(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.removeCustomView(){ [weak self] (result, error) in
                guard let strongSelf = self else {return}
                strongSelf.handle(resolve: resolve, result)
            }
        }
    }

    /// Sets display location of appMessage whether at the bottom or the center.
    /// - Parameters:
    ///   - location: Location instance that will be get via Constants.
    ///   - resolve:  In the success scenario, Boolean instance, true, is returned.
    ///   - reject:  Exception instance is returned in the failure scenario.
    @objc func setDisplayLocation(_ location: Int, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.setDisplayLocation(location: location)
            strongSelf.handle(resolve: resolve, true)
        }
    }

    /// Triggers message display.
    /// - Parameters:
    ///   - eventId: String instance that refers to eventId.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func trigger(_ eventId: String, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.trigger(eventId: eventId)
            strongSelf.handle(resolve: resolve, true)
        }
    }

    /// When using custom app message layout, handle custom app message click events like below.
    /// - Parameters:
    ///   - params: gets eventType (ON_MESSAGE_DISPLAY, ON_MESSAGE_CLICK, ON_MESSAGE_DISMISS, ON_MESSAGE_ERROR) and dismissType param(DISMISS_TYPE_CLICK, DISMISS_TYPE_CLICK_OUTSIDE, DISMISS_TYPE_AUTO, DISMISS_TYPE_SWIPE) when using ON_MESSAGE_DISMISS.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    ///   - reject: Exception instance is returned in the failure scenario.
    @objc func handleCustomViewMessageEvent(_ params: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        AgcAppmessagingLog.debug(#function) { [weak self] in
            guard let strongSelf = self else {return}
            strongSelf.viewModel.handleCustomViewMessageEvent(params)
            strongSelf.handle(resolve: resolve, true)
        }
    }

    /// **supportedEvents** must be implemented to return an array of event names that we can listen to.
    /// - Returns: Array of Strings.
    override func supportedEvents() -> [String]! {
        return ["onMessageDisplay", "onMessageClick", "onMessageDismiss", "onMessageError", "customView"]
    }

    /// **requiresMainQueueSetup** must be implemented to use constantsToExport or have implemented an init() method for UIKit components in React Native v0.49+.
    /// - Returns: a Boolean: **true** if the class needed to be initialized on the main thread, **false** if the class can be initialized on a background thread.
    @objc override static func requiresMainQueueSetup() -> Bool {
        return false
    }

    // MARK: - Deinit

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    // MARK: - Notification Functions

    private func addNotifications(){
        NotificationCenter.default.addObserver(self, selector: #selector(self.onRNMsgDisplay(_:)), name: Notification.Name.rnAgcMsgDisplayNotf, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.onRNMsgClick(_:)), name: Notification.Name.rnAgcMsgClickNotf, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.onRNMsgDismiss(_:)), name: Notification.Name.rnAgcMsgDismissNotf, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.onRNMsgError(_:)), name: Notification.Name.rnAgcMsgErrorNotf, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.onRNMsgCustomView(_:)), name: Notification.Name.rnAgcMsgCustomViewNotf, object: nil)
    }

    @objc(onRNMsgDisplay:)
    func onRNMsgDisplay(_ notification: NSNotification) {
        guard let appMessage = notification.object as? AGCAppMessagingDisplayMessage else {return}
        sendEvent(withName: "onMessageDisplay", body: ["appMessage": appDisplayMessagetoJson(appMessage)])
    }

    @objc(onRNMsgClick:)
    func onRNMsgClick(_ notification: NSNotification) {
        guard let appMessage = notification.object as? AGCAppMessagingDisplayMessage else {return}
        sendEvent(withName: "onMessageClick", body: ["appMessage": appDisplayMessagetoJson(appMessage)])
    }

    @objc(onRNMsgDismiss:)
    func onRNMsgDismiss(_ notification: NSNotification) {
        guard let appMessage = notification.object as? AGCAppMessagingDisplayMessage else {return}
        guard let dict = notification.userInfo as NSDictionary?, let dismissType = dict["dismissType"] as? String else { return }
        sendEvent(withName: "onMessageDismiss", body: ["appMessage": appDisplayMessagetoJson(appMessage), "dismissType" : dismissType])
    }

    @objc(onRNMsgError:)
    func onRNMsgError(_ notification: NSNotification) {
        guard let appMessage = notification.object as? AGCAppMessagingDisplayMessage else {return}
        sendEvent(withName: "onMessageError", body: ["appMessage": appDisplayMessagetoJson(appMessage)])
    }

    @objc(onRNMsgCustomView:)
    func onRNMsgCustomView(_ notification: NSNotification) {
        guard let appMessage = notification.object as? AGCAppMessagingDisplayMessage else {return}
        sendEvent(withName: "customView", body: ["appMessage": appDisplayMessagetoJson(appMessage)])
    }

    // MARK: - Encoder to RN Side

    private func appDisplayMessagetoJson(_ appMessage: AGCAppMessagingDisplayMessage) -> [String: Any]{
        appMessagingDisplay = appMessage
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

    private func hexStringFromColor(color: UIColor) -> String {
        let components = color.cgColor.components
        let r: CGFloat = components?[0] ?? 0.0
        let g: CGFloat = components?[1] ?? 0.0
        let b: CGFloat = components?[2] ?? 0.0
        let hexString = String.init(format: "#%02lX%02lX%02lX", lroundf(Float(r * 255)), lroundf(Float(g * 255)), lroundf(Float(b * 255)))
        return hexString
    }
}


// MARK: - Notification Names
var appMessagingDisplay:AGCAppMessagingDisplayMessage?

struct RNAGCAppMessagingConstants {
    static let onMessageDisplay: String="onMessageDisplay"
    static let onMessageClick: String="onMessageClick"
    static let onMessageDismiss: String="onMessageDismiss"
    static let onMessageError: String="onMessageError"
    static let dismissTypeClick: String="dismissTypeClick"
    static let dismissTypeClickOutside: String="dismissTypeClickOutside"
    static let dismissTypeAuto: String="dismissTypeAuto"
    static let dismissTypeSwipe: String="dismissTypeSwipe"
}

public extension Notification.Name {
    static let rnAgcMsgDisplayNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationRNOnMessageDisplay")
    static let rnAgcMsgClickNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationRNOnMessageClick")
    static let rnAgcMsgDismissNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationRNOnMessageDismiss")
    static let rnAgcMsgErrorNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationRNOnMessageError")
    static let rnAgcMsgCustomViewNotf = Notification.Name(rawValue: "AGCAppMessagingNotificationRNCustomView")
}

import AGConnectAppMessaging
import AGConnectCore

// - MARK: - Exposed functions to RN Side by AppDelegate.

@objc public extension UIResponder{

    @objc func agcAppMessagingStartUp() {
        AGCInstance.startUp()
        AGCAppMessaging.sharedInstance().delegate = self

        if let isCustomView = Bundle.main.infoDictionary?["AGCCustomView"] as? Bool {
            if isCustomView{
                AGCAppMessaging.sharedInstance().displayComponent = self
            }
        }
    }
}

extension UIResponder: AGCAppMessagingDelegate{
    public func appMessaging(onClick message: AGCAppMessagingDisplayMessage, button: AGCAppMessagingActionButton) {
        NotificationCenter.default.post(name: Notification.Name.rnAgcMsgClickNotf, object: message)
    }

    public func appMessaging(onDismiss message: AGCAppMessagingDisplayMessage, dismissType: AGCAppMessagingDismissType) {
        var mDismissType = "UNKNOWN_DISMISS_TYPE"
        switch dismissType {
        case AGCAppMessagingDismissType.click:
            mDismissType = "CLICK"
            break
        case AGCAppMessagingDismissType.clickOutside:
            mDismissType = "CLICK_OUTSIDE"
            break
        case AGCAppMessagingDismissType.swipe:
            mDismissType = "SWIPE"
            break
        case AGCAppMessagingDismissType.auto:
            mDismissType = "AUTO"
            break
        default:
            break
        }

        NotificationCenter.default.post(name: Notification.Name.rnAgcMsgDismissNotf, object: message, userInfo: ["dismissType": mDismissType])
    }

    public func appMessaging(onError message: AGCAppMessagingDisplayMessage) {
        NotificationCenter.default.post(name: Notification.Name.rnAgcMsgErrorNotf, object: message)
    }

    public func appMessaging(onDisplay message: AGCAppMessagingDisplayMessage) {
        NotificationCenter.default.post(name: Notification.Name.rnAgcMsgDisplayNotf, object: message)
    }
}
extension UIResponder: AGCAppMessagingDisplayDelegate{
    public func appMessageDisplay(_ message: AGCAppMessagingDisplayMessage, delegate: AGCAppMessagingDelegate) {
        NotificationCenter.default.post(name: Notification.Name.rnAgcMsgCustomViewNotf, object: message)
    }
}
