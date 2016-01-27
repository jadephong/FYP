package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.fyp.R.layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class UserProfile extends Activity {
	private String username;
	private EditText txt_username;
	private EditText txt_gender;
	private EditText txt_DOB;
	private EditText txt_phone_num;
	private EditText txt_email;

	public String getTxt_email() {
		return txt_email.getText().toString();
	}

	public void setTxt_email(String txt_email1) {
		String email=txt_email.getText().toString();
		email= txt_email1;
	}

	private static final String TAG_RESULTS="result";
	ArrayList<HashMap<String, String>> userList;
	JSONArray users = null;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.userprofile);
		username= getIntent().getStringExtra("username");
		userList = new ArrayList<HashMap<String,String>>();
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>User Profile </font>"));
		getUsers(username);
        int keyCode = 0;
			 
			 if(keyCode == KeyEvent.KEYCODE_BACK) {
				setContentView(R.layout.navigation);
			 }
			 
			 else
			 {
				 txt_username = (EditText) findViewById(R.id.usernameValue);
			     txt_gender = (EditText) findViewById(R.id.genderValue);
				 txt_DOB = (EditText) findViewById(R.id.bdValue);
				 txt_phone_num = (EditText) findViewById(R.id.contactValue);
			 
			final ImageButton save = (ImageButton)findViewById(R.id.imgbtnsave);
			final ImageButton cancel = (ImageButton)findViewById(R.id.imgbtncancel);
			
			final ImageButton btn_edit = (ImageButton)findViewById(R.id.imgbtnedit);
				 btn_edit.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) { 
			
			txt_username.setEnabled(true);
			txt_gender.setEnabled(true);
			txt_DOB.setEnabled(true);
			txt_phone_num.setEnabled(true);

					btn_edit.setVisibility(ImageButton.GONE);
			save.setVisibility(ImageButton.VISIBLE);
			cancel.setVisibility(ImageButton.VISIBLE);
				}
			});
			
			save.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_username.setText(txt_username.getText().toString());
				txt_gender.setText(txt_gender.getText().toString());
				txt_DOB.setText(txt_DOB.getText().toString());
				txt_phone_num.setText(txt_phone_num.getText().toString());
				
				txt_username.setEnabled(false);
     			txt_gender.setEnabled(false);
     			txt_DOB.setEnabled(false);
     			txt_phone_num.setEnabled(false);

				btn_edit.setVisibility(ImageButton.VISIBLE);
     			save.setVisibility(ImageButton.GONE);
     			cancel.setVisibility(ImageButton.GONE);
				
			}
				
			});
			
			cancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_username.setEnabled(false);
     			txt_gender.setEnabled(false);
     			txt_DOB.setEnabled(false);
     			txt_phone_num.setEnabled(false);

				btn_edit.setVisibility(ImageButton.VISIBLE);
     			save.setVisibility(ImageButton.GONE);
     			cancel.setVisibility(ImageButton.GONE);
				
			}
				
			});
			 }
      /*  getActionBar().hide();*/
}
	protected void showUsers(String json){
		try {
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(URLDecoder.decode(json, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONArray result = jsonObject.getJSONArray("result");
			JSONObject c = result.getJSONObject(0);
			String username = c.getString("username");
			String dob = c.getString("DOB");
			String gender = c.getString("gender");
			String phone_num = c.getString("phone_num");
			String email = c.getString("email");
			txt_username.setText(username);
			txt_DOB.setText(dob);
			txt_gender.setText(gender);
			txt_phone_num.setText(phone_num);
			txt_email.setText(email);
			Email emailClass=new Email();
			emailClass.setEmail(email);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void getUsers(final String param){
		String urlSuffix=null;
		if(param.contains("@")){
			urlSuffix = "?email="+param;
		}
		else if(param.matches("\\d+")){
			urlSuffix = "?phone_num="+param;
		}
		else if(param.matches("[a-zA-Z]+")){
			urlSuffix = "?username="+param;
		}
		class GetUser extends AsyncTask<String,Void,String>{
			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(UserProfile.this, "", "Please Wait...", true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				try {
					showUsers(s);
				}catch (Exception e){
					Log.e("SendMail", e.getMessage(), e);
				}
			}

			@Override
			protected String doInBackground(String... params) {
				String param = params[0];
				RequestHandler rh = new RequestHandler();
				String s1 = rh.sendGetRequestParam("http://jstarcnavigator.esy.es/andriod_user_api/getUserDetails.php",param);
				return s1;
			}
		}
		GetUser gu = new GetUser();
		gu.execute(urlSuffix);
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
	public class Email implements Serializable {

		private String email ;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}


	}
}