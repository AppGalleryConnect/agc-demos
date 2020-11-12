//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "AppleProvider.h"
#import <AuthenticationServices/AuthenticationServices.h>
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>

@interface AppleProvider () <ASAuthorizationControllerDelegate,ASAuthorizationControllerPresentationContextProviding>

@property(nonatomic,strong)NSString *currentRequestNonce;

@end

@implementation AppleProvider

+ (instancetype)sharedInstance {
    static AppleProvider * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)loginWithViewController:(id<SignInDelegate>)delegate {
    self.signInDelegate = delegate;
    if (@available(iOS 13.0, *)) {
        ASAuthorizationAppleIDProvider *provider  = [[ASAuthorizationAppleIDProvider alloc] init];
        ASAuthorizationAppleIDRequest *request = [provider createRequest];
        request.requestedScopes = @[ASAuthorizationScopeFullName,ASAuthorizationScopeEmail];
        self.currentRequestNonce = [self randomString];
        request.nonce = [self stringBySha256HashingString:self.currentRequestNonce];
        ASAuthorizationController *controller = [[ASAuthorizationController alloc] initWithAuthorizationRequests:@[request]];
        controller.delegate = self;
        controller.presentationContextProvider = self;
        [controller performRequests];
    }
}

- (void)linkWithViewController:(UIViewController *)viewController {
    self.signInDelegate = viewController;
    
    self.isLink = YES;
    if (@available(iOS 13.0, *)) {
        ASAuthorizationAppleIDProvider *provider  = [[ASAuthorizationAppleIDProvider alloc] init];
        ASAuthorizationAppleIDRequest *request = [provider createRequest];
        request.requestedScopes = @[ASAuthorizationScopeFullName,ASAuthorizationScopeEmail];
        self.currentRequestNonce = [self randomString];
        request.nonce = [self stringBySha256HashingString:self.currentRequestNonce];
        ASAuthorizationController *controller = [[ASAuthorizationController alloc] initWithAuthorizationRequests:@[request]];
        controller.delegate = self;
        controller.presentationContextProvider = self;
        [controller performRequests];
    }
}

- (void)unlink {
    [[[[[AGCAuth getInstance] currentUser] unlink:AGCAuthProviderTypeApple] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
                [ToastUtil showToast:@"unlink success"];
            }] addOnFailureCallback:^(NSError * _Nonnull error) {
                [ToastUtil showToast:@"unlink failed"];
            }];
}

#pragma mark - ASAuthorizationControllerDelegate

- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithAuthorization:(ASAuthorization *)authorization  API_AVAILABLE(ios(13.0)){
    if ([authorization.credential isKindOfClass:ASAuthorizationAppleIDCredential.class]) {
        ASAuthorizationAppleIDCredential *appleCredential = (ASAuthorizationAppleIDCredential *)authorization.credential;
        NSString *jwt = [[NSString alloc] initWithData:appleCredential.identityToken encoding:NSUTF8StringEncoding];
        NSLog(@"%@",jwt);
        AGCAuthCredential *credential = [AGCAppleIDAuthProvider credentialWithIdentityToken:appleCredential.identityToken nonce:self.currentRequestNonce];
        if (self.isLink) {
            [self linkWithCredential:credential];
            self.isLink = NO;
        }else {
            [self signInWithCredential:credential];
        }
        
    } else if ([authorization.credential isKindOfClass:ASPasswordCredential.class]) {
        
    }
}

- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithError:(NSError *)error  API_AVAILABLE(ios(13.0)){

}

#pragma mark - ASAuthorizationControllerPresentationContextProviding
- (ASPresentationAnchor)presentationAnchorForAuthorizationController:(ASAuthorizationController *)controller  API_AVAILABLE(ios(13.0)){
    return ((UIViewController *)self.signInDelegate).view.window;
}

#pragma mark - private

- (NSString *)randomString {
  NSInteger length = 32;
  NSAssert(length > 0, @"Expected nonce to have positive length");
  NSString *characterSet = @"0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._";
  NSMutableString *result = [NSMutableString string];
  NSInteger remainingLength = length;

  while (remainingLength > 0) {
    NSMutableArray *randoms = [NSMutableArray arrayWithCapacity:16];
    for (NSInteger i = 0; i < 16; i++) {
      uint8_t random = 0;
      int errorCode = SecRandomCopyBytes(kSecRandomDefault, 1, &random);
      NSAssert(errorCode == errSecSuccess, @"Unable to generate nonce: OSStatus %i", errorCode);

      [randoms addObject:@(random)];
    }

    for (NSNumber *random in randoms) {
      if (remainingLength == 0) {
        break;
      }

      if (random.unsignedIntValue < characterSet.length) {
        unichar character = [characterSet characterAtIndex:random.unsignedIntValue];
        [result appendFormat:@"%C", character];
        remainingLength--;
      }
    }
  }

  return result;
}

- (NSString *)stringBySha256HashingString:(NSString *)input {
  const char *string = [input UTF8String];
  unsigned char result[CC_SHA256_DIGEST_LENGTH];
  CC_SHA256(string, (CC_LONG)strlen(string), result);

  NSMutableString *hashed = [NSMutableString stringWithCapacity:CC_SHA256_DIGEST_LENGTH * 2];
  for (NSInteger i = 0; i < CC_SHA256_DIGEST_LENGTH; i++) {
    [hashed appendFormat:@"%02x", result[i]];
  }
  return hashed;
}


@end
