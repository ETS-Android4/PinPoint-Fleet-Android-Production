package slider;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fleetanalytics.pinpoint.LoginActivity;
import com.fleetanalytics.pinpoint.R;
import com.fleetanalytics.pinpoint.SMSCommandFragment;
import com.fleetanalytics.pinpoint.Utils;

import java.util.ArrayList;

import admin.AccountAdminFragment;
import admin.GroupAdminFragment;
import admin.UserAdminFragment;
import left_side_slider_menu.DrawerItem;
import left_side_slider_menu.DrawerListAdapter;
import roles.RolesViewFragment;

public class SliderHomeActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<DrawerItem> navDrawerItems;
	private DrawerListAdapter adapter;

	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slider_home);
		//startSendingTrackingData();
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.theme_color)));

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<DrawerItem>();
		// ----------------------------
		SharedPreferences sp = context.getSharedPreferences("saveURL",
				Activity.MODE_PRIVATE);
		String userrole = sp.getString("user_role", "");
		// ----------------------------
		Log.v("USERROLE:", ":" + userrole);
		Log.v("navLength", ":" + navMenuTitles.length);

		// Version 1.3
//		if (userrole.equalsIgnoreCase("ROLE_ADMIN")) {
//			for (int i = 0; i < navMenuTitles.length; i++) {
//				navDrawerItems.add(new DrawerItem(navMenuTitles[i],
//						navMenuIcons.getResourceId(i, -1)));
//			}
//		} else {
//			for (int i = 0; i < 2; i++) {
//				navDrawerItems.add(new DrawerItem(navMenuTitles[i],
//						navMenuIcons.getResourceId(i, -1)));
//			}
//		}

        // Version 1.4 ---- 19 March 2018
        for (int i = 0; i < 2; i++) {
				navDrawerItems.add(new DrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1)));
        }

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new DrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
			saveSharedDrawer(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			try {
				Log.v("SlideMenuClickListener", ":" + position);
				if (position != 2) {
					SharedPreferences savedItem = getSharedPreferences(
							"saveDrawer", 0);
					if (savedItem.getInt("position", 0) != position) {
						displayView(position);
						saveSharedDrawer(position);

					} else {
						mDrawerLayout.closeDrawer(mDrawerList);
					}
				} else {
					mDrawerLayout.closeDrawer(mDrawerList);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.v("Exceptionnnn", ":" + e.getMessage());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Utils util = new Utils();
		Fragment fragment = null;
		// Bundle bundle = null;
		switch (position) {
		case 0:
			fragment = new MappingFragmentNEW2();
			break;
		case 1:
			fragment = new SMSCommandFragment();
			// util.showToast(context, "POI");
			break;
		case 2:
			util.showToast(context, "Admin Panel");
			break;
		case 3:
			fragment = new UserAdminFragment();
			// util.showToast(context, "User Admin");
			break;
		case 4:
			// util.showToast(context, "Group Admin");
			fragment = new GroupAdminFragment();
			/*
			 * //util.showToast(context, "New Device Admin"); fragment = new
			 * NewDeviceFragment();
			 */
			break;
		case 5:
			fragment = new AccountAdminFragment();
			// util.showToast(context, "Account Admin");
			break;
		case 6:
			fragment = new VehicleAdminFragment();
			// util.showToast(context, "Vehicle Admin");
			break;
		case 7:
			fragment = new RolesViewFragment();
			// util.showToast(context, "Roles");
			break;
		case 8:
			logout();
			break;
		default:
			break;
		}

		if (fragment != null) {
		    Log.v("SliderHomeActivity----","Choose Fragment Not Null");
			FragmentManager fragmentManager = getSupportFragmentManager();
			//fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else{
            Log.v("SliderHomeActivity----","Choose Fragment Null--===");
        }
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public void saveSharedDrawer(int position) {
		SharedPreferences saveUrl = getSharedPreferences("saveDrawer", 0);
		SharedPreferences.Editor e = saveUrl.edit();
		e.putInt("position", position);
		e.commit();
	}

	public void logout() {

		SharedPreferences setSP = getSharedPreferences("MyApp", 0);
		SharedPreferences.Editor e = setSP.edit();
		e.clear();
		e.commit();

		startActivity(new Intent(this, LoginActivity.class));
		finish();

	}

	/*public void startSendingTrackingData() {
		AlarmManager am = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, broadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		// After after 30 sec
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 60 * 5, pi);
	}

	*//**
	 * This method disables the Broadcast receiver registered in the
	 * AndroidManifest file.
	 * 
	 * @param view
	 *//*
	public void stopSendingTrackingData() {
		Intent intent = new Intent(this, broadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	public void enableBroadcastReceiver() {
		ComponentName receiver = new ComponentName(this,
				broadcastReceiver.class);
		PackageManager pm = this.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	*//**
	 * This method disables the Broadcast receiver registered in the
	 * AndroidManifest file.
	 * 
	 * @param view
	 *//*
	public void disableBroadcastReceiver() {
		ComponentName receiver = new ComponentName(this,
				broadcastReceiver.class);
		PackageManager pm = this.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}*/

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//stopSendingTrackingData();
	}

}
