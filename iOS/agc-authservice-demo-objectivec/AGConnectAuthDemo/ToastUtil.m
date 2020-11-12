//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ToastUtil.h"
#import <MBProgressHUD/MBProgressHUD.h>

@implementation ToastUtil

+ (void)showToast:(NSString *)string {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:[[UIApplication sharedApplication].windows firstObject] animated:YES];
    hud.mode = MBProgressHUDModeText;
    hud.label.text = string;
    [hud hideAnimated:YES afterDelay:1];
}

@end
