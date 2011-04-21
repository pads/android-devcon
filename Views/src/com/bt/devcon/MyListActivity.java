package com.bt.devcon;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyListActivity extends ListActivity implements OnItemClickListener {
	
	private static final String[] ITEMS = {"1", "2", "3"};
	
	private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        listView = this.getListView();
        listView.setOnItemClickListener(this);
        // This populates the list and presents it (instead of setContentView(layout)
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, ITEMS));
    }
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int index, long id) {
		Toast.makeText(this.getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
	}
}