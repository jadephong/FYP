package com.example.fyp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
	private static final String REGISTER_URL = "http://jstarcnavigator.esy.es/andriod_user_api/register.php";

	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getActionBar().hide();
        final Button signup = (Button)findViewById(R.id.btnsignupp);

        signup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EditText txtemail = (EditText) findViewById(R.id.txtemail);
				EditText txtusername = (EditText) findViewById(R.id.txtusername);
				EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
				EditText txtmobile = (EditText) findViewById(R.id.txtmobile);
				EditText txtrPassword = (EditText) findViewById(R.id.txtrPassword);
				String username = txtusername.getText().toString().trim().toLowerCase();
				String password = txtPassword.getText().toString().trim().toLowerCase();
				String email = txtemail.getText().toString().trim().toLowerCase();
				String mobile = txtmobile.getText().toString().trim().toLowerCase();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(signup.getApplicationWindowToken(), 0);
				if(email.length()<1) {
					Toast.makeText(MainActivity.this, "Email field cannot be empty", Toast.LENGTH_SHORT).show();
					 return;
				}
				else if(username.length()<1) {
					Toast.makeText(MainActivity.this, "Username field cannot be empty", Toast.LENGTH_SHORT).show();
					txtusername.requestFocus();
				    return;
				}
				else if(mobile.length()<1) {
					Toast.makeText(MainActivity.this, "Mobile field cannot be empty", Toast.LENGTH_SHORT).show();
					txtmobile.requestFocus();
				    return;
				}
				else if(password.toString().length()<1) {
					Toast.makeText(MainActivity.this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
					txtusername.requestFocus();
				    return;
				}
				else if(txtrPassword.getText().toString().length()<1) {
					Toast.makeText(MainActivity.this, "Retype Password field cannot be empty", Toast.LENGTH_SHORT).show();

					txtrPassword.requestFocus();
				    return;
				}

				else
				{
					validateAndRegister(email, username, mobile, password, txtrPassword.getText().toString());
				}
    
			}
				
		});
        
       
    }
	public void validateAndRegister(String email,String username,String mobile,String password,String rPassword){
		Validator validator=new Validator();
		if(validator.isValidEmail(email)!=true) {
			Toast.makeText(MainActivity.this, "Not A Valid Email", Toast.LENGTH_SHORT).show();
			return;
		}

		else if(validator.isOnlyChar(username)!=true) {
			Toast.makeText(MainActivity.this, "Only Character Allow For Username ", Toast.LENGTH_SHORT).show();
			return;
		}

		else if(validator.validatePhoneNum(mobile)!=true){
			Toast.makeText(MainActivity.this, "Not A Valid Phone Number", Toast.LENGTH_SHORT).show();
			return;
		}

		else if(validator.validatePassword(password)!=true){
			Toast.makeText(MainActivity.this, "Password must atleast 6,with digit and character", Toast.LENGTH_SHORT).show();
			return;
		}

		else if(!rPassword.equals(password)){
			Toast.makeText(MainActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
			return;
		}

		else{
			LayoutInflater layout = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			register(username, password, email, mobile);
		}
	}
	private void register(String username, String password, String email, String mobile) {
		String urlSuffix = "?username="+username+"&password="+password+"&email="+email+"&phone_num="+mobile;
		class RegisterUser extends AsyncTask<String, Void, String> {

			ProgressDialog loading;


			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(MainActivity.this, "", "Please Wait...", true);
			}
			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
				if("register sucesssfully".equals(s)){
					Intent intent = new Intent(MainActivity.this, Login.class);
					startActivity(intent);
			}
				else if ("connect failed".equals(s)){
					Toast.makeText(MainActivity.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(MainActivity.this, "Unknown Error Occur", Toast.LENGTH_LONG).show();
				}
			}
			@Override
			protected String doInBackground(String... params) {
				String s = params[0];
				BufferedReader bufferedReader = null;
				String result;
				try {
					if(Validator.checknetwork(getApplicationContext()) != false) {
						URL url = new URL(REGISTER_URL + s);
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));



						result = bufferedReader.readLine();

						return result;
					}else
					{
						return "connect failed";
					}
				}catch(Exception e){
					return null;
				}
			}
		}

		RegisterUser ru = new RegisterUser();
		ru.execute(urlSuffix);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        return super.onOptionsItemSelected(item);
    }
       

}
