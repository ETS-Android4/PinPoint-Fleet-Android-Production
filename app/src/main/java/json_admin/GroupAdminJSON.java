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

public class GroupAdminJSON {

	public String viewGroupAdmin(Context context) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=viewGroupAdmin";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID="+ accountID; 
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

			Log.v("ViewGroupAdmin","URL:"+url);
			Log.v("Post Parameters:",":"+parameters);
			Log.v("","");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			Log.v("(JSON) - GroupAdminInfo -Response:", response.toString());
			return response.toString();
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String createNewGroup(Context context, String groupName,String selectedGroup) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=newGroupAdmin";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID=" + accountID + "&g_newname="
					+ groupName+"&deviceIdlist="+selectedGroup;
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
			if (returnString.equals("true")) {
				returnString = context.getResources().getString(R.string.success);
			} else if(returnString.equals("false")){
				returnString = context.getResources().getString(R.string.fail);
			}else{
				returnString = context.getResources().getString(R.string.group_is_already_exist);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String editGroup(Context context,String group_id,
			String groupName, String device_list) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=editGroupAdmin";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&groupid=" + group_id + "&groupname="
					+ groupName + "&devicelist=" + device_list;
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
			if (returnString.equals("true")) {
				returnString =context.getResources().getString(R.string.success);
			} else {
				returnString = context.getResources().getString(R.string.fail);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String deleteGroup(Context context, String groupid) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=delGroupAdmin";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters ="&accountID="+accountID+"&groupid="
					+ groupid;
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
			if (returnString.equals("true")) {
				returnString = context.getResources().getString(R.string.success);
			} else {
				returnString = context.getResources().getString(R.string.fail);
			}
			return returnString;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
