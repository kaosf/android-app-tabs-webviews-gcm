package com.example.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;

import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

public class GCMIntentService extends GCMBaseIntentService {

  private static final String TAG = "com.example.app";

  @Override
  public void onRegistered(Context context, String registrationId) {
    Log.d(TAG, "on registered, registrationId: " + registrationId);

    // POST registrationId
    try {
      String uri = "";
      HttpPost hp = new HttpPost(uri);

      String key = "";
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair(key, registrationId));
      hp.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

      DefaultHttpClient client = new DefaultHttpClient();
      HttpResponse hr = client.execute(hp);
      int status = hr.getStatusLine().getStatusCode();

      if (status != HttpStatus.SC_OK) {
        throw new Exception("");
      }

      Log.d(TAG, EntityUtils.toString(hr.getEntity()));
    } catch (Exception e) {
      Log.e(TAG, "on registered ERROR!");
    }
  }

  @Override
  public void onUnregistered(Context context, String registrationId) {
    Log.d(TAG, "on unregistered, registrationId: " + registrationId);
  }

  @Override
  public void onMessage(Context context, Intent intent) {
    HashMap data = new HashMap();
    for (String key: intent.getExtras().keySet()) {
      String eventKey = key.startsWith("data.") ? key.substring(5) : key;
      data.put(eventKey, intent.getExtras().getString(key));
    }

    int icon = R.drawable.ic_launcher;
    CharSequence tickerText = (CharSequence) data.get("ticker");
    long when = System.currentTimeMillis();
    Log.d(TAG, "tickerText: " + tickerText);

    // Get the notification ID
    int notifyID = 1;
    String contentID = (String) data.get("id");
    if (contentID != null) {
      notifyID = Integer.parseInt(contentID);
    }

    CharSequence contentTitle = (CharSequence) data.get("title");
    CharSequence contentText = (CharSequence) data.get("message");

    Intent launcherintent = new Intent(this, MainActivity.class);
    launcherintent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

    // Save data
    JSONObject json = new JSONObject(data);
    String jsonString = json.toString();
    launcherintent.putExtra("PushNotifications-Data", jsonString);

    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, launcherintent, 0);

    Notification notification = new Notification(icon, tickerText, when);

    // Custom
    CharSequence vibrate = (CharSequence) data.get("vibrate");
    CharSequence sound = (CharSequence) data.get("sound");
    if ("default".equals(sound)) {
      notification.defaults |= Notification.DEFAULT_SOUND;
    }
    else if (sound != null) {
      String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
      final String packageName = GCMIntentService.class.getPackage().getName();
      String path = baseDir + "/" + packageName + "/sound/" + sound;

      File file = new File(path);
      if (file.exists()) {
        Uri soundUri = Uri.fromFile(file);
        notification.sound = soundUri;
      }
      else {
        notification.defaults |= Notification.DEFAULT_SOUND;
      }
    }
    if (vibrate != null) {
      notification.defaults |= Notification.DEFAULT_VIBRATE;
    }

    notification.defaults |= Notification.DEFAULT_LIGHTS;
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    nm.notify(notifyID, notification);
  }

  @Override
  public void onError(Context context, String errorId) {
    Log.d(TAG, "on error, :errorId: " + errorId);
  }

  @Override
  public boolean onRecoverableError(Context context, String errorId) {
    Log.d(TAG, "on recoverable error, errorId: " + errorId);
    return false;
  }

}
