package json_admin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.fleetanalytics.pinpoint.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class VehicleAdminJSON {

	public String createNewDevice(Context context, String deviceId,String phoneNo,
			String vehicle_type, String avg_consumption, String tank_capacity,
			String min_theft, String device_type_id, String company_name,String dev_name,
			String reg_exp_date, String min_fuel_fill, String vehicleReg) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=addVehicle";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&account_id="+accountID+"&device_id="+deviceId+"&phone_number="+phoneNo+
					"&vehicle_type="+vehicle_type+"&avg_consumption="+avg_consumption+
					"&tank_capacity="+tank_capacity+"&min_theft="+min_theft+"&device_type_id="+
					device_type_id+"&company_name="+company_name+"&name="+dev_name+
					"&registr_expir_date="+reg_exp_date+"&min_fuelfill="+min_fuel_fill+
					"&vehicle_reg="+vehicleReg;
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			Log.v("(JSON) - Response:", response.toString());

			JSONObject jObject = new JSONObject(response.toString());
			String returnString = jObject.getString("success");
			if (returnString.equalsIgnoreCase("true")) {
				returnString = context.getResources().getString(R.string.success);
			} else if(returnString.equalsIgnoreCase("false")){
				returnString = context.getResources().getString(R.string.fail);
			}else{
				returnString = context.getResources().getString(R.string.vehicle_is_already_exist);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	
	public String viewVehicleInfo(Context context) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=vehicleDetails";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID=" + accountID;
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			return response.toString();
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public String editVehicleInfoDisplayData(Context context, String id) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		//String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=getEditVehicleData";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String parameters = "&id="+id;
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			return response.toString();
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public String editVehicleInfo(Context context,String id, String deviceId,String phoneNo,
			String vehicle_type, String avg_consumption, String tank_capacity,
			String min_theft, String device_type_id, String company_name,String dev_name,
			String reg_exp_date, String min_fuel_fill, String vehicleReg,String oldname,String olddeviceid) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=updateVehicle";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&id="+id+"&device_id="+deviceId+"&phone_number="+phoneNo+
					"&vehicle_type="+vehicle_type+"&avg_consumption="+avg_consumption+
					"&tank_capacity="+tank_capacity+"&min_theft="+min_theft+"&device_type_id="+
					device_type_id+"&company_name="+company_name+"&name="+dev_name+
					"&registr_expir_date="+reg_exp_date+"&min_fuelfill="+min_fuel_fill+
					"&vehicle_reg="+vehicleReg+"&account_id="+accountID+"&oldname="+oldname+
					"&oldDeviceid="+olddeviceid;
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			JSONObject jObject = new JSONObject(response.toString());
			String returnString = jObject.getString("success");
			if (returnString.equals("true")) {
				returnString = context.getResources().getString(R.string.success);
			} else if(returnString.equalsIgnoreCase("false")){
				returnString = context.getResources().getString(R.string.fail);
			}else{
				returnString = context.getResources().getString(R.string.vehicle_is_already_exist);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public String deleteVehicleInfo(Context context, String id) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		//String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=deleteVehicle";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&id="+id;
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			JSONObject jObject = new JSONObject(response.toString());
			String returnString = jObject.getString("success");
			if (returnString.equals("true")) {
				returnString = context.getResources().getString(R.string.success);
			} else {
				returnString = context.getResources().getString(R.string.fail);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}
}
