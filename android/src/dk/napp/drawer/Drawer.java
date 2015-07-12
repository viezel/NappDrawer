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

import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiBaseActivity.ConfigurationChangedListener;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.ActivityProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;

import org.appcelerator.titanium.proxy.TiWindowProxy;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.animation.Interpolator;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.slidingmenu.lib.SlidingMenu;

import chrisrenke.drawerarrowdrawable.DrawerArrowDrawable ;


public class Drawer extends TiUIView implements ConfigurationChangedListener{
	
	private SlidingMenu slidingMenu;
	private TiViewProxy leftView;
	private TiViewProxy rightView;
	private TiViewProxy centerView;
	
	private static final String TAG = "NappDrawer";
	private TiBaseActivity activity;
	private int leftMenuWidth;
	private int rightMenuWidth;
	
	// Used by DrawerArrowDrawable
	private float leftMenuOffset=0;
	private DrawerArrowDrawable drawerArrowDrawable;
	private boolean useHamburgerIcon = false;
	private boolean useArrowAnimation = false;
	private boolean useHamburgerIconCustomColor = false;
	int hamburgerIconColor = 0;
	
	// Static Properties
	public static final String PROPERTY_LEFT_VIEW = "leftWindow";
	public static final String PROPERTY_CENTER_VIEW = "centerWindow";
	public static final String PROPERTY_RIGHT_VIEW = "rightWindow";
	public static final String PROPERTY_LEFT_VIEW_WIDTH = "leftDrawerWidth";
	public static final String PROPERTY_RIGHT_VIEW_WIDTH = "rightDrawerWidth";
	public static final String PROPERTY_FADING = "fading";
	public static final String PROPERTY_MENU_SCROLL_SCALE = "parallaxAmount";
	public static final String PROPERTY_SHADOW_WIDTH = "shadowWidth";
	public static final String PROPERTY_OPEN_MODE = "openDrawerGestureMode";
	public static final String PROPERTY_CLOSE_MODE = "closeDrawerGestureMode";
	public static final String PROPERTY_ANIMATION_MODE = "animationMode";
	public static final String PROPERTY_HAMBURGER_ICON = "hamburgerIcon";
	public static final String PROPERTY_ARROW_ANIMATION = "arrowAnimation";
    public static final String PROPERTY_HAMBURGER_ICON_COLOR = "hamburgerIconColor";
    
	// for animations
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};
	
	
	public Drawer(final DrawerProxy proxy, TiBaseActivity activity)
	{
		super(proxy);
		this.activity = activity;
		activity.addConfigurationChangedListener(this);
        
		// configure the SlidingMenu
		slidingMenu = new SlidingMenu(activity);
		
		// set the drawerArrowIcon
		Resources resources = activity.getResources();
		drawerArrowDrawable = new DrawerArrowDrawable(resources);
		
		slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			@Override
			public void onClosed() {
				if (proxy.hasListeners("windowDidClose")) {
					KrollDict options = new KrollDict();
					proxy.fireEvent("windowDidClose", options);
				}
			}
		});
		
		slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen(int leftOrRight) {
				if (proxy.hasListeners("windowDidOpen")) {
					KrollDict options = new KrollDict();
					options.put("window", NappdrawerModule.LEFT_WINDOW);
					proxy.fireEvent("windowDidOpen", options);
				}
			}
		});
		
		slidingMenu.setSecondaryOnOpenListner(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen(int leftOrRight) {
				if (proxy.hasListeners("windowDidOpen")) {
					KrollDict options = new KrollDict();
					options.put("window", NappdrawerModule.RIGHT_WINDOW);
					proxy.fireEvent("windowDidOpen", options);
				}
			}
		});
		
		slidingMenu.setOnScrolledListener(new SlidingMenu.OnScrolledListener() {
			@Override
			public void onScrolled(int scroll) {
				if (proxy.hasListeners("didChangeOffset")) {
					KrollDict options = new KrollDict();
					options.put("offset", scroll);
					proxy.fireEvent("didChangeOffset", options);
				}
				if( useArrowAnimation){
					// Only the rightDrawer should convert the offset in 0-1 range for the drawerArrowAnimation.
					if (scroll <= 0f) {
						leftMenuOffset = 0f;
					}else{
						leftMenuOffset = (float) scroll/leftMenuWidth;
					}
					
		    	    if (leftMenuOffset >= .995) {
		    	      drawerArrowDrawable.setFlip(true);
		    	    } else if (leftMenuOffset <= .005) {
		    	      drawerArrowDrawable.setFlip(false);
		    	    }
		    	    drawerArrowDrawable.setParameter(leftMenuOffset);
				}
			}
		});
		
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		leftMenuWidth = -100;
		slidingMenu.setFadeDegree(0.0f);
		slidingMenu.setBehindScrollScale(0.0f);
		slidingMenu.setShadowWidth(0);
		
		updateMenuWidth();
		
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		
		// shadow
		int[] colors1 = {Color.argb(0, 0, 0, 0), Color.argb(50, 0, 0, 0)};
		GradientDrawable shadow = new GradientDrawable(Orientation.LEFT_RIGHT, colors1);		
		GradientDrawable shadowR = new GradientDrawable(Orientation.RIGHT_LEFT, colors1);		
		slidingMenu.setShadowDrawable(shadow);
		slidingMenu.setSecondaryShadowDrawable(shadowR);

		setNativeView(slidingMenu);
	}
	
	public SlidingMenu getSlidingMenu()
	{
		return slidingMenu;
	}
	
	private void updateMenuWidth()
	{
		if (leftMenuWidth > 0)
			slidingMenu.setBehindWidth(leftMenuWidth);
		else
			slidingMenu.setBehindOffset(-leftMenuWidth);
	}

	private void updateRightMenuWidth()
	{
		if (rightMenuWidth > 0)
			slidingMenu.setRightBehindWidth(rightMenuWidth);
		else
			slidingMenu.setRightBehindOffset(-rightMenuWidth);
	}

	
	public int getMenuWidth()
	{
		return leftMenuWidth;
	}
	
	private void updateCloseDrawerGestureMode(int mode)
	{
		if (mode == NappdrawerModule.CLOSE_MODE_ALL) {
			slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else if (mode == NappdrawerModule.CLOSE_MODE_MARGIN) {
			slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		} else if (mode == NappdrawerModule.CLOSE_MODE_NONE) {
			slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
		} else {
			slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
	}
	
	private void updateOpenDrawerGestureMode(int mode)
	{
		if (mode == NappdrawerModule.OPEN_MODE_ALL) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else if (mode == NappdrawerModule.OPEN_MODE_MARGIN) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		} else if (mode == NappdrawerModule.OPEN_MODE_NONE) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
	}
	
	private void updateAnimationMode(int mode)
	{
		if (mode == NappdrawerModule.ANIMATION_SCALE) {
			// scale
			slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {
					canvas.scale(percentOpen, 1, 0, 0);
				}			
			});	
		} else if (mode == NappdrawerModule.ANIMATION_SLIDEUP) {
			// slide
			slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {
					canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
				}			
			});
		} else if (mode == NappdrawerModule.ANIMATION_ZOOM) {
			// zoom animation
			slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {
					float scale = (float) (percentOpen*0.25 + 0.75);
					canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
				}
			});
		} else {
			// No animation
			slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {}
			});
		}
		
		// we need to reset the scrollScale when applying custom animations
		if( mode == NappdrawerModule.ANIMATION_SCALE || 
			mode == NappdrawerModule.ANIMATION_SLIDEUP ||
			mode == NappdrawerModule.ANIMATION_ZOOM){
			slidingMenu.setBehindScrollScale(0.0f);
		}
		
	}
	
	
	
	private void updateMenus() {
		if (this.leftView != null && this.rightView != null) {
			slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
			slidingMenu.setMenu((this.leftView).getOrCreateView().getOuterView());
			slidingMenu.setSecondaryMenu((this.rightView).getOrCreateView().getOuterView());
		}
		else if (this.rightView != null)
		{
			slidingMenu.setMode(SlidingMenu.RIGHT);
			slidingMenu.setMenu((this.rightView).getOrCreateView().getOuterView());
			slidingMenu.setSecondaryMenu(null);
		}
		else if (this.leftView != null)
		{
			slidingMenu.setMode(SlidingMenu.LEFT);
			slidingMenu.setMenu((this.leftView).getOrCreateView().getOuterView());
			slidingMenu.setSecondaryMenu(null);
		}
		else
		{
			slidingMenu.setMode(SlidingMenu.LEFT);
			slidingMenu.setMenu(null);
			slidingMenu.setSecondaryMenu(null);
		}
	}
	
	
	private void updateDrawerArrowDrawable() {
		if( useArrowAnimation || useHamburgerIcon ){
			if( useHamburgerIconCustomColor ){
				drawerArrowDrawable.setStrokeColor(hamburgerIconColor);
			}
			activity.getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);
			activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			activity.getSupportActionBar().setHomeAsUpIndicator(0);
		}
	}

	@Override
	public void processProperties(KrollDict d)
	{
		if (d.containsKey(TiC.PROPERTY_ACTIVITY)) {
			Object activityObject = d.get(TiC.PROPERTY_ACTIVITY);
			ActivityProxy activityProxy = getProxy().getActivityProxy();
			if (activityObject instanceof HashMap<?, ?> && activityProxy != null) {
				@SuppressWarnings("unchecked")
				KrollDict options = new KrollDict((HashMap<String, Object>) activityObject);
				activityProxy.handleCreationDict(options);
			}
		}
		if (d.containsKey(PROPERTY_LEFT_VIEW)) {
			Object leftView = d.get(PROPERTY_LEFT_VIEW);
			if (leftView != null && leftView instanceof TiViewProxy) {
				if (leftView instanceof TiWindowProxy)
					throw new IllegalStateException("[ERROR] Cannot use window as SlideMenu view");
				this.leftView = (TiViewProxy)leftView;
			} else {
				Log.e(TAG, "[ERROR] Invalid type for leftView");
			}
		}
		if (d.containsKey(PROPERTY_RIGHT_VIEW)) {
			Object rightView = d.get(PROPERTY_RIGHT_VIEW);
			if (rightView != null && rightView instanceof TiViewProxy) {
				if (rightView instanceof TiWindowProxy)
					throw new IllegalStateException("[ERROR] Cannot use window as SlideMenu view");
				this.rightView = (TiViewProxy)rightView;
			} else {
				Log.e(TAG, "[ERROR] Invalid type for rightView");
			}
		}
		
		if (d.containsKey(PROPERTY_CENTER_VIEW)) {
			Object centerView = d.get(PROPERTY_CENTER_VIEW);
			if (centerView != null && centerView instanceof TiViewProxy) {
				if (centerView instanceof TiWindowProxy)
					throw new IllegalStateException("[ERROR] Cannot use window as SlideMenu view");
				
				this.centerView = (TiViewProxy)centerView;
				TiCompositeLayout content = ((TiCompositeLayout) activity.getLayout());
				TiCompositeLayout.LayoutParams params = new TiCompositeLayout.LayoutParams();
				params.autoFillsHeight = true;
				params.autoFillsWidth = true;
				content.addView(((TiViewProxy)centerView).getOrCreateView().getOuterView(), params);						
			} else {
				Log.e(TAG, "[ERROR] Invalid type for centerView");
			}
		}

		
		updateMenus();	
		
		if(d.containsKey(PROPERTY_CLOSE_MODE)) {
			updateCloseDrawerGestureMode(TiConvert.toInt(d.get(PROPERTY_CLOSE_MODE)));
		}
		
		if(d.containsKey(PROPERTY_OPEN_MODE)) {
			updateOpenDrawerGestureMode(TiConvert.toInt(d.get(PROPERTY_OPEN_MODE)));
		}
		
		if (d.containsKey(PROPERTY_LEFT_VIEW_WIDTH)) {
			leftMenuWidth = getDevicePixels(d.get(PROPERTY_LEFT_VIEW_WIDTH)); 
			updateMenuWidth();
		}
		if (d.containsKey(PROPERTY_RIGHT_VIEW_WIDTH)) {
			rightMenuWidth = getDevicePixels(d.get(PROPERTY_RIGHT_VIEW_WIDTH));
			updateRightMenuWidth();
		}
		
		if (d.containsKey(PROPERTY_FADING)) {
			slidingMenu.setFadeDegree(d.getDouble(PROPERTY_FADING).floatValue());
		}
		if (d.containsKey(PROPERTY_MENU_SCROLL_SCALE)) {
			slidingMenu.setBehindScrollScale(d.getDouble(PROPERTY_MENU_SCROLL_SCALE).floatValue());
		}
		if (d.containsKey(PROPERTY_SHADOW_WIDTH)) {
			slidingMenu.setShadowWidth(getDevicePixels(d.get(PROPERTY_SHADOW_WIDTH)));
		}
		
		if (d.containsKey(PROPERTY_ANIMATION_MODE)) {
			updateAnimationMode(TiConvert.toInt(d.get(PROPERTY_ANIMATION_MODE)));
		}
		
        if (d.containsKey(PROPERTY_HAMBURGER_ICON)) {
        	useHamburgerIcon = TiConvert.toBoolean(d.get(PROPERTY_HAMBURGER_ICON));
        	updateDrawerArrowDrawable();
        }
        
        if (d.containsKey(PROPERTY_HAMBURGER_ICON_COLOR)) {
        	hamburgerIconColor = TiConvert.toColor(d.getString(PROPERTY_HAMBURGER_ICON_COLOR));
        	useHamburgerIconCustomColor = true;
        	updateDrawerArrowDrawable();
		 }
        
		if (d.containsKey(PROPERTY_ARROW_ANIMATION)) {
			useArrowAnimation = TiConvert.toBoolean(d.get(PROPERTY_ARROW_ANIMATION));
			updateDrawerArrowDrawable();
		}
		
		
		super.processProperties(d);
	}
	
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		Log.d(TAG, "Property: " + key + " old: " + oldValue + " new: " + newValue, Log.DEBUG_MODE);
		
		if (key.equals(PROPERTY_LEFT_VIEW)) {
			
			if (newValue == this.leftView) return;
			TiViewProxy newProxy = null;
			if (newValue != null && newValue instanceof TiViewProxy) {
				if (newValue instanceof TiWindowProxy)
					throw new IllegalStateException("Cannot use window as SlideMenu view");
				newProxy = (TiViewProxy)newValue;
			} else {
				Log.e(TAG, "Invalid type for leftView");
			}
			this.leftView = newProxy;
			updateMenus();
		} else if (key.equals(PROPERTY_RIGHT_VIEW)) {
			if (newValue == this.rightView) return;
			TiViewProxy newProxy = null;
			if (newValue != null && newValue instanceof TiViewProxy) {
				if (newValue instanceof TiWindowProxy)
					throw new IllegalStateException("Cannot use window as SlideMenu view");
				newProxy = (TiViewProxy)newValue;
			} else {
				Log.e(TAG, "Invalid type for rightView");
			}
			this.rightView = newProxy;
			updateMenus();
		} else if (key.equals(PROPERTY_CENTER_VIEW)) {
			if (newValue == this.centerView) return;
			TiCompositeLayout content = ((TiCompositeLayout) activity.getLayout());
			TiViewProxy newProxy = null;
			int index = 0;
			if (this.centerView != null) {
				index = content.indexOfChild(this.centerView.getOrCreateView().getNativeView());
			}
			if (newValue != null && newValue instanceof TiViewProxy) {
				if (newValue instanceof TiWindowProxy)
					throw new IllegalStateException("Cannot use window as SlideMenu view");
				newProxy = (TiViewProxy)newValue;
				TiCompositeLayout.LayoutParams params = new TiCompositeLayout.LayoutParams();
				params.autoFillsHeight = true;
				params.autoFillsWidth = true;
				content.addView(newProxy.getOrCreateView().getOuterView(), index, params);						
			} else {
				Log.e(TAG, "Invalid type for centerView");
			}
			if (this.centerView != null) {
				content.removeView(this.centerView.getOrCreateView().getNativeView());
			}
			this.centerView = newProxy;	
		} else if(key.equals(PROPERTY_CLOSE_MODE)) {
			updateCloseDrawerGestureMode(TiConvert.toInt(newValue));
		} else if(key.equals(PROPERTY_OPEN_MODE)) {
			updateOpenDrawerGestureMode(TiConvert.toInt(newValue));
		} else if (key.equals(PROPERTY_LEFT_VIEW_WIDTH)) {
			leftMenuWidth = getDevicePixels(newValue);
			updateMenuWidth();
		} else if (key.equals(PROPERTY_RIGHT_VIEW_WIDTH)) {
			rightMenuWidth = getDevicePixels(newValue);
			updateRightMenuWidth();
		} else if (key.equals(PROPERTY_FADING)) {
			slidingMenu.setFadeDegree(TiConvert.toFloat(newValue));
		} else if (key.equals(PROPERTY_MENU_SCROLL_SCALE)) {
			slidingMenu.setBehindScrollScale(TiConvert.toFloat(newValue));
		} else if (key.equals(PROPERTY_SHADOW_WIDTH)) {
			slidingMenu.setShadowWidth(getDevicePixels(newValue));
		} else if (key.equals(PROPERTY_ANIMATION_MODE)) {
			updateAnimationMode(TiConvert.toInt(newValue));
		} else if (key.equals(PROPERTY_HAMBURGER_ICON)) {
			useHamburgerIcon = TiConvert.toBoolean(newValue);
        	updateDrawerArrowDrawable();
		} else if (key.equals(PROPERTY_HAMBURGER_ICON_COLOR)) {
	        	hamburgerIconColor = TiConvert.toColor((String) newValue);
	        	useHamburgerIconCustomColor = true;
	        	updateDrawerArrowDrawable();
		} else if (key.equals(PROPERTY_ARROW_ANIMATION)) {
			useArrowAnimation = TiConvert.toBoolean(newValue);
			updateDrawerArrowDrawable();	        	
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	@Override
	public void onConfigurationChanged(TiBaseActivity activity, Configuration newConfig) {
		updateMenuWidth();
	}
	
	/**
	 * Napp: "100dp" to pixels
	 * @param value
	 * @return pixels
	 */
	public int getDevicePixels(Object value){
		return TiConvert.toTiDimension(TiConvert.toString(value), TiDimension.TYPE_WIDTH).getAsPixels(slidingMenu);
	}

}
