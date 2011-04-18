package com.bt.devcon.android;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private Button startButton, stopButton, startActivityButton, stopActivityButton;
	private EditText textBox; 
	private Intent serviceIntent;
	private ActivityWorker worker;
	private static boolean working;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        serviceIntent = new Intent(this, MyService.class);
        worker = new ActivityWorker();
        
        startButton = (Button)this.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        
        stopButton = (Button)this.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        
        startActivityButton = (Button)this.findViewById(R.id.startActivityButton);
        startActivityButton.setOnClickListener(this);
        
        stopActivityButton = (Button)this.findViewById(R.id.stopActivityButton);
        stopActivityButton.setOnClickListener(this);
        
        textBox = (EditText)this.findViewById(R.id.textBox);
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
		case R.id.startActivityButton:
			startActivityButton.setEnabled(false);
			stopActivityButton.setEnabled(true);
			working = true;
			worker = new ActivityWorker();
			worker.start();
			break;
		case R.id.stopActivityButton:
			working = false;
			stopActivityButton.setEnabled(false);
			startActivityButton.setEnabled(true);
			textBox.setText(R.string.text_box_text);
			break;
		}
	}
	
	private class ActivityWorker extends Thread {
		public void run() {						
			while(MainActivity.working) {
				textBox.post(new Runnable() {
					public void run() {
						textBox.setText("Activity working..." + new Random().nextInt());
					}
				});							
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {}							
			}
		}	
	}
	
}