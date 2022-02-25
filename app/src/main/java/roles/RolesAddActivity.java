package roles;

import java.util.ArrayList;

import json_admin.CheckBoxDeviceListData;
import json_admin.RolesJSON;
import json_admin.VehicleAdminJSON;

import org.json.JSONArray;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

public class RolesAddActivity extends Activity {

	ProgressDialog progDailog;
	ListView lvDeviceList;

	RolesJSON rolesJSON;
	VehicleAdminJSON vehicleJSON;
	CheckBox chkCreateGeofence, chkViewTracks, chkCommandsRestrictions, chkSos,
			chkConnectionLoss, chkGpsLoss, chkOverspeed, chkMileage, chkIdeals,
			chkGeofences, chkIOAct, chkLowPower, chkPowerCut, chkRestoreGPs,
			chkWakeUp, chkShakeAlarm, chkHardAcceleration, chkHarshBreak,
			chkMove, chkTrips, chkGeozones, chkIgnitionDetail, chkInputDetail,
			chkDistance, chkTemperature, chkPeriodicService, chkGpsRawData,
			chkReportOverspeed;
	MyCustomAdapter dataAdapter = null;

	EditText etUserRole;

	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_roles_add);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		init();
	}

	private void init() {
		vehicleJSON = new VehicleAdminJSON();
		rolesJSON = new RolesJSON();
		lvDeviceList = (ListView) findViewById(R.id.deviceList);
		// -----------
		View headerView = View.inflate(this, R.layout.roles_list_header, null);
		lvDeviceList.addHeaderView(headerView);
		View footerView = View.inflate(this, R.layout.roles_list_footer, null);
		lvDeviceList.addFooterView(footerView);
		// --------------
		etUserRole = (EditText) findViewById(R.id.etUserRole);
		chkCommandsRestrictions = (CheckBox) findViewById(R.id.chkCommandsRestrictions);
		chkConnectionLoss = (CheckBox) findViewById(R.id.chkConnectionLoss);
		chkDistance = (CheckBox) findViewById(R.id.chkDistance);
		chkGeofences = (CheckBox) findViewById(R.id.chkGeofences);
		chkCreateGeofence = (CheckBox) findViewById(R.id.chkCreateGeofence);
		chkGeozones = (CheckBox) findViewById(R.id.chkGeozones);
		chkGpsLoss = (CheckBox) findViewById(R.id.chkGpsLoss);
		chkGpsRawData = (CheckBox) findViewById(R.id.chkGpsRawData);
		chkHardAcceleration = (CheckBox) findViewById(R.id.chkHardAcceleration);
		chkHarshBreak = (CheckBox) findViewById(R.id.chkHarshBreak);
		chkIdeals = (CheckBox) findViewById(R.id.chkIdeals);
		chkIgnitionDetail = (CheckBox) findViewById(R.id.chkIgnitionDetail);
		chkInputDetail = (CheckBox) findViewById(R.id.chkInputDetail);
		chkIOAct = (CheckBox) findViewById(R.id.chkIOAct);
		chkLowPower = (CheckBox) findViewById(R.id.chkLowPower);
		chkMileage = (CheckBox) findViewById(R.id.chkMileage);
		chkMove = (CheckBox) findViewById(R.id.chkMove);
		chkOverspeed = (CheckBox) findViewById(R.id.chkOverspeed);
		chkPeriodicService = (CheckBox) findViewById(R.id.chkPeriodicService);
		chkPowerCut = (CheckBox) findViewById(R.id.chkPowerCut);
		chkReportOverspeed = (CheckBox) findViewById(R.id.chkReportOverspeed);
		chkRestoreGPs = (CheckBox) findViewById(R.id.chkRestoreGPs);
		chkShakeAlarm = (CheckBox) findViewById(R.id.chkShakeAlarm);
		chkSos = (CheckBox) findViewById(R.id.chkSos);
		chkTemperature = (CheckBox) findViewById(R.id.chkTemperature);
		chkTrips = (CheckBox) findViewById(R.id.chkTrips);
		chkViewTracks = (CheckBox) findViewById(R.id.chkViewTracks);
		chkWakeUp = (CheckBox) findViewById(R.id.chkWakeUp);
		setCheckboxChecked();
		
		new ViewVehicleInfoTask().execute();
	}

	private void setCheckboxChecked(){
		
		chkCommandsRestrictions.setChecked(true);
		chkConnectionLoss.setChecked(true);
		chkDistance.setChecked(true);
		chkGeofences.setChecked(true);
		chkCreateGeofence.setChecked(true);
		chkGeozones.setChecked(true);
		chkGpsLoss.setChecked(true);
		chkGpsRawData.setChecked(true);
		chkHardAcceleration.setChecked(true);
		chkHarshBreak.setChecked(true);
		chkIdeals.setChecked(true);
		chkIgnitionDetail.setChecked(true);
		chkInputDetail.setChecked(true);
		chkIOAct.setChecked(true);
		chkLowPower.setChecked(true);
		chkMileage.setChecked(true);
		chkMove.setChecked(true);
		chkOverspeed.setChecked(true);
		chkPeriodicService.setChecked(true);
		chkPowerCut.setChecked(true);
		chkReportOverspeed.setChecked(true);
		chkRestoreGPs.setChecked(true);
		chkShakeAlarm.setChecked(true);
		chkSos.setChecked(true);
		chkTemperature.setChecked(true);
		chkTrips.setChecked(true);
		chkViewTracks.setChecked(true);
		chkWakeUp.setChecked(true);
		
	}
	private class MyCustomAdapter extends ArrayAdapter<CheckBoxDeviceListData> {

		private ArrayList<CheckBoxDeviceListData> stateList;

		public MyCustomAdapter(Context context, int textViewResourceId,

		ArrayList<CheckBoxDeviceListData> stateList) {
			super(context, textViewResourceId, stateList);
			this.stateList = new ArrayList<CheckBoxDeviceListData>();
			this.stateList.addAll(stateList);
		}

		private class ViewHolder {
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.devicelist_single_custom_row,
						null);

				holder = new ViewHolder();
				holder.name = (CheckBox) convertView
						.findViewById(R.id.cbDeviceList);

				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						CheckBoxDeviceListData _state = (CheckBoxDeviceListData) cb
								.getTag();
						_state.setSelected(cb.isChecked());
					}
				});

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			CheckBoxDeviceListData state = stateList.get(position);

			holder.name.setText(state.getName());
			holder.name.setChecked(state.isSelected());

			holder.name.setTag(state);

			return convertView;
		}

	}

	class ViewVehicleInfoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String response = vehicleJSON.viewVehicleInfo(context);
				Log.v("RolesAddActivity", ":" + response);
				final ArrayList<CheckBoxDeviceListData> stateList = new ArrayList<CheckBoxDeviceListData>();
				if (response != null) {
					JSONObject jbo = new JSONObject(response);
					JSONArray jArray = new JSONArray(
							jbo.getString("vehicleData"));
					if (jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							final JSONObject Jobj = new JSONObject(
									jArray.getString(i));

							CheckBoxDeviceListData _states = new CheckBoxDeviceListData(
									Jobj.getString("id"),
									Jobj.getString("device_name"), true);
							stateList.add(_states);

						}
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dataAdapter = new MyCustomAdapter(context,
									R.layout.devicelist_single_custom_row,
									stateList);
							lvDeviceList.setAdapter(dataAdapter);
						}
					});
				}
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
		}
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

			StringBuffer responseText = new StringBuffer();
			ArrayList<CheckBoxDeviceListData> stateList = dataAdapter.stateList;

			for (int i = 0; i < stateList.size(); i++) {
				CheckBoxDeviceListData state = stateList.get(i);

				if (state.isSelected()) {

					responseText.append(state.getCode() + ",");

				}
			}
			if (responseText.toString().length() != 0) {
				saveRole(responseText.toString().substring(0,
						(responseText.toString().length() - 1)));
			} else {
				saveRole("");
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveRole(String selectedGroups) {
		String gName = etUserRole.getText().toString().trim();
		if (!gName.equals("")) {
			new AddRoleTask().execute(gName, selectedGroups);
		} else {
			ToastOnUI(getResources().getString(R.string.enter_abv_data_first));

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

	class AddRoleTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.adding));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String newRoleName = params[0];
				String selectedVehicleList = params[1];

				String response = rolesJSON.addEditRole(context, newRoleName,
						selectedVehicleList, chkCreateGeofence.isChecked(),
						chkViewTracks.isChecked(), chkSos.isChecked(),
						chkConnectionLoss.isChecked(), chkGpsLoss.isChecked(),
						chkOverspeed.isChecked(), chkMileage.isChecked(),
						chkIdeals.isChecked(), chkGeofences.isChecked(),
						chkIOAct.isChecked(), chkLowPower.isChecked(),
						chkPowerCut.isChecked(), chkRestoreGPs.isChecked(),
						chkWakeUp.isChecked(), chkShakeAlarm.isChecked(),
						chkHardAcceleration.isChecked(),
						chkHarshBreak.isChecked(), chkMove.isChecked(),
						chkCommandsRestrictions.isChecked(),
						chkTrips.isChecked(), chkGeozones.isChecked(),
						chkIgnitionDetail.isChecked(),
						chkInputDetail.isChecked(), chkDistance.isChecked(),
						chkTemperature.isChecked(),
						chkPeriodicService.isChecked(),
						chkGpsRawData.isChecked(),
						chkReportOverspeed.isChecked(), "", "Add");
				/*
				 * String response = groupJSON.createNewGroup(context,
				 * newgroupName, selectedGroupName);
				 */
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
}
