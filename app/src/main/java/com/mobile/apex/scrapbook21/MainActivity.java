package com.mobile.apex.scrapbook21;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import com.mobile.apex.scrapbook21.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
        HolidayFragment.OnListFragmentInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        ScrapbookFragment.OnScrapbookFragmentInteractionListener,
        EventsFragment.OnEventsFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_nav21);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //TODO: change the Holiday fragment from being the first page, change this to Home Fragment.
        //Create a new Fragment to be placed in the activity layout
        HomeFragment firstFragment = new HomeFragment();
        insertDefaultFragment(firstFragment);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void insertDefaultFragment(Fragment firstFragment)
    {
        //In case this activity was started with special instructions from an
        //intent, pass the intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        //Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, firstFragment).commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scrap_nav21, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            // Handle the home action.
            HomeFragment firstFragment = new HomeFragment();
                insertDefaultFragment(firstFragment);
        }
        else if (id == R.id.nav_myHoliday)
        {
            // Handle the MyHoliday action.
            HolidayFragment firstFragment = new HolidayFragment();
                insertDefaultFragment(firstFragment);
        }
        else if (id == R.id.nav_calender)
        {
            // Handle the MyHoliday action.
            Toast.makeText(this, "I clicked the Calendar option", Toast.LENGTH_LONG).show();
            CalendarFragment firstFragment = new CalendarFragment();
                insertDefaultFragment(firstFragment);
        }
        else if (id == R.id.nav_scrapbook)
        {
            //Toast.makeText(this, "I clicked the Scrapbook option", Toast.LENGTH_LONG).show();
            ScrapbookFragment firstFragment = new ScrapbookFragment();
                insertDefaultFragment(firstFragment);
        }
        else if (id == R.id.nav_worldView)
        {
            Toast.makeText(this, "I clicked the WorldView option", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_events)
        {
            Toast.makeText(this, "I clicked the Events option", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_promotional)
        {
            Toast.makeText(this, "I clicked the Promo option", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_faqs)
        {
            Toast.makeText(this, "I clicked the FAQS option", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_settings)
        {
            Toast.makeText(this, "I clicked the Settings option", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item){
        Toast.makeText(this, "You clicked " + item.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        Toast.makeText(this, "You clicked Home", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCalendarFragmentInteraction(Uri uri) {
        Toast.makeText(this,"You clicked Calendar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onScrapbookFragmentInteraction(Uri uri) {
        Toast.makeText(this, "You clicked Scrapbook", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEventsFragmentInteraction(Uri uri) {
        Toast.makeText(this, "You clicked Events", Toast.LENGTH_LONG).show();
    }
}
