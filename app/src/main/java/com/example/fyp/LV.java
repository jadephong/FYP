package com.example.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LV extends ListActivity {
	 ListView listView ;
	 String[] values = new String[] { "User Profile","Account","About","Log Out"
            };
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.setting);
         ActionBar ab = getActionBar(); 
         ab.setDisplayHomeAsUpEnabled(true);
         ab.setTitle(Html.fromHtml("<font color='#000000'>Settings </font>"));
         ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
        setListAdapter(new ArrayAdapter
        		<String>(this,android.R.layout.simple_list_item_1,values));
     }
 
         
               public void onListItemClick(ListView parent,View v,int position,long id){
            	   
            	   if(position==0){
            		   Intent intent = new Intent(LV.this,UserProfile.class);
            		   startActivity(intent);
            		   
            	   }
            	   else if(position==1){
            		   Intent intent1 = new Intent(LV.this,Account.class);
            		   startActivity(intent1);
            		   
            	   }
            	   else if(position==2){
            		   Intent intent3 = new Intent(LV.this,About.class);
            		   startActivity(intent3);
            		   
            	   }
            	   else if(position==3){
            		   Toast.makeText(LV.this, "Signing out...", Toast.LENGTH_SHORT).show();
   				       Intent intent4 = new Intent(LV.this,Login.class);
            		   startActivity(intent4);
            		   
            	   }
            	 
        }
               @Override
               public boolean onOptionsItemSelected(MenuItem item) {
               	 switch (item.getItemId()) {
                    case android.R.id.home:
                        Intent intent=new Intent(this,MainActivity2.class);
                        startActivity(intent);
                        return true;
                        default:
                        return super.onOptionsItemSelected(item); 
               	 }
               }

}