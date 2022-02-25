package admin;

import java.util.HashMap;

import json_admin.UserAdminJSON;

import org.json.JSONObject;

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

public class ViewUserAdminDetailsActivity extends Activity {

	UserAdminJSON userAdminJSON;
	Context context = this;
	HashMap<String, String> hm;
	EditText etphone, etPass, etCntPerson, etUsername, etCompany, etSkype,
			etAddress, etCity, etWebsite;
	Spinner spTimeZone;
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user_admin_details);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		init();
		Intent i = getIntent();
		userId = i.getStringExtra("username");
		Log.v("ViewUserAdminDetails:", ":" + userId);
		new DisplayUserInfoTask().execute(userId);
	}

	public void init() {
		userAdminJSON = new UserAdminJSON();
		hm = new HashMap<String, String>();

		etPass = (EditText) findViewById(R.id.etViewPassword);
		etphone = (EditText) findViewById(R.id.etViewContactPhone);
		etUsername = (EditText) findViewById(R.id.etViewUsername);
		etCntPerson = (EditText) findViewById(R.id.etViewContactPerson);
		etCompany = (EditText) findViewById(R.id.etViewCompany);
		etSkype = (EditText) findViewById(R.id.etViewSkypeAcc);
		etWebsite = (EditText) findViewById(R.id.etViewWebsite);
		etAddress = (EditText) findViewById(R.id.etViewAddress);
		etCity = (EditText) findViewById(R.id.etViewCity);

		spTimeZone = (Spinner) findViewById(R.id.spTimeZone);

		// editTextEnableDisable(false);
	}

	/*
	 * public void editTextEnableDisable(boolean b) {
	 * 
	 * etCity.setEnabled(b); etAddress.setEnabled(b); etWebsite.setEnabled(b);
	 * etSkype.setEnabled(b); etPass.setEnabled(b); etphone.setEnabled(b);
	 * etCompany.setEnabled(b); etCntPerson.setEnabled(b);
	 * spTimeZone.setEnabled(b); if (!b) { etUsername.setEnabled(b);
	 * setAlpha((float) 0.4); }
	 * 
	 * if (b) { setAlpha((float) 1.0); etUsername.requestFocus();
	 * InputMethodManager imm =
	 * (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	 * imm.showSoftInput(etUsername, InputMethodManager.SHOW_IMPLICIT); } }
	 * 
	 * public void setAlpha(float f){ etCntName.setAlpha(f);
	 * etemail.setAlpha(f); spIsActive.setAlpha(f); etNotifyEmail.setAlpha(f);
	 * etPass.setAlpha(f); etphone.setAlpha(f); etUsername.setAlpha(f);
	 * spTimeZone.setAlpha(f); etUserId.setAlpha(f); }
	 */
	ProgressDialog progDailog;

	class DisplayUserInfoTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewUserAdminDetailsActivity.this, getResources()
							.getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String userId = params[0];
				String response = userAdminJSON.editUserAdminDisplayData(
						context, userId);
				Log.v("response", ":" + response);
				if (response != null) {
					JSONObject Jobj = new JSONObject(response);

					hm.put("address", Jobj.getString("address"));
					hm.put("u_tmz", Jobj.getString("u_tmz"));
					hm.put("city", Jobj.getString("city"));
					hm.put("company", Jobj.getString("company"));
					hm.put("contact_person", Jobj.getString("contact_person"));
					hm.put("password", Jobj.getString("password"));

					hm.put("phone_number", Jobj.getString("phone_number"));
					hm.put("skype", Jobj.getString("skype"));
					hm.put("username", Jobj.getString("username"));
					hm.put("website", Jobj.getString("website"));

				}
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			try {
				String address = (hm.get("address").equals("null")) ? "" : hm
						.get("address");
				etAddress.setText(address);
				String city = (hm.get("city").equals("null")) ? "" : hm
						.get("city");
				etCity.setText(city);
				String company = (hm.get("company").equals("null")) ? "" : hm
						.get("company");
				etCompany.setText(company);
				String ContactPerson = (hm.get("contact_person").equals("null")) ? ""
						: hm.get("contact_person");
				etCntPerson.setText(ContactPerson);
				/*String pass = (hm.get("password").equals("null")) ? "" : hm
						.get("password");
				etPass.setText(pass);*/
				String phone = (hm.get("phone_number").equals("null")) ? ""
						: hm.get("phone_number");
				etphone.setText(phone);
				String skype = (hm.get("skype").equals("null")) ? "" : hm
						.get("skype");
				etSkype.setText(skype);
				String username = (hm.get("username").equals("null")) ? "" : hm
						.get("username");
				etUsername.setText(username);
				String website = (hm.get("website").equals("null")) ? "" : hm
						.get("website");
				etWebsite.setText(website);

				String[] mTempArray = getResources().getStringArray(
						R.array.time_zone);
				int selectedValue = 0;
				for (int i = 0; i < mTempArray.length; i++) {
					if (mTempArray[i].equals(hm.get("u_tmz"))) {
						selectedValue = i;
						break;
					}
				}
				spTimeZone.setSelection(selectedValue);

			} catch (Exception e) {
				e.printStackTrace();
			}
			progDailog.dismiss();

		}
	}

	class UpdateUserInfoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewUserAdminDetailsActivity.this, getResources()
							.getString(R.string.updating));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				String updUname, updUPass, updPhone, updTimeZone, updSkype, updAddress, updCity, updCompany, updCntPerson, updWebsite;

				updUname = etUsername.getText().toString().trim();
				updUPass = etPass.getText().toString().trim();
				updPhone = etphone.getText().toString().trim();
				updTimeZone = spTimeZone.getSelectedItem().toString();
				updSkype = etSkype.getText().toString().trim();
				updAddress = etAddress.getText().toString().trim();
				updCity = etCity.getText().toString().trim();
				updCompany = etCompany.getText().toString().trim();
				updCntPerson = etCntPerson.getText().toString().trim();
				updWebsite = etWebsite.getText().toString().trim();

				
				String response = userAdminJSON.EditUserAdminInfo(context,
						updUname, updUPass, updCompany, updWebsite,
						updCntPerson, updPhone, updSkype, updAddress,
						updTimeZone, updCity, "Edit");
				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			// btnEdit.setText("Edit");
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			ViewUserAdminDetailsActivity.this.finish();
		}
	}

	class DeleteUserTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewUserAdminDetailsActivity.this, getResources()
							.getString(R.string.deleting));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String userID = params[0];
				String response = userAdminJSON
						.deleteUserAdmin(context, userID);
				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			ViewUserAdminDetailsActivity.this.finish();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_user_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {

		case R.id.action_save_user:
			//-------
			String phn = etphone.getText().toString().trim();
			int count = 0;
			for (int i = 0; i < phn.length(); i++) {
			    if (Character.isDigit(phn.charAt(i))) {
			        count++;  
			    }
			}
			//------------
			if(count <=10){
				if(etUsername.getText().toString().trim().length() != 0 &&
						etPass.getText().toString().trim().length() != 0){
					new UpdateUserInfoTask().execute();
				}else{
					ToastOnUI(getResources().getString(
							R.string.username_password_mandatory));
				}
			}else{
				ToastOnUI(getResources().getString(
						R.string.invalid_above_data));
			}
			break;
		case R.id.action_delete_user:
			new DeleteUserTask().execute(userId);
			break;

		}
		return super.onOptionsItemSelected(item);
	}
}
