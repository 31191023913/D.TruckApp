package com.example.dtruckapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dtruckapp.DetailOrderFragment;
import com.example.dtruckapp.EditProfileFragment;
import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.User;
import com.example.dtruckapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrderAdapter extends FirebaseRecyclerAdapter<Order,OrderAdapter.MyOrderviewHolder> {


    public OrderAdapter(@NonNull FirebaseRecyclerOptions<Order> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyOrderviewHolder holder, int position, @NonNull Order model) {
        holder.NamePublisher.setText(model.getOrderNameUser());
        holder.getDatePost.setText(model.getDataTimePost());
        holder.NumberContact.setText(model.getOrderNumberPhone());
        holder.numberCr.setText(model.getOrderCarRequired());
        holder.txtContenP.setText(model.getOrderDesc());
        holder.selectservicetruck.setText(model.getOrderCategory());
        Glide.with(holder.ImgPost.getContext())
                .load(model.getOrderUserImg())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(holder.ImgPost);
        holder.OrderItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrderIDs = model.getOrderuid();
                DetailOrderFragment orderFragments = new DetailOrderFragment(OrderIDs);
                Bundle bundle = new Bundle();
                bundle.putString("Orderids",OrderIDs);
                orderFragments.setArguments(bundle);
                AppCompatActivity goDteil = (AppCompatActivity)view.getContext();
                FragmentTransaction fragmentTransaction = goDteil.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerFL,orderFragments).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public MyOrderviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderView= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posting_order_layout,parent,false);
        return new OrderAdapter.MyOrderviewHolder(orderView);    }



    public class MyOrderviewHolder extends RecyclerView.ViewHolder {

        CircleImageView ImgPost;
        TextView NamePublisher, getDatePost, NumberContact, numberCr, txtContenP, selectservicetruck;
        androidx.cardview.widget.CardView OrderItems;

        public MyOrderviewHolder(@NonNull View itemView) {
            super(itemView);
            ImgPost = itemView.findViewById(R.id.imgUserPost);
            NamePublisher = itemView.findViewById(R.id.nameUserPost);
            getDatePost = itemView.findViewById(R.id.date_orderpost);
            NumberContact = itemView.findViewById(R.id.txtNumberContact);
            numberCr =itemView.findViewById(R.id.txtNumberTruckOrder);
            txtContenP = itemView.findViewById(R.id.txtPostContent);
            selectservicetruck = itemView.findViewById(R.id.carServiceCategory);
            OrderItems = itemView.findViewById(R.id.card_itemOrder);


        }

    }

}
