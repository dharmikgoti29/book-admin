package com.example.yourestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class user_adapter extends RecyclerView.Adapter<user_adapter.myview> {
    Context context;
    ArrayList<model_user> list;

    user_adapter(Context context, ArrayList<model_user> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_example_layout,parent,false);
        myview myview = new myview(v);
        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {

        holder.firstname.setText(list.get(position).firstname);
        holder.lastname.setText(list.get(position).lastname);
        holder.email.setText(list.get(position).email);
        holder.age.setText(list.get(position).age);
        holder.username.setText(list.get(position).username);
        holder.userid.setText(list.get(position).username);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myview extends RecyclerView.ViewHolder
    {
        TextView firstname,lastname,age,email,username,userid;

        public myview(View itemview) {
            super(itemview);

            firstname=itemview.findViewById(R.id.user_name);
            lastname=itemview.findViewById(R.id.user_lastname);
            age=itemview.findViewById(R.id.user_age);
            email=itemview.findViewById(R.id.user_useremail);
            username=itemview.findViewById(R.id.user_username);
            userid=itemview.findViewById(R.id.user_id);

        }
    }
}
