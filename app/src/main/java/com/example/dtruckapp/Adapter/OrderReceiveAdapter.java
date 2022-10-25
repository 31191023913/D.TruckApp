package com.example.dtruckapp.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dtruckapp.DetailOrderFragment;
import com.example.dtruckapp.FinishOrderFragment;
import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.OrderReceive;
import com.example.dtruckapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class OrderReceiveAdapter extends FirebaseRecyclerAdapter<OrderReceive,OrderReceiveAdapter.MyOrderRViewHolder> {


    public OrderReceiveAdapter(@NonNull FirebaseRecyclerOptions<OrderReceive> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderReceiveAdapter.MyOrderRViewHolder holder, int position, @NonNull OrderReceive model) {
        holder.NumberContact.setText(model.getOrderNumberPhone());
        holder.txtContenP.setText(model.getOrderDesc());
        holder.selectservicetruck.setText(model.getOrderCategory());
        holder.numberCr.setText(model.getOrderCarProvived());
        holder.OrderRItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrderIDs = model.getOrderuid();
                String NamePubl = model.getOrderNamePublisher();;
                String numbercontacts = model.getOrderNumberPhone();;
                String txtContents = model.getOrderDesc();
                String selectServi  =model.getOrderCategory();
                String NumberUgives= model.getOrderCarProvived();
                String DriverTake = model.getOrderTakenBy();

                FinishOrderFragment CartFragmentss = new FinishOrderFragment(OrderIDs,NamePubl ,numbercontacts,txtContents,selectServi,NumberUgives,DriverTake);
                Bundle bundle = new Bundle();
                bundle.putString("Orderids",OrderIDs);
                CartFragmentss.setArguments(bundle);
                AppCompatActivity goDteil = (AppCompatActivity)view.getContext();
                FragmentTransaction fragmentTransaction = goDteil.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerFL,CartFragmentss).addToBackStack(null).commit();

            }
        });
    }

    @NonNull
    @Override
    public OrderReceiveAdapter.MyOrderRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View CartView= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_your_taken_order,parent,false);
        return new OrderReceiveAdapter.MyOrderRViewHolder(CartView);    }


    public class MyOrderRViewHolder extends RecyclerView.ViewHolder {

        TextView NumberContact, numberCr, txtContenP, selectservicetruck;
        androidx.cardview.widget.CardView OrderRItems;

        public MyOrderRViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberContact = itemView.findViewById(R.id.NumberContactOwner);
            numberCr = itemView.findViewById(R.id.QuantiyTruckofYou);
            txtContenP = itemView.findViewById(R.id.DesContentOrder);
            selectservicetruck= itemView.findViewById(R.id.LoaiHinhDon);
            OrderRItems= itemView.findViewById(R.id.card_itemOrderReceive);
        }
    }
}
