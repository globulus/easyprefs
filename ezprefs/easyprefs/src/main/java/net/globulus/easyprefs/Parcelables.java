package net.globulus.easyprefs;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.globulus.easyprefs.processor.util.FrameworkUtil;

/**
 * Created by gordanglavas on 29/09/16.
 */
public final class Parcelables {

	private static ParcelerList sParcelerList;

	private Parcelables() { }

	static void setParcelerList(ParcelerList parcelerList) {
		sParcelerList = parcelerList;
	}

	private static ParcelerList getParcelerList() {
		if (sParcelerList == null) {
			try {
				// Initiate class loading for the ParcelerList implementation class
				Class.forName(FrameworkUtil.getQualifiedName(FrameworkUtil.getParcelerListImplClassName()));
			} catch (ClassNotFoundException e) {
				throw new AssertionError(e);  // Can't happen
			}
		}
		return sParcelerList;
	}

	@Nullable
	private static Parceler getParcelerForClass(@NonNull Class clazz) {
		return getParcelerForClass(clazz, true);
	}

	@Nullable
	private static Parceler getParcelerForClass(@NonNull Class clazz, boolean retry) {
		Parceler parceler = getParcelerList().getParcelerForClass(clazz);
		if (parceler != null) {
			return parceler;
		}
		if (retry) {
			return getParcelerForClass(clazz, false);
		}
		return null;
	}

	public static <T extends Parcelable> void writeToParcel(@NonNull T object,
															@NonNull Parcel dest,
															int flags) {
		Class<?> clazz = object.getClass();
		do {
			Parceler parceler = getParcelerForClass(clazz);
			if (parceler != null) {
				parceler.writeToParcel(object, dest, flags);
				break;
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

	public static <T extends Parcelable> void readFromParcel(@NonNull T object,
															 @NonNull Parcel in) {
		Class<?> clazz = object.getClass();
		do {
			Parceler parceler = getParcelerForClass(clazz);
			if (parceler != null) {
				parceler.readFromParcel(object, in);
				break;
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

	public static <T extends Parcelable> Parcelable.Creator<T> getCreator(@NonNull Class<T> clazz) {
		Parceler<T> parceler = getParcelerForClass(clazz);
		if (parceler == null) {
			return null;
		}
		return parceler.getCreator();
	}

	public static class Utils {

		public static String printParcelerList() {
			StringBuilder sb = new StringBuilder();
			for (Class<? extends Parcelable> clazz : getParcelerList().getAllClasses()) {
				Parceler parceler = getParcelerForClass(clazz);
				sb.append(clazz.getName()).append(" => ");
				if (parceler == null) {
					sb.append("null");
				} else {
					sb.append(parceler.getClass().getName());
				}
				sb.append('\n');
			}
			return sb.toString();
		}
	}
}
