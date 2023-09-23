package com.example.client_panel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CFragment newInstance(String param1, String param2) {
        CFragment fragment = new CFragment();
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
        View root = inflater.inflate(R.layout.fragment_c, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText edt_search = root.findViewById(R.id.edt_Search1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView rw1 =root.findViewById(R.id.rw2);
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rw1.setLayoutManager(gridLayoutManager);
        rw1.setAdapter(adapter);
        db.collection("user_detail")
                .document(uid)
                .collection("faurite")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();

                            for (QueryDocumentSnapshot queryDocumentSnapshot1 : task.getResult()) {
                                String cat_id = queryDocumentSnapshot1.getString("cat_id");

                               db.collection("books")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot bookDocument : task.getResult()) {
                                                        String id = bookDocument.getId();
                                                        String bookName = bookDocument.getString("bookname");
                                                        String authorName = bookDocument.getString("authorname");
                                                        model_A modelA = new model_A(bookName, authorName, id);
                                                        if(id.equals(cat_id))
                                                        {
                                                            list.add(modelA);
                                                        }

                                                    }

                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });


        return  root;
    }
}