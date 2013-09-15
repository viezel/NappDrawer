# Napp Drawer Module

## Description

The Napp Drawer module extends the Appcelerator Titanium Mobile framework. 
The module is licensed under the MIT license.

Thanks to the folks at Mutual Mobile for their great work with MMDrawerController https://github.com/mutualmobile/MMDrawerController


### Get the module

**Find the newest version in the dist folder**

## Referencing the module in your Ti mobile application 

Simply add the following lines to your `tiapp.xml` file:
    
    <modules>
        <module platform="iphone">dk.napp.drawer</module> 
    </modules>


## Reference

For more detailed code examples take a look into the example app

### centerWindow, leftWindow, rightWindow

Napp Drawer does not require you to use the 3 windows. You can also use either the combo of center/left or center/right for your desired needs. 

```javascript
var mainWindow = NappDrawerModule.createDrawer({
	centerWindow: navController, 
	leftWindow: winLeft,
	rightWindow: winRight,
	closeDrawerGestureMode: NappDrawerModule.CLOSE_MODE_ALL,
	openDrawerGestureMode: NappDrawerModule.OPEN_MODE_ALL,
	leftDrawerWidth: 180,
	rightDrawerWidth: 220
});
```	

## API Properties
	
### CenterWindow, leftWindow, rightWindow

A method that allow change of a window. You can either use a window or a navigation group. 
```javascript	
var newWin = Ti.UI.createWindow({
	backgroundColor: "#FF0000"
};
mainWindow.setCenterWindow(newWin);	
```

### LeftDrawerWidth, rightDrawerWidth

Update the width with these methods. The default width is `280`

```javascript
mainWindow.setLeftDrawerWidth(160);
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

## Changelog


* v1.0  
  * init


## Author

**Mads Moller**  
web: http://www.napp.dk  
email: mm@napp.dk  
twitter: @nappdev  


## License

    Copyright (c) 2010-2013 Mads Moller

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
