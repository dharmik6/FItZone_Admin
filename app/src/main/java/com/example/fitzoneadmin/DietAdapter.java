package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {
    private List<DietList> dietLists;
    Context context;
    private List<DietList> filteredDietLists; // List to hold filtered diet items

    public DietAdapter(Context context, List<DietList> dietLists){
        this.dietLists = dietLists;
        this.context=context;
        this.filteredDietLists = new ArrayList<>(dietLists); // Initialize filtered list

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_list_item, parent, false);
        return new ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DietList member = dietLists.get(position);
        holder.dietname.setText(member.getName());
//        holder.textexperience.setText(member.getDescription());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getImageUrl()) // Assuming getImage() returns the URL of the image
                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.dietimage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DietList item = dietLists.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, Diet.class);
                    // Pass data to the intent
                    intent.putExtra("imageUrl", item.getImageUrl());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("Description", item.getDescription());

                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dietLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dietname;
        public ImageView dietimage;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dietname = itemView.findViewById(R.id.diet_item_name);
            dietimage = itemView.findViewById(R.id.diet_item_image);
        }
    }
    // Filter method to filter the diet lists based on search query
    public void filter(String text) {
        filteredDietLists.clear();

        if (text.isEmpty()) {
            filteredDietLists.addAll(dietLists);
        } else {
            String query = text.toLowerCase().trim();
            for (DietList diet : dietLists) {
                if (diet.getName().toLowerCase().contains(query)) {
                    filteredDietLists.add(diet);
                }
            }
        }

        notifyDataSetChanged(); // Update the adapter with filtered list
    }
    // Add this method in your DietAdapter class
    public void filterList(List<DietList> filteredList) {
        dietLists = filteredList;
        notifyDataSetChanged();
    }

}


