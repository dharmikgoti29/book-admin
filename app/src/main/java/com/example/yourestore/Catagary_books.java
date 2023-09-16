package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class Catagary_books extends AppCompatActivity {

    String subject;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText sw;
    ArrayList<model_recycle> recycle_list;
    recycle_adapter recycle_adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagary_books);

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

        String cat_id = getIntent().getStringExtra("catagory_id");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("subjects").child(cat_id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subject = snapshot.getValue().toString();
                Toast.makeText(Catagary_books.this, subject, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.recycle_view);
        recycle_list = new ArrayList<>();
        recycle_adapter = new recycle_adapter(this,recycle_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
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
                                model_recycle model_recycle = new model_recycle(a,b,c,imgname,s,d,documentid);
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

    private void filter(String toString) {
        ArrayList<model_recycle> list = new ArrayList<>();
        for(model_recycle item : recycle_list){
            if(item.getBook_title().toLowerCase().contains(toString.toLowerCase()))
            {
                list.add(item);
            }

        }
        recycle_adapter.filter(list);
    }
}