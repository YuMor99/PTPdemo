package com.example.yumor.uitest;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YuMor on 2018/7/7.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context mContext;

    private List<Photo> mPhotoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView PhotoImage;
        TextView PhotoName;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            PhotoImage = (ImageView) view.findViewById(R.id.photo_image);
            PhotoName = (TextView) view.findViewById(R.id.photo_name);
        }
    }

    public PhotoAdapter(List<Photo> photoList){
        mPhotoList = photoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_photos, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Photo p = mPhotoList.get(position);
        viewHolder.PhotoName.setText(p.getName());
        viewHolder.PhotoImage.setImageDrawable(mContext.getDrawable(p.getImageResourceId(mContext)));
    }

    @Override
    public int getItemCount()
    {
        // 返回数据总数
        return mPhotoList == null ? 0 : mPhotoList.size();
    }

}
