package edu.msu.cse.selingal.madhatter;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class HatterActivity extends Activity {

	private static final String PARAMETERS = "parameters";
	
	/**
     * The hatter view object
     */
    private HatterView hatterView = null;

    /**
     * The feather checkbox
     */
    private Button colorButton = null;
    
    /**
     * The feather checkbox
     */
    private CheckBox featherCheck = null;
    
    /**
     * The hat choice spinner
     */
    private Spinner spinner;
    /**
     * Request code when selecting a picture
     */
    private static final int SELECT_PICTURE = 1;
    
    /**
     * Color returned from color selection
     */
    private static final int GOT_COLOR = 2;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hatter);
        /*
         * Get some of the views we'll keep around
         */
        hatterView = (HatterView)findViewById(R.id.hatterView);
        colorButton = (Button)findViewById(R.id.buttonColor);
        featherCheck = (CheckBox)findViewById(R.id.checkFeather);
        spinner = (Spinner) findViewById(R.id.spinnerHat);
        
        /*
         * Set up the spinner
         */
        
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
        		R.array.hats_spinner, android.R.layout.simple_spinner_item);
        
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                    int pos, long id) {
            	hatterView.setHat(pos);
            	
            	if (pos == 2) {
            		colorButton.setEnabled(true);
            	} else {
            		colorButton.setEnabled(false);
            	}
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
            
        });
        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            hatterView.getFromBundle(PARAMETERS, savedInstanceState);
            loadUi(savedInstanceState);
            spinner.setSelection(hatterView.getHat());
        }
	}
	
	/**
	 * Handle a picture button press
	 * @param view 
	 */
	public void onPicture(View view) {
		// Get a picture from the gallery
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);    
	}
	
	/**
	 * Handle a color button press
	 * @param view
	 */
	public void onColor(View view) {
		Intent intent = new Intent(this, ColorSelectActivity.class);
		startActivityForResult(intent, GOT_COLOR);
	}
	
	/**
	 * Handle a feather press
	 * @param view
	 */
	public void onFeather(View view) {
		hatterView.setFeather(featherCheck.isChecked());
	}
	
    /**
     * Function called when we get a result from some external 
     * activity called with startActivityForResult()
     * @param requestCode the request code we sent to the activity 
     * @param resultCode a result of from the activity - ok or cancelled
     * @param data data from the activity
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
			// Response from the picture selection
			Uri imageUri = data.getData();
			
			// Uri identifies image in the system. Need to query the system
			// for the location. Find out what columns we're interested in
			String[] projection = {MediaStore.Images.Media.DATA};
			
			// Do the query
			Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
			
			// Get the column for the index path
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			
			// Read the first row.
			cursor.moveToFirst();
			
			// Get the path name
			String path = cursor.getString(column_index);
			if (path != null) {
				hatterView.setImagePath(path);
			}
		}
		
		if (requestCode == GOT_COLOR && resultCode == Activity.RESULT_OK) {
			int color = data.getIntExtra(ColorSelectActivity.COLOR, Color.BLACK);
			hatterView.setColor(color);
		}
	}
	
    /* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveUi(outState);
		hatterView.putToBundle(PARAMETERS, outState);
	}
	
    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void saveUi(Bundle bundle) {
    	bundle.putBoolean("featherChecked", featherCheck.isChecked());
        bundle.putInt("spinnerValue", spinner.getSelectedItemPosition());
    }
    
    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void loadUi(Bundle bundle) {
    	featherCheck.setChecked(bundle.getBoolean("featherChecked"));
        spinner.setSelection(bundle.getInt("spinnerValue"));
    }
}
