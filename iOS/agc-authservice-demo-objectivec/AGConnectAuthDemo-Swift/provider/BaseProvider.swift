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

typealias  CredentialBlock = (_ credential: AGCAuthCredential?) -> Void

class BaseProvider: NSObject {
    
    var credentialBlock:CredentialBlock?
    
    func startUp() {
        
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return false
    }
    
    func fetchCredential(controller:UIViewController, completion: @escaping CredentialBlock) {
        
    }

    static func sendVerifyCode(countryCode:String, phoneNumber:String, action:AGCVerifyCodeAction) {
        let setting = AGCVerifyCodeSettings.init(action: action, locale: nil, sendInterval:30)
        AGCPhoneAuthProvider.requestVerifyCode(withCountryCode: countryCode, phoneNumber: phoneNumber, settings: setting).onSuccess { (result) in
            print("send verification code success")
        }.onFailure { (error) in
            print("send verification code failed")
        } 
    }
    
    static func sendVerifyCode(email:String, action:AGCVerifyCodeAction) {
        let setting = AGCVerifyCodeSettings.init(action: action, locale: nil, sendInterval:30)
        AGCEmailAuthProvider.requestVerifyCode(withEmail: email, settings: setting).onSuccess { (result) in
            print("send verification code success")
        }.onFailure { (error) in
            print("send verification code failed")
        }
    }
}
