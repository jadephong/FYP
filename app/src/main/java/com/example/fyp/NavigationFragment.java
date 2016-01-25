package com.example.fyp;

import com.example.fyp.R.id;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class NavigationFragment extends Fragment {
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
  // TODO Auto-generated method stub
	 View view = inflater.inflate(R.layout.navigation, container, false);
	 final EditText txtFrom = (EditText) view.findViewById(R.id.txtFrom);
	 final EditText txtTo = (EditText) view.findViewById(R.id.txtTo);
	 final LinearLayout login=(LinearLayout)view.findViewById(R.id.loginlinear);
	 final LinearLayout linearshow=(LinearLayout)view.findViewById(id.linearshow);
	 final LinearLayout linearhide=(LinearLayout)view.findViewById(id.linearhide);
	 ImageButton btnhide = (ImageButton) view.findViewById(R.id.imageButtonhide);
	 ImageButton btnshow = (ImageButton) view.findViewById(R.id.imageButtonshow);
	 Button btngonav = (Button) view.findViewById(R.id.btnGo);
	 btnshow.setOnClickListener(new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			linearhide.setVisibility(LinearLayout.GONE);
			linearshow.setVisibility(LinearLayout.VISIBLE);
		}});
     btnhide.setOnClickListener(new ImageButton.OnClickListener() {

 		@Override
 		public void onClick(View v) {
 			linearhide.setVisibility(LinearLayout.VISIBLE);
			linearshow.setVisibility(LinearLayout.GONE);
 			
 		}});
 	btngonav.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        	
      	  if(txtFrom.getText().toString().length()<1) {
    			Toast.makeText(getActivity(), "Location field cannot be empty", Toast.LENGTH_SHORT).show();
    		    return;
    		}
    		else if(txtTo.getText().toString().length()<1) {
    			Toast.makeText(getActivity(), "Destination field cannot be empty", Toast.LENGTH_SHORT).show();
    			txtTo.requestFocus();
    		    return;
    		}
      	  
    		else {
    			login.setBackgroundResource(R.drawable.navmap);

        }
        }
    });
 	 return view;
 }


}
