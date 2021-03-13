package com.thisispiri.common.andr;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import androidx.annotation.StringRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class AndrUtil {
	/**Obtains a {@code File} called {@code fileName} from {@code directoryName} under the external storage.
	 * @param directoryName The name of the directory, right under the external storage. Subdirectories are supported.
	 * @param fileName The filename of the file to obtain.
	 * @param allowCreation If true, will create a new, empty file in case a file called {@code fileName} doesn't exist.*/
	public static File getFile(String directoryName, String fileName, boolean allowCreation) throws IOException {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			throw new IOException("External storage not mounted");
		}
		final File directory = Environment.getExternalStoragePublicDirectory(directoryName);
		final File file = new File(directory, fileName);
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Couldn't find or make requested directory");
		}
		if (!file.exists()) {
			if (!allowCreation || !file.createNewFile()) throw new IOException("Requested file didn't exist and creation failed or was disabled");
		}
		return file;
	}

	/**Shows a short `Toast` `saying` something. Safe to call from any thread.
	 * @param inActivity the `Activity` to display the `Toast` in.
	 * @param saying The `String` to display.
	 * @param length The length of the `Toast`. Must either be `Toast.LENGTH_SHORT` or `Toast.LENGTH_LONG`. Defaults to `LENGTH_SHORT`.*/
	public static void showToast(Activity inActivity, String saying, int length) {
		if (Looper.myLooper() != Looper.getMainLooper())
			inActivity.runOnUiThread(() -> AndrUtil.showToast(inActivity, saying));
		else
			Toast.makeText(inActivity, saying, length).show();
	}
	public static void showToast(Activity inActivity, String saying) {
		showToast(inActivity, saying, Toast.LENGTH_SHORT);
	}

	/**@see AndrUtil#showToast(Activity, String, int)
	 * @param saying The resource ID of the string to show.*/
	public static void showToast(Activity inActivity, @StringRes int saying, int length) {
		showToast(inActivity, inActivity.getString(saying), length);
	}
	public static void showToast(Activity inActivity, @StringRes int saying) {
		showToast(inActivity, inActivity.getString(saying), Toast.LENGTH_SHORT);
	}


	/**Changes the status of the `RadioButton` without alerting its `group`'s `OnCheckedChangeListener`.
	 * Note that this function doesn't touch any listener attached to the `button` itself.
	 * @param group The `RadioGroup` `button` is in.
	 * @param button The `RadioButton` to click.
	 * @param listener The `OnCheckedChangeListener` to attach to the `group` after clicking.
	 * @param to If `true`, `button` will be checked. If `false`, the opposite.*/
	public static void hiddenClick(RadioGroup group, RadioButton button, RadioGroup.OnCheckedChangeListener listener, Boolean to) {
		group.setOnCheckedChangeListener(null);
		button.setChecked(to);
		group.setOnCheckedChangeListener(listener);
	}

	/**Puts `these` in a `Bundle` and returns it.
	 * @param these A vararg. The `String`s in even indices are the keys and the ones in odd indices the values.
	 * In other words, the `Bundle` will contain (these[0], these[1]), (these[2], these[3]), ... (these[size - 2] these[size - 1]).
	 * The number of arguments should be even; if it is odd, the last one will be ignored.
	 * @return The `Bundle`. An empty one if less than 2 arguments are supplied.*/
	public static Bundle bundleWith(String ... these) {
		Bundle result = new Bundle();
		for(int i = 1;i < these.length;i += 2) {
			result.putString(these[i - 1], these[i]);
		}
		return result;
	}

	/**Sees if the `permission` is granted to the `Activity` and, if it isn't, requests that it be.
	 * Override `onRequestPermissionsResult` in the `Activity` to be notified if the request was granted.
	 * @param activity The `Activity` that should have the permission.
	 * @param permission The permission to check for/request.
	 * @param requestCode The request code to use in case the permission isn't granted.
	 * @param rationaleId The Android resource ID of the rationale string.
	 * @return Whether the permission was already granted at the time of call.*/
	public static boolean getPermission(Activity activity, String permission, int requestCode, int rationaleId){
		if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
			if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				showToast(activity, rationaleId);
			}
			ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
			return false;
		}
		else return true;
	}
}
