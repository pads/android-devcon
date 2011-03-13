package com.bt.devcon.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button startButton, stopButton;
	private Intent serviceIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        serviceIntent = new Intent(this, MyService.class);
        
        startButton = (Button)this.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        
        stopButton = (Button)this.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.startButton:
			startService(serviceIntent);
			startButton.setEnabled(false);
			startButton.setText("Service Started");
			stopButton.setEnabled(true);
			stopButton.setText("Stop Service");
			break;
		case R.id.stopButton:
			stopService(serviceIntent);
			stopButton.setEnabled(false);
			stopButton.setText(R.string.stop_button_text);
			startButton.setEnabled(true);
			startButton.setText(R.string.start_button_text);
			break;
		}
	}
}