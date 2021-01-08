package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chandra.mamischool.ActivityStudent.StudentHome;
import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AdminHome extends AppCompatActivity {
    Button btnAchievment,btnFacilities,btnEskul,btnDescription,btnSchoolPage;
    ImageView imgView,btnLogout;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference mDocRef;
    private String userID;
    TextView schoolName,classes,address,accreditation,students,npsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnAchievment = findViewById(R.id.btn_achievment);
        btnDescription = findViewById(R.id.btn_description);
        btnFacilities = findViewById(R.id.btn_facilities);
        btnLogout = findViewById(R.id.logout_home);
        btnSchoolPage = findViewById(R.id.student_home_page);

        btnSchoolPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, StudentHome.class);
                startActivity(intent);
            }
        });

        schoolName = findViewById(R.id.home_school_name);
        classes = findViewById(R.id.home_classes);
        address = findViewById(R.id.home_address);
        accreditation = findViewById(R.id.home_accreditation);
        students = findViewById(R.id.home_students);
        npsn = findViewById(R.id.home_npsn);


        btnEskul  = findViewById(R.id.btn_extracuricullar);
        imgView = findViewById(R.id.imageView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        mDocRef = fStore.collection("school").document(userID);
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                System.out.println("=================================="+document.getData().get("imgSchool").toString());
                Picasso.get()
                        .load(document.getData().get("imgSchool").toString())
                        .fit()
                        .into(imgView);
                schoolName.setText(document.getData().get("schoolName").toString());
                classes.setText(document.getData().get("classes").toString());
                address.setText(document.getData().get("address").toString());
                accreditation.setText(document.getData().get("accreditation").toString());
                students.setText(document.getData().get("students").toString());
                npsn.setText(document.getData().get("npsn").toString());

            }
        });

        btnAchievment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminAchievment.class);
                startActivity(intent);
            }
        });
        btnEskul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminExtracuricullar.class);
                startActivity(intent);
            }
        });
        btnFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminFacilities.class);
                startActivity(intent);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminHome.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminHome.this, AdminLogin.class));
            }
        });

        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminDescription.class);
                startActivity(intent);
            }
        });

    }
}
