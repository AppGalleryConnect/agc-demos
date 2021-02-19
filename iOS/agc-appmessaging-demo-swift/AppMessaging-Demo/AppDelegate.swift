//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

import UIKit
import AGConnectCore
import AGConnectAppMessaging

@main
class AppDelegate: UIResponder, UIApplicationDelegate, AGCAppMessagingDelegate {



    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        AGCInstance.startUp()
        AGCAppMessaging.sharedInstance().delegate = self
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    // MARK: AGCAppMessagingDelegate

    func appMessaging(onDisplay message: AGCAppMessagingDisplayMessage) {
        print("on display message : \(message.messageId)")
    }
    
    func appMessaging(onClick message: AGCAppMessagingDisplayMessage, button: AGCAppMessagingActionButton) {
        print("on click message : \(message.messageId)")
    }
    
    func appMessaging(onDismiss message: AGCAppMessagingDisplayMessage, dismissType: AGCAppMessagingDismissType) {
        print("on dismiss message : \(message.messageId)")
    }
    
    func appMessaging(onError message: AGCAppMessagingDisplayMessage) {
        print("on error message : \(message.messageId)")
    }

}

