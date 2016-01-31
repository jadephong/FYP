package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;


public class Account extends Activity {
	private EditText txt_email;
	private EditText txt_password;
	private EditText txt_currentPassword;
	private ImageButton btnconfirm;
	private ImageButton btnsave;
	private ImageButton btncancel;
	private String username;
	private String ori_email;
	DialogCustom dialog;
	private TextView del_acc;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account);
		dialog=new DialogCustom(this);
		username= getIntent().getStringExtra("username");
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>Account </font>"));
		    txt_email = (EditText) findViewById(R.id.emailValue);
			txt_password = (EditText) findViewById(R.id.passwordValue);
			btnsave = (ImageButton)findViewById(R.id.imgbtnsave);
			btncancel = (ImageButton)findViewById(R.id.imgbtncancel);
			
		 del_acc = (TextView)findViewById(R.id.delete);
		try {
			getUsers(username);
		}catch (Exception e){
			e.printStackTrace();
		}
		del_acc.setVisibility(TextView.GONE);
		del_acc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.alert()
						.withTitle("Warning!")
						.withIcon(getResources().getDrawable(R.drawable.warning9))
						.withMessage("Are You Sure To Delete This Account?")
						.withButton1Text("Cancel")
						.withButton2Text("OK")
						.setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								deleteUser();

							}
						})
						.show();

			}});
			
			final ImageButton edit = (ImageButton)findViewById(R.id.imgbtnedit);
			txt_currentPassword=(EditText)findViewById(R.id.currentPassword);
			btnconfirm = (ImageButton)findViewById(R.id.imgbtnconfirm);
			edit.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					txt_currentPassword.setVisibility(EditText.VISIBLE);
					btnconfirm.setVisibility(ImageButton.VISIBLE);
					txt_currentPassword.setText("");
					txt_email.setVisibility(EditText.GONE);
					txt_password.setVisibility(EditText.GONE);
					del_acc.setVisibility(TextView.GONE);
					edit.setVisibility(ImageButton.GONE);
					btncancel.setVisibility(ImageButton.GONE);
				}
			});

		btnconfirm.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkPassword(ori_email, txt_currentPassword.getText().toString());

			}

		});
		btnsave.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(btnsave.getApplicationWindowToken(), 0);
				Validator validator=new Validator();
				String password= txt_password.getText().toString();


				if(validator.validatePassword(password)!=true) {
					Toast.makeText(Account.this, "Password must atleast 6,with digit and character", Toast.LENGTH_SHORT).show();
					return;
				}


				else {
				updateUser();
				getUsers(username);
				txt_email.setEnabled(false);
				txt_password.setEnabled(false);
				del_acc.setVisibility(TextView.GONE);
      			edit.setVisibility(ImageButton.VISIBLE);
				btnsave.setVisibility(ImageButton.GONE);
				btncancel.setVisibility(ImageButton.GONE);
				
			}
				
			}});

		btncancel.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_password.setText("12346555");
				txt_email.setEnabled(false);
				txt_password.setEnabled(false);
				del_acc.setVisibility(TextView.GONE);

      			edit.setVisibility(ImageButton.VISIBLE);
				btnsave.setVisibility(ImageButton.GONE);
				btncancel.setVisibility(ImageButton.GONE);


			}

			});
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
			String email = c.getString("email");
			txt_email.setText(email);
			ori_email=email;
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void getUsers(String param){
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
		class GetUser extends AsyncTask<String,Void,String> {
			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(Account.this, "", "Please Wait...", true);
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

	private void updateUser() {

		String urlSuffix = "?email=" + ori_email + "&password=" + txt_password.getText().toString();
		class UpdateUser extends AsyncTask<String, Void, String> {

			ProgressDialog loading;


			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(Account.this, "", "Please Wait...", true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();

				if ("Password Recover Email Is Sending".equals(s)) {
					Toast.makeText(Account.this, "Password Reset!", Toast.LENGTH_LONG).show();
				}
				else if (s==null){
					Toast.makeText(Account.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
				}
				else {

					dialog.alert()

							.withTitle("Opps!")
							.withIcon(getResources().getDrawable(R.drawable.warning9))
							.withMessage("An Error Occur!Please Try Again")
							.withButton1Text("OK")
							.show();

				}
			}

			@Override
			protected String doInBackground(String... params) {
				String s = params[0];
				BufferedReader bufferedReader = null;
				try {
					if(Validator.checknetwork(getApplicationContext())!=false) {
						URL url = new URL("http://jstarcnavigator.esy.es/andriod_user_api/checkemailexist.php" + s);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String result;
						result = bufferedReader.readLine();
						return result;
					}
					else{

						return null;
					}
				} catch (Exception e) {
					return null;
				}
			}
		}


		UpdateUser rp = new UpdateUser();
		rp.execute(urlSuffix);

	}

	public void checkPassword(final String param, String password) {
		String urlSuffix=null;
		if(param.contains("@")){
			urlSuffix = "?email="+param+"&password="+password;
		}

		class CheckPassword extends AsyncTask<String, Void, Boolean> {

			private Dialog loadingDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingDialog = ProgressDialog.show(Account.this, "", "Please Wait...", true);
			}

			@Override
			protected Boolean doInBackground(String... params) {
				String s = params[0];

				String CheckPassword_URL = "http://jstarcnavigator.esy.es/andriod_user_api/login.php";
				BufferedReader bufferedReader = null;
				try {

						URL url = new URL(CheckPassword_URL + s);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String result;

						result = bufferedReader.readLine();
						if(result.contains("welcome back")){
							return true;

						}
						else {
							return false;
						}


				}catch(Exception e){
					return null;
				}

			}

			@Override
			protected void onPostExecute(Boolean result){
				loadingDialog.dismiss();
				OnclickConfirm(result);
			}
		}

		CheckPassword la = new CheckPassword();
		la.execute(urlSuffix);

	}
	public void OnclickConfirm(final Boolean result) {

				if(result==true) {
					txt_email.setVisibility(EditText.VISIBLE);
					txt_password.setVisibility(EditText.VISIBLE);
					txt_password.setEnabled(true);
					btnsave.setVisibility(ImageButton.VISIBLE);
					txt_currentPassword.setVisibility(EditText.GONE);
					btnconfirm.setVisibility(EditText.GONE);
					btncancel.setVisibility(ImageButton.VISIBLE);
					txt_password.setText("");
					del_acc.setVisibility(TextView.VISIBLE);
				}
				else{
						dialog.alert()
								.withTitle("Opps!")
								.withIcon(getResources().getDrawable(R.drawable.warning9))
								.withMessage("Password Not Correct!Please Try Again!")
								.withButton1Text("OK")
								.show();

					}


	}


	private void deleteUser(){
		final String email = ori_email.trim();
		class DeleteUser extends AsyncTask<Void,Void,String>{
			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(Account.this, "", "Please Wait...", true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				if(s.equalsIgnoreCase("connect failed")){
					Toast.makeText(Account.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
				}else if("Delete Successfully".equals(s)){
					Toast.makeText(Account.this, s, Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Account.this, Login.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(Account.this, s, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {

				HashMap<String,String> param = new HashMap<String,String>();
				param.put("email",email);
				RequestHandler rh = new RequestHandler();
				if(Validator.checknetwork(getApplicationContext())!=false) {
					String res = rh.sendPostRequest("http://jstarcnavigator.esy.es/andriod_user_api/deleteUser.php", param);
					return res;
				}
				else{
					return "connect failed";
				}
			}
		}

		DeleteUser du = new DeleteUser();
		du.execute();
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