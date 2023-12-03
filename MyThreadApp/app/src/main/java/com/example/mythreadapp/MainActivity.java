package com.example.mythreadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btnView1;
    TextView tv1, tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnView1 = findViewById(R.id.buttonView1);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        tv1.setText("");
        tv2.setText("");

        btn1.setOnClickListener(v -> {
            printing(100, tv1);
        });

        btn2.setOnClickListener(v -> {
            printing(300, tv2);
        });

        btnView1.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        });
    }

    public void printing(int delay, TextView tv){
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(delay);
                    runOnUiThread(() -> {
                        tv.append("X");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}