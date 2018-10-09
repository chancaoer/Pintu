package com.ce.pintu.pintu_work;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

//传入bitmap,切成piece * piece 块，返回List<imagePiece>
public class imageCutter {
    public static List<imagePiece> cutImage(Bitmap bitmap, int piece){

        List<imagePiece> pieces = new ArrayList<imagePiece>();//将切好的块存储在集合pieces里

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width,height)/piece;//取图片的宽高最小值/piece做小块的宽

        //开始切割，第一次循环切第一行，第二次循环切第二行，以此类推
        for(int i=0;i<piece;i++){ //i=0，即第0行
            for(int j=0;j<piece;j++){//j为每一行的小块的位置
                imagePiece ipiece = new imagePiece();
                ipiece.setIndex(j+i*piece);//降维，记下图片顺序
                //获得切割出来的小块在原图的x，y坐标
                int x = j*pieceWidth;
                int y = i*pieceWidth;
                //利用Bitmap的createBitmap方法和双重循环实现图片的分割复制
                ipiece.setBitmap(Bitmap.createBitmap(bitmap,x,y,pieceWidth,pieceWidth));//x,y表示顶点坐标
                pieces.add(ipiece);//把切好的小块存入pieces
            }
        }
        return pieces;
    }
}
