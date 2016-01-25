package com.example.fyp;

import com.example.fyp.R.layout;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class TrackingFragment extends Fragment {
 
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	 View v = inflater.inflate(R.layout.tracking_view, container, false);
	    	  final ImageView imageViewtrack= (ImageView)v.findViewById(R.id.imageViewtrack);
	         final LinearLayout lineartrack = (LinearLayout)v.findViewById(R.id.lineartrack);
	        
	         imageViewtrack.setOnClickListener(new View.OnClickListener() {

	    			@Override
	    			public void onClick(View v) {
	    				// TODO Auto-generated method stub
	    				LayoutInflater layout = (LayoutInflater)
	    						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    				final View popUp = layout.inflate(R.layout.pop_up_window_track, null);
	    				final PopupWindow popUpWindow = new PopupWindow(popUp, LayoutParams.WRAP_CONTENT,
	    						LayoutParams.WRAP_CONTENT);
	    				popUpWindow.showAtLocation(popUp, Gravity.CENTER, 0, 0);
	    				new Handler().postDelayed(new Runnable() {
	    		            @Override
	    		            public void run() {
	    		            	
	    						popUpWindow.dismiss();
	    						imageViewtrack.setVisibility(ImageView.GONE);
	    						lineartrack.setVisibility(LinearLayout.VISIBLE);
	    						
	    		            }
	    		        }, 5000);		
	    					
	    			}});
	       return v;
	   }
       
}


   
     
