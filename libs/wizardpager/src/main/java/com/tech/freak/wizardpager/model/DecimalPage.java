package com.tech.freak.wizardpager.model;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.ui.DecimalFragment;
import com.tech.freak.wizardpager.ui.NumberFragment;

public class DecimalPage extends TextPage {

	public DecimalPage(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return DecimalFragment.create(getKey());
	}

}
