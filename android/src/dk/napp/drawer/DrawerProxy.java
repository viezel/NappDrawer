/**
 * Copyright (c) 2010-2013 by Napp ApS
 * www.napp.dk
 * Author Mads Møller
 * 
 * Special thanks to Martin Guillon
 *
 * Appcelerator Titanium is Copyright (c) 2009-2013 by Appcelerator, Inc.
 * and licensed under the Apache Public License (version 2)
 */

package dk.napp.drawer;

import java.lang.ref.WeakReference;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiActivity;
import org.appcelerator.titanium.TiActivityWindow;
import org.appcelerator.titanium.TiActivityWindows;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiWindowProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiUIHelper;

import com.slidingmenu.lib.SlidingMenu;

import dk.napp.drawer.Drawer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

@Kroll.proxy(creatableInModule=NappdrawerModule.class)
public class DrawerProxy extends TiWindowProxy implements TiActivityWindow
{
	private static final String TAG = "NappDrawerProxy";

	private static final int MSG_FIRST_ID = TiWindowProxy.MSG_LAST_ID + 1;

	private static final int MSG_TOGGLE_LEFT_VIEW = MSG_FIRST_ID + 100;
	private static final int MSG_TOGGLE_RIGHT_VIEW = MSG_FIRST_ID + 101;
	private static final int MSG_OPEN_LEFT_VIEW = MSG_FIRST_ID + 102;
	private static final int MSG_OPEN_RIGHT_VIEW = MSG_FIRST_ID + 103;
	private static final int MSG_CLOSE_LEFT_VIEW = MSG_FIRST_ID + 104;
	private static final int MSG_CLOSE_RIGHT_VIEW = MSG_FIRST_ID + 105;
	private static final int MSG_CLOSE_VIEWS = MSG_FIRST_ID + 106;

	protected static final int MSG_LAST_ID = MSG_FIRST_ID + 999;
	
	private WeakReference<Activity> slideMenuActivity;
	private WeakReference<SlidingMenu> slidingMenu;

	public DrawerProxy()
	{
		super();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what) {
			case MSG_TOGGLE_LEFT_VIEW: {
				handleToggleLeftView((Boolean)msg.obj);
				return true;
			}
			case MSG_TOGGLE_RIGHT_VIEW: {
				handleToggleRightView((Boolean)msg.obj);
				return true;
			}
			case MSG_OPEN_LEFT_VIEW: {
				handleOpenLeftView((Boolean)msg.obj);
				return true;
			}
			case MSG_OPEN_RIGHT_VIEW: {
				handleOpenRightView((Boolean)msg.obj);
				return true;
			}
			case MSG_CLOSE_LEFT_VIEW: {
				handleCloseLeftView((Boolean)msg.obj);
				return true;
			}
			case MSG_CLOSE_RIGHT_VIEW: {
				handleCloseRightView((Boolean)msg.obj);
				return true;
			}
			case MSG_CLOSE_VIEWS: {
				handleCloseViews((Boolean)msg.obj);
				return true;
			}
			default : {
				return super.handleMessage(msg);
			}
		}
	}

	@Override
	public void handleCreationDict(KrollDict options) {
		super.handleCreationDict(options);

		// Support setting orientation modes at creation.
		Object orientationModes = options.get( TiC.PROPERTY_ORIENTATION_MODES);
		if (orientationModes != null && orientationModes instanceof Object[]) {
			try {
				int[] modes = TiConvert.toIntArray((Object[]) orientationModes);
				setOrientationModes(modes);

			} catch (ClassCastException e) {
				Log.e(TAG, "Invalid orientationMode array. Must only contain orientation mode constants.");
			}
		}
	}


	@Override
	protected void handleOpen(KrollDict options)
	{
		Activity topActivity = TiApplication.getAppCurrentActivity();

		if (topActivity == null || topActivity.isFinishing()) {
			Log.w(TAG, "Unable to open drawer. Activity is null");
			return;
		}		

		Intent intent = new Intent(topActivity, TiActivity.class);
		fillIntent(topActivity, intent);

		int windowId = TiActivityWindows.addWindow(this);
		intent.putExtra(TiC.INTENT_PROPERTY_USE_ACTIVITY_WINDOW, true);
		intent.putExtra(TiC.INTENT_PROPERTY_WINDOW_ID, windowId);

		topActivity.startActivity(intent);
	}

	@Override
	public void windowCreated(TiBaseActivity activity, Bundle savedInstanceState) {
		slideMenuActivity = new WeakReference<Activity>(activity);
		activity.setWindowProxy(this);
		setActivity(activity);
		view = new Drawer(this, activity);
		slidingMenu = new WeakReference<SlidingMenu>(((Drawer)view).getSlidingMenu());
		setModelListener(view);

		handlePostOpen();

		// Push the tab group onto the window stack. It needs to intercept
		// stack changes to properly dispatch tab focus and blur events
		// when windows open and close on top of it.
		activity.addWindowToStack(this);
	}

	@Override
	public void handlePostOpen()
	{
		super.handlePostOpen();

		opened = true;

		// First open before we load and focus our first tab.
		fireEvent(TiC.EVENT_OPEN, null);

		// Setup the new tab activity like setting orientation modes.
		onWindowActivityCreated();
	}

	@Override
	protected void handleClose(KrollDict options)
	{
		Log.d(TAG, "handleClose: " + options, Log.DEBUG_MODE);
		
		modelListener = null;
		releaseViews();
		view = null;

		opened = false;

		Activity activity = slideMenuActivity.get();
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
		}
		
	}
	
	@Override
	public void closeFromActivity(boolean activityIsFinishing) {
		// Call super to fire the close event on the tab group.
		// This event must fire after each tab has been closed.
		super.closeFromActivity(activityIsFinishing);
	}
	
	@Override
	public void onWindowFocusChange(boolean focused) {
		if (focused){
			fireEvent(TiC.EVENT_FOCUS, null);
		} else {
			fireEvent(TiC.EVENT_BLUR, null);
		}
	}

	private void fillIntent(Activity activity, Intent intent)
	{
		if (hasProperty(TiC.PROPERTY_FULLSCREEN)) {
			intent.putExtra(TiC.PROPERTY_FULLSCREEN, TiConvert.toBoolean(getProperty(TiC.PROPERTY_FULLSCREEN)));
		}
		if (hasProperty(TiC.PROPERTY_WINDOW_SOFT_INPUT_MODE)) {
			intent.putExtra(TiC.PROPERTY_WINDOW_SOFT_INPUT_MODE, TiConvert.toInt(getProperty(TiC.PROPERTY_WINDOW_SOFT_INPUT_MODE)));
		}

		if (hasProperty(TiC.PROPERTY_EXIT_ON_CLOSE)) {
			intent.putExtra(TiC.INTENT_PROPERTY_FINISH_ROOT, TiConvert.toBoolean(getProperty(TiC.PROPERTY_EXIT_ON_CLOSE)));
		} else {
			intent.putExtra(TiC.INTENT_PROPERTY_FINISH_ROOT, activity.isTaskRoot());
		}
	}

	@Override
	public TiBlob handleToImage()
	{
		KrollDict d = TiUIHelper.viewToImage(new KrollDict(), getActivity().getWindow().getDecorView());
		return TiUIHelper.getImageFromDict(d);
	}

	@Override
	public void releaseViews()
	{
		super.releaseViews();
	}

	@Override
	protected Activity getWindowActivity()
	{
		return (slideMenuActivity != null) ? slideMenuActivity.get() : null;
	}
	
	private void handleToggleLeftView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		menu.toggle(animated);
	}
	
	private void handleToggleRightView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		menu.toggleSecondary(animated);
	}
	
	private void handleOpenLeftView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		menu.showMenu(animated);
	}
	
	private void handleOpenRightView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		menu.showSecondaryMenu(animated);
	}
	
	private void handleCloseLeftView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		if (menu.isMenuShowing())
			menu.showContent(animated);
	}
	
	private void handleCloseRightView(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		if (menu.isSecondaryMenuShowing())
			menu.showContent(animated);
	}
	
	private void handleCloseViews(boolean animated)
	{
		SlidingMenu menu = slidingMenu.get();
		if (menu.isMenuShowing() || menu.isSecondaryMenuShowing())
			menu.showContent(animated);
	}
	
	public SlidingMenu getSlidingMenu(){
		return slidingMenu.get();
	}
	
	/*
	 * METHODS
	 */
	
	@Kroll.method
	public void toggleLeftWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleToggleLeftView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_TOGGLE_LEFT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void toggleRightWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleToggleRightView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_TOGGLE_RIGHT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void openLeftWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleOpenLeftView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_OPEN_LEFT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void openRightWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleOpenRightView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_OPEN_RIGHT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void closeLeftWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleCloseLeftView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_CLOSE_LEFT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void closeRightWindow(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleCloseRightView(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_CLOSE_RIGHT_VIEW, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public void closeWindows(@Kroll.argument(optional = true) Object obj)
	{
		Boolean animated = true;
		if (obj != null) {
			animated = TiConvert.toBoolean(obj);
		}
		
		if (TiApplication.isUIThread()) {
			handleCloseViews(animated);
			return;
		}
		Message message = getMainHandler().obtainMessage(MSG_CLOSE_VIEWS, animated);
		message.sendToTarget();
	}
	
	@Kroll.method
	public boolean isLeftWindowOpen()
	{
		return getSlidingMenu().isMenuShowing();
	}
	
	@Kroll.method
	public boolean isRightWindowOpen()
	{
		return getSlidingMenu().isSecondaryMenuShowing();
	}
	
	@Kroll.method
	public boolean isAnyWindowOpen()
	{
		SlidingMenu menu = getSlidingMenu();
		return menu.isSecondaryMenuShowing() || menu.isMenuShowing();
	}
	
	@Kroll.method
	public int getRealLeftViewWidth()
	{
		SlidingMenu menu = slidingMenu.get();
		return menu.getBehindOffset();
	}
	
	@Kroll.method
	public int getRealRightViewWidth()
	{
		return getSlidingMenu().getBehindOffset();
	}
	
	/*
	 * PROPERTIES
	 */
	
	@Kroll.method @Kroll.setProperty
	@Override
	public void setOrientationModes(int[] modes) {
		// Unlike Windows this setter is not defined in JavaScript.
		// We need to expose it here with an annotation.
		super.setOrientationModes(modes);
	}
	
	// Parallax
	@Kroll.method @Kroll.getProperty
	public float getParallaxAmount() {
		SlidingMenu menu = slidingMenu.get();
		return menu.getBehindScrollScale();
	}
	
	@Kroll.method @Kroll.setProperty
	public void setParallaxAmount(Object value){
		SlidingMenu menu = slidingMenu.get();
		menu.setBehindScrollScale(TiConvert.toFloat(value));
	}
	
	@Kroll.method @Kroll.setProperty
	public void setFading(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_FADING, arg);
	}
	
	// window setters..
	@Kroll.method @Kroll.setProperty
	public void setLeftWindow(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_LEFT_VIEW, arg);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setRightWindow(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_RIGHT_VIEW, arg);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setCenterWindow(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_CENTER_VIEW, arg);
	}
	
	// Drawer width
	@Kroll.method @Kroll.setProperty
	public void setLeftDrawerWidth(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_LEFT_VIEW_WIDTH, arg);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setRightDrawerWidth(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_RIGHT_VIEW_WIDTH, arg);
	}
	
	// Shadow
	@Kroll.method @Kroll.setProperty
	public void setShadowWidth(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_SHADOW_WIDTH, arg);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setOpenDrawerGestureMode(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_OPEN_MODE, arg);
	}
	
	// Gesture & animation modes
	@Kroll.method @Kroll.setProperty
	public void setCloseDrawerGestureMode(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_CLOSE_MODE, arg);
	}
	
	@Kroll.method @Kroll.setProperty
	public void setAnimationMode(Object arg){
		setPropertyAndFire(Drawer.PROPERTY_ANIMATION_MODE, arg);
	}
	
	
	@Kroll.method
	@Kroll.setProperty
	public void setHamburgerIcon(Object arg) {
		setPropertyAndFire(Drawer.PROPERTY_HAMBURGER_ICON, arg);
	}
	
	@Kroll.method
	@Kroll.setProperty
	public void setHamburgerIconColor(Object arg) {
		setPropertyAndFire(Drawer.PROPERTY_HAMBURGER_ICON_COLOR, arg);
	}	
	
	@Kroll.method
	@Kroll.setProperty
	public void setArrowAnimation(Object arg) {
		setPropertyAndFire(Drawer.PROPERTY_ARROW_ANIMATION, arg);
	}	
}
