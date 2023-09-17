package com.example.yourstore_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class client_dashboard extends AppCompatActivity {


    ImageButton logout;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser muser =  FirebaseAuth.getInstance().getCurrentUser();

        if(muser != null)
        {

        }
        else {
            Intent intent = new Intent(client_dashboard.this, log_in_page.class);
            startActivity(intent);
            finish();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        logout=findViewById(R.id.logout1);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.signOut();
                    Intent intent = new Intent(client_dashboard.this,log_in_page.class);
                    startActivity(intent);
                    finishAffinity();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception appropriately, e.g., show a Toast with an error message
                }
            }
        });

    }
}