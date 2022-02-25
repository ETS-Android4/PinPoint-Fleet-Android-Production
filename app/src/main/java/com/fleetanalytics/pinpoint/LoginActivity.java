package com.fleetanalytics.pinpoint;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONObject;

import json_parsing.LoginJSON;
import slider.SliderHomeActivity;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText etUser, etPass;
	Context context = this;

	// First start activity--------------------
	private int count;
	private SharedPreferences setSP;
	//public String url = "http://199.87.53.154:8080"; // Local - http://192.168.111.92:8080    // Live - http://199.87.53.154:8080
	public String url = "http://209.145.61.6:8080"; // Fleet analytics - Gary Server URL, Shared via Slack on 4th Feb
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// First start activity--------------------
		setSP = this.getSharedPreferences("MyApp", 0);
		count = setSP.getInt("count", 0);
		selectContentView();
		// ------------------

		init();
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (Utils.getConnectivityStatus(context) != 0) {
					String sUser = etUser.getText().toString();
					String sPass = etPass.getText().toString();
					if (!sUser.equals("") && !sPass.equals("")) {
						new DownloadFileAsync().execute(sUser, sPass);
					} else {
						ToastOnUI(getResources().getString(
								R.string.enter_abv_data_first));
					}
				} else {
					ToastOnUI(getResources().getString(
							R.string.no_internet_connection));
				}

			}
		});
	}

	public void init() {

		btnLogin = (Button) findViewById(R.id.btnLogin);
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		
		SharedPreferences saveUrl = getSharedPreferences("saveURL", 0);
		SharedPreferences.Editor e = saveUrl.edit();
		e.putString("url", url);
		e.commit();
		askForPermission();
	}

	private void askForPermission(){
		String[] permissions = {android.Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
				Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE
				};
		Permissions.check(this,permissions, null, null, new PermissionHandler() {
			@Override
			public void onGranted() {
				Log.v("Permission:", "Granted");
				//isPermissionGranted = true;
			}
		});
	}
	private void selectContentView() {
		// TODO Auto-generated method stub
		switch (count) {
		case 1:
			setCount(1);
			Intent i = new Intent(this, SliderHomeActivity.class);
			// i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			this.finish();
			break;
		default:
			setContentView(R.layout.activity_login);
			break;
		}
	}

	private void setCount(int count) {
		SharedPreferences.Editor e = setSP.edit();
		e.putInt("count", count);
		e.commit();
	}

	ProgressDialog progDailog;

	class DownloadFileAsync extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.authenticating));
		}

		@Override
		protected Void doInBackground(String... aur) {

			try {
				String user = aur[0];
				String pass = aur[1];
				String s = new LoginJSON().LoginMethod(context,user,
						pass);
				Log.v("LOGIN RESPONSE: ",":"+s);
				JSONObject jObject = new JSONObject(s);
				String success = jObject.getString("success");
				
				String userrole = "";
				if (success.equalsIgnoreCase("True")) {
					if(s.contains("userole")){
					userrole = jObject.getString("userole");
					}
					String account_id = jObject.getString("account_id");
					String time_zone = jObject.getString("account_tmz");
					String userID = jObject.getString("userId");
//					String loginId = "";
//					if(jObject.has("userId")){
//						loginId = jObject.getString("userId");	
//					}
					 						
					saveLoginCredential(url,userID,account_id,userrole,time_zone);
					setCount(1);
					Intent i = new Intent(LoginActivity.this,
							SliderHomeActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					LoginActivity.this.finish();
					
					
				}else{
					ToastOnUI(success);
				}
			} catch (Exception e) {
				e.printStackTrace();
				ToastOnUI(getResources().getString(R.string.error));
				if (progDailog != null) {
					progDailog.dismiss();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			// unlockScreenOrientation();
		}
	}

	public void saveLoginCredential(String ip_port, String userID,
					String account_id,String user_role,String timeZone) {
		SharedPreferences saveUrl = getSharedPreferences("saveURL", 0);
		SharedPreferences.Editor e = saveUrl.edit();
		e.putString("url", ip_port);
		e.putString("accountID", account_id);
		e.putString("userID", userID);
		e.putString("user_role",user_role);
		e.putString("time_zone",timeZone);
		//e.putString("loginId", LoginId);
		e.commit();
	}

	private void lockScreenOrientation() {
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/*
	 * private void unlockScreenOrientation() {
	 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR); }
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progDailog != null) {
			progDailog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (progDailog != null) {
			progDailog.dismiss();
		}
	}

	protected void ToastOnUI(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Utils().showToast(LoginActivity.this, text);
			}
		});
	}
}