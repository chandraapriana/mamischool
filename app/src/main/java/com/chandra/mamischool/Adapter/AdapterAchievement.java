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

import com.chandra.mamischool.Class.Achievement;
import com.chandra.mamischool.Dialog.DialogDeleteAchievement;
import com.chandra.mamischool.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterAchievement extends RecyclerView.Adapter<AdapterAchievement.MyViewHolder> {
    private Context context;
    private ArrayList<Achievement> listAchievment = new ArrayList<>();

    Activity activity;

    public AdapterAchievement(Context context, ArrayList<Achievement> listAchievment, Activity activity){
        this.context = context;
        this.listAchievment = listAchievment;
        this.activity = activity;
    }
    public AdapterAchievement(Activity activity){
        this.activity = activity;
    }
    public AdapterAchievement(Context context, ArrayList<Achievement> listAchievement){
        this.context = context;
        this.listAchievment = listAchievement;
    }

    @NonNull
    @Override
    public AdapterAchievement.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_achievment,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Achievement achievement = listAchievment.get(position);
        System.out.println("=======================================ADAPATER"+achievement.getLevel());
        holder.txtTitle.setText(achievement.getTitle().toString());
        holder.txtYear.setText(achievement.getYear().toString()+" - ");
        holder.txtNumber.setText("Juara "+achievement.getNumber().toString()+" ");
        holder.txtLevel.setText(achievement.getLevel().toString());
        Picasso.get().load(achievement.getImgAchievement()).into(holder.imgAchievement);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                String desc_id = achievement.getId();
                openDialog(desc_id);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAchievment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtNumber,txtLevel, txtYear;
        ImageView imgAchievement;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle =itemView.findViewById(R.id.txt_title);
            txtLevel = itemView.findViewById(R.id.txt_level);
            txtNumber = itemView.findViewById(R.id.txt_number);
            txtYear = itemView.findViewById(R.id.txt_year);
            imgAchievement = itemView.findViewById(R.id.img_cover_achievment);

        }
    }

    public void openDialog(String desc_id){
        DialogDeleteAchievement dialog = new DialogDeleteAchievement(activity);

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
