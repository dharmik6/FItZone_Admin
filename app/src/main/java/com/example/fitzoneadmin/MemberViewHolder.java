package com.example.fitzoneadmin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    public TextView textName;
    public TextView textEmail;

    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.name_members);
        textEmail = itemView.findViewById(R.id.email_members);
    }
}
