package com.example.fitzoneadmin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDataViewHolder extends RecyclerView.ViewHolder {
    public TextView textDataName;
    public CircleImageView textDataImage;
    public TextView textDataEmail;

    public MemberDataViewHolder(@NonNull View itemView) {
        super(itemView);
        textDataName = itemView.findViewById(R.id.name_data_member);
        textDataEmail = itemView.findViewById(R.id.email_data_member);
        textDataImage = itemView.findViewById(R.id.image_data_member);
    }
}
