package com.mobile.apex.scrapbook21.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mobile.apex.scrapbook21.Adapters.PlacesAutoCompleteAdapter;
import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;
import com.mobile.apex.scrapbook21.model.HolidayData;
import com.mobile.apex.scrapbook21.model.PlaceInfo;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;

public class HolidayDetailsFragment extends Fragment
        implements FABresponse,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "HolidayDetailsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Item";
    private static final String ARG_PARAM2 = "useFAB";
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-50, -198), new LatLng(56, 154));

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // TODO: Rename and change types of parameters
    private Holiday holiday;
    private boolean useFAB;

    private OnHolidayDetailsFragmentInteractionListener mListener;

    //-----------------------------------MAP Widgets-----------------------------\\
    private AutoCompleteTextView mSearchText;
    private GoogleApiClient mGoogleApiClient;
    //private GeoDataApi mGeoDataAPI;

    //------------------------------NORMAL VARIABLES --------------------------------\\

    private EditText titleField;
    private EditText notesField;
    private ImageView headerImage;
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

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editable_holiday_details, container, false);

        headerImage = (ImageView) view.findViewById(R.id.header_image);
        titleField = (EditText) view.findViewById(R.id.titleView);
        notesField = (EditText) view.findViewById(R.id.notesView);
        startDate = (Button) view.findViewById(R.id.startButton);
        endDate = (Button) view.findViewById(R.id.endButton);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.hdf_input_search);


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

    private void init()
    {
        Log.d(TAG, "Holiday Fragment Initialising");

        //Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity(),null);

        //Construct a PlaceDetection Client .
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        //headerImage.setImageBitmap();

        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holiday.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleField.setText(holiday.getTitle());

        notesField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holiday.setNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        notesField.setText(holiday.getNotes());

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
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    //execute our method for searching
                    hideSoftKeyboard();

                    //TODO: Check the ImageView SetPic() method at the bottom of the class!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //Find Out how to set a bitmap....
                    //InputStream is = getResources().openRawResource(mPlaceInfo.getPhotoRef());

                    //headerImage.setImageBitmap(mPlaceInfo.getPhotoRef());

                    int targetW = headerImage.getWidth();
                    int targetH = headerImage.getHeight();

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    int scaleFactor = 1;
                    if ((targetW > 0) || (targetH > 0)) {
                        scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                    }
                    Log.i("AJB", "Scale factor is " + scaleFactor);

                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    Bitmap photoRef = BitmapFactory.decodeFile(mPlaceInfo.getPhotoRef(), bmOptions);

                    headerImage.setImageBitmap(photoRef);
                }

                return false;
            }
        });
        mSearchText.setText(holiday.getLocation());
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holiday.setLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void hideSoftKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

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
    public void onStop()
    {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
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

                Log.d(TAG, "onResult: place: " + mPlaceInfo.toString());

            }
            catch(NullPointerException e)
            {
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            places.release();
        }
    };

    private void getPhotos()
    {

        final String placeId = mPlaceInfo.getId();
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>()
        {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task)
            {
                //Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();

                //Get the PlacePhotoMetadataBuffer (metadata for all of the photos)
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);

                //Get the attribution text.
                CharSequence attribution = photoMetadata.getAttributions();

                //Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();
                        try {
                            mPlaceInfo.setPhotoRef(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

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
