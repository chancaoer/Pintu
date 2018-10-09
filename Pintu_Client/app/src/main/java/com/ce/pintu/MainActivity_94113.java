package com.ce.pintu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ce.pintu.wanjia.model_wanjia.wanjia;
import com.ce.pintu.youke.youke;

//显示主界面
public class MainActivity_94113 extends AppCompatActivity {

    private ImageButton model_youke = null;
    private ImageButton model_wanjia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_94113);

        model_youke = findViewById(R.id.model_youke);
        model_wanjia = findViewById(R.id.model_wanjia);

        model_youke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_94113.this,youke.class);//跳转到youke_fanhui_94113界面;
                startActivity(intent);
                finish();
            }
        });

        model_wanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setContentView(R.layout.model_wanjia_94113);//仅仅是在mainactivity里加载了wanjia界面，并没有跳转出去，所以不能对界面进行操作
                Intent intent = new Intent(MainActivity_94113.this,wanjia.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
