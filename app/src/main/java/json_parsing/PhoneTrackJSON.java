package json_parsing;

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
import android.telephony.TelephonyManager;
import android.util.Log;

import com.fleetanalytics.pinpoint.R;

public class PhoneTrackJSON {

	public String phoneTrack(Context context, String latt_longi) {

		Log.v("phoneTrack","Called");
		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String userID = sp.getString("userID", "");
		String unqueID = getDeviceId(context);
		String deviceName = getDeviceName();

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=phonetrack";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&username=" + userID + "&uniqueID=" + unqueID
					+ latt_longi
					+ "&deviceName=" + deviceName + "&type=android";
			String urlParameters = "method=login&format=JSON" + parameters;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			Log.v("URL",":"+url);
			Log.v("parameters",":"+parameters);
			Log.v("response",":"+response);
			
			JSONObject jObject = new JSONObject(response.toString());
			String returnString = jObject.getString("success");
			if (returnString.equalsIgnoreCase("true")) {
				returnString = context.getResources().getString(
						R.string.success);
			} else if (returnString.equalsIgnoreCase("false")) {
				returnString = context.getResources().getString(R.string.fail);
			} else {
				returnString = context.getResources().getString(
						R.string.vehicle_is_already_exist);
			}
			return returnString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getDeviceId(Context context) {
		final String deviceId = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (deviceId != null) {
			return deviceId;
		} else {
			return android.os.Build.SERIAL;
		}
	}

	private static String getDeviceName() {
		return android.os.Build.MODEL;
	}
}
