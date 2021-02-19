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

class GoogleProvider: BaseProvider, GIDSignInDelegate {
    
    static let sharedInstance = GoogleProvider()
    
    private override init() {}
    
    override func startUp() {
        GIDSignIn.sharedInstance()?.clientID = "GOOGLE_CLIENT_ID"
        GIDSignIn.sharedInstance()?.delegate = self
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return GIDSignIn.sharedInstance()?.handle(url) ?? false
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        GIDSignIn.sharedInstance()?.presentingViewController = controller
        GIDSignIn.sharedInstance()?.signIn()
    }

    // GIDSignInDelegate
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if error != nil {
            print("error")
            return
        }
        let credential = AGCGoogleAuthProvider.credential(withToken: user.authentication.idToken)
        if let credentialBlock = credentialBlock {
            credentialBlock(credential)
        }
    }
    
}
