package com.example.fitzoneadmin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    public TextView textName;
    public CircleImageView textImage;
    public TextView textEmail;

    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.name_members);
        textEmail = itemView.findViewById(R.id.email_members);
        textImage = itemView.findViewById(R.id.image_members);
    }
}
