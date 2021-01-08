package com.chandra.mamischool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chandra.mamischool.Class.Description;
import com.chandra.mamischool.Dialog.DialogDeleteDescription;
import com.chandra.mamischool.R;

import java.util.ArrayList;

public class AdapterDescription extends RecyclerView.Adapter<AdapterDescription.MyViewHolder> {
    private Context context;
    private ArrayList<Description> listDescription = new ArrayList<>();
    Activity activity;
    public AdapterDescription(Context context, ArrayList<Description> listDescription, Activity activity) {
        this.context = context;
        this.listDescription = listDescription;
        this.activity = activity;
    }
    public AdapterDescription(Activity activity){
        this.activity = activity;
    }
    public AdapterDescription(Context context, ArrayList<Description> listDescription){
        this.context = context;
        this.listDescription = listDescription;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_description,parent,false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Description description = listDescription.get(position);
        holder.txtTitle.setText(description.getTitle());
        holder.txtBody.setText(description.getDescription());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                String desc_id = description.getId();
                openDialog(desc_id);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDescription.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtBody;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.description_title);
            txtBody = itemView.findViewById(R.id.description_body);
        }
    }

    public void openDialog(String desc_id){
        DialogDeleteDescription dialog = new DialogDeleteDescription(activity);

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
