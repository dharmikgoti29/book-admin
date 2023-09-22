package com.example.client_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class client_dashboard extends AppCompatActivity {

    ImageButton logout;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    BottomNavigationView btn;

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

        /*logout=findViewById(R.id.logout1);

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
*/

        btn=findViewById(R.id.bnview);

        btn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.nav_home){
                    loadFrag(new AFragment(),true);
                } else if (id==R.id.category) {
                    loadFrag(new BFragment(),false );
                } else if (id==R.id.favrit) {
                    loadFrag(new CFragment(),false);
                }else{
                    loadFrag(new DFragment(),false);
                }
                return true;
            }
        });
        btn.setSelectedItemId(R.id.nav_home);

    }
    public void loadFrag(Fragment fragment, boolean flag){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(flag){
            ft.add(R.id.container, fragment);
        }
        else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }
}