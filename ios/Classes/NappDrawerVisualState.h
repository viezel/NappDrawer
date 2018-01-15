/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "MMDrawerController.h"
#import <Foundation/Foundation.h>

@interface NappDrawerVisualState : NSObject

+ (MMDrawerControllerDrawerVisualStateBlock)slideAndScaleVisualStateBlock;

+ (MMDrawerControllerDrawerVisualStateBlock)slideVisualStateBlock;

+ (MMDrawerControllerDrawerVisualStateBlock)swingingDoorVisualStateBlock;

// parallax
+ (MMDrawerControllerDrawerVisualStateBlock)parallax3VisualStateBlock;
+ (MMDrawerControllerDrawerVisualStateBlock)parallax5VisualStateBlock;
+ (MMDrawerControllerDrawerVisualStateBlock)parallax7VisualStateBlock;

// fade
+ (MMDrawerControllerDrawerVisualStateBlock)fadeVisualStateBlock;

// node
+ (MMDrawerControllerDrawerVisualStateBlock)noneVisualStateBlock;

@end
