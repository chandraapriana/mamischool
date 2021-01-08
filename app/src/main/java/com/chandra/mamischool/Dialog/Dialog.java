package com.chandra.mamischool.Dialog;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.chandra.mamischool.ActivityAdmin.AdminHome;
import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Dialog extends AppCompatDialogFragment {

    private StorageReference mStorageRef;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Activity activity;
    public Integer SUCCESS_UPLOAD=0;
    
    private static final int PICK_IMAGE_REQUEST=2;
    EditText cpTitle, cpNumber,cpLevel,cpYear;
    ImageView imageUpload;
    Button btnChooseImage;
    Uri imageUri;

    private DialogListener listener;

    public Dialog(Activity activity){
        this.activity = activity;
    }


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        btnChooseImage = view.findViewById(R.id.btn_choose_img_achievment);
        imageUpload = view.findViewById(R.id.img_thumbnail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imgAchievment");

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        builder.setView(view);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final String iTitle = cpTitle.getText().toString();
                final String iYear = cpYear.getText().toString();
                final String iNumber = cpNumber.getText().toString();
                final String iLevel = cpLevel.getText().toString();
                if (TextUtils.isEmpty(iTitle)) {
                    cpTitle.setError("Title Required");
                    return;
                }
                if (TextUtils.isEmpty(iYear)) {
                    cpYear.setError("Year Required");
                    return;
                }
                if (TextUtils.isEmpty(iNumber)) {
                    cpNumber.setError("Number Required");
                    return;
                }
                if (TextUtils.isEmpty(iLevel)) {
                    cpLevel.setError("Level Required");
                    return;
                }

                if (imageUri != null) {
                    final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String userID = fAuth.getCurrentUser().getUid();

                                    CollectionReference documentReference = fStore.collection("schoolAchievment");
                                    final Map<String, Object> achievment = new HashMap<>();
                                    achievment.put("id", userID);
                                    achievment.put("title", iTitle);
                                    achievment.put("year", iYear);
                                    achievment.put("number", iNumber);
                                    achievment.put("level", iLevel);
                                    achievment.put("imgAchievment", uri.toString());
                                    documentReference.add(achievment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            System.out.println("SUKSES");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Fail upload", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }

                    });
                    Intent intent = new Intent(activity, AdminHome.class);
                    startActivity(intent);
                }

            }
        });

        cpTitle = view.findViewById(R.id.champion_title);
        cpNumber = view.findViewById(R.id.champion_number);
        cpYear = view.findViewById(R.id.champion_year);
        cpLevel = view.findViewById(R.id.champion_level);
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"Must imPLEMENT");
        }

    }


    private void openFileChoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            imageUri = data.getData();


            Picasso.get()
                    .load(imageUri)
                    .fit()
                    .into(imageUpload);
        }
    }

    public interface  DialogListener{
        void applyTexts(String iTitle,String iNumber,String iLevel,String iYear);
    }



    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
