package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainReporte extends AppCompatActivity {
    TextView lblreporte;
    Button btnregresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reporte);
        lblreporte = (TextView) findViewById(R.id.lblreporte);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            String mensaje = (String)b.get("mensaje");
            String otromensaje = (String)b.get("otromensaje");
            lblreporte.setText(mensaje+otromensaje);
        }
        btnregresar = (Button)findViewById(R.id.btnregresar);
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}