package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandra.mamischool.Class.Achievement;
import com.chandra.mamischool.Adapter.AdapterAchievement;
import com.chandra.mamischool.Dialog.Dialog;
import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminAchievment extends AppCompatActivity implements Dialog.DialogListener {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference mDocRef;
    FloatingActionButton fabAchievement;
    String userID;

    AdapterAchievement adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_achievment);
        recyclerView = findViewById(R.id.rv_achievment);
        fabAchievement = findViewById(R.id.fab_achievment);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        mDocRef = fStore.collection("schoolAchievment");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Achievement> listAchievment = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        System.out.println("======================="+document.getData().get("title"));
                        Achievement achievement = new Achievement();
                        achievement.setId(document.getId());
                        achievement.setTitle( document.getData().get("title").toString());
                        achievement.setLevel( document.getData().get("level").toString());
                        achievement.setNumber( document.getData().get("number").toString());
                        achievement.setYear( document.getData().get("year").toString());
                        achievement.setImgAchievement(document.get("imgAchievment").toString());
                        listAchievment.add(achievement);
                    }
                    adapter = new AdapterAchievement(AdminAchievment.this,listAchievment);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminAchievment.this));

                }else{
                    Toast.makeText(AdminAchievment.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });


        fabAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();


            }
        });
    }

    public void openDialog(){
        Dialog dialog = new Dialog(AdminAchievment.this);
        dialog.show(getSupportFragmentManager(),"dialog");
        System.out.println("========================="+dialog.SUCCESS_UPLOAD);
    }

    @Override
    public void applyTexts(String iTitle, String iNumber, String iLevel, String iYear) {

    }
}
