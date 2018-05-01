package com.thisispiri.preferences;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
/**Saves two integers. Supply firstKey, firstText, firstDefault, secondKey, secondText, secondDefault in the preference xml instead of the regular attributes.*/
public class TwoNumberPreference extends DialogPreference {
	private final int limit;
	private EditText firstEdit, secondEdit;
	private int firstDefault, secondDefault;
	private String firstKey, secondKey, firstText, secondText;
	private static final int LIMIT_DIVISOR = 4;
	public TwoNumberPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.two_text);
		TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TwoNumberPreference, 0, 0);
		firstKey = array.getString(R.styleable.TwoNumberPreference_firstKey); firstText = array.getString(R.styleable.TwoNumberPreference_firstText); firstDefault = array.getInt(R.styleable.TwoNumberPreference_firstDefault, 15);
		secondKey = array.getString(R.styleable.TwoNumberPreference_secondKey); secondText = array.getString(R.styleable.TwoNumberPreference_secondText); secondDefault = array.getInt(R.styleable.TwoNumberPreference_secondDefault, 15);
		array.recycle();
		array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Number, 0, 0);
		if(array.getBoolean(R.styleable.Number_imposeScreenLimit, true)) {
			Point size = new Point();
			((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
			limit = size.x / LIMIT_DIVISOR + 1;
		}
		else limit = Integer.MAX_VALUE;
		array.recycle();
	}
	@Override public void onBindDialogView(View v) {
		super.onBindDialogView(v);
		((TextView)v.findViewById(R.id.twoTextFirstView)).setText(firstText);
		((TextView)v.findViewById(R.id.twoTextSecondView)).setText(secondText);
		firstEdit = v.findViewById(R.id.twoTextFirstEdit);
		secondEdit = v.findViewById(R.id.twoTextSecondEdit);
		SharedPreferences pref = getSharedPreferences();
		firstEdit.setText(String.valueOf(pref.getInt(firstKey, firstDefault)));
		secondEdit.setText(String.valueOf(pref.getInt(secondKey, secondDefault)));
	}
	@Override protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			StringBuilder builder = new StringBuilder();
			SharedPreferences.Editor editor = getSharedPreferences().edit();
			try {
				int value1 = Integer.valueOf(firstEdit.getText().toString());
				if(value1 < 1 || value1 > limit) throw new NumberFormatException();
				editor.putInt(firstKey, value1);
			}
			catch(NumberFormatException e) {
				builder.append(firstText).append(", ");
			}
			try {
				int value2 = Integer.valueOf(secondEdit.getText().toString());
				if(value2 < 1 || value2 > limit) throw new NumberFormatException();
				editor.putInt(secondKey, value2);
			}
			catch(NumberFormatException e) {
				builder.append(secondText).append(", ");
			}
			if(builder.length() > 0) {
				builder.setLength(builder.length() - 2); //erase the last comma
				Toast.makeText(getContext(), String.format(Locale.getDefault(), getContext().getString(R.string.naturalNumberSmallerThan), builder.toString(), limit), Toast.LENGTH_SHORT).show();
			}
			editor.apply();
		}
	}
}
