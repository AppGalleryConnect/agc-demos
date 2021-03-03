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

//Handling blueprint and declerations.
protocol AGCCloudFunctionsHanding {
    func handle(resolve: @escaping FlutterResult, _ isSuccess: Bool?, _ error: Error?)
    func handle<T>(resolve: FlutterResult, _ instance: T)
    func fail(with error: Error, resolve: @escaping FlutterResult)
}

extension AGCCloudFunctionsHanding {
    func handle(resolve: @escaping FlutterResult, _ isSuccess: Bool?=true, _ error: Error?=nil) {
        Result.shared.resolve(resolve: resolve, isSuccess, error: error)
    }
    
    func handle<T>(resolve: FlutterResult, _ instance: T) {
        Result.shared.resolve(resolve: resolve, instance: instance)
    }
    
    func fail(with error: Error, resolve: @escaping FlutterResult){
        Result.shared.resolve(resolve: resolve, false, error: error)
    }
}

class Result {
    static let shared = Result()
    
    private init() { }
    
    func resolve<T>(resolve: FlutterResult, instance: T?) {
        resolve(instance)
    }
    
    func resolve(resolve: @escaping FlutterResult, _ isSuccess: Bool?, error: Error?) {
        guard let success = isSuccess else { return }
        if success {
            resolve(success)
        }else {
            resolve(FlutterError(code: "-1", message: error.debugDescription , details: error?.localizedDescription))
        }
    }
}
