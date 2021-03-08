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

class TwitterProvider: BaseProvider {
    
    static let sharedInstance = TwitterProvider()
    
    private override init() {}
    
    override func startUp() {
        TWTRTwitter.sharedInstance().start(withConsumerKey: "TWITTER_CONSUMER_KEY", consumerSecret: "TWITTER_CONSUMER_SECRET")
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return TWTRTwitter.sharedInstance().application(app, open: url, options: options)
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        TWTRTwitter.sharedInstance().logIn { (session, error) in
            if let session = session {
                let credential = AGCTwitterAuthProvider.credential(withToken: session.authToken, secret: session.authTokenSecret)
                if let credentialBlock = self.credentialBlock {
                    credentialBlock(credential)
                }
            }else{
                print("error")
                if let credentialBlock = self.credentialBlock {
                    credentialBlock(nil)
                }
            }
        }
    }
}
