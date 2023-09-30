package com.example.client_panel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class a_adapter extends RecyclerView.Adapter<a_adapter.myview> {

    Context context;
    ArrayList<model_A> list;

    a_adapter(Context context, ArrayList<model_A> list)
    {
       this.context=context;
       this.list=list;
    }


    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.client_exp,parent,false);
        myview vieholder = new myview(v);
        return vieholder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        holder.t1.setText(list.get(position).book_name);
        holder.t2.setText(list.get(position).author_name);

        //image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String catid=list.get(position).cat_id;
        String imagname = catid+".jpg";
        StorageReference imgReference=storageReference.child(imagname);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img);
            }
        });

        //faurite button

        db.collection("user_detail").document(uid).collection("faurite").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot queryDocumentSnapshot  : task.getResult())
                    {
                        String fcat_id = queryDocumentSnapshot.getString("cat_id");
                        if(fcat_id.equals(catid))
                        {
                            holder.t3.setText("Remove");
                            holder.imgbtn.setImageResource(R.drawable.baseline_favorite_red);
                            break;
                        }
                    }
                }

            }
        });

        holder.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               if(holder.t3.getText().toString().equals("Add to wishlist"))
               {
                   Map<String , Object> map = new HashMap<>();
                   map.put("cat_id",catid);
                   db.collection("user_detail").document(uid).collection("faurite").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
                           Toast.makeText(context, "book added into your collection", Toast.LENGTH_SHORT).show();
                           holder.t3.setText("Remove");
                           holder.imgbtn.setImageResource(R.drawable.baseline_favorite_red);
                       }
                   });
               }
               else
               {
                   db.collection("user_detail").document(uid).collection("faurite").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                {
                                    String fcat_id = queryDocumentSnapshot.getString("cat_id");
                                    if(fcat_id.equals(catid))
                                    {
                                        db.collection("user_detail").document(uid).collection("faurite").document(queryDocumentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(context, "removed from wishlist", Toast.LENGTH_SHORT).show();
                                                holder.t3.setText("Add to wishlist");
                                                holder.imgbtn.setImageResource(R.drawable.faurite_white);
                                            }
                                        })   ;
                                    }
                                }
                            }
                       }
                   });
               }
            }
        });

        //inside

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, read_book.class);
                intent.putExtra("cat_id",catid);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  void filter(ArrayList<model_A> filterlist)
    {
        list = filterlist;
        notifyDataSetChanged();

    }

    public class myview extends RecyclerView.ViewHolder{

        TextView t1,t2,t3;
        ImageView img;
        ImageButton imgbtn;
        LinearLayout l1;

        public myview(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.demo_bookname);
            t2=itemView.findViewById(R.id.demo_authorname);
            t3=itemView.findViewById(R.id.t1);
            img=itemView.findViewById(R.id.demo_image);
            imgbtn=itemView.findViewById(R.id.imgbtn1);
            l1=itemView.findViewById(R.id.l1);
        }
    }
}
