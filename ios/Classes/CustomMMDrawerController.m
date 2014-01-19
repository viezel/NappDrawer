/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * CustomMMDrawerController - PR from Azwan b. Amit
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "CustomMMDrawerController.h"

@implementation CustomMMDrawerController

-(void)openDrawerSide:(MMDrawerSide)drawerSide animated:(BOOL)animated completion:(void (^)(BOOL finished))completion
{
    [super openDrawerSide:drawerSide animated:animated completion:^(BOOL finished) {
        if (finished){
            _callback(@"open");
        }
    }];
}

-(void)closeDrawerAnimated:(BOOL)animated completion:(void (^)(BOOL finished))completion
{
    [super closeDrawerAnimated:animated completion:^(BOOL finished) {
        if (finished){
            _callback(@"close");
        }
    }];
}

-(void)setWindowAppearanceCallback:(void(^)(NSString*))callback
{
    _callback = [callback copy];
    
    // add callback for the gestures
    [super setGestureCompletionBlock:^(MMDrawerController *controller, UIGestureRecognizer *gesture) {
        if(controller.openSide == MMDrawerSideNone){
            _callback(@"close");
        } else {
            _callback(@"open");
        }
    }];
}

-(void)dealloc
{
    [super dealloc];
    [_callback release];
    _callback = nil;
}

@end
