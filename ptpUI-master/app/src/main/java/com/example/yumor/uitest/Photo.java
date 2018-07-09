package com.example.yumor.uitest;

import android.content.Context;

public class Photo{

    String name;

    String picName;

    public Photo(String name, String picName){
        this.name = name;
        this.picName = picName;
    }

    public String getName(){
        return name;
    }
    public int getImageResourceId( Context context )
    {
        try
        {
            return context.getResources().getIdentifier(this.picName, "drawable", context.getPackageName());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

}
