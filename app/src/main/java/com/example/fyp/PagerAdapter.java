package com.example.fyp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class PagerAdapter extends FragmentPagerAdapter {
 NavigationFragment navigationFragment = null;
 TrackingFragment trackingFragment = null;
 MessagingFragment messagingFragment = null;
 ViewPager viewpager = null;

 public PagerAdapter(FragmentManager fm) {
  super(fm);
  // TODO Auto-generated constructor stub
 }

 @Override
 public Fragment getItem(int position) {

  Fragment f = new Fragment();

  switch (position) {
   case 0:
    navigationFragment = new NavigationFragment();
    f = navigationFragment;
    break;
   case 1:
    trackingFragment = new TrackingFragment();
    f = trackingFragment;
    break;
   case 2:
    messagingFragment = new MessagingFragment();
    f = messagingFragment;
    break;
  }
  return f;
 }

 @Override
 public int getCount() {
  // TODO Auto-generated method stub
  return 3;
 }

}

