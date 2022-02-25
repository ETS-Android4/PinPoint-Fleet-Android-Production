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

public class AccountAdminJSON {

	public String ViewAccountAdmin(Context context){
		
		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=dafaultAccAdmin";
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
	
	public String editAccountAdminInfo(Context context, String id,String name,String address,
				String city,String email,String cnt_person,String country,String max_devices,
				String maxUsers,String skype,String website,String zip,String uDistance,
				String uFuelEco,String uPressure,String uSpeed,String uTemp,String uVolume,String phone) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		//String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=editAccAdmin";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&account_id="+id+"&account_name=" + name+"&email="+email+
					"&contact_person="+cnt_person+"&phone="+phone+"&skype="+skype+"&max_user="+maxUsers+
					"&max_device="+max_devices+"&address="+address+"&city="+city+
					"&country="+country+"&zip="+zip+"&website="+website+"&speed="+uSpeed+
					"&distance="+uDistance+"&volume="+uVolume+"&fuel="+uFuelEco+
					"&pressure="+uPressure+"&temp="+uTemp;
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
