package left_side_slider_menu;


import java.util.ArrayList;

import com.fleetanalytics.pinpoint.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<DrawerItem> navDrawerItems;
	
	public DrawerListAdapter(Context context, ArrayList<DrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.menu_linearlayout);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
       // TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
         
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        layout.setBackgroundResource(R.color.list_background);
        if(position == 2 || position == 8){
        	layout.setBackgroundResource(R.color.theme_color);
        	txtTitle.setTextColor(Color.WHITE);
        }else{
        	layout.setBackgroundResource(R.color.list_background);
        }
      
        return convertView;
	}

}
