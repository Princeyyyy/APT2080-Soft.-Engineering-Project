package com.prince.project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setUserName(String userName) {
        TextView userTextView = mView.findViewById(R.id.user_name);
        userTextView.setText(userName);
    }

    public void setTime(String time) {
        TextView userTextView = mView.findViewById(R.id.time);
        userTextView.setText(time);
    }

}
