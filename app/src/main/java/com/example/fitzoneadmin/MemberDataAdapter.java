package com.example.fitzoneadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDataAdapter extends RecyclerView.Adapter<MemberDataAdapter.ViewHolder> {
    private List<MemberList> memberList;
    Context context;
    private List<MemberList> memberListFull;

    public MemberDataAdapter(Context context, List<MemberList> memberList){
        this.memberList = memberList;
        this.context=context;
        memberListFull = new ArrayList<>(memberList);

    }

    public void filterList(List<MemberList> filteredList) {
        memberList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_data_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberList member = memberList.get(position);
        holder.textDataName.setText(member.getName());
        holder.textDataEmail.setText(member.getEmail());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getImage()) // Assuming getImage() returns the URL of the image
                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.textDataImage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MemberList item = memberList.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, MembersDataProfile.class);
                    // Pass data to the intent
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("uid", item.getId());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("username", item.getUsername());
                    intent.putExtra("email", item.getEmail());
//                    intent.putExtra("number", item.getNumber());
//                    intent.putExtra("gender", item.getGender());
//                    intent.putExtra("age", item.getAge());
//                    intent.putExtra("address", item.getAddress());
//                    intent.putExtra("activity", item.getActicity()); // corrected typo
//                    intent.putExtra("joidate", item.getJoidate()); // corrected typo

                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDataName;
        public CircleImageView textDataImage;
        public TextView textDataEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDataName = itemView.findViewById(R.id.name_data_member);
            textDataEmail = itemView.findViewById(R.id.email_data_member);
            textDataImage = itemView.findViewById(R.id.image_data_member);
        }
    }
}


