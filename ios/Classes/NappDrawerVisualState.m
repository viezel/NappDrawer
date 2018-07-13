/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "NappDrawerVisualState.h"
#import <QuartzCore/QuartzCore.h>

@implementation NappDrawerVisualState

+ (MMDrawerControllerDrawerVisualStateBlock)slideAndScaleVisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    CGFloat minScale = .90;
    CGFloat scale = minScale + (percentVisible * (1.0 - minScale));
    CATransform3D scaleTransform = CATransform3DMakeScale(scale, scale, scale);

    CGFloat maxDistance = 50;
    CGFloat distance = maxDistance * percentVisible;
    CATransform3D translateTransform = CATransform3DIdentity;
    UIViewController *sideDrawerViewController = nil;

    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
      translateTransform = CATransform3DMakeTranslation((maxDistance - distance), 0.0, 0.0);
    } else if (drawerSide == MMDrawerSideRight) {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      translateTransform = CATransform3DMakeTranslation(-(maxDistance - distance), 0.0, 0.0);
    }

    if (sideDrawerViewController != nil && !CATransform3DIsIdentity(translateTransform)) {
      [sideDrawerViewController.view.layer setTransform:CATransform3DConcat(scaleTransform, translateTransform)];
      [sideDrawerViewController.view setAlpha:percentVisible];
    }
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)swingingDoorVisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    UIViewController *sideDrawerViewController;
    CGPoint anchorPoint = CGPointZero;
    CGFloat maxDrawerWidth = 0.0;
    CGFloat xOffset;
    CGFloat angle = 0.0;

    if (drawerSide == MMDrawerSideLeft) {

      sideDrawerViewController = drawerController.leftDrawerViewController;
      anchorPoint = CGPointMake(1.0, .5);
      maxDrawerWidth = MAX(drawerController.maximumLeftDrawerWidth, drawerController.visibleLeftDrawerWidth);
      xOffset = -(maxDrawerWidth / 2.0) + (maxDrawerWidth)*percentVisible;
      angle = -M_PI_2 + (percentVisible * M_PI_2);
    } else {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      anchorPoint = CGPointMake(0.0, .5);
      maxDrawerWidth = MAX(drawerController.maximumRightDrawerWidth, drawerController.visibleRightDrawerWidth);
      xOffset = (maxDrawerWidth / 2.0) - (maxDrawerWidth)*percentVisible;
      angle = M_PI_2 - (percentVisible * M_PI_2);
    }

    [sideDrawerViewController.view.layer setAnchorPoint:anchorPoint];
    [sideDrawerViewController.view.layer setShouldRasterize:YES];
    [sideDrawerViewController.view.layer setRasterizationScale:[[UIScreen mainScreen] scale]];

    CATransform3D swingingDoorTransform = CATransform3DIdentity;
    if (percentVisible <= 1.f) {

      CATransform3D identity = CATransform3DIdentity;
      identity.m34 = -1.0 / 1000.0;
      CATransform3D rotateTransform = CATransform3DRotate(identity, angle, 0.0, 1.0, 0.0);

      CATransform3D translateTransform = CATransform3DMakeTranslation(xOffset, 0.0, 0.0);

      CATransform3D concatTransform = CATransform3DConcat(rotateTransform, translateTransform);

      swingingDoorTransform = concatTransform;
    } else {
      CATransform3D overshootTransform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);

      NSInteger scalingModifier = 1.f;
      if (drawerSide == MMDrawerSideRight) {
        scalingModifier = -1.f;
      }

      overshootTransform = CATransform3DTranslate(overshootTransform, scalingModifier * maxDrawerWidth / 2, 0.f, 0.f);
      swingingDoorTransform = overshootTransform;
    }

    [sideDrawerViewController.view.layer setTransform:swingingDoorTransform];
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)fadeVisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    UIViewController *sideDrawerViewController;
    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
    } else {
      sideDrawerViewController = drawerController.rightDrawerViewController;
    }
    [sideDrawerViewController.view setAlpha:percentVisible];
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)noneVisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)slideVisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    //SETTING
    CGFloat parallaxFactor = 1.0;

    UIViewController *sideDrawerViewController = nil;
    CATransform3D transform = CATransform3DIdentity;

    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumLeftDrawerWidth, drawerController.visibleLeftDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((-distance) / parallaxFactor + (distance * percentVisible / parallaxFactor), 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, drawerController.maximumLeftDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    } else if (drawerSide == MMDrawerSideRight) {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumRightDrawerWidth, drawerController.visibleRightDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((distance) / parallaxFactor - (distance * percentVisible) / parallaxFactor, 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, -drawerController.maximumRightDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    }

    if (sideDrawerViewController != nil && !CATransform3DIsIdentity(transform)) {
      [sideDrawerViewController.view.layer setTransform:transform];
    }
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)parallax3VisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    //SETTING
    CGFloat parallaxFactor = 3.0;

    UIViewController *sideDrawerViewController = nil;
    CATransform3D transform = CATransform3DIdentity;

    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumLeftDrawerWidth, drawerController.visibleLeftDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((-distance) / parallaxFactor + (distance * percentVisible / parallaxFactor), 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, drawerController.maximumLeftDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    } else if (drawerSide == MMDrawerSideRight) {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumRightDrawerWidth, drawerController.visibleRightDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((distance) / parallaxFactor - (distance * percentVisible) / parallaxFactor, 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, -drawerController.maximumRightDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    }
    if (sideDrawerViewController != nil && !CATransform3DIsIdentity(transform)) {
      [sideDrawerViewController.view.layer setTransform:transform];
    }
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)parallax5VisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    //SETTING
    CGFloat parallaxFactor = 5.0;

    UIViewController *sideDrawerViewController = nil;
    CATransform3D transform = CATransform3DIdentity;

    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumLeftDrawerWidth, drawerController.visibleLeftDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((-distance) / parallaxFactor + (distance * percentVisible / parallaxFactor), 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, drawerController.maximumLeftDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    } else if (drawerSide == MMDrawerSideRight) {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumRightDrawerWidth, drawerController.visibleRightDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((distance) / parallaxFactor - (distance * percentVisible) / parallaxFactor, 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, -drawerController.maximumRightDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    }

    if (sideDrawerViewController != nil && !CATransform3DIsIdentity(transform)) {
      [sideDrawerViewController.view.layer setTransform:transform];
    }
  };
  return visualStateBlock;
}

+ (MMDrawerControllerDrawerVisualStateBlock)parallax7VisualStateBlock
{
  MMDrawerControllerDrawerVisualStateBlock visualStateBlock = ^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
    if (drawerSide == MMDrawerSideNone) {
      return;
    }

    //SETTING
    CGFloat parallaxFactor = 7.0;

    UIViewController *sideDrawerViewController = nil;
    CATransform3D transform = CATransform3DIdentity;

    if (drawerSide == MMDrawerSideLeft) {
      sideDrawerViewController = drawerController.leftDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumLeftDrawerWidth, drawerController.visibleLeftDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((-distance) / parallaxFactor + (distance * percentVisible / parallaxFactor), 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, drawerController.maximumLeftDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    } else if (drawerSide == MMDrawerSideRight) {
      sideDrawerViewController = drawerController.rightDrawerViewController;
      CGFloat distance = MAX(drawerController.maximumRightDrawerWidth, drawerController.visibleRightDrawerWidth);
      if (percentVisible <= 1.f) {
        transform = CATransform3DMakeTranslation((distance) / parallaxFactor - (distance * percentVisible) / parallaxFactor, 0.0, 0.0);
      } else {
        transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
        transform = CATransform3DTranslate(transform, -drawerController.maximumRightDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
      }
    }

    if (sideDrawerViewController != nil && !CATransform3DIsIdentity(transform)) {
      [sideDrawerViewController.view.layer setTransform:transform];
    }
  };
  return visualStateBlock;
}

@end
