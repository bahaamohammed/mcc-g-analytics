package com.bahaadev.mcc_g_analytics;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


public class AddProduct extends AppCompatActivity {

    AppAnalytics appAnalytics;
    final int[] timer = {0};
    CountDownTimer countDownTimer;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri filePath;
    StorageReference storageRef;
    Button btnAddImage,btnSave;
    ImageView ivImage;
    TextView tvName,tvCatName,tvDetails,tvImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnAddImage = findViewById(R.id.btnAddImage);
        btnSave = findViewById(R.id.btnSave);

        ivImage = findViewById(R.id.ivImage);

        tvName = findViewById(R.id.tvName);
        tvCatName = findViewById(R.id.tvCatName);
        tvDetails = findViewById(R.id.tvDetails);
        tvImageName = findViewById(R.id.tvImageName);

        Intent myIntent = getIntent();
        String category = myIntent.getStringExtra("category");
        tvCatName.setText(category);

        /*addFirstData("Laptop HP","Electronic");
        addFirstData("Iphone X","Electronic");
        addFirstData("Smart TV","Electronic");*/

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] parts = tvDetails.getText().toString().split(",");
                int count = 0;
                String key = "";
                Map<String,Object> details = new HashMap<>();
                for (String part : parts) {
                    count++;
                    if (count == 1){
                        key = part;
                    }else{
                        details.put(key,part);
                        count = 0;
                    }
                }


                addData(tvName.getText().toString(),tvCatName.getText().toString(),tvImageName.getText().toString(),details);



            }
        });

        appAnalytics = new AppAnalytics(getApplicationContext());
        appAnalytics.trackScreen("Add Product","AddProduct");

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

    void addData(String productName,String catName,String imageName, Map<String,Object> details){
        // String productPrice = "";

        Map<String,Object> product = new HashMap<>();
        product.put("CatName",catName);
        product.put("Name",productName);
        product.put("Image",imageName);
        product.put("details",details);


        db.collection("products").add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddProduct.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProduct.this, "Fail to Add Data", Toast.LENGTH_SHORT).show();
                    }
                });

        uploadImage(imageName);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null && data.getData() != null){
                            filePath = data.getData();
                            ivImage.setImageURI(filePath);
                            Toast.makeText(AddProduct.this, "filePath : "+ filePath, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);

    }
    void uploadImage(String imgName){
        StorageReference imgReferance = storageRef.child("images/"+imgName);
        imgReferance.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddProduct.this, "Image Added Successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProduct.this, "Fail to Add Image", Toast.LENGTH_SHORT).show();

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
        appAnalytics.UserSpends("Add Product","AddProduct",String.valueOf(timer[0]));
        countDownTimer.onFinish();
    }
}