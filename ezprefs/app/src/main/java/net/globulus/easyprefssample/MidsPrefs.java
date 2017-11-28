package net.globulus.easyprefssample;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;

/**
 * Created by gordanglavas on 21/11/2017.
 */

@PrefClass
public class MidsPrefs {

    int proba1;
    final String proba2 = "asss";
    final long proba3 = 3;
    @Pref(clear = false)
    boolean proba4;
}