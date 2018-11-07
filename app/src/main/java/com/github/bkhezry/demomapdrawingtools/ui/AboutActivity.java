package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.utils.AppUtils;

public class AboutActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
        ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
    String versionName = "";
    try {
      versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      // do nothing
    }
    setTextWithLinks((TextView) findViewById(R.id.text_application_info), getString(R.string.application_info_text, versionName));
    setTextWithLinks((TextView) findViewById(R.id.text_developer_info), getString(R.string.developer_info_text));
    setTextWithLinks((TextView) findViewById(R.id.text_libraries), getString(R.string.libraries_text));
    setTextWithLinks((TextView) findViewById(R.id.text_license), getString(R.string.license_text));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setTextWithLinks(TextView textView, String htmlText) {
    AppUtils.setTextWithLinks(textView, AppUtils.fromHtml(htmlText));
  }
}

