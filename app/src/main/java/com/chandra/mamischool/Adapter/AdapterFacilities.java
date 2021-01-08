package com.chandra.mamischool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chandra.mamischool.Class.Facilities;
import com.chandra.mamischool.Dialog.DialogDeleteFacility;
import com.chandra.mamischool.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFacilities extends RecyclerView.Adapter<AdapterFacilities.MyViewHolder> {
    private Context context;
    private ArrayList<Facilities> listFacilities = new ArrayList<>();
    Activity activity;
    public AdapterFacilities(Context context, ArrayList<Facilities> listFacilities) {
        this.context = context;
        this.listFacilities = listFacilities;
    }

    @NonNull
    @Override
    public AdapterFacilities.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_facilities,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Facilities facilities = listFacilities.get(position);
        holder.txtTitle.setText(facilities.getTitle().toString());
        System.out.println("============================="+facilities.getTitle());
        System.out.println("==================================="+facilities.getId());
        Picasso.get().load(facilities.getImg()).into(holder.imgFacilities);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                String desc_id = facilities.getId();
                openDialog(desc_id);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFacilities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imgFacilities;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.facilities_title);
            imgFacilities = itemView.findViewById(R.id.img_cover_facilities);
        }
    }

    public void openDialog(String desc_id){
        DialogDeleteFacility dialog = new DialogDeleteFacility(activity);

        Bundle bundle = new Bundle();
        bundle.putString("header", desc_id);
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"dialog");

        System.out.println("=============="+dialog.SUCCESS_UPLOAD);
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }
}
