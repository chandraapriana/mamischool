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

import com.chandra.mamischool.Class.Extracurricular;
import com.chandra.mamischool.Dialog.DialogDeleteExtracurricular;
import com.chandra.mamischool.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterExtracurriculer extends RecyclerView.Adapter<AdapterExtracurriculer.MyViewHolder > {
    private Context context;
    private ArrayList<Extracurricular> listExtracurricular = new ArrayList<>();
    Activity activity;
    public AdapterExtracurriculer(Context context, ArrayList<Extracurricular> listExtracurricular,Activity activity) {
        this.context = context;
        this.listExtracurricular = listExtracurricular;
        this.activity = activity;
    }
    public AdapterExtracurriculer(Activity activity){
        this.activity = activity;
    }
    public AdapterExtracurriculer(Context context, ArrayList<Extracurricular> listExtracurriculer){
        this.context = context;
        this.listExtracurricular = listExtracurriculer;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_extracurricular,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Extracurricular extracurricular = listExtracurricular.get(position);
        System.out.println("==================================="+holder.txtTitle);
            holder.txtTitle.setText(extracurricular.getTitle());
        Picasso.get().load(extracurricular.getImage()).into(holder.imgExtracurricular);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                String desc_id = extracurricular.getId();
                openDialog(desc_id);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listExtracurricular.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imgExtracurricular;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.extracurricular_title);
            imgExtracurricular=itemView.findViewById(R.id.img_cover_extracurricular);

        }
    }

    public void openDialog(String desc_id){
        DialogDeleteExtracurricular dialog = new DialogDeleteExtracurricular(this.activity);

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
