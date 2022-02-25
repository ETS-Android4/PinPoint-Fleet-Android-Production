package admin;

import json_admin.AccountAdminJSON;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

public class AccountAdminFragment extends Fragment {

	private static View view;
	private AccountAdminJSON accountAdminJSON;
	// List<HashMap<String, String>> aList;
	Button btnUpdate;

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
			view = inflater.inflate(R.layout.fragment_account_admin, container,
					false);
			accountAdminJSON = new AccountAdminJSON();
			// aList = new ArrayList<HashMap<String, String>>();
			init(view);

			new Utils();
			if (Utils.getConnectivityStatus(getActivity()) != 0) {
				new ViewAccountInfoTask().execute();
			} else {
				ToastOnUI(getResources().getString(
						R.string.no_internet_connection));
			}
		} catch (Exception e) {
		}
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//-------
				String phn = etCntPhone.getText().toString().trim();
				int count = 0;
				for (int i = 0; i < phn.length(); i++) {
				    if (Character.isDigit(phn.charAt(i))) {
				        count++;  
				    }
				}
				//------------
				if(count <=10){
				if(etAccName.getText().toString().trim().length() != 0){
				new UpdateAccountInfoTask().execute();
				}else{
					ToastOnUI(getResources().getString(
							R.string.accountName_is_mandatory));
				}
				}else{
					ToastOnUI(getResources().getString(
							R.string.invalid_above_data));
				}
			}
		});

		return view;
	}

	ProgressDialog progDailog;

	// -----------------------------------------------------
	class ViewAccountInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lockScreenOrientation();
			progDailog = new Utils().showDialog(getActivity(), getResources()
					.getString(R.string.data_geathering));
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				String response = accountAdminJSON
						.ViewAccountAdmin(getActivity());
				return response;
			} catch (Exception e) {
				return null;
			}

		}

		@Override
		protected void onPostExecute(String response) {
			try {
				if (response != null) {
					JSONObject Jobj = new JSONObject(response);
					
					etAccId.setText(Jobj.getString("id"));
					etAccName.setText(Jobj.getString("account_name"));
					etAddress.setText(Jobj.getString("address"));
					etCity.setText(Jobj.getString("city"));
					etCntEmail.setText(Jobj.getString("contact_email"));
					etCntPerson.setText(Jobj.getString("contact_person"));
					etCountry.setText(Jobj.getString("country"));
					etMaxNrDevice.setText(Jobj.getString("max_devices"));
					etMaxUsers.setText(Jobj.getString("max_users"));
					etSkype.setText(Jobj.getString("skype"));
					etWebsite.setText(Jobj.getString("website"));
					etZIP.setText(Jobj.getString("zip"));
					etCntPhone.setText(Jobj.getString("contact_phone"));
					// ------------------------------------------
					String[] mDistanceArray = getResources().getStringArray(
							R.array.distance_unit);
					int selectedValue = 0;
					String dis_unit = Jobj.getString("units_distance").substring(Jobj.getString("units_distance").indexOf(".")+1);
					for (int i = 0; i < mDistanceArray.length; i++) {
						if (mDistanceArray[i].equalsIgnoreCase(dis_unit)) {
							selectedValue = i;
							break;
						}
					}
					spDistanceUnit.setSelection(selectedValue);
					// ------------------------------------------
					String[] mFuelArray = getResources().getStringArray(
							R.array.fuel_efficiency);
					selectedValue = 0;
					String economy_unit = Jobj.getString("units_economy").substring(Jobj.getString("units_economy").indexOf(".")+1);
					for (int i = 0; i < mFuelArray.length; i++) {
						if (mFuelArray[i].equalsIgnoreCase(economy_unit)) {
							selectedValue = i;
							break;
						}
					}
					spFuelEfficiency.setSelection(selectedValue);
					// ------------------------------------------
					String[] mPressureArray = getResources().getStringArray(
							R.array.pressure_unit);
					selectedValue = 0;
					String pressure_unit = Jobj.getString("units_pressure").substring(Jobj.getString("units_pressure").indexOf(".")+1);
					for (int i = 0; i < mPressureArray.length; i++) {
						if (mPressureArray[i].equalsIgnoreCase(pressure_unit)) {
							selectedValue = i;
							break;
						}
					}
					spPressureUnit.setSelection(selectedValue);
					// ------------------------------------------
					String[] mSpeedArray = getResources().getStringArray(
							R.array.speed_unit);
					selectedValue = 0;
					String speed_unit = Jobj.getString("units_speed").substring(Jobj.getString("units_speed").indexOf(".")+1);
					for (int i = 0; i < mSpeedArray.length; i++) {
						if (mSpeedArray[i]
								.equalsIgnoreCase(speed_unit)) {
							selectedValue = i;
							break;
						}
					}
					spSpeedUnit.setSelection(selectedValue);
					// ------------------------------------------
					String[] mTempArray = getResources().getStringArray(
							R.array.temp_unit);
					selectedValue = 0;
					String temp_unit = Jobj.getString("units_temp").substring(Jobj.getString("units_temp").indexOf(".")+1);
					for (int i = 0; i < mTempArray.length; i++) {
						if (mTempArray[i].equalsIgnoreCase(temp_unit)) {
							selectedValue = i;
							break;
						}
					}
					spTempUnit.setSelection(selectedValue);
					// ------------------------------------------
					String[] mVolumeArray = getResources().getStringArray(
							R.array.volume_unit);
					selectedValue = 0;
					String volume_unit = Jobj.getString("units_volume").substring(Jobj.getString("units_volume").indexOf(".")+1);
					for (int i = 0; i < mVolumeArray.length; i++) {
						if (mVolumeArray[i].equalsIgnoreCase(volume_unit)) {
							selectedValue = i;
							break;
						}
					}
					spVolumeUnit.setSelection(selectedValue);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			progDailog.dismiss();
		}
	}

	class UpdateAccountInfoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute(); //
			progDailog = new Utils().showDialog(getActivity(), getResources()
					.getString(R.string.updating));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				try {

					String sAccId = etAccId.getText().toString().trim();
					String sAccNm = etAccName.getText().toString().trim();
					String sAdd = etAddress.getText().toString().trim();
					String sCity = etCity.getText().toString().trim();
					String sCnt_Email = etCntEmail.getText().toString().trim();
					String sCntPerson = etCntPerson.getText().toString().trim();
					String sCountry = etCountry.getText().toString().trim();
					String sMaxDev = etMaxNrDevice.getText().toString().trim();
					String sMaxUsers = etMaxUsers.getText().toString().trim();
					String skype = etSkype.getText().toString().trim();
					String swebsite = etWebsite.getText().toString().trim();
					String sZip = etZIP.getText().toString().trim();
					String sPhone = etCntPhone.getText().toString().trim();

					String updDistanceUnit = spDistanceUnit.getSelectedItem()
							.toString();
					String updFuelEff = spFuelEfficiency.getSelectedItem()
							.toString();
					String updPressure = spPressureUnit.getSelectedItem()
							.toString();
					String updSpeed = spSpeedUnit.getSelectedItem().toString();
					String updTemp = spTempUnit.getSelectedItem().toString();
					String updVolume = spVolumeUnit.getSelectedItem()
							.toString();

					if(sPhone.matches("[0-9+-]*")){
					String response = accountAdminJSON.editAccountAdminInfo(
							getActivity(), sAccId, sAccNm, sAdd, sCity, sCnt_Email,
							sCntPerson, sCountry, sMaxDev, sMaxUsers, skype,
							swebsite, sZip, updDistanceUnit, updFuelEff,
							updPressure, updSpeed, updTemp, updVolume, sPhone);

					ToastOnUI(response);
					
					}else{
						ToastOnUI(getResources().getString(
								R.string.invalid_above_data));
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			progDailog.dismiss();
			reloadFragment();
		}
	}

	// -----------------------------------------------------

	EditText etAccId, etAccName, etWebsite, etCntPerson, etCntPhone,
			etCntEmail, etSkype, etMaxNrDevice, etMaxUsers, etAddress, etZIP,
			etCity, etCountry;
	Spinner spSpeedUnit, spDistanceUnit, spVolumeUnit, spFuelEfficiency,
			spPressureUnit, spTempUnit;

	protected void init(View view) {

		etAccId = (EditText) view.findViewById(R.id.etViewAccountID);
		etAccName = (EditText) view.findViewById(R.id.etViewAccountName);
		etWebsite = (EditText) view.findViewById(R.id.etViewWebsite);
		etCntEmail = (EditText) view.findViewById(R.id.etViewContactEmail);
		etCntPerson = (EditText) view.findViewById(R.id.etViewContactPerson);
		etCntPhone = (EditText) view.findViewById(R.id.etViewContactPhone);
		etSkype = (EditText) view.findViewById(R.id.etViewSkypeAcc);
		etMaxNrDevice = (EditText) view.findViewById(R.id.etViewMaxNoDevices);
		etMaxUsers = (EditText) view.findViewById(R.id.etViewMaxUsers);
		etCountry = (EditText) view.findViewById(R.id.etViewCountry);
		etAddress = (EditText) view.findViewById(R.id.etViewAddress);
		etZIP = (EditText) view.findViewById(R.id.etViewZipPostalCode);
		etCity = (EditText) view.findViewById(R.id.etViewCity);

		spDistanceUnit = (Spinner) view.findViewById(R.id.spDistanceUnit);
		spSpeedUnit = (Spinner) view.findViewById(R.id.spSpeedUnit);
		spVolumeUnit = (Spinner) view.findViewById(R.id.spVolumeUnit);
		spFuelEfficiency = (Spinner) view.findViewById(R.id.spFuelEfficiency);
		spPressureUnit = (Spinner) view.findViewById(R.id.spPressureUnit);
		spTempUnit = (Spinner) view.findViewById(R.id.spTemperatureUnit);

		btnUpdate = (Button) view.findViewById(R.id.viewBtnEdit);
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
			Fragment currentFragment = new AccountAdminFragment();
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
}
