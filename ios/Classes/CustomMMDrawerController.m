//
//  CustomMMDrawerController.m
//  NappDrawer
//
//  Created by Azwan b. Amit on 12/31/13.
//
//

#import "CustomMMDrawerController.h"

@implementation CustomMMDrawerController

-(void)openDrawerSide:(MMDrawerSide)drawerSide animated:(BOOL)animated velocity:(CGFloat)velocity animationOptions:(UIViewAnimationOptions)options completion:(void (^)(BOOL finished))completion
{
    NSLog(@"openDrawerSide()!");
    [super openDrawerSide:drawerSide animated:animated velocity:velocity animationOptions:options completion:^(BOOL finished) {
        if (finished){
            _callback(@"open");
        }
    }];
}

-(void)closeDrawerAnimated:(BOOL)animated velocity:(CGFloat)velocity animationOptions:(UIViewAnimationOptions)options completion:(void (^)(BOOL finished))completion
{
    NSLog(@"closeDrawerAnimated()!");
    [super closeDrawerAnimated:animated velocity:velocity animationOptions:options completion:^(BOOL finished) {
        if (finished){
            _callback(@"close");
        }
    }];
}

-(void)setWindowAppearanceCallback:(void(^)(NSString*))callback
{
    _callback = [callback copy];
}

-(void)dealloc
{
    [super dealloc];
    [_callback release];
    _callback = nil;
}

@end
