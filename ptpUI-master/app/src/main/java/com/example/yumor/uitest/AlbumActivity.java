package com.example.yumor.uitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private Photo[] photos = {new Photo("pic1","pic1"), new Photo("pic2", "pic2"), new Photo("pic3", "pic3"),
    new Photo("pic4", "pic4"), new Photo("pic5", "pic5")};

    private List<Photo> photoList = new ArrayList<>();
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initPhotos();
        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapter = new PhotoAdapter(photoList);
        recyclerView.setAdapter(adapter);

    }
    private void initPhotos() {
        photoList.clear();
        photoList.addAll(Arrays.asList(photos).subList(0, 5));
    }
}

