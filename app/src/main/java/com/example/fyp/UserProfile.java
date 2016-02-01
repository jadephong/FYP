package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R.layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class UserProfile extends Activity {
	private String username;
	private EditText txt_username;
	private Spinner spinner_gender;
	private EditText txt_DOB;
	private EditText txt_phone_num;
	ArrayList<HashMap<String, String>> userList;
	private DatePickerDialog datepicker;
	private SimpleDateFormat dateFormatter;
	private String ori_email;
	private ImageView imageView;
	private Bitmap bitmap;
	private Uri filePath;
	private ImageButton imgbtnupload;
	private  ImageButton btnconfirm;
	private  ImageButton btn_edit;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.userprofile);
		btnconfirm=(ImageButton) findViewById(R.id.btnconfirm);
		imgbtnupload=(ImageButton) findViewById(R.id.imgbtnupload);
		imageView=(ImageView) findViewById(R.id.imageView1);
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		username= getIntent().getStringExtra("username");
		userList = new ArrayList<HashMap<String,String>>();
        ActionBar ab = getActionBar(); 
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>User Profile </font>"));
		try {
			getImage(username);
			getUsers(username);
		}catch(Exception e){
			Log.e("Get", e.getMessage(), e);
		}
        int keyCode = 0;
			 
			 if(keyCode == KeyEvent.KEYCODE_BACK) {
				setContentView(R.layout.navigation);
			 }
			 
			 else
			 {

				 txt_username = (EditText) findViewById(R.id.usernameValue);
			     spinner_gender = (Spinner) findViewById(R.id.genderValue);
				 spinner_gender.setEnabled(false);
				 txt_DOB = (EditText) findViewById(R.id.bdValue);
				 Calendar newCalendar = Calendar.getInstance();
				 datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

					 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						 Calendar newDate = Calendar.getInstance();
						 newDate.set(year, monthOfYear, dayOfMonth);
						 txt_DOB.setText(dateFormatter.format(newDate.getTime()));
					 }

				 },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

				 txt_DOB.setOnClickListener(new TextView.OnClickListener() {
					 @Override
					 public void onClick(View v) {
						 datepicker.show();
					 }
				 });

				 txt_phone_num = (EditText) findViewById(R.id.contactValue);

				 final ImageButton save = (ImageButton)findViewById(R.id.imgbtnsave);
				 final ImageButton cancel = (ImageButton)findViewById(R.id.imgbtncancel);
				 btn_edit = (ImageButton)findViewById(R.id.imgbtnedit);


				 ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
						 .createFromResource(getApplicationContext(), R.array.gender_array, R.layout.simple_spinner_item);
				 staticAdapter
						 .setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
				 spinner_gender.setAdapter(staticAdapter);
				 spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					 @Override
					 public void onItemSelected(AdapterView<?> parent, View view,
												int position, long id) {
						 ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
						 if (spinner_gender.isEnabled() == true) {
							 ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
						 } else {
							 ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
						 }

						 switch (position) {
							 case 0:
								 spinner_gender.setSelection(0);
								 break;
							 case 1:
								 spinner_gender.setSelection(1);
								 break;
							 case 2:
								 spinner_gender.setSelection(2);
								 break;

						 }
					 }

					 @Override
					 public void onNothingSelected(AdapterView<?> parent) {

					 }
				 });
				 imgbtnupload.setOnClickListener(new ImageButton.OnClickListener() {
					 @Override
					 public void onClick(View v) {
						 try {
							 showFileChooser();
							 btnconfirm.setVisibility(ImageButton.VISIBLE);
							 btn_edit.setVisibility(ImageButton.GONE);
							btnconfirm.setOnClickListener(new ImageButton.OnClickListener() {
								 @Override
								 public void onClick(View v) {
									 uploadImage();


								 }
							 });
						 } catch (Exception e) {
							 Log.e("File", e.getMessage(), e);
						 }

					 }
				 });
				 btn_edit.setOnClickListener(new Button.OnClickListener() {

					 @Override
					 public void onClick(View v) {
						 txt_username.setEnabled(true);
						 spinner_gender.setEnabled(true);
						 txt_DOB.setEnabled(true);
						 txt_DOB.setKeyListener(null);
						 txt_phone_num.setEnabled(true);
						 btn_edit.setVisibility(ImageButton.GONE);
						 save.setVisibility(ImageButton.VISIBLE);
						 cancel.setVisibility(ImageButton.VISIBLE);
					 }
				 });

				 save.setOnClickListener(new Button.OnClickListener() {

					 @Override
					 public void onClick(View v) {
						 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						 imm.hideSoftInputFromWindow(save.getApplicationWindowToken(), 0);
						 Validator validator = new Validator();
						 String validate_username = txt_username.getText().toString();
						 String phone_num = txt_phone_num.getText().toString();

						 if (validator.isOnlyChar(validate_username) != true) {
							 Toast.makeText(UserProfile.this, "Only Character Allow For Username ", Toast.LENGTH_SHORT).show();
							 return;
						 } else if (validator.validatePhoneNum(phone_num) != true) {
							 Toast.makeText(UserProfile.this, "Not A Valid Phone Number", Toast.LENGTH_SHORT).show();
							 return;
						 } else {
							 try {
								 updateUser();
								 getUsers(username);
							 } catch (Exception e) {
								 Log.e("Update", e.getMessage(), e);
							 }
						 }
						 txt_username.setEnabled(false);
						 spinner_gender.setEnabled(false);
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
						 spinner_gender.setEnabled(false);
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
			if(!dob.equals("null")) {
				txt_DOB.setText(dob);
			}
			ori_email=email;
			ArrayAdapter genderAdapter = (ArrayAdapter) spinner_gender.getAdapter();
			int spinnerPosition = genderAdapter.getPosition(gender);
			spinner_gender.setSelection(spinnerPosition);
			txt_phone_num.setText(phone_num);
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
					if(s.equalsIgnoreCase("connect failed")){
						Toast.makeText(UserProfile.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
					}else {
						showUsers(s);
					}
				}catch (Exception e){
					Log.e("SendMail", e.getMessage(), e);
				}
			}

			@Override
			protected String doInBackground(String... params) {
				String param = params[0];
				RequestHandler rh = new RequestHandler();
				if(Validator.checknetwork(getApplicationContext())!=false) {
					String s1 = rh.sendGetRequestParam("http://jstarcnavigator.esy.es/andriod_user_api/getUserDetails.php", param);
					return s1;
				}
				else{
					return "connect failed";
				}
			}
		}
		GetUser gu = new GetUser();
		gu.execute(urlSuffix);
	}
	private void updateUser(){
		final String email = ori_email.trim();
		final String username = txt_username.getText().toString().trim();
		final String phone_num = txt_phone_num.getText().toString().trim();
		final String gender = spinner_gender.getSelectedItem().toString();
		final String DOB = txt_DOB.getText().toString();
		class UpdateUser extends AsyncTask<Void,Void,String>{
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
				if(s.equalsIgnoreCase("connect failed")){
					Toast.makeText(UserProfile.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(UserProfile.this, s, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {

				HashMap<String,String> param = new HashMap<String,String>();
				param.put("email",email);
				param.put("username",username);
				param.put("phone_num",phone_num);
				param.put("gender",gender);
				param.put("DOB",DOB);
				param.put("ori_email",email);
				RequestHandler rh = new RequestHandler();
				if(Validator.checknetwork(getApplicationContext())!=false) {
					String res = rh.sendPostRequest("http://jstarcnavigator.esy.es/andriod_user_api/updateUser.php", param);
					return res;
				}
				else{
					return "connect failed";
				}
			}
		}

		UpdateUser uu = new UpdateUser();
		uu.execute();
	}

	private void showFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

			filePath = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				Bitmap b1=Bitmap.createScaledBitmap(
						bitmap, 500, 500, false);
				RoundImage roundedImage = new RoundImage(b1);
				imageView.setImageDrawable(roundedImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getStringImage(Bitmap bmp){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap b1=bmp.createScaledBitmap(
				bitmap, 500, 500, false);
		b1.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	private void uploadImage(){
		class UploadImage extends AsyncTask<Bitmap,Void,String>{

			ProgressDialog loading;
			RequestHandler rh = new RequestHandler();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(UserProfile.this, "", "Uploading Image...", true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				getImage(username);
				loading.dismiss();
				Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
				imgbtnupload.setVisibility(ImageButton.VISIBLE);
				btn_edit.setVisibility(ImageButton.VISIBLE);
				btnconfirm.setVisibility(ImageButton.GONE);
			}

			@Override
			protected String doInBackground(Bitmap... params) {
				Bitmap bitmap = params[0];
				String uploadImage = getStringImage(bitmap);

				HashMap<String,String> data = new HashMap<String,String>();
				data.put("image", uploadImage);
				data.put("ori_email", ori_email);
				String result = rh.sendPostRequest("http://jstarcnavigator.esy.es/andriod_user_api/uploadImage.php",data);

				return result;
			}
		}

		UploadImage ui = new UploadImage();
		ui.execute(bitmap);
	}

	public void getImage(String param) {
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
		class GetImage extends AsyncTask<String,Void,Bitmap>{
			ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading =ProgressDialog.show(UserProfile.this, "", "Please Wait...", true);
			}

			@Override
			protected void onPostExecute(Bitmap b) {
				super.onPostExecute(b);
				loading.dismiss();
				RoundImage roundedImage = new RoundImage(b);
				imageView.setImageDrawable(roundedImage);
			}

			@Override
			protected Bitmap doInBackground(String... params) {
				String s = params[0];
				String add = "http://jstarcnavigator.esy.es/andriod_user_api/getImage.php"+s;
				URL url = null;
				Bitmap image = null;
				try {
					url = new URL(add);
					image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return image;
			}
		}

		GetImage gi = new GetImage();
		gi.execute(urlSuffix);
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
	@Override
	public void onBackPressed() {
		Intent intent1 = new Intent(UserProfile.this,LV.class);
		intent1.putExtra("username", username);
		startActivity(intent1);
	}
}