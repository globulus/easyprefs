package net.globulus.easyprefs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gordanglavas on 29/09/16.
 */

public interface Parceler<T extends Parcelable> {

	void writeToParcel(T source, Parcel dest, int flags);
	void readFromParcel(T target, Parcel in);
	Parcelable.Creator<T> getCreator();
}
