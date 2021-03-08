/*
 Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License")
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 https://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import Flutter
import UIKit
import AGConnectCore

public class AGCCloudFunctionsPluginCallHandler: NSObject, FlutterPlugin {
    let agcCloudFunctions: AGCCloudFunctionsFlutter = AGCCloudFunctionsFlutter.init()
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "com.huawei.agc.flutter.cloudfunctions/MethodChannel", binaryMessenger: registrar.messenger())
        let instance = AGCCloudFunctionsPluginCallHandler()
        registrar.addMethodCallDelegate(instance, channel: channel)
        registrar.addApplicationDelegate(instance)
    }
    
    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable : Any] = [:]) -> Bool {
        AGCInstance.startUp()
        return true
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any] else {
            return
        }
        let method = Methods.init()
        
        switch call.method {
        case method.CALL_FUNCTION:
            guard let trigger = args["httpTriggerURI"] as? String else { return }
            agcCloudFunctions.call(trigger, options: args, resolver: result)
            
        default:
            result(FlutterError(code: "platformError", message: "Not supported on iOS platform", details: ""));
        }
    }
    
    struct Methods {
        let CALL_FUNCTION = "callFunction"
    }
}
