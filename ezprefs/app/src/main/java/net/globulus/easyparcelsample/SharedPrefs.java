package net.globulus.easyparcelsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import net.globulus.easyprefs.annotation.EasyPrefsMaster;
import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;

/**
 * Created by gordanglavas on 21/11/2017.
 */

@PrefClass(autoInclude = false, staticClass = false)
public class SharedPrefs {

    private static SharedPreferences sSecureInstance;

    @EasyPrefsMaster
    public static SharedPreferences getSecureInstance(@NonNull Context context) {
        if (sSecureInstance == null) {
           sSecureInstance = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sSecureInstance;
    }

    int probaa1;
    @Pref
    String probaa2;
    @Pref(key = "test")
    long probaa3;
}
