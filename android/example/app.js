
var leftMenuView = Ti.UI.createView({
	backgroundColor:'white',
	width: Ti.UI.FILL,
	height: Ti.UI.FILL
});

var centerView = Ti.UI.createView({
	backgroundColor:'white',
	width: Ti.UI.FILL,
	height: Ti.UI.FILL
});

var rightMenuView = Ti.UI.createView({
	backgroundColor:'#ddd',
	width: Ti.UI.FILL,
	height: Ti.UI.FILL
});


// create a menu
var leftTableView = Ti.UI.createTableView({
	font:{fontSize:12},
	rowHeight:40,
	data:[
		{title:'Toggle Left View'},
		{title:'Change Center Windowr'}, 
		{title:'Default Window'} 
	]
});
leftMenuView.add(leftTableView);
leftTableView.addEventListener("click", function(e){
	Ti.API.info("isAnyWindowOpen: " + drawer.isAnyWindowOpen());
	switch(e.index){
		case 0:
			drawer.toggleLeftWindow(); //animate back to center
			alert("You clicked " + e.rowData.title + ". Implement menu structure.. ");
			break;
		case 1:
			drawer.setCenterWindow(Ti.UI.createView({backgroundColor:"red"}));
			drawer.toggleLeftWindow(); //animate back to center
			break;
		case 2:
			drawer.setCenterWindow(centerView);
			drawer.toggleLeftWindow(); //animate back to center
			break;
	}
});


// Action Bar - FAKE example
var actionBar = Ti.UI.createView({
	top:0,
	height:"44dp",
	backgroundColor:"#333"
});
var leftToolbarBtn = Ti.UI.createButton({
	title:"Left",
	left: "6dp",
	backgroundColor:"transparent",
	color: "#FFF"
});
leftToolbarBtn.addEventListener("click", function(){
	drawer.toggleLeftWindow();
});
var rightToolbarBtn = Ti.UI.createButton({
	title:"Right",
	right: "6dp",
	backgroundColor:"transparent",
	color: "#FFF"
});
rightToolbarBtn.addEventListener("click", function(){
	drawer.toggleRightWindow();
});
var centerLabel = Ti.UI.createLabel({
	text:"NappDrawer",
	font:{
		fontSize:"14dp",
		fontWeight:"bold"
	},
	color: "#FFF"
});
actionBar.add(leftToolbarBtn);
actionBar.add(rightToolbarBtn);
actionBar.add(centerLabel);
centerView.add(actionBar);



// create interface
var scrollView = Ti.UI.createScrollView({
	layout:"vertical",
	left:0,right:0,top:"44dp",
    contentHeight:'auto',
    contentWidth:"100%",
    showVerticalScrollIndicator: true,
    showHorizontalScrollIndicator: false
});




var slider = Ti.UI.createSlider({
	top: "20dp", width: "280dp",
    min: 0, max: 1,
    value: 0.2
});
var label = Ti.UI.createLabel({
    text: "Parallax: " + slider.value,
    color:"#000",
    top: "15dp"
});
slider.addEventListener('touchend', function(e) {
	label.setText("Parallax: " + e.source.value);
    drawer.setParallaxAmount(e.source.value);
});
scrollView.add(label);
scrollView.add(slider);	

var gestureModeBtn = Ti.UI.createButton({title:"Gesture Mode: ALL", toggled:true, top:10});
gestureModeBtn.addEventListener("click", function(e){
	if(!e.source.toggled){
		var mode = "ALL";
		drawer.setOpenDrawerGestureMode(NappDrawerModule.OPEN_MODE_ALL);
	} else {
		var mode = "NONE";
		drawer.setOpenDrawerGestureMode(NappDrawerModule.OPEN_MODE_NONE);
	}
	gestureModeBtn.setTitle("Gesture Mode: " + mode);
	e.source.toggled = !e.source.toggled;
	
});
scrollView.add(gestureModeBtn);


function updateSlider(value){
	slider.value=value;
	slider.fireEvent("touchend", {source:{value:value}});
}

// animation mode
var animationMode = 0;
var animationModeBtn = Ti.UI.createButton({
	title:"Animation Mode: NONE", 
	top:10
});
var aniModeText;
animationModeBtn.addEventListener("click", function(e){
	if(animationMode == 3){
		animationMode = 0;
	} else {
		animationMode++;
	}
	switch(animationMode){
		case 0:
			drawer.setAnimationMode(NappDrawerModule.ANIMATION_NONE);
			updateSlider(0.2);
			aniModeText = "NONE";
			break;
		case 1:
			drawer.setAnimationMode(NappDrawerModule.ANIMATION_SLIDEUP);
			updateSlider(0);
			aniModeText = "SLIDEUP";
			break;
		case 2:
			drawer.setAnimationMode(NappDrawerModule.ANIMATION_ZOOM);
			updateSlider(0);
			aniModeText = "ZOOM";
			break;
		case 3:
			drawer.setAnimationMode(NappDrawerModule.ANIMATION_SCALE);
			updateSlider(0);
			aniModeText = "SCALE";
			break;
	}
	animationModeBtn.setTitle("Animation Mode: " + aniModeText);
});
scrollView.add(animationModeBtn);

centerView.add(scrollView);


// CREATE THE MODULE
var NappDrawerModule = require('dk.napp.drawer');
var drawer = NappDrawerModule.createDrawer({
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


drawer.addEventListener("didChangeOffset", function(e){
	//Ti.API.info("didChangeOffset: " + e.offset);
});

drawer.addEventListener("windowDidOpen", function(e){
	if(e.window == NappDrawerModule.LEFT_WINDOW) {
		Ti.API.info("windowDidOpen - LEFT DRAWER");
	} else if (e.window == NappDrawerModule.RIGHT_WINDOW) {
		Ti.API.info("windowDidOpen - RIGHT DRAWER");
	}
});
drawer.addEventListener("windowDidClose", function(e){
	Ti.API.info("windowDidClose");
});


// Action Bar - REAL example
drawer.addEventListener('open', onNavDrawerWinOpen);
function onNavDrawerWinOpen(evt) {
    this.removeEventListener('open', onNavDrawerWinOpen);

    if(this.getActivity()) {
        // need to explicitly use getXYZ methods
        var actionBar = this.getActivity().getActionBar();

        if (actionBar) {
            // Now we can do stuff to the actionbar  
            actionBar.setTitle('NappDrawer Example');
            
            // show an angle bracket next to the home icon,
            // indicating to users that the home icon is tappable
            actionBar.setDisplayHomeAsUp(true);

            // toggle the left window when the home icon is selected
            actionBar.setOnHomeIconItemSelected(function() {
                drawer.toggleLeftWindow();
           });
        }
    }    
}


// lets open it
drawer.open();
