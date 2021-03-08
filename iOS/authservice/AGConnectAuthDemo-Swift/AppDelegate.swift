/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import UIKit
import AGConnectCore

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // initialize agconnet sdk
        AGCInstance.startUp()
        
        // initialize social accounts sdk
        WechatProvider.sharedInstance.startUp()
        WeiboProvider.sharedInstance.startUp()
        QQProvider.sharedInstance.startUp()
        FacebookProvider.sharedInstance.startUp()
        GoogleProvider.sharedInstance.startUp()
        TwitterProvider.sharedInstance.startUp()
        
        return true
    }
    
    func application(_ application: UIApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        // handle url for social accounts auth sdk
        return WechatProvider.sharedInstance.handleOpenUniversalLink(userActivity: userActivity)
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        // handle url for social accounts auth sdk
        let isWeChatSuccess = WechatProvider.sharedInstance.application(app, open: url, options: options)
        let isWeiboSuccess = WeiboProvider.sharedInstance.application(app, open: url, options: options)
        let isQQSuccess = QQProvider.sharedInstance.application(app, open: url, options: options)
        let isGoogleSuccess = GoogleProvider.sharedInstance.application(app, open: url, options: options)
        let isTwitterSuccess = TwitterProvider.sharedInstance.application(app, open: url, options: options)
        let isFacebookSuccess = FacebookProvider.sharedInstance.application(app, open: url, options: options)
        return isWeiboSuccess || isQQSuccess || isWeChatSuccess || isGoogleSuccess || isTwitterSuccess || isFacebookSuccess
    }
}

