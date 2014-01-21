package com.example.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

  private SectionsPagerAdapter sectionsPagerAdapter;

  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GCMRegistrar.checkDevice(this);
    GCMRegistrar.checkManifest(this);
    String registrationId = GCMRegistrar.getRegistrationId(this);
    if (registrationId == "") {
      GCMRegistrar.register(this, getString(R.string.sender_id));
    }

    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    Fragment[] fragments = {
      new WebViewFragment("url0"),
      new WebViewFragment("url1"),
      new WebViewFragment("url2"),
      new WebViewFragment("url3")
    };
    CharSequence[] titles = {
      "tab0",
      "tab1",
      "tab2",
      "tab3"
    };
    sectionsPagerAdapter = new SectionsPagerAdapter(
      getSupportFragmentManager(),
      fragments,
      titles
    );

    viewPager = (ViewPager) findViewById(R.id.pager);
    viewPager.setAdapter(sectionsPagerAdapter);

    viewPager.setOnPageChangeListener(
      new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
          actionBar.setSelectedNavigationItem(position);
        }
      }
    );

    for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
      actionBar.addTab(
        actionBar.newTab()
          .setText(sectionsPagerAdapter.getPageTitle(i))
          .setTabListener(this)
      );
    }
    viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount() - 1);
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

}
