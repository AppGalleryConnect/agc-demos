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

class QQProvider: BaseProvider, TencentSessionDelegate {
    
    static let sharedInstance = QQProvider()
    
    var qq : TencentOAuth? = nil
    
    
    private override init() {}
    
    override func startUp() {
        qq = TencentOAuth(appId: "QQ_APP_ID", andDelegate: self)
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return TencentOAuth.handleOpen(url)
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        qq!.authorize(["all"])
    }

    // TencentSessionDelegate
    func tencentDidLogin() {
        let credential = AGCQQAuthProvider.credential(withToken: qq!.accessToken, openId: qq!.openId)
        if let credentialBlock = credentialBlock {
            credentialBlock(credential)
        }
    }
    
    func tencentDidNotLogin(_ cancelled: Bool) {
        
    }
    
    func tencentDidNotNetWork() {
        
    }

}
