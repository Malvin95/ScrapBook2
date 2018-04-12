package com.mobile.apex.scrapbook21;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mobile.apex.scrapbook21.Fragments.HolidayDetailsFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayFragment;
import com.mobile.apex.scrapbook21.Fragments.MapsFragment;
import com.mobile.apex.scrapbook21.Fragments.ScrapbookFragment;
import com.mobile.apex.scrapbook21.model.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;

public class HolidayHomeTabbedActivity extends AppCompatActivity
        implements MapsFragment.OnMapsFragmentInteractionListener,
                    HolidayFragment.OnHolidayFragmentInteractionListener,
                    ScrapbookFragment.OnScrapbookFragmentInteractionListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
    private FloatingActionButton fab;
    private int defaultFabIcon;
    private boolean isFabVisible;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_home_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //TODO: FAB implementation.
        /**
        fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (f instanceof FABresponse) {
                    ((FABresponse) f).FABClick();
                }
            }
        });
        defaultFabIcon = android.R.drawable.ic_input_add;
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_holiday_home_tabbed, menu);
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

    //TODO: FAB interface Usage!
    /**
    @Override
    public void changeFabIcon(int id) {
        fab.setImageResource(id);
    }

    @Override
    public void defaultFabIcon() {
        fab.setImageResource(defaultFabIcon);
    }*/

    @Override
    public void onMapsFragmentInteraction(Uri uri) {

    }

    @Override
    public void showHolidayDetailsFragment(Holiday item, boolean useFAB) {

        HolidayDetailsFragment newFragment = HolidayDetailsFragment.newInstance(item, useFAB);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onScrapbookFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    MapsFragment mapsFragment = new MapsFragment();
                    return mapsFragment;
                case 1:
                    HolidayFragment holidayFragment = new HolidayFragment();
                    return holidayFragment;
                case 2:
                    ScrapbookFragment scrapbookFragment = new ScrapbookFragment();
                    return scrapbookFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Maps";
                case 1:
                    return "My Holiday";
                case 2:
                    return "Scrapbook";

                default:
                    return null;
            }
        }
    }

    //TODO: FAB Interface Implementation.
    /**
    @Override
    public void toggleFAB(){
        if (isFabVisible) {
            fab.hide();
        } else {
            fab.show();
        }
        isFabVisible = !isFabVisible;
    }*/
}
