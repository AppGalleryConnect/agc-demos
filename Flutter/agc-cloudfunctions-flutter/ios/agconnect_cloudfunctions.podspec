#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint agconnect_cloudfunctions.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'agconnect_cloudfunctions'
  s.version          = '1.2.0+221'
  s.summary          = 'AppGallery Connect Cloud Functions Kit plugin for Flutter. Cloud Functions enables serverless computing.'
  s.description      = <<-DESC
AppGallery Connect Cloud Functions Kit plugin for Flutter. Cloud Functions enables serverless computing.
                       DESC
  s.homepage         = 'https://developer.huawei.com/consumer/en/agconnect/'
  s.license          =  { :type => 'Apache 2.0', :file => '../LICENSE' }
  s.author           = { 'Huawei Technologies' => 'huaweideveloper1@gmail.com' }
  s.source           = { :git => '' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.dependency 'AGConnectFunction', '1.2.1.300'
  s.platform = :ios, '9.0'
  s.static_framework = true


  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
