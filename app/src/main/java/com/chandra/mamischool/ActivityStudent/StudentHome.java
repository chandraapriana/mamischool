package com.chandra.mamischool.ActivityStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandra.mamischool.ActivityAdmin.AdminLogin;
import com.chandra.mamischool.Class.School;
import com.chandra.mamischool.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class StudentHome extends AppCompatActivity {
    private RecyclerView recyclerView;
    ImageView admin;
    private FirebaseFirestore fStore;

    private FirestoreRecyclerAdapter adapter;
    private EditText etSchoolName;
    private Button btnSubmit;
    private String searchString;
    private String SCHOOL_NAME_EXTRA = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rv_list);
        etSchoolName = findViewById(R.id.school_name);
        btnSubmit = findViewById(R.id.submit);
        admin = findViewById(R.id.admin_login);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentHome.this, AdminLogin.class);
                startActivity(intent);
            }
        });

        Query query;

        if(getIntent().getStringExtra("SCHOOL_NAME_EXTRA") == null){
            query = fStore.collection("school");

            FirestoreRecyclerOptions<School> options = new FirestoreRecyclerOptions.Builder<School>()
                    .setQuery(query, School.class)
                    .build();
            adapter = new FirestoreRecyclerAdapter<School, SchoolViewHolder>(options){
                @NonNull
                @Override
                public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_item, parent, false);

                    return new SchoolViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(SchoolViewHolder holder, int position, final School model) {
                    System.out.println("==============================================================modelnya"+model.getId());
                    holder.tvName.setText(model.getSchoolName());
                    holder.tvAddress.setText(model.getAddress());
                    String url = model.getImgSchool();
                    Picasso.get()
                            .load(url)
//                        .error(R.drawable.ic_image_off)
                            .fit()
                            .into(holder.imgPhoto);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent (StudentHome.this, StudentSchoolDetail.class);
                            i.putExtra("iSchoolName", model.getSchoolName());
                            i.putExtra("iAddress",model.getAddress());
                            i.putExtra("iAccreditation",model.getAccreditation());
                            i.putExtra("iClass",model.getClasses());
                            i.putExtra("iImgSchool",model.getImgSchool());
                            i.putExtra("iLatitude",model.getLatitude());
                            i.putExtra("iLongitude",model.getLongitude());
                            i.putExtra("iPhone",model.getPhone());
                            i.putExtra("iRadius",model.getRadius());
                            i.putExtra("iNPSN",model.getNpsn());
                            i.putExtra("iStudent",model.getStudents());
                            i.putExtra("iId",model.getId());
                            startActivity(i);
                        }
                    });

                }
            };

        }else{

            CollectionReference schoolsRef = fStore.collection("school");
            query = schoolsRef.whereGreaterThanOrEqualTo("schoolName", getIntent().getStringExtra("SCHOOL_NAME_EXTRA"));

            FirestoreRecyclerOptions<School> options = new FirestoreRecyclerOptions.Builder<School>()
                    .setQuery(query, School.class)
                    .build();
            adapter = new FirestoreRecyclerAdapter<School, SchoolViewHolder>(options){
                @NonNull
                @Override
                public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_item, parent, false);

                    return new SchoolViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(SchoolViewHolder holder, int position, final School model) {
                    System.out.println("==============================================================modelnya"+model.getStudents());
                    holder.tvName.setText(model.getSchoolName());
                    holder.tvAddress.setText(model.getAddress());

                    String url = model.getImgSchool();
                    Picasso.get()
                            .load(url)
//                        .error(R.drawable.ic_image_off)
                            .fit()
                            .into(holder.imgPhoto);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent (StudentHome.this, StudentSchoolDetail.class);
                            i.putExtra("iSchoolName", model.getSchoolName());
                            i.putExtra("iAddress",model.getAddress());
                            i.putExtra("iAccreditation",model.getAccreditation());
                            i.putExtra("iClass",model.getClasses());
                            i.putExtra("iImgSchool",model.getImgSchool());
                            i.putExtra("iLatitude",model.getLatitude());
                            i.putExtra("iLongitude",model.getLongitude());
                            i.putExtra("iNPSN",model.getNpsn());
                            i.putExtra("iStudent",model.getStudents());
                            i.putExtra("iId",model.getId());
                            startActivity(i);
                        }
                    });

                }
            };

        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchString = etSchoolName.getText().toString();
                if (TextUtils.isEmpty(searchString)){
                    etSchoolName.setError("School name is required!");
                }else{
                    Intent detailBook = new Intent(getBaseContext(), StudentHome.class);
                    detailBook.putExtra("SCHOOL_NAME_EXTRA",searchString);
                    startActivity(detailBook);
                }

            }
        });
        //        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private class SchoolViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvAddress;
        private ImageView imgPhoto;
        public SchoolViewHolder(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_name);
            tvAddress = itemView.findViewById(R.id.txt_address);
            imgPhoto = itemView.findViewById(R.id.img_cover);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
