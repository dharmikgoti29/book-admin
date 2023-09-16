package com.example.yourestore;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
        holder.date.setText(list.get(position).date);
        String documentid=list.get(position).getDocumentid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        StorageReference  imgReference=storageReference.child(list.get(position).image);
        String pdfname = documentid+".pdf";
        StorageReference pdfReference=storageReference.child(pdfname);
        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img);
            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.option_items, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.action_update) {

                            Intent intent = new Intent(context,update_book.class);
                            intent.putExtra("documentid",documentid);
                            context.startActivity(intent);

                            return true;
                        } else if (itemId == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Are you sure you want to delete this book?");
                            builder.setTitle("Alert !");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference docRef = db.collection("books").document(documentid);
                                    docRef.delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    pdfReference.delete();
                                                    imgReference.delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(context, "book deleted", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, "book deletion failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show();
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
                            return true;
                        } else if (itemId == R.id.action_read) {

                            pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                                    viewIntent.setDataAndType(uri, "application/pdf");
                                    viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    // Add this line if targeting Android 7.0+ to use a File Provider
                                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                    try {
                                        context.startActivity(viewIntent);
                                    } catch (ActivityNotFoundException e) {
                                        // Handle case where PDF viewer app is not available
                                        Toast.makeText(context, "No PDF viewer app available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public  void filter(ArrayList<model_recycle> filterlist)
    {
        list = filterlist;
        notifyDataSetChanged();

    }

    public class myview extends RecyclerView.ViewHolder
    {
        TextView bname,aname,desc,sub,date;
        ImageView img;
        ImageButton option;
        public myview(@NonNull View itemView) {
            super(itemView);
            bname=itemView.findViewById(R.id.txtTitle);
            aname=itemView.findViewById(R.id.txtAuthorName);
            desc=itemView.findViewById(R.id.txtDescription);
            img=itemView.findViewById(R.id.imgView_pdf);
            sub=itemView.findViewById(R.id.txtsubject);
            date=itemView.findViewById(R.id.txtdate);
            option=itemView.findViewById(R.id.option);
        }
    }
}
