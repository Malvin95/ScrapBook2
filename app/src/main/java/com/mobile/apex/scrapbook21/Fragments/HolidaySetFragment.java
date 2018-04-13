package com.mobile.apex.scrapbook21.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;
import com.mobile.apex.scrapbook21.model.HolidayData;

public class HolidaySetFragment extends Fragment implements FABresponse
{
    private static final String TAG = "HolidaySetFragment";

    private static final String ARG_PARAM1 = "Item";
    private static final String ARG_PARAM2 = "useFAB";

    private Holiday holiday;
    private boolean useFAB;

    private ImageView headerImage;
    private TextView title;
    private TextView notes;
    private TextView Location;
    private TextView startDate;
    private TextView endDate;
    public HolidayData HolidayD;

    private OnHolidaySetFragmentInteractionListener mListener;

    public HolidaySetFragment(){
    }

    public static HolidaySetFragment newInstance(Holiday holiday, boolean useFAB)
    {
        HolidaySetFragment fragment = new HolidaySetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, holiday);
        args.putBoolean(ARG_PARAM2, useFAB);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            holiday = (Holiday)getArguments().getSerializable(ARG_PARAM1);
            useFAB = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_holiday_details, container,false);
        headerImage = (ImageView) view.findViewById(R.id.header_image);
        title = (TextView) view.findViewById(R.id.titleView);
        Location = (TextView) view.findViewById(R.id.location);
        notes = (TextView) view.findViewById(R.id.notesView);
        startDate = (TextView) view.findViewById(R.id.startButton);
        endDate = (TextView) view.findViewById(R.id.endButton);

        init();

        return view;
    }

    private void init()
    {
        Log.d(TAG, "HolidaySetFragment initialising");

        //headerImage.setImageBitmap(photoRef);
        title.setText(holiday.getTitle());
        Location.setText(holiday.getLocation());
        notes.setText(holiday.getNotes());
        startDate.setText((CharSequence) holiday.getStartDate());
        endDate.setText((CharSequence) holiday.getEndDate());

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnHolidaySetFragmentInteractionListener)
        {
            mListener = (OnHolidaySetFragmentInteractionListener) context;
        }
        else
            {
                throw new RuntimeException(context.toString()
                        + "Must implement OnHolidaySetFragmentInteractionListener");
            }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener.defaultFabIcon();
        mListener = null;
    }

    @Override
    public void  onResume()
    {
        super.onResume();
        mListener.defaultFabIcon();
        mListener.changeFabIcon(android.R.drawable.ic_menu_edit);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mListener.defaultFabIcon();
        mListener.changeFabIcon(android.R.drawable.ic_menu_edit);
    }

    @Override
    public void FABClick() {
        //TODO: Get the needed holiday details fragment that is paired with the holidaySetFragment
        mListener.showHolidayDetailsFragment(new Holiday("","",""), true);
    }

    public interface OnHolidaySetFragmentInteractionListener
    {
        void showHolidayDetailsFragment(Holiday item, boolean useFAB);
        void changeFabIcon(int id);
        void defaultFabIcon();
        void toggleFAB();
        void onHolidaySetInteraction(Uri uri);
    }
}
