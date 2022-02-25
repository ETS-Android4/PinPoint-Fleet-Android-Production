package admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class EditGroupActivity extends Activity {

	EditText etGroupId, etGroupName;
	//Button btnCancel;
	Context context = this;
	GroupAdminJSON groupJSON;
	VehicleAdminJSON vehicleJSON;
	MyCustomAdapter dataAdapter = null;
	
	ListView lvDeviceList;
	List<String> SelecteDeviceList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_group);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));
		init();

		/*btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditGroupActivity.this.finish();
			}
		});*/
	}

	private void init() {
		groupJSON = new GroupAdminJSON();
		vehicleJSON = new VehicleAdminJSON();
		//etDeviceList = (EditText) findViewById(R.id.etViewDeviceList);
		etGroupName = (EditText) findViewById(R.id.etViewGroupName);
		etGroupId = (EditText) findViewById(R.id.etViewGroupID);
		lvDeviceList = (ListView) findViewById(R.id.deviceList);
		//btnCancel = (Button) findViewById(R.id.dialogButtonCancel);

		Intent i = getIntent();
		etGroupId.setText(i.getStringExtra("Group_ID"));
		etGroupName.setText(i.getStringExtra("Group_Name"));
		String deviceList = i.getStringExtra("deviceList");
		
		
		SelecteDeviceList = Arrays.asList(deviceList.split(","));
		new ViewVehicleInfoTask().execute();
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
			String sGID = etGroupId.getText().toString();
			String sGNM = etGroupName.getText().toString().trim();
			
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
			updateGroup(sGID, sGNM,responseText.toString().substring(0,
					(responseText.toString().length() - 1)));
			}else{
				updateGroup(sGID, sGNM," ");
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			break;

		case R.id.action_delete_user:
			String sGID2 = etGroupId.getText().toString();
			new DeleteGroupTask().execute(sGID2);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	ProgressDialog progDailog;

	class EditGroupTask extends AsyncTask<String, Void, Void> {

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
				String newgroupId = params[0];
				String newgroupName = params[1];
				String device_list = params[2];
				String response = groupJSON.editGroup(context, newgroupId,
						newgroupName, device_list);
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
				String newgroupId = params[0];
				String response = groupJSON.deleteGroup(context, newgroupId);
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
	
	private void finishActivity(){
		
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
							for(int a = 0 ; a < SelecteDeviceList.size();a++ ){	
								if(SelecteDeviceList.get(a).equals(Jobj.getString("id"))){
									b = true;
									break;
								}
							}
							CheckBoxDeviceListData _states = new CheckBoxDeviceListData(
									Jobj.getString("id"),
									Jobj.getString("device_name"),b);
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

	private void updateGroup(String groupId,String groupName,String selectedGroups) {
		if (!groupName.equals("")) {
			new EditGroupTask().execute(groupId,groupName,selectedGroups);
		} else {
			ToastOnUI(getResources().getString(R.string.enter_abv_data_first));

		}

	}
}
