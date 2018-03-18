package com.thisispiri.dialogs;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.AlertDialog;
import android.widget.EditText;
/**Shows the message specified by the message parameter in {@link EditTextDialogFragment#show} and returns the text the user entered via {@code DialogListener.giveResult()}.
 * Also puts the message in the {@code Bundle} to it. Returns null if the user cancels.
 * Supply a {@code String} with resource id R.string.hintArgument to set the hint of the {@code EditText}.*/
public class EditTextDialogFragment extends ListenerDialogFragment {
    private String message = "Error", hint = "";
    @Override @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext, null));
        builder.setMessage(message);
        builder.setPositiveButton(message, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) {
            getArguments().putString(getString(R.string.piri_dialogs_messageArgument), message);
            getListener().giveResult(((EditText) ((AlertDialog) getDialog()).findViewById(R.id.fileName)).getText().toString(), getArguments());
        }});
        builder.setNegativeButton(R.string.piri_dialogs_cancel, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {
            getArguments().putString(getString(R.string.piri_dialogs_messageArgument), message);
            getListener().giveResult(null, getArguments());
        } });
        return builder.create();
    }
    @Override public void onStart() {
        super.onStart();
        ((EditText) ((AlertDialog) getDialog()).findViewById(R.id.fileName)).setHint(hint);
    }
    /**Same as {@link EditTextDialogFragment#show(FragmentManager, String, String, String)}, but without the hint.
     * @param message The {@code String} containing the message.*/
    public void show(FragmentManager manager, String tag, String message) {
        this.message = message;
        show(manager, tag);
    }
    /**Shows the {@code Dialog} with the message and the hint.
     * @param message The {@code String} containing the message.
     * @param hint The {@code String} containing the hint that will be displayed in the {@code EditText}.*/
    public void show(FragmentManager manager, String tag, String message, String hint) {
        this.message = message;
        this.hint = hint;
        show(manager, tag);
    }
}
