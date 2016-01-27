package com.example.fyp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
 public class MainActivity2 extends FragmentActivity implements ActionBar.TabListener{  
      ActionBar actionbar;  
      ViewPager viewpager;  
      PagerAdapter ft;
      private String username;
      private FragmentTabHost mTabHost;
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
      
      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.main, menu);
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

	
		
	
	
 }