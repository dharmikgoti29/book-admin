package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class update_book extends AppCompatActivity {

    //activity back
    ImageButton btn_back;
    Button upload_pdf,insert_book,insert_pdf;
    ImageView img;
    Spinner catagary;
    TextInputLayout textInputLayout_title,textInputLayout_author,textInputLayout_desc;
    EditText book_name,author_name,description;
    String bookname,authorname,desc,cata;

    //for spiner
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("subjects");

    Integer pic=100;
    Integer pdf=200;

    Uri input_image,input_pdf;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference=storage.getReference();
    StorageReference pdfReference,imgReference;
    String subject_selected;
    String intentcat_id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        btn_back = findViewById(R.id.btnBackfromadd);
        img=findViewById(R.id.image);
        upload_pdf=findViewById(R.id.upload_pdf);
        insert_book=findViewById(R.id.insert_book);
        insert_pdf=findViewById(R.id.insert_pdf);
        catagary=findViewById(R.id.catagary);
        textInputLayout_title=findViewById(R.id.book_name);
        textInputLayout_author=findViewById(R.id.author_name);
        textInputLayout_desc=findViewById(R.id.description);
        book_name=textInputLayout_title.getEditText();
        author_name=textInputLayout_author.getEditText();
        description=textInputLayout_desc.getEditText();





        String documentid = getIntent().getStringExtra("documentid");
        db.collection("books").document(documentid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    book_name.setText(documentSnapshot.getString("bookname"));
                    author_name.setText(documentSnapshot.getString("authorname"));
                    description.setText(documentSnapshot.getString("discription"));
                    subject_selected = documentSnapshot.getString("subject").toString();
                }
            }
        });
        FirebaseStorage first_storage = FirebaseStorage.getInstance();
        StorageReference first_storageReference=first_storage.getReference();
        StorageReference  first_imgReference=first_storageReference.child(documentid+".jpg");
        StorageReference  first_pdfReference=first_storageReference.child(documentid+".pdf");
        first_imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                input_image=uri;
                Glide.with(update_book.this)
                        .load(input_image)
                        .into(img);
            }
        });
        first_pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                input_pdf=uri;
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayList<String> spiner_list = new ArrayList<>();
        ArrayAdapter<String> spiner_adapter = new ArrayAdapter<>(this,R.layout.custom_spiner,spiner_list);
        catagary.setAdapter(spiner_adapter);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                spiner_list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    spiner_list.add(snapshot1.getValue().toString());
                }
                spiner_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,pic);
            }
        });
        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(input_pdf, "application/pdf");
                    viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add this line if targeting Android 7.0+ to use a File Provider
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(viewIntent);
                    } catch (ActivityNotFoundException e) {
                        // Handle case where PDF viewer app is not available
                        Toast.makeText(update_book.this, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                    }
            }

        });
        insert_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, pdf);
            }
        });
        catagary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cata=adapterView.getItemAtPosition(i).toString();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            String tempstr = dataSnapshot.getValue().toString();
                            if(tempstr.equals(cata))
                            {
                                intentcat_id = dataSnapshot.getKey().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        insert_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookname=book_name.getText().toString();
                authorname=author_name.getText().toString();
                desc=description.getText().toString();
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String dateString = dateFormat.format(currentDate);

                if(bookname.isEmpty() || authorname.isEmpty() || desc.isEmpty() || cata.isEmpty() || input_pdf==null || input_image==null)
                {
                    Toast.makeText(update_book.this, "Add value first", Toast.LENGTH_SHORT).show();
                }
                else {


                    Map<String, Object> book_detail = new HashMap<>();
                    book_detail.put("bookname", bookname);
                    book_detail.put("authorname", authorname);
                    book_detail.put("authorname", authorname);
                    book_detail.put("subject", cata);
                    book_detail.put("discription", desc);
                    book_detail.put("upload_date", dateString);
                    insert_book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            bookname = book_name.getText().toString();
                            authorname = author_name.getText().toString();
                            desc = description.getText().toString();
                            Date currentDate = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String dateString = dateFormat.format(currentDate);

                            if (bookname.isEmpty() || authorname.isEmpty() || desc.isEmpty() || cata.isEmpty() || input_pdf == null || input_image == null) {
                                Toast.makeText(update_book.this, "Add value first", Toast.LENGTH_SHORT).show();
                            } else {

                                Map<String, Object> book_detail = new HashMap<>();
                                book_detail.put("bookname", bookname);
                                book_detail.put("authorname", authorname);
                                book_detail.put("authorname", authorname);
                                book_detail.put("subject", cata);
                                book_detail.put("discription", desc);
                                book_detail.put("upload_date", dateString);
                                db.collection("books").document(documentid).update(book_detail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        String pdfname = documentid+".pdf";
                                        String imgname = documentid+".jpg";
                                        pdfReference=storageReference.child(pdfname);
                                        UploadTask uploadTask = pdfReference.putFile(input_pdf);
                                        imgReference=storageReference.child(imgname);
                                        UploadTask uploadTask1 = imgReference.putFile(input_image);
                                        Toast.makeText(update_book.this, "Success fully updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(update_book.this,Catagary_books.class);
                                        intent.putExtra("catagory_id",intentcat_id);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    });
                }


            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == pic)
        {

            if(data != null)
            {
                input_image=data.getData();
                img.setImageURI(input_image);
                img.setBackgroundResource(0);
            }
        }
        else if (requestCode == pdf)
        {

            if(data != null)
            {
                input_pdf=data.getData();
            }

        }
    }
}