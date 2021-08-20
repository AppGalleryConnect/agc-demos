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

#import "ViewController.h"
#import <AGConnectStorage/AGConnectStorage.h>
#import <AGConnectAuth/AGConnectAuth.h>

@interface ViewController ()

@property (nonatomic) AGCStorage *storage;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.storage = [AGCStorage getInstanceForBucketName:@"your_bucket_name"];
}

- (IBAction)login:(id)sender {
    [[[[AGCAuth getInstance] signInAnonymously] addOnSuccessCallback:^(AGCSignInResult * _Nullable result) {
        NSLog(@"login success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"login failed  %@",error);
    }];
}

- (IBAction)logout:(id)sender {
    [[AGCAuth getInstance] signOut];
}

- (IBAction)uploadFile:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    NSString *dirPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
    NSString *filePath = [dirPath stringByAppendingPathComponent:@"test.jpg"];
    AGCStorageUploadTask *task = [storageReference uploadFile:[NSURL fileURLWithPath:filePath]];
    [[[[[[task addOnSuccessCallback:^(AGCStorageUploadResult * _Nullable result) {
        NSLog(@"upload success  %@",result);
    }] addOnCompleteCallback:^(AGCStorageTask<AGCStorageUploadResult *> * _Nonnull task) {
        NSLog(@"upload complete  %@",task.result);
    }] addOnProgressCallback:^(AGCStorageUploadResult * _Nullable result) {
        NSLog(@"upload progress  %ld, %ld",(long)result.bytesTransferred, (long)result.totalByteCount);
    }] addOnPausedCallback:^(AGCStorageUploadResult * _Nullable result) {
        NSLog(@"upload paused  %ld, %ld",(long)result.bytesTransferred, (long)result.totalByteCount);
    }] addOnCanceledCallback:^{
        NSLog(@"upload canceled");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"upload failed  %@",error);
    }];
}

- (IBAction)downloadFile:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    NSString *dirPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
    NSString *destFilePath = [dirPath stringByAppendingPathComponent:@"test.jpg"];
    AGCStorageDownloadTask *task = [storageReference downloadToFile:[NSURL fileURLWithPath:destFilePath]];
    [[[[[[task addOnSuccessCallback:^(AGCStorageDownloadResult * _Nullable result) {
        NSLog(@"download success  %@",result);
    }] addOnCompleteCallback:^(AGCStorageTask<AGCStorageDownloadResult *> * _Nonnull task) {
        NSLog(@"download complete  %@",task.result);
    }] addOnProgressCallback:^(AGCStorageDownloadResult * _Nullable result) {
        NSLog(@"download progress  %ld, %ld",(long)result.bytesTransferred, (long)result.totalByteCount);
    }] addOnPausedCallback:^(AGCStorageDownloadResult * _Nullable result) {
        NSLog(@"download paused  %ld, %ld",(long)result.bytesTransferred, (long)result.totalByteCount);
    }] addOnCanceledCallback:^{
        NSLog(@"download canceled");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"download failed  %@",error);
    }];
}

- (IBAction)getFileMetadata:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    HMFTask<AGCStorageMetadata *> *task = [storageReference getMetadata];
    [[task addOnSuccessCallback:^(AGCStorageMetadata * _Nullable result) {
        NSLog(@"getFileMetadata success  %@",result);
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"getFileMetadata failed  %@",error);
    }];
}

- (IBAction)updateFileMetadata:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    AGCStorageMetadata *metadata = [AGCStorageMetadata new];
    metadata.contentType = @"image/jpg";
    metadata.cacheControl = @"no-cache";
    metadata.contentEncoding = @"identity";
    metadata.contentDisposition = @"inline";
    metadata.contentLanguage = @"en";
    HMFTask<AGCStorageMetadata *> *task = [storageReference updateMetadata:metadata];
    [[task addOnSuccessCallback:^(AGCStorageMetadata * _Nullable result) {
        NSLog(@"updateFileMetadata success  %@",result);
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"updateFileMetadata failed  %@",error);
    }];
}

- (IBAction)getFileList:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    HMFTask<AGCStorageListResult *> *task = [storageReference list:100];
    [[task addOnSuccessCallback:^(AGCStorageListResult * _Nullable result) {
        NSLog(@"getFileList success  %@",result);
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"getFileList failed  %@",error);
    }];
}

- (IBAction)deleteFile:(id)sender {
    AGCStorageReference *storageReference = [self.storage referenceWithPath:@"test.jpg"];
    HMFTask *task = [storageReference deleteFile];
    [[task addOnSuccessCallback:^(id  _Nullable result) {
        NSLog(@"deleteFile success");
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"deleteFile failed  %@",error);
    }];
}

@end
