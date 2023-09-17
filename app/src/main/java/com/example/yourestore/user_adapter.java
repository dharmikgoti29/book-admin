package com.example.yourestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


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

        String userid=list.get(position).toString();
        holder.firstname.setText(list.get(position).firstname);
        holder.email.setText(list.get(position).email);
        holder.age.setText(list.get(position).age);
        holder.userid.setText(list.get(position).username);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public  void filter(ArrayList<model_user> filterlist)
    {
        list = filterlist;
        notifyDataSetChanged();

    }

    public class myview extends RecyclerView.ViewHolder
    {
        TextView firstname,age,email,userid;


        public myview(View itemview) {
            super(itemview);

            firstname=itemview.findViewById(R.id.user_name);
            age=itemview.findViewById(R.id.user_age);
            email=itemview.findViewById(R.id.user_useremail);
            userid=itemview.findViewById(R.id.user_id);


        }
    }
}
