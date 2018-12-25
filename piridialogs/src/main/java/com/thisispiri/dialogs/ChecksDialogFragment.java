package com.thisispiri.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

/**Shows multiple {@code CheckBox}s with questions supplied in {@link ChecksDialogFragment#show(FragmentManager, String, String, String[])}.
 * Gives a {@code boolean[]} containing whether the boxes were checked as result. It will be {@code null} if the user cancels.*/
public class ChecksDialogFragment extends ListenerDialogFragment {
	private String message;
	private String[] questions;
	private CheckBox[] boxes;
	private LinearLayout layout;
	@Override @SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		builder.setView(layout);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.piri_dialogs_confirm, (DialogInterface dialog, int id) -> {
			final boolean[] result = new boolean[boxes.length];
			for(int i = 0;i < boxes.length;i++)
				result[i] = boxes[i].isChecked();
			getListener().giveResult(result, getArguments());
		});
		builder.setNegativeButton(R.string.piri_dialogs_cancel, (DialogInterface dialog, int id) -> getListener().giveResult(null, getArguments()));
		return builder.create();
	}
	@Override public void onStart() {
		super.onStart();
		boxes = new CheckBox[questions.length];
		for(int i = 0;i < questions.length;i++) {
			boxes[i] = new CheckBox(getContext());
			boxes[i].setText(questions[i]);
			layout.addView(boxes[i]);
		}
	}
	/**Shows the {@code Dialog}.
	 * @param questions The questions.*/
	public void show(FragmentManager manager, String tag, String message, String[] questions) {
		this.questions = questions;
		this.message = message;
		show(manager, tag);
	}

	/**Shows the {@code Dialog}.
	 * @param questionIds The resource IDs for the questions.*/
	public void show(FragmentManager manager, String tag, String message, int[] questionIds) {
		String[] questions = new String[questionIds.length];
		for(int i = 0;i < questions.length;i++)
			questions[i] = getResources().getString(questionIds[i]);
		show(manager, tag, message, questions);
	}
}
