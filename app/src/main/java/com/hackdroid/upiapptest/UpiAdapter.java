package com.hackdroid.upiapptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpiAdapter extends RecyclerView.Adapter<UpiAdapter.ViewHolder> {
    Activity activity;
    Context context;
    List<ResolveInfo> list;
    Intent intent;

    public UpiAdapter(Activity activity, Context context, List<ResolveInfo> list, Intent intent) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        this.intent = intent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_layout_upi, parent, false);

        return new UpiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ResolveInfo info = list.get(position);

        String name = String.valueOf(info.loadLabel(context.getPackageManager()));
        final Drawable icon = info.loadIcon(context.getPackageManager());
        // holder.bind(name, icon);
        holder.textView.setText(name);
        holder.imageView.setImageDrawable(icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setPackage(info.activityInfo.packageName);
                //;
                try {
                   activity.startActivityForResult(intent, 10001);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.upiName);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
