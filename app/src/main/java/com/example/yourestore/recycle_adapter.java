package com.example.yourestore;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class recycle_adapter extends RecyclerView.Adapter<recycle_adapter.myview> {

    Context context;
    ArrayList<model_recycle> list;

    recycle_adapter(Context context,ArrayList<model_recycle> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycleview,parent,false);
        myview myview= new myview(v);
        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        holder.bname.setText(list.get(position).book_title);
        holder.aname.setText(list.get(position).book_author);
        holder.desc.setText(list.get(position).description);
        holder.sub.setText(list.get(position).subject);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        StorageReference  imgReference=storageReference.child(list.get(position).image);
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myview extends RecyclerView.ViewHolder
    {
        TextView bname,aname,desc,sub;
        ImageView img;
        public myview(@NonNull View itemView) {
            super(itemView);
            bname=itemView.findViewById(R.id.txtTitle);
            aname=itemView.findViewById(R.id.txtAuthorName);
            desc=itemView.findViewById(R.id.txtDescription);
            img=itemView.findViewById(R.id.imgView_pdf);
            sub=itemView.findViewById(R.id.txtsubject);
        }
    }
}
