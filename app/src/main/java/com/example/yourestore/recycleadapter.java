package com.example.yourestore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class recycleadapter extends RecyclerView.Adapter<recycleadapter.vieholder>{

    Context context;
    ArrayList<model> list;
    recycleadapter (Context context, ArrayList<model> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_card,parent,false);
        vieholder vieholder = new vieholder(v);
        return vieholder;
    }

    @Override
    public void onBindViewHolder(@NonNull vieholder holder, int position) {

        model current_item = list.get(position);
        holder.catagaryname.setText(current_item.getCatagary());
        String cat_id = current_item.cat_id;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("subjects").child(cat_id);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        myRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "catagory deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Catagary_books.class);
                intent.putExtra("catagory_id",cat_id);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  void filter(ArrayList<model> filterlist)
    {
        list = filterlist;
        notifyDataSetChanged();

    }

    public  class  vieholder extends RecyclerView.ViewHolder{

        TextView catagaryname;
        ImageButton btn_delete;

        public vieholder(View itemview)
        {
            super(itemview);
            catagaryname = itemview.findViewById(R.id.txtCat123);
            btn_delete = itemview.findViewById(R.id.ImgBtnDelete);
        }

    }


}
