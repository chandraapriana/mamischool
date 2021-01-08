package com.chandra.mamischool.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class DialogDescription  extends AppCompatDialogFragment {

    private StorageReference mStorageRef;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Activity activity;
    public Integer SUCCESS_UPLOAD=0;

    private static final int PICK_IMAGE_REQUEST=2;
    EditText cpTitle, cpBody,cpLevel,cpYear;
    ImageView imageUpload;
    Button btnChooseImage;
    Uri imageUri;
    DialogListener listener;

    public DialogDescription(Activity activity){
        this.activity = activity;
    }

    public interface  DialogListener{
        void applyTexts(String iTitle,String iNumber,String iLevel,String iYear);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogDescription.DialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_description,null);

        cpTitle = view.findViewById(R.id.description_title_dialog);
        cpBody = view.findViewById(R.id.description_body_dialog);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imgExtracurricular");

        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String iTitle = cpTitle.getText().toString();
                final String iBody = cpBody.getText().toString();

                if (TextUtils.isEmpty(iTitle)) {
                    cpTitle.setError("Title Required");
                    return;
                }
                if (TextUtils.isEmpty(iBody)) {
                    cpBody.setError("Body Required");
                    return;
                }

                String userID = fAuth.getCurrentUser().getUid();

                CollectionReference documentReference = fStore.collection("schoolDescription");
                final Map<String, Object> extracurriler = new HashMap<>();
                extracurriler.put("id", userID);
                extracurriler.put("title", iTitle);
                extracurriler.put("body", iBody);
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
                Intent intent = new Intent(activity, AdminHome.class);
                startActivity(intent);

            }
        });



        return builder.create();
    }
}
