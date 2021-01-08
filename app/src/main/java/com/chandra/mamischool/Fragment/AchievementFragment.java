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

import com.chandra.mamischool.ActivityStudent.StudentSchoolDetail;
import com.chandra.mamischool.Adapter.AdapterAchievement;
import com.chandra.mamischool.Adapter.AdapterFacilities;
import com.chandra.mamischool.Class.Achievement;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementFragment extends Fragment {

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
    ArrayList<Achievement> listAchievement = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchievementFragment newInstance(String param1, String param2) {
        AchievementFragment fragment = new AchievementFragment();
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_achievement, container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_achievement_student);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        StudentSchoolDetail activity = (StudentSchoolDetail) getActivity();
        userID = activity.getMyID();
        System.out.println("====================================="+userID);
        mDocRef = fStore.collection("schoolAchievment");
        mDocRef.whereEqualTo("id",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){

                        Achievement achievement = new Achievement();
                        achievement.setTitle( document.getData().get("title").toString());
                        achievement.setImgAchievement(document.get("imgAchievment").toString());
                        achievement.setLevel( document.getData().get("level").toString());
                        achievement.setNumber( document.getData().get("number").toString());
                        achievement.setYear( document.getData().get("year").toString());
                        listAchievement.add(achievement);
                    }
                    System.out.println("====================================FACILITIESONCREATEVIEW"+listAchievement.size());
                    System.out.println("====================================facilities"+listAchievement.size());
                    AdapterAchievement adapter = new AdapterAchievement(getContext(),listAchievement);
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