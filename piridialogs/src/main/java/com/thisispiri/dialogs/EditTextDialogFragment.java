package com.thisispiri.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

/**Shows the message specified by the message parameter in {@link EditTextDialogFragment#show} and returns the text the user entered via {@link DialogListener#giveResult}.
 * Also puts the message in the {@code Bundle} to it. Returns null if the user cancels.
 * The positive button will have the message as its text.*/
public class EditTextDialogFragment extends ListenerDialogFragment {
	private String hint = "";
	@Override @SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = getBuilder();
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext, null));
		builder.setPositiveButton(message, (dialog, id) -> {
			getArguments().putString(getString(R.string.piri_dialogs_messageArgument), message);
			getListener().giveResult(((EditText) getDialog().findViewById(R.id.fileName)).getText().toString(), getArguments());
		});
		builder.setNegativeButton(R.string.piri_dialogs_cancel, (dialog, id) -> {
			getArguments().putString(getString(R.string.piri_dialogs_messageArgument), message);
			getListener().giveResult(null, getArguments());
		});
		return builder.create();
	}
	@Override public void onStart() {
		super.onStart();
		((EditText) getDialog().findViewById(R.id.fileName)).setHint(hint);
	}
	/**Shows the {@code Dialog} with the message and the hint.
	 * @param message The {@code String} containing the message.
	 * @param hint The {@code String} containing the hint that will be displayed in the {@code EditText}.*/
	public void show(FragmentManager manager, String tag, String message, String hint) {
		this.hint = hint;
		show(manager, tag, message);
	}
}
