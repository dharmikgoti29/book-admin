package com.example.yourestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
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

public class AddBooks extends AppCompatActivity {

    //activity back
    ImageButton btn_back;
    Button upload_pdf,insert_book;
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
    String intentcat_id;
    Uri input_image,input_pdf;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference=storage.getReference();
    StorageReference pdfReference,imgReference;
    String default_sub;
    int c ;
    boolean isSettingSelectionProgrammatically;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        btn_back = findViewById(R.id.btnBackfromadd);
        img=findViewById(R.id.image);
        upload_pdf=findViewById(R.id.upload_pdf);
        insert_book=findViewById(R.id.insert_book);
        catagary=findViewById(R.id.catagary);
        textInputLayout_title=findViewById(R.id.book_name);
        textInputLayout_author=findViewById(R.id.author_name);
        textInputLayout_desc=findViewById(R.id.description);
        book_name=textInputLayout_title.getEditText();
        author_name=textInputLayout_author.getEditText();
        description=textInputLayout_desc.getEditText();


        btn_back.setOnClickListener(view -> onBackPressed());



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
                if(upload_pdf.getText().equals("Upload PDF"))//insert pdf
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, pdf);
                } else if (upload_pdf.getText().equals("view pdf")) {//view pdf
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(input_pdf, "application/pdf");
                    viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(viewIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(AddBooks.this, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        catagary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!isSettingSelectionProgrammatically) {
                    cata = adapterView.getItemAtPosition(i).toString();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                if(snapshot1.getValue().toString().equals(cata))
                                {
                                    intentcat_id = snapshot1.getKey().toString();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra("sub_id")) {
            String sub_id = intent.getStringExtra("sub_id");
            c = 10;
            myRef.child(sub_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        default_sub = snapshot.getValue(String.class);

                        int index = getIndexForValue(default_sub);
                        if (index != -1) {

                            isSettingSelectionProgrammatically = true;
                            catagary.setSelection(index);

                            isSettingSelectionProgrammatically = false;
                        }
                    }
                }

                private int getIndexForValue(String defaultSub) {
                    for (int i = 0; i < catagary.getCount(); i++) {
                        if (catagary.getItemAtPosition(i).toString().equals(defaultSub)) {
                            return i;
                        }
                    }
                    return -1;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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
                    Toast.makeText(AddBooks.this, "Add value first", Toast.LENGTH_SHORT).show();
                }
                else {

                    Map<String,Object> book_detail = new HashMap<>();
                    book_detail.put("bookname",bookname);
                    book_detail.put("authorname",authorname);
                    book_detail.put("subject",cata);
                    book_detail.put("discription",desc);
                    book_detail.put("upload_date",dateString);
                    db.collection("books").add(book_detail).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String documentid = documentReference.getId();
                            String pdfname = documentid+".pdf";
                            String imgname = documentid+".jpg";
                            pdfReference=storageReference.child(pdfname);
                            UploadTask uploadTask = pdfReference.putFile(input_pdf);
                            imgReference=storageReference.child(imgname);
                            UploadTask uploadTask1 = imgReference.putFile(input_image);
                            Toast.makeText(AddBooks.this, "book uploaded", Toast.LENGTH_SHORT).show();
                            if(c == 10)
                            {
                                Intent intent1 = new Intent(AddBooks.this,Catagary_books.class);
                                intent1.putExtra("catagory_id",intentcat_id);
                                startActivity(intent1);
                                finish();
                            }
                            else {
                                onBackPressed();
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
                upload_pdf.setText("view pdf");
            }

        }
    }
}