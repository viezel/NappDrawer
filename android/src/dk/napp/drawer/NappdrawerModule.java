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

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.TiConfig;


@Kroll.module(name="Nappdrawer", id="dk.napp.drawer")
public class NappdrawerModule extends KrollModule
{
	private static final String LCAT = "NappDrawerModule";
	private static final boolean DBG = TiConfig.LOGD;

	// constants
	@Kroll.constant public static final int LEFT_WINDOW = 1;
	@Kroll.constant public static final int RIGHT_WINDOW = 3;
	@Kroll.constant public static final int CENTER_WINDOW = 2;
	
	@Kroll.constant public static final int OPEN_MODE_MARGIN = 0;
	@Kroll.constant public static final int OPEN_MODE_ALL = 1;
	@Kroll.constant public static final int OPEN_MODE_NONE = 2;
	
	@Kroll.constant public static final int CLOSE_MODE_MARGIN = 0;
	@Kroll.constant public static final int CLOSE_MODE_ALL = 1;
	@Kroll.constant public static final int CLOSE_MODE_NONE = 2;
	
	@Kroll.constant public static final int ANIMATION_NONE = 1;
	@Kroll.constant public static final int ANIMATION_SLIDEUP = 2;
	@Kroll.constant public static final int ANIMATION_ZOOM = 3;
	@Kroll.constant public static final int ANIMATION_SCALE = 4;
	
	public NappdrawerModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		// lets do nothing :)
	}

}

