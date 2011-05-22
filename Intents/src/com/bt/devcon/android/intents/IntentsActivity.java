package com.bt.devcon.android.intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IntentsActivity extends Activity implements OnClickListener {
    
	public static final String INTENT_KEY = "data";
	
	private Button nextButton;
	private Button dialButton;
	private Button noActionButton;
	private EditText dialText;
	private EditText dataText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        dataText = (EditText)findViewById(R.id.data_text_box);
        
        dialButton = (Button)findViewById(R.id.dial_button);
        dialButton.setOnClickListener(this);
        dialText = (EditText)findViewById(R.id.dial_text_box);
        
        noActionButton = (Button)findViewById(R.id.no_action_button);
        noActionButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		Intent intent;
		switch(view.getId()) {
			case R.id.next_button:
				// Constructor -> from: this activity, to: next activity
				intent = new Intent(IntentsActivity.this, NextActivity.class);
				// Add any extra data we like as a key value pair
				intent.putExtra(INTENT_KEY, dataText.getText().toString());
				break;
			case R.id.dial_button:
				// This constructor directs the UI flow to the dialler
				intent = new Intent(Intent.ACTION_DIAL);
				// Dialler activity expects data in a certain format
				intent.setData(Uri.parse("tel:" + dialText.getText()));
				break;
			default:
				// Android will have no idea what to do with this, an exception will be thrown
				intent = new Intent();
		}
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException exception) {
			Toast.makeText(this, "Sorry, this action cannot be performed", Toast.LENGTH_SHORT).show();
		}
	}
}