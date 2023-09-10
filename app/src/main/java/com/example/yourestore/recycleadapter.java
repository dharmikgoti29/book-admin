package com.example.yourestore;

import android.content.Context;
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

import java.util.ArrayList;

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

        holder.catagaryname.setText(list.get(position).catagary);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
