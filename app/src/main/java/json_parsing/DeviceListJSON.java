package json_parsing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DeviceListJSON {

    public String DeviceListMethod(Context context) {

        SharedPreferences sp = context.getSharedPreferences("saveURL",
                Activity.MODE_PRIVATE);
        String jsonURL = sp.getString("url", "");
        String accountID = sp.getString("accountID", "");
        String userRole = sp.getString("user_role", null);
        String userID = sp.getString("userID", "");
        try {
            //OLD Code Till 21st Mar 2018
//			String url = jsonURL + "/PinPointFleet/opengts?reqType=deviceListnew";
//			String parameters = "&accountID=" + accountID;
//			if(userRole.equalsIgnoreCase("ROLE_USER")){
//				url = jsonURL + "/PinPointFleet/opengts?reqType=deviceList";
//				parameters = "&accountID=" + accountID+"&userrole="+userRole;
//			}
            //New Code
            String url = jsonURL + "/PinPointFleet/opengts?reqType=deviceListnew";
            String parameters = "&accountID=" + accountID;
            if (userRole.equalsIgnoreCase("ROLE_USER")) {
                url = jsonURL + "/PinPointFleet/opengts?reqType=getRoleBasedDeviceList";
                parameters = "&accountID=" + accountID + "&userrole=" + userRole + "&userID=" + userID;
            }
            Log.v("DeviceListMethod", ":" + url + "&" + parameters);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setConnectTimeout(5000);

            //String parameters = "&accountID=" + accountID;
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
            //Log.v("Device List Response:",":"+response.toString());
            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public String SendDeviceListgetMapPoints(Context context, String deviceList) {

        SharedPreferences sp = context.getSharedPreferences("saveURL",
                Activity.MODE_PRIVATE);
        String jsonURL = sp.getString("url", "");
        String timeZone = sp.getString("time_zone", "");
        //String loginId = sp.getString("loginId", "");
        try {
            String url = jsonURL + "/PinPointFleet/opengts?reqType=allmapDetails";
            //Log.v("AllMapDetailss:", "URL:"+url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setConnectTimeout(5000);
            //String parameters = "&tmz="+timeZone+"&devicelist=" + deviceList+"&userID="+loginId;
            String parameters = "&tmz=" + timeZone + "&devicelist=" + deviceList;
            String urlParameters = "method=login&format=JSON" + parameters;
            Log.v("AllMapDetail:Parameters:", url + ":" + urlParameters);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            Log.v("Response Code:" + responseCode, "Response:" + con.getResponseMessage());
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            //Log.v("FirstTimeMap:URL",":"+url);
            //Log.v("FirstTimeMap:Parameters",":"+urlParameters);
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
