//
//  CustomMMDrawerController.h
//  NappDrawer
//
//  Created by Azwan b. Amit on 12/31/13.
//
//

#import "MMDrawerController.h"

typedef void windowAppearanceChange;

@interface CustomMMDrawerController : MMDrawerController
{
    void (^_callback)(NSString *state);
}

-(void)setWindowAppearanceCallback:(void(^)(NSString*))callback;

@end
