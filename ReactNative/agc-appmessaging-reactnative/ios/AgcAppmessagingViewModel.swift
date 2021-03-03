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

import AGConnectAppMessaging

/// All the AGConnectAppMessaging API's can be reached via AgcAppmessagingViewModel class instance.
public class AgcAppmessagingViewModel {

  /// **CompletionHandler** is a typealias that provides result and error when the request is completed.
  /// - Parameters:
  ///   - result: Any Object that will be returned when the result comes.
  ///   - error: NSError that will be returned when there is an error.
  public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void

  private var completionHandler: CompletionHandler?

  /// Data synchronization from the AppGallery Connect server.
  /// - Parameter enabled: Boolean value that refers to whether enable or disable fetch message feature.
  func setFetchMessageEnable(enabled: Bool){
    AGCAppMessaging.sharedInstance().isFetchMessageEnable = enabled
  }

  /// Sets displayEnable in AGCAppMessaging.
  /// - Parameter enabled: Boolean value that refers to whether enable or disable display feature.
  func setDisplayEnable(enabled: Bool){
    AGCAppMessaging.sharedInstance().isDisplayEnable = enabled
  }

  /// Returns the result of isDisplayEnable.
  /// - Parameter completion: CompletionHandler instance that will be fetched via AGCAppMessaging.swift.
  func isDisplayEnable(completion: @escaping CompletionHandler){
    completion(AGCAppMessaging.sharedInstance().isDisplayEnable, nil)
  }

  /// Sets whether to allow data synchronization from the AppGallery Connect server.
  /// - Parameter completion: CompletionHandler instance that will be fetched via AGCAppMessaging.swift.
  func isFetchMessageEnable(completion: @escaping CompletionHandler){
    completion(AGCAppMessaging.sharedInstance().isFetchMessageEnable, nil)
  }

  /// This feauture is not available by calling this method.
  /// Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.
  /// - Parameter completion: CompletionHandler instance that will be fetched via AGCAppMessaging.swift.
  func setForceFetch(completion: @escaping CompletionHandler){
    completion("Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.", nil)
  }

  /// This feauture is not available by calling this method.
  /// Refer to your project/AppDelegate file to remove custom view implementations.
  /// - Parameter completion: CompletionHandler instance that will be fetched via AGCAppMessaging.swift.
  func removeCustomView(completion: @escaping CompletionHandler){
    completion("Refer to your project/Info.plist file to whether remove or set false AGCCustomView parameter.", nil)
  }

  /// Sets display location of appMessage whether at the bottom or the center.
  /// - Parameter location: Location instance that will be get via Constants.
  func setDisplayLocation(location: Int){
    if(location == 0){
      AGCAppMessagingDefaultDisplay.sharedInstance().defaultLocation = AGCAppMessagingDefaultDisplayLocation.bottom
    }else{
      AGCAppMessagingDefaultDisplay.sharedInstance().defaultLocation = AGCAppMessagingDefaultDisplayLocation.center
    }
  }

  /// Triggers message display.
  /// - Parameter eventId: String instance that refers to eventId.
  func trigger(eventId: String){
    AGCAppMessaging.sharedInstance().triggerEvent(eventId)
  }

  /// When using custom app message layout, handle custom app message click events like below.
  /// - Parameters:
  ///   - params: gets eventType (ON_MESSAGE_DISPLAY, ON_MESSAGE_CLICK, ON_MESSAGE_DISMISS, ON_MESSAGE_ERROR) and dismissType param(DISMISS_TYPE_CLICK, DISMISS_TYPE_CLICK_OUTSIDE, DISMISS_TYPE_AUTO, DISMISS_TYPE_SWIPE) when using ON_MESSAGE_DISMISS.
  ///   - resolve: In the success scenario, Boolean instance, true, is returned.
  ///   - reject: Exception instance is returned in the failure scenario.
  func handleCustomViewMessageEvent(_ params: NSDictionary) -> Void {
    if let params = (params as? [String : Any]){
      guard let eventType = params["eventType"] as? String else {
        fatalError("Failed to get eventType parameter.")
      }
      guard appMessagingDisplay != nil else {
        return
      }
      switch eventType {
      case RNAGCAppMessagingConstants.onMessageClick:
        AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onClick: appMessagingDisplay!, button: AGCAppMessagingActionButton())
        break
      case RNAGCAppMessagingConstants.onMessageDismiss:
        guard let dismissType = params["dismissType"] as? String else {
          return
        }
        var mDismissType: AGCAppMessagingDismissType = .auto
        switch dismissType {
        case RNAGCAppMessagingConstants.dismissTypeClick:
          mDismissType = .click
          break
        case RNAGCAppMessagingConstants.dismissTypeClickOutside:
          mDismissType = .clickOutside
          break
        case RNAGCAppMessagingConstants.dismissTypeSwipe:
          mDismissType = .swipe
          break
        default:
          break
        }
        AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onDismiss: appMessagingDisplay!, dismissType: mDismissType)
        break
      case RNAGCAppMessagingConstants.onMessageDisplay:
        AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onDisplay: appMessagingDisplay!)
        break
      case RNAGCAppMessagingConstants.onMessageError:
        AGCAppMessaging.sharedInstance().delegate?.appMessaging?(onError: appMessagingDisplay!)
        break
      default:
        break
      }
    }
  }
}

