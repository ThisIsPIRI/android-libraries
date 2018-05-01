package com.thisispiri.preferences;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import com.thisispiri.fragments.ArgbSelector;
/**Saves the color input by the user in the key.*/
public class ArgbPreference extends DialogPreference {
	private ArgbSelector selector;
	public ArgbPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.piri_preferences_argb_selector);
	}
	@Override
	public void onBindDialogView(final View v) {
		super.onBindDialogView(v);
		selector = v.findViewById(R.id.piri_preferenes_argbSelector);
		selector.setColor(getPersistedInt(0xFFFFFFFF));
	}
	@Override protected void onSetInitialValue(final boolean restorePersistedValue, final Object defaultValue) {
		if(!restorePersistedValue) persistInt((Integer)defaultValue); //get default value
	}
	@Override protected Object onGetDefaultValue(final TypedArray a, final int index) {
		return a.getInteger(index, 0xFFFFFF);
	}
	@Override protected void onDialogClosed(final boolean positiveResult) {
		if (positiveResult) {
			if(callChangeListener(selector.getColor())) persistInt(selector.getColor());
		}
		super.onDialogClosed(positiveResult);
	}
}