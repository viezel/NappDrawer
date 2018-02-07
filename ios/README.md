# Napp Drawer Module

## Description

The Napp Drawer module extends the Appcelerator Titanium Mobile framework.
The module is licensed under the MIT license.

Thanks to the folks at Mutual Mobile for their great work with MMDrawerController https://github.com/mutualmobile/MMDrawerController


### Get the module

**Find the newest version in the dist folder**

## Referencing the module in your Ti mobile application

Simply add the following lines to your `tiapp.xml` file:

```xml
<modules>
  <module platform="iphone">dk.napp.drawer</module>
</modules>
```

## Reference

For more detailed code examples take a look into the example app.

### centerWindow, leftWindow, rightWindow

Napp Drawer does not require you to use the three windows. You can also use either the combo of center/left or center/right for your desired needs.

```javascript
var mainWindow = NappDrawerModule.createDrawer({
  centerWindow: navController,
  leftWindow: winLeft,
  rightWindow: winRight,
  closeDrawerGestureMode: NappDrawerModule.CLOSE_MODE_ALL,
  openDrawerGestureMode: NappDrawerModule.OPEN_MODE_ALL,
  leftDrawerWidth: 180,
  rightDrawerWidth: 220,
  statusBarStyle: NappDrawerModule.STATUSBAR_WHITE,
  orientationModes: [Ti.UI.PORTRAIT, Ti.UI.UPSIDE_PORTRAIT]
});
```

## API Properties

### CenterWindow, leftWindow, rightWindow

A method that allow change of a window. You can either use a window or a navigation group.
```javascript
var newWin = Ti.UI.createWindow({
  backgroundColor: '#FF0000'
};
mainWindow.setCenterWindow(newWin);
```

If you want to remove the side drawer, you can do this be parsing false as the argument.
```javascript
mainWindow.setRightWindow(false);
```

### LeftDrawerWidth, rightDrawerWidth

Update the width with these methods. The default width is `280`

```javascript
mainWindow.setLeftDrawerWidth(160);
```

With animation:

```javascript
mainWindow.setLeftDrawerWidth({
  width: 160,
  animated: true
});
```

### CenterHiddenInteractionMode

Set different types of interactivity for the centerWindow.

```javascript
mainWindow.setCenterHiddenInteractionMode(NappDrawerModule.OPEN_CENTER_MODE_FULL);
```

| input (constant) | Description |
| ----- | ----------- |
| OPEN_CENTER_MODE_NONE | The user can not interact with any content in the center view. |
| OPEN_CENTER_MODE_FULL | The user can interact with all content in the center view. |
| OPEN_CENTER_MODE_NAVBAR | The user can interact with only content on the navigation bar. The setting allows the menu button to still respond, allowing you to toggle the drawer closed when it is open. This is the default setting. |


### OpenDrawerGestureMode

Set gesture support for opening the drawer through a mask.

```javascript
mainWindow.setOpenDrawerGestureMode(NappDrawerModule.OPEN_MODE_BEZEL_PANNING_CENTERWINDOW);
```

| input (constant) | Description |
| ----- | ----------- |
| OPEN_MODE_NONE | The user can not open the drawer by panning. |
| OPEN_MODE_ALL | The user can open the drawer by panning anywhere. |
| OPEN_MODE_PANNING_NAVBAR | The user can open the drawer by panning anywhere on the navigation bar. |
| OPEN_MODE_PANNING_CENTERWINDOW | The user can open the drawer by panning anywhere on the center view. |
| OPEN_MODE_BEZEL_PANNING_CENTERWINDOW | The user can open the drawer by starting a pan anywhere within 20 points of the bezel. |


### CloseDrawerGestureMode

Set gesture support for closing the drawer through a mask.

```javascript
mainWindow.setCloseDrawerGestureMode(NappDrawerModule.CLOSE_MODE_TAP_CENTERWINDOW);
```

| input (constant) | Description |
| ----- | ----------- |
| CLOSE_MODE_NONE | The user cannot close the drawer by any panning gestures. |
| CLOSE_MODE_ALL | The user can close the drawer by panning anywhere. |
| CLOSE_MODE_PANNING_NAVBAR | The user can close the drawer by panning anywhere on the navigation bar. |
| CLOSE_MODE_PANNING_CENTERWINDOW | The user can close the drawer by panning anywhere on the center view. |
| CLOSE_MODE_BEZEL_PANNING_CENTERWINDOW | The user can close the drawer by starting a pan anywhere within the bezel of the center view. |
| CLOSE_MODE_TAP_NAVBAR | The user can close the drawer by tapping the navigation bar. |
| CLOSE_MODE_TAP_CENTERWINDOW | The user can close the drawer by tapping the center view. |
| CLOSE_MODE_PANNING_DRAWER | The user can close the drawer by panning anywhere on the drawer view. |

*Note that these gestures may impact touches sent to the child view controllers, so be sure to use these appropriately for your application.*

### AnimationMode

Set the overall animation of the side windows when opening and closing the drawer.

```javascript
mainWindow.setAnimationMode(NappDrawerModule.ANIMATION_SLIDE);
```

| input (constant) | Description |
| ----- | ----------- |
| ANIMATION_NONE | No animation |
| ANIMATION_SLIDE | The side window will slide in at the same speed as the drawer. This is equivalent to a parallax effect with factor 1 (no parallax effect).  |
| ANIMATION_PARALLAX_FACTOR_3 | The slide animation, but with a parallax effect. The higher factor equals less animation distance rate. This is 3 times slower than ANIMATION_SLIDE. |
| ANIMATION_PARALLAX_FACTOR_5 | The slide animation, but with a parallax effect. The higher factor equals less animation distance rate. This is 5 times slower than ANIMATION_SLIDE. |
| ANIMATION_PARALLAX_FACTOR_7 | The slide animation, but with a parallax effect. The higher factor equals less animation distance rate. This is 7 times slower than ANIMATION_SLIDE. |
| ANIMATION_FADE | Fading out/in the side window. |
| ANIMATION_SLIDE_SCALE | The Side window will slide and scale simultaneously. |

### AnimationVelocity

The animation velocity of the open and close methods, measured in points per second.
By default, this is set to 840 points per second (three times the default drawer width), meaning it takes 1/3 of a second for the `Window` to open/close across the default drawer width. Note that there is a minimum .1 second duration for built in animations, to account for small distance animations.

```javascript
mainWindow.setAnimationVelocity(400);
```

### ShowShadow

Should the shadow be visible?

```javascript
mainWindow.setShowShadow(true);
```

### ShouldStretchDrawer

A small effect when the drawer is dragged, the drawer stretches.

```javascript
mainWindow.setShouldStretchDrawer(true);
```

### OrientationModes

Use this property to restrict the drawer to a certain set of orientations. You can use any of Titanium's orientation constants defined in Ti.UI. (LANDSCAPE_LEFT, LANDSCAPE_RIGHT, PORTRAIT, UPSIDE_PORTRAIT).

```javascript
mainWindow.setOrientationModes([Ti.UI.PORTRAIT, Ti.UI.UPSIDE_PORTRAIT]);
```

### StatusBarStyle

Use this property to set the statusBar. You will need to add the following to tiapp.xml in order to make this work:

```xml
<ios>
  <plist>
    <dict>
      <key>UIViewControllerBasedStatusBarAppearance</key>
      <false/>
    </dict>
  </plist>
</ios>
```

| input (constant) | Description |
| ----- | ----------- |
| STATUSBAR_BLACK | The statusbar icons and text will be black |
| STATUSBAR_WHITE | The statusbar icons and text will be white |

```javascript
mainWindow.setStatusBarStyle(NappDrawerModule.STATUSBAR_WHITE);
```

### Alternative method

If your drawer has a rightWindow, you can also set the statusbarStyle off that window, which will automagically apply that style to all of the drawer. This was discovered by Andrea Vitale.

```javascript
rightWindow: Ti.UI.createWindow({
  statusBarStyle: Ti.UI.iPhone.StatusBar.LIGHT_CONTENT
});
```

## API Methods

### toggleLeftWindow, toggleRightWindow

`toggleLeftWindow()` and `toggleRightWindow()` are used to toggle each visibility of either the left or right window.

```javascript
mainWindow.toggleLeftWindow();
```

### bounceLeftWindow, bounceRightWindow

A small animation to show the app user that its possible to interact with the drawer.

```javascript
mainWindow.bounceLeftWindow();
```

### isAnyWindowOpen, isLeftWindowOpen, isRightWindowOpen

Check if a side or any window is opened in the drawer.

```javascript
mainWindow.isAnyWindowOpen();
```

## Events

### windowDidOpen

When the drawer has been opened.

```javascript
mainWindow.addEventListener('windowDidOpen', function(e) {
  // Drawer opened
});
```

### windowDidClose

When the drawer has been closed.

```javascript
mainWindow.addEventListener('windowDidClose', function(e) {
  // Drawer closed
});
```

### centerWindowDidFocus

When the drawer center window has been focussed.

```javascript
mainWindow.addEventListener('centerWindowDidFocus', function(e) {
  // Center window focussed
});
```

### centerWindowDidBlur

When the drawer center window has been blurred.

```javascript
mainWindow.addEventListener('centerWindowDidBlur', function(e) {
  // Center window blurred
});
```

## Changelog

*v2.1.0
  * Added accessibility to the menu buttons (#102)

* v2.0.0
  * Major module refactoring (https://github.com/viezel/NappDrawer/pull/211)

* v1.2.3
  * Fixed statusbare frame when rotating the device

* v1.2.2
  * Fixed isRightDrawerOpen() returning wrong value

* v1.2.1
  * Fixed close() leaking child windows

* v1.1.7
  * Added support for setting the drawer width with an animation

* v1.1.6
  * Updated MMDrawerController classes to latest release

* v1.1.5
  * Recompiled with Ti 3.2.3.GA to fix `close()` crash.

* v1.1.4
  * Added gesture events for open and close the drawer.
  * Added `close()` method to remove the Drawer completely.

* v1.1.3
  * Added `statusBarStyle` to help iOS7 and the LIGHT CONTENT bug. Thanks to @adrianopaladini.

* v1.1.2
  * Added events for open and close the drawer.
  * Added `animationMode`, `animationVelocity`, `shouldStretchDrawer` to the create method.

* v1.1.1
  * Added support for removing the side drawer.

* v1.1.0
  * iOS7 support
  * Titanium minimum SDK changed to 3.1.3.GA
  * iOS min-sdk is now 5.0

* v1.0.3
  * Fixed issue with setShowShadow(bool) that prevented it from working. Exposed property 'showShadow' (true/false) in 'createDrawer' function.

* v1.0.2
  * Added animations. `animationMode`

* v1.0.1
  * Added `isAnyWindowOpen`, `isLeftWindowOpen`, `isRightWindowOpen` methods

* v1.0
  * init


## Author

**Mads Møller**
web: http://www.napp.dk
email: mm@napp.dk
twitter: @nappdev


## License

    Copyright (c) 2010-present Mads Møller

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
