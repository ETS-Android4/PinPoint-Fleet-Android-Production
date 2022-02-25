package admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json_admin.UserAdminJSON;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class UserAdminFragment extends Fragment {

	private static View view;
	UserAdminJSON userAdminJSON;
	ListView listView;
	List<HashMap<String, String>> aList;
	ImageButton btnAdd;
	boolean isEditPermission, isAddDelPermission;
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
			userAdminJSON = new UserAdminJSON();
			listView = (ListView) view.findViewById(R.id.listVehicleDetails);
			btnAdd = (ImageButton) view.findViewById(R.id.imageViewAddVehicle);
			aList = new ArrayList<HashMap<String, String>>();

			new Utils();
			if (Utils.getConnectivityStatus(getActivity()) != 0) {
				new ViewUserAdminTask().execute();
			} else {
				ToastOnUI(getResources().getString(R.string.no_internet_connection));
			}
		} catch (Exception e) {
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				String did = aList.get(position).get("username");
				String finalId = did.substring(did.indexOf(":") + 2);
				Intent i = new Intent(getActivity(),
						ViewUserAdminDetailsActivity.class);
				i.putExtra("username", finalId);
				//startActivity(i);
				startActivityForResult(i, 1);
			}
		});

		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						NewUserActivity.class);
				startActivityForResult(i, 1);
				
				//startActivity(new Intent(getActivity(),NewUserActivity.class));
			}
		});
		return view;
	}

	ProgressDialog progDailog;

	class ViewUserAdminTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(getActivity(),getResources().getString(R.string.data_geathering));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String response = userAdminJSON
						.viewUserAdminDetail(getActivity());
				if (response != null) {
					JSONObject jbo = new JSONObject(response);
					JSONArray jArray = new JSONArray(
							jbo.getString("userAdminInfo"));
					if (jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							final JSONObject Jobj = new JSONObject(
									jArray.getString(i));
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("username","Name: "+ Jobj.getString("username"));
							hm.put("contact_person", Jobj.getString("contact_person"));
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
			String[] sfrm = { "contact_person","username", "contact_person"};
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
			Fragment currentFragment = new UserAdminFragment();
			if (currentFragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, currentFragment).commit();
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
