package com.example.client_panel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Uri input_image;
    ImageView img;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference=storage.getReference();
    StorageReference imgReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String cuid;
    String name,age;

    public DFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DFragment newInstance(String param1, String param2) {
        DFragment fragment = new DFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_d, container, false);

           int pic = 100;
          @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button logout=  v.findViewById(R.id.logout);
          Button update = v.findViewById(R.id.update);
          @SuppressLint({"MissingInflatedId","LocalSuppress"}) Button updimg = v.findViewById(R.id.updimg);
          img=v.findViewById(R.id.img);
         TextInputLayout tt1,tt2,tt3;
          EditText t1,t2,t3;
          tt1 = v.findViewById(R.id.full_name);
          tt2 = v.findViewById(R.id.age);
          tt3 = v.findViewById(R.id.email);
          t1 = tt1.getEditText();
          t2 = tt2.getEditText();
          t3 = tt3.getEditText();
        user = auth.getCurrentUser();
        cuid = user.getUid().toString();
        imgReference = storageReference.child(cuid+".jpg");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_detail").document(cuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists())
                    {
                        name = documentSnapshot.getString("name");
                        age = documentSnapshot.getString("age");
                        String email = documentSnapshot.getString("email");
                        t1.setText(name);
                        t2.setText(age);
                        t3.setText(email);
                        t3.setFocusable(false);
                    }
                }
            }
        });
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(img);
            }
        });

        updimg.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(Intent.ACTION_PICK);
                  intent.setType("image/*");
                  startActivityForResult(intent,100);


              }
          });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cname , cage;
                cname = t1.getText().toString();
                cage = t2.getText().toString();
                if(!cname.equals(name)||!cage.equals(age))
                {
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",cname);
                    map.put("age",cage);
                    db.collection("user_detail").document(cuid).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    auth.signOut();
                    Intent intent = new Intent(getContext(),log_in_page.class);
                    startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception appropriately, e.g., show a Toast with an error message
                }
            }
        });

        return  v;
    }

    private void updimage(Uri input_image) {
        if(input_image!=null)
        {
            UploadTask uploadTask = imgReference.putFile(input_image);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "image updated successfully...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "image update failed try again...", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {

            if(data != null)
            {
                input_image=data.getData();
                img.setImageURI(input_image);
                img.setBackgroundResource(0);
                updimage(input_image);
            }
        }
    }
}