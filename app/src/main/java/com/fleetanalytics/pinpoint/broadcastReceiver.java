package com.fleetanalytics.pinpoint;

import json_parsing.PhoneTrackJSON;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class broadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		final String cuu_loc = getCurrentLocation(context);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new PhoneTrackJSON().phoneTrack(context, cuu_loc);
			}
		}).start();
	}

	private String getCurrentLocation(Context context) {

		String lat_longi;
		String provider;
		Criteria criteria = new Criteria();
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null)
			lat_longi = "&lat=" + location.getLatitude() + "&lng="
					+ location.getLongitude();
		else {
			lat_longi = "&lat=0.000000&lng=0.000000";
		}

		return lat_longi;
	}
}
