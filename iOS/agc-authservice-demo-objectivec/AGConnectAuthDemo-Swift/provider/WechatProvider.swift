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
import AGConnectCredential

class WechatProvider: BaseProvider, WXApiDelegate{
    
    static let sharedInstance = WechatProvider()
    
    private override init() {}
    
    override func startUp() {
        WXApi.registerApp("WEIXIN_APP_ID", universalLink: "WEIXIN_UNIVERSAL_LINK")
    }

    func handleOpenUniversalLink(userActivity:NSUserActivity) -> Bool {
        return WXApi.handleOpenUniversalLink(userActivity, delegate: self)
    }

    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return WXApi.handleOpen(url, delegate: self)
    }

    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        credentialBlock = completion
        let req:SendAuthReq = SendAuthReq()
        req.scope = "snsapi_userinfo"
        req.state = "none"
        WXApi.sendAuthReq(req, viewController: controller, delegate: self, completion: nil)
    }

    // WXApiDelegate
    func onReq(_ req: BaseReq) {
        
    }
    
    func onResp(_ resp: BaseResp) {
        if let authResp = resp as? SendAuthResp {
            let urlString = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxee9b4cbba326bd96&secret=abc2ea29ec6a0d0f4f48e6d22f5e7d42&code=\(authResp.code ?? "")&grant_type=authorization_code"
            let request = URLRequest(url: URL(string: urlString)!)
            AGCBackend.sharedInstance().dataTask(with: request).onSuccess { (result) in
                let body = try? JSONSerialization.jsonObject(with: result! as Data, options: JSONSerialization.ReadingOptions.mutableContainers)
                if let dict = body as? Dictionary<String, String> {
                    let accessToken = dict["access_token"]
                    let openid = dict["openid"]
                    if accessToken == nil || openid == nil {
                        print("failed")
                    }
                    let credential = AGCWeiXinAuthProvider.credential(withToken: accessToken!, openId: openid!)
                    if let credentialBlock = self.credentialBlock {
                        credentialBlock(credential)
                    }
                }else{
                    print("error")
                }
            }.onFailure { (error) in
                if let credentialBlock = self.credentialBlock {
                    credentialBlock(nil)
                }
            }
        }
    }
}
