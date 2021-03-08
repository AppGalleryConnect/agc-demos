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
import AuthenticationServices
import CommonCrypto
import AGConnectAuth

class AppleProvider: BaseProvider, ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding {
    var currentRequestNonce : String?
    var signViewController : UIViewController?
    
    static let sharedInstance = AppleProvider()

    private override init() {}
    
    override func fetchCredential(controller: UIViewController, completion: @escaping CredentialBlock) {
        signViewController = controller
        credentialBlock = completion
        if #available(iOS 13.0, *) {
            let provider = ASAuthorizationAppleIDProvider()
            let request = provider.createRequest()
            request.requestedScopes = [ASAuthorization.Scope.fullName, ASAuthorization.Scope.email]
            currentRequestNonce = randomString()
            request.nonce = sha256Hashing(string: currentRequestNonce!)
            let controller = ASAuthorizationController.init(authorizationRequests: [request])
            controller.delegate = self
            controller.presentationContextProvider = self
            controller.performRequests()
        }
    }

    // MARK: ASAuthorizationControllerDelegate
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        if let appleCredential = authorization.credential as? ASAuthorizationAppleIDCredential {
            let jwt = String(data: appleCredential.identityToken!, encoding: String.Encoding.utf8)
            let credential = AGCAppleIDAuthProvider.credential(withIdentityToken: appleCredential.identityToken!, nonce: currentRequestNonce!)
            if let credentialBlock = credentialBlock {
                credentialBlock(credential)
            }
        }
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        if let credentialBlock = credentialBlock {
            credentialBlock(nil)
        }
    }

    // MARK: ASAuthorizationControllerPresentationContextProviding
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return (signViewController?.view.window)!
    }

    // MARK: private
    func randomString() -> String {
        let length = 32
        let letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
        return String((0..<length).map{ _ in letters.randomElement()! })
    }
    
    func sha256Hashing(string:String) -> String {
        let data : Data = string.data(using: String.Encoding.utf8)!
        var hash = [UInt8](repeating: 0,  count: Int(CC_SHA256_DIGEST_LENGTH))
        data.withUnsafeBytes {
            _ = CC_SHA256($0, CC_LONG(data.count), &hash)
        }
        
        var result = ""
        for e in hash {
            result += String(format: "%02x", e)
        }
        return result
    }

}
