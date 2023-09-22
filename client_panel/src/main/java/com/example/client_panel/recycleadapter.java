package com.example.client_panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class recycleadapter extends RecyclerView.Adapter<recycleadapter.vieholder>{

    Context context;
    ArrayList<model> list;
    recycleadapter(Context context, ArrayList<model> list)
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

     holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,catagory_view_book.class);
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

        public vieholder(View itemview)
        {
            super(itemview);
            catagaryname = itemview.findViewById(R.id.txtCat123);
        }

    }


}
