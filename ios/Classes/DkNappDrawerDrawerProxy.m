/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

#import "DkNappDrawerDrawerProxy.h"
#import "DkNappDrawerDrawer.h"
#import "TiUtils.h"

@implementation DkNappDrawerDrawerProxy

- (void)windowDidOpen
{
  [super windowDidOpen];
  [self reposition];
}

- (void)windowWillClose
{
  TiViewProxy *leftWinProxy = [self valueForUndefinedKey:@"leftWindow"];
  TiViewProxy *rightWinProxy = [self valueForUndefinedKey:@"rightWindow"];
  TiViewProxy *centerWinProxy = [self valueForUndefinedKey:@"centerWindow"];
  [leftWinProxy windowWillClose];
  [rightWinProxy windowWillClose];
  [centerWinProxy windowWillClose];

  [super windowWillClose];
}

- (void)windowDidClose
{
  TiViewProxy *leftWinProxy = [self valueForUndefinedKey:@"leftWindow"];
  TiViewProxy *rightWinProxy = [self valueForUndefinedKey:@"rightWindow"];
  TiViewProxy *centerWinProxy = [self valueForUndefinedKey:@"centerWindow"];
  [leftWinProxy windowDidClose];
  [rightWinProxy windowDidClose];
  [centerWinProxy windowDidClose];

  [super windowDidClose];
}

- (MMDrawerController *)_controller
{
  return [(DkNappDrawerDrawer *)[self view] controller];
}

- (TiUIView *)newView
{
  return [[DkNappDrawerDrawer alloc] init];
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
  return [[self _controller] preferredStatusBarStyle];
}

- (UIViewController *)childViewControllerForStatusBarStyle
{
  return nil;
}

#pragma API

- (void)toggleLeftWindow:(id)args
{
  TiThreadPerformOnMainThread(^{
    [(DkNappDrawerDrawer *)[self view] toggleLeftWindow:args];
  },
      NO);
}

- (void)toggleRightWindow:(id)args
{
  TiThreadPerformOnMainThread(^{
    [(DkNappDrawerDrawer *)[self view] toggleRightWindow:args];
  },
      NO);
}

- (void)bounceLeftWindow:(id)args
{
  TiThreadPerformOnMainThread(^{
    [(DkNappDrawerDrawer *)[self view] bounceLeftWindow:args];
  },
      NO);
}

- (void)bounceRightWindow:(id)args
{
  TiThreadPerformOnMainThread(^{
    [(DkNappDrawerDrawer *)[self view] bounceRightWindow:args];
  },
      NO);
}

- (NSNumber *)isAnyWindowOpen:(id)args
{
  return [(DkNappDrawerDrawer *)[self view] isAnyWindowOpen:args];
}

- (NSNumber *)isLeftWindowOpen:(id)args
{
  return [(DkNappDrawerDrawer *)[self view] isLeftWindowOpen:args];
}

- (NSNumber *)isRightWindowOpen:(id)args
{
  return [(DkNappDrawerDrawer *)[self view] isRightWindowOpen:args];
}

@end
