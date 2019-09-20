package com.example.jewelleryshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jewelleryshop.R;
import com.example.jewelleryshop.adapter.ProductAdapter;
import com.example.jewelleryshop.databinding.ActivityProductBinding;
import com.example.jewelleryshop.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private ActivityProductBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference category;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private EditText productNameET, productPriceET, productDiscount;
    private ImageView productImageCamera, productImageShow;
    private TextView btnUpload;
    private String categoryId, name, price, discount, imageUrl = "";
    private Uri saveUri;
    private List<Product> productList;
    private ProductAdapter adapter;
    private int discountAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product);

        init();
        categoryId = getIntent().getStringExtra("key");
        getProduct();
    }

    private void getProduct() {

        DatabaseReference productRef = category.child(categoryId).child("Product");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productList.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        productList.add(product);
                        adapter.notifyDataSetChanged();
                    }
                    binding.productRecyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void init() {

        productList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        adapter = new ProductAdapter(productList, this);
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.productRecyclerView.setAdapter(adapter);

    }
}
