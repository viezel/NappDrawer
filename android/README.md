# Napp Drawer Module

## Description

The Napp Drawer module extends the Appcelerator Titanium Mobile framework. 
The module is licensed under the MIT license.

Thanks to *Jeremy Feinstein* for his work with this repo: https://github.com/jfeinstein10/SlidingMenu and a special thanks to *Martin Guillon* for helping me out.


### NOTICE: The below API method calls are often called something with window. The is only to keep parity with the iOS version of this module. Please remember that this Android version uses views and not windows.


## Get the module

**Find the newest version in the dist folder**

## Referencing the module in your Ti mobile application 

Simply add the following lines to your `tiapp.xml` file:
    
    <modules>
        <module platform="android">dk.napp.drawer</module> 
    </modules>


## Reference

For more detailed code examples take a look into the example app.

For Alloy projects, use the [nl.fokkezb.drawer](https://github.com/fokkezb/nl.fokkezb.drawer) widget.

### centerWindow, leftWindow, rightWindow

Napp Drawer does not require you to use the 3 views. You can also use either the combo of center/left or center/right for your desired needs. 

```javascript
var NappDrawerModule = require('dk.napp.drawer');
var mainWindow = NappDrawerModule.createDrawer({
	fullscreen:false, 
	leftWindow: leftMenuView,
	centerWindow: centerView,
	rightWindow: rightMenuView,
	fading: 0.2, // 0-1
	parallaxAmount: 0.2, //0-1
	shadowWidth:"40dp", 
	leftDrawerWidth: "200dp",
	rightDrawerWidth: "200dp",
	animationMode: NappDrawerModule.ANIMATION_NONE,
	closeDrawerGestureMode: NappDrawerModule.CLOSE_MODE_MARGIN,
	openDrawerGestureMode: NappDrawerModule.OPEN_MODE_ALL,
	orientationModes: [Ti.UI.PORTRAIT, Ti.UI.UPSIDE_PORTRAIT]
});
```

## API Properties
	
### CenterWindow, leftWindow, rightWindow

A method that allow change of a view.  

```javascript	
var newView = Ti.UI.createView({
	backgroundColor: "#FF0000"
};
mainWindow.setCenterWindow(newView);	
```

### LeftDrawerWidth, rightDrawerWidth

Update the width with the drawer side view. 

```javascript
mainWindow.setLeftDrawerWidth("160dp");
```


### OpenDrawerGestureMode

Set gesture support for opening the drawer through a mask.  

```javascript
mainWindow.setOpenDrawerGestureMode(NappDrawerModule.OPEN_MODE_NONE);
```

| input (constant) | Description | 
| ----- | ----------- |
| OPEN_MODE_NONE | The user can not open the drawer by any swipe gestures. | 
| OPEN_MODE_MARGIN | The user can open the drawer by a swipe gesture on the screen's margin. | 
| OPEN_MODE_ALL | The user can open the drawer by a swipe gesture anywhere on the screen. | 



### CloseDrawerGestureMode

Set gesture support for closing the drawer through a mask.

```javascript
mainWindow.setCloseDrawerGestureMode(NappDrawerModule.CLOSE_MODE_NONE);
```

| input (constant) | Description | 
| ----- | ----------- |
| CLOSE_MODE_NONE | The user can not close the drawer by any swipe gestures. | 
| CLOSE_MODE_MARGIN | The user can close the drawer by a swipe gesture on the screen's margin. | 
| CLOSE_MODE_ALL | The user can close the drawer by a swipe gesture anywhere on the screen. | 

*Note that these gestures may impact touches sent to the child view controllers, so be sure to use these appropriately for your application.*

### AnimationMode

Set the overall animation of the side views when opening and closing the drawer. 

```javascript
mainWindow.setAnimationMode(NappDrawerModule.ANIMATION_SLIDEUP);
```

| input (constant) | Description | 
| ----- | ----------- |
| ANIMATION_NONE | No animation | 
| ANIMATION_SLIDEUP | The side view will slide upwards while appearing on the screen. | 
| ANIMATION_ZOOM | Zoom animation. | 
| ANIMATION_SCALE | The side view will scale. | 


### ShadowWidth

How big should the shadow be. Use the "dp" notation to support different resolution on android.

```javascript
mainWindow.setShadowWidth("40dp");
```


### ParallaxAmount

Parallax is the amount of parallax between the centerView and a sideView animation. Set a value between 0 and 1. Its a very cool effect. Play with the slider in the example code to see the effect!

```javascript
mainWindow.setParallaxAmount(0.3);
```

### Fading

Use this property if you want the sideView drawer to fade in and out while entering/leaving the screen. The fade degree is between 0.0 and 1.0.

```javascript
mainWindow.setFading(0.3);
```

### OrientationModes

Use this property to restrict the drawer to a certain set of orientations. You can use any of Titanium's orientation constants defined in Ti.UI. (LANDSCAPE_LEFT, LANDSCAPE_RIGHT, PORTRAIT, UPSIDE_PORTRAIT).

```javascript
mainWindow.setOrientationModes([Ti.UI.PORTRAIT, Ti.UI.UPSIDE_PORTRAIT]);
```

## API Methods

### toggleLeftWindow, toggleRightWindow

`toggleLeftWindow()` and `toggleRightWindow()` are used to toggle each visibility of either the left or right view. 

```javascript
mainWindow.toggleLeftWindow();
```


### isAnyWindowOpen, isLeftWindowOpen, isRightWindowOpen

Check if a side or any views is opened in the drawer.

```javascript
mainWindow. isAnyWindowOpen();
```

## Changelog

* v1.1.6
  * Updated Ti min-sdk to 4.1.0.GA

* v1.1.5
  * Fix #159 - useArrowAnimation not working when there is no listener for didChangeOffset
  * Fix #162 - Lollipop Bottom Nav Bar Overlays App on Android 5.0 and up
  * Updated Ti min-sdk to 4.0.0.GA
  
* v1.1.4
  * Burger To Arrow animation icon. (Android L style)
  
* v1.1.3  
  * Fix #71 - IllegalStateException when recreating activity in Android

* v1.1.2  
  * Added focus/blur events for Android 

* v1.1.1  
  * Bugfix for ShadowWidth. Issue #51
  * Updated Ti min-sdk to 3.1.3.GA

* v1.1  
  * Added variable names to resemble iOS version more - for openDrawerGestureMode and closeDrawerGestureMode
  
* v1.0  
  * init


## Author

**Mads Møller**  
web: http://www.napp.dk  
email: mm@napp.dk  
twitter: @nappdev  


## License

    Copyright (c) 2010-2013 Mads Møller

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
