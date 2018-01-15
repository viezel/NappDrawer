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

#import "MMDrawerController.h"

typedef void windowAppearanceChange;

@interface CustomMMDrawerController : MMDrawerController {
  void (^_callback)(NSString *state);
}

- (void)setWindowAppearanceCallback:(void (^)(NSString *))callback;

@end
