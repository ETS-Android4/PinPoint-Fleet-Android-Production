package roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class EditRolesActivity extends Activity {

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
	List<String> SelecteDeviceList;
	String roleID, devList = null;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_roles_add);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		i = getIntent();
		roleID = i.getStringExtra("role_id");
		init();
	}

	private void init() {
		vehicleJSON = new VehicleAdminJSON();
		rolesJSON = new RolesJSON();
		lvDeviceList = (ListView) findViewById(R.id.deviceList);
//-----------
		View headerView = View.inflate(this, R.layout.roles_list_header, null);
		lvDeviceList.addHeaderView(headerView);
		View footerView = View.inflate(this, R.layout.roles_list_footer, null);
		lvDeviceList.addFooterView(footerView);
//--------------
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

		new ViewRolesDataTask().execute();
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

		case R.id.action_delete_user:
			new DeleteGroupTask().execute(roleID);
			break;
		case R.id.action_save_user:

			StringBuffer responseText = new StringBuffer();
			ArrayList<CheckBoxDeviceListData> stateList = dataAdapter.stateList;

			for (int i = 0; i < stateList.size(); i++) {
				CheckBoxDeviceListData state = stateList.get(i);

				if (state.isSelected()) {

					responseText.append(state.getCode() + ",");

				}
			}
			if (responseText.toString().length() != 0) {
				updateGRole(
						etUserRole.getText().toString().trim(),
						responseText.toString().substring(0,
								(responseText.toString().length() - 1)));
			} else {
				updateGRole(etUserRole.getText().toString(), "");
			}

			break;
		}

		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class DeleteGroupTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.please_wait));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String Id = params[0];
				String response = rolesJSON.deleteRoles(context, Id);
				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			finishActivity();
		}
	}

	private void finishActivity() {

		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	protected void ToastOnUI(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Utils().showToast(context, text);
			}
		});
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
				SelecteDeviceList = new ArrayList<String>();
				if (!devList.equals(null)) {
					SelecteDeviceList = Arrays.asList(devList.split(","));
				}
				String response = vehicleJSON.viewVehicleInfo(context);
				final ArrayList<CheckBoxDeviceListData> stateList = new ArrayList<CheckBoxDeviceListData>();
				if (response != null) {
					JSONObject jbo = new JSONObject(response);
					JSONArray jArray = new JSONArray(
							jbo.getString("vehicleData"));
					if (jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							final JSONObject Jobj = new JSONObject(
									jArray.getString(i));

							boolean b = false;
							for (int a = 0; a < SelecteDeviceList.size(); a++) {
								if (SelecteDeviceList.get(a).equals(
										Jobj.getString("id"))) {
									b = true;
									break;
								}
							}
							CheckBoxDeviceListData _states = new CheckBoxDeviceListData(
									Jobj.getString("id"),
									Jobj.getString("device_name"), b);
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
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
		}
	}

	class ViewRolesDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(Void... params) {

			final String response = rolesJSON.editRoleInfoDisplayData(context,
					roleID);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					parseEditResponse(response);
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			new ViewVehicleInfoTask().execute();
		}
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

	private String parseEditResponse(String response) {
		try {
			if (response != null) {
			/*	JSONArray jArray = new JSONArray(response);
				if (jArray.length() > 0) {
			*/		JSONObject Jobj = new JSONObject(response);
					etUserRole.setText(Jobj.getString("description"));
					//Jobj = new JSONObject(jArray.getString(1));
					devList = Jobj.getString("devicelist");
					chkCommandsRestrictions.setChecked(Boolean
							.parseBoolean(Jobj.getString("command")));
					chkConnectionLoss.setChecked(Boolean.parseBoolean(Jobj
							.getString("connection")));
					chkCreateGeofence.setChecked(Boolean.parseBoolean(Jobj
							.getString("geofences")));
					chkDistance.setChecked(Boolean.parseBoolean(Jobj
							.getString("distance")));
					chkGeofences.setChecked(Boolean.parseBoolean(Jobj
							.getString("notifygeo")));
					chkGeozones.setChecked(Boolean.parseBoolean(Jobj
							.getString("reportgeo")));
					chkGpsLoss.setChecked(Boolean.parseBoolean(Jobj
							.getString("GPSloss")));
					chkGpsRawData.setChecked(Boolean.parseBoolean(Jobj
							.getString("gpsrowdata")));
					chkHardAcceleration.setChecked(Boolean.parseBoolean(Jobj
							.getString("acceleration")));
					chkHarshBreak.setChecked(Boolean.parseBoolean(Jobj
							.getString("harshbrake")));
					chkIdeals.setChecked(Boolean.parseBoolean(Jobj
							.getString("idles")));
					chkIgnitionDetail.setChecked(Boolean.parseBoolean(Jobj
							.getString("ignition")));
					chkInputDetail.setChecked(Boolean.parseBoolean(Jobj
							.getString("input")));
					chkIOAct.setChecked(Boolean.parseBoolean(Jobj
							.getString("ioact")));
					chkLowPower.setChecked(Boolean.parseBoolean(Jobj
							.getString("lowpower")));
					chkMileage.setChecked(Boolean.parseBoolean(Jobj
							.getString("mileage")));
					chkMove.setChecked(Boolean.parseBoolean(Jobj
							.getString("move")));
					chkOverspeed.setChecked(Boolean.parseBoolean(Jobj
							.getString("overspeed")));
					chkPeriodicService.setChecked(Boolean.parseBoolean(Jobj
							.getString("periSer")));
					chkPowerCut.setChecked(Boolean.parseBoolean(Jobj
							.getString("powercut")));
					chkReportOverspeed.setChecked(Boolean.parseBoolean(Jobj
							.getString("overspeedrepo")));
					chkRestoreGPs.setChecked(Boolean.parseBoolean(Jobj
							.getString("restoregps")));
					chkShakeAlarm.setChecked(Boolean.parseBoolean(Jobj
							.getString("shakealarm")));
					chkSos.setChecked(Boolean.parseBoolean(Jobj
							.getString("sos")));
					chkTemperature.setChecked(Boolean.parseBoolean(Jobj
							.getString("temp")));
					chkTrips.setChecked(Boolean.parseBoolean(Jobj
							.getString("trips")));
					chkViewTracks.setChecked(Boolean.parseBoolean(Jobj
							.getString("track")));
					chkWakeUp.setChecked(Boolean.parseBoolean(Jobj
							.getString("wakeup")));

					devList = Jobj.getString("devicelist");

				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devList;

	}

	private void updateGRole(String UserRole, String selectedVehicles) {
		if (!UserRole.equals("")) {
			new EditRoleTask().execute(UserRole, selectedVehicles);
		} else {
			ToastOnUI(getResources().getString(R.string.enter_abv_data_first));
		}
	}

	class EditRoleTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.please_wait));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String userRole = params[0];
				String selectedVehicles = params[1];
				String response = rolesJSON.addEditRole(context, userRole,
						selectedVehicles, chkCreateGeofence.isChecked(),
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
						chkReportOverspeed.isChecked(), roleID, "Edit");

				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			finishActivity();
		}
	}
}
