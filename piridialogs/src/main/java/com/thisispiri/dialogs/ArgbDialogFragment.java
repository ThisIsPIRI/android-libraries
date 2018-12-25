package com.thisispiri.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.thisispiri.components.ArgbSelector;

/**Lets the user input a color and gives the color to the {@code Activity} by calling {@link DialogListener#giveResult}. Gives {@code null} if the user cancels.
 * Call {@code setArguments()} with a {@code Bundle} that has an int with R.string.piri_dialogs_initialColor as the key to set the initial color.*/
public class ArgbDialogFragment extends ListenerDialogFragment {
	private ArgbSelector selector;
	/**Sets up the content(except buttons), but doesn't create the {@code Dialog} and instead returns the builder.
	 * @return An {@code AlertDialog.Builder} with an {@code ArgbFragment}. Contains the default value and message.*/
	public AlertDialog.Builder setUpContent() {
		AlertDialog.Builder builder = getBuilder();
		builder.setMessage(getString(R.string.piri_dialogs_selectColor));
		selector = new ArgbSelector(getContext());
		builder.setView(selector);
		//Get the default color specified by the arguments
		int defaultColor = 0;
		if(getArguments() != null)
			defaultColor = getArguments().getInt(getString(R.string.piri_dialogs_initialColor));
		selector.setColor(defaultColor);
		return builder;
	}
	@Override public Dialog onCreateDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = setUpContent();
		builder.setPositiveButton(R.string.piri_dialogs_confirm, (dialog, id) -> getListener().giveResult(selector.getColor(), getArguments()));
		builder.setNegativeButton(R.string.piri_dialogs_cancel, (dialog, id) -> getListener().<Integer>giveResult(null, getArguments()));
		return builder.create();
	}
	public int getColor() {return selector.getColor();}
	public void setColor(int color) {selector.setColor(color);}
}
