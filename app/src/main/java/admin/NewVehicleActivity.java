package admin;

import java.util.Calendar;

import json_admin.VehicleAdminJSON;
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

public class NewVehicleActivity extends Activity {

	EditText etDeviceName, etDeviceID, etPhoneNo, etVehicleType, etVehicleReg,
			etRegExpDate, etCompanyName, etminFuelFill, etminFuelTheft,
			etTankCapacity, etAvgConsumption;
	Spinner spDevicetype;
	Context context = this;
	VehicleAdminJSON vehicleJSON;

	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_vehicle);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
		init();
		
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

	private void init() {
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
		//etDeviceTypeId = (EditText) findViewById(R.id.etViewDeviceTypeId);
		etCompanyName = (EditText) findViewById(R.id.etViewCompanyName);
		etDeviceName = (EditText) findViewById(R.id.etViewDeviceName);
		etRegExpDate = (EditText) findViewById(R.id.etViewRegExpDate);
		etVehicleReg = (EditText) findViewById(R.id.etViewVehicleRegistration);
		etminFuelFill = (EditText) findViewById(R.id.etViewMinFuelFilling);
		
		spDevicetype = (Spinner) findViewById(R.id.spDeviceType);
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
						.append(String.format("%02d",month + 1)).append("-").append(String.format("%02d",day)));

		}
	};
	
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
					new AddVehicleTask().execute();
				}else{
					ToastOnUI(getResources().getString(
							R.string.invalid_above_data));
				}
			} else {
				ToastOnUI(this.getResources().getString(
						R.string.enter_abv_data_first));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	ProgressDialog progDailog;

	class AddVehicleTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.adding));
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
				//String devTypeId = etDeviceTypeId.getText().toString();
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
				String[] s = getResources().getStringArray(R.array.vehicle_type_value);
				String devTypeId = s[spDevicetype.getSelectedItemPosition()];
				//----
				
				String response = vehicleJSON.createNewDevice(context, devId,
						phone, vehicle_type, avgConsump, tankCap, minTheft,
						devTypeId, compName, devName, regExpDt, minFuelFill,
						vehReg);
				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			Intent i = new Intent();
			setResult(RESULT_OK, i);
			finish();
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
	
	private void hideSoftKeyboard() {
	    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
}
