package com.mytasks;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TaskPreferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}