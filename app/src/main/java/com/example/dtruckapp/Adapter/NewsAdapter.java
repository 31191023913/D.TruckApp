package com.example.dtruckapp.Adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dtruckapp.DetailNewsFragment;
import com.example.dtruckapp.HomeFragment;
import com.example.dtruckapp.Model.TheNews;
import com.example.dtruckapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsAdapter extends FirebaseRecyclerAdapter<TheNews,NewsAdapter.MyNewsviewHolder> {


    public NewsAdapter(@NonNull FirebaseRecyclerOptions<TheNews> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyNewsviewHolder holder, int position, @NonNull TheNews model) {
        holder.nameNewss.setText(model.getTenTT());
        holder.datenews.setText(model.getNgayCN());
        holder.ttnew.setText(model.getTt());
        Glide.with(holder.imgNEW.getContext()).load(model.getImgurl()).into(holder.imgNEW);
        //Chua xong button chuyen sang trang detailNews
            holder.newItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity)view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerFL,new DetailNewsFragment(model.getTenTT(),model.getNgayCN(),model.getDetailTT(), model.getImgurl())).addToBackStack(null).commit();
                }

            });
    }

    private void OnClickGoToDetail(TheNews model) {


    }

    @NonNull
    @Override
    public MyNewsviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerownews,parent,false);
        return new MyNewsviewHolder(view);
    }

    public static class MyNewsviewHolder extends RecyclerView.ViewHolder{
        ImageView imgNEW;
        TextView nameNewss, datenews, ttnew;
        androidx.cardview.widget.CardView newItems;

        public MyNewsviewHolder(@NonNull View itemView) {
            super(itemView);
            newItems = itemView.findViewById(R.id.card_itemnews);
            imgNEW = (ImageView) itemView.findViewById(R.id.imgNewss);
            nameNewss = (TextView) itemView.findViewById(R.id.titleNew);
            datenews = (TextView)itemView.findViewById(R.id.dateofNew) ;
            ttnew = (TextView)itemView.findViewById(R.id.descriptionNews);
        }
    }

}
