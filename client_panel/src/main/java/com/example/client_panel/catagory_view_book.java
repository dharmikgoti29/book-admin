package com.example.client_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class catagory_view_book extends AppCompatActivity {

    String subject;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText sw;
    ArrayList<model_A> recycle_list;
    a_adapter recycle_adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_view_book);

        String cat_id = getIntent().getStringExtra("catagory_id");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("subjects").child(cat_id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subject = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.recycle_view);
        recycle_list = new ArrayList<>();
        recycle_adapter = new a_adapter(this,recycle_list);
        EditText edt_search = findViewById(R.id.edt_Search);

        edt_search.addTextChangedListener(new TextWatcher() {
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

            private void filter(String toString) {
                ArrayList<model_A> list1 = new ArrayList<>();
                for(model_A item : recycle_list){
                    if(item.getBook_name().toLowerCase().contains(toString.toLowerCase()))
                    {
                        list1.add(item);
                    }

                }
                recycle_adapter.filter(list1);
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycle_adapter);
        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        recycle_list.clear();
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                String documentid = queryDocumentSnapshot.getId();
                                String imgname = documentid+".jpg";
                                String a = queryDocumentSnapshot.getString("bookname");
                                String b = queryDocumentSnapshot.getString("authorname");
                                String c = queryDocumentSnapshot.getString("discription");
                                String s= queryDocumentSnapshot.getString("subject");
                                String d= queryDocumentSnapshot.getString("upload_date");
                                model_A model_recycle = new model_A(a,b,documentid);
                                if(subject.equals(s))
                                {
                                    recycle_list.add(model_recycle);
                                }

                                recycle_adapter.notifyDataSetChanged();

                            }
                        }
                    }
                });


    }
}