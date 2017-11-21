package net.globulus.easyprefs;

import android.os.Parcelable;

import java.util.Set;

/**
 * Created by gordanglavas on 30/09/16.
 */
public interface ParcelerList {

	<T extends Parcelable> Parceler<T> getParcelerForClass(Class<T> clazz);

	Set<Class<? extends Parcelable>> getAllClasses();
}
