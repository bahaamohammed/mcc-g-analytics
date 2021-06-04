package com.bahaadev.mcc_g_analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    AppAnalytics appAnalytics;
    final int[] timer = {0};
    CountDownTimer countDownTimer;

    RecyclerView recyclerView;
    ArrayList<ProductsModle> products;
    ProductsAdapter adapter;
    StorageReference storageRef;
    FloatingActionButton btnAddProduct;

    TextView tvTitle;
    ImageView ivImage;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

       storageRef = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvTitle);
        ivImage = findViewById(R.id.ivImage);

        btnAddProduct = findViewById(R.id.btnAddProduct);

        products = new ArrayList<>();

        Intent myIntent = getIntent();
         category = myIntent.getStringExtra("category");

        tvTitle.setText(category);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), AddProduct.class);
                myIntent.putExtra("category",category);

                startActivity(myIntent);
            }
        });



        CollectionReference productsRef = db.collection("products");
        Query productsDataQuery = productsRef.whereEqualTo("CatName", category);
        productsDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        ProductsModle productsModle = document.toObject(ProductsModle.class);
                        products.add(productsModle);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        initRecyclerView();

        appAnalytics = new AppAnalytics(getApplicationContext());
        appAnalytics.trackScreen(category + " Products","ProductActivity");

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer[0]++;
            }

            @Override
            public void onFinish() {
                timer[0] =0;
            }
        };
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProductsAdapter(products);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.OnMyClickListener(new ProductsAdapter.OnMyClickListener() {
            @Override
            public void OnItemClick(int position) {
                ProductsModle productsModle = products.get(position);
              /*  Toast.makeText(ProductActivity.this, "" + productsModle.getName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ProductActivity.this, "" + productsModle.getCatName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ProductActivity.this, "" + productsModle.getImage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ProductActivity.this, "" + productsModle.getDetails(), Toast.LENGTH_SHORT).show();*/

                storageRef.child("images").child(productsModle.getImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downUri = task.getResult();
                            String imageUrl = downUri != null ? downUri.toString() : null;

                            Intent myIntent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                            myIntent.putExtra("imageUrl",imageUrl);
                            myIntent.putExtra("Name",productsModle.getName());
                            myIntent.putExtra("CatName",productsModle.getCatName());
                            myIntent.putExtra("Details",productsModle.getDetails());

                            startActivity(myIntent);
                        }else{
                            Toast.makeText(ProductActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        appAnalytics.UserSpends(category + " Products","ProductActivity",String.valueOf(timer[0]));
        countDownTimer.onFinish();
    }
}