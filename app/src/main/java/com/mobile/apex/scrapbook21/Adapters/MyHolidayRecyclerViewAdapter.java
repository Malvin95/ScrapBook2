package com.mobile.apex.scrapbook21.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.apex.scrapbook21.Fragments.HolidayFragment;
import com.mobile.apex.scrapbook21.Fragments.HolidayFragment.OnHolidayFragmentInteractionListener;
//import com.mobile.apex.scrapbook21.dummy.DummyContent.DummyItem;
import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.Holiday;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Holiday} and makes a call to the
 * specified {@link HolidayFragment.OnHolidayFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHolidayRecyclerViewAdapter extends RecyclerView.Adapter<MyHolidayRecyclerViewAdapter.ViewHolder> {

    private final List<Holiday> mValues;
    private final HolidayFragment.OnHolidayFragmentInteractionListener mListener;

    public MyHolidayRecyclerViewAdapter(List<Holiday> items, OnHolidayFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_holiday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getNotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Holiday item = holder.mItem;
                    mListener.showHolidayDetailsFragment(holder.mItem, false);
                    //mListener.showHolidaySetFragment(holder.mItem, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Holiday mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
