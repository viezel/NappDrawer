/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "DkNappDrawerDrawer.h"
#import "DkNappDrawerDrawerProxy.h"
#import "TiUtils.h"
#import "TiViewController.h"
#import "TiUIiOSNavWindowProxy.h"

UIViewController * ControllerForViewProxy(TiViewProxy * proxy);

UIViewController * ControllerForViewProxy(TiViewProxy * proxy)
{
    [[proxy view] setAutoresizingMask:UIViewAutoresizingNone];
    
    //make the proper resize !
    TiThreadPerformOnMainThread(^{
        [proxy windowWillOpen];
        [proxy reposition];
        [proxy windowDidOpen];
    },YES);
    return [[[TiViewController alloc] initWithViewProxy:proxy] autorelease];
}

UINavigationController * NavigationControllerForViewProxy(TiUIiOSNavWindowProxy *proxy)
{
    return [proxy controller];
}

@implementation DkNappDrawerDrawer

-(void)dealloc
{
	RELEASE_TO_NIL(controller);
	[super dealloc];
}

- (id)accessibilityElement
{
	return self.controller;
}

-(MMDrawerController*)controller
{
	if (controller==nil)
	{
        
        // Check in centerWindow is a UINavigationController
        BOOL useNavController = FALSE;
        if([[[[self.proxy valueForUndefinedKey:@"centerWindow"] class] description] isEqualToString:@"TiUIiOSNavWindowProxy"]) {
            useNavController = TRUE;
        }
        
        // navController or TiWindow ?
        UIViewController *centerWindow = useNavController ? NavigationControllerForViewProxy([self.proxy valueForUndefinedKey:@"centerWindow"]) : ControllerForViewProxy([self.proxy valueForUndefinedKey:@"centerWindow"]);
        
		TiViewProxy *leftWindow = [self.proxy valueForUndefinedKey:@"leftWindow"];
        TiViewProxy *rightWindow = [self.proxy valueForUndefinedKey:@"rightWindow"];
        
        if(leftWindow != nil){
            if(rightWindow != nil){
                //both left and right
                controller =  [[CustomMMDrawerController alloc] initWithCenterViewController: centerWindow
                                                              leftDrawerViewController: ControllerForViewProxy(leftWindow)
                                                             rightDrawerViewController: ControllerForViewProxy(rightWindow) ];
            } else {
                //left only
                controller =  [[CustomMMDrawerController alloc] initWithCenterViewController: centerWindow
                                                              leftDrawerViewController: ControllerForViewProxy(leftWindow)];
            }
        } else if(rightWindow != nil){
            //right only
            controller =  [[CustomMMDrawerController alloc] initWithCenterViewController: centerWindow
                                                         rightDrawerViewController: ControllerForViewProxy(rightWindow) ];
        } else {
            //error
            NSLog(@"[ERROR] NappDrawer: No windows assigned");
            return nil;
        }
                
        // SET PROPERTIES at init
        if([self.proxy valueForUndefinedKey:@"openDrawerGestureMode"] != nil){
            [self setOpenDrawerGestureMode_:[self.proxy valueForUndefinedKey:@"openDrawerGestureMode"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"closeDrawerGestureMode"] != nil){
            [self setCloseDrawerGestureMode_:[self.proxy valueForUndefinedKey:@"closeDrawerGestureMode"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"leftDrawerWidth"] != nil){
            [self setLeftDrawerWidth_:[self.proxy valueForUndefinedKey:@"leftDrawerWidth"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"rightDrawerWidth"] != nil){
            [self setRightDrawerWidth_:[self.proxy valueForUndefinedKey:@"rightDrawerWidth"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"centerHiddenInteractionMode"] != nil){
            [self setCenterHiddenInteractionMode_:[self.proxy valueForUndefinedKey:@"centerHiddenInteractionMode"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"showShadow"] != nil){
            [self setShowShadow_:[self.proxy valueForUndefinedKey:@"showShadow"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"animationMode"] != nil) {
            [self setAnimationMode_:[self.proxy valueForUndefinedKey:@"animationMode"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"animationVelocity"] != nil) {
            [self setAnimationVelocity_:[self.proxy valueForUndefinedKey:@"animationVelocity"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"shouldStretchDrawer"] != nil) {
            [self setShouldStretchDrawer_:[self.proxy valueForUndefinedKey:@"shouldStretchDrawer"]];
        }

        if([self.proxy valueForUndefinedKey:@"showStatusBarView"] != nil){
            [self setShowsStatusBarBackgroundView_:[self.proxy valueForUndefinedKey:@"showStatusBarView"]];
        }
        
        if([self.proxy valueForUndefinedKey:@"statusBarStyle"] != nil){
            [self setStatusBarStyle_:[self.proxy valueForUndefinedKey:@"statusBarStyle"]];
        }

        // open/close window
        [controller setWindowAppearanceCallback:^(NSString *state) {
            if ([state isEqualToString:@"open"]) {
                [self.proxy fireEvent:@"windowDidOpen"];
            }
            else if ([state isEqualToString:@"close"]) {
                [self.proxy fireEvent:@"windowDidClose"];
            }
        }];
        
        // set frame bounds & add it
        UIView * controllerView = [controller view];
        [controllerView setFrame:[self bounds]];
        [self addSubview:controllerView];
        
	}
	return controller;
}

-(void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{
	[[[self controller] view] setFrame:bounds];
    [super frameSizeChanged:frame bounds:bounds];
}

// PROPERTIES

-(void)setCenterWindow_:(id)args
{
	ENSURE_UI_THREAD(setCenterWindow_, args);
    BOOL useNavController = FALSE;
    if([[[args class] description] isEqualToString:@"TiUIiOSNavWindowProxy"]) {
        useNavController = TRUE;
    }
    UIViewController *centerWindow = useNavController ? NavigationControllerForViewProxy([self.proxy valueForUndefinedKey:@"centerWindow"]) : ControllerForViewProxy([self.proxy valueForUndefinedKey:@"centerWindow"]);
	[controller setCenterViewController: centerWindow];
}

-(void)setLeftWindow_:(id)args
{
	ENSURE_UI_THREAD(setLeftWindow_, args);
    if([TiUtils boolValue:args] == 0 ){
        [controller setLeftDrawerViewController:nil];
    } else {
        [controller setLeftDrawerViewController:ControllerForViewProxy(args)];
    }
}

-(void)setRightWindow_:(id)args
{
	ENSURE_UI_THREAD(setRightWindow_, args);
    if([TiUtils boolValue:args] == 0 ){
        [controller setRightDrawerViewController:nil];
    } else {
        [controller setRightDrawerViewController:ControllerForViewProxy(args)];
    }
}

-(void)setLeftDrawerWidth_:(id)args
{
    ENSURE_UI_THREAD(setLeftDrawerWidth_,args);
    //ENSURE_SINGLE_ARG(args, NSNumber);
	[controller setMaximumLeftDrawerWidth:[TiUtils floatValue:args]];
}

-(void)setRightDrawerWidth_:(id)args
{
    ENSURE_UI_THREAD(setRightDrawerWidth_,args);
   // ENSURE_SINGLE_ARG(args, NSNumber);
	[controller setMaximumRightDrawerWidth:[TiUtils floatValue:args]];
}

-(void)setCloseDrawerGestureMode_:(id)args
{
    ENSURE_UI_THREAD(setCloseDrawerGestureMode_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setCloseDrawerGestureModeMask:[TiUtils intValue:args]];
}

-(void)setOpenDrawerGestureMode_:(id)args
{
    ENSURE_UI_THREAD(setOpenDrawerGestureMode_,args);    
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setOpenDrawerGestureModeMask:[TiUtils intValue:args]];
}

-(void)setCenterHiddenInteractionMode_:(id)args
{
    ENSURE_UI_THREAD(setCenterHiddenInteractionMode_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setCenterHiddenInteractionMode:[TiUtils intValue:args]];
}

-(void)setAnimationVelocity_:(id)args
{
    ENSURE_UI_THREAD(setAnimationVelocity_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
	[controller setAnimationVelocity:[TiUtils floatValue:args]];
}

-(void)setShowShadow_:(id)args
{
    ENSURE_UI_THREAD(setShowShadow_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setShowsShadow:[TiUtils boolValue:args]];
}

-(void)setShouldStretchDrawer_:(id)args
{
    ENSURE_UI_THREAD(setShouldStretchDrawer_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setShouldStretchDrawer:[TiUtils boolValue:args]];
    
}

-(void)setShowsStatusBarBackgroundView_:(id)args
{
    ENSURE_UI_THREAD(setShowsStatusBarBackgroundView_,args);
    ENSURE_SINGLE_ARG(args, NSNumber);
    [controller setShowsStatusBarBackgroundView:[TiUtils boolValue:args]];
}

-(UIStatusBarStyle)preferredStatusBarStyle {
    if(self.controller.showsStatusBarBackgroundView){
        return UIStatusBarStyleLightContent;
    }
    else {
        return UIStatusBarStyleDefault;
    }
}


-(void)setStatusBarStyle_:(NSNumber *)style
{
    ENSURE_UI_THREAD(setStatusBarStyle_,style);
    [[UIApplication sharedApplication] setStatusBarStyle:[style intValue]];
}


-(void)setAnimationMode_:(id)args
{
    ENSURE_UI_THREAD(setAnimationMode_,args);
    int mode = [TiUtils intValue:args];
    switch (mode) {
        case 1:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState slideAndScaleVisualStateBlock]];
            break;
        case 2:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState slideVisualStateBlock]];
            break;
        case 3:
            //[controller setDrawerVisualStateBlock:[NappDrawerVisualState swingingDoorVisualStateBlock]];
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState noneVisualStateBlock]];
            break;
        case 4:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState parallax3VisualStateBlock]];
            break;
        case 5:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState parallax5VisualStateBlock]];
            break;
        case 6:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState parallax7VisualStateBlock]];
            break;
        case 7:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState fadeVisualStateBlock]];
            break;
        case 100:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState noneVisualStateBlock]];
        default:
            [controller setDrawerVisualStateBlock:[NappDrawerVisualState noneVisualStateBlock]];
            break;
    }

}


// API
-(void)toggleLeftWindow:(id)args
{
    ENSURE_UI_THREAD(toggleLeftWindow,args);
    [controller toggleDrawerSide:MMDrawerSideLeft animated:YES completion:nil];
}

-(void)toggleRightWindow:(id)args
{
    ENSURE_UI_THREAD(toggleRightWindow,args);
    [controller toggleDrawerSide:MMDrawerSideRight animated:YES completion:nil];
}

-(void)bounceLeftWindow:(id)args
{
    ENSURE_UI_THREAD(bounceLeftWindow,args);
    [controller bouncePreviewForDrawerSide:MMDrawerSideLeft completion:nil];
}

-(void)bounceRightWindow:(id)args
{
    ENSURE_UI_THREAD(bounceRightWindow,args);
    [controller bouncePreviewForDrawerSide:MMDrawerSideRight completion:nil];
}

-(NSNumber*)isAnyWindowOpen:(id)args
{
    return NUMBOOL(controller.openSide != MMDrawerSideNone);
}

-(NSNumber*)isLeftWindowOpen:(id)args
{
    return NUMBOOL(controller.openSide == MMDrawerSideLeft);
}

-(NSNumber*)isRightWindowOpen:(id)args
{
    return NUMBOOL(controller.openSide == MMDrawerSideLeft);
}

-(void)close:(id)args {
    self.removeFromSuperview;
    RELEASE_TO_NIL(controller);
    [self dealloc];
}



@end
