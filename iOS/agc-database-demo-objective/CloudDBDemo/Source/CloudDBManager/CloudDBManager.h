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

#import <Foundation/Foundation.h>

#import "BookInfo.h"
#import "BMQueryBookDataModel.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM (NSInteger, CloudDBManagerSortType) {
    CloudDBManagerSortTypeAsc,
    CloudDBManagerSortTypeDesc
};

@interface CloudDBManager : NSObject

/// Login state
@property (nonatomic, readonly, assign) BOOL isLogin;

+ (instancetype)shareInsatnce;

/**
 Log in AGCCloudDatabase

 * @param complete  A callback for login. YES succeeds, NO fails
 */
- (void)loginAGCWithComplete:(void (^)(BOOL success, NSError *error))complete;

/**
 Log out AGCCloudDatabase

 * @param complete Log out of the logon callback. YES succeeds, NO fails
 */
- (void)logoutAGCWithComplete:(void (^)(BOOL success))complete;

/**
 Add AGC listener when data updae

 * @param complete  block of new data
 */
- (void)subscribeSnapshotComplete:(void (^)(NSArray *bookList, NSError *error))complete;

/**
 Add book message by AGC mehod "executeUpsert"

 * @param dataModel  A data model for books
 */
- (void)executeUpsertWithBook:(BookInfo *__nonnull)dataModel complete:(void (^)(BOOL success, NSError *error))complete;

/**
 Update book message by AGC mehod "executeUpsert"

 * @param book  A data model for books
 */
- (void)executeUpdateWithBook:(BookInfo *__nonnull)book complete:(void (^)(BOOL success, NSError *error))complete;

/**
 Delete the book by its title by AGC mehod "executeDelete"

 * @param bookID Book id
 * @warning Advance queries are performed before deletion is performed
 */
- (void)deleteAGCDataWithBookID:(NSString *)bookID
                       complete:(void (^)(BOOL success, NSError *error))complete;

/**
 Query data with table name .use AGC method "executeQuery".

 * @param results query results
 */
- (void)queryAllBooksWithResults:(void (^)(NSArray *bookList, NSError *error))results;

/**
 Fuzzy query book by book model.  pass nil, means query all. Add query criteria through AGCCloudDBQuery

 * @param bookInfo book info
 * @param results query results
 */
- (void)fuzzyQueryAGCDataWithBookInfo:(nonnull BMQueryBookDataModel *)bookInfo
                              results:(void (^)(NSArray *bookList, NSError *error))results;

/**
 Query book by fieldName and sortType. Add query criteria through AGCCloudDBQuery
 Use AGC method "executeQuery"

 * @param fieldName BookInfo key
 * @param sortType desc or asc
 * @param results query results
 */
- (void)queryAGCDataWithFieldName:(NSString *)fieldName
                         sortType:(CloudDBManagerSortType)sortType
                          results:(void (^)(NSArray *bookList, NSError *error))results;

@end

NS_ASSUME_NONNULL_END
