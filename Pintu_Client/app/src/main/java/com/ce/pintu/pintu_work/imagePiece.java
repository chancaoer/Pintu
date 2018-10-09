package com.ce.pintu.pintu_work;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

//该类定义将图片切成piece * piece的块，要传入要切的bitmap参数
public class imagePiece {
    private int index; //定义图片在原来完整图片中的固定位置index
    private Bitmap bitmap;  //用bitmap加载图片

    public imagePiece(){

    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

}
