package com.example.chipinv01;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chipinv01.Event.Credit;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import recycleViewAdapters.homePageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView homepageRecycle;
    FirebaseUser userKey = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Query queryChart = firebaseFirestore.collection("some");
    homePageAdapter homepageRecycleAdapter;
    Drawable defaultImage;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Credit credit = new Credit();
        credit.setCreditName("Сбор денег на шашлыки");
        credit.setCreditTime("Вчера в 20:48");
        credit.setDeadline("30.02.2042");
        credit.setCreditorName("Коркачев Даниил");
        credit.setMemberAmount("7");
        credit.setFullamount("15000");
        ArrayList<Credit>Credits = new ArrayList<>();



        Credits.add(credit);
        firebaseFirestore.collection("cities").document("new-city-id").set(credit);

        FirestoreRecyclerOptions<Credit> options =
                new FirestoreRecyclerOptions.Builder<Credit>()
                        .setQuery(queryChart, Credit.class)
                        .build();


        homepageRecycle = view.findViewById(R.id.chipsResycle);
        homepageRecycle.setHasFixedSize(true);
        homepageRecycle.setLayoutManager(new LinearLayoutManager(view.getContext()));

        homepageRecycleAdapter = new homePageAdapter(Credits);

        homepageRecycle.setAdapter(homepageRecycleAdapter);
        //homepageRecycleAdapter.setCredits(Credits);



        return view;

    }

    public void OnItemClickListener(View view, int position) {
    }
    public void OnItemLongClickListener(View view, int position) {

    }
}
