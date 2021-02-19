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

class WeiboProvider: BaseProvider, WeiboSDKDelegate {
    
    static let sharedInstance = WeiboProvider()
    
    private override init() {}
    
    override func startUp() {
        WeiboSDK.registerApp("WEIBO_APP_KEY")
        WeiboSDK.enableDebugMode(true)
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return WeiboSDK.handleOpen(url, delegate: self)
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        let req:WBAuthorizeRequest = WBAuthorizeRequest()
        req.redirectURI = "https://api.weibo.com/oauth2/default.html"
        req.scope = "all"
        WeiboSDK.send(req)
    }

    // WeiboSDKDelegate
    func didReceiveWeiboRequest(_ request: WBBaseRequest!) {
        
    }
    
    func didReceiveWeiboResponse(_ response: WBBaseResponse!) {
        if let response = response as? WBAuthorizeResponse {
            let credential = AGCWeiboAuthProvider.credential(withToken: response.accessToken, uid: response.userID)
            if let credentialBlock = credentialBlock {
                credentialBlock(credential)
            }
        }
    }

}
