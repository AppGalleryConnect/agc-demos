/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

///Promise Handling blueprint and declerations.
protocol AgcAppmessagingHandling {
  func handle(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?, _ message: String?)
  func handle<T>(resolve: RCTPromiseResolveBlock, _ instance: T)
  func fail(with error: Error?, reject: @escaping RCTPromiseRejectBlock)
}

extension AgcAppmessagingHandling {
  func handle(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?=true, _ message: String?=nil) {
    AgcAppmessagingPromise.shared.resolve(resolve: resolve, isSuccess, message)
  }

  func handle<T>(resolve: RCTPromiseResolveBlock, _ instance: T) {
    AgcAppmessagingPromise.shared.resolve(resolve: resolve, instance: instance)
  }

  func fail(with error: Error?, reject: @escaping RCTPromiseRejectBlock){
    AgcAppmessagingPromise.shared.reject(reject: reject, error: error)
  }
}

class AgcAppmessagingPromise {
  static let shared = AgcAppmessagingPromise()

  private init() { }

  func resolve<T>(resolve: RCTPromiseResolveBlock, instance: T?) {
    resolve(instance)
  }

  func resolve(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?, _ message: String?) {
    var params:[String: Any] = ["isSuccess": isSuccess ?? true]
    if let message = message{
      params["message"] = message
    }
    resolve(params)
  }

  func reject(reject: RCTPromiseRejectBlock, error: Error?) {
    if let error = error as NSError?{
      reject("\(error.code)", error.debugDescription, error)
      return
    }
    reject(Bundle.main.packageName, Bundle.main.appVersionShort, error)
  }

}


