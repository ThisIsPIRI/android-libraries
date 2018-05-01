package com.thisispiri.dialogs;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
/**The base class for {@code DialogFragment}s using {@link DialogListener}.*/
public class ListenerDialogFragment extends DialogFragment {
	private DialogListener listener;
	@Override public void onAttach(final Context context) {
		super.onAttach(context);
		listener = (DialogListener) context;
	}
	@Override public void onAttach(final Activity activity) { //Backward compatibility with API levels under 23
		super.onAttach(activity);
		listener = (DialogListener) activity;
	}
	protected DialogListener getListener() {return listener;}
}