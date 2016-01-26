package com.silva.benjamin.guessthehashtag.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silva.benjamin.guessthehashtag.R;
import com.silva.benjamin.guessthehashtag.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by benjamin on 12/10/15.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private ArrayList<User> mDataset;
    private Context mContext;
    private String mType;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView mUsername;
        protected TextView mScore;
        protected TextView mRanking;
        protected ImageView mProfilePic;


        public ViewHolder(View v) {
            super(v);
            mUsername = (TextView) v.findViewById(R.id.username);
            mScore = (TextView) v.findViewById(R.id.score);
            mRanking = (TextView) v.findViewById(R.id.ranking);
            mProfilePic = (ImageView) v.findViewById(R.id.profile_pic);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(ArrayList<User> mDataset, Context mContext, String mType) {
        this.mDataset = mDataset;
        this.mContext = mContext;
        this.mType = mType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User u = mDataset.get(position);
        holder.mUsername.setText(u.getUsername());
        if (mType.equals("WEEKLY"))
            holder.mScore.setText("" + u.getWeek_score() + " " + mContext.getString(R.string.points));
        else
            holder.mScore.setText("" + u.getScore() + " " + mContext.getString(R.string.points));
        holder.mRanking.setText("" + (position + 1));
        Picasso.with(mContext).load(u.getProfile_picture()).into(holder.mProfilePic);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public User getItemAt(int position) {
        return mDataset.get(position);
    }

}
