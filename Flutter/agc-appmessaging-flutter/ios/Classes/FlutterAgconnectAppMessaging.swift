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

class FlutterAgconnectAppMessaging: NSObject{
    
    private lazy var viewModel: AgconnectAppMessagingViewModel = AgconnectAppMessagingViewModel()
    private lazy var customEventHandlerAppMessagingCustomEventHandler = AppMessagingCustomEventHandler()
    
    /// Triggers message display.
    /// - Parameters:
    ///   - params: String instance that refers to eventId.
    @objc func trigger(_ params: NSDictionary, resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.trigger(params, result: resolve)
        }
    }
    
    /// Returns the result of isDisplayEnable.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    @objc func isDisplayEnable(resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.isDisplayEnable(result: resolve)
        }
    }
    
    /// Sets displayEnable in AGCAppMessaging.
    /// - Parameters:
    ///   - params: Boolean value that refers to whether enable or disable display feature.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    @objc func setDisplayEnable(_ params: Bool, resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.setDisplayEnable(params, result: resolve)
        }
    }
    
    /// Data synchronization from the AppGallery Connect server.
    /// - Parameters:
    ///   - params: Boolean value that refers to whether enable or disable fetch message feature.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    @objc func setFetchMessageEnable(_ params: Bool, resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.setFetchMessageEnable(params, result: resolve)
        }
    }
    
    ///  Sets whether to allow data synchronization from the AppGallery Connect server.
    /// - Parameters:
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    @objc func isFetchMessageEnable(resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.isFetchMessageEnable(result: resolve)
        }
    }
    
    
    /// Sets display location of appMessage whether at the bottom or the center.
    /// - Parameters:
    ///   - params: Location instance that will be get via Constants.
    @objc func setDisplayLocation(_ params: Int, resolve: @escaping FlutterResult) -> Void {
        
        Log.debug(#function) {
            viewModel.delegate = self
            viewModel.setDisplayLocation(params, result: resolve)
        }
    }
    /// When using custom app message layout, handle custom app message click events like below.
    /// - Parameters:
    ///   - params: gets eventType (ON_MESSAGE_DISPLAY, ON_MESSAGE_CLICK, ON_MESSAGE_DISMISS, ON_MESSAGE_ERROR) and dismissType param(DISMISS_TYPE_CLICK, DISMISS_TYPE_CLICK_OUTSIDE, DISMISS_TYPE_AUTO, DISMISS_TYPE_SWIPE) when using ON_MESSAGE_DISMISS.
    ///   - resolve: In the success scenario, Boolean instance, true, is returned.
    @objc func handleCustomViewMessageEvent(_ params: NSDictionary, resolve: @escaping FlutterResult) -> Void {
        Log.debug(#function) {
            customEventHandlerAppMessagingCustomEventHandler.handleCustomViewMessageEvent(params: params, result: resolve)
        }
    }
}
extension FlutterAgconnectAppMessaging: ViewModelDelegate {
    func post(result: (Any?) -> Void) {
        result("Success")
    }
    
    func postBool(data: Bool, result: (Any?) -> Void) {
        result(data)
    }
    
    func postError(error: Error?, result: FlutterResult) {
        result(FlutterError(code: "", message: error?.localizedDescription, details: ""))
    }
    
    func postMap(data: [String : Any], result: FlutterResult) {
        result(data)
    }
}
