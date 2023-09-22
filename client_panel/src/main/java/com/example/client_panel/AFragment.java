package com.example.client_panel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public AFragment() {
        // Required empty public constructor



    }


    public static AFragment newInstance(String param1, String param2) {
        AFragment fragment = new AFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_a, container, false);

        EditText edt_search = root.findViewById(R.id.edt_Search);
        RecyclerView rw1 =root.findViewById(R.id.rw1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<model_A> list = new ArrayList<>();
        a_adapter adapter = new a_adapter(getContext(),list);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

            private void filter(String toString) {
                ArrayList<model_A> list1 = new ArrayList<>();
                for(model_A item : list){
                    if(item.getBook_name().toLowerCase().contains(toString.toLowerCase()))
                    {
                        list1.add(item);
                    }

                }
                adapter.filter(list1);
            }
        });



        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rw1.setLayoutManager(gridLayoutManager);
        rw1.setAdapter(adapter);
        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                String id=queryDocumentSnapshot.getId();
                                String a = queryDocumentSnapshot.getString("bookname");
                                String b = queryDocumentSnapshot.getString("authorname");
                                model_A modelA = new model_A(a,b,id);
                                list.add(modelA);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                });

        return root;
    }

}