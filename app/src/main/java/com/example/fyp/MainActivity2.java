package com.example.fyp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity2 extends FragmentActivity implements ActionBar.TabListener{
      ActionBar actionbar;  
      ViewPager viewpager;  
      PagerAdapter ft;
      private String username;
      private MenuItem action_settings;
      private Boolean exit = false;
      @Override  
      protected void onCreate(Bundle savedInstanceState) {  
           super.onCreate(savedInstanceState);  
           setContentView(R.layout.activity_main);
           username= getIntent().getStringExtra("username");
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
           actionbar.addTab(actionbar.newTab().setIcon( R.drawable.messaging).setTabListener(this)
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
               ProgressDialog loading;

               @Override
               protected void onPreExecute() {
                    super.onPreExecute();
                    loading =ProgressDialog.show(MainActivity2.this, "", "Please Wait...", true);
               }

               @Override
               protected void onPostExecute(Bitmap b) {
                    super.onPostExecute(b);
                    loading.dismiss();
                   Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(b, 64, 64);
                    RoundImage roundedImage = new RoundImage(ThumbImage);
                   action_settings.setIcon(roundedImage);
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


	
	
 }