package com.example.yourestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminDashBoard extends AppCompatActivity {

    CardView cardAddCategory,cardAddBooks,viewbooks,user;
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
            Intent intent = new Intent(AdminDashBoard.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        cardAddCategory = findViewById(R.id.add_category_card);
        cardAddBooks = findViewById(R.id.add_book_card);
        viewbooks = findViewById(R.id.card_book);
        user=findViewById(R.id.home_card);
        logout=findViewById(R.id.logout1);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.signOut();
                    Intent intent = new Intent(AdminDashBoard.this,LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception appropriately, e.g., show a Toast with an error message
                }
            }
        });

        //open add category activity
        cardAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashBoard.this, AddCategory.class));
            }
        });

        //open add books activity
        cardAddBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashBoard.this, AddBooks.class));
            }
        });

        viewbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(AdminDashBoard.this, view_books.class);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(AdminDashBoard.this, User_manage.class);
                startActivity(intent);
            }
        });

    }
}