package slider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.fleetanalytics.pinpoint.MapSettingsActivity;
import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import json_parsing.DeviceListData;
import json_parsing.DeviceListJSON;
import json_parsing.MapDetailsData;
import json_parsing.MapDetailsJSON;
import slider.Product.SubCategory;
import slider.Product.SubCategory.ItemList;

public class MappingFragmentNEW2 extends Fragment implements OnCheckedChangeListener {

    GoogleMap googleMap;
    private static View view;
    private LatLng point;
    boolean b = false;
    ArrayList<DeviceListData> dld;
    ArrayList<MapDetailsData> mdd;
    int arrayId;
    // -------------------
    public DrawerLayout dl;
    // public ExpandableListView xl;
    public ActionBarDrawerToggle adt;
    public List<String> alkitab;
    public HashMap<String, List<String>> data_alkitab;
    // public HashMap<String, HashMap<String, String>> data_alkitab;
    // public TreeMap<String, TreeMap<String, String>> data_alkita;
    public CharSequence title;
    private int lastExpandPosition = -1;
    private ExpandableDrawerAdapter adapt;
    String json_response;
    // --------------
    private SharedPreferences setMapRefreshSP;
    private SharedPreferences.Editor MapRefreshEditor;
    private String savedDevID_Map = "MAP_DEV_ID";
    private String savedDateFR_Map = "MAP_DEV_DATE_FR";
    private String savedDateTO_Map = "MAP_DEV_DATE_TO";
    private String savedTimeZone_Map = "MAP_DEV_TIME_ZONE";
    private String isGroupList = "IS_GROUP_LIST";
    SegmentedRadioGroup segmentText;

    // ---------
    private LinearLayout mLinearListView;
    private ScrollView scrollViewDeviceList;
    private ArrayList<Product> pProductArrayList;
    private ArrayList<SubCategory> pSubItemArrayList;
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            try {
                view = inflater.inflate(R.layout.fragment_mapping, container, false);
                dld = new ArrayList<DeviceListData>();
                setMapRefreshSP = getActivity().getPreferences(0);
                MapRefreshEditor = setMapRefreshSP.edit();
                MapRefreshEditor.clear().commit();

                // --------------------------------
                dl = (DrawerLayout) view.findViewById(R.id.drawer_layout);
                dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                adt = new ActionBarDrawerToggle(getActivity(), dl, R.drawable.device_list, R.string.device_list,
                        R.string.device_list) {
                };
                adt.setDrawerIndicatorEnabled(true);
                dl.setDrawerListener(adt);
                // --------------------------------
                new Utils();
                if (Utils.getConnectivityStatus(getActivity()) != 0) {
                    if (googleMap == null) {
                        //SupportMapFragment supportMapFragment = (SupportMapFragment) getActivity()
                        //		.getSupportFragmentManager().findFragmentById(R.id.map);

                        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                                .findFragmentById(R.id.map));
                        googleMap = supportMapFragment.getMap();
                        if (googleMap != null) {
                            new DeviceListAsync().execute();
                        }
                    }
                } else {
                    ToastOnUI(getResources().getString(R.string.no_internet_connection));
                }

                try {
                    googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker m) {

                            try {
                                arrayId = Integer.parseInt(m.getTitle());
                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                            return false;
                        }
                    });

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            String deviceName = mdd.get(arrayId).getId();
                            System.out.println("setOnInfoWindowClickListener Window Clicked======"+deviceName);

                            if (deviceName.length() > 0 && dld.size() > 0) {
                                for (int a = 0; a < dld.size(); a++) {
                                    if (dld.get(a).getDisplayName().equalsIgnoreCase(deviceName.trim())) {
                                        //new LastMapDetailsAsync().execute(dld.get(a).getDeviceID());
                                        SimpleDateFormat todayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                        SimpleDateFormat todayTimeFormat = new SimpleDateFormat("HH:mm");

                                        Calendar last24Hours = Calendar.getInstance();
                                        last24Hours.set(Calendar.HOUR,-24);
                                        last24Hours.set(Calendar.MINUTE,0);
                                        String deviceID = dld.get(a).getDeviceID();
                                        String from = todayDateFormat.format(last24Hours.getTime())+" "+todayTimeFormat.format(last24Hours.getTime());
                                        String to = todayDateFormat.format(new Date())+" "+todayTimeFormat.format(Calendar.getInstance().getTime());
                                        String timeZone = "GMT";
                                        System.out.println("setOnInfoWindowClickListener Window Clicked======"+deviceID);
                                        googleMap.clear();
                                        // saveMapPreference(deviceID, from, to, timeZone, false);
                                        MapDetailsAsync mda = new MapDetailsAsync();
                                        mda.execute(deviceID, to, from, timeZone, "false");
                                        break;
                                    }
                                }
                            }
                        }
                    });

                    googleMap.setInfoWindowAdapter(new

                                                           InfoWindowAdapter() {

                                                               @Override
                                                               public View getInfoWindow

                                                                       (Marker arg0) {
                                                                   return null;
                                                               }

                                                               @Override
                                                               public View getInfoContents

                                                                       (Marker arg0) {
                                                                   View v =

                                                                           inflater.inflate(

                                                                                   R.layout.info_window_layout, null);

                                                                   showCustomToolTip(v);
                                                                   return v;
                                                               }
                                                           });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InflateException e) {
            }

            Button btnSearchViaDeviceID = (Button) view.findViewById(R.id.btnSearchViaDeviceID);
            btnSearchViaDeviceID.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    searchViaDeviceIDAlertBox();
                }
            });

            Button btnList = (Button) view.findViewById(R.id.btnDeviceList);
            btnList.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {
                        dl.openDrawer(scrollViewDeviceList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Button btnSettings = (Button) view.findViewById

                    (R.id.btnSettings);
            btnSettings.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), MapSettingsActivity.class);
                    i.putParcelableArrayListExtra("deviceListArray", dld);
                    // startActivity(i);
                    startActivityForResult(i, 1);
                }
            });
            Button btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
            btnRefresh.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new Utils();
                    if (Utils.getConnectivityStatus(getActivity()) != 0) {

                        // Version 1.2
                        // googleMap.clear();
                        // new DeviceListAsync().execute();
                        // new
                        // FirsttimeAllDeviceLoad().execute(setMapRefreshSP.getString(savedDevID_Map,
                        // ""));

                        // Version 1.0
                        setMapRefreshSP = getActivity().getPreferences(0);
                        if (setMapRefreshSP.contains(savedDevID_Map)) {
                            googleMap.clear();

                            if (setMapRefreshSP.getBoolean(isGroupList, false)) {
                                try {
                                    Log.v("REFRESH Button", ":" + setMapRefreshSP.getString(savedDevID_Map, "") + ":");
                                    new FirsttimeAllDeviceLoad().execute(setMapRefreshSP.getString(savedDevID_Map, ""));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                new MapDetailsAsync().execute(setMapRefreshSP.getString(savedDevID_Map, ""),
                                        setMapRefreshSP.getString(savedDateFR_Map, ""),
                                        setMapRefreshSP.getString(savedDateTO_Map, ""),
                                        setMapRefreshSP.getString(savedTimeZone_Map, ""));
                            }
                        } else {
                            ToastOnUI(getResources().getString(R.string.no_data_found));
                        }

                    } else {
                        ToastOnUI(getResources

                                ().getString(

                                R.string.no_internet_connection));
                    }
                }
            });

        } else {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        }
        // ----------------------------
        segmentText = (SegmentedRadioGroup) view.findViewById(R.id.segment_text);
        segmentText.setOnCheckedChangeListener(this);
        // -----------------------------
        return view;
    }

    public void showCustomToolTip(View v) {

        // Getting reference to the TextView to set latitude
        TextView tvAddress = (TextView) v.findViewById(R.id.tv_address);
        TextView tvDate = (TextView) v.findViewById(R.id.tv_date);
        TextView tvDeviceName = (TextView) v.findViewById(R.id.tv_Device_name);
        TextView tvlatLong = (TextView) v.findViewById(R.id.tv_lat_lng);
        TextView tvSpeed = (TextView) v.findViewById(R.id.tv_speed);
        TextView tvEngineStatus = (TextView) v.findViewById(R.id.tv_engineStatus);

        try {
            String latFormat = String.format("%.2f / %.2f", Double.parseDouble(mdd.get(arrayId).getLat()),
                    Double.parseDouble(mdd.get(arrayId).getLongi()));
            tvlatLong.setText(latFormat);
            /*
             * tvlatLong.setText(mdd.get(arrayId).getLat() + " / " +
             * mdd.get(arrayId).getLongi());
             */
            // 28/11/16-----
            // SharedPreferences sp =
            // getActivity().getSharedPreferences("saveURL",
            // Activity.MODE_PRIVATE);
            // String timeZone = sp.getString("time_zone", "");
            // final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
            // HH:mm:ss Z");
            // sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            // sdf.parse(mdd.get(arrayId).getDate());
            // tvDate.setText(mdd.get(arrayId).getDate()+":"+);
            // -------------
            tvDate.setText(mdd.get(arrayId).getDate());
            tvDeviceName.setText(mdd.get(arrayId).getId());
            tvSpeed.setText(mdd.get(arrayId).getSpeed() + " mph");
            tvEngineStatus.setText(mdd.get(arrayId).getEngine_status());
            if (!mdd.get(arrayId).getAddress().equals("null")) {
                tvAddress.setText(mdd.get(arrayId).getAddress

                        ());
            } else {
                tvAddress.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastOnUI(getResources().getString

                    (R.string.error_textfield));
        }
    }

    class DeviceListAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new Utils().showDialog(getActivity(), getResources().getString(R.string.fetch_dev_list));
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                json_response = new DeviceListJSON().DeviceListMethod(getActivity());
                // parseJSONFile(json_response);
            } catch (Exception e) {
                e.printStackTrace();
                ToastOnUI(getResources().getString

                        (R.string.error));
                if (progDailog != null) {
                    progDailog.dismiss();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                loadData1(json_response);
                progDailog.dismiss();
                StringBuilder sb = new StringBuilder();
                Log.v("TOTAL DEVICES::::::", ":" + dld.size());
                for (int a = 0; a < dld.size(); a++) {
                    if (!dld.get(a).getDeviceID().equals("")) {
                        sb.append(dld.get(a).getDeviceID() + ",");
                    }
                }
                saveMapPreference(sb.toString(), getCurrentDate(), getCurrentDate(), "GMT", true);
                // 28 June 2017
                new FirsttimeAllDeviceLoad().execute(sb.toString());
            } catch (Exception e) {
                ToastOnUI(getResources().getString(R.string.no_data_found));
            }

        }
    }

    // ---------------------------------------------------------------
    class MapDetailsAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog mapDetailDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (dl.isDrawerOpen(scrollViewDeviceList)) {
                    dl.closeDrawer(scrollViewDeviceList);
                }

                if (googleMap != null) {
                    googleMap.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // lockScreenOrientation();
            mapDetailDialog = new Utils().showDialog(getActivity(),

                    getResources().getString(R.string.fetch_map_details));
        }

        @Override
        protected Void doInBackground(String... params) {
            // Fetch map Details---------------------
            String deviceId = params[0].toString();
            String date_from = params[1].toString();
            String date_to = params[2].toString();
            String time_zone = params[3].toString();
            boolean onlyLatestData = Boolean.parseBoolean(params[4]);
            String response = new MapDetailsJSON().mapDetails(getActivity(), deviceId, date_from, date_to);
            Log.v("MappingFragmentFinal-MapDetailsAsync", "deviceID:" + deviceId);
            Log.v("MapDetailsAsync-Response:", ":" + response);
            if (response != null) {
                try {
                    mdd = new ArrayList<MapDetailsData>();
                    JSONObject jbo = new JSONObject(response);
                    JSONArray jArray = new JSONArray(jbo.getString("mapData"));
                    if (jArray.length() > 0) {

                        if (onlyLatestData) {
                            int i = jArray.length() - 1;
                            final JSONObject Jobj = new JSONObject(jArray.getString(i));
                            MapDetailsData mapData = new MapDetailsData(Jobj.getString("id"), Jobj.getString("lat"),
                                    Jobj.getString("lon"), Jobj.getString("address"), Jobj.getString("speed"),
                                    Jobj.getString("date"), Jobj.getString("engine_status"), Jobj.getString("icon"));// Jobj.getString("engine_status")
                            mdd.add(mapData);
                            Log.v("Single Data: id:" + Jobj.getString("id"), "Date:" + Jobj.getString("date"));
                        } else {
                            for (int i = 0; i < jArray.length(); i++) {
                                final JSONObject Jobj = new JSONObject(jArray.getString(i));
                                MapDetailsData mapData = new MapDetailsData(Jobj.getString("id"), Jobj.getString("lat"),
                                        Jobj.getString("lon"), Jobj.getString("address"), Jobj.getString("speed"),
                                        Jobj.getString("date"), Jobj.getString("engine_status"),
                                        Jobj.getString("icon"));// Jobj.getString("engine_status")
                                mdd.add(mapData);
                                Log.v("id:" + Jobj.getString("id"), "Date:" + Jobj.getString("date"));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastOnUI(getResources().getString

                            (R.string.error));
                    if (mapDetailDialog != null) {
                        mapDetailDialog.dismiss();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            mapDetailDialog.dismiss();
            // unlockScreenOrientation();
            try {
                if (!mdd.isEmpty()) {
                    new PlotingTask().execute("false");

                } else {
                    ToastOnUI(getResources().getString(R.string.no_data_found));
                }
            } catch (Exception e) {

            }

        }
    }

    //---------------------------------------------------------------- 9th Aug 2017 : START
    class LastMapDetailsAsync extends AsyncTask<String, Void, Void> {
        ProgressDialog mapDetailDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (dl.isDrawerOpen(scrollViewDeviceList)) {
                    dl.closeDrawer(scrollViewDeviceList);
                }
                if (googleMap != null) {
                    googleMap.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // lockScreenOrientation();
            mapDetailDialog = new Utils().showDialog(getActivity(), getResources().getString(R.string.fetch_map_details));
        }

        @Override
        protected Void doInBackground(String... params) {
            String deviceId = params[0].toString();
            //String time_zone = params[3].toString();
            String response = new MapDetailsJSON().getLastMapDetails(getActivity(), deviceId);
            Log.v("LastMapDetailsAsync:" + deviceId + ":Response:", ":" + response);
            if (response != null) {
                try {
                    mdd = new ArrayList<MapDetailsData>();
                    JSONObject jbo = new JSONObject(response);
                    JSONArray jArray = new JSONArray(jbo.getString("mapData"));
                    if (jArray.length() > 0) {
                        int i = jArray.length() - 1;
                        final JSONObject Jobj = new JSONObject(jArray.getString(i));
                        MapDetailsData mapData = new MapDetailsData(Jobj.getString("id"), Jobj.getString("lat"),
                                Jobj.getString("lon"), Jobj.getString("address"), Jobj.getString("speed"),
                                Jobj.getString("date"), Jobj.getString("engine_status"), Jobj.getString("icon"));// Jobj.getString("engine_status")
                        mdd.add(mapData);
                        Log.v("Single Data: id:" + Jobj.getString("id"), "Date:" + Jobj.getString("date"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastOnUI(getResources().getString(R.string.error));
                    if (mapDetailDialog != null) {
                        mapDetailDialog.dismiss();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            mapDetailDialog.dismiss();
            // unlockScreenOrientation();
            try {
                if (!mdd.isEmpty()) {
                    new PlotingTask().execute("false");
                } else {
                    ToastOnUI(getResources().getString(R.string.no_data_found));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --------------------------------------------------------------- 9th Aug 2017 : END
    class PlotingTask extends AsyncTask<String, Void, String> {

        //ProgressDialog plotingProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // lockScreenOrientation();
//			plotingProgressDialog = new Utils().showDialog(getActivity(),
//					getResources().getString(R.string.ploting_points));
        }

        @Override
        protected String doInBackground(String... params) {
            String firstTime = params[0];
            try {
                if (mdd.size() > 0 && mdd != null) {
                    for (int d = 0; d < mdd.size(); d++) {
                        final MarkerOptions tempMarkerOption;
                        point = new LatLng(Double.parseDouble(mdd.get(d).getLat()),
                                Double.parseDouble(mdd.get(d).getLongi()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        // markerOptions.icon(createPureTextIcon(mdd.get(d).getId()));
                        if (firstTime.equalsIgnoreCase("true")) {
                            if (mdd.get(d).getEngine_status().equalsIgnoreCase("off")
                                    && mdd.get(d).getSpeed().equalsIgnoreCase("0")) {
                                // red Pin
                                // markerOptions.icon(createPureTextIcon(mdd.get(d).getId(),
                                // Color.RED));
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin_red));
                            } else if (mdd.get(d).getEngine_status().equalsIgnoreCase("on")
                                    && mdd.get(d).getSpeed().equalsIgnoreCase("0")) {
                                // yellow pin
                                // markerOptions.icon(createPureTextIcon(mdd.get(d).getId(),
                                // Color.YELLOW));
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin_yellow));
                            } else if (mdd.get(d).getEngine_status().equalsIgnoreCase("on")
                                    && !mdd.get(d).getSpeed().equalsIgnoreCase("0")) {
                                // green pin
                                // markerOptions.icon(createPureTextIcon(mdd.get(d).getId(),
                                // Color.GREEN));
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin_green));
                            } else {
                                // markerOptions.icon(createPureTextIcon(mdd.get(d).getId(),
                                // Color.RED));
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin_red));
                            }
                        } else {
                            if (mdd.get(d).getIcon_name() != null) {
                                String name = mdd.get(d).getIcon_name().substring(0,
                                        mdd.get(d).getIcon_name().length() - 4);
                                int id = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(id));

                            }
                        }
                        markerOptions.position(point);
                        markerOptions.title(String.valueOf(d));
                        tempMarkerOption = markerOptions;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mdd.size() == 1) {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17.0f));
                                } else {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
                                }
                                googleMap.getMaxZoomLevel();
                                googleMap.addMarker(tempMarkerOption);
                            }
                        });
                    }
                } else {
                    ToastOnUI(getResources().getString(R.string.no_data_found));
                }
                // drawPolyline();
            } catch (Exception e) {
                e.printStackTrace();
                ToastOnUI(getResources().getString(R.string.error));
//				if (plotingProgressDialog != null) {
//					plotingProgressDialog.dismiss();
//				}
            }

            return firstTime;
        }

        @Override
        protected void onPostExecute(String unused) {
            if (!unused.equalsIgnoreCase("true")) {
                drawPolyline();
            }
            //plotingProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == -1) {
                try {
                    String deviceID = data.getStringExtra("deviceID");
                    String from = data.getStringExtra("from");
                    String to = data.getStringExtra("to");
                    String timeZone = data.getStringExtra("timeZone");

                    googleMap.clear();
                    // saveMapPreference(deviceID, from, to, timeZone, false);
                    MapDetailsAsync mda = new MapDetailsAsync();
                    mda.execute(deviceID, from, to, timeZone, "false");

                } catch (Exception e) {
                    ToastOnUI(getResources().getString

                            (R.string.error));
                }
            }
        }
    }

    public void drawPolyline() {
        try {
            if (!mdd.isEmpty()) {

                ArrayList<LatLng> pointo = new ArrayList<LatLng>

                        ();
                PolylineOptions lineOptions = null;
                for (int d = 0; d < mdd.size(); d++) {
                    lineOptions = new PolylineOptions();
                    LatLng latlng = new LatLng

                            (Double.parseDouble(mdd.get(d).getLat()),

                                    Double.parseDouble(mdd.get(d).getLongi()));
                    pointo.add(latlng);

                    lineOptions.addAll(pointo);
                    lineOptions.width(4);
                    lineOptions.color(Color.RED);
                }

                googleMap.addPolyline(lineOptions);
            } else {
                ToastOnUI(getResources().getString

                        (R.string.no_data_found));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastOnUI(getResources().getString(R.string.error));
        }
    }

    protected String getCurrentDate() {
        // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder date = new StringBuilder().append(year).append("-").append(String.format("%02d", month + 1))
                .append("-").append(String.format("%02d", day));
        return date.toString();
    }

    protected void ToastOnUI(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Utils().showToast(getActivity(), text);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//		try {
//			Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
//			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//			ft.remove(fragment);
//            ft.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }

    private void loadData1(String json_response) {
        mLinearListView = (LinearLayout) view.findViewById(R.id.linear_listview);
        scrollViewDeviceList = (ScrollView) view.findViewById(R.id.scrollViewVehicleList);
        pProductArrayList = new ArrayList<Product>();

        SharedPreferences sp = getActivity().getSharedPreferences("saveURL", Activity.MODE_PRIVATE);
        String userRole = sp.getString("user_role", null);
        if (userRole.equalsIgnoreCase("ROLE_USER")) {
            // New Method - On 24th November 2016
            try {
                JSONObject mainObj = new JSONObject(json_response.toString());
                JSONArray arr = new JSONArray(mainObj.getString("List"));
                // JSONArray arr = new JSONArray(json_response.toString());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject Jobj = new JSONObject(arr.getString(i));
                    String mainName = Jobj.getString("name");
                    Log.v("NAME;;;;;;;", ":" + mainName);
                    pSubItemArrayList = new ArrayList<SubCategory>();
                    if (Jobj.has("List")) {
                        JSONArray grouplistArr = new
                                JSONArray(Jobj.getString("List").toString());
                        for (int j = 0; j < grouplistArr.length(); j++) {
                            JSONObject groupJobj = new
                                    JSONObject(grouplistArr.getString(j));
                            String groupName = groupJobj.getString("name");
                            JSONArray DevListArr = new
                                    JSONArray(groupJobj.getString("List").toString());
                            ArrayList<ItemList> mItemListArray = new
                                    ArrayList<ItemList>();
                            for (int k = 0; k < DevListArr.length(); k++) {
                                JSONObject grpDevLstJobj = new
                                        JSONObject(DevListArr.getString(k));
                                String grpDevListName = grpDevLstJobj.getString("name");
                                String grpDevListID =
                                        grpDevLstJobj.getString("deviceID");
                                mItemListArray.add(new ItemList(grpDevListName,
                                        grpDevListID));
                                DeviceListData dl = new DeviceListData(grpDevListID,
                                        grpDevListName);
                                dld.add(dl);
                            }

                            if (DevListArr.length() != 0) {
                                Log.v("GROUP~~~~" + DevListArr.length(), ":" + groupName);
                                pSubItemArrayList.add(new SubCategory(groupName,
                                        mItemListArray));
                            }
                        }
                        // pProductArrayList.add(new Product(mainName,
                        // pSubItemArrayList));
                    }

                    // 22nd March 2018
//					if (Jobj.has("List")) {
//						JSONArray devistArr = new JSONArray(Jobj.getString("List").toString());
//						ArrayList<ItemList> mItemListArray = new ArrayList<ItemList>();
//						for (int j = 0; j < devistArr.length(); j++) {
//							JSONObject OtherDevLstJobj = new JSONObject(devistArr.getString(j));
//							String grpDevListName = OtherDevLstJobj.getString("name");
//							String grpDevListID = OtherDevLstJobj.getString("deviceID");
//							mItemListArray.add(new ItemList(grpDevListName, grpDevListID));
//							DeviceListData dl = new DeviceListData(grpDevListID, grpDevListName);
//							dld.add(dl);
//						}
//						pSubItemArrayList.add(new SubCategory("Other Devices", mItemListArray));
//					}
                    pProductArrayList.add(new Product(mainName, pSubItemArrayList));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // OLD Method - Before 24th November 2016
            try {
                JSONArray arr = new JSONArray(json_response.toString());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject Jobj = new JSONObject(arr.getString(i));
                    String mainName = Jobj.getString("name");
                    Log.v("NAME;;;;;;;", ":" + mainName);
                    pSubItemArrayList = new ArrayList<SubCategory>();
                    if (Jobj.has("grouplist")) {
                        JSONArray grouplistArr = new JSONArray(Jobj.getString("grouplist").toString());
                        for (int j = 0; j < grouplistArr.length(); j++) {
                            JSONObject groupJobj = new JSONObject(grouplistArr.getString(j));
                            String groupName = groupJobj.getString("group");
                            JSONArray DevListArr = new JSONArray(groupJobj.getString("DeviceList").toString());
                            ArrayList<ItemList> mItemListArray = new ArrayList<ItemList>();
                            for (int k = 0; k < DevListArr.length(); k++) {
                                JSONObject grpDevLstJobj = new JSONObject(DevListArr.getString(k));
                                String grpDevListName = grpDevLstJobj.getString("name");
                                String grpDevListID = grpDevLstJobj.getString("deviceID");
                                mItemListArray.add(new ItemList(grpDevListName, grpDevListID));
                                DeviceListData dl = new DeviceListData(grpDevListID, grpDevListName);
                                dld.add(dl);
                            }

                            pSubItemArrayList.add(new SubCategory(groupName, mItemListArray));
                        }
                        // pProductArrayList.add(new Product(mainName,
                        // pSubItemArrayList));
                    }

                    if (Jobj.has("DeviceList")) {
                        JSONArray devistArr = new JSONArray(Jobj.getString("DeviceList").toString());
                        ArrayList<ItemList> mItemListArray = new ArrayList<ItemList>();
                        for (int j = 0; j < devistArr.length(); j++) {
                            JSONObject OtherDevLstJobj = new JSONObject(devistArr.getString(j));
                            String grpDevListName = OtherDevLstJobj.getString("name");
                            String grpDevListID = OtherDevLstJobj.getString("deviceID");
                            mItemListArray.add(new ItemList(grpDevListName, grpDevListID));
                            DeviceListData dl = new DeviceListData(grpDevListID, grpDevListName);
                            dld.add(dl);
                        }
                        pSubItemArrayList.add(new SubCategory("Other Devices", mItemListArray));
                    }
                    pProductArrayList.add(new Product(mainName, pSubItemArrayList));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < pProductArrayList.size(); i++) {

            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.row_first, null);

            final TextView mProductName = (TextView) mLinearView.findViewById(R.id.textViewName);
            final RelativeLayout mLinearFirstArrow = (RelativeLayout) mLinearView.findViewById(R.id.linearFirst);
            final ImageView mImageArrowFirst = (ImageView) mLinearView.findViewById(R.id.imageFirstArrow);
            final LinearLayout mLinearScrollSecond = (LinearLayout) mLinearView.findViewById(R.id.linear_scroll);

            if (isFirstViewClick == false) {
                mLinearScrollSecond.setVisibility(View.GONE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
            } else {
                mLinearScrollSecond.setVisibility(View.VISIBLE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
            }

            mLinearFirstArrow.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (isFirstViewClick == false) {
                        isFirstViewClick = true;
                        mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
                        mLinearScrollSecond.setVisibility(View.VISIBLE);

                    } else {
                        isFirstViewClick = false;
                        mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
                        mLinearScrollSecond.setVisibility(View.GONE);
                    }
                    return false;
                }
            });

            final String name = pProductArrayList.get(i).getpName();
            mProductName.setText(name);

            /**
             *
             */
            for (int j = 0; j < pProductArrayList.get(i).getmSubCategoryList().size(); j++) {

                LayoutInflater inflater2 = null;
                inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                TextView mSubItemName = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                ImageView GroupMapButton = (ImageView) mLinearView2.findViewById(R.id.groupMapButton);
                final RelativeLayout mLinearSecondArrow = (RelativeLayout) mLinearView2.findViewById(R.id.linearSecond);
                final ImageView mImageArrowSecond = (ImageView) mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird = (LinearLayout) mLinearView2
                        .findViewById(R.id.linear_scroll_third);

                if (isSecondViewClick == false) {
                    mLinearScrollThird.setVisibility(View.GONE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
                } else {
                    mLinearScrollThird.setVisibility(View.VISIBLE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
                }

                mLinearSecondArrow.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (isSecondViewClick == false) {
                            isSecondViewClick = true;
                            mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
                            mLinearScrollThird.setVisibility(View.VISIBLE);

                        } else {
                            isSecondViewClick = false;
                            mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
                            mLinearScrollThird.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });

                final String catName = pProductArrayList.get(i).getmSubCategoryList().get(j).getpSubCatName();
                mSubItemName.setText(catName);

                final StringBuilder sb = new StringBuilder();
                for (int k = 0; k < pProductArrayList.get(i).getmSubCategoryList().get(j).getmItemListArray()
                        .size(); k++) {

                    if (pProductArrayList.get(i).getmSubCategoryList().get(j).getmItemListArray().get(k)
                            .getItemName() != null) {
                        final String itemName = pProductArrayList.get(i).getmSubCategoryList().get(j)
                                .getmItemListArray().get(k).getItemName();
                        final String itemPrice = pProductArrayList.get(i).getmSubCategoryList().get(j)
                                .getmItemListArray().get(k).getItemPrice();

                        sb.append(itemPrice + ",");
                    }
                }

                GroupMapButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            if (dl.isDrawerOpen(scrollViewDeviceList)) {
                                dl.closeDrawer(scrollViewDeviceList);
                            }
                            googleMap.clear();
                            new Utils();
                            if (Utils.getConnectivityStatus(getActivity()) != 0) {

                                // saveMapPreference(sb.toString(),
                                // getCurrentDate(),getCurrentDate(),
                                // "GMT",true);
                                Log.v("Selected Group Device IDS:", ":" + sb.toString());
                                new FirsttimeAllDeviceLoad().execute(sb.toString());
                            } else {
                                ToastOnUI(getResources().getString(R.string.no_internet_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastOnUI(getResources().getString(R.string.error));
                        }

                    }
                });

                /**
                 *
                 */
                for (int k = 0; k < pProductArrayList.get(i).getmSubCategoryList().get(j).getmItemListArray()
                        .size(); k++) {

                    LayoutInflater inflater3 = null;
                    inflater3 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView3 = inflater3.inflate(R.layout.row_third, null);

                    TextView mItemName = (TextView) mLinearView3.findViewById(R.id.textViewItemName);
                    TextView mItemPrice = (TextView) mLinearView3.findViewById(R.id.textViewItemPrice);
                    final String itemName = pProductArrayList.get(i).getmSubCategoryList().get(j).getmItemListArray()
                            .get(k).getItemName();
                    final String itemPrice = pProductArrayList.get(i).getmSubCategoryList().get(j).getmItemListArray()
                            .get(k).getItemPrice();
                    mItemName.setText(itemName);
                    mItemPrice.setText(itemPrice);
                    mLinearView3.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            // Toast.makeText(getActivity(),"Name:"+itemName+"\nID:"+itemPrice,Toast.LENGTH_LONG).show();

                            try {
                                googleMap.clear();
                                new Utils();
                                if (Utils.getConnectivityStatus(getActivity()) != 0) {
                                    // saveMapPreference(itemPrice,getCurrentDate(), getCurrentDate(),"GMT", false);
                                    // 9th Aug 2017---- START
                                    //new MapDetailsAsync().execute(itemPrice, getCurrentDate(), getCurrentDate(), "GMT","true");
                                    new LastMapDetailsAsync().execute(itemPrice);
                                    // 9th Aug 2017 END
                                } else {
                                    ToastOnUI(getResources().getString(R.string.no_internet_connection));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastOnUI(getResources().getString(R.string.error));
                            }
                        }
                    });
                    mLinearScrollThird.addView(mLinearView3);
                }

                mLinearScrollSecond.addView(mLinearView2);

            }

            mLinearListView.addView(mLinearView);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adt.onConfigurationChanged(newConfig);
    }

    public class ExpandableDrawerAdapter extends BaseExpandableListAdapter {

        public final Context _context;
        public List<String> _alkitab, tempchild;
        public HashMap<String, List<String>> _data_alkitab;

        public ExpandableDrawerAdapter(Context context, List<String> alkitab,
                                       HashMap<String, List<String>> data_alkitab) {
            this._context = context;
            this._alkitab = alkitab;
            this._data_alkitab = data_alkitab;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._data_alkitab.get(this._alkitab.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            final String childText = (String) getChild(groupPosition, childPosition);
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_data, null);
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            if (childText.contains(":")
                    && childText.contains(_context.getResources().getString(R.string.right_arrow))) {
                String subStringName = childText.substring(0, childText.indexOf(":"));
                txtListChild.setText(subStringName);
            } else {
                txtListChild.setTextColor(Color.parseColor("#EC5B00"));
                txtListChild.setTypeface(null, Typeface.BOLD);
                txtListChild.setText(childText);
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._data_alkitab.get(this._alkitab.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._alkitab.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._alkitab.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)

                        this._context.getSystemService

                                (Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate

                        (R.layout.list_row_group, null);
            }
            ((ImageView) convertView.findViewById(R.id.image2)).setImageResource

                    (R.drawable.device_list);
            TextView b = (TextView) convertView.findViewById

                    (R.id.text1);
            b.setTypeface(null, Typeface.BOLD);
            b.setTextColor(getResources().getColor

                    (R.color.theme_color));
            b.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void saveMapPreference(String devId, String dtFr, String dtTo, String timeZone, boolean isGroupListt) {
        try {
            MapRefreshEditor = setMapRefreshSP.edit();
            MapRefreshEditor.putString(savedDevID_Map, devId);
            MapRefreshEditor.putString(savedDateFR_Map, dtFr);
            MapRefreshEditor.putString(savedDateTO_Map, dtTo);
            MapRefreshEditor.putString(savedTimeZone_Map, timeZone);
            MapRefreshEditor.putBoolean(isGroupList, isGroupListt);
            MapRefreshEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.button_one) {
            if (googleMap != null) {
                googleMap.setMapType

                        (GoogleMap.MAP_TYPE_NORMAL);
            }
        } else if (checkedId == R.id.button_two) {
            if (googleMap != null) {
                googleMap.setMapType

                        (GoogleMap.MAP_TYPE_SATELLITE);
            }
        } else if (checkedId == R.id.button_three) {
            if (googleMap != null) {
                googleMap.setMapType

                        (GoogleMap.MAP_TYPE_HYBRID);
            }
        }

    }

    private void searchViaDeviceIDAlertBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Search");
        //alertDialog.setMessage("Enter Device ID here");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter Device ID here");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //24109869
        alertDialog.setPositiveButton("SEARCH",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceIdInput = input.getText().toString();
                        if (deviceIdInput.length() > 0 && dld.size() > 0) {
                            for (int a = 0; a < dld.size(); a++) {
                                if (dld.get(a).getDisplayName().equalsIgnoreCase(deviceIdInput.trim())) {
                                    new LastMapDetailsAsync().execute(dld.get(a).getDeviceID());
                                    break;
                                } else if (!dld.get(a).getDisplayName().equalsIgnoreCase(deviceIdInput.trim()) &&
                                        a == (dld.size() - 1)) {
                                    Toast.makeText(getActivity(),
                                            getResources().getString(R.string.no_data_found),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    "Invalid input !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    // ----------------------------

    class FirsttimeAllDeviceLoad extends AsyncTask<String, Void, Void> {

        ProgressDialog firstTimeDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // lockScreenOrientation();
            firstTimeDialog = new Utils().showDialog(getActivity(), getResources().getString(R.string.please_wait));
        }

        @Override
        protected Void doInBackground(String... params) {

            // -----28 June 2017 Starts
            String[] saperatedDeviceList = params[0].split(",");
            mdd = new ArrayList<MapDetailsData>();
            StringBuilder sb20Modulo = new StringBuilder();
            for (int a = 0; a < saperatedDeviceList.length; a++) {
                sb20Modulo.append(saperatedDeviceList[a] + ",");
                if ((a % 20 == 0 && a != 0) || a == (saperatedDeviceList.length - 1)) {
                    try {
                        String sb = sb20Modulo.toString();
                        /*
                         * StringBuilder sb = new StringBuilder(); for (int a = 0; a <
                         * dld.size(); a++) { if (!dld.get(a).getDeviceID().equals(""))
                         * { sb.append(dld.get(a).getDeviceID() + ","); } }
                         */
                        // saveMapPreference(sb.toString(), getCurrentDate(),
                        // getCurrentDate(), "GMT", true);
                        if (dld.size() != 0) {
                            json_response = new DeviceListJSON().SendDeviceListgetMapPoints(getActivity(),
                                    sb.toString().substring(0, (sb.toString().length() - 1)));

                            //Log.v("AllMapDetails:RESPONSE:", ":" + json_response);
                            if (json_response != null) {
                                // 28 June 2017 Starts
                                // mdd = new ArrayList<MapDetailsData>();
                                // 28 June 2017 Ends
                                JSONObject jbo = new JSONObject(json_response);
                                if (jbo.has("mapData")) {
                                    JSONArray jArray = new JSONArray(jbo.getString("mapData"));
                                    if (jArray.length() > 0) {
                                        for (int i = 0; i < jArray.length(); i++) {
                                            final JSONObject Jobj = new JSONObject(jArray.getString(i));
                                            MapDetailsData mapData = new MapDetailsData(Jobj.getString("id"), Jobj.getString("lat"),
                                                    Jobj.getString("lon"), Jobj.getString("address"), Jobj.getString("speed"),
                                                    Jobj.getString("date"), Jobj.getString("engine_status"), null);// Jobj.getString("engine_status")
                                            mdd.add(mapData);
                                        }
                                    }
                                } else {
                                    ToastOnUI(getResources().getString(R.string.error));
                                }
                            }
                        }
                        sb20Modulo = new StringBuilder();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastOnUI(getResources().getString(R.string.error));
                        if (firstTimeDialog != null) {
                            firstTimeDialog.dismiss();
                        }
                    }
                }
            }
            // -----28 June 2017 Ends
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            firstTimeDialog.dismiss();
            try {
                Log.v("MDD ARRAY COUNT:", ":" + mdd.size());
                if (!mdd.isEmpty()) {
                    new PlotingTask().execute("true");

                } else {
                    ToastOnUI(getResources().getString(R.string.no_data_found));
                }
            } catch (Exception e) {

            }
        }
    }

    // public BitmapDescriptor createPureTextIcon(String text, int color) {
    //
    // Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // Adapt to your
    // needs
    // textPaint.setColor(Color.BLACK);
    // textPaint.setTextSize(18);
    // //textPaint.setColor(Color.BLACK);
    // float textWidth = textPaint.measureText(text)+20;
    // float textHeight = textPaint.getTextSize()+20;
    // int width = (int) (textWidth);
    // int height = (int) (textHeight);
    // textPaint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
    //
    // Bitmap image = Bitmap.createBitmap(width,height, Config.ARGB_8888);
    // Canvas canvas = new Canvas(image);
    // canvas.translate(0, height);
    // // For development only:
    // // Set a background in order to see the
    // // full size and positioning of the bitmap.
    // // Remove that for a fully transparent icon.
    // //canvas.drawColor(Color.LTGRAY);
    // canvas.drawColor(color);
    // //canvas.drawColor(android.R.color.transparent);
    // canvas.drawText(text, 10, -10, textPaint);
    //
    // return BitmapDescriptorFactory.fromBitmap(image);
    // }
}
