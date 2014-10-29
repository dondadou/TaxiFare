package com.taxifare.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.taxifare.R;
import com.taxifare.model.pojos.NavigationDrawerItem;

import java.util.List;

public class NavigationDrawerAdapter extends BaseAdapter {
    private final Context context;
    private final List<NavigationDrawerItem> navigationDrawerItems;

    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> navigationDrawerItems) {
        this.context = context;
        this.navigationDrawerItems = navigationDrawerItems;
    }

    @Override
    public int getCount() {
        return navigationDrawerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return navigationDrawerItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_item_layout, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navigationDrawerItems.get(position).icon);
        txtTitle.setText(navigationDrawerItems.get(position).title);

        return convertView;
    }
}
