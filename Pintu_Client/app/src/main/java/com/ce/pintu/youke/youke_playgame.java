package com.ce.pintu.youke;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ce.pintu.MainActivity_94113;
import com.ce.pintu.R;
import com.ce.pintu.pintu_work.gameLayout;

public class youke_playgame extends AppCompatActivity{

    private ImageButton playgame_fanhui = null;
    private gameLayout gamepintu = null;
    private TextView level = null;
    private TextView timeleft = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        gamepintu = findViewById(R.id.gamepintu);
        level = findViewById(R.id.level);
        timeleft = findViewById(R.id.timeleft);
        playgame_fanhui = findViewById(R.id.playgame_fanhui);

        gamepintu.setTimeEnabled(true);
        gamepintu.setOnGamepintuListener(new gameLayout.GamepintuListener() {
            @Override
            public void nextLevel(final int nextlevel) {
                new AlertDialog.Builder(youke_playgame.this)
                        .setTitle("拼一拼就okay")
                        .setMessage("恭喜您！挑战成功！")
                        .setPositiveButton("继续挑战", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(gamepintu.nextLevel()) {
                                    level.setText("" + nextlevel);
                                }
                            }
                        }).setNegativeButton("放弃挑战", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(youke_playgame.this, MainActivity_94113.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }

            @Override
            public void timechanged(int currentTime) {
                timeleft.setText(""+currentTime);
            }

            @Override
            public void gameover() {
                new AlertDialog.Builder(youke_playgame.this)
                        .setTitle("拼一拼就okay")
                        .setMessage("哎呀，差一点就挑战成功了呢！")
                        .setPositiveButton("重新挑战", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gamepintu.restart();
                            }
                        }).setNegativeButton("放弃挑战", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(youke_playgame.this, MainActivity_94113.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }
        });

        playgame_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(youke_playgame.this, youke.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onPause(){
        super.onPause();
        gamepintu.pause();
    }

    protected void onResume() {
        super.onResume();
        gamepintu.resume();
    }
}
