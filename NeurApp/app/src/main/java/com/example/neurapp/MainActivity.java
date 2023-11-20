package com.example.neurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neurapp.api.DataAPI;
import com.example.neurapp.api.RetrofitData;
import com.google.android.material.chip.Chip;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    Chip chip1, chip2,chip3,chip4,chip5,chip6,chip7,chip8;
    TextView tx;
    String result;
    Button btnPredict;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chip1 = findViewById(R.id.chip);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);

        tx = findViewById(R.id.textView2);

        btnPredict = findViewById(R.id.button);

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predict();
            }
        });
    }


    public void predict(){
        Retrofit r = RetrofitData.getInstance();
        DataAPI dataAPI = r.create(DataAPI.class);
        int x1,x2,x3,x4,x5,x6,x7,x8;
        x1 = chip1.isChecked() ? 1:0;
        x2 = chip2.isChecked() ? 1:0;
        x3 = chip3.isChecked() ? 1:0;
        x4 = chip4.isChecked() ? 1:0;
        x5 = chip5.isChecked() ? 1:0;
        x6 = chip6.isChecked() ? 1:0;
        x7 = chip7.isChecked() ? 1:0;
        x8 = chip8.isChecked() ? 1:0;
        try {
            Call<ResponseBody> call = dataAPI.getPredict(x1, x2, x3, x4, x5, x6, x7, x8);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful())
                        return;
                    try {
                        result = response.body().string();
                        tx.setText(result);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "pues esto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "pues no c", Toast.LENGTH_SHORT).show();
        }
        
    }
}