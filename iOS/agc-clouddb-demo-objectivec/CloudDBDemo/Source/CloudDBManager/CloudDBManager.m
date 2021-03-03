/*
* Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

#import "CloudDBManager.h"

#import <AGConnectDatabase/AGConnectDatabase.h>
#import <AGConnectAuth/AGCAuth.h>

#import "AGCCloudDBObjectTypeHeaders.h"

@interface CloudDBManager ()

@property (nonatomic, strong) AGConnectCloudDB *agcConnectCloudDB;

@property (nonatomic, strong) AGCCloudDBZone *dbZone;

@property (nonatomic, assign) BOOL loginState;

@property (nonatomic, strong) dispatch_queue_t serverQueue;

@end

@implementation CloudDBManager

static CloudDBManager *_shareInsatnce = nil;

+ (instancetype)shareInsatnce {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _shareInsatnce = [[CloudDBManager alloc] init];
    });
    return _shareInsatnce;
}

#pragma mark - setter && getter
- (BOOL)isLogin {
    return self.loginState;
}

- (dispatch_queue_t)serverQueue {
    if (!_serverQueue) {
        _serverQueue = dispatch_queue_create("com.managerServer.queue", DISPATCH_QUEUE_CONCURRENT);
    }
    return _serverQueue;
}

#pragma mark - login serve
- (void)loginAGCWithComplete:(void (^)(BOOL success, NSError *error))complete {
    dispatch_async(self.serverQueue, ^{
        
        //Step 1: Initializes cloud db
        //This step simply initializes the local database
        NSError *error = nil;
        [AGConnectCloudDB initEnvironment:&error];
        
        if (error == nil) {
            NSLog(@"init cloud db suceess");
            self.agcConnectCloudDB = [AGConnectCloudDB shareInstance];
            
            // Step 2:create database object
            // After successful initialization cloud db, build the database
            NSError *createError = nil;
            [self.agcConnectCloudDB createObjectType:[AGCCloudDBObjectTypeInfoHelper obtainObjectTypeInfo]
                                               error:&createError];
            if (createError) {
                NSLog(@"created cloud database Object failed with reson:%@", createError);
            }
            
            // Step 3: Set the caching policy and Synchronization strategies.
            NSString *zoneName = @"QuickStartDemo";
            AGCCloudDBZoneConfig *zoneConfig = [[AGCCloudDBZoneConfig alloc]
                                                initWithZoneName:zoneName
                                                        syncMode:AGCCloudDBZoneSyncModeCloudCache
                                                      accessMode:AGCCloudDBZoneAccessModePublic];
            zoneConfig.persistence = YES;
            
            // Step 4: Open cloud db zone.
            __weak typeof(self) weakSelf = self;
            [self.agcConnectCloudDB openCloudDBZone2:zoneConfig
                                         allowCreate:YES
                                            callback:^(AGCCloudDBZone * _Nullable zone, NSError * _Nullable error) {
                if (error) {
                    NSLog(@"created cloud database zone failed with reson:%@", error);
                } else {
                    weakSelf.dbZone = zone;
                }
                
                // Step 5: Turn on the synchronous network switch.
                // The default is to turn on
                [self.agcConnectCloudDB enableNetwork:zoneName];
            }];
            
        } else {
            NSLog(@"init cloud db failed with reson：%@", error.description);
            if (complete) {
                complete(NO, error);
            }
            return;
        }

        // Step 6: inanonymously login
        // Login agc connect
        AGCAuth *agcAuth = [AGCAuth getInstance];
        NSLog(@"start agc inanonymously login");
        
        if (agcAuth.currentUser == nil) {
            NSLog(@"agc current user is nil");
            HMFTask<AGCSignInResult *> *loginTask = agcAuth.signInAnonymously;
            [[loginTask addOnSuccessCallback:^(AGCSignInResult *_Nullable result) {
                NSLog(@"agc sign inanonymously success.");
                self.loginState = YES;
                if (complete) {
                    complete(YES, nil);
                }
            }] addOnFailureCallback:^(NSError *_Nonnull error) {
                NSLog(@"agc sign inanonymously failed. error: %@", error);
                if (complete) {
                    complete(NO, error);
                }
            }];
        } else {
            NSLog(@"agc has login.");
            self.loginState = YES;
            if (complete) {
                complete(YES, nil);
            }
        }
    });
}

- (void)logoutAGCWithComplete:(void (^)(BOOL success))complete {
    [[AGCAuth getInstance] signOut];
    if (complete) {
        complete(YES);
    }
}

#pragma mark - Cloud Database Method

#pragma mark - add AGC Listener
- (void)subscribeSnapshotComplete:(void (^)(NSArray *bookList, NSError *error))complete {
    AGCCloudDBQuery *query = [AGCCloudDBQuery where:[BookInfo class]];
    [query equalTo:@YES forField:@"shadowFlag"];

    [self.dbZone subscribeSnapshotWithQuery:query
                                     policy:AGCCloudDBQueryPolicyCloud
                                   listener:^(AGCCloudDBSnapshot *_Nullable snapshot, NSError *_Nullable error) {
        if (snapshot != nil) {
            NSArray *bookList = snapshot.snapshotObjects;
            if (complete) {
                complete(bookList, nil);
            }
        } else {
            if (complete) {
                complete(nil, error);
            }
        }
    }];
}

#pragma mark - add data
- (void)executeUpsertWithBook:(BookInfo *__nonnull)book complete:(void (^)(BOOL success, NSError *error))complete {
    if (book == nil) {
        return;
    }

    if (book.bookName.length == 0) {
        return;
    }

    book.id = @([[self getCurrentTimestamp] integerValue]);

    // insert data
    [self.dbZone executeUpsertOne:book
                      onCompleted:^(NSInteger count, NSError *_Nullable error) {
        if (error) {
            if (complete) {
                complete(NO, error);
            }
        } else {
            if (complete) {
                complete(YES, nil);
            }
        }
    }];
}

#pragma mark - updata data
- (void)executeUpdateWithBook:(BookInfo *__nonnull)book
                     complete:(void (^)(BOOL success, NSError *error))complete {
    if (book == nil) {
        return;
    }

    if (book.bookName.length == 0) {
        return;
    }

    // update data
    [self.dbZone executeUpsertOne:book
                      onCompleted:^(NSInteger count, NSError *_Nullable error) {
        if (error) {
            if (complete) {
                complete(NO, error);
            }
        } else {
            if (complete) {
                complete(YES, nil);
            }
        }
    }];
}

#pragma mark - delete data
- (void)deleteAGCDataWithBookID:(NSString *)bookID
                       complete:(void (^)(BOOL success, NSError *error))complete;
{
    AGCCloudDBQuery *query = [AGCCloudDBQuery where:[BookInfo class]];
    [query equalTo:@(bookID.integerValue) forField:@"id"];
    __weak typeof(self) weakSelf = self;
    [self.dbZone executeQuery:query
                       policy:AGCCloudDBQueryPolicyCloud
                  onCompleted:^(AGCCloudDBSnapshot *_Nullable snapshot, NSError *_Nullable error) {
        if (snapshot != nil) {
            [weakSelf.dbZone executeDelete:snapshot.snapshotObjects
                            onCompleted:^(NSInteger count, NSError *_Nullable error) {
                if (error) {
                    if (complete) {
                        complete(NO, error);
                    }
                } else {
                    if (complete) {
                        complete(YES, nil);
                    }
                }
            }];
        }
    }];
}

#pragma mark - query table all data
- (void)queryAllBooksWithResults:(void (^)(NSArray *bookList, NSError *error))results {
    AGCCloudDBQuery *query = [AGCCloudDBQuery where:[BookInfo class]];

    [self.dbZone executeQuery:query
                       policy:AGCCloudDBQueryPolicyCloud
                  onCompleted:^(AGCCloudDBSnapshot *_Nullable snapshot, NSError *_Nullable error) {
        if (error) {
            NSLog(@"query book list failed with reson : %@", error);
            if (results) {
                results(nil, error);
            }
        } else {
            NSArray *bookList = snapshot.snapshotObjects;
            if (results) {
                results(bookList, nil);
            }
        }
    }];
}

#pragma mark - fuzzy query
- (void)fuzzyQueryAGCDataWithBookInfo:(nonnull BMQueryBookDataModel *)bookInfo
                              results:(void (^)(NSArray *bookList, NSError *error))results {
    dispatch_async(self.serverQueue, ^{
        if (bookInfo == nil) {
            NSError *error = [NSError errorWithDomain:@"DataModel is nil" code:0 userInfo:nil];
            if (results) {
                results(nil, error);
            }
        }
        // fuzzy queries are triggered only when at least one of the three is present.bookname minprice maxprice
        if (bookInfo.bookName.length == 0 &&
            bookInfo.minBookPrice.doubleValue == 0 &&
            bookInfo.maxBookPrice.doubleValue == 0 &&
            bookInfo.count <= 0) {
            NSError *error = [NSError errorWithDomain:@"The content cannot be empty" code:0 userInfo:nil];
            if (results) {
                results(nil, error);
            }
            return;
        }

        AGCCloudDBQuery *query = [AGCCloudDBQuery where:[BookInfo class]];

        // You can also use. Syntax
        //  query.contains(bookModel.bookName,@"bookName").greaterThanOrEqualTo(bookModel.minBookPrice,@"price")
        //  .lessThanOrEqualTo(bookModel.maxBookPrice,@"price").limit((int)bookModel.count);

        if (bookInfo.bookName.length > 0) {
            [query contains:bookInfo.bookName forField:@"bookName"];
        }

        if (bookInfo.minBookPrice.doubleValue > 0) {
            [query greaterThanOrEqualTo:bookInfo.minBookPrice forField:@"price"];
        }

        if (bookInfo.maxBookPrice.doubleValue > 0) {
            [query lessThanOrEqualTo:bookInfo.maxBookPrice forField:@"price"];
        }

        if (bookInfo.count > 0) {
            [query limit:(int)bookInfo.count];
        }

        [query orderByAsc:@"bookName"];

        [self.dbZone executeQuery:query
                           policy:AGCCloudDBQueryPolicyCloud
                      onCompleted:^(AGCCloudDBSnapshot *_Nullable snapshot, NSError *_Nullable error) {
            if (error) {
                NSLog(@"query book list error : %@", error);
                if (results) {
                    results(nil, error);
                }
            } else {
                NSArray *bookList = snapshot.snapshotObjects;
                if (results) {
                    results(bookList, nil);
                }
            }
        }];
    });
}

#pragma mark - query order by ASC or DESC
- (void)queryAGCDataWithFieldName:(NSString *)fieldName
                         sortType:(CloudDBManagerSortType)sortType
                          results:(void (^)(NSArray *bookList, NSError *error))results {
    AGCCloudDBQuery *query = [AGCCloudDBQuery where:[BookInfo class]];

    if (sortType == CloudDBManagerSortTypeAsc) {
        [query orderByAsc:fieldName];
    } else {
        [query orderByDesc:fieldName];
    }

    [self.dbZone executeQuery:query
                       policy:AGCCloudDBQueryPolicyCloud
                  onCompleted:^(AGCCloudDBSnapshot *_Nullable snapshot, NSError *_Nullable error) {
        if (error) {
            NSLog(@"query book list error : %@", error);
            if (results) {
                results(nil, error);
            }
        } else {
            NSArray *bookList = snapshot.snapshotObjects;
            if (results) {
                results(bookList, nil);
            }
        }
    }];
}

- (NSString *)getCurrentTimestamp {
    NSDate *date = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval time = [date timeIntervalSince1970] * 1000;
    NSString *timeString = [NSString stringWithFormat:@"%.0f", time];
    return timeString;
}

@end
