package com.example.dtruckapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.OrderReceive;
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


public class FinishOrderFragment extends Fragment {
    private String orderIDs, numbercontacts, namePubl,txtContents, selectServi,numberUgives,driverTake;
    private TextView Publisher, PhoneContact, CategoryPost, DetailOrders, numberUgive, CompleteOrder, btnDone, buttonCancelOrder;
    private Button BtnYesss, BtnNooo;
    private DatabaseReference completeOrder, recentOrder;
    ImageButton GoBackListCartR;
    private FirebaseAuth Dauth;
    private String Current_Driver_id;
    private ProgressDialog loadingBar;

    public FinishOrderFragment() {
        // Required empty public constructor
    }
    public FinishOrderFragment(String orderIDs,String namePubl ,String numbercontacts, String txtContents, String selectServi, String numberUgives, String driverTake) {
        this.orderIDs = orderIDs;
        this.namePubl = namePubl;
        this.numbercontacts = numbercontacts;
        this.txtContents = txtContents;
        this.selectServi = selectServi;
        this.numberUgives = numbercontacts;
        this.driverTake = driverTake;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View FOview =inflater.inflate(R.layout.fragment_finish_order, container, false);
        Publisher = FOview.findViewById(R.id.postNameC);
        PhoneContact = FOview.findViewById(R.id.postContactC);
        CategoryPost = FOview.findViewById(R.id.CategoryTruckSerC);
        DetailOrders = FOview.findViewById(R.id.OrderDetailsC);
        numberUgive = FOview.findViewById(R.id.numberUwantC);
        GoBackListCartR = FOview.findViewById(R.id.backtolistCart);
        CompleteOrder = FOview.findViewById(R.id.buttonPay);
        buttonCancelOrder =FOview.findViewById(R.id.buttonCancelOrder);

        Publisher.setText(namePubl);
        PhoneContact.setText(numbercontacts);
        CategoryPost.setText(selectServi);
        DetailOrders.setText(txtContents);
        numberUgive.setText(numberUgives);

        loadingBar = new ProgressDialog(getContext());

        orderIDs = getArguments().getString("Orderids");
        Dauth = FirebaseAuth.getInstance();
        Current_Driver_id = Dauth.getCurrentUser().getUid();

        GoBackListCartR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment cfragment = new CartFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerFL,cfragment).commit();

            }
        });

        CompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteXD(Gravity.CENTER,orderIDs,numbercontacts, namePubl,txtContents, selectServi,numberUgives,driverTake);
            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelOrder(Gravity.CENTER,orderIDs);
            }
        });

        return FOview;
    }

    private void CancelOrder(int centers, String orderIDs) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fabitem3);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = centers;
        window.setAttributes(windowAttributes);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        BtnYesss = dialog.findViewById(R.id.btnYesIDo);
        BtnNooo = dialog.findViewById(R.id.btnNoIdontWant);
        recentOrder = FirebaseDatabase.getInstance().getReference().child("OrderReceiveList").child(Current_Driver_id);

        BtnNooo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        BtnYesss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recentOrder.child(orderIDs).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"Hủy đơn thành công!!",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            CartFragment cartF = new CartFragment();
                            AppCompatActivity goBackCa = (AppCompatActivity)getContext();
                            FragmentTransaction fragmentTransaction = goBackCa.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.containerFL,cartF).disallowAddToBackStack().commit();
                        }
                    }
                });
            }
        });

    }

    private void CompleteXD(int center, String orderIDs, String numbercontacts, String namePubl, String txtContents, String selectServi, String numberUgives, String driverTake) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fabitem2);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        btnDone = dialog.findViewById(R.id.buttonDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setTitle("Thanh toán");
                loadingBar.setMessage("Vui lòng chờ đợi hệ thống phản hồi");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);
                Map<String,Object> GotoAdmin = new HashMap<>();
                GotoAdmin.put("orderuid",orderIDs);
                GotoAdmin.put("orderNamePublisher",namePubl);
                GotoAdmin.put("orderDriverFinishO",driverTake);
                GotoAdmin.put("orderNumberOfPublisher",numbercontacts);
                GotoAdmin.put("orderCategory",selectServi);
                GotoAdmin.put("orderDetail",txtContents);
                GotoAdmin.put("orderNumberTruck",numberUgives);
                recentOrder = FirebaseDatabase.getInstance().getReference().child("OrderReceiveList").child(Current_Driver_id);
                completeOrder = FirebaseDatabase.getInstance().getReference().child("DoneOrder").child(Current_Driver_id);
                completeOrder.child(orderIDs).setValue(GotoAdmin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingBar.dismiss();
                        Toast.makeText(getContext(),"Thanh toán thành công!!",Toast.LENGTH_LONG).show();
                        recentOrder.child(orderIDs).removeValue();
                        dialog.dismiss();
                        CartFragment cartF = new CartFragment();
                        AppCompatActivity goBackC = (AppCompatActivity)getContext();
                        FragmentTransaction fragmentTransaction = goBackC.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerFL,cartF).disallowAddToBackStack().commit();

                    }
                });
            }
        });



    }
    /*
    private void FinishOrders(String orderIDs) {
        completeOrder = FirebaseDatabase.getInstance().getReference().child("OrderReceiveList").child(Current_Driver_id);
        completeOrder.child(orderIDs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    OrderReceive orderdetail = snapshot.getValue(OrderReceive.class);

                    Publisher.setText(orderdetail.getOrderNamePublisher());
                    PhoneContact.setText(orderdetail.getOrderNumberPhone());
                    CategoryPost.setText(orderdetail.getOrderCategory());
                    DetailOrders.setText(orderdetail.getOrderDesc());
                    numberUgive.setText(orderdetail.getOrderCarProvived());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    */
}