package com.example.app;

import android.os.Bundle;
import android.app.Activity;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

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
  }

}
