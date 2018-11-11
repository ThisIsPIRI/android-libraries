package com.thisispiri.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.app.AlertDialog;

/**Shows the string specified by message parameter in {@link DecisionDialogFragment#show} and notifies the {@code Activity} if the user agreed or not through {@link DialogListener#giveResult}.*/
public class DecisionDialogFragment extends ListenerDialogFragment {
	private String message;
	@Override public Dialog onCreateDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message);
		builder.setPositiveButton(R.string.piri_dialogs_agree, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				getListener().giveResult(true, getArguments());
			}
		});
		builder.setNegativeButton(R.string.piri_dialogs_reject, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				getListener().giveResult(false, getArguments());
			}
		});
		return builder.create();
	}
	/**Shows the {@code Dialog} with the message specified by message parameter.
	 * @param message The {@code String} containing the message.*/
	public void show(FragmentManager manager, String tag, String message) {
		this.message = message;
		show(manager, tag);
	}
}
