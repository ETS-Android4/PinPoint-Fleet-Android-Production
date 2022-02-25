package admin;

import java.util.Calendar;
import java.util.HashMap;

import json_admin.VehicleAdminJSON;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

public class ViewVehicleDetailsActivity extends Activity {

	EditText etDeviceName, etDeviceID, etPhoneNo, etVehicleType, etVehicleReg,
			etRegExpDate, etCompanyName, etminFuelFill, etminFuelTheft,
			etTankCapacity, etAvgConsumption;
	Spinner spDevicetype;
	VehicleAdminJSON vehicleJSON;
	Context context = this;
	HashMap<String, String> hm;
	String id,oldname,olddeviceid;

	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_vehicle);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));

		init();
		Intent i = getIntent();
		id = i.getStringExtra("id");
		new DisplayVehicleInfoTask().execute(id);

		etRegExpDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
			
				if(hasFocus){
					hideSoftKeyboard();
					showDialog(DATE_DIALOG_ID);
				}
			}
		});
		
		etRegExpDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftKeyboard();
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	public void init() {
		vehicleJSON = new VehicleAdminJSON();
		hm = new HashMap<String, String>();

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		vehicleJSON = new VehicleAdminJSON();
		etDeviceID = (EditText) findViewById(R.id.etViewDeviceId);
		etPhoneNo = (EditText) findViewById(R.id.etViewContactPhone);
		etVehicleType = (EditText) findViewById(R.id.etViewVehicleTypeId);
		etAvgConsumption = (EditText) findViewById(R.id.etViewAvgConsumption);
		etTankCapacity = (EditText) findViewById(R.id.etViewTankCapacity);
		etminFuelTheft = (EditText) findViewById(R.id.etViewMinFuelTheft);
		// etDeviceTypeId = (EditText) findViewById(R.id.etViewDeviceTypeId);
		etCompanyName = (EditText) findViewById(R.id.etViewCompanyName);
		etDeviceName = (EditText) findViewById(R.id.etViewDeviceName);
		etRegExpDate = (EditText) findViewById(R.id.etViewRegExpDate);
		etVehicleReg = (EditText) findViewById(R.id.etViewVehicleRegistration);
		etminFuelFill = (EditText) findViewById(R.id.etViewMinFuelFilling);

		spDevicetype = (Spinner) findViewById(R.id.spDeviceType);

	}

	ProgressDialog progDailog;

	class DisplayVehicleInfoTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewVehicleDetailsActivity.this,
					getResources().getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String deviceId = params[0];
				String response = vehicleJSON.editVehicleInfoDisplayData(
						context, deviceId);
				if (response != null) {
					JSONObject jbo = new JSONObject(response);
					JSONObject Jobj = new JSONObject(
							jbo.getString("vehicleData"));

					oldname = Jobj.getString("name");
					olddeviceid = Jobj.getString("device_id");
					hm.put("phone_number", Jobj.getString("phone_number"));
					hm.put("vehicle_type", Jobj.getString("vehicle_type"));
					hm.put("avg_consumption", Jobj.getString("avg_consumption"));
					hm.put("tank_capacity", Jobj.getString("tank_capacity"));
					hm.put("vehicle_reg", Jobj.getString("vehicle_reg"));
					hm.put("device_id", Jobj.getString("device_id"));
					hm.put("min_theft", Jobj.getString("min_theft"));
					hm.put("device_type_id", Jobj.getString("device_type_id"));
					hm.put("id", Jobj.getString("id"));
					hm.put("company_name", Jobj.getString("company_name"));
					hm.put("name", Jobj.getString("name"));
					hm.put("registr_expir_date",
							Jobj.getString("registr_expir_date"));
					hm.put("min_fuelfill", Jobj.getString("min_fuelfill"));
				}
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			try {

				etAvgConsumption.setText(hm.get("avg_consumption"));
				etCompanyName.setText(hm.get("company_name"));
				etDeviceID.setText(hm.get("device_id"));
				etDeviceName.setText(hm.get("name"));
				etminFuelFill.setText(hm.get("min_fuelfill"));
				etminFuelTheft.setText(hm.get("min_theft"));
				etPhoneNo.setText(hm.get("phone_number"));
				etRegExpDate.setText(hm.get("registr_expir_date"));
				etTankCapacity.setText(hm.get("tank_capacity"));
				etVehicleReg.setText(hm.get("vehicle_reg"));
				etVehicleType.setText(hm.get("vehicle_type"));

				String[] mTempArray = getResources().getStringArray(
						R.array.vehicle_type_value);
				int selectedValue = 0;
				for (int i = 0; i < mTempArray.length; i++) {
					if (mTempArray[i].equals(hm.get("device_type_id"))) {
						selectedValue = i;
						break;
					}
				}
				spDevicetype.setSelection(selectedValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
			progDailog.dismiss();

		}
	}

	class UpdateVehicleInfoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewVehicleDetailsActivity.this,
					getResources().getString(R.string.updating));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				String devId = etDeviceID.getText().toString().trim();
				String phone = etPhoneNo.getText().toString().trim();
				String vehicle_type = etVehicleType.getText().toString().trim();
				String avgConsump = etAvgConsumption.getText().toString().trim();
				String tankCap = etTankCapacity.getText().toString().trim();
				String minTheft = etminFuelTheft.getText().toString().trim();
				// String devTypeId = etDeviceTypeId.getText().toString();
				String compName = etCompanyName.getText().toString().trim();
				String devName = etDeviceName.getText().toString().trim();
				String regExpDt = etRegExpDate.getText().toString().trim();
				String minFuelFill = etminFuelFill.getText().toString().trim();
				String vehReg = etVehicleReg.getText().toString().trim();

				//--------------
				minFuelFill = (minFuelFill.equals("")) ? "0" : minFuelFill;
				minTheft = (minTheft.equals("")) ? "0" : minTheft;
				tankCap = (tankCap.equals("")) ? "0" : tankCap;
				avgConsump = (avgConsump.equals("")) ? "0" : avgConsump;
				//----
				// ----
				String[] s = getResources().getStringArray(
						R.array.vehicle_type_value);
				String devTypeId = s[spDevicetype.getSelectedItemPosition()];
				// ----
				String response = vehicleJSON.editVehicleInfo(context, id,
						devId, phone, vehicle_type, avgConsump, tankCap,
						minTheft, devTypeId, compName, devName, regExpDt,
						minFuelFill, vehReg,oldname,olddeviceid);

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
			ViewVehicleDetailsActivity.this.finish();
		}
	}

	class DeleteVehicleInfoTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(
					ViewVehicleDetailsActivity.this,
					getResources().getString(R.string.deleting));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String deviceId = params[0];
				String response = vehicleJSON.deleteVehicleInfo(context,
						deviceId);
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
			ViewVehicleDetailsActivity.this.finish();
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
		int Iid = item.getItemId();
		switch (Iid) {

		case R.id.action_save_user:
			if (etDeviceID.getText().toString().trim().length() != 0 &&
			etDeviceName.getText().toString().trim().length() != 0 &&
			etVehicleType.getText().toString().trim().length() != 0) {
				
				//-------
				String phn = etPhoneNo.getText().toString().trim();
				int count = 0;
				for (int i = 0; i < phn.length(); i++) {
				    if (Character.isDigit(phn.charAt(i))) {
				        count++;  
				    }
				}
				//------------
				
			if(count <=10){
			new UpdateVehicleInfoTask().execute();
			}else{
				ToastOnUI(getResources().getString(
						R.string.invalid_above_data));
			}
			} else {
				ToastOnUI(this.getResources().getString(
						R.string.enter_abv_data_first));
			}
			break;
		case R.id.action_delete_user:
			new DeleteVehicleInfoTask().execute(id);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			etRegExpDate.setText(new StringBuilder().append(year).append("-")
					.append(String.format("%02d", month + 1)).append("-")
					.append(String.format("%02d", day)));

		}
	};

	private void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
				.getWindowToken(), 0);
	}
}
