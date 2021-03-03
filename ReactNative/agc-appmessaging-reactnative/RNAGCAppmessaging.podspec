ENV['SWIFT_VERSION'] = '5'
require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNAGCAppmessaging"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  RNAGCAppmessaging
                   DESC
  s.homepage     = "https://developer.huawei.com/consumer/en/agconnect"
  # brief license entry:
  s.license      = package["license"]

  s.platforms    = { :ios => "9.0" }
  s.authors      = { "Huawei AGConnect" => "agconnect@huawei.com" }
  s.source       = { :git => "", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.static_framework = true
  s.dependency "React"
  # Huawei AGConnectAppMessaging Kit SDK
  s.dependency "AGConnectAppMessaging", "1.2.1.300"

end
