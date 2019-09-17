package net.globulus.easyprefs;

import android.content.Context;

import java.util.Set;

public abstract class IEasyPrefs {

    private static IEasyPrefs sInstance;

    static void setInstance(IEasyPrefs instance) {
        sInstance = instance;
    }

    public static IEasyPrefs get() {
        return sInstance;
    }

    public abstract void putPreferencesField(Context context, String key, int value);
    public abstract void putPreferencesField(Context context, String key, long value);
    public abstract void putPreferencesField(Context context, String key, float value);
    public abstract void putPreferencesField(Context context, String key, boolean value);
    public abstract void putPreferencesField(Context context, String key, String value);
    public abstract void putPreferencesField(Context context, String key, Set<String> value);

    public abstract int getPreferencesField(Context context, String key, int defaultValue);
    public abstract long getPreferencesField(Context context, String key, long defaultValue);
    public abstract float getPreferencesField(Context context, String key, float defaultValue);
    public abstract boolean getPreferencesField(Context context, String key, boolean defaultValue);
    public abstract String getPreferencesField(Context context, String key, String defaultValue);
    public abstract Set<String> getPreferencesField(Context context, String key, Set<String> defaultValue);

    public abstract void addToPreferencesField(Context context, String key, String value);
    public abstract void removeFromPreferencesField(Context context, String key, String value);

    public abstract void removePreferencesField(Context context, String key);
    public abstract void clearAll(Context context);
}
