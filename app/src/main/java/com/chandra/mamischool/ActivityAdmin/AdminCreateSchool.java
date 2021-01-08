package com.chandra.mamischool.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chandra.mamischool.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminCreateSchool extends FragmentActivity{
    private static final int PICK_IMAGE_REQUEST=2;

    private StorageReference mStorageRef;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private Uri imageUri;
    GoogleMap map;
    String userID;


    EditText nameSchool,npsn,address,accreditation,students,Class,latitude,longitude,phone,zooning;
    Button btnPicker, btnCreateSchool, btnChooseImage;
    ImageView imageUpload;


    private ProgressBar progressBar;
    public static final String TAG = "TAG";

    int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_school);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnPicker = findViewById(R.id.btn_picker);
        btnCreateSchool = findViewById(R.id.btnCreateSchool);
        btnChooseImage = findViewById(R.id.btn_upload_images_school);
        imageUpload = findViewById(R.id.upload_images);

        nameSchool = findViewById(R.id.nameSchool);
        npsn = findViewById(R.id.npsn);
        address = findViewById(R.id.address);
        accreditation = findViewById(R.id.accreditation);
        students = findViewById(R.id.students);
        Class = findViewById(R.id.Class);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        phone = findViewById(R.id.create_phone);
        zooning = findViewById(R.id.create_radius);




        mStorageRef = FirebaseStorage.getInstance().getReference("imgSchool");

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        btnCreateSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fNameSchool = nameSchool.getText().toString().trim();
                final String fnpsn = npsn.getText().toString().trim();
                final String fAddress = address.getText().toString();
                final String fAccreditation    = accreditation.getText().toString();
                final String fStudents = students.getText().toString();
                final String fClass = Class.getText().toString();
                final String fLatitude = latitude.getText().toString();
                final String fLongitude = longitude.getText().toString();
                final String fphone = phone.getText().toString();
                final String fzooning = zooning.getText().toString();



                if(TextUtils.isEmpty(fNameSchool)){
                    nameSchool.setError("School Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fnpsn)){
                    npsn.setError("npsn is Required.");
                    return;
                }
                if(TextUtils.isEmpty(fAddress)){
                    address.setError("address is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fAccreditation)){
                    accreditation.setError("Accreditation is Required.");
                    return;
                }
                if(TextUtils.isEmpty(fStudents)){
                    students.setError("Amount Of Students is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fClass)){
                    Class.setError("Class is Required.");
                    return;
                }
                if(TextUtils.isEmpty(fLatitude)){
                    latitude.setError("latitude is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fLongitude)){
                    longitude.setError("Longitude is Required.");
                    return;
                }
                if(TextUtils.isEmpty(fphone)){
                    phone.setError("Phone is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fzooning)){
                    zooning.setError("Radius is Required.");
                    return;
                }


                if (imageUri!=null){
                    final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
                    System.out.println("================================"+imageUri);
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    userID = fAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("school").document(userID);
                                    final DocumentReference userReference = fStore.collection("users").document(userID);
                                    System.out.println("======================================="+userReference);
                                    final Map<String,Object> school = new HashMap<>();
                                    school.put("id",userID);
                                    school.put("schoolName",fNameSchool);
                                    school.put("npsn",fnpsn);
                                    school.put("address",fAddress);
                                    school.put("accreditation",fAccreditation);
                                    school.put("students",fStudents);
                                    school.put("classes",fClass);
                                    school.put("latitude",fLatitude);
                                    school.put("longitude",fLongitude);
                                    school.put("phone",fphone);
                                    school.put("radius",fzooning);
                                    Toast.makeText(AdminCreateSchool.this, "Upload Success", Toast.LENGTH_SHORT).show();
                                    school.put("imgSchool",uri.toString());
                                    documentReference.set(school).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            userReference.update("isSchool",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AdminCreateSchool.this, "Success Update Users School", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            Toast.makeText(AdminCreateSchool.this, "Created School", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onSuccess: School is created for "+ userID);
                                            Intent i = new Intent(getApplicationContext(),AdminHome.class);
                                            startActivity(i);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.toString());
                                        }
                                    });
                                }
                            });


//                            school.put("imgSchool",taskSnapshot.getStorage().getDownloadUrl().toString());


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminCreateSchool.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("==============================="+e.getMessage());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                            progressBar.setProgress((int)progress);

                        }
                    });
                }else{
                    Toast.makeText(AdminCreateSchool.this, "No FIle Selected", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AdminCreateSchool.this),PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {

                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                String latitudeMaps = String.valueOf(place.getLatLng().latitude);
                String longitudeMaps = String.valueOf(place.getLatLng().longitude);

                latitude.setText(latitudeMaps);
                longitude.setText(longitudeMaps);
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            imageUri = data.getData();


            Picasso.get()
                    .load(imageUri)
                    .fit()
                    .into(imageUpload);
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//
//        LatLng school = new LatLng(-7.078245244746735, 107.7365638161709);
//        map.addMarker(new MarkerOptions().position(school).title("Sekolah"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(school));
//    }

    private void openFileChoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
