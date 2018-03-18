package com.thisispiri.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CreditsPreference extends DialogPreference {
	private final String text;
	public CreditsPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.credit_text);
		TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Credits, 0, 0);
		text = array.getString(R.styleable.Credits_text);
		array.recycle();
	}
	@Override
	public void onBindDialogView(final View v) {
		super.onBindDialogView(v);
		((TextView) v.findViewById(R.id.credit_text)).setText(text);
	}
}
