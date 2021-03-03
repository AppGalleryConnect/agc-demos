#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint agconnect_appmessaging.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'agconnect_appmessaging'
  s.version          = '1.2.0+221'
  s.summary          = 'HUAWEI AGC App Messaging Kit plugin for Flutter.'
  s.description      = <<-DESC
  AGC App Messaging Kit plugin for Flutter. You can use App Messaging of AppGallery Connect
    to send relevant messages to target users.
                       DESC
  s.homepage         = 'https://developer.huawei.com/consumer/en/agconnect/'
  s.license          =  { :type => 'Apache 2.0', :file => '../LICENSE' }
  s.author           = { 'Huawei AGConnect' => 'agconnect@huawei.com' }
  s.source           = { :git => '' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.dependency 'AGConnectAppMessaging'
  s.platform = :ios, '9.0'
  s.static_framework = true

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }

end
