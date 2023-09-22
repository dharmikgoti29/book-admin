package com.example.client_panel;

import android.content.Context;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
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
