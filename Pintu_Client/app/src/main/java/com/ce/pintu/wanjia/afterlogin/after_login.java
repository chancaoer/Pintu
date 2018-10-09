package com.ce.pintu.wanjia.afterlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.ce.pintu.MainActivity_94113;
import com.ce.pintu.R;
import com.ce.pintu.wanjia.Delivery;
import com.ce.pintu.wanjia.login.login;
import com.ce.pintu.wanjia.query.paiming;
import com.ce.pintu.wanjia.query.zhanji;
import com.ce.pintu.wanjia.wanjia_play.wanjia_playgame;

public class after_login extends AppCompatActivity{

    private ImageButton wanjia_play = null;
    private ImageButton wanjia_zhanji = null;
    private ImageButton wanjia_paiming = null;
    private ImageButton afterlogin_fanhui = null;

    private SharedPreferences.Editor loginEditor = null;
    private SharedPreferences loginPreference = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanjia_afterlogin);

        wanjia_play = findViewById(R.id.wanjia_play);
        wanjia_zhanji = findViewById(R.id.wanjia_zhanji);
        afterlogin_fanhui = findViewById(R.id.afterlogin_fanhui);
        wanjia_paiming = findViewById(R.id.wanjia_paiming);

        wanjia_zhanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent lastintent = getIntent();
//                String name = lastintent.getStringExtra("login_id");
                Intent intent = new Intent(after_login.this,zhanji.class);
                String name = Delivery.getDeli_id();
                intent.putExtra("login_id",name);
                startActivity(intent);
                finish();
            }
        });

        wanjia_paiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(after_login.this,paiming.class);
                String name = Delivery.getDeli_id();
                intent.putExtra("login_id",name);
                startActivity(intent);
                finish();
            }
        });

        afterlogin_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(after_login.this,MainActivity_94113.class);
                startActivity(intent);
                finish();
            }
        });

        wanjia_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent lastintent = getIntent();
//                String name = lastintent.getStringExtra("login_id");
                Intent intent = new Intent(after_login.this,wanjia_playgame.class);
                String name = Delivery.getDeli_id();
                intent.putExtra("login_id",name);
                startActivity(intent);
                finish();
            }
        });
    }

    //加入了右上角的小菜单：注销和退出;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.zhuxiao:
                //把登陆界面所有东西清空;
                loginPreference = getSharedPreferences("userlogininfo", MODE_PRIVATE);
                loginEditor = loginPreference.edit();
                loginEditor.putBoolean("isAutoLogin",false);
                loginEditor.commit();  //改完要记得提交;
                Intent intent = new Intent(after_login.this,login.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
