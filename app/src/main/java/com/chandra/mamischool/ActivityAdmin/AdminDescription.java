package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandra.mamischool.Adapter.AdapterDescription;
import com.chandra.mamischool.Class.Description;
import com.chandra.mamischool.Dialog.DialogDescription;
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

public class AdminDescription extends AppCompatActivity implements DialogDescription.DialogListener {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference mDocRef;
    FloatingActionButton fabDescription;
    String userID;

    AdapterDescription adapter;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_description);
        recyclerView = findViewById(R.id.rv_description);
        fabDescription = findViewById(R.id.fab_description);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mDocRef = fStore.collection("schoolDescription");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    ArrayList<Description> listDescription = new ArrayList<>();
                    listDescription.clear();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Description description = new Description();
                        description.setId(document.getId());
                        description.setTitle(document.getData().get("title").toString());
                        description.setDescription(document.getData().get("body").toString());
                        listDescription.add(description);

                    }

                    adapter = new AdapterDescription(AdminDescription.this,listDescription);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminDescription.this));
                }else{
                    Toast.makeText(AdminDescription.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fabDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        DialogDescription dialog = new DialogDescription(AdminDescription.this);
        dialog.show(getSupportFragmentManager(),"dialog");
        System.out.println("=============="+dialog.SUCCESS_UPLOAD);
    }

    @Override
    public void applyTexts(String iTitle, String iNumber, String iLevel, String iYear) {

    }
}
