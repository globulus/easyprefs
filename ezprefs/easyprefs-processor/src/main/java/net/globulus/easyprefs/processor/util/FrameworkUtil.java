package net.globulus.easyprefs.processor.util;

public final class FrameworkUtil {

	public static final String IMPORT_SHARED_PREFERENCES = "android.content.SharedPreferences";
	public static final String IMPORT_CONTEXT = "android.content.Context";
	public static final String IMPORT_STRING = "java.lang.String";

	public static final String TYPE_SET_STRING = "Set<java.lang.String>";

	public static final String LINE_EDITOR_INIT = "SharedPreferences.Editor editor = getPrefs(context).edit()";
	public static final String LINE_EDITOR_COMMIT = "editor.commit()";


	public static final String PARAM_TARGET = "target";
	public static final String PARAM_SOURCE = "source";
	public static final String PARAM_FLAGS = "flags";
	public static final String PARAM_PARCEL = "parcel";

	private FrameworkUtil() { }

	public static String getEasyPrefsPackageName() {
		return "net.globulus.easyprefs";
	}

	public static String capitalize(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

}
