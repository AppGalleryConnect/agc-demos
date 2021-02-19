//
//  Copyright (c) Huawei Technologies Co., Ltd. 2020. All rights reserved
//

#import "ViewController.h"
#import <AGConnectFunction/AGConnectFunction.h>

@interface NumberInfo : NSObject

@property (nonatomic) NSInteger number1;

@property (nonatomic) NSInteger number2;

@end

@implementation NumberInfo

@end

@interface SumInfo : NSObject

@property (nonatomic) NSInteger result;

@end

@implementation SumInfo

@end

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UITextField *input1;

@property (weak, nonatomic) IBOutlet UITextField *input2;

@property (weak, nonatomic) IBOutlet UILabel *sumLabel;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)clickAddButton:(id)sender {
    AGCFunctionCallable *callable = [[AGCFunction getInstance] wrap:@"addtest-$latest"];
    NumberInfo *num = [[NumberInfo alloc] init];
    num.number1 = [_input1.text intValue];
    num.number2 = [_input2.text intValue];
    __weak typeof(self) weakSelf = self;
    [[[callable callWithObject:num] addOnSuccessCallback:^(AGCFunctionResult * _Nullable result) {
        SumInfo *sum = [result valueWithClass:[SumInfo class]];
        NSLog(@"onSuccess, sum = %ld",(long)sum.result);
        weakSelf.sumLabel.text = [NSString stringWithFormat:@"Sum : %ld", sum.result];
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        NSLog(@"onFailure %@",error);
    }];
    
}

@end
