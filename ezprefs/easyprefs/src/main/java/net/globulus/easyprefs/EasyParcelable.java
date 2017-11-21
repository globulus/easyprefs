package net.globulus.easyprefs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gordanglavas on 25/04/17.
 */

public abstract class EasyParcelable implements Parcelable {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Parcelables.writeToParcel(this, dest, flags);
	}
}
