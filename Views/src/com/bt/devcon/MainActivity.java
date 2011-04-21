package com.bt.devcon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
	}
	
	public void loadListView(View view) {
		Intent intent = new Intent(this, MyListActivity.class);				
		this.startActivity(intent);
	}
}
