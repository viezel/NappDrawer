/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */
#import "DkNappDrawerModule.h"
#import "TiBase.h"
#import "TiHost.h"
#import "TiUtils.h"

#import "MMDrawerController.h"

@implementation DkNappDrawerModule

# pragma mark GestureModes

MAKE_SYSTEM_PROP(CLOSE_MODE_NONE,                       MMCloseDrawerGestureModeNone);
MAKE_SYSTEM_PROP(CLOSE_MODE_ALL,                        MMCloseDrawerGestureModeAll);
MAKE_SYSTEM_PROP(CLOSE_MODE_PANNING_NAVBAR,             MMCloseDrawerGestureModePanningNavigationBar);
MAKE_SYSTEM_PROP(CLOSE_MODE_PANNING_CENTERWINDOW,       MMCloseDrawerGestureModePanningCenterView);
MAKE_SYSTEM_PROP(CLOSE_MODE_BEZEL_PANNING_CENTERWINDOW, MMCloseDrawerGestureModeBezelPanningCenterView);
MAKE_SYSTEM_PROP(CLOSE_MODE_TAP_NAVBAR,                 MMCloseDrawerGestureModeTapNavigationBar);
MAKE_SYSTEM_PROP(CLOSE_MODE_TAP_CENTERWINDOW,           MMCloseDrawerGestureModeTapCenterView);
MAKE_SYSTEM_PROP(CLOSE_MODE_PANNING_DRAWER,             MMCloseDrawerGestureModePanningDrawerView);

MAKE_SYSTEM_PROP(OPEN_MODE_NONE,                        MMOpenDrawerGestureModeNone);
MAKE_SYSTEM_PROP(OPEN_MODE_ALL,                         MMOpenDrawerGestureModeAll);
MAKE_SYSTEM_PROP(OPEN_MODE_PANNING_NAVBAR,              MMOpenDrawerGestureModePanningNavigationBar);
MAKE_SYSTEM_PROP(OPEN_MODE_PANNING_CENTERWINDOW,        MMOpenDrawerGestureModePanningCenterView);
MAKE_SYSTEM_PROP(OPEN_MODE_BEZEL_PANNING_CENTERWINDOW,  MMOpenDrawerGestureModeBezelPanningCenterView);

MAKE_SYSTEM_PROP(OPEN_CENTER_MODE_NONE,                 MMDrawerOpenCenterInteractionModeNone);
MAKE_SYSTEM_PROP(OPEN_CENTER_MODE_FULL,                 MMDrawerOpenCenterInteractionModeFull);
MAKE_SYSTEM_PROP(OPEN_CENTER_MODE_NAVBAR,               MMDrawerOpenCenterInteractionModeNavigationBarOnly);


# pragma mark AnimationModes

MAKE_SYSTEM_PROP(ANIMATION_SLIDE_SCALE,                 1);
MAKE_SYSTEM_PROP(ANIMATION_SLIDE,                       2);
//MAKE_SYSTEM_PROP(ANIMATION_SWINGING_DOOR,               3);
MAKE_SYSTEM_PROP(ANIMATION_PARALLAX_FACTOR_3,           4);
MAKE_SYSTEM_PROP(ANIMATION_PARALLAX_FACTOR_5,           5);
MAKE_SYSTEM_PROP(ANIMATION_PARALLAX_FACTOR_7,           6);
MAKE_SYSTEM_PROP(ANIMATION_FADE,                        7);
MAKE_SYSTEM_PROP(ANIMATION_NONE,                        100);


# pragma mark StatusBar

MAKE_SYSTEM_PROP(STATUSBAR_BLACK,0);
MAKE_SYSTEM_PROP(STATUSBAR_WHITE,1);

MAKE_SYSTEM_PROP(STATUSBAR_ANIMATION_NONE,0);
MAKE_SYSTEM_PROP(STATUSBAR_ANIMATION_FADE,1);
MAKE_SYSTEM_PROP(STATUSBAR_ANIMATION_SLIDE,2);


#pragma mark Internal

// this is generated for your module, please do not change it
-(id)moduleGUID
{
	return @"2a446559-1d59-4808-aefc-7d02d3130ebb";
}

// this is generated for your module, please do not change it
-(NSString*)moduleId
{
	return @"dk.napp.drawer";
}

#pragma mark Lifecycle

-(void)startup  // module class startup method
{
    // this method is called when the module is first loaded
	// you *must* call the superclass
    [super startup];
    NSLog(@"[INFO] %@ loaded",self);
}

-(void)shutdown:(id)sender
{
	// this method is called when the module is being unloaded
	// typically this is during shutdown. make sure you don't do too
	// much processing here or the app will be quit forceably
	
	// you *must* call the superclass
	[super shutdown:sender];
}

#pragma mark Cleanup 

-(void)dealloc
{
	// release any resources that have been retained by the module
	[super dealloc];
}

#pragma mark Internal Memory Management

-(void)didReceiveMemoryWarning:(NSNotification*)notification
{
	// optionally release any resources that can be dynamically
	// reloaded once memory is available - such as caches
	[super didReceiveMemoryWarning:notification];
}

#pragma mark Listener Notifications

-(void)_listenerAdded:(NSString *)type count:(int)count
{
	if (count == 1 && [type isEqualToString:@"my_event"])
	{
		// the first (of potentially many) listener is being added 
		// for event named 'my_event'
	}
}

-(void)_listenerRemoved:(NSString *)type count:(int)count
{
	if (count == 0 && [type isEqualToString:@"my_event"])
	{
		// the last listener called for event named 'my_event' has
		// been removed, we can optionally clean up any resources
		// since no body is listening at this point for that event
	}
}



#pragma Public APIs


-(void)hideStatusBar:(id)args
{
    ENSURE_SINGLE_ARG_OR_NIL(args,NSDictionary);
    ENSURE_UI_THREAD(hideStatusBar,args);
    
    int style = [TiUtils intValue:@"animationStyle" properties:args def:UIStatusBarAnimationSlide];
    [[UIApplication sharedApplication] setStatusBarHidden:YES withAnimation:style];
}


-(void)showStatusBar:(id)args
{
    ENSURE_SINGLE_ARG_OR_NIL(args,NSDictionary);
    ENSURE_UI_THREAD(showStatusBar,args);
    
    int style = [TiUtils intValue:@"animationStyle" properties:args def:UIStatusBarAnimationSlide];
    [[UIApplication sharedApplication] setStatusBarHidden:NO withAnimation:style];
}


-(void)setStatusBarStyle:(NSNumber *)style
{
	ENSURE_UI_THREAD(setStatusBarStyle,style);
	[[UIApplication sharedApplication] setStatusBarStyle:[style intValue]];
}

@end
