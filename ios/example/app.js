var NappDrawerModule = require('dk.napp.drawer');

function createAPIExampleWindow() {
  var win = Ti.UI.createWindow();

  var data = [{
      title: 'Toggle shadow'
    },
    {
      title: 'Toggle stretch drawer'
    },
    {
      title: 'Close Drawer'
    },
    {
      title: 'New Window'
    },
    {
      title: 'Default Window'
    },
    {
      title: 'Remove right Drawer'
    }
  ];

  var tableView = Ti.UI.createTableView({
    data: data
  });

  tableView.addEventListener('click', function(e) {
    Ti.API.info('isLeftWindowOpen: ' + drawer.isLeftWindowOpen());
    switch (e.index) {
      case 0:
        drawer.setShowShadow(!drawer.showShadow);
        break;
      case 1:
        drawer.setShouldStretchDrawer(!drawer.shouldStretchDrawer);
        break;
      case 2:
        drawer.toggleLeftWindow();
        break;
      case 3:
        var newWin = openNewNavWindow();
        drawer.setCenterWindow(newWin);
        drawer.toggleLeftWindow();
        break;
      case 4:
        drawer.setCenterWindow(createCenterNavWindow());
        drawer.toggleLeftWindow();
        break;
      case 5:
        drawer.setRightWindow(false);
        drawer.toggleLeftWindow();
        break;
    }
  });

  win.add(tableView);
  return win;
}


function openNewNavWindow() {
  var leftBtn = Ti.UI.createButton({
    title: 'Left'
  });
  leftBtn.addEventListener('click', function() {
    drawer.toggleLeftWindow();
  });
  var win = Ti.UI.createWindow({
    backgroundColor: '#222',
    translucent: true,
    extendEdges: [Ti.UI.EXTEND_EDGE_TOP],
    title: 'New Nav Window',
    barColor: '#FFA',
    tintColor: 'yellow',
    leftNavButton: leftBtn
  });

  var scrollView = Ti.UI.createScrollView({
    layout: 'vertical',
    left: 0,
    right: 0,
    contentHeight: 'auto',
    contentWidth: '100%',
    showVerticalScrollIndicator: true,
    showHorizontalScrollIndicator: false
  });

  for (var i = 0; i < 20; i++) {
    var label = Ti.UI.createLabel({
      top: 30,
      text: 'iOS7 is the new black',
      color: '#FFF',
      font: {
        fontSize: 20
      }
    });
    scrollView.add(label);
  }
  win.add(scrollView);
  var navController = Ti.UI.iOS.createNavigationWindow({
    window: win
  });
  return navController;
}


function createCenterNavWindow() {
  var leftBtn = Ti.UI.createButton({
    title: 'Left'
  });
  leftBtn.addEventListener('click', function() {
    drawer.toggleLeftWindow();
  });
  var rightBtn = Ti.UI.createButton({
    title: 'Right'
  });
  rightBtn.addEventListener('click', function() {
    drawer.toggleRightWindow();
  });

  var win = Ti.UI.createWindow({
    backgroundColor: '#eee',
    translucent: false,
    title: 'NappDrawer',
    barColor: '#ca2127',
    tintColor: '#ca2127',
    navTintColor: '#fff',
    titleAttributes: {
      color: '#fff'
    },
    leftNavButton: leftBtn,
    rightNavButton: rightBtn
  });

  var closeGestureMode = 1;
  var closeGestureModeBtn = Ti.UI.createButton({
    title: 'closeGestureMode: ALL',
    width: 300,
    top: 80
  });

  closeGestureModeBtn.addEventListener('click', function(e) {
    if (closeGestureMode == 2) {
      closeGestureMode = 0;
    } else {
      closeGestureMode++;
    }
    switch (closeGestureMode) {
      case 0:
        drawer.setCloseDrawerGestureMode(NappDrawerModule.CLOSE_MODE_TAP_CENTERWINDOW);
        closeGestureModeBtn.setTitle('closeGesture: Tap Center');
        break;
      case 1:
        drawer.setCloseDrawerGestureMode(NappDrawerModule.CLOSE_MODE_ALL);
        closeGestureModeBtn.setTitle('closeGesture: ALL');
        break;
      case 2:
        drawer.setCloseDrawerGestureMode(NappDrawerModule.CLOSE_MODE_PANNING_NAVBAR);
        closeGestureModeBtn.setTitle('closeGesture: NAVBAR');
        break;
    }
  });
  win.add(closeGestureModeBtn);


  var animationMode = 0;
  var animationModeBtn = Ti.UI.createButton({
    title: 'animation: NONE',
    width: 300,
    top: 140
  });
  animationModeBtn.addEventListener('click', function(e) {
    if (animationMode == 5) {
      animationMode = 0;
    } else {
      animationMode++;
    }
    switch (animationMode) {
      case 0:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_NONE);
        animationModeBtn.setTitle('animation: None');
        break;
      case 1:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_PARALLAX_FACTOR_3);
        animationModeBtn.setTitle('animation: Parallax factor 3');
        break;
      case 2:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_PARALLAX_FACTOR_7);
        animationModeBtn.setTitle('animation: Parallax factor 7');
        break;
      case 3:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_FADE);
        animationModeBtn.setTitle('animation: Fade');
        break;
      case 4:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_SLIDE);
        animationModeBtn.setTitle('animation: Slide');
        break;
      case 5:
        drawer.setAnimationMode(NappDrawerModule.ANIMATION_SLIDE_SCALE);
        animationModeBtn.setTitle('animation: Slide & Scale');
        break;
    }
  });
  win.add(animationModeBtn);


  var slider = Ti.UI.createSlider({
    top: 280,
    min: 50,
    max: 280,
    width: 280,
    value: 200
  });
  var label = Ti.UI.createLabel({
    text: 'Left Drawer Width: ' + slider.value,
    top: 250
  });
  slider.addEventListener('touchend', function(e) {
    var value = Math.round(e.value);
    label.setText('Left Drawer Width: ' + value);
    drawer.setLeftDrawerWidth(value);
  });
  win.add(label);
  win.add(slider);

  var navController = Ti.UI.iOS.createNavigationWindow({
    window: win
  });
  return navController;
}

var mainWindow = createCenterNavWindow();

var drawer = NappDrawerModule.createDrawer({
  leftWindow: createAPIExampleWindow(),
  centerWindow: mainWindow,
  rightWindow: Ti.UI.createWindow({
    backgroundColor: '#FFF'
  }),
  closeDrawerGestureMode: NappDrawerModule.CLOSE_MODE_ALL,
  openDrawerGestureMode: NappDrawerModule.OPEN_MODE_ALL,
  showShadow: false, //no shadow in iOS7
  leftDrawerWidth: 200,
  rightDrawerWidth: 120,
  statusBarStyle: NappDrawerModule.STATUSBAR_WHITE, // remember to set UIViewControllerBasedStatusBarAppearance to false in tiapp.xml
  orientationModes: [Ti.UI.PORTRAIT, Ti.UI.UPSIDE_PORTRAIT]
});

drawer.addEventListener('centerWindowDidFocus', function() {
  Ti.API.info('Center did focus!');
});

drawer.addEventListener('centerWindowDidBlur', function() {
  Ti.API.info('Center did blur!');
});

drawer.addEventListener('windowDidOpen', function(e) {
  Ti.API.info('windowDidOpen');
});

drawer.addEventListener('windowDidClose', function(e) {
  Ti.API.info('windowDidClose');
});

drawer.open();

Ti.API.info('isAnyWindowOpen: ' + drawer.isAnyWindowOpen());
