package com.example.dtruckapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dtruckapp.Common.Common;
import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class DetailOrderFragment extends Fragment {
    private EditText NumberTruckProvived;
    private TextView Publisher, DateUp, PhoneContact, CategoryPost, DetailOrders, numberTheywant;
    private Button TakeOrder, DeleteOrder, btnNos,btnYess;
    private ImageButton GoBackList;
    private String orderIDs,Nt,Rq;
    private DatabaseReference OrderDaRef, AddToCart;
    private FirebaseAuth Dauth;
    private String Current_Driver_id;
    private int giveN = 0,requica = 0;

    public DetailOrderFragment() {
    }

    public DetailOrderFragment(String orderIDs) {
        this.orderIDs=orderIDs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View OrderDview = inflater.inflate(R.layout.fragment_detail_order, container, false);

        orderIDs = getArguments().getString("Orderids");//Get id order ban chon

        Publisher = OrderDview.findViewById(R.id.postName);
        DateUp = OrderDview.findViewById(R.id.postTime);
        PhoneContact = OrderDview.findViewById(R.id.postContact);
        CategoryPost = OrderDview.findViewById(R.id.CategoryTruckSer);
        DetailOrders = OrderDview.findViewById(R.id.OrderDetails);
        TakeOrder = OrderDview.findViewById(R.id.btnTakeOrder);
        numberTheywant = OrderDview.findViewById(R.id.numberTheywant);
        GoBackList = OrderDview.findViewById(R.id.backtolist);
        NumberTruckProvived = OrderDview.findViewById(R.id.NumberTruckYouProvive);
        DeleteOrder = OrderDview.findViewById(R.id.btnDeleteOrder);

        Dauth = FirebaseAuth.getInstance();
        Current_Driver_id = Dauth.getCurrentUser().getUid();

        getOrderDetail(orderIDs);

        GoBackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderFragment listorder = new OrderFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerFL,listorder).commit();
            }
        });
        TakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCartList();
            }
        });

        if (orderIDs.contains(Current_Driver_id)){
            DeleteOrder.setVisibility(View.VISIBLE);
            DeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteThisOrder(orderIDs,Gravity.CENTER);
                }
            });
        }

        return OrderDview;
        
    }

    private void DeleteThisOrder(String orderIDs, int center) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fabitem1);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OrderDaRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        btnNos = dialog.findViewById(R.id.btnNoCancels);
        btnYess = dialog.findViewById(R.id.btnYesDeleteIT);

        btnNos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnYess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDaRef.child(orderIDs).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"H???y ????n th??nh c??ng!!",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            OrderFragment ListOrder = new OrderFragment();
                            AppCompatActivity goBackList = (AppCompatActivity)getContext();
                            FragmentTransaction fragmentTransaction = goBackList.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.containerFL,ListOrder).disallowAddToBackStack().commit();

                        }
                    }
                });
            }
        });
    }

    private void AddToCartList() {
        Nt = NumberTruckProvived.getText().toString();
        if (!TextUtils.isEmpty(Nt)){
            giveN = Integer.parseInt(Nt);
        }
        Rq = numberTheywant.getText().toString();
        if (!TextUtils.isEmpty(Rq)){
            requica = Integer.parseInt(Rq);
        }
        OrderDaRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        AddToCart = FirebaseDatabase.getInstance().getReference().child("OrderReceiveList").child(Current_Driver_id);
        Map<String,Object> OrderTake=new HashMap<>();
        OrderTake.put("orderuid",orderIDs);
        OrderTake.put("orderNamePublisher",Publisher.getText().toString());
        OrderTake.put("dataTimePost",DateUp.getText().toString());
        OrderTake.put("orderNumberPhone",PhoneContact.getText().toString());
        OrderTake.put("orderCarProvived",Nt);
        OrderTake.put("orderDesc",DetailOrders.getText().toString());
        OrderTake.put("orderCategory",CategoryPost.getText().toString());
        OrderTake.put("orderTakenBy", Common.currentUser.getFullName());

        if (orderIDs.contains(Current_Driver_id)){
            Toast.makeText(getContext(),"????y l?? ???y th??c c???a b???n n???u kh??ng mu???n c?? th??? h???y!!",Toast.LENGTH_LONG).show();
        } else if (giveN == 0) {
            Toast.makeText(getContext(),"B???n ch??a nh???p s??? l?????ng xe b???n s??? cung ???ng!!",Toast.LENGTH_LONG).show();
        } else if (giveN < Integer.parseInt(numberTheywant.getText().toString())) {
            AddToCart.child(orderIDs).setValue(OrderTake).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Nh???n ???y th??c th??nh c??ng!!", Toast.LENGTH_LONG).show();
                        Map<String, Object> mapNuM = new HashMap<>();
                        mapNuM.put("orderCarRequired", String.valueOf(requica - giveN));
                        OrderDaRef.child(orderIDs).updateChildren(mapNuM);
                        OrderFragment ListOrder = new OrderFragment();
                        AppCompatActivity goBackList = (AppCompatActivity)getContext();
                        FragmentTransaction fragmentTransaction = goBackList.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerFL,ListOrder).disallowAddToBackStack().commit();
                    }
                }
            });
        } else if (giveN == requica) {
            AddToCart.child(orderIDs).setValue(OrderTake).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Nh???n ???y th??c th??nh c??ng!!", Toast.LENGTH_LONG).show();
                        OrderDaRef.child(orderIDs).removeValue();
                        OrderFragment ListOrder = new OrderFragment();
                        AppCompatActivity goBackList = (AppCompatActivity)getContext();
                        FragmentTransaction fragmentTransaction = goBackList.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerFL,ListOrder).disallowAddToBackStack().commit();
                    }
                }
            });
        } else if (giveN > requica) {
            Toast.makeText(getContext(), "S??? l?????ng xe ???y th??c v?????t qu?? y??u c???u!!", Toast.LENGTH_LONG).show();
        }


    }

    private void getOrderDetail(String orderIDs) {

        OrderDaRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        OrderDaRef.child(orderIDs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Order orderdetail = snapshot.getValue(Order.class);

                    Publisher.setText(orderdetail.getOrderNameUser());
                    DateUp.setText(orderdetail.getDataTimePost());
                    PhoneContact.setText(orderdetail.getOrderNumberPhone());
                    CategoryPost.setText(orderdetail.getOrderCategory());
                    DetailOrders.setText(orderdetail.getOrderDesc());
                    numberTheywant.setText(orderdetail.getOrderCarRequired());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}