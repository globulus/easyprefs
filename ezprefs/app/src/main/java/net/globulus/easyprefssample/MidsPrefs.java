package net.globulus.easyprefssample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;
import net.globulus.easyprefs.annotation.PrefFunction;

/**
 * Created by gordanglavas on 21/11/2017.
 */

@PrefClass
public class MidsPrefs {

    int proba1;
    final String proba2 = "asss";
    final long proba3 = 3;
    @Pref(clear = false, comment = "Proba 4", autoset = "true")
    boolean proba4;

    @Pref(function = JsonObjectString.class, rawDefaultValue = "(String) null")
    JsonObject jsonTest;

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
