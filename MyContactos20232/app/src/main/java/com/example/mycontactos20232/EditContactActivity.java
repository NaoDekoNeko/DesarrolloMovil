package com.example.mycontactos20232;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditContactActivity extends AppCompatActivity {
    EditText txtnombre2,txtalias2;
    Spinner spntipo2;
    Button btneditarcontacto,btncancelar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto_listado);
        txtnombre2 = (EditText)findViewById(R.id.txtnombre2);
        txtalias2 = (EditText)findViewById(R.id.txtalias2);
        spntipo2 = (Spinner) findViewById(R.id.spntipo2);
        btneditarcontacto = (Button)findViewById(R.id.btneditarcontacto);
        btncancelar2 = (Button)findViewById(R.id.btncancelar2);
        int contact_position = getIntent().getIntExtra("contact_position", -1);
        // Encuentra el contacto existente y actualiza sus detalles
        Contacto c = MainActivity.miscontactos.get(contact_position);
        txtnombre2.setText(c.getNombre());
        txtalias2.setText(c.getAlias());
        btneditarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre2.getText().toString();
                String alias = txtalias2.getText().toString();
                int tipo = spntipo2.getSelectedItemPosition();

                int resultado = MainActivity.dbHelper.actualizarContacto(c.getIdcontacto(), nombre, alias, tipo);

                if (resultado > 0) {
                    c.setNombre(nombre);
                    c.setAlias(alias);
                    c.setTipo(tipo);

                    String mensaje ="Contacto Actualizado";
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),ListarContacto.class);
                    startActivity(intent);
                } else {
                    String mensaje ="Error al actualizar contacto";
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncancelar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
