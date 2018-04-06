package com.mobile.apex.scrapbook21.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mobile.apex.scrapbook21.Adapters.CustomInfoWindowAdapter;
import com.mobile.apex.scrapbook21.Adapters.PlacesAutoCompleteAdapter;
import com.mobile.apex.scrapbook21.MapsActivity;
import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;
import com.mobile.apex.scrapbook21.model.HolidayData;
import com.mobile.apex.scrapbook21.model.PlaceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class HolidayDetailsFragment extends Fragment
        implements FABresponse, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback
{

    private static final String TAG = "HolidayDetailsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Item";
    private static final String ARG_PARAM2 = "useFAB";
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-50, -198), new LatLng(56, 154));

    // TODO: Rename and change types of parameters
    private Holiday holiday;
    private boolean useFAB;

    private OnHolidayDetailsFragmentInteractionListener mListener;

    //-----------------------------------MAP Widgets-----------------------------\\
    private AutoCompleteTextView mSearchText;
    private GoogleApiClient mGoogleApiClient;

    private EditText titleField;
    private EditText notesField;
    private Button Save;
    private Button startDate;
    private Button endDate;
    public HolidayData HolidayD;

    //----------------------------MAPSACTIVITY variables-----------------------------\\
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private PlaceInfo mPlaceInfo;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Boolean mLocationPermissionsGranted = false;

    //---------------------------mapView Variables ----------------------------------\\
    private MapView mMapView;

    public HolidayDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param holiday Parameter 1.
     * @return A new instance of fragment HolidayDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HolidayDetailsFragment newInstance(Holiday holiday, boolean useFAB) {
        HolidayDetailsFragment fragment = new HolidayDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, holiday);
        args.putBoolean(ARG_PARAM2, useFAB);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            holiday = (Holiday)getArguments().getSerializable(ARG_PARAM1);
            useFAB = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editable_holiday_details, container, false);

        titleField = (EditText) view.findViewById(R.id.titleView);
        notesField = (EditText) view.findViewById(R.id.notesView);
        startDate = (Button) view.findViewById(R.id.startButton);
        endDate = (Button) view.findViewById(R.id.endButton);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.hdf_input_search);
        mMapView  = view.findViewById(R.id.mapView);

        init();

        return view;
    }

    /**
    public void OnViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMapView  = view.findViewById(R.id.mapView);
        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }
     */

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo)
    {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        mGoogleMap.clear();

        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        if(placeInfo != null)
        {

            try
            {
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mGoogleMap.addMarker(options);

            }
            catch(NullPointerException e)
            {
                Log.e(TAG,"moveCamera: NullPointerException: " + e.getMessage() );
            }

            hideSoftKeyboard();
        }
        else
        {
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mGoogleMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void init()
    {
        Log.d(TAG, "Holiday Fragment Initialising");

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        titleField.addTextChangedListener(titleWatcher);
        notesField.addTextChangedListener(titleWatcher);

        startDate.setText(holiday.formatDate(true));
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,true);
            }
        });

        endDate.setText(holiday.formatDate(false));
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,false);
            }
        });

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), mGoogleApiClient,LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlacesAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                /**|| keyEvent.getAction() == KeyEvent.ACTION_DOWN*/)
                {
                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    private void geoLocate()
    {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e)
        {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0)
        {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, , Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlacesAutoCompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private void getDeviceLocation()
    {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        //TODO: check the getActivity() call at the end of the line below, it could be switched out for a getContext instead!
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void hideSoftKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private final TextWatcher titleWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                //Once the text is changed call the FAB and make it a save button instead
            } else {
            }


            String holidayTitle = titleField.getText().toString();
            holiday.setTitle(holidayTitle);

            String notes = notesField.getText().toString();
            holiday.setNotes(notes);
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHolidayDetailsFragmentInteractionListener) {
            mListener = (OnHolidayDetailsFragmentInteractionListener) context;
            //mListener.changeFabIcon(android.R.drawable.ic_menu_save);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHolidayDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.defaultFabIcon();
        mListener = null;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mListener.defaultFabIcon();
        mListener.changeFabIcon(android.R.drawable.ic_menu_save);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mListener.defaultFabIcon();
        mListener.changeFabIcon(android.R.drawable.ic_menu_save);
    }

    @Override
    public void FABClick()
    {
        //Log.i("TAG", "FABClick Works to save HolidayDetails.Fragment");
        /**
         * This if statement checks if the list holidays contains the holiday object to be saved.
         * If it does, then the holiday is saved over.
         * Else, the holiday is added to the list.
         */
        if (HolidayData.getInstance().getAllHolidays().contains(holiday))
        {
            HolidayData.getInstance().setHoliday(holiday);
        }
        else
        {
            HolidayData.getInstance().getAllHolidays().add(holiday);
        }

        Toast.makeText(getContext(), "Saved", LENGTH_LONG).show();
    }

    public void showDatePickerDialog(View v, boolean isStartDate)
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setHoliday(holiday);

        if(isStartDate)
        {
            Log.i("Info", "Setting the Start Date Button");
            newFragment.setDateButton(startDate);
        }
        else
        {
            Log.i("info", "setting End Date Button");
            newFragment.setDateButton(endDate);
        }

        newFragment.setStart(isStartDate);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess())
            {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);

            try
            {
                mPlaceInfo = new PlaceInfo();
                mPlaceInfo.setName(place.getName().toString());
                mPlaceInfo.setAddress(place.getAddress().toString());
                //mPlaceInfo.setAttributions(place.getAttributions().toString());
                mPlaceInfo.setPhoneNumber(place.getPhoneNumber().toString());
                mPlaceInfo.setWebsiteUri(place.getWebsiteUri());
                mPlaceInfo.setId(place.getId());
                mPlaceInfo.setLatlng(place.getLatLng());
                mPlaceInfo.setRating(place.getRating());

                Log.d(TAG, "onReuslt: place: " + mPlaceInfo.toString());

            }
            catch(NullPointerException e)
            {
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlaceInfo);

            places.release();
        }
    };
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHolidayDetailsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void changeFabIcon(int id);
        void defaultFabIcon();
        void toggleFAB();
        void onHolidayDetailsInteraction(Uri uri);
    }
}
