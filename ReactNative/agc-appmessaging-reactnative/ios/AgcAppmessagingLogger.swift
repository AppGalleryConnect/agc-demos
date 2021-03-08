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

import UIKit
import SystemConfiguration
import CoreTelephony
import Foundation

let AgcAppmessagingLog = AgcAppmessagingLogger(version: "1.2.1.300", service: "ReactNativeAGCAppMessaging")

/// Custom AgcAppmessagingLogger Class to listen api log events from RN Side.
final class AgcAppmessagingLogger {

  /// Info Types to declare success and failure scenarios.
  public enum InfoType{
    case call, success, fail
  }

  private var prefix: String = GenericFunctions.className(AgcAppmessagingLogger.self)
  private var version: String?
  private var service: String?

  /// Version and Service names may be added to give more detail in panel.
  /// - Parameters:
  ///   - version: Refers to version number of the framework.
  ///   - service: Refers to service declaration.
  public init(version: String? = nil, service: String? = nil) {
    self.version = version
    self.service = service
  }

  /// This function can be used during synchronized events. It gets the method name, calculates start end end times and reports to console as single events.
  /// - Parameters:
  ///   - file: Refers to file name that the function is in.
  ///   - line: Refers to line number.
  ///   - name: Refers to name of the function.
  ///   - eventType: Refers to InfoType instance.
  ///   - message: Refers to message that can be added in the panel.
  ///   - block: Refers to function that will be used.
  /// - Returns: Void
  func debug(file: String = #file, line: Int = #line, name: String = #function, _ message: String, block:()->()) {
      showInPanel(file:file, line:line, name:name, message: message, type: .call)
      block()
      showInPanel(file:file, line:line, name:name, message: message, type: .success)
  }

  // MARK: - Private Functions

  /// Shows infos in panel
  func showInPanel(callTime: Double? = nil,
                           file: String = #file,
                           line: Int = #line,
                           name: String = #function,
                           message: String?,
                           timeElapsed: Double? = nil,
                           type: AgcAppmessagingLogger.InfoType) {
    switch type {
    case .call:
      printCall(name)
      return
    case .success:
      printStatus(name, true)
    case .fail:
      printStatus(name, false)
    }
  }

  /// Prints call messages among with the function names.
  private func printCall(_ message: String){
    print("call \(message)")
  }

  /// Print status messages.
  private func printStatus(_ name: String, _ status: Bool){
    let statusMsg = status ? "success" : "failure"
    print("\(name) \(statusMsg)")
  }

  /// Returns file name of the function is in.
  private func getFileName(_ filePath: String) -> String {
    let parser = filePath.split(separator:"/")
    if let fileName = String(parser.last ?? "").split(separator: ".").first {
      return String(fileName)
    }
    return ""
  }
}

// MARK: - Helper Functions

struct GenericFunctions {
  static func className<T>(_ name: T) -> String {
    return "\(name)"
  }
}

extension Bundle {

  public var packageName: String {
    if let result = Bundle.main.bundleIdentifier {
      return result
    } else {
      return "⚠️"
    }
  }

  public var appVersionShort: String {
    if let result = infoDictionary?["CFBundleShortVersionString"] as? String {
      return result
    } else {
      return "⚠️"
    }
  }
  public var appVersionLong: String {
    if let result = infoDictionary?["CFBundleVersion"] as? String {
      return result
    } else {
      return "⚠️"
    }
  }
  public var appName: String {
    if let result = infoDictionary?["CFBundleName"] as? String {
      return result
    } else {
      return "⚠️"
    }
  }
}
