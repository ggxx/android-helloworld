package com.plagh.ggxx.helloworld;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plagh.ggxx.helloworld.call.CallRecord;
import com.plagh.ggxx.helloworld.CallRecordFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * Created by ggxx on 2017/6/18.
 */

public class CallRecordRecyclerViewAdapter extends RecyclerView.Adapter<CallRecordRecyclerViewAdapter.ViewHolder> {

    private final List<CallRecord> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CallRecordRecyclerViewAdapter(List<CallRecord> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_callrecord, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mNumberView.setText(mValues.get(position).getNumber());
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mTypeView.setText(mValues.get(position).getCallType());
        holder.mDurationView.setText(mValues.get(position).getDurationString());

        if (mValues.get(position).getCallType() == "未接") {
            holder.mTypeView.setTextColor(holder.mView.getResources().getColor(R.color.darkred));
        } else {
            holder.mTypeView.setTextColor(holder.mView.getResources().getColor(R.color.darkblue));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mNameView;
        public final TextView mNumberView;
        public final TextView mDateView;
        public final TextView mTypeView;
        public final TextView mDurationView;
        public CallRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.callrecord_name);
            mNumberView = (TextView) view.findViewById(R.id.callrecord_number);
            mDateView = (TextView) view.findViewById(R.id.callrecord_date);
            mTypeView = (TextView) view.findViewById(R.id.callrecord_calltype);
            mDurationView = (TextView) view.findViewById(R.id.callrecord_duration);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
