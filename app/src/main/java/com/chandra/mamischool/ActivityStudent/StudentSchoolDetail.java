package com.chandra.mamischool.ActivityStudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandra.mamischool.Class.Facilities;
import com.chandra.mamischool.Fragment.AchievementFragment;
import com.chandra.mamischool.Fragment.DescriptionFragment;
import com.chandra.mamischool.Fragment.ExtracurriculerFragment;
import com.chandra.mamischool.Fragment.FacilitiesFragment;
import com.chandra.mamischool.R;
import com.chandra.mamischool.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class StudentSchoolDetail extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvSchoolName;
    private ImageView imgSchool, chat;
    FloatingActionButton fabZooning;
    public String id,akre,alamat,kelas,siswa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_school_detail);
        tvSchoolName = findViewById(R.id.school_name_detail);
        imgSchool = findViewById(R.id.image_school_detail);
        tvSchoolName.setText(getIntent().getStringExtra("iSchoolName"));
        fabZooning = findViewById(R.id.fab_zooning);
        chat = findViewById(R.id.chat);
        String number = getIntent().getStringExtra("iPhone");

        assert number != null;
        final String number2 = "+62" +number.substring(1);
        System.out.println("==================================================="+number2);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://wa.me/"+number2));
                startActivity(intent);
            }
        });

        fabZooning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSchoolDetail.this,StudentZooning.class);
                intent.putExtra("id",getIntent().getStringExtra("iId"));
                intent.putExtra("schoolName",getIntent().getStringExtra("iSchoolName"));
                intent.putExtra("latitude",getIntent().getStringExtra("iLatitude"));
                intent.putExtra("longitude",getIntent().getStringExtra("iLongitude"));
                intent.putExtra("radius",getIntent().getStringExtra("iRadius"));
                startActivity(intent);
            }
        });

        String url = getIntent().getStringExtra("iImgSchool");
        Picasso.get()
                .load(url)
//                        .error(R.drawable.ic_image_off)
                .fit()
                .into(imgSchool);

        this.id = getIntent().getStringExtra("iId");
        this.akre = getIntent().getStringExtra("iAccreditation");
        this.alamat = getIntent().getStringExtra("iAddress");
        this.kelas = getIntent().getStringExtra("iClass");
        this.siswa = getIntent().getStringExtra("iStudent");
        System.out.println("=================================kelas siswa"+getIntent().getStringExtra("iClass"));
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        adapter = new TabAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments

        adapter.addFragment(new DescriptionFragment(), "Description");
        adapter.addFragment(new AchievementFragment(), "Achievement");
        adapter.addFragment(new FacilitiesFragment(), "Facilities");
        adapter.addFragment(new ExtracurriculerFragment(), "Extracurriculer");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public String getMyID() {
        return id;
    }
    public String[] getMyData(){
        String[] data = new String[4];
        data[0] = this.akre;
        data[1] = this.alamat;
        data[2] = this.kelas;
        data[3] = this.siswa;
        return data;}
}
