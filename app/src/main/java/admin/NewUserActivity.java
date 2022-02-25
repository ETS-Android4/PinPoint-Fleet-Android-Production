package admin;

import json_admin.UserAdminJSON;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

public class NewUserActivity extends Activity {

	EditText etCompanyName, etWebsite, etCntPerson, etCntPhone,
			etSkype, etUsername, etPassword, etAddress, etZIP,etCity,etCountry;
	Spinner  spUserRole,spTimeZone;
	Context context = this;
	UserAdminJSON userAdminJSON;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		init();
	}

	
	private void init(){
		userAdminJSON = new UserAdminJSON();
		etCompanyName = (EditText)findViewById(R.id.etViewCompanyName);
		etWebsite = (EditText)findViewById(R.id.etViewWebsite);
		etCntPerson = (EditText)findViewById(R.id.etViewContactPerson);
		etCntPhone = (EditText)findViewById(R.id.etViewContactPhone);
		etSkype = (EditText)findViewById(R.id.etViewSkypeAcc);
		etUsername = (EditText)findViewById(R.id.etViewUsername);
		etPassword = (EditText)findViewById(R.id.etViewPassword);
	//	etConfirmPassword = (EditText)findViewById(R.id.etViewConfirmPassword);
		etAddress = (EditText)findViewById(R.id.etViewAddress);
		etZIP = (EditText)findViewById(R.id.etViewZipPostalCode);
		etCity = (EditText)findViewById(R.id.etViewCity);
		etCountry = (EditText)findViewById(R.id.etViewCountry);
		
		//spCountry = (Spinner)findViewById(R.id.spCountry);
		spTimeZone = (Spinner)findViewById(R.id.spTimeZone);
		spUserRole = (Spinner)findViewById(R.id.spUserRole);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_new_user) {
			//-------
			String phn = etCntPhone.getText().toString().trim();
			int count = 0;
			for (int i = 0; i < phn.length(); i++) {
			    if (Character.isDigit(phn.charAt(i))) {
			        count++;  
			    }
			}
			//------------
			if(count <=10){
				if(etUsername.getText().toString().trim().length() != 0 &&
						etPassword.getText().toString().trim().length() != 0){
					new AddUserTask().execute();	
				}else{
					ToastOnUI(getResources().getString(
							R.string.username_password_mandatory));
				}
				
			}else{
				ToastOnUI(getResources().getString(
						R.string.invalid_above_data));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	ProgressDialog progDailog;
	class AddUserTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					NewUserActivity.this,getResources().getString(R.string.adding));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				String updUname, updUPass,updPhone, updTimeZone,updSkype,
						updAddress,updCity,updCompany,updCntPerson,updWebsite,
						updUserRole,updZip,updCountry;

				updUname = etUsername.getText().toString().trim();
				updUPass = etPassword.getText().toString().trim();
				updPhone = etCntPhone.getText().toString().trim();
				updTimeZone = spTimeZone.getSelectedItem().toString();
				updSkype = etSkype.getText().toString().trim();
				updAddress = etAddress.getText().toString().trim();
				updCity = etCity.getText().toString().trim();
				updCompany = etCompanyName.getText().toString().trim();
				updCntPerson = etCntPerson.getText().toString().trim();
				updWebsite = etWebsite.getText().toString().trim();
				String[] s = getResources().getStringArray(R.array.userRole_values);
				updUserRole = s[spUserRole.getSelectedItemPosition()];
				Log.v("userRole",":"+updUserRole);
				updZip = etZIP.getText().toString().trim();
				updCountry = etCountry.getText().toString().trim();
				
				
				if(updUname != null){
					
				String response = userAdminJSON.ADDUserAdminInfo(context,
						updUname, updUPass, updCompany, updWebsite, updCntPerson,
						updPhone, updSkype, updAddress, updTimeZone, updCity,"Add",
						updUserRole,updZip,updCountry);
				ToastOnUI(response);
					
				}else{
					ToastOnUI(getResources().getString(R.string.username_is_mandatory));
				}
				
				
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			//btnEdit.setText("Edit");
			Intent returnIntent = new Intent();
			setResult(RESULT_OK,returnIntent);
			NewUserActivity.this.finish();
		}
	}
	
	protected void ToastOnUI(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Utils().showToast(context, text);
			}
		});
	}
}
