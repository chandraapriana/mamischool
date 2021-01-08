package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandra.mamischool.Adapter.AdapterExtracurriculer;
import com.chandra.mamischool.Dialog.DialogExtracurricular;
import com.chandra.mamischool.Class.Extracurricular;
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

public class AdminExtracuricullar extends AppCompatActivity implements DialogExtracurricular.DialogListener {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference mDocRef;
    FloatingActionButton fabExtracurricular;
    String userID;

    AdapterExtracurriculer adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_extracuricullar);
        recyclerView = findViewById(R.id.rv_extracurricular);
        fabExtracurricular = findViewById(R.id.fab_extracurricular);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        mDocRef = fStore.collection("schoolExtracurriculer");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Extracurricular> listFacilities = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        System.out.println("======================="+document.getData().get("title"));
                        Extracurricular extracurricular = new Extracurricular();
                        extracurricular.setTitle( document.getData().get("title").toString());
                        extracurricular.setImage(document.get("imgExtracurricular").toString());
                        extracurricular.setId(document.getId());
                        listFacilities.add(extracurricular);
                    }
                    adapter = new AdapterExtracurriculer(AdminExtracuricullar.this,listFacilities);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminExtracuricullar.this));

                }else{
                    Toast.makeText(AdminExtracuricullar.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabExtracurricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    public void openDialog(){
        DialogExtracurricular dialog = new DialogExtracurricular(AdminExtracuricullar.this);
        dialog.show(getSupportFragmentManager(),"dialog");
        System.out.println("========================="+dialog.SUCCESS_UPLOAD);
    }


    @Override
    public void applyTexts(String iTitle, String iNumber, String iLevel, String iYear) {

    }
}
