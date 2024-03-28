package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationDetail extends AppCompatActivity {

    ImageView backpress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        TextView name = findViewById(R.id.noti_member_name);
        TextView msg = findViewById(R.id.massage);
        backpress = findViewById(R.id.back);


        Intent intent = getIntent();
        String str_name = intent.getStringExtra("name");
        String str_msg = intent.getStringExtra("msg");

        name.setText(str_name);
        msg.setText(str_msg);

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}