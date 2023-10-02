package com.example.mycontactos20232;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AgregarContactoLista extends AppCompatActivity {
    EditText txtnombre,txtalias;
    Spinner spntipo;
    Button btnagregarcontacto,btncancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto_lista);
        txtnombre = (EditText)findViewById(R.id.txtnombre);
        txtalias = (EditText)findViewById(R.id.txtalias);
        spntipo = (Spinner) findViewById(R.id.spntipo);
        btnagregarcontacto = (Button)findViewById(R.id.btnAgregarContacto);
        btncancelar = (Button)findViewById(R.id.btncancelar);
        btnagregarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre.getText().toString();
                String alias = txtalias.getText().toString();
                int tipo = spntipo.getSelectedItemPosition();
                String mensaje;
                if(!nombre.isEmpty()){
                    long id = MainActivity.dbHelper.insertarContacto(nombre, alias, tipo);
                    if (id != -1) {
                        Contacto c = new Contacto((int) id, nombre, alias, tipo);
                        MainActivity.miscontactos.add(c);
                        mensaje ="Contacto Agregado";
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                        txtnombre.setText("");
                        txtalias.setText("");
                        Intent intent = new Intent(getApplicationContext(),ListarContacto.class);
                        startActivity(intent);
                    } else {
                        mensaje ="Error al agregar contacto";
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    mensaje ="Ingrese un nombre";
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                }
            }
        });

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}