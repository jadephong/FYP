package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R.layout;


public class Account extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.account);
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>Account </font>"));
			final EditText t1 = (EditText) findViewById(R.id.emailValue);
			final EditText t2 = (EditText) findViewById(R.id.passwordValue);
			final TextView tv1 = (TextView) findViewById(R.id.delete);
			 
			final ImageButton save = (ImageButton)findViewById(R.id.imgbtnsave);
			final ImageButton cancel = (ImageButton)findViewById(R.id.imgbtncancel);		
			
			final TextView del = (TextView)findViewById(R.id.delete);
			del.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater layout = (LayoutInflater)
						getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popUp = layout.inflate(R.layout.pop_up_window_del_acc, null);
				final PopupWindow popUpWindow = new PopupWindow(popUp, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				
				Button del = (Button) popUp.findViewById(R.id.yes);
				del.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Toast.makeText(Account.this, "Account deleted.", Toast.LENGTH_SHORT).show();
						
						popUpWindow.dismiss();
						
						Intent intent = new Intent(Account.this, MainActivity.class);
						startActivity(intent);
						
					}});
				
				Button dismiss = (Button) popUp.findViewById(R.id.no);
				dismiss.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popUpWindow.dismiss();
					}});
				
				popUpWindow.showAtLocation(popUp, Gravity.CENTER, 0, 0);	
			}
		});
			
			final ImageButton edit = (ImageButton)findViewById(R.id.imgbtnedit);
			final EditText currentPassword=(EditText)findViewById(R.id.currentPassword);
			final ImageButton btnconfirm = (ImageButton)findViewById(R.id.imgbtnconfirm);
			edit.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) { 
			currentPassword.setVisibility(EditText.VISIBLE);
			btnconfirm.setVisibility(ImageButton.VISIBLE);
			t1.setVisibility(EditText.GONE);
			t2.setVisibility(EditText.GONE);
			tv1.setVisibility(TextView.GONE);
			
			edit.setVisibility(ImageButton.GONE);
			
			cancel.setVisibility(ImageButton.GONE);
				}
			});
			btnconfirm.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					t1.setVisibility(EditText.VISIBLE);
					t2.setVisibility(EditText.VISIBLE);
					t1.setEnabled(true);
					t2.setEnabled(true);
					save.setVisibility(ImageButton.VISIBLE);
					currentPassword.setVisibility(EditText.GONE);
					btnconfirm.setVisibility(EditText.GONE);
					cancel.setVisibility(ImageButton.VISIBLE);
				}
			});
			
			
			save.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				t1.setText(t1.getText().toString());
				t2.setText(t2.getText().toString());				
				
				t1.setEnabled(false);
      			t2.setEnabled(false);
      			tv1.setVisibility(TextView.VISIBLE);
      			
      			edit.setVisibility(ImageButton.VISIBLE);
      			save.setVisibility(ImageButton.GONE);
      			cancel.setVisibility(ImageButton.GONE);
				
			}
				
			});
			
			cancel.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				t1.setEnabled(false);
      			t2.setEnabled(false);
      			tv1.setVisibility(TextView.VISIBLE);
      			
      			edit.setVisibility(ImageButton.VISIBLE);
      			save.setVisibility(ImageButton.GONE);
      			cancel.setVisibility(ImageButton.GONE);
      			
				
			}
				
			});                
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