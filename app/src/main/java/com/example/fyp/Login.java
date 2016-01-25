package com.example.fyp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R.id;
import com.example.fyp.R.layout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {
	DialogCustom dialog;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login);
		dialog=new DialogCustom(this);

        getActionBar().hide();
     // Login
        Button login = (Button)findViewById(id.btnSignin);
        login.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText txtusername = (EditText) findViewById(id.txtlusername);
				EditText txtPassword = (EditText) findViewById(id.txtlpassword);
				String username = txtusername.getText().toString();
				String password = txtPassword.getText().toString();

				if (txtusername.getText().toString().length() < 1) {
					Toast.makeText(Login.this, "Username field cannot be empty", Toast.LENGTH_SHORT).show();
					return;
				} else if (txtPassword.getText().toString().length() < 1) {
					Toast.makeText(Login.this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
					txtPassword.requestFocus();
					return;
				} else {
					login(username, password);
				/*	Toast.makeText(Login.this, "Welcome back", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Login.this, MainActivity2.class);
					startActivity(intent);*/

				}

			}
		});
        TextView reg = (TextView)findViewById(id.reg);
        reg.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Login.this, MainActivity.class);
				startActivity(intent1);

				
			}});

		TextView tv_forgot_password = (TextView)findViewById(id.forgot_password);
		tv_forgot_password.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Login.this, ForgotPassword.class);
				startActivity(intent1);


			}});
	}
	private void login(final String param, String password) {
		String urlSuffix=null;
		if(param.contains("@")){
			 urlSuffix = "?email="+param+"&password="+password;
		}
		else if(param.matches("\\d+")){
			 urlSuffix = "?phone_num="+param+"&password="+password;
		}
		else if(param.matches("[a-zA-Z]+")){
			urlSuffix = "?username="+param+"&password="+password;
		}
		class LoginAsync extends AsyncTask<String, Void, String> {

			private Dialog loadingDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingDialog = ProgressDialog.show(Login.this, "", "Please Wait...", true);
			}

			@Override
			protected String doInBackground(String... params) {
				String s = params[0];

				String LOGIN_URL = "http://jstarcnavigator.esy.es/andriod_user_api/login.php";
				BufferedReader bufferedReader = null;
				try {
					if(Validator.checknetwork(getApplicationContext())!=false) {
						URL url = new URL(LOGIN_URL + s);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

						String result;

						result = bufferedReader.readLine();

						return result;
					}
					else {
						return "connect failed";
					}
				}catch(Exception e){
					return null;
				}

			}

			@Override
			protected void onPostExecute(String result){
				String s = result.trim();
				loadingDialog.dismiss();
				if(s.equalsIgnoreCase("welcome back")){
					Intent intent = new Intent(Login.this, MainActivity2.class);
					/*intent.putExtra(USER_NAME, username);*/

					startActivity(intent);
				}else if(s.equalsIgnoreCase("connect failed")){
					Toast.makeText(Login.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
				}
				else {
					dialog.alert()
							.withTitle("Opps!")
							.withIcon(getResources().getDrawable(R.drawable.warning9))
							.withMessage("Invalid User Name or Password!")
							.withButton1Text("OK")
							.show();

				}
			}
		}

		LoginAsync la = new LoginAsync();
		la.execute(urlSuffix);

	}


	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        
	      
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	       
	        return super.onOptionsItemSelected(item);
	    }
	       

}
