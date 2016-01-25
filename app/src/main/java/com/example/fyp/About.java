package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fyp.R.layout;

public class About extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.about);
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>About </font>"));
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
}//
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