package com.example.chipinv01;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chipinv01.Event.Cardview_item_decor;
import com.example.chipinv01.Event.ContactsAdapter;
import com.example.chipinv01.Event.Event;
import com.example.chipinv01.Event.NewEvent;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;

import recycleViewAdapters.homePageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView homepageRecycle;
    FirebaseUser firebaseUserID = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Query queryChart = firebaseFirestore.collection(firebaseUserID.getUid());
    homePageAdapter homepageRecycleAdapter;
    Drawable defaultImage;
    Event ExtendedEvent;




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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initHomePageAdapter(view);
        homepageRecycleAdapter.setOnItemListenerListener(new homePageAdapter.OnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                homepageRecycleAdapter.getItem(position);
                initNewExtendedAdapter(position);
                if (ExtendedEvent != null){
                    Intent ExtendedEventIntent = new Intent(getActivity(), NewEvent.class);
                    ExtendedEventIntent.putExtra("ExtendedEvent", String.valueOf(ExtendedEvent));
                    startActivity(ExtendedEventIntent);

                }
            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

            }
        });
        homepageRecycle.addItemDecoration(new Cardview_item_decor.SpacesItemDecoration(20));
        return view;

    }
    public void initHomePageAdapter(View view){
        FirestoreRecyclerOptions<Event> options =
                new FirestoreRecyclerOptions.Builder<Event>()
                        .setQuery(queryChart, Event.class)
                        .build();
        homepageRecycle = view.findViewById(R.id.chipsResycle);
        homepageRecycle.setHasFixedSize(true);
        homepageRecycle.setLayoutManager(new LinearLayoutManager(view.getContext()));

        homepageRecycleAdapter = new homePageAdapter(options);
        homepageRecycleAdapter.setOnItemListenerListener(new homePageAdapter.OnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent NewEventIntent = new Intent(view.getContext(), Homepage.class);
                Event event = homepageRecycleAdapter.getItem(position);
                startActivity(NewEventIntent);

            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

            }
        });
        homepageRecycle.setAdapter(homepageRecycleAdapter);

    }
    @Override
    public void onStop(){
        super.onStop();
        homepageRecycleAdapter.stopListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        homepageRecycleAdapter.startListening();
    }


    public void initNewExtendedAdapter(int position){
        DocumentReference documentReference = firebaseFirestore.collection(firebaseUserID.getUid()).
                document(
                        String.valueOf(homepageRecycleAdapter.getItem(position).getUniqueId())
        );
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ExtendedEvent = documentSnapshot.toObject(Event.class);

            }
        });
    }
}
