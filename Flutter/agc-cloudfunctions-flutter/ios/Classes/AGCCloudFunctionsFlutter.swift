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

import Foundation
import AGConnectFunction
import Flutter

@objc(AGCCloudFunctions)

/// Provides methods to initialize Cloud Functions Kit and implement analysis functions.
class AGCCloudFunctionsFlutter: NSObject, AGCCloudFunctionsHanding {
    /// All the AGCCloudFunctions API's can be reached via AGCCloudFunctionsViewModel class instance.
    private lazy var viewModel: AGCCloudFunctionsViewModel = AGCCloudFunctionsViewModel()
    
    /// Creates a function callable instance, sets its configuration and calls the cloud functions
    /// - Parameters:
    ///   - triggerIdentifier: HTTP trigger identifier of the cloud function to be called.
    ///   - options: Dictionary that contains AGCFunctionCallable instance configurations and cloud functions parameters
    ///   - resolve: In the success scenario, returns FlutterResult instance, with request's response.
    @objc func call(
        _ triggerIdentifier: String,
        options: Dictionary<String, Any>,
        resolver resolve: @escaping FlutterResult
    ) {
        AGCCloudFunctionsLog.showInPanel(message: #function, type: .call)
        let functionCallable: AGCFunctionCallable = AGCFunction.getInstance().wrap(triggerIdentifier);
        if let timeout = options["timeout"] as? Double {
            if let timeUnit = options["units"] as? Int {
                let timeoutInterval = timeUnitConverter(timeUnit: timeUnit, timeout: timeout)
                functionCallable.timeoutInterval = timeoutInterval
            }
        }
        if let params = options["functionParameters"]  {
            callFunction(functionCallable: functionCallable, param: params, resolver: resolve)
        }else {
            callFunction(functionCallable: functionCallable, resolve: resolve)
        }
    }
    
    // MARK: - Private Helper Functions
    
    /// Calls a function without input parameters.
    /// - Parameters:
    ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
    ///   - resolve: In the success scenario, returns FlutterResult instance, with request's response.
    /// - Returns: Void
    private func callFunction(
        functionCallable: AGCFunctionCallable,
        resolve: @escaping FlutterResult
    ){
        viewModel.callFunction(functionCallable: functionCallable){ [weak self] (result, error) in
            guard let strongSelf = self else {return}
            if let error = error as NSError? {
                AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
                strongSelf.fail(with: error, resolve: resolve )
            }
            if let result = result as? String {
                AGCCloudFunctionsLog.showInPanel(message: #function, type: .success)
                strongSelf.handle(resolve: resolve, result)
            }
        }
    }
    
    /// Calls a function with input parameters.
    /// - Parameters:
    ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
    ///   - param: Dictionary that contains the input parameter values of a function.
    ///   - resolve: In the success scenario, returns FlutterResult instance, with request's response.
    /// - Returns: Void
    private func callFunction(
        functionCallable: AGCFunctionCallable,
        param: Any,
        resolver resolve: @escaping FlutterResult
    ){
        viewModel.callFunction(functionCallable: functionCallable, param: param){ [weak self] (result, error) in
            guard let strongSelf = self else {return}
            if let error = error as NSError? {
                AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
                strongSelf.fail(with: error, resolve: resolve )
            }
            if let result = result as? String {
                AGCCloudFunctionsLog.showInPanel(message: #function, type: .success)
                strongSelf.handle(resolve: resolve,result )
            }
        }
    }
     /// Convert timeout value to second.
        /// - Parameters:
        ///   - timeUnit: Defining the time unit.
        ///   - timeout:  Timeout interval.
        ///   - Returns: Timeout value in seconds.
    private func timeUnitConverter(timeUnit: Int, timeout: Double) -> Double{
        switch timeUnit {
        case 0:
            return timeout/1000000000
        case 1:
            return timeout/1000000
        case 2:
            return timeout/1000
        case 4:
            return timeout*60
        case 5:
            return timeout*3600
        case 6:
            return timeout*86400
        default:
            return timeout
        }
    }
}