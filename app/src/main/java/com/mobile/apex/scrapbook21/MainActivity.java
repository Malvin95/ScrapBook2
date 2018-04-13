package com.mobile.apex.scrapbook21;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.mobile.apex.scrapbook21.Fragments.CalendarFragment;
import com.mobile.apex.scrapbook21.Fragments.EventsFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayDetailsFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayHomeTabbedFragment;
import com.mobile.apex.scrapbook21.Fragments.HomeFragment;
import com.mobile.apex.scrapbook21.Fragments.MapsFragment;
import com.mobile.apex.scrapbook21.Fragments.PhotoFragment;
import com.mobile.apex.scrapbook21.Fragments.ScrapbookFragment;
import com.mobile.apex.scrapbook21.model.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
        HolidayFragment.OnHolidayFragmentInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        ScrapbookFragment.OnScrapbookFragmentInteractionListener,
        EventsFragment.OnEventsFragmentInteractionListener,
        HolidayDetailsFragment.OnHolidayDetailsFragmentInteractionListener,
        PhotoFragment.OnPhotoFragmentInteractionListener,
        HolidayHomeTabbedFragment.OnHolidayHomeFragmentInteractionListener,
        MapsFragment.OnMapsFragmentInteractionListener
{

    private String mCurrentPhotoPath;
    private  final int permissionRequestCode=1;
    private boolean canSaveExternal;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private Uri photoUri;

    private Fragment currentFragment;
    private FloatingActionButton fab;
    private int defaultFabIcon;
    private boolean isFabVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
        {
            canSaveExternal=false;
            //if you don't have required permissions, ask for it.
            //(Only required for API 23+)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    permissionRequestCode);
        }
        else
        {
            //we already have the permission.
            canSaveExternal=true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_nav21);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        isFabVisible = true;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (f instanceof FABresponse) {
                    ((FABresponse) f).FABClick();
                }

            }
        });
        defaultFabIcon = android.R.drawable.ic_input_add;

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

    private void addFragment(Fragment fragment, boolean addToStack) {
        // Add the fragment to the 'fragment_container' FrameLayout
        if (!addToStack) {
            // add and don't add to backstack
            // this is called from the nav drawer so clear the back stack addToStack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);
                getSupportFragmentManager().popBackStack(entry.getId(), POP_BACK_STACK_INCLUSIVE);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else {
            // replace and add to backstack
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
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
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scrap_nav21, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
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
            /**Toast.makeText(this, "I clicked the WorldView option", Toast.LENGTH_LONG).show();
            WorldViewFragment worldViewFragment = new WorldViewFragment();
            insertDefaultFragment(worldViewFragment);
            */

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_camera)
        {
            //Toast.makeText(this, "I clicked the Events option", Toast.LENGTH_LONG).show();
            PhotoFragment firstFragment = new PhotoFragment();
            insertDefaultFragment(firstFragment);
        }
        else if (id == R.id.nav_promotional)
        {
            Toast.makeText(this, "I clicked the Promo option", Toast.LENGTH_LONG).show();
            HolidayHomeTabbedFragment firstFragment = new HolidayHomeTabbedFragment();
            insertDefaultFragment(firstFragment);
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
    public void showHolidayDetailsFragment(Holiday item, boolean useFAB)
    {
        //Toast.makeText(this, "You clicked " + item.toString(), Toast.LENGTH_LONG).show();
        //Create the new fragment
        HolidayDetailsFragment newFragment = HolidayDetailsFragment.newInstance(item, useFAB);
        // add an argument specifying the item it should show
        // note that the DummyItem class must implement Serializable
        //Bundle args = new Bundle();
        //args.putSerializable("Item", item);
        //newFragment.setArguments(args);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
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

    @Override
    //TODO: Find what this would be used by and for!
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void changeFabIcon(int id) {
        fab.setImageResource(id);
    }

    @Override
    public void defaultFabIcon() {
        fab.setImageResource(defaultFabIcon);
    }

    @Override
    public void onHolidayDetailsInteraction(Uri uri) {
        Toast.makeText(this, "You clicked Events", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHolidayHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private File createImageFile() throws IOException
    {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                JPEG_FILE_SUFFIX, /*suffix*/
                storageDir /*directory*/
        );
        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onPhotoFragmentInteraction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                File f = null;

                try {
                    f = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                if (f != null) {

                    photoUri = FileProvider.getUriForFile(this,
                            "com.mobile.apex.scrapbook21.fileprovider",
                            f);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    Log.i("AJB", "file is " + f.getAbsolutePath());
                    Log.i("AJB", "extra output " + photoUri.toString());
                    startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_B);
                }
            }
    }

    /**
     * Process the result of the picture being taken by the camera.
     *
     * @param requestCode the original request code in the intent.
     * @param resultCode  the result code.
     * @param data        the additional data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("AJB", "Activity result " + requestCode + " result was " + resultCode);
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    //handlePhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B


        } // switch
    }

    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }*/

    @Override
    public void toggleFAB(){
        if (isFabVisible) {
            fab.hide();
        } else {
            fab.show();
        }
        isFabVisible = !isFabVisible;
    }
}
