//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

import UIKit
import AGConnectCore
import AGConnectAppLinking

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        AGCInstance.startUp()
        AGCAppLinking.instance().handle { (link, error) in
            let deepLink = link?.deepLink
            //TODO: 增加deeplink地址跳转
        }
        return true
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        let isAppLinking = AGCAppLinking.instance().openDeepLinkURL(url)
        return isAppLinking
    }
    
    func application(_ application: UIApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        let isAppLinking = AGCAppLinking.instance().continueUserActivity(userActivity)
        return isAppLinking
    }
}

