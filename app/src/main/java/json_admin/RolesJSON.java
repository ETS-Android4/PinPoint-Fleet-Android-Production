package json_admin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fleetanalytics.pinpoint.R;

public class RolesJSON {

	public String addEditRole(Context context, String roleName,String deviceList,
			boolean geofences, boolean track, boolean sos,boolean connection,
			boolean gpsLoss, boolean overspeed, boolean mileage,boolean ideals,
			boolean notifygeo, boolean ioact, boolean lowPower,boolean powerCut,
			boolean restoreGps,boolean wakeUp,boolean shakeAlarm,
			boolean acceleration, boolean harshBreak, boolean move,boolean cmmand,
			boolean trips, boolean reportGeo, boolean ignition,boolean input,
			boolean distance, boolean temp, boolean periSer, boolean gpsrowdata,
			boolean overspeedreport, String roleID, String actionType) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=addEditrole";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID="+accountID+"&description="+roleName+
					"&deviceList="+deviceList+"&geofences="+geofences+
					"&track="+track+"&sos="+sos+"&connection="+connection+
					"&GPSloss="+gpsLoss+"&overspeed="+overspeed+"&mileage="+mileage+
					"&idles="+ideals+"&notifygeo="+notifygeo+"&ioact="+ioact+
					"&lowpower="+lowPower+"&powercut="+powerCut+"&restoregps="+restoreGps+
					"&wakeup="+wakeUp+"&shakealarm="+shakeAlarm+"&acceleration="+acceleration+
					"&harshbrake="+harshBreak+"&move="+move+"&command="+cmmand+"&trips="+trips+
					"&reportgeo="+reportGeo+"&ignition="+ignition+"&input="+input+"&distance="+
					distance+"&temp="+temp+"&periSer="+periSer+"&gpsrowdata="+gpsrowdata+
					"&overspeedrepo="+overspeedreport+"&roleID="+roleID+"&actiontype="+actionType;
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
	
	
	public String viewRolesInfo(Context context) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=getroleslist";
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
	
	public String editRoleInfoDisplayData(Context context, String roleID) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=getEditRoleDetails";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String parameters = "&accountID="+accountID+"&roleID="+roleID;
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
	
	public String deleteRoles(Context context, String roleID) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=deleterole";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID="+accountID+"&roleID="+roleID;
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
