package com.thisispiri.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;

/**Shows the string specified by message parameter in {@link DecisionDialogFragment#show} and tells the {@code Activity} if the user agreed or not through {@link DialogListener#giveResult}.*/
public class DecisionDialogFragment extends ListenerDialogFragment {
	private String message, positive, negative;
	@Override public Dialog onCreateDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = getBuilder();
		if(positive == null) positive = getString(R.string.piri_dialogs_agree);
		if(negative == null) negative = getString(R.string.piri_dialogs_reject);
		builder.setPositiveButton(positive, (dialog, id) -> getListener().giveResult(true, getArguments()));
		builder.setNegativeButton(negative, (dialog, id) -> getListener().giveResult(false, getArguments()));
		return builder.create();
	}
	/**Shows the {@code Dialog} with the message specified by message parameter.
	 * You may pass {@code null} to {@code positive} or {@code negative}; they will default to
	 * {@code R.string.piri_dialogs_agree} and {@code R.string.piri_dialogs_reject} respectively.
	 * @param message The {@code String} containing the message.
	 * @param positive The {@code String} on the button for a positive response.
	 * @param negative The {@code String} on the button for a negative response.*/
	public void show(FragmentManager manager, String tag, String message, String positive, String negative) {
		this.positive = positive;
		this.negative = negative;
		show(manager, tag, message);
	}
}
