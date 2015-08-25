package br.com.livroandroid.carros.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import br.com.livroandroid.carros.R;
import livroandroid.lib.fragment.NavigationDrawerFragment;

public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);


        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Snackbar.make(mContentFrame, "Item One", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 0;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_messages:
                        Snackbar.make(mContentFrame, "Item Two", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 1;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_friends:
                        Snackbar.make(mContentFrame, "Item Three", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 2;
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_discussion:
                        Snackbar.make(mContentFrame, "Item Four", Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 3;
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    public NavigationDrawerFragment.NavDrawerListView getNavDrawerView(NavigationDrawerFragment navigationDrawerFragment, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public ListAdapter getNavDrawerListAdapter(NavigationDrawerFragment navigationDrawerFragment) {
        return null;
    }

    @Override
    public void onNavDrawerItemSelected(NavigationDrawerFragment navigationDrawerFragment, int i) {

    }
}
