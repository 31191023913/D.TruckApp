package com.example.dtruckapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dtruckapp.Model.TheNews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailNewsFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



    String tenTT, ngayCN, detailTT, imgurl;

    public DetailNewsFragment(){

    }

    public DetailNewsFragment(String tentt, String ngayCN, String detailTT, String imgurl){
        this.tenTT = tentt;
        this.ngayCN = ngayCN;
        this.detailTT = detailTT;
        this.imgurl = imgurl;
    }


    public static DetailNewsFragment newInstance(String param1, String param2) {
        DetailNewsFragment fragment = new DetailNewsFragment();
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
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_news, container, false);

        ImageView imageViewholder = view.findViewById(R.id.imgNewssD);
        TextView titleNe = view.findViewById(R.id.titleNewD);
        TextView datenews = view.findViewById(R.id.dateofNewD);
        TextView contentnews = view.findViewById(R.id.descriptionNewsD);

        titleNe.setText(tenTT);
        datenews.setText(ngayCN);
        contentnews.setText(detailTT);
        Glide.with(getContext()).load(imgurl).into(imageViewholder);


        return view;
    }

    public void onBackPressed(){
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerFL,new HomeFragment()).addToBackStack(null).commit();
    }


}