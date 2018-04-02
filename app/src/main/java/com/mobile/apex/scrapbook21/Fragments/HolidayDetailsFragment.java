package com.mobile.apex.scrapbook21.Fragments;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHolidayDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayDetailsFragment extends Fragment
        implements FABresponse {

    private static final String TAG = "HolidayDetailsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Item";
    private static final String ARG_PARAM2 = "useFAB";

    // TODO: Rename and change types of parameters
    private Holiday holiday;
    private boolean useFAB;

    private OnHolidayDetailsFragmentInteractionListener mListener;

    private AutoCompleteTextView mSearchText;

    private EditText titleField;
    private EditText notesField;
    private Button Save;
    private Button startDate;
    private Button endDate;
    public HolidayData HolidayD;

    //MAPSACTIVITY variables
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_holiday_details, container, false);
        View view = inflater.inflate(R.layout.fragment_editable_holiday_details, container, false);


        titleField = (EditText) view.findViewById(R.id.titleView);
        titleField.addTextChangedListener(titleWatcher);
        notesField = (EditText) view.findViewById(R.id.notesView);
        notesField.addTextChangedListener(titleWatcher);
        startDate = (Button) view.findViewById(R.id.startButton);
        startDate.setText(holiday.formatDate(true));
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,true);
            }
        });
        endDate = (Button) view.findViewById(R.id.endButton);
        endDate.setText(holiday.formatDate(false));
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,false);
            }
        });

        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.hdf_input_search);
        mSearchText.setAdapter(mPlacesAutoCompleteAdapter);

        return view;
    }

    private final TextWatcher titleWatcher = new TextWatcher() {

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
