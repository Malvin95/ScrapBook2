package com.mobile.apex.scrapbook21.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mobile.apex.scrapbook21.Adapters.ImageAdapter;
import com.mobile.apex.scrapbook21.FABInterface;
import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.FABresponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnScrapbookFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScrapbookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScrapbookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridview;
    private ImageAdapter imageAdapter;

    private OnScrapbookFragmentInteractionListener mListener;

    public ScrapbookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScrapbookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScrapbookFragment newInstance(String param1, String param2) {
        ScrapbookFragment fragment = new ScrapbookFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scrapbook, container, false);

        /* The grid view where the pictures will be displayed */
        gridview = (GridView) view.findViewById(R.id.gridview);
        /* The image adapter that contains a list of filenames to be displayed in the grid */
        imageAdapter = new ImageAdapter(getActivity());
        /* now set the adapter for the grid view */
        gridview.setAdapter(imageAdapter);

        /* Set an onclick listener for the grid view for when we click on an image */
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), imageAdapter.getImageFileName(position),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * Add the image files to the image adapter.
     */
    private void readImageFiles() {
        for (String f : getPictureFiles()) {
            imageAdapter.addImage(f);
        }
    }

    private List<String> getPictureFiles()
    {
        ArrayList<String> imageFileNames = new ArrayList<>();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] listOfFiles = storageDir.listFiles();
        Log.i("AJB", "storage dir when getting picture files is " + storageDir.getAbsolutePath());

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                Log.i("AJB", "Image File " + listOfFiles[i].getName());
                imageFileNames.add(listOfFiles[i].getAbsolutePath());
            }
            else if (listOfFiles[i].isDirectory())
            {
                Log.i("AJB", "Directory " + listOfFiles[i].getName());
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return imageFileNames;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onScrapbookFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScrapbookFragmentInteractionListener) {
            mListener = (OnScrapbookFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScrapbookFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        imageAdapter.clear();
        /* Read the file names from the app's Picture folder */
        readImageFiles();
        /* notify the data changed */
        imageAdapter.notifyDataSetChanged();
        mListener.toggleFAB();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        mListener.toggleFAB();
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
    public interface OnScrapbookFragmentInteractionListener extends FABInterface {
        // TODO: Update argument type and name
        void onScrapbookFragmentInteraction(Uri uri);
    }
}
