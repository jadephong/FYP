package com.example.fyp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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
import java.util.HashMap;

public class MainActivity2 extends FragmentActivity implements ActionBar.TabListener{

      ActionBar actionbar;  
      ViewPager viewpager;  
      PagerAdapter ft;
      private String username;
      private MenuItem action_settings;
      private Boolean exit = false;
      private String regId;
      GoogleCloudMessaging gcm;
    SessionManager sessionManager;
      @Override  
      protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);  
           setContentView(R.layout.activity_main);
          sessionManager=new SessionManager();
          username= getIntent().getStringExtra("username");
          if (TextUtils.isEmpty(regId)) {
              regId =registerGCM();
              sessionManager.setRegId(MainActivity2.this, regId);
              Log.d("Main", "GCM RegId: " + regId);
          }
          updateGcm();
          viewpager = (ViewPager) findViewById(R.id.pager);
           ft = new PagerAdapter(getSupportFragmentManager());
           actionbar = getActionBar(); 
           actionbar.setTitle(Html.fromHtml("<font color='#000000'>Taruc Navigator </font>"));
           
           viewpager.setAdapter(ft);  
           actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
           actionbar.addTab(actionbar.newTab().setIcon(R.drawable.navigation).setTabListener(this)
                           .setText("Navigate")
           );
           actionbar.addTab(actionbar.newTab().setIcon(R.drawable.tracking).setTabListener(this)
                           .setText(" Track")
           );
           actionbar.addTab(actionbar.newTab().setIcon(R.drawable.messaging).setTabListener(this)
                           .setText(" Message")
           );
           
           viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageSelected(int arg0) {
                   actionbar.setSelectedNavigationItem(arg0);
               }

               @Override
               public void onPageScrolled(int arg0, float arg1, int arg2) {
                   // TODO Auto-generated method stub
               }

               @Override
               public void onPageScrollStateChanged(int arg0) {
                   // TODO Auto-generated method stub
               }
           });
         /* startService(new Intent(this, ShowNotification.class));*/
      }
      
      @Override  
      public void onTabReselected(Tab tab, FragmentTransaction ft) {  
           // TODO Auto-generated method stub  
      }  
      @Override  
      public void onTabSelected(Tab tab, FragmentTransaction ft) {  
           viewpager.setCurrentItem(tab.getPosition());  
      }  
      @Override  
      public void onTabUnselected(Tab tab, FragmentTransaction ft) {  
           // TODO Auto-generated method stub  
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
          class GetImage extends AsyncTask<String,Void,Bitmap> {


               @Override
               protected void onPreExecute() {
                    super.onPreExecute();
               }

               @Override
               protected void onPostExecute(Bitmap b) {
                    super.onPostExecute(b);
                   if(b!=null) {
                       Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(b, 64, 64);
                       RoundImage roundedImage = new RoundImage(ThumbImage);
                       action_settings.setIcon(roundedImage);
                   }
               }

               @Override
               protected Bitmap doInBackground(String... params) {
                   String s = params[0];
                   String add = "http://jstarcnavigator.esy.es/andriod_user_api/getImage.php"+s;
                   URL url = null;
                   Bitmap image = null;
                   BufferedReader bufferedReader = null;
                   String result;
                    try {
                         url = new URL(add);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        result = bufferedReader.readLine();
                        String r=result.trim().replace("\\", "");
                        JSONObject jsonObject= new JSONObject(URLDecoder.decode(r, "UTF-8"));
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
      public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.main, menu);
           action_settings=menu.findItem(R.id.action_settings);
          getImage(username);
          return true;
      }
      @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_settings:
                Intent intent = new Intent(MainActivity2.this, LV.class);
                intent.putExtra("username", username);
                startActivity(intent);
				return true;
  
			default:
				return super.onOptionsItemSelected(item);
			}
		}


    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(getApplicationContext());

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("Main",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString("regID", "");
        if (registrationId.isEmpty()) {
            Log.i("fyp", "Registration not found.");
            return "";
        }

        return registrationId;
    }

    private void registerInBackground() {
        class Register extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("Main", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                 storeRegistrationId(getApplicationContext(), regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("Main", "Error: " + msg);
                }
                Log.d("Main", "AsyncTask completed: " + msg);
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }
        Register rg = new Register();
        rg.execute();
    }
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("regID", regId);
        editor.commit();
    }
    private void updateGcm(){

        class UpdateGcm extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equalsIgnoreCase("connect failed")){
                    Toast.makeText(MainActivity2.this, "Please Check Your Connection!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String,String> param = new HashMap<String,String>();
                param.put("reg_id",regId);
                param.put("unique",username);
                RequestHandler rh = new RequestHandler();
                if(Validator.checknetwork(getApplicationContext())!=false) {
                    String res = rh.sendPostRequest("http://jstarcnavigator.esy.es/gcm/updategcm.php", param);
                    return res;
                }
                else{
                    return "connect failed";
                }
            }
        }

        UpdateGcm updateGcm = new UpdateGcm();
        updateGcm.execute();
    }
    }
