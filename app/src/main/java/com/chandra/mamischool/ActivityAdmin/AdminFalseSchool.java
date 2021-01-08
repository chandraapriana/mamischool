package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminFalseSchool extends AppCompatActivity {
    ImageView btnLogout;
    Button btnCreateSchool;

    TextView btnAdminRegister ;
    Button btnAdminLogin;
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,forgotTextLink;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_false_school);
        btnLogout = (ImageView) findViewById(R.id.logout);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminFalseSchool.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminFalseSchool.this, AdminLogin.class));
            }
        });

       btnCreateSchool = (Button) findViewById(R.id.btn_create_school);
       btnCreateSchool.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(AdminFalseSchool.this,AdminCreateSchool.class));
           }
       });
    }


}
