package com.devcon.prefs;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ShowPreferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
