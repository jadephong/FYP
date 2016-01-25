package com.example.fyp;

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
import android.widget.ImageView;

public class MessagingFragment extends Fragment {
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState) {
	 View view = inflater.inflate(R.layout.messaging_view, container, false);
     final ImageView imageViewContact = (ImageView)view.findViewById(R.id.imageViewContact);
     final ImageView imageViewMessage = (ImageView)view.findViewById(R.id.imageViewMessage);
     final ImageView imageViewAfterContact = (ImageView)view.findViewById(R.id.imageViewAfterContact);
     final EditText txtSend=(EditText)view.findViewById(R.id.txtSend);
     final Button bc = (Button) view.findViewById(R.id.buttonchat);
     imageViewContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageViewContact.setVisibility(ImageView.GONE);
				imageViewMessage.setVisibility(ImageView.VISIBLE);
				txtSend.setVisibility(EditText.VISIBLE);
				bc.setVisibility(Button.VISIBLE);
			}
     	
     });
     
     bc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageViewMessage.setVisibility(ImageView.GONE);
				imageViewAfterContact.setVisibility(ImageView.VISIBLE);
				txtSend.setText("");
			}
     	
     });
  return view;
 }


}
