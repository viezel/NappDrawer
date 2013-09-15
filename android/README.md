# Napp Drawer Module

## Description

The Napp Drawer module extends the Appcelerator Titanium Mobile framework. 
The module is licensed under the MIT license.

Thanks to *Jeremy Feinstein* for his work with this repo: https://github.com/jfeinstein10/SlidingMenu and a special thanks to *Martin Guillon* for helping me out.


### NOTICE: The below API method calls are often called something with window. The is only to keep parity with the iOS version of this module. Please remember that this Android version uses views and not windows.**


## Get the module

**Find the newest version in the dist folder**

## Referencing the module in your Ti mobile application 

Simply add the following lines to your `tiapp.xml` file:
    
    <modules>
        <module platform="android">dk.napp.drawer</module> 
    </modules>


## Reference

For more detailed code examples take a look into the example app

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
	closeDrawerGestureMode: NappDrawerModule.MODE_MARGIN,
	openDrawerGestureMode: NappDrawerModule.MODE_ALL
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
mainWindow.setOpenDrawerGestureMode(NappDrawerModule.MODE_NONE);
```

| input (constant) | Description | 
| ----- | ----------- |
| MODE_NONE | The user can not open the drawer by any swipe gestures. | 
| MODE_MARGIN | The user can open the drawer by a swipe gesture on the screen's margin. | 
| MODE_ALL | The user can open the drawer by a swipe gesture anywhere on the screen. | 



### CloseDrawerGestureMode

Set gesture support for closing the drawer through a mask.

```javascript
mainWindow.setCloseDrawerGestureMode(NappDrawerModule.MODE_MARGIN);
```

| input (constant) | Description | 
| ----- | ----------- |
| MODE_NONE | The user can not close the drawer by any swipe gestures. | 
| MODE_MARGIN | The user can close the drawer by a swipe gesture on the screen's margin. | 
| MODE_ALL | The user can close the drawer by a swipe gesture anywhere on the screen. | 

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

### AnimationVelocity

The animation velocity of the open and close methods, measured in points per second.
By default, this is set to 840 points per second (three times the default drawer width), meaning it takes 1/3 of a second for the `Window` to open/close across the default drawer width. Note that there is a minimum .1 second duration for built in animations, to account for small distance animations.

```javascript
mainWindow.setAnimationVelocity(400);
```

### ShadowWidth

How big should the shadow be. Use the "dp" notation to support different resolution on android.

```javascript
mainWindow.setShadowWidth("40dp");
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
