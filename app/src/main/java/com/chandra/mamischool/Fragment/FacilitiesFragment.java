package com.chandra.mamischool.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandra.mamischool.ActivityAdmin.AdminFacilities;
import com.chandra.mamischool.ActivityStudent.StudentSchoolDetail;
import com.chandra.mamischool.Adapter.AdapterFacilities;
import com.chandra.mamischool.Class.Facilities;
import com.chandra.mamischool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacilitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference mDocRef;
    FloatingActionButton fabFacilities;
    String userID;
    ArrayList<Facilities> listFacilities = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FacilitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FacilitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FacilitiesFragment newInstance(String param1, String param2) {
        FacilitiesFragment fragment = new FacilitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_facilities, container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_facilities_student);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        StudentSchoolDetail activity = (StudentSchoolDetail) getActivity();
        userID = activity.getMyID();
        System.out.println("====================================="+userID);
        mDocRef = fStore.collection("schoolFacilities");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        System.out.println("======================="+document.getData().get("title"));
                        Facilities facility = new Facilities();
                        facility.setTitle( document.getData().get("title").toString());
                        facility.setImg(document.get("imgAchievment").toString());
                        listFacilities.add(facility);
                    }
                    System.out.println("====================================FACILITIESONCREATEVIEW"+listFacilities.size());
                    System.out.println("====================================facilities"+listFacilities.size());
                    AdapterFacilities adapter = new AdapterFacilities(getContext(),listFacilities);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);


                    return;

                }else{
                    System.out.println("==================================GAGAL");
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;

    }
}
