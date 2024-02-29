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

public class RejectedAdapter extends RecyclerView.Adapter<RejectedAdapter.ViewHolder> {
    private List<TrainersList> trainersLists;
    Context context;
    private List<TrainersList> memberListFull;

    public RejectedAdapter(Context context, List<TrainersList> trainersLists){
        this.trainersLists = trainersLists;
        this.context=context;
        memberListFull = new ArrayList<>(trainersLists);

    }

    public void filterList(List<TrainersList> filteredList) {
        trainersLists = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RejectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_pending_list_item, parent, false);
        return new RejectedAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectedAdapter.ViewHolder holder, int position) {
        TrainersList member = trainersLists.get(position);
        holder.textTname.setText(member.getTname());
        holder.textexperience.setText(member.getExperience());
        holder.textspecialization.setText(member.getSpecialization());
//        holder.textreview.setText(member.getReview());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getTimage()) // Assuming getImage() returns the URL of the image
                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.textTimage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TrainersList item = trainersLists.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, RejectTrainerProfile.class);
                    // Pass data to the intent
                    intent.putExtra("image", item.getTimage());
                    intent.putExtra("name", item.getTname());
                    intent.putExtra("specialization", item.getSpecialization());
                    intent.putExtra("experience", item.getExperience());
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
        return trainersLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTname;
        public CircleImageView textTimage;
        public TextView textspecialization;
        public TextView textexperience;
//        public TextView textreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTname = itemView.findViewById(R.id.trainer_name);
            textexperience = itemView.findViewById(R.id.trainer_experience);
            textspecialization = itemView.findViewById(R.id.trainer_specialization);
            textTimage = itemView.findViewById(R.id.trainer_image);
//            textreview = itemView.findViewById(R.id.approved_re);
        }
    }
}
