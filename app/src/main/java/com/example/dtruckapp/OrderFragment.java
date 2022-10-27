package com.example.dtruckapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.dtruckapp.Adapter.NewsAdapter;
import com.example.dtruckapp.Adapter.OrderAdapter;
import com.example.dtruckapp.Model.Order;
import com.example.dtruckapp.Model.TheNews;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class OrderFragment extends Fragment {
    private RecyclerView orderrecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderlist,orderlistold;
    private DatabaseReference uDataref, PostRef, PostRefS;
    private SearchView searchViewO;

    FloatingActionButton fabPostOrder;
    private String saveCurrnentDate, saveCurrnentTime, postRandomname, CurrentUserName,CurrentUserIMG, searchtext;
    private String Current_user_id="";
    private FirebaseAuth userAuth;
    private FirebaseDatabase fdatabase;
    private EditText phonecontactS,carneeded,ordercontent;
    private Button uploadToList,CancelUp;
    private ProgressDialog loadingBar;

    LinearLayoutManager linearLayoutManagerO; // for sorting
    SharedPreferences mSharedPref; //for saving sorting setting
    Toolbar toolbar;
    private MenuItem menuItem;

    String[] carservice = {"Vận chuyển Hàng hóa", "Vận chuyển nhà", "Thuê xe", "Du lịch"};
    AutoCompleteTextView autoCompleteCarser;
    ArrayAdapter<String> adapterListSer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Oview =  inflater.inflate(R.layout.fragment_order, container, false);

        fabPostOrder = Oview.findViewById(R.id.btnAddPostOrder);
        userAuth = FirebaseAuth.getInstance();
        Current_user_id = userAuth.getCurrentUser().getUid();
        toolbar = Oview.findViewById(R.id.layoutheader);

        AppCompatActivity activityS = (AppCompatActivity)getActivity();
        activityS.setSupportActionBar(toolbar);
        activityS.getSupportActionBar().setTitle("Tìm đơn ủy thác");

        loadingBar = new ProgressDialog(getContext());

        fdatabase = FirebaseDatabase.getInstance();
        uDataref = fdatabase.getReference().child("User");
        PostRef = fdatabase.getReference().child("Orders");
        orderrecyclerView = Oview.findViewById(R.id.recview_order);
        /*
        LinearLayoutManager linearLayoutManagerO = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        */
        mSharedPref= getActivity().getSharedPreferences("SortSetting", Context.MODE_PRIVATE);
        String mSorting= mSharedPref.getString("Sort","newest");// where if no setting, it default is newesst
        if (mSorting.equals("newest")){
            linearLayoutManagerO = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
            linearLayoutManagerO.setReverseLayout(true);
            linearLayoutManagerO.setStackFromEnd(true);

        }
        else if (mSorting.equals("oldest")){
            linearLayoutManagerO = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            linearLayoutManagerO.setReverseLayout(false);
            linearLayoutManagerO.setStackFromEnd(false);

        }
        orderrecyclerView.setHasFixedSize(true);
        orderrecyclerView.setLayoutManager(linearLayoutManagerO);
        orderlist = new ArrayList<>();
        orderlistold = new ArrayList<>();
        orderAdapter= new OrderAdapter(orderlist,orderlistold);
        orderrecyclerView.setAdapter(orderAdapter);

        GetOrder();

        fabPostOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadOrder(Gravity.CENTER);
            }
        });
        return Oview;
    }

    private void GetOrder() {
        PostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Order orders = dataSnapshot.getValue(Order.class);
                    orderlist.add(orders);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void UploadOrder(int gravity) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fabitem);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        phonecontactS= dialog.findViewById(R.id.PhoneContactS);
        carneeded = dialog.findViewById(R.id.numbercar);
        ordercontent = dialog.findViewById(R.id.OrderDetail);
        uploadToList = dialog.findViewById(R.id.btnUploadList);
        CancelUp = dialog.findViewById(R.id.btnCancelDialog);
        autoCompleteCarser = dialog.findViewById(R.id.categoryCarSer);
        adapterListSer = new ArrayAdapter<String>(getContext(),R.layout.list_service_car,carservice);
        autoCompleteCarser.setAdapter(adapterListSer);


        CancelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        autoCompleteCarser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String carservices = adapterView.getItemAtPosition(i).toString();
            }
        });

        uploadToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedSer = autoCompleteCarser.getText().toString();
                String phoneContactc = phonecontactS.getEditableText().toString().trim();
                String carNeeded = carneeded.getEditableText().toString().trim();
                String descOrder = ordercontent.getEditableText().toString().trim();

                if (TextUtils.isEmpty(selectedSer)){
                    Toast.makeText(getContext(),"Xin hãy chọn loại hình ủy thác",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(phoneContactc)){
                    Toast.makeText(getContext(),"Xin hãy nhập số điện thoại liên lạc",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(carNeeded)){
                    Toast.makeText(getContext(),"Xin hãy nhập số lượng xe bạn cần vận chuyển hay thuê",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(descOrder)){
                    Toast.makeText(getContext(),"Xin hãy miêu tả nội dung đơn ủy thác để các tài xế hiểu rõ",Toast.LENGTH_LONG).show();
                }
                else {
                    UploadOrders(dialog,selectedSer,phoneContactc,carNeeded,descOrder);
                }

            }
        });

    }

    private void UploadOrders(Dialog dialog, String selectedSer, String phoneContactc, String carNeeded, String descOrder) {
        loadingBar.setTitle("Đăng ủy thác");
        loadingBar.setMessage("Vui lòng chờ đợi chúng tôi đăng ủy thác cho bạn");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrnentDate = currentDate.format(calforDate.getTime());

        Calendar calforTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH;mm");
        saveCurrnentTime = currentTime.format(calforTime.getTime());

        postRandomname = saveCurrnentDate + " "+ saveCurrnentTime;


        uDataref.child(Current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentUserName = snapshot.child("fullName").getValue().toString();
                    CurrentUserIMG = snapshot.child("urlImage").getValue().toString();

                    Map<String,Object> mapPost=new HashMap<>();
                    mapPost.put("orderuid",Current_user_id + postRandomname);
                    mapPost.put("orderNameUser",CurrentUserName);
                    mapPost.put("orderUserImg",CurrentUserIMG);
                    mapPost.put("dataTimePost",postRandomname);
                    mapPost.put("orderNumberPhone",phoneContactc);
                    mapPost.put("orderCarRequired",carNeeded);
                    mapPost.put("orderDesc",descOrder);
                    mapPost.put("orderCategory",selectedSer);
                    PostRef.child(Current_user_id + postRandomname).setValue(mapPost)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(),"Đăng ủy thác thành công!!",Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        dialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Gặp lỗi khi đăng!!",Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu,@NonNull MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu_item,menu);

        MenuItem item = menu.findItem(R.id.SearchID);

        searchViewO = (SearchView) item.getActionView();
        searchViewO.setMaxWidth(Integer.MAX_VALUE);
        searchViewO.setIconified(true);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchViewO.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchViewO.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                orderAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, menuInflater);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //handle other action bar item
        if (id == R.id.action_sort){
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] sortOptions = {"Newest","Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            //newest
                            OrderFragment orderFragment = new OrderFragment();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","newest");
                            editor.apply();
                            fm.beginTransaction().replace(R.id.containerFL,orderFragment)
                                    .commit();

                        }
                        else if (i==1){
                            //oldest
                            OrderFragment orderFragment = new OrderFragment();
                            FragmentManager fm = getActivity().getSupportFragmentManager();

                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();

                            fm.beginTransaction().replace(R.id.containerFL,orderFragment)
                                    .commit();

                        }
                    }
                });
        builder.show();
    }

}