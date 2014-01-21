package com.example.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {
  private String uri;

  public WebViewFragment(String uri) {
    this.uri = uri;
  }

  @Override
  public View onCreateView(
    LayoutInflater inflater,
    ViewGroup container,
    Bundle savedInstanceState
  ) {
    View rootView = inflater.inflate(
      R.layout.fragment_web_view, container, false
    );
    WebView wv = (WebView)(rootView.findViewById(R.id.webview));
    wv.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
      }
    });
    wv.getSettings().setJavaScriptEnabled(true);
    wv.loadUrl(uri);
    return rootView;
  }
}
