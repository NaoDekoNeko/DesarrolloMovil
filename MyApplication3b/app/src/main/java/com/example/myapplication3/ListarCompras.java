package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class ListarCompras extends AppCompatActivity {
    ListView lstvcompras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_compras);
        lstvcompras = (ListView) findViewById(R.id.lstvcompras);
        Adaptador adaptador = new Adaptador(getApplicationContext(),MainActivity.compras);
        lstvcompras.setAdapter(adaptador);
    }
}