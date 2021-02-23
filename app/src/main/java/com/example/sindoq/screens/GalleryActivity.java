package com.example.sindoq.screens;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sindoq.R;
import com.example.sindoq.adapter.WallpaperListAdapter;
import com.example.sindoq.global.Constants;
import com.example.sindoq.listener.RecyclerItemClickListener;
import com.example.sindoq.model.WallpaperMain;
import com.example.sindoq.ui.GridSpacingItemDecoration;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    RecyclerView rvWallpaper;
    String urlsArray[] = {
            "https://i.imgur.com/CFwbeku.jpg",
            "https://i.imgur.com/SedIPiP.png",
            "https://i.imgur.com/wpfLPa7.jpg",
            "https://i.imgur.com/XyaGfCx.jpg",
            "https://i.imgur.com/YrVx0mW.jpg"
    };

    WallpaperListAdapter wallpaperListAdapter;
    ArrayList<WallpaperMain> wallpaperMainArrayList;
    WallpaperMain wallpaperMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        loadWallpaper();
    }

    public void loadWallpaper() {
        int mColumnCount = 2;
        rvWallpaper = findViewById(R.id.rvWallpaper);
        rvWallpaper.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        rvWallpaper.addItemDecoration(new GridSpacingItemDecoration(10)); // 16px. In practice, you'll want to use getDimensionPixelSize
        wallpaperMainArrayList = new ArrayList<>();

        for (int i = 0; i < urlsArray.length; i++) {
            wallpaperMainArrayList.add(new WallpaperMain(urlsArray[i]));
        }

        wallpaperListAdapter = new WallpaperListAdapter(this, wallpaperMainArrayList);
        rvWallpaper.setAdapter(wallpaperListAdapter);
        rvWallpaper.addOnItemTouchListener(new RecyclerItemClickListener(this, rvWallpaper, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                wallpaperMain = wallpaperMainArrayList.get(position);
                if (wallpaperMain != null) {

                    Intent wallpaperIntent = new Intent();
                    wallpaperIntent.putExtra(Constants.INTENT_WALLPAPER, wallpaperMain.getImgUrl());
                    setResult(RESULT_OK, wallpaperIntent);
                    finish();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }
}
