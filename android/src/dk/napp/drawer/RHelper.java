package dk.napp.drawer;

import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;

public class RHelper {

	public static int getString(String str) {
		try {
			return TiRHelper.getApplicationResource("string." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getLayout(String str) {
		try {
			return TiRHelper.getApplicationResource("layout." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getId(String str) {
		try {
			return TiRHelper.getApplicationResource("id." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getDrawable(String str) {
		try {
			return TiRHelper.getApplicationResource("drawable." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getColor(String str) {
		try {
			return TiRHelper.getApplicationResource("color." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getRaw(String str) {
		try {
			return TiRHelper.getApplicationResource("raw." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getXML(String str) {
		try {
			return TiRHelper.getApplicationResource("xml." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getAnimator(String str) {
		try {
			return TiRHelper.getApplicationResource("animator." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
