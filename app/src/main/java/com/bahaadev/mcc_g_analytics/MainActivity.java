package com.bahaadev.mcc_g_analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    AppAnalytics appAnalytics;

    CountDownTimer countDownTimer;
    RecyclerView recyclerView;
    ArrayList<ItemModle> items;
    Adapter adapter;
    final int[] timer = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();


        items = new ArrayList<>();
        //categories

        CollectionReference questionsRef = db.collection("categories");
        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        ItemModle itemModle = document.toObject(ItemModle.class);
                        items.add(itemModle);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        initRecyclerView();





         appAnalytics = new AppAnalytics(getApplicationContext());
        appAnalytics.trackScreen("Categories","MainActivity");

        countDownTimer =  new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer[0]++;
               // Toast.makeText(MainActivity.this, "" + timer[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                timer[0] =0;

            }
        };


    }



    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rvCategories);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
         adapter = new Adapter(items);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.OnMyClickListener(new Adapter.OnMyClickListener() {
            @Override
            public void OnItemClick(int position) {
                ItemModle itemModle = items.get(position);

                Intent myIntent = new Intent(getApplicationContext(), ProductActivity.class);
                myIntent.putExtra("category",itemModle.getName());

                startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        appAnalytics.UserSpends("Categories","MainActivity",String.valueOf(timer[0]));
        countDownTimer.onFinish();
    }
}