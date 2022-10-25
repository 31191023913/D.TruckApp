package com.example.dtruckapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dtruckapp.Adapter.OrderAdapter;
import com.example.dtruckapp.Adapter.OrderReceiveAdapter;
import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.OrderReceive;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CartFragment extends Fragment {
    private RecyclerView orderGotrecyclerView;
    private OrderReceiveAdapter orderRA;
    private String Current_user_id="";
    private FirebaseAuth userAuth;
    private DatabaseReference OrderTaken;
    private FirebaseDatabase fdatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View CarryView = inflater.inflate(R.layout.fragment_cart, container, false);

        userAuth = FirebaseAuth.getInstance();
        Current_user_id = userAuth.getCurrentUser().getUid();

        fdatabase = FirebaseDatabase.getInstance();
        OrderTaken = fdatabase.getReference().child("OrderReceiveList").child(Current_user_id);


        orderGotrecyclerView = CarryView.findViewById(R.id.recview_cartorder);
        LinearLayoutManager linearLayoutOrder = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderGotrecyclerView.setLayoutManager(linearLayoutOrder);
        FirebaseRecyclerOptions<OrderReceive> options =
                new FirebaseRecyclerOptions.Builder<OrderReceive>()
                        .setQuery(OrderTaken, OrderReceive.class)
                        .build();
        orderRA= new OrderReceiveAdapter(options);
        orderGotrecyclerView.setAdapter(orderRA);


        return CarryView;
    }



    @Override
    public void onStart() {
        super.onStart();
        orderRA.notifyDataSetChanged();
        if (orderRA != null) {
            orderRA.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        orderRA.notifyDataSetChanged();
        if (orderRA != null) {
            orderRA.stopListening();
        }
    }




}