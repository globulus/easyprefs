package net.globulus.easyprefssample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import net.globulus.easyprefs.annotation.PrefMaster;
import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;

/**
 * Created by gordanglavas on 21/11/2017.
 */

// Add fields from this class to EasyPrefs, but only those annotated with @Pref.
// Add fields directly to EasyPrefs, and not inside an enclosing static class.
@PrefClass(autoInclude = false, staticClass = false)
public class SharedPrefs {

    private static SharedPreferences sSecureInstance;

    //
    // PrefMaster annotation is placed on a public static method returning an instance of
    // SharedPreferences, and this method will be used in generated code to retrieve the
    // SharedPreferences instance we want to work with.
    //
    @PrefMaster
    public static SharedPreferences getSecureInstance(@NonNull Context context) {
        if (sSecureInstance == null) {
           sSecureInstance = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sSecureInstance;
    }

    int test1; // This field won't be included

    @Pref
    String test2; // This field will be included.

    @Pref(key = "notTest3")
    long test3; // This field will be included with a custom key.
}
