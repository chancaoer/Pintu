package com.ce.pintu.wanjia.getsetjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ce.pintu.R;

import java.util.List;

public class paimingAdapter extends ArrayAdapter<zhanjitable>{
    private int resourceId;

    public paimingAdapter(Context context, int textViewResourceId, List<zhanjitable> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    //重写getview方法显示数据源，新建textview显示字符串
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        zhanjitable pm = getItem(position); //获取当前战绩实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);//false表示layout属性生效
        TextView paiming_shunxu = view.findViewById(R.id.paiming_shunxu);
        TextView paiming_wanjiaid = view.findViewById(R.id.paiming_wanjiaid);
        TextView paiming_level = view.findViewById(R.id.paiming_level);
        paiming_shunxu.setText(pm.getNO()+"");
        paiming_wanjiaid.setText(pm.getWanjiaId()+"");
        paiming_level.setText(pm.getLevel()+"");
        return view;
    }
}
