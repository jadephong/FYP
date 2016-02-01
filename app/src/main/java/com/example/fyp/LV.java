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
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LV extends ListActivity {
	 ListView listView ;
     private String username;
    private ImageView imageview;
	 String[] values = new String[] { "User Profile","Account","About","Log Out"
            };
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.setting);

         imageview=(ImageView)findViewById(R.id.imageView1);
         username= getIntent().getStringExtra("username");
         try{
             getImage(username);}
         catch (Exception e){
             e.printStackTrace();
         }
         ActionBar ab = getActionBar(); 
         ab.setDisplayHomeAsUpEnabled(true);
         ab.setTitle(Html.fromHtml("<font color='#000000'>Settings </font>"));
         ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
        setListAdapter(new ArrayAdapter
        		<String>(this,android.R.layout.simple_list_item_1,values));
     }
 
         
               public void onListItemClick(ListView parent,View v,int position,long id){
            	   
            	   if(position==0){
                       Intent intent = new Intent(LV.this, UserProfile.class);
                       intent.putExtra("username", username);
                       startActivity(intent);
            		   
            	   }
            	   else if(position==1){
            		   Intent intent1 = new Intent(LV.this,Account.class);
                       intent1.putExtra("username", username);
            		   startActivity(intent1);
            		   
            	   }
            	   else if(position==2){
            		   Intent intent3 = new Intent(LV.this,About.class);
                       intent3.putExtra("username", username);
            		   startActivity(intent3);
            		   
            	   }
            	   else if(position==3){
            		   Toast.makeText(LV.this, "Signing out...", Toast.LENGTH_SHORT).show();
   				       Intent intent4 = new Intent(LV.this,Login.class);
            		   startActivity(intent4);
            		   
            	   }
            	 
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
                loading =ProgressDialog.show(LV.this, "", "Please Wait...", true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                RoundImage roundedImage = new RoundImage(b);
                imageview.setImageDrawable(roundedImage);
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
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(LV.this,MainActivity2.class);
        intent1.putExtra("username", username);
        startActivity(intent1);
    }
}