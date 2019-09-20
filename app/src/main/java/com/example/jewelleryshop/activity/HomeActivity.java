package com.example.jewelleryshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.example.jewelleryshop.R;
import com.example.jewelleryshop.adapter.CategoryAdapter;
import com.example.jewelleryshop.databinding.ActivityHomeBinding;
import com.example.jewelleryshop.model.Category;
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

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference category;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<Category> categoryList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        init();
        getCategory();
    }

    private void init() {

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryList, this);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.categoryHomeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.categoryHomeRecyclerView.setAdapter(adapter);

    }


    private void getCategory() {
        // String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference catRef = category;
        catRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoryList.clear();


                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Category category = data.getValue(Category.class);
                        categoryList.add(category);
                        adapter.notifyDataSetChanged();
                    }
                    binding.categoryHomeRecyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
