package com.example.fyp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by jadephong on 20/1/2016.
 */
public class ForgotPassword extends Activity {
    private static final String URL = "http://jstarcnavigator.esy.es/andriod_user_api/checkemailexist.php";
    private static final String senderEmail = "tarcnavigator2016@gmail.com";
    private static final String senderPassword = "navigator2016";
    DialogCustom dialog;
    Toast Toast;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        dialog=new DialogCustom(this);
        Toast.setGravity(Gravity.CENTER, 0, 0);
        getActionBar().hide();
        Button btnRecover = (Button) findViewById(R.id.btnRecover);
        btnRecover.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EditText emailRecover = (EditText) findViewById(R.id.txtEmailRecover);
                String email = emailRecover.getText().toString();
                final String randomPassword = getRandomPassword();
                if (emailRecover.getText().toString().length() < 1) {

                    Toast.makeText(ForgotPassword.this, "Please Enter Email", Toast.LENGTH_SHORT).show();

                } else {
                     forgotPassword(email, randomPassword);
                }

            }
        });
        TextView tv_goBack = (TextView) findViewById(R.id.tv_goBack);
        tv_goBack.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent1);


            }
        });
    }

    private void forgotPassword(final String email,final String randomPassword) {

        String urlSuffix = "?email=" + email + "&password=" + randomPassword;
        class RecoverPassword extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading =ProgressDialog.show(ForgotPassword.this, "", "Please Wait...", true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if ("Password Recover Email Is Sending".equals(s)) {
                    sendMail(email,randomPassword);
                   /* Intent intent = new Intent(ForgotPassword.this, Login.class);
                    startActivity(intent);*/

                }
                else if (s==null){
                    Toast.makeText(ForgotPassword.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
                }
                else {

                    dialog.alert()

                            .withTitle("Opps!")
                            .withIcon(getResources().getDrawable(R.drawable.warning9))
                            .withMessage("Account Not Existing!")
                             .withButton1Text("OK")
                            .show();

                    /*FragmentManager fm = getFragmentManager();
                    Dialog.YesNoDialog dialogFragment = new Dialog.YesNoDialog ();

                    dialogFragment.show(fm, "Sample Fragment");*/
               /*     Toast.makeText(ForgotPassword.this, "Account not existing", Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    if(Validator.checknetwork(getApplicationContext())!=false) {
                        URL url = new URL(URL + s);
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


        RecoverPassword rp = new RecoverPassword();
        rp.execute(urlSuffix);

    }

    private void sendMail(final String email,final String randomPassword) {
        class RetreiveFeedTask extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading =ProgressDialog.show(ForgotPassword.this, "", "Sending Mail...", true);
            }
            @Override
            protected String doInBackground(String... params) {

                  try {
                      if (Validator.checknetwork(getApplicationContext()) != false){
                          GMail gmail = new GMail();
                      if (gmail.sendMail(new String[]{email}, randomPassword)) {
                          return "done";
                      } else {
                          return "failed";
                      }
                  }
                      else {

                          return "connect failed";
                      }
                  }
                  catch (Exception e){
                      Log.e("SendMail", e.getMessage(), e);
                      return "error";
                  }

            }

            @Override
            protected void onPostExecute(String result) {
                loading.dismiss();
                if(result.equals("done")) {
                    dialog.alert()

                            .withTitle("Congratulation!")
                            .withIcon(getResources().getDrawable(R.drawable.tick14))
                            .withMessage("Recovery Email Was Sent.Please Check Your Email!")
                            .withButton1Text("OK")
                            .show();
                }
                else if("connect failed".equals(result)){
                    Toast.makeText(ForgotPassword.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
                }
                else {
                    dialog.alert()

                            .withTitle("Opps!")
                            .withIcon(getResources().getDrawable(R.drawable.warning9))
                            .withMessage("An Error Occur.Please Try Again!")
                            .withButton1Text("OK")
                            .show();
                }
            }


        }
        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }



    protected String getRandomPassword() {
        int length=8;
        String candidateChars="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
