package com.chandra.mamischool.ActivityStudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chandra.mamischool.R;

public class StudentRegister extends AppCompatActivity {
    TextView btnStudentLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        btnStudentLogin = (TextView) findViewById(R.id.student_login);
        btnStudentLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),StudentLogin.class);
                startActivity(i);
            }
        });

    }
}
