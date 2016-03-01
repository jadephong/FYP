package com.example.fyp;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LV extends ListActivity {
    ListView listView;
    private String username;
    private ImageView imageview;
    private DialogCustom dialogCustom;
    String[] values = new String[]{"User Profile", "Account", "About", "Log Out"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        dialogCustom = new DialogCustom(this);
        imageview = (ImageView) findViewById(R.id.imageView1);
        username = getIntent().getStringExtra("username");
        try {
            getImage(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(Html.fromHtml("<font color='#000000'>Settings </font>"));
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
        setListAdapter(new ArrayAdapter
                <String>(this, android.R.layout.simple_list_item_1, values));
    }


    public void onListItemClick(ListView parent, View v, int position, long id) {
        Intent intent;
        if (position == 0) {
            intent = new Intent(LV.this, UserProfile.class);
            intent.putExtra("username", username);
            startActivity(intent);

        } else if (position == 1) {
            intent = new Intent(LV.this, Account.class);
            intent.putExtra("username", username);
            startActivity(intent);

        } else if (position == 2) {
            intent = new Intent(LV.this, About.class);
            intent.putExtra("username", username);
            startActivity(intent);

        } else if (position == 3) {

            dialogCustom.confimation("Log Out", "Are You Sure To Log Out?")
                    .setCancelText("No")
                    .showCancelButton(true)
                    .setConfirmClickListener(
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    SessionManager sessionManager = new SessionManager();
                                    sessionManager.clearUserName(LV.this);
                                    ShowNotification showNotification = new ShowNotification(LV.this);
                                    showNotification.clearNotification();
                                    Intent intent = new Intent(LV.this, Login.class);
                                    startActivity(intent);
                                }
                            }
                    ).show();


        }

    }

    public void getImage(String param) {
        String urlSuffix = null;
        if (param.contains("@")) {
            urlSuffix = "?email=" + param;
        } else if (param.matches("\\d+")) {
            urlSuffix = "?phone_num=" + param;
        } else if (param.matches("[a-zA-Z]+")) {
            urlSuffix = "?username=" + param;
        }
        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LV.this, "", "Please Wait...", true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                if (b != null) {
                    RoundImage roundedImage = new RoundImage(b);
                    imageview.setImageDrawable(roundedImage);
                }
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String s = params[0];
                String add = "http://jstarcnavigator.esy.es/andriod_user_api/getImage.php" + s;
                URL url = null;
                Bitmap image = null;
                BufferedReader bufferedReader = null;
                String result;
                try {
                    url = new URL(add);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    result = bufferedReader.readLine();
                    String r = result.trim().replace("\\", "");
                    JSONObject jsonObject = new JSONObject(URLDecoder.decode(r, "UTF-8"));
                    JSONArray jsonresult = jsonObject.getJSONArray("result");
                    JSONObject c = jsonresult.getJSONObject(0);
                    URL url2 = new URL(c.getString("url"));
                    image = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(urlSuffix);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity2.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getImage(username);
    }
}