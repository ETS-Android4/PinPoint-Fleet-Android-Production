package admin;

import java.util.ArrayList;

import json_admin.CheckBoxDeviceListData;
import json_admin.GroupAdminJSON;
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

public class NewGroupActivity extends Activity {

	private Context context = this;
	ProgressDialog progDailog;
	EditText etGroupName;
	ListView lvDeviceList;
	VehicleAdminJSON vehicleJSON;
	MyCustomAdapter dataAdapter = null;
	GroupAdminJSON groupJSON;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		init();
	}

	private void init() {
		vehicleJSON = new VehicleAdminJSON();
		groupJSON = new GroupAdminJSON();

		etGroupName = (EditText) findViewById(R.id.etNewGroupName);
		lvDeviceList = (ListView) findViewById(R.id.deviceList);
		new ViewVehicleInfoTask().execute();

		/*
		 * lvDeviceList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view, int
		 * position, long id) { CheckBoxDeviceListData state =
		 * (CheckBoxDeviceListData) parent .getItemAtPosition(position); } });
		 */
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

	class AddGroupTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(context, getResources()
					.getString(R.string.adding_new_group));
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String newgroupName = params[0];
				String selectedGroupName = params[1];
				String response;
				 response = groupJSON.createNewGroup(context,
						newgroupName, selectedGroupName);
				ToastOnUI(response);
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			progDailog.dismiss();
			Intent i = new Intent();
			setResult(RESULT_OK,i);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int Iid = item.getItemId();
		switch (Iid) {

		case R.id.action_add_new_user:

			StringBuffer responseText = new StringBuffer();
			ArrayList<CheckBoxDeviceListData> stateList = dataAdapter.stateList;

			try{
			for (int i = 0; i < stateList.size(); i++) {
				CheckBoxDeviceListData state = stateList.get(i);

				if (state.isSelected()) {

					responseText.append(state.getCode() + ",");

				}
			}
			if(responseText.toString().length() != 0){
			saveGroup(responseText.toString().substring(0,
					(responseText.toString().length() - 1)));
			}else{
				saveGroup(" ");
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			break;
		}
		return super.onOptionsItemSelected(item);
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
									Jobj.getString("device_name"), false);
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

	private void saveGroup(String selectedGroups) {
		String gName = etGroupName.getText().toString().trim();
		if (!gName.equals("")) {
			new AddGroupTask().execute(gName, selectedGroups);
		} else {
			ToastOnUI(getResources().getString(R.string.enter_abv_data_first));

		}

	}
}
