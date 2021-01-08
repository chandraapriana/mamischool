package com.chandra.mamischool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chandra.mamischool.ActivityAdmin.AdminLogin;
import com.chandra.mamischool.ActivityStudent.StudentLogin;

public class MainActivity extends AppCompatActivity {
    Button btnStudent, btnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStudent=(Button)findViewById(R.id.btn_student);
        btnAdmin = (Button)findViewById(R.id.btn_admin);

        btnStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), StudentLogin.class);
                startActivity(i);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(i);
            }
        });
    }
}
