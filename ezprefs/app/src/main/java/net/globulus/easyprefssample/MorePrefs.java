package net.globulus.easyprefssample;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.globulus.easyprefs.EasyPrefs;
import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;
import net.globulus.easyprefs.annotation.PrefFunction;
import net.globulus.easyprefs.annotation.PrefMethod;

/**
 * Created by gordanglavas on 21/11/2017.
 */

// Automatically add all fields from this class to EasyPrefs.
// Make an in inner static class in EasyPrefs to group these fields.
@PrefClass
public class MorePrefs {

    int intField;
    final String stringFieldWithDefaultValue = "asss";
    final long longFieldWithDefaultValue = 3;

    // Don't add this field to clear() or clearAll() methods
    // Add a comment above its getter and setter
    // When invoking put, autoset the value to true.
    @Pref(clear = false, comment = "Proba 4", autoset = "true")
    boolean dontClearAddCommentAutoSetToTrue;

    // Classes that don't go into SharedPreferences by default can be added to it
    // via mapper classes, which transform the custom class into one of the
    // SharedPreferences-friendly types.
    @Pref(function = JsonObjectString.class, rawDefaultValue = "(String) null")
    JsonObject jsonTest;

    @PrefMethod
    public static void putThisPref(Context context, int pref) {
        EasyPrefs.putPreferencesField(context, "thisPref", pref);
    }

    @PrefMethod
    public static int getThisPref(Context context) {
        return EasyPrefs.getPreferencesField(context, "thisPref", 1);
    }

    public static class JsonObjectString implements PrefFunction<JsonObject, String> {

        @Override
        public String put(JsonObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }
            return jsonObject.toString();
        }

        @Override
        public JsonObject get(String s) {
            if (s == null) {
                return null;
            }
            return new Gson().fromJson(s, JsonObject.class);
        }
    }
}
