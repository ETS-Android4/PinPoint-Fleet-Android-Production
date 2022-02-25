package slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json_admin.VehicleAdminJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import admin.NewVehicleActivity;
import admin.ViewVehicleDetailsActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

public class VehicleAdminFragment extends Fragment {

	private static View view;
	VehicleAdminJSON vehicleJSON;
	ListView listView;
	List<HashMap<String, String>> aList;
	ImageButton btnAdd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}

		try {
			view = inflater.inflate(R.layout.fragment_vehicle_admin, container,
					false);
			vehicleJSON = new VehicleAdminJSON();
			listView = (ListView) view.findViewById(R.id.listVehicleDetails);
			btnAdd = (ImageButton) view.findViewById(R.id.imageViewAddVehicle);
			aList = new ArrayList<HashMap<String, String>>();

			new Utils();
			if (Utils.getConnectivityStatus(getActivity()) != 0) {
				new ViewVehicleInfoTask().execute();
			} else {
				ToastOnUI(getResources().getString(R.string.no_internet_connection));
			}
		} catch (Exception e) {
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				reloadFragment();
				String Sid = aList.get(position).get("id");
				Intent i = new Intent(getActivity(),
						ViewVehicleDetailsActivity.class);
				i.putExtra("id", Sid);
				startActivityForResult(i, 1);

			}
		});

		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),NewVehicleActivity.class);
				startActivityForResult(i, 1);
			}

		});
		return view;
	}

	ProgressDialog progDailog;

	class ViewVehicleInfoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(getActivity(),getResources().getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String response = vehicleJSON.viewVehicleInfo(getActivity());
				if (response != null) {
					JSONObject jbo = new JSONObject(response);
					JSONArray jArray = new JSONArray(
							jbo.getString("vehicleData"));
					if (jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							final JSONObject Jobj = new JSONObject(
									jArray.getString(i));
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("id", Jobj.getString("id"));
							hm.put("deviceName",
									"Name: " + Jobj.getString("device_name"));
							hm.put("TextName",Jobj.getString("device_name"));
							hm.put("deviceID",
									"ID: " + Jobj.getString("deviceID"));
							aList.add(hm);

						}
					}
				}
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			String[] sfrm = { "deviceID", "deviceName", "TextName" };
			int[] sto = { R.id.tvCustomDeviceID, R.id.tvCustomDeviceDesc,R.id.tv_first_later_list };
			
			SimpleAdapter adapter = new SimpleAdapter(getActivity()
					.getBaseContext(), aList,
					R.layout.custom_vehicle_list_layout, sfrm, sto);
			listView.setAdapter(adapter);
			progDailog.dismiss();

		}
	}

	protected void ToastOnUI(final String text) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Utils().showToast(getActivity(), text);
			}
		});
	}

	public void reloadFragment() {
		try {
			Fragment currentFragment = new VehicleAdminFragment();
			if (currentFragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, currentFragment)
						.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {

			if (resultCode == -1) {
				try {
					reloadFragment();
				} catch (Exception e) {
				}
			}
		}
	}

}
