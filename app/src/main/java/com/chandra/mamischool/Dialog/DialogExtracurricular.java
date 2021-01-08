package com.chandra.mamischool.Dialog;

import android.app.Activity;
import android.app.Dialog;
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

public class DialogExtracurricular extends AppCompatDialogFragment {
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
    DialogListener listener;


    public DialogExtracurricular(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_extracurricular,null);
        btnChooseImage = view.findViewById(R.id.btn_choose_img_extracurricular);
        imageUpload = view.findViewById(R.id.img_thumbnail_extracurricular);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imgExtracurricular");

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        cpTitle = view.findViewById(R.id.extracurriculer_title_dialog);
        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String iTitle = cpTitle.getText().toString();
                if (TextUtils.isEmpty(iTitle)) {
                    cpTitle.setError("Level Required");
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

                                    CollectionReference documentReference = fStore.collection("schoolExtracurriculer");
                                    final Map<String, Object> extracurriler = new HashMap<>();
                                    extracurriler.put("id", userID);
                                    extracurriler.put("title", iTitle);
                                    extracurriler.put("imgExtracurricular", uri.toString());
                                    documentReference.add(extracurriler).addOnSuccessListener( new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            System.out.println("========================SUKSES");
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
        return builder.create();


    }

    private void openFileChoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement");
        }
    }
}
