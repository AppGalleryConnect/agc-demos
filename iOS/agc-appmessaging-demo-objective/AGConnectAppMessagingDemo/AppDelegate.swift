//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

import UIKit
import AGConnectCore
import AGConnectAppMessaging

@main
class AppDelegate: UIResponder, UIApplicationDelegate , AGCAppMessagingDelegate{

    var window: UIWindow?   
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        AGCInstance.startUp()
        AGCAppMessaging.sharedInstance().delegate = self
        return true
    }

    func appMessaging(onDisplay message: AGCAppMessagingDisplayMessage) {
        
    }
    
    func appMessaging(onClick message: AGCAppMessagingDisplayMessage, button: AGCAppMessagingActionButton?) {
        
    }
    
    func appMessaging(onError message: AGCAppMessagingDisplayMessage) {
        
    }
    
    func appMessaging(onDismiss message: AGCAppMessagingDisplayMessage, dismissType: AGCAppMessagingDismissType) {
        
    }
}

