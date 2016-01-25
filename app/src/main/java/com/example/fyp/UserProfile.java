package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.fyp.R.layout;

public class UserProfile extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.userprofile);
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>User Profile </font>"));
        
        int keyCode = 0;
			 
			 if(keyCode == KeyEvent.KEYCODE_BACK) {
				setContentView(R.layout.navigation);
			 }
			 
			 else
			 {
			      			 
			final EditText t1 = (EditText) findViewById(R.id.usernameValue);
			final EditText t2 = (EditText) findViewById(R.id.genderValue);
			final EditText t3 = (EditText) findViewById(R.id.bdValue);
			final EditText t4 = (EditText) findViewById(R.id.contactValue);
			 
			final ImageButton save = (ImageButton)findViewById(R.id.imgbtnsave);
			final ImageButton cancel = (ImageButton)findViewById(R.id.imgbtncancel);
			
			final ImageButton edit = (ImageButton)findViewById(R.id.imgbtnedit);
			edit.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) { 
			
			t1.setEnabled(true);
			t2.setEnabled(true);
			t3.setEnabled(true);
			t4.setEnabled(true);
			
			edit.setVisibility(ImageButton.GONE);
			save.setVisibility(ImageButton.VISIBLE);
			cancel.setVisibility(ImageButton.VISIBLE);
				}
			});
			
			save.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				t1.setText(t1.getText().toString());
				t2.setText(t2.getText().toString());
				t3.setText(t3.getText().toString());
				t4.setText(t4.getText().toString());						
				
				t1.setEnabled(false);
     			t2.setEnabled(false);
     			t3.setEnabled(false);
     			t4.setEnabled(false);
     			
     			edit.setVisibility(ImageButton.VISIBLE);
     			save.setVisibility(ImageButton.GONE);
     			cancel.setVisibility(ImageButton.GONE);
				
			}
				
			});
			
			cancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				t1.setEnabled(false);
     			t2.setEnabled(false);
     			t3.setEnabled(false);
     			t4.setEnabled(false);
     			
     			edit.setVisibility(ImageButton.VISIBLE);
     			save.setVisibility(ImageButton.GONE);
     			cancel.setVisibility(ImageButton.GONE);
				
			}
				
			});
			 }
      /*  getActionBar().hide();*/
}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	 switch (item.getItemId()) {
         case android.R.id.home:
             Intent intent=new Intent(this,LV.class);
             startActivity(intent);
             return true;
             default:
             return super.onOptionsItemSelected(item); 
    	 }
    	 }
       
}