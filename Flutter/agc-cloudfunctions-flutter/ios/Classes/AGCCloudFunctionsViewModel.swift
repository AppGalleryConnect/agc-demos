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

/// All the AGCCloudFunctions API's can be reached via AGCCloudFunctionsViewModel class instance.
public class AGCCloudFunctionsViewModel {
    
    /// **CompletionHandler** is a typealias that provides result and error when the request is completed.
    /// - Parameters:
    ///   - result: Any Object that will be returned when the result comes.
    ///   - error: NSError that will be returned when there is an error.
    public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void
    
    /// Calls a function without input parameters.
    ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
    /// - Parameter completion: Provides result and error when the request is completed.
    func callFunction(functionCallable: AGCFunctionCallable, completion: @escaping CompletionHandler){
        functionCallable.call().onComplete(callback: { task in
            if task.isSuccessful {
                completion(task.result, nil)
                return
            }else {
                completion(nil,task.error)
                return
            }
        })
    }
    
    /// Calls a function with input parameters.
    /// - Parameters:
    ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
    ///   - param: Dictionary that contains the input parameter values of a function.
    ///   - completion: Provides result and error when the request is completed.
    func callFunction(functionCallable: AGCFunctionCallable, param: Any, completion: @escaping CompletionHandler){
        functionCallable.call(with: param).onComplete(callback: { task in
            if task.isSuccessful {
                completion(task.result?.value(),nil)
                return
            }else {
                completion(nil,task.error)
                return
            }
        })
    }
    
}
