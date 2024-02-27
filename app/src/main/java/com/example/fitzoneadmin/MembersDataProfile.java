package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

public class MembersDataProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_data_profile);

        ImageView backPress = findViewById(R.id.back_press);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // BMI chat java code code

        HalfGauge halfGauge = findViewById(R.id.halfGauge);

        double value = 0; // Set your value here

        Range range1= new Range();
        range1.setColor(Color.parseColor("#2739C9"));
        range1.setFrom(15);
        range1.setTo(16);
        halfGauge.addRange(range1);


        Range range2= new Range();
        range2.setColor(Color.parseColor("#3977F0"));
        range2.setFrom(16.0);
        range2.setTo(18.5);
        halfGauge.addRange(range2);

        Range range3= new Range();
        range3.setColor(Color.parseColor("#5CC2D8"));
        range3.setFrom(18.5);
        range3.setTo(25);
        halfGauge.addRange(range3);

        Range range4= new Range();
        range4.setColor(Color.parseColor("#F7CC4A"));
        range4.setFrom(25);
        range4.setTo(30.0);
        halfGauge.addRange(range4);

        Range range5= new Range();
        range5.setColor(Color.parseColor("#F29837"));
        range5.setFrom(30.0);
        range5.setTo(35.0);
        halfGauge.addRange(range5);

        Range range6= new Range();
        range6.setColor(Color.parseColor("#D8313B"));
        range6.setFrom(35.0);
        range6.setTo(40.0);
        halfGauge.addRange(range6);

        halfGauge.setMinValue(15);
        halfGauge.setMaxValue(40.0);
        halfGauge.setValue(value);


    }
}