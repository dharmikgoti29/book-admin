package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class view_books extends AppCompatActivity {

    Button addcat;
    EditText sw;
    RecyclerView recyclerView;
    ArrayList<model> recycle_list;
    recycleadapter recycleadapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("subjects");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        addcat=findViewById(R.id.btn_add_categories);
        recyclerView=findViewById(R.id.categoriesList);
        sw=findViewById(R.id.edt_Search);

        sw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view_books.this, AddCategory.class);
                startActivity(intent);
            }
        });


        recycle_list = new ArrayList<>();

        recycleadapter = new recycleadapter(this,recycle_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleadapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                recycle_list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    String cat_name,cat_id;
                    cat_id= snapshot1.getKey().toString();
                    cat_name=snapshot1.getValue().toString();
                    model model = new model(cat_name,cat_id);
                    recycle_list.add(model);
                }
                recycleadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void filter(String editable) {
        ArrayList<model> list = new ArrayList<>();
        for(model item : recycle_list){
            if(item.getCatagary().toLowerCase().contains(editable.toLowerCase()))
            {
                list.add(item);
            }

        }
        recycleadapter.filter((ArrayList<model>) list);
    }
}