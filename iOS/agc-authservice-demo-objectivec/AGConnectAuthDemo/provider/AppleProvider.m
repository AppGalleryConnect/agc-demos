/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

#import "AppleProvider.h"
#import <AuthenticationServices/AuthenticationServices.h>
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>

@interface AppleProvider () <ASAuthorizationControllerDelegate,ASAuthorizationControllerPresentationContextProviding>

@property(nonatomic,strong)NSString *currentRequestNonce;
@property(nonatomic) UIViewController *signViewController;

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

- (void)fetchCredentialWithController:(UIViewController *)vc completion:(CredentialBlock)completion {
    self.signViewController = vc;
    self.credentialBlock = completion;
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

#pragma mark - ASAuthorizationControllerDelegate

- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithAuthorization:(ASAuthorization *)authorization  API_AVAILABLE(ios(13.0)){
    if ([authorization.credential isKindOfClass:ASAuthorizationAppleIDCredential.class]) {
        ASAuthorizationAppleIDCredential *appleCredential = (ASAuthorizationAppleIDCredential *)authorization.credential;
        NSString *jwt = [[NSString alloc] initWithData:appleCredential.identityToken encoding:NSUTF8StringEncoding];
        NSLog(@"%@",jwt);
        AGCAuthCredential *credential = [AGCAppleIDAuthProvider credentialWithIdentityToken:appleCredential.identityToken nonce:self.currentRequestNonce];
        if (self.credentialBlock) {
            self.credentialBlock(credential);
        }
    }
}

- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithError:(NSError *)error  API_AVAILABLE(ios(13.0)){
    if (self.credentialBlock) {
        self.credentialBlock(nil);
    }
}

#pragma mark - ASAuthorizationControllerPresentationContextProviding
- (ASPresentationAnchor)presentationAnchorForAuthorizationController:(ASAuthorizationController *)controller  API_AVAILABLE(ios(13.0)){
    return self.signViewController.view.window;
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
