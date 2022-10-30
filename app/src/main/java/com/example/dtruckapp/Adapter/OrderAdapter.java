package com.example.dtruckapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyOrderViewHolder> implements Filterable{

    List<Order> ordershow;
    List<Order> ordershowOld;

    public OrderAdapter(List<Order> ordershow, List<Order> ordershowOld) {
        this.ordershow = ordershow;
        this.ordershowOld = ordershowOld;
    }


    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderView= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posting_order_layout,parent,false);
        return new OrderAdapter.MyOrderViewHolder(orderView);    }


    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {
        Order orderL = ordershow.get(position);
        if (orderL==null){
            return;
        }
        holder.NamePublisher.setText(orderL.getOrderNameUser());
        holder.getDatePost.setText(orderL.getDataTimePost());
        holder.NumberContact.setText(orderL.getOrderNumberPhone());
        holder.numberCr.setText(orderL.getOrderCarRequired());
        holder.txtContenP.setText(orderL.getOrderDesc());
        holder.selectservicetruck.setText(orderL.getOrderCategory());
        Glide.with(holder.ImgPost.getContext())
                .load(orderL.getOrderUserImg())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(holder.ImgPost);
        holder.OrderItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrderIDs = orderL.getOrderuid();
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

    @Override
    public int getItemCount() {
        if (ordershow!= null){
            return ordershow.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyOrderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ImgPost;
        TextView NamePublisher, getDatePost, NumberContact, numberCr, txtContenP, selectservicetruck;
        androidx.cardview.widget.CardView OrderItems;

        public MyOrderViewHolder(@NonNull View itemView) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    ordershow = ordershowOld;
                }else {
                    List<Order> orderLis = new ArrayList<>();
                    for (Order orderL:ordershowOld){
                        if ( orderL.getOrderDesc().toLowerCase().contains(strSearch.toLowerCase()) ||
                         orderL.getOrderCategory().toLowerCase().contains(strSearch.toLowerCase()) ||
                        orderL.getOrderNumberPhone().toLowerCase().contains(strSearch.toLowerCase()) ){
                            orderLis.add(orderL);
                        }
                    }
                    ordershow = orderLis;
                }

                FilterResults filterResults= new FilterResults();
                filterResults.values = ordershow;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ordershow = (List<Order>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

}

