package com.example.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
  private Fragment[] fragments;

  private CharSequence[] titles;

  public SectionsPagerAdapter(
    FragmentManager fm, Fragment[] fragments, CharSequence[] titles
  ) {
    super(fm);
    this.fragments = fragments;
    this.titles = titles;
  }

  @Override
  public Fragment getItem(int position) {
    return fragments[position];
  }

  @Override
  public int getCount() {
    return fragments.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }
}
