package com.example.mycontactos20232;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mycontactos20232.db.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Contacto> miscontactos = new ArrayList<Contacto>();
    public static int contador = 0;
    Button btniragregar,btnirlistar;
    public static DBHelper dbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu,menu);
        dbHelper = new DBHelper(this);
        miscontactos = dbHelper.obtenerTodosLosContactos();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.mnuiragregarcontactolista:
                Intent intent1 = new Intent(getApplicationContext(),AgregarContactoLista.class);
                startActivity(intent1);
            break;
            case R.id.mnuirListarContacto:
                Intent intent = new Intent(getApplicationContext(),ListarContacto.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btniragregar = (Button) findViewById(R.id.btnirAgregar);
        btnirlistar = (Button) findViewById(R.id.btnirListar);
        btniragregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AgregarContactoLista.class);
                startActivity(intent);
            }
        });
        btnirlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListarContacto.class);
                startActivity(intent);
            }
        });
    }
}