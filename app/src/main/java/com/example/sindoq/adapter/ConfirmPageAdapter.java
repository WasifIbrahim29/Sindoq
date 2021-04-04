package com.example.sindoq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.R;
import com.example.sindoq.model.AppListMain;

import java.util.ArrayList;
import java.util.List;

public class ConfirmPageAdapter extends RecyclerView.Adapter<ConfirmPageAdapter.ViewHolder> {

    ArrayList<AppListMain> appListMainArrayList;
    AppListMain appListMain;
    Context context;
    DatabaseHelper databaseHelper;

    public ConfirmPageAdapter(Context context, ArrayList<AppListMain> appListMainArrayList) {
        this.context = context;
        this.appListMainArrayList = appListMainArrayList;
        this.databaseHelper=new DatabaseHelper(context);
    }

    @Override
    public ConfirmPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_row, parent, false);
        return new ConfirmPageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ConfirmPageAdapter.ViewHolder holder, int position) {
        try {
            appListMain = appListMainArrayList.get(position);
            //holder.ivAppIcon.setImageDrawable(appListMainArrayList.get(position).getAppIcon());
            holder.tvAppLabel.setText(appListMainArrayList.get(position).getAppName());
            holder.tvAppPackage.setText(appListMainArrayList.get(position).getAppPackage());
            holder.ivAppIcon.setImageDrawable(appListMainArrayList.get(position).getAppIcon());
            if(databaseHelper.CHeckIfAppExistsInUnblocked(appListMainArrayList.get(position).getAppName().toString()))
            {
                holder.checkbox.setChecked(true);
            }
            else {
                holder.checkbox.setChecked(false);
            }
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
        public CheckBox checkbox;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAppIcon = view.findViewById(R.id.ivAppIcon);
            tvAppLabel = view.findViewById(R.id.tvAppLabel);
            tvAppPackage = view.findViewById(R.id.tvAppPackage);
            checkbox=view.findViewById(R.id.checkbox);
        }
    }

    public void updateList(ArrayList<AppListMain> appListMainArrayList,int position) {
        appListMainArrayList.remove(position);
        this.appListMainArrayList = appListMainArrayList;
        notifyDataSetChanged();
    }
}
