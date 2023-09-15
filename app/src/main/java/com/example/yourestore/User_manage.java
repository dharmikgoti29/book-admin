package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class User_manage extends AppCompatActivity {

    RecyclerView rw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        rw=findViewById(R.id.recycleview_user);
        ArrayList<model_user> arrayList = new ArrayList<>();
        user_adapter userAdapter = new user_adapter(this,arrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        rw.setLayoutManager(gridLayoutManager);
        rw.setAdapter(userAdapter);
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                arrayList.clear();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                    {
                        String a = queryDocumentSnapshot.getString("Age");
                        String b = queryDocumentSnapshot.getString("Email");
                        String c = queryDocumentSnapshot.getString("firstname");
                        String d = queryDocumentSnapshot.getString("lastname");
                        String e = queryDocumentSnapshot.getString("username");
                        String f = queryDocumentSnapshot.getId();
                        model_user modelUser = new model_user(a,b,c,d,e,f);
                        arrayList.add(modelUser);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}