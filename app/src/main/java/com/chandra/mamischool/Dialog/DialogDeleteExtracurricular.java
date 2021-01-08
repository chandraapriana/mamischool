package com.chandra.mamischool.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DialogDeleteExtracurricular extends AppCompatDialogFragment {

    private StorageReference mStorageRef;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Activity activity;
    public Integer SUCCESS_UPLOAD=0;
    CollectionReference mDocRef;
    private static final int PICK_IMAGE_REQUEST=2;
    EditText cpTitle, cpBody;

    String header;

    public DialogDeleteExtracurricular(Activity activity){
        this.activity = activity;
    }
    public DialogDeleteExtracurricular(){}

    public interface  DialogListener{
        void applyTexts(String iTitle, String iNumber, String iLevel, String iYear);
    }
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            listener = (DialogDeleteDescription.DialogListener) context;
//        }catch (ClassCastException e){
//            throw  new ClassCastException(context.toString()+"must implement");
//        }
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete,null);

        cpTitle = view.findViewById(R.id.description_title_dialog);
        cpBody = view.findViewById(R.id.description_body_dialog);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imgExtracurricular");

        if (getArguments() != null) {
            header = getArguments().getString("header","");
        }

        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                System.out.println("==================== desc id   "+header);
                DocumentReference noteRef = fStore.collection("schoolExtracurriculer").document(header);
                noteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            System.out.println("Successfull");

                        }else{

                        }
                    }
                });
            }
        });



        return builder.create();
    }


}
