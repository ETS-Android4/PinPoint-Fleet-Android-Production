package com.fleetanalytics.pinpoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import json_parsing.DeviceListData;

public class MapSettingsActivity extends Activity {

	Spinner spDeviceMap;
	static TextView tvFromDate, tvToDate,tvFromTime,tvToTime;
	//String backFrom, backTo;
	Button btnUpdate, btnCancel, btnLast24Hrs, btnLast3Days;

	//private int year;
	//private int month;
	//private int day;
	//static final int DATE_DIALOG_ID = 999;
	//static final int TIME_DIALOG_ID = 888;
	static boolean isFromDate = false;
	static boolean isFromTime = false;
	private ArrayAdapter<String> deviceList;
	ArrayList<DeviceListData> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map_settings);
		init();

		tvFromDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isFromDate = true;
				DialogFragment dialogfragment = new DatePickerDialogTheme();
				dialogfragment.show(getFragmentManager(), "From Date");
			}
		});

		tvToDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isFromDate = false;
				DialogFragment dialogfragment = new DatePickerDialogTheme();
				dialogfragment.show(getFragmentManager(), "To Date");
			}
		});

		tvFromTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isFromTime = true;
				DialogFragment timeDialogFragment = new TimePickerDialogTheme();
				timeDialogFragment.show(getFragmentManager(), "From Time");
			}
		});
		tvToTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isFromTime = false;
				DialogFragment timeDialogFragment = new TimePickerDialogTheme();
				timeDialogFragment.show(getFragmentManager(), "From Time");
			}
		});

		btnLast24Hrs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SimpleDateFormat todayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat todayTimeFormat = new SimpleDateFormat("HH:mm");

				Calendar last24Hours = Calendar.getInstance();
				last24Hours.set(Calendar.HOUR,-24);
				last24Hours.set(Calendar.MINUTE,0);
				tvFromDate.setText(todayDateFormat.format(last24Hours.getTime()));
				tvFromTime.setText(todayTimeFormat.format(last24Hours.getTime()));

				tvToDate.setText(todayDateFormat.format(new Date()));
				tvToTime.setText(todayTimeFormat.format(Calendar.getInstance().getTime()));

			}
		});

		btnLast3Days.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SimpleDateFormat todayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat todayTimeFormat = new SimpleDateFormat("HH:mm");

				Calendar last24Hours = Calendar.getInstance();
				last24Hours.set(Calendar.HOUR,-72);
				last24Hours.set(Calendar.MINUTE,0);
				tvFromDate.setText(todayDateFormat.format(last24Hours.getTime()));
				tvFromTime.setText(todayTimeFormat.format(last24Hours.getTime()));

				tvToDate.setText(todayDateFormat.format(new Date()));
				tvToTime.setText(todayTimeFormat.format(Calendar.getInstance().getTime()));
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MapSettingsActivity.this.finish();
			}
		});

		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				try {
					String deviceId = mList.get(spDeviceMap.getSelectedItemPosition()).getDeviceID().toString();
					Log.v("DeviceID:" + deviceId, ":" + deviceId);
					
					 String stvFromDate = tvFromDate.getText().toString(); 
					 String stvToDate = tvToDate.getText().toString();
					 String stvToTime = tvToTime.getText().toString();
					 String stvFromTime = tvFromTime.getText().toString();
					 
					//String stvFrom = backFrom;
					//String stvTo = backTo;
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					Date dfr = format.parse(stvFromDate);
					Date dto = format.parse(stvToDate);
					Date currentDate = new Date();
					
					if(!stvFromDate.equals("") && !stvFromTime.equals("") && !stvToDate.equals("") && !stvToTime.equals("")){
					
						if ((dfr.before(dto) || dfr.equals(dto)) && (dfr.before(currentDate) || dfr.equals(currentDate))
								&& (dto.before(currentDate) || dfr.equals(currentDate))) {

							Intent returnIntent = new Intent();
							returnIntent.putExtra("deviceID", deviceId);
							// Have set fromDate in 'To' parameter & toDate in 'From' parameter because of Web service has set it by mistake.
							//Because of we do not want to change API war file we have set it in mobile app end. DT:19th AUG 2017  
							returnIntent.putExtra("to", stvFromDate+" "+stvFromTime);
							returnIntent.putExtra("from", stvToDate+" "+stvToTime);
							returnIntent.putExtra("timeZone", "GMT");
							setResult(RESULT_OK, returnIntent);
							finish();

						} else {
							new Utils().showToast(getApplicationContext(),
									getResources().getString(R.string.invalid_date_range));
						}
						
					}else{
						new Utils().showToast(getApplicationContext(), getResources().getString(R.string.all_fields_are_required));
					}
				} catch (NullPointerException e) {
					new Utils().showToast(getApplicationContext(), getResources().getString(R.string.enter_date_first));
				} catch (Exception e) {
					e.printStackTrace();
					new Utils().showToast(getApplicationContext(), getResources().getString(R.string.error));
				}
			}
		});

	}

	public void init() {

		try {
			spDeviceMap = (Spinner) findViewById(R.id.spDeviceMap);
			// spTimeZone = (Spinner) findViewById(R.id.spTimeZone);
			tvFromDate = (TextView) findViewById(R.id.tvFromDate);
			tvToDate = (TextView) findViewById(R.id.tvToDate);
			tvFromTime = (TextView) findViewById(R.id.tvFromTime);
			tvToTime = (TextView) findViewById(R.id.tvToTime);
			btnUpdate = (Button) findViewById(R.id.dialogButtonUpdate);
			btnCancel = (Button) findViewById(R.id.dialogButtonCancel);
			btnLast24Hrs = (Button) findViewById(R.id.btnLast24Hrs);
			btnLast3Days = (Button) findViewById(R.id.btnLast3Days);
			ArrayList<String> devList = new ArrayList<String>();
			if (getIntent() != null && getIntent().hasExtra("deviceListArray")) {
				mList = getIntent().getParcelableArrayListExtra("deviceListArray");
				mList = getUniqueDeviceList(mList);
				Collections.sort(mList, new Comparator<DeviceListData>() {
					@Override
					public int compare(DeviceListData s1, DeviceListData s2) {
						return s1.getDisplayName().compareToIgnoreCase(s2.getDisplayName());
					}
				});
			}
			for (int in = 0; in < mList.size(); in++) {
				devList.add(mList.get(in).getDisplayName());
			}

			deviceList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, devList);
			deviceList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spDeviceMap.setAdapter(deviceList);
			
			SimpleDateFormat todayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat todayTimeFormat = new SimpleDateFormat("HH:mm");
			tvFromDate.setText(todayDateFormat.format(new Date()));
			tvToDate.setText(todayDateFormat.format(new Date()));
			tvFromTime.setText("00:00");
			tvToTime.setText(todayTimeFormat.format(Calendar.getInstance().getTime()));
			
		} catch (Exception e) {
			new Utils().showToast(getApplicationContext(), getResources().getString(R.string.error));
		}
	}

//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case DATE_DIALOG_ID:
//			// set date picker as current date
//			return new DatePickerDialog(this, datePickerListener, year, month, day);
//		case TIME_DIALOG_ID:
//			return new TimePickerDialog(this, timePickerListener,0,0,true);
//		}
//			
//		return null;
//	}

	@SuppressLint("ValidFragment")
	public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener{

		@Override
		 public Dialog onCreateDialog(Bundle savedInstanceState){
		 final Calendar calendar = Calendar.getInstance();
		 int year = calendar.get(Calendar.YEAR);
		 int month = calendar.get(Calendar.MONTH);
		 int day = calendar.get(Calendar.DAY_OF_MONTH);
		 DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
		 AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);
		 datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		 
		 return datepickerdialog;
		 }

		 public void onDateSet(DatePicker view, int year, int month, int day){
			 if (isFromDate) {
					tvFromDate.setText(String.format("%02d", month + 1) + "/" + String.format("%02d", day) + "/" + year);
					//backFrom = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);

				} else {
					tvToDate.setText(String.format("%02d", month + 1) + "/" + String.format("%02d", day) + "/" + year);
					//backTo = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
				}
		 }
		}

	@SuppressLint("ValidFragment")
	public static class TimePickerDialogTheme extends DialogFragment implements OnTimeSetListener{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			TimePickerDialog timepickerdialog = new TimePickerDialog(getActivity(),
					 AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,0,0,true);
			return timepickerdialog;
		}

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
			int hour = hourOfDay;
			int min = minute;
			if (isFromTime) {
				tvFromTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
				//backFrom = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
			} else {
				tvToTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
				//backTo = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
			}
		}
		
	}
	
//	private TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener() {
//		
//		@Override
//		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//			int hour = hourOfDay;
//			int min = minute;
//			if (isFromTime) {
//				tvFromTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
//				//backFrom = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
//			} else {
//				tvToTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
//				//backTo = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
//			}
//		}
//	};
//	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
//
//		// when dialog box is closed, below method will be called.
//		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//			year = selectedYear;
//			month = selectedMonth;
//			day = selectedDay;
//
//			if (isFromDate) {
//				// set selected date into textview
//				/*
//				 * tvFrom.setText(new StringBuilder().append(year).append("-")
//				 * .append(String.format("%02d", month + 1)).append("-")
//				 * .append(String.format("%02d", day)));
//				 */
//				tvFromDate.setText(String.format("%02d", month + 1) + "/" + String.format("%02d", day) + "/" + year);
//
//				backFrom = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
//
//			} else {
//				/*
//				 * backTo = String.format("%02d", month + 1) + "/"+
//				 * String.format("%02d", day)+"/"+year; tvTo.setText(new
//				 * StringBuilder().append(year).append("-")
//				 * .append(String.format("%02d", month + 1)).append("-")
//				 * .append(String.format("%02d", day)));
//				 */
//				tvToDate.setText(String.format("%02d", month + 1) + "/" + String.format("%02d", day) + "/" + year);
//
//				backTo = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
//			}
//		}
//	};

	private ArrayList<DeviceListData> getUniqueDeviceList(ArrayList<DeviceListData> list) {
		ArrayList<DeviceListData> uniqueList = new ArrayList<DeviceListData>();
		ArrayList<String> enemyIds = new ArrayList<String>();
		for (DeviceListData entry : list) {
			if (!enemyIds.contains(entry.getDeviceID())) {
				enemyIds.add(entry.getDeviceID());
				uniqueList.add(entry);
			}
		}
		return uniqueList;
	}
}
