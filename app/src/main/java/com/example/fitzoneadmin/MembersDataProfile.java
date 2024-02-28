package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
//import matplotlib.pyplot as plt
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersDataProfile extends AppCompatActivity {
    MaterialTextView data_name,data_username;
    CircleImageView data_image;
    AppCompatTextView data_cur_weight,data_activity,data_gender,data_height,data_weight;
    LineChart lineChart;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_data_profile);
        // Initialize your TextView elements
        data_name = findViewById(R.id.data_name);
        data_username = findViewById(R.id.data_username);
        data_activity = findViewById(R.id.data_activity);
        data_gender = findViewById(R.id.data_gender);
        data_height = findViewById(R.id.data_height);
        data_weight = findViewById(R.id.data_weight);
        data_cur_weight = findViewById(R.id.data_cur_weight);
        data_image = findViewById(R.id.data_image);
        lineChart = findViewById(R.id.lineChart);

        Intent intent = getIntent();
        String memberid = intent.getStringExtra("uid");
        String memberemailid = intent.getStringExtra("email");

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String membername = documentSnapshot.getString("name");
                String memberusername = documentSnapshot.getString("username");
                String memberactivity = documentSnapshot.getString("activity");
                String membergoal = documentSnapshot.getString("goal");
                String memberweight = documentSnapshot.getString("weight");
                String memberheight = documentSnapshot.getString("height");
                String memberimage = documentSnapshot.getString("image");

//                 Check if the userNameFromIntent matches the user
                if (memberid.equals(membername)) {
                    // Display the data only if they match
                    data_name.setText(membername != null ? membername : "No name");
                    data_username.setText(memberusername != null ? memberusername : "No username");
                    data_activity.setText(memberactivity != null ? memberactivity : "No activity");
                    data_weight.setText(memberweight != null ? memberweight : "No address");
                    data_height.setText(memberheight != null ? memberheight : "No age");
                    data_gender.setText(membergoal != null ? membergoal : "No gender");
                    data_cur_weight.setText(memberweight != null ? memberweight : "No gender");
                    if (memberimage != null) {
                        Glide.with(MembersDataProfile.this)
                                .load(memberimage)
                                .into(data_image);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        // Create a list of entries representing the data points on the chart
        // Create a list of entries representing the data points on the chart
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 300));


        // Create a dataset from the entries
        LineDataSet dataSet = new LineDataSet(entries, "Label for the dataset");
        dataSet.setColor(Color.BLUE); // Set the color of the line
        dataSet.setValueTextColor(Color.RED); // Set the color of the values
        dataSet.setLineWidth(2f); // Set the width of the line

        // Create a LineData object with the dataset
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // Customize the appearance of the chart
        lineChart.getDescription().setEnabled(false); // Disable description
        lineChart.setTouchEnabled(true); // Enable touch gestures
        lineChart.setDragEnabled(true); // Enable drag and drop gestures
        lineChart.setScaleEnabled(true); // Enable scaling gestures
        lineChart.setPinchZoom(true); // Enable pinch zoom
        lineChart.setDrawGridBackground(false); // Disable grid background

        // Customize the X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position at the bottom
        xAxis.setGranularity(1f); // Interval between each X axis value

        // Customize the Y axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setGranularity(5f); // Interval between each Y axis value
// Set padding for the Y-axis
        float padding = 5f; // Adjust this value as needed
        float minYValue = yAxis.getAxisMinimum() - padding;
        float maxYValue = yAxis.getAxisMaximum() + padding;
        yAxis.setAxisMinimum(minYValue);
        yAxis.setAxisMaximum(maxYValue);

// Format the values on the Y axis
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value); // Format as integer
            }
        });

        // Disable the right Y axis
        lineChart.getAxisRight().setEnabled(false);

        // Invalidate the chart to refresh
        lineChart.invalidate();


    }
}