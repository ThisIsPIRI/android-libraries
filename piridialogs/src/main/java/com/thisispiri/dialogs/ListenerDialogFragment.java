package com.thisispiri.dialogs;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;

/**The base class for {@code DialogFragment}s using {@link DialogListener}.*/
public class ListenerDialogFragment extends DialogFragment {
	private DialogListener listener;
	protected String message;
	@Override public void onAttach(final Context context) {
		super.onAttach(context);
		listener = (DialogListener) context;
	}
	public DialogListener getListener() {return listener;}
	/**Shows the Dialog with the {@code message}.*/
	public void show(FragmentManager manager, String tag, String message) {
		this.message = message;
		super.show(manager, tag);
	}
	/**Returns an {@code AlertDialog.Builder} with the message supplied in {@link ListenerDialogFragment#show(FragmentManager, String, String)}.
	 * Use this instead of constructing a new one yourself if you intend to use the aforementioned method.}.
	 * The message is null if no message was supplied in the method.*/
	protected AlertDialog.Builder getBuilder() {
		return new AlertDialog.Builder(getContext()).setMessage(message);
	}
}