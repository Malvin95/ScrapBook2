package com.mobile.apex.scrapbook21.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.mobile.apex.scrapbook21.Adapters.PlacesAutoCompleteAdapter;
import com.mobile.apex.scrapbook21.FABInterface;
import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.FABresponse;
import com.mobile.apex.scrapbook21.model.Holiday;
import com.mobile.apex.scrapbook21.model.HolidayData;
import com.mobile.apex.scrapbook21.model.PlaceInfo;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHolidayHomeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayHomeTabbedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayHomeTabbedFragment extends Fragment
        implements FABresponse
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    private OnHolidayHomeFragmentInteractionListener mListener;

    //-----------------------------------MAP Widgets-----------------------------\\
    private AutoCompleteTextView mSearchText;
    private GoogleApiClient mGoogleApiClient;
    //private GeoDataApi mGeoDataAPI;

    //----------------------------MAPSACTIVITY variables-----------------------------\\
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private PlaceInfo mPlaceInfo;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Boolean mLocationPermissionsGranted = false;

    //---------------------------mapView Variables ----------------------------------\\
    private MapView mMapView;
    private Holiday holiday;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;


    public HolidayHomeTabbedFragment() {
        // Required empty public constructor
    }


    public static HolidayHomeTabbedFragment newInstance(String param1, String param2) {
        HolidayHomeTabbedFragment fragment = new HolidayHomeTabbedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_holiday_home_tabbed, container, false);
        // Inflate the layout for this fragment
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHolidayHomeFragmentInteraction(uri);
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new MapsFragment(), "Around Me");
        adapter.addFragment(new HolidayFragment(), "My Holidays");
        adapter.addFragment(new ScrapbookFragment(), "Scrapbook");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void FABClick() {
        //Toast.makeText(getContext(), "This the HolidayFragment FAB interaction!", Toast.LENGTH_LONG).show();
        mListener.showHolidayDetailsFragment(new Holiday("", "", ""), true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHolidayHomeFragmentInteractionListener) {
            mListener = (OnHolidayHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHolidayHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }
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
    public interface OnHolidayHomeFragmentInteractionListener {
        // TODO: Update argument type and name
        void showHolidayDetailsFragment(Holiday item, boolean useFAB);
        void onHolidayHomeFragmentInteraction(Uri uri);
    }
}