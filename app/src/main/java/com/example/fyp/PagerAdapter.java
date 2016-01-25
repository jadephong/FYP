package com.example.fyp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.EditText;

public class PagerAdapter extends FragmentPagerAdapter {

 public PagerAdapter(FragmentManager fm) {
  super(fm);
  // TODO Auto-generated constructor stub
 }

 @Override
 public Fragment getItem(int arg0) {
  // TODO Auto-generated method stub
  switch (arg0) {
  case 0:
	 
  return new NavigationFragment();
  
  case 1:
   return new TrackingFragment();
  case 2:
   return new MessagingFragment();

  default:
   break;
  }
  return null;
 }

 @Override
 public int getCount() {
  // TODO Auto-generated method stub
  return 3;
 }


}

