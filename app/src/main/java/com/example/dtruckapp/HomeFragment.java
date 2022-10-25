package com.example.dtruckapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dtruckapp.Adapter.NewsAdapter;
import com.example.dtruckapp.Common.Common;
import com.example.dtruckapp.Model.TheNews;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageSlider imageSlider;
    NewsAdapter newsAdapter;

  /*  ArrayList<TheNews> newsArrayList;*/
    TextView NameUsers;
    FirebaseAuth mAuth;

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
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
        View Hview = inflater.inflate(R.layout.fragment_home, container, false);

        NameUsers = Hview.findViewById(R.id.nameUser);


        imageSlider = Hview.findViewById(R.id.image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.slide1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slide2,ScaleTypes.FIT)); //Ko full để scateType là null
        imageList.add(new SlideModel(R.drawable.slide3,ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slide4,ScaleTypes.FIT));
        imageSlider.setImageList(imageList);

        NameUsers.setText(Common.currentUser.getFullName());

        recyclerView = Hview.findViewById(R.id.recview_new);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<TheNews> options =
                new FirebaseRecyclerOptions.Builder<TheNews>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("News"),TheNews.class)
                        .build();
        newsAdapter= new NewsAdapter(options);
        recyclerView.setAdapter(newsAdapter);

        return  Hview;


    }

    @Override
    public void onStart() {
        super.onStart();
        if (newsAdapter != null) {
            newsAdapter.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (newsAdapter != null) {
            newsAdapter.stopListening();
        }
    }



}