package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandra.mamischool.Adapter.AdapterFacilities;
import com.chandra.mamischool.Dialog.DialogFacilities;
import com.chandra.mamischool.Class.Facilities;
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

public class AdminFacilities extends AppCompatActivity implements DialogFacilities.DialogListener {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference mDocRef;
    FloatingActionButton fabFacilities;
    String userID;

    AdapterFacilities adapter;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_facilities);
        recyclerView = findViewById(R.id.rv_facilities);
        fabFacilities = findViewById(R.id.fab_facilities);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mDocRef = fStore.collection("schoolFacilities");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    ArrayList<Facilities> listFacilities = new ArrayList<>();
                    listFacilities.clear();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        System.out.println("======================="+document.getId());
                        Facilities facility = new Facilities();
                        facility.setTitle( document.getData().get("title").toString());
                        facility.setImg(document.get("imgAchievment").toString());
                        facility.setId(document.getId());
                        listFacilities.add(facility);
                    }
                    adapter = new AdapterFacilities(AdminFacilities.this,listFacilities);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminFacilities.this));

                }else{
                    Toast.makeText(AdminFacilities.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    public void openDialog(){
        DialogFacilities dialog = new DialogFacilities(AdminFacilities.this);
        dialog.show(getSupportFragmentManager(),"dialog");
        System.out.println("========================="+dialog.SUCCESS_UPLOAD);
    }

    @Override
    public void applyTexts(String iTitle, String iNumber, String iLevel, String iYear) {

    }
}
