package com.thisispiri.dialogs;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**Shows multiple {@code CheckBox}s with questions supplied in {@link ChecksDialogFragment#show(FragmentManager, String, String, String[])}.
 * Gives a {@code boolean[]} containing whether the boxes were checked as the result, in the same order as your supplied questions.
 * The result will be {@code null} if the user cancels.*/
public class ChecksDialogFragment extends ListenerDialogFragment {
	private String[] questions;
	private int[] questionIds;
	private CheckBox[] boxes;
	private LinearLayout layout;
	@Override @SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = getBuilder();
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		builder.setView(layout);
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
		if(questions == null) {
			questions = new String[questionIds.length];
			for(int i = 0;i < questions.length;i++)
				questions[i] = getString(questionIds[i]);
		}
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
		show(manager, tag, message);
	}

	/**Shows the {@code Dialog}.
	 * @param questionIds The resource IDs for the questions.*/
	public void show(FragmentManager manager, String tag, String message, int[] questionIds) {
		this.questionIds = questionIds;
		show(manager, tag, message);
	}
}
