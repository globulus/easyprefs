package net.globulus.easyprefs.processor;

import java.util.List;

/**
 * Created by gordanglavas on 20/11/2017.
 */

public class PrefType {

    public final String name;
    public final List<PrefField> fields;
    public final boolean staticClass;

    public PrefType(String name, List<PrefField> fields, boolean staticClass) {
        this.name = name;
        this.fields = fields;
        this.staticClass = staticClass;
    }
}
