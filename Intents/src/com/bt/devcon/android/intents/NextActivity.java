package com.bt.devcon.android.intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends Activity implements OnClickListener {
	
	private Button previousButton;
	private TextView dataTextView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_layout);
        
        previousButton = (Button)findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);
        
        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        String receivedData = extras.getString(IntentsActivity.INTENT_KEY);
        dataTextView = (TextView)findViewById(R.id.data_text_view);
        dataTextView.setText(dataTextView.getText() + receivedData);
    }
	
	@Override
	public void onClick(View view) {
		Intent nextActivityIntent = new Intent(NextActivity.this, IntentsActivity.class);
		startActivity(nextActivityIntent);
	}
}
