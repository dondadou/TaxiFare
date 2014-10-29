package com.taxifare.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.taxifare.R;
import com.taxifare.model.pojos.NavigationDrawerItem;
import com.taxifare.ui.adapter.NavigationDrawerAdapter;
import com.taxifare.ui.fragment.HomeFragment;

import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity implements HomeFragment.OnCalculateButtonListener {
    public static final int DEF_VALUE = -1;
    public static final String ORIGIN_EXTRA = "origin";
    public static final String DESTINATION_EXTRA = "destination";
    public static final String POLYLINE_EXTRA = "polyline";
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private String[] menuList;
    private TypedArray menuListIcon;
    ListView menuListView;
    CharSequence title;
    private ArrayList<NavigationDrawerItem> navigationDrawerItems;
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        retrieveMenuList();
        populateMenuList();

        title = getTitle();
        drawerTitle = "Menu";
        menuListView.setOnItemClickListener(new DrawerItemOnClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, navigationDrawer,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        navigationDrawer.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayFragment(0);
        }
    }

    private void initViews() {
        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuListView = (ListView) findViewById(R.id.menu_list);
        menuList = getResources().getStringArray(R.array.navigation_menu);
        menuListIcon = getResources().obtainTypedArray(R.array.navigation_menu_icon);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(this.title);
    }

    @Override
    public void onResultFound(String origin, String destination, String polyLine) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ORIGIN_EXTRA, origin);
        intent.putExtra(DESTINATION_EXTRA, destination);
        intent.putExtra(POLYLINE_EXTRA, polyLine);
        startActivity(intent);
    }

    private class DrawerItemOnClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            displayFragment(position);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayFragment(int position) {
        Fragment fragment = new HomeFragment();

        switch (position){
            case 0:
                fragment = HomeFragment.newInstance();
                break;
//            case 1:
//                fragment = FavoritesFragment.newInstance();
//                break;
//            case 2:
//                fragment = CountryFragment.newInstance();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        menuListView.setItemChecked(position, true);
        menuListView.setSelection(position);
        setTitle(menuList[position]);
        navigationDrawer.closeDrawer(menuListView);

    }

    private void populateMenuList() {
        menuListView.setAdapter(new NavigationDrawerAdapter(this, navigationDrawerItems));
    }

    private void retrieveMenuList() {
        navigationDrawerItems = new ArrayList<NavigationDrawerItem>();
        for (int index = 0; index < menuList.length; index++) {
            navigationDrawerItems.add(new NavigationDrawerItem(menuListIcon.getResourceId(index, DEF_VALUE), menuList[index]));
        }

        menuListIcon.recycle();
    }
}
