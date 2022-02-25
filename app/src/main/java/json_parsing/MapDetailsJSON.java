package json_parsing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fleetanalytics.pinpoint.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MapDetailsJSON {

	public String mapDetails(Context context, String deviceId, String dateFrom,
			String dateTo) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String timeZone = sp.getString("time_zone", "");
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=mapDetails";
			Log.v("mapDetailss:", "URL:"+url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&deviceID="
					+ deviceId + "&date_fr=" + dateFrom + "&date_to=" + dateTo+"&tmz="+timeZone;
			//---------			
			String urlParameters = "method=login&format=JSON" + parameters;
			Log.v("Parameters:", ":"+urlParameters);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.print("\nSending 'POST' request to URL : " + url);
			System.out.print("Post parameters : " + urlParameters);
			System.out.print("Response Code : " + responseCode);

			Log.v("Map_parameters:",":"+parameters);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			//new Utils().appendLog("REQ:"+url+"\nPARAMETER:"+urlParameters+"\nRESPONSE CODE:"+responseCode+"\n\nRESPONSE:"+response, "PPF_MapDetails");
			return response.toString();
		} catch (IOException e) {
			return null;
		}catch(Exception pe){
			Log.v("Parse Exception", ":"+pe.getLocalizedMessage());
			pe.getStackTrace();
			return null;
		}
	}

	public String getLastMapDetails(Context context, String deviceId) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String timeZone = sp.getString("time_zone", "");
		try {
			String url = jsonURL + "/PinPointFleet/opengts?reqType=getlastmapDetails";
			Log.v("getLastMapDetails:", "URL:"+url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&deviceID="
					+ deviceId +"&tmz="+timeZone;
			//---------			
			String urlParameters = "method=login&format=JSON" + parameters;
			Log.v("Parameters:", ":"+urlParameters);
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
		}catch(Exception pe){
			Log.v("Parse Exception", ":"+pe.getLocalizedMessage());
			pe.getStackTrace();
			return null;
		}
	}
	/*public String GroupMapDetails(Context context, String groupId,
			String TimeZone) {

		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String jsonURL = sp.getString("url", "");
		String accountID = sp.getString("accountID", "");

		try {
			String url = jsonURL + "/OpenGTS/opengts?reqType=groupmapDetails";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setConnectTimeout(5000);
			String parameters = "&accountID=" + accountID + "&groupID="
					+ groupId + "&date_tz=" + TimeZone;
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
		}
	}*/
}
