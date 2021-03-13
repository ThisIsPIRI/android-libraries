package com.thisispiri.dialogs;

import android.os.Bundle;

/**Have your {@code Activity} implement this interface to receive results from various {@code DialogFragment}s.
 * It is recommended for implementations to put something in the arguments {@code Bundle} to know what {@code DialogFragment} called {@link DialogListener#giveResult}.
 * {@code DialogFragment}s that call {@link DialogListener#giveResult} are recommended to pass the aforementioned arguments {@code Bundle} as the second argument of it.*/
public interface DialogListener {
	/**{@code DialogFragment}s will call this method to deliver their results to the {@code Activity} that called them before closing.
	 * {@code DialogFragment}s should only call this once, and immediately dismiss themselves afterwards. <b>Do not</b> call this without dismissing the Dialog.
	 * @param result The result of the {@code Dialog}.
	 * @param arguments The {@code Bundle} delivered to the {@code DialogFragment} by calling {@code setArgument()}. {@code DialogFragments} are recommended to pass the arguments {@code Bundle} to this.*/
	<T>void giveResult(T result, Bundle arguments);
}
