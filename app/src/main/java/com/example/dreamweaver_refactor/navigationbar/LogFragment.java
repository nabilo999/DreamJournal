package com.example.dreamweaver_refactor.navigationbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreamweaver_refactor.R;
import com.example.dreamweaver_refactor.entries_recycler.dream_entry_components;
import com.example.dreamweaver_refactor.entries_recycler.recycler_view_adapter_entries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
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
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        ArrayList<dream_entry_components> journal = new ArrayList<>();
        //journal.add(new dream_entry_components("Dream about stuff","Jun 29, 2023 • 07:30","sleepy","..."));

        recycler_view_adapter_entries adapter = new recycler_view_adapter_entries(getActivity());
        adapter.setContacts(journal);
        RecyclerView contacts_recyclerview2 = view.findViewById(R.id.journal_recycler);
        contacts_recyclerview2.setAdapter(adapter);
        contacts_recyclerview2.setLayoutManager(new LinearLayoutManager(getActivity()));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Logref =database.getReference("Dream Journal");

        Logref.addValueEventListener(new ValueEventListener() {// adds items from database to recycler
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journal.clear(); // Prevent duplicates

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dream_entry_components header = dataSnapshot.getValue(dream_entry_components.class);
                    if (header != null) {
                        journal.add(header);
                    }
                }
                adapter.notifyDataSetChanged(); // Refresh RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}