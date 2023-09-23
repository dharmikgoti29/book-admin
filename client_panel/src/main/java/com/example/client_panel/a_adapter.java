package com.example.client_panel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        holder.t1.setText(list.get(position).book_name);
        holder.t2.setText(list.get(position).author_name);

        //image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        String catid=list.get(position).cat_id;
        String imagname = catid+".jpg";
        StorageReference imgReference=storageReference.child(imagname);
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img);
            }
        });

        //faurite button

        holder.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();
                Map<String , Object> map = new HashMap<>();
                map.put("cat_id",catid);
                db.collection("user_detail").document(uid).collection("faurite").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "book added into your collection", Toast.LENGTH_SHORT).show();
                    }
                });
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

        TextView t1,t2;
        ImageView img;
        LinearLayout l1;
        public myview(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.demo_bookname);
            t2=itemView.findViewById(R.id.demo_authorname);
            img=itemView.findViewById(R.id.demo_image);
            l1=itemView.findViewById(R.id.l1);
        }
    }
}
