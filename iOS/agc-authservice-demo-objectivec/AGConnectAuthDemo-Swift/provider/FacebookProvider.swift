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
import AGConnectAuth

class FacebookProvider: BaseProvider {
    
    static let sharedInstance = FacebookProvider()
    
    let manager : LoginManager
    
    
    private override init() {
        manager = LoginManager()
    }
    
    func startUp(_ app:UIApplication, options:[UIApplication.LaunchOptionsKey : Any]) {
        ApplicationDelegate.shared.application(app, didFinishLaunchingWithOptions: options)
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return ApplicationDelegate.shared.application(app, open: url, sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String, annotation: options[UIApplication.OpenURLOptionsKey.annotation])
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        manager.logIn(permissions: ["public_profile","email"], from: controller) { (result, error) in
            if error==nil {
                let credential = AGCFacebookAuthProvider.credential(withToken: result?.token?.tokenString ?? "")
                if let credentialBlock = self.credentialBlock {
                    credentialBlock(credential)
                }
            }
        }
    }

}
