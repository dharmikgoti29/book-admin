package com.example.client_panel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class read_book extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();


    ImageView img;
    Button btn;
    TextView name,author,desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        String cat_id= getIntent().getStringExtra("cat_id");

        img=findViewById(R.id.imgview1);
        btn=findViewById(R.id.btnRead);
        name=findViewById(R.id.txtName);
        author=findViewById(R.id.txtAuthor);
        desc=findViewById(R.id.txtDescription);

        String imgname = cat_id+".jpg";
        String pdfname = cat_id+".pdf";
        StorageReference storageReference=storage.getReference();
        StorageReference imgReference=storageReference.child(imgname);
        StorageReference pdfReference=storageReference.child(pdfname);
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(read_book.this)
                        .load(uri)
                        .into(img);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                        viewIntent.setDataAndType(uri, "application/pdf");
                        viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add this line if targeting Android 7.0+ to use a File Provider
                        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            startActivity(viewIntent);
                        } catch (ActivityNotFoundException e) {
                            // Handle case where PDF viewer app is not available
                            Toast.makeText(read_book.this, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        db.collection("books").document(cat_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name.setText( documentSnapshot.getString("bookname").toString());
                author.setText( documentSnapshot.getString("authorname").toString());
                desc.setText( documentSnapshot.getString("discription").toString());

            }
        });

    }
}