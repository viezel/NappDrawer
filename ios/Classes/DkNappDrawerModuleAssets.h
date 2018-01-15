/**
 * Module developed by Napp ApS
 * www.napp.dk
 * Mads Møller
 *
 * Appcelerator Titanium is Copyright (c) 2009-2010 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */
@interface DkNappDrawerModuleAssets : NSObject {
}
- (NSData *)moduleAsset;
- (NSData *)resolveModuleAsset:(NSString *)path;

@end
