package com.ce.pintu.youke;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.ce.pintu.MainActivity_94113;
import com.ce.pintu.R;

public class youke extends AppCompatActivity{

    private ImageButton youke_play = null;
    private ImageButton youke_fanhui = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_youke);

        youke_play = findViewById(R.id.youke_play);
        youke_fanhui = findViewById(R.id.youke_fanhui);

        youke_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(youke.this, MainActivity_94113.class);
                startActivity(intent);
                finish();
            }
        });

        youke_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(youke.this,youke_playgame.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
