package com.ce.pintu.wanjia.getsetjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.ce.pintu.R;

import java.util.List;

public class zhanjiAdapter extends ArrayAdapter<zhanjitable> {

    private int resourceId;

    public zhanjiAdapter(Context context, int textViewResourceId, List<zhanjitable> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    //重写getview方法显示数据源，新建textview显示字符串
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        zhanjitable zj = getItem(position); //获取当前战绩实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);//false表示layout属性生效
        TextView zhanji_wanjiaId = view.findViewById(R.id.zhanji_wanjiaid);
        TextView zhanji_level = view.findViewById(R.id.zhanji_level);
        TextView zhanji_playtime = view.findViewById(R.id.zhanji_playtime);
        zhanji_wanjiaId.setText(zj.getWanjiaId()+"");
        zhanji_level.setText(zj.getLevel()+"");
        zhanji_playtime.setText(zj.getPlayTime());
        return view;
    }
}
