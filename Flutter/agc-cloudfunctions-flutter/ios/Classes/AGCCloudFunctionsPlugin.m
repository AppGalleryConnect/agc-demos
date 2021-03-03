#import "AGCCloudFunctionsPlugin.h"
#if __has_include(<agconnect_cloudfunctions/agconnect_cloudfunctions-Swift.h>)
#import <agconnect_cloudfunctions/agconnect_cloudfunctions-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "agconnect_cloudfunctions-Swift.h"
#endif

@implementation AGCCloudFunctionsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [AGCCloudFunctionsPluginCallHandler registerWithRegistrar:registrar];
}
@end
