package com.mobile.apex.scrapbook21;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.mobile.apex.scrapbook21.Fragments.CalendarFragment;
import com.mobile.apex.scrapbook21.Fragments.EventsFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayDetailsFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayFragment;
import com.mobile.apex.scrapbook21.Fragments.HomeFragment;
import com.mobile.apex.scrapbook21.Fragments.ScrapbookFragment;
import com.mobile.apex.scrapbook21.model.Holiday;
import com.mobile.apex.scrapbook21.model.HolidayData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
        HolidayFragment.OnListFragmentInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        ScrapbookFragment.OnScrapbookFragmentInteractionListener,
        EventsFragment.OnEventsFragmentInteractionListener,
        HolidayDetailsFragment.OnHolidayDetailsFragmentInteractionListener
{

    private  final int permissionRequestCode=1;
    private boolean canSaveExternal;
    private static final int ACTION_TAKE_PHOTO_B = 1;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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

        Button picBtn = (Button)findViewById(R.id.btnIntend);
        setBtnListenerOrDisable(picBtn, mTakePicOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
    }

    @Override//android recommended class to handle permissions
    public void onRequestPermissionResult(int requestCode, String permissions[], int [] grantResults)
    {
        switch (requestCode)
        {
            case permissionRequestCode:
            {
                //If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //we have been granted permission
                    canSaveExternal = true;
                }
                else
                {
                    //permission denied, disable the functionality that depends on this permission.
                    Toast.makeText(PhotoIntentActivity.this,
                        "Permission denied to read your External Storage",
                        Toast.LENGTH_SHORT).show();
                    canSaveExternal = false;
                }
                return;
            }
            //other 'case' line to check for other permissions this app might request.
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

    private Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

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
    public void onListFragmentInteraction(Holiday item)
    {
        //Toast.makeText(this, "You clicked " + item.toString(), Toast.LENGTH_LONG).show();
        //Create the new fragment
        HolidayDetailsFragment newFragment = new HolidayDetailsFragment();
        // add an argument specifying the item it should show
        // note that the DummyItem class must implement Serializable
        Bundle args = new Bundle();
        args.putSerializable("Item", item);
        newFragment.setArguments(args);

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
    public void onHolidayDetailsInteraction(Uri uri) {
        Toast.makeText(this, "You clicked Events", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void dispatchTakePictureIntent(int actionCode)
    {
        intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_PHOTO_B:
                //ensure that there's a camera activity to handle the intent
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
                                "com.example.android.fileprovider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_B);
                    }
                }
                //if
                break;
            default:
                break;
        }
    }

    private File createImageFile() throws IOException
    {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File storageDir = getExternalFilesFir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                JPEG_FILE_SUFFIX, /*suffix*/
                storageDir /*directory*/
        );
        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void handlePhoto()
    {
        if(mCurrentPhotoPath != null)
        {
            Log.i("AJB","photo located at"+mCurrentPhotoPath);
            imageAdapter.addImage(mcurrentPhotoPath);
            imageAdapter.notifyDataSetChanged();
            grabImage();
            mCurrentPhotoPath = null;
        }
        else
        {
            Log.i("AJB", "photo but no path");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case ACTION_TAKE_PHOTO_B:
            {
                if(resultCode == RESULT_OK)
                {
                    handlePhoto();
                }
                break;
            }//ACTION_TAKE_PHOTO_B
        }//switch
    }

    private void grabImage()
    {
        if(canSaveExternal)
        {
            this.getContentResolver().nofifyChange(photoUri, null);
            ContentResolver cr = this.getContentResolver();
            Bitmap bitmap;

            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, photoUri);
                File album = getAlbumDir();
                String fName = photoUri.getLastPathSegment();
                File file = new File(album, fName);
                Log.i("AJB", "Saving big image into " + file.getAbsolutePath());

                if (file.exists()) {
                    file.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.d("AJB", "Failed to save to " + file.getAbsoluteFile());
                    e.printStackTrace();
                }
            }catch (Exception e){
                Log.d("AJB", "Failed to Load", e);
                }
        }
        else
        {
            Log.i("AJB", "Not allowed to save to users Pictures folder");
        }
    }
}
