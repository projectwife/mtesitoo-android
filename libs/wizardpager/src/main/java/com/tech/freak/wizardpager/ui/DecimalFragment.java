package com.tech.freak.wizardpager.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;

public class DecimalFragment extends TextFragment {
	public static DecimalFragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		DecimalFragment f = new DecimalFragment();
		f.setArguments(args);
		return f;
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void setInputType() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			mEditTextInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else {
			mEditTextInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}
	}

}
