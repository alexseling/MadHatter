package edu.msu.cse.selingal.madhatter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ColorSelectActivity extends Activity {

	public static final String COLOR = "COLOR";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_select);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_color_select, menu);
		return true;
	}
	
    public void selectColor(int color) {
    	Intent result = new Intent();
    	result.putExtra(COLOR, color);
    	setResult(Activity.RESULT_OK, result);
    	finish();
    }

}
