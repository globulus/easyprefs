package net.globulus.easyprefs.annotation;

/**
 * Created by gordanglavas on 28/11/2017.
 */

public interface PrefFunction<T, R> {
    R put(T t);
    T get(R r);
}
