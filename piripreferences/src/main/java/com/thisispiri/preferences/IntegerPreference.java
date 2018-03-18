package com.thisispiri.preferences;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;
/**Gets an integer from the user using an {@code EditText}.*/
public class IntegerPreference extends EditTextPreference {
    private final int limit;
    private static final int LIMIT_DIVIDEND = 4;
    public IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Number, 0, 0);
        if(array.getBoolean(R.styleable.Number_imposeScreenLimit, false)) {
            Point size = new Point();
            ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
            limit = size.x / LIMIT_DIVIDEND + 1;
        }
        else limit = Integer.MAX_VALUE;
        array.recycle();
    }
    @Override protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 5);
    }
    @Override protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        getEditText().setText(String.valueOf(getPersistedInt(5)));
    }
    @Override protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue((int)(restoreValue ? getPersistedInt(5) : defaultValue));
    }
    @Override protected void onDialogClosed(boolean positiveResult) {
        //do not call the superclass' method as it will persist a string
        if (positiveResult) {
            setText(getEditText().getText().toString());
        }
    }
    @Override public void setText(String text) {
        //do not call the superclass' method as it persists a string.
        try {
            setValue(Integer.valueOf(text));
        }
        catch (NumberFormatException e) {
            Toast.makeText(getContext(), String.format(Locale.getDefault(), getContext().getString(R.string.naturalNumberSmallerThan), getTitle(), limit), Toast.LENGTH_SHORT).show();
        }
    }
    private void setValue(int value) {
        if (value < 1 || value > limit) {
            throw new NumberFormatException();
        }
        if (callChangeListener(value)) {
            persistInt(value);
        }
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
    }
}
