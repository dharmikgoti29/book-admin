package com.example.yourestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminDashBoard extends AppCompatActivity {

    CardView cardAddCategory,cardAddBooks,viewbooks,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        cardAddCategory = findViewById(R.id.add_category_card);
        cardAddBooks = findViewById(R.id.add_book_card);
        viewbooks = findViewById(R.id.card_book);
        user=findViewById(R.id.home_card);

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