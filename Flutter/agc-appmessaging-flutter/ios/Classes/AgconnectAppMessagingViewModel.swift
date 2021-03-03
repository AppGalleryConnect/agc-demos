
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

public protocol ViewModelDelegate: AnyObject {
    func post(result: FlutterResult)
    func postMap(data:[String:Any], result: FlutterResult)
    func postBool(data:Bool, result: FlutterResult)
    func postError(error: Error?, result: FlutterResult)
}

/// All the AGConnectAppMessaging API's can be reached via AgcAppmessagingViewModel class instance.
public class AgconnectAppMessagingViewModel{
    var delegate: ViewModelDelegate?
    
    /// Triggers message display.
    /// - Parameter params: String instance that refers to eventId.
    func trigger(_ params: NSDictionary , result: @escaping FlutterResult){
        if let params = (params as? [String : Any]){
            let eventId = params["eventId"] as! String
            AGCAppMessaging.sharedInstance().triggerEvent(eventId)
        }
    }
    
    /// Returns the result of isDisplayEnable.
    /// - Parameter result: FlutterResult instance that will be fetched via AGCAppMessaging.swift.
    func isDisplayEnable(result: @escaping FlutterResult){
        let isEnable = AGCAppMessaging.sharedInstance().isDisplayEnable
        self.delegate?.postBool(data: isEnable, result: result)
    }
    
    /// Sets whether to allow data synchronization from the AppGallery Connect server.
    /// - Parameter result: FlutterResult instance that will be fetched via AGCAppMessaging.swift.
    func isFetchMessageEnable(result: @escaping FlutterResult){
        let isEnable = AGCAppMessaging.sharedInstance().isFetchMessageEnable
        self.delegate?.postBool(data: isEnable, result: result)
    }
    
    /// Sets displayEnable in AGCAppMessaging.
    /// - Parameter params: Boolean value that refers to whether enable or disable display feature.
    func setDisplayEnable(_ params: Bool , result: @escaping FlutterResult){
        
        AGCAppMessaging.sharedInstance().isDisplayEnable = params
        self.delegate?.post(result: result)
    }
    
    /// Data synchronization from the AppGallery Connect server.
    /// - Parameter params: Boolean value that refers to whether enable or disable fetch message feature.
    func setFetchMessageEnable(_ params: Bool , result: @escaping FlutterResult){
        
        AGCAppMessaging.sharedInstance().isFetchMessageEnable = params
        self.delegate?.post(result: result)
    }
    
    /// This feauture is not available by calling this method.
    /// Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.
    /// - Parameter result: FlutterResult instance that will be fetched via AGCAppMessaging.swift.
    func setForceFetch(result: @escaping FlutterResult){
        result("Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.")
    }
    
    /// This feauture is not available by calling this method.
    /// Refer to your project/AppDelegate file to add custom view. **displayComponent parameter must be set to AppDelegate itself for using custom view in app messaging.
    /// - Parameter result: FlutterResult instance that will be fetched via AGCAppMessaging.swift.
    func addCustomView(result: @escaping FlutterResult){
        result("Refer to your project/AppDelegate file to add custom view. **displayComponent parameter must be set to AppDelegate itself for using custom view in app messaging.")
    }
    
    /// This feauture is not available by calling this method.
    /// Refer to your project/AppDelegate file to remove custom view implementations.
    /// - Parameter result: FlutterResult instance that will be fetched via AGCAppMessaging.swift.
    func removeCustomView(result: @escaping FlutterResult){
        result("Refer to your project/AppDelegate file to remove custom view implementations.")
    }
    
    /// Sets display location of appMessage whether at the bottom or the center.
    /// - Parameter params: Location instance that will be get via Constants.
    func setDisplayLocation(_ params: Int , result: @escaping FlutterResult){
        switch params {
        case 0:
            AGCAppMessagingDefaultDisplay.sharedInstance().defaultLocation = AGCAppMessagingDefaultDisplayLocation.bottom
        case 1:
            AGCAppMessagingDefaultDisplay.sharedInstance().defaultLocation = AGCAppMessagingDefaultDisplayLocation.center
        default:
            result(FlutterError(code: "", message: "Invalid argument expected Error", details: "This function accept only AppMessagingConstant type"))
        }
        self.delegate?.post(result: result)
    }
}
