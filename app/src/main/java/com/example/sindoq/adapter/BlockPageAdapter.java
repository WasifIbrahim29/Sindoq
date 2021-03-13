package com.example.sindoq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.R;
import com.example.sindoq.model.AppListMain;

import java.util.ArrayList;
import java.util.List;


import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class BlockPageAdapter extends RecyclerView.Adapter<BlockPageAdapter.ViewHolder> {

    ArrayList<AppListMain> appListMainArrayList;
    AppListMain appListMain;
    Context context;
    DatabaseHelper databaseHelper;
    private PackageManager packageManager;


    public BlockPageAdapter(Context context, ArrayList<AppListMain> appListMainArrayList) {
        this.context = context;
        this.appListMainArrayList = appListMainArrayList;
        this.databaseHelper=new DatabaseHelper(context);
    }

    @Override
    public BlockPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_row1, parent, false);
        return new BlockPageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BlockPageAdapter.ViewHolder holder, int position) {
        try {
            appListMain = appListMainArrayList.get(position);
           // holder.ivAppIcon.setImageDrawable(GetAppIcOn(appListMainArrayList.get(position).getAppName().toString()));
           // holder.ivAppIcon.setImageDrawable(appListMainArrayList.get(position).getAppIcon());
            holder.tvAppLabel.setText(appListMainArrayList.get(position).getAppName());
            holder.tvAppPackage.setText(appListMainArrayList.get(position).getAppPackage());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return appListMainArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivAppIcon;
        public TextView tvAppLabel, tvAppPackage;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAppIcon = view.findViewById(R.id.ivAppIcon1);
            tvAppLabel = view.findViewById(R.id.tvAppLabel1);
            tvAppPackage = view.findViewById(R.id.tvAppPackage1);

        }
    }

    public void updateList(ArrayList<AppListMain> appListMainArrayList,int position) {
        appListMainArrayList.remove(position);
        this.appListMainArrayList = appListMainArrayList;
        notifyDataSetChanged();
    }

    public Drawable GetAppIcOn(String AppName) {
        try {

            packageManager=context.getPackageManager();
            appListMainArrayList = new ArrayList<>();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

            for (ResolveInfo resolveInfo : resolveInfoList) {
                AppListMain appListMain = new AppListMain();
                appListMain.setAppIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                appListMain.setAppName(resolveInfo.loadLabel(packageManager).toString());
                appListMain.setAppPackage(resolveInfo.activityInfo.packageName);
                appListMainArrayList.add(appListMain);

                if(resolveInfo.loadLabel(packageManager).toString().equals(AppName))
                {
                    return resolveInfo.activityInfo.loadIcon(packageManager);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
